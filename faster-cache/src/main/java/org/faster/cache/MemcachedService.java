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
import org.apache.commons.lang3.time.StopWatch;
import org.faster.util.Exceptions;

import java.util.List;

/**
 * Cache 操作接口的 Memcached 实现
 *
 * @author sqwen
 */
public class MemcachedService extends AbstractCacheService {

    public static final int MAX_KEY_SIZE = 255;

	private MemcachedClient memcachedClient;

	private long getTimeout = 10000;

	public void setGetTimeout(long getTimeout) {
		this.getTimeout = getTimeout;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

    @Override
    protected int getMaxKeySize() {
        return MAX_KEY_SIZE;
    }

    @Override
	protected void doPutInCache(String key, int expiration, Object obj) {
		try {
			memcachedClient.set(key, expiration, obj);
		} catch (Exception e) {
			log.error("Putting [" + key + "] to Memcached failed: " + e.getMessage(), e);
            throw Exceptions.unchecked(e);
		}
	}

	@Override
	protected Object doGetFromCache(String internalKey, int expiration, CacheMissHandler handler) {
		try {
			StopWatch sw = null;
			if (log.isDebugEnabled()) {
				sw = new StopWatch();
				sw.start();
				log.debug("Getting from Memcached[key={}]...", internalKey);
			}

			Object ret = memcachedClient.get(internalKey, getTimeout);
			if (ret != null) {
				if (log.isDebugEnabled()) {
					log.debug("Memcached hit[key={}]. ({} ms)", internalKey, sw.getTime());
				}
				return ret;
			}

            return directSearch(internalKey, expiration, handler);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

    @Override
    protected void doFlush(String key) throws Exception {
        memcachedClient.delete(key);
    }

    @Override
    protected void doFlush(List<String> internalKeys) throws Exception{
        for (String key : internalKeys) {
            doFlush(key);
        }
    }

}
