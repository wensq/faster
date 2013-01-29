/*
 * Copyright (c) 2013 @iSQWEN. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.faster.cache;

import net.rubyeye.xmemcached.MemcachedClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.faster.commons.Encrypts;
import org.faster.commons.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Cache 操作接口的 Memcached 实现
 *
 * @author sqwen
 */
public class MemcachedService implements CacheService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final int MAX_KEY_SIZE = 255;

	protected MemcachedClient memcachedClient;

	private long getTimeout = 10000;

	// 是否需要对key进行hash处理
	private boolean hashKey = true;

	public void setGetTimeout(long getTimeout) {
		this.getTimeout = getTimeout;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public void setHashKey(boolean hashKey) {
		this.hashKey = hashKey;
	}

	@Override
	public String buildInternalKey(String outerKey) {
		return hashKey || outerKey.length() > MAX_KEY_SIZE ? Encrypts.encryptByMD5(outerKey) : outerKey;
	}

	@Override
	public void putInCache(String key, int expiration, Object obj) {
		key = buildInternalKey(key);
		try {
			memcachedClient.set(key, expiration, obj);
		} catch (Exception e) {
			log.error("Putting [" + key + "] to Memcached failed: " + e.getMessage(), e);
            throw Exceptions.unchecked(e);
		}
	}

	@Override
	public Object getFromCache(String key, int expiration, CacheMissAction cma) {
		if (expiration < 0) {
			return cma.doFind();
		}

		String newKey = buildInternalKey(key);
		log.debug("Memcached key: " + key + " -> " + newKey);
		try {
			StopWatch sw = null;
			if (log.isDebugEnabled()) {
				sw = new StopWatch();
				sw.start();
				log.debug("Getting from Memcached[key=" + newKey + "]...");
			}

			Object ret = memcachedClient.get(newKey, getTimeout);
			if (ret != null) {
				if (log.isDebugEnabled()) {
					log.debug("Memcached hit[key=" + newKey + "] (" + sw.getTime() + " ms)");
				}
				return ret;
			}

			if (cma == null) {
				throw new IllegalArgumentException("CacheMissAction should be provided!");
			}

			if (log.isDebugEnabled()) {
				log.debug("Memcached miss[key=" + key + "], direct search...");
			}

			synchronized (this) {
				ret = memcachedClient.get(newKey, getTimeout);
				if (ret != null) {
					if (log.isDebugEnabled()) {
						log.debug("Memcached hit during double check. (" + sw.getTime() + " ms)");
					}
					return ret;
				}

				ret = cma.doFind();
				if (ret != null) {
					memcachedClient.set(newKey, expiration, ret);
				}
			}

			if (log.isDebugEnabled()) {
				log.debug("Direct search completed[found=" + (ret != null) + "]. (" + sw.getTime() + " ms)");
			}
			return ret;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void flush(String key) {
		StopWatch sw = new StopWatch();
		sw.start();
		String newKey = buildInternalKey(key);
		try {
			memcachedClient.delete(newKey);
			log.info("Key[" + key + "] flushed. (" + sw.getTime() + " ms)");
		} catch (Exception e) {
			log.error("Delete cache failed[key=" + key + "]!", e);
		}
	}

	@Override
	public void flush(List<String> keys) {
		if (keys == null || keys.isEmpty()) {
			return;
		}

		StopWatch sw = new StopWatch();
		sw.start();
		for (String key : keys) {
			flush(key);
		}
		String allKey = StringUtils.join(keys, ",");
		log.info(keys.size() + " keys[" + allKey + "] flushed. (" + sw.getTime() + " ms)");
	}

}
