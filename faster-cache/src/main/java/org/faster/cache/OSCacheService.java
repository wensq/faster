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

import java.util.List;

/**
 * Cache 操作接口的 OSCache 实现
 *
 * @author sqwen
 */
public class OSCacheService extends AbstractCacheService {

	private static final GeneralCacheAdministrator cache = new GeneralCacheAdministrator();

	@Override
	protected void doPutInCache(String key, int expiration, Object obj) {
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
    protected Object doGetFromCache(String internalKey, int expiration, CacheMissHandler handler) {
        try {
            return cache.getFromCache(internalKey, expiration);
        } catch (NeedsRefreshException nre) {
            assertHandlerIsNotNull(handler);
            StopWatch sw = null;
            if (log.isDebugEnabled()) {
                sw = new StopWatch();
                sw.start();
                log.debug("Cache miss[key={}], finding directly...", internalKey);
            }
            Object ret = null;
            boolean updated = false;
            try {
                ret = handler.doFind();
                if (ret != null) {
                    doPutInCache(internalKey, expiration, ret);
                    updated = true;
                }
            } finally {
                if (!updated) {
                    cache.cancelUpdate(internalKey);
                }
                if (log.isDebugEnabled()) {
                    log.debug("Direct search completed[found={}]. ({} ms)", updated, sw.getTime());
                }
            }
            return ret;
        }
    }

    @Override
    protected void doFlush(String key) throws Exception {
        cache.flushEntry(key);
    }

    @Override
    protected void doFlush(List<String> internalKeys) throws Exception {
        for (String key : internalKeys) {
            doFlush(key);
        }
    }

}
