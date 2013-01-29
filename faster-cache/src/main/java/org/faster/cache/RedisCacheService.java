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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Cache 操作接口的 Redis 实现
 *
 * @author sqwen
 */
public class RedisCacheService implements CacheService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("rawtypes")
	private RedisTemplate redis;

	@SuppressWarnings("rawtypes")
	public void setRedis(RedisTemplate redis) {
		this.redis = redis;
	}

	@Override
	public String buildInternalKey(String outerKey) {
		return outerKey;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void putInCache(String key, int expiration, Object obj) {
		redis.opsForValue().set(key, obj);
		if (expiration > 0) {
			redis.expire(key, expiration, TimeUnit.SECONDS);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void flush(String key) {
		StopWatch sw = new StopWatch();
		sw.start();
		redis.delete(key);
		log.info("Key[" + key + "] flushed. (" + sw.getTime() + " ms)");
	}

	@Override
	@SuppressWarnings("unchecked")
	public void flush(List<String> keys) {
		if (keys == null || keys.isEmpty()) {
			return;
		}

		StopWatch sw = new StopWatch();
		sw.start();
		redis.delete(keys);
		String allKey = StringUtils.join(keys, ",");
		log.info(keys.size() + " keys[" + allKey + "] flushed. (" + sw.getTime() + " ms)");
	}

	@Override
	public Object getFromCache(String key, int expiration, CacheMissHandler handler) {
		if (expiration < 0) {
			return handler.doFind();
		}

		String newKey = buildInternalKey(key);

		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			log.debug("Getting from Redis Cache[key=" + newKey + "]...");
		}

		Object ret = redis.opsForValue().get(newKey);
		if (ret != null) {
			if (log.isDebugEnabled()) {
				log.debug("Redis Cache hit. (" + sw.getTime() + " ms)");
			}
			return ret;
		}

		if (handler == null) {
			throw new IllegalArgumentException("CacheMissHandler should be provided!");
		}

		if (log.isDebugEnabled()) {
			log.debug("Redis Cache miss, direct search...");
		}

		ret = redis.opsForValue().get(newKey);
		if (ret != null) {
			if (log.isDebugEnabled()) {
				log.debug("Redis Cache hit during double check. (" + sw.getTime() + " ms)");
			}
			return ret;
		}

		ret = handler.doFind();
		if (ret != null) {
			putInCache(key, expiration, ret);
		}
		if (log.isDebugEnabled()) {
			log.debug("Direct search completed[found=" + (ret != null) + "]. (" + sw.getTime() + " ms)");
		}
		return ret;
	}

}
