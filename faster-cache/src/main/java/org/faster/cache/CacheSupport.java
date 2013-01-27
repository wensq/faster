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

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缓存容器
 *
 * @author sqwen
 */
public class CacheSupport<T> {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected final String KEY_PREFIX = getClass().getCanonicalName();

	public static final int DAY = 24 * 60 * 60;

	public static final int HOUR = 60 * 60;

	public static final int MINUTE = 60;

	protected CacheService cacheService;

	protected String buildGlobalCacheKey(String localKey) {
		return getKeyPrefix() + "-" + localKey;
	}

	protected final String getKeyPrefix() {
		String keyPrefix = getCacheGroup();
		return isBlank(keyPrefix) ? KEY_PREFIX : keyPrefix;
	}

	protected String getCacheGroup() {
		return null;
	}

	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	public void flushCache(String key) {
		cacheService.flush(key);
	}

	public void flushCache(List<String> keys) {
		cacheService.flush(keys);
	}

	public void putInCache(String key, int expiration, Object obj) {
		cacheService.putInCache(key, expiration, obj);
	}

	public T findFromCache(String key, CacheMissAction cma) {
		return findFromCache(key, 0, cma);
	}

	public T findFromCache(String key, CacheStrategy cs, CacheMissAction cma) {
		return findFromCache(key, cs, 0, cma);
	}

	public Object findObjectFromCache(String key, CacheMissAction cma) {
		return findObjectFromCache(key, 0, cma);
	}

	public Object findObjectFromCache(String key, CacheStrategy cs, CacheMissAction cma) {
		return findObjectFromCache(key, cs, 0, cma);
	}

	public Object findObjectFromCache(String key, int expiration, CacheMissAction cma) {
		return cacheService.getFromCache(key, expiration, cma);
	}

	public Object findObjectFromCache(String key, CacheStrategy cs, int expiration, CacheMissAction cma) {
		switch (cs) {
		case FLUSH:
			flushCache(key);
			log.info("Flushed key: " + key);
			return null;
		case NO:
			return cma.doFind();
		case YES:
			return findObjectFromCache(key, expiration, cma);
		case REBUILD:
			log.info("Rebuilding cache: " + key);
			Object ret = cma.doFind();
			if (ret != null) {
				putInCache(key, expiration, ret);
			} else {
				flushCache(key);
			}
			return ret;
		default:
			throw new IllegalArgumentException("Unknown CacheStrategy: " + cs);
		}
	}

	@SuppressWarnings("unchecked")
	public T findFromCache(String key, int expiration, CacheMissAction cma) {
		return (T) findObjectFromCache(key, expiration, cma);
	}

	@SuppressWarnings("unchecked")
	public T findFromCache(String key, CacheStrategy cs, int expiration, CacheMissAction cma) {
		return (T) findObjectFromCache(key, cs, expiration, cma);
	}

	public List<T> findAllFromCache(String key, CacheMissAction cma) {
		return findAllFromCache(key, 0, cma);
	}

	public List<T> findAllFromCache(String key, CacheStrategy cs, CacheMissAction cma) {
		return findAllFromCache(key, cs, 0, cma);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllFromCache(String key, int expiration, CacheMissAction cma) {
		return (List<T>) findObjectFromCache(key, expiration, cma);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllFromCache(String key, CacheStrategy cs, int expiration, CacheMissAction cma) {
		return (List<T>) findObjectFromCache(key, cs, expiration, cma);
	}

}
