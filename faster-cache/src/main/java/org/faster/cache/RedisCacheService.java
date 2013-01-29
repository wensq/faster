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

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Cache 操作接口的 Redis 实现
 *
 * @author sqwen
 */
public class RedisCacheService extends AbstractCacheService {

	@SuppressWarnings("rawtypes")
	private RedisTemplate redis;

	@SuppressWarnings("rawtypes")
	public void setRedis(RedisTemplate redis) {
		this.redis = redis;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doPutInCache(String key, int expiration, Object obj) {
		redis.opsForValue().set(key, obj);
		if (expiration > 0) {
			redis.expire(key, expiration, TimeUnit.SECONDS);
		}
	}

	@Override
	protected Object doGetFromCache(String internalKey, int expiration, CacheMissHandler handler) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			log.debug("Getting from Redis Cache[key={}]...", internalKey);
		}

		Object ret = redis.opsForValue().get(internalKey);
		if (ret != null) {
			if (log.isDebugEnabled()) {
				log.debug("Redis Cache hit[key={}]. ({} ms)", internalKey, sw.getTime());
			}
			return ret;
		}

		assertHandlerIsNotNull(handler);

		if (log.isDebugEnabled()) {
            log.debug("Redis cache miss[key={}], direct search...", internalKey);
		}

		ret = handler.doFind();
		if (ret != null) {
			doPutInCache(internalKey, expiration, ret);
		}

        if (log.isDebugEnabled()) {
            log.debug("Direct search completed[found={}]. ({} ms)", ret != null, sw.getTime());
        }
		return ret;
	}

    @Override
    protected void doFlush(String key) throws Exception {
        redis.delete(key);
    }

    @Override
    protected void doFlush(List<String> internalKeys) throws Exception {
        redis.delete(internalKeys);
    }

}
