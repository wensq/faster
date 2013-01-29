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

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Cache 操作接口的 OSCache 实现
 *
 * @author sqwen
 */
public class OSCacheService implements CacheService {

	private static final GeneralCacheAdministrator cache = new GeneralCacheAdministrator();

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * OSCache对key无要求，直接采用外部key
	 */
	@Override
	public String buildInternalKey(String outerKey) {
		return outerKey;
	}

	@Override
	public void putInCache(String key, int expiration, Object obj) {
		key = buildInternalKey(key);
		boolean updated = false;
		try {
			if (obj != null) {
				cache.putInCache(key, obj);
				updated = true;
			}
		} finally {
			if (!updated) {
				cache.cancelUpdate(key);
			}
		}
	}

	@Override
	public Object getFromCache(String key, int expiration, CacheMissHandler handler) {
		if (expiration < 0) {
			return handler.doFind();
		}

		key = buildInternalKey(key);
		try {
			return cache.getFromCache(key, expiration);
		} catch (NeedsRefreshException nre) {
			if (handler == null) {
				throw new IllegalArgumentException("CacheMissHandler should be provided!");
			}
			StopWatch sw = null;
			if (log.isDebugEnabled()) {
				sw = new StopWatch();
				sw.start();
			}
			Object ret = null;
			boolean updated = false;
			try {
				if (log.isDebugEnabled()) {
					log.debug("Cache miss hit[key={}], finding directly...", key);
				}
				ret = handler.doFind();
				if (ret != null) {
					cache.putInCache(key, ret);
					updated = true;
				}
			} finally {
				if (!updated) {
					cache.cancelUpdate(key);
				}
				if (log.isDebugEnabled()) {
					log.debug("Direct search completed[found={}]. ({} ms)", updated, sw.getTime());
				}
			}
			return ret;
		}
	}

	@Override
	public void flush(String key) {
		key = buildInternalKey(key);
		cache.flushEntry(key);
	}

	@Override
	public void flush(List<String> keys) {
		if (keys == null || keys.isEmpty()) {
			return;
		}
		for (String key : keys) {
			flush(key);
		}
	}

}
