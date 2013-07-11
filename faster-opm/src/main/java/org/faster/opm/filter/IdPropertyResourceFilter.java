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
package org.faster.opm.filter;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

import static org.faster.opm.ResourceRedisKeyHelper.getResourceHashKey;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * ID属性的资源过滤器
 *
 * @author sqwen
 * @version 1.0, 2012-5-16
 */
public class IdPropertyResourceFilter extends AbstractResourceFilter {

    @SuppressWarnings("rawtypes")
    public IdPropertyResourceFilter(RedisTemplate redis) {
        super(redis);
    }

    @SuppressWarnings("rawtypes")
    public IdPropertyResourceFilter(RedisTemplate redis, String resourceType, String resourceFilterId, String resourceFilterValue, String resourceFilterValueDelimiter) {
        super(redis, resourceType, resourceFilterId, resourceFilterValue, resourceFilterValueDelimiter);
    }

    public boolean canDirectLoadAll() {
        return isBlank(resourceFilterValue);
    }

    public Set<String> getDirectFilteredResourceIds() {
        return getResourceFilterValues();
    }

    @SuppressWarnings("unchecked")
    public String getChainedFilterKey() {
        Set<String> ids = null;
        if (isBlank(resourceFilterValue)) {
            String key = getResourceHashKey(resourceType);
            ids = redis.opsForHash().keys(key);
        } else {
            ids = getResourceFilterValues();
        }

        setTempKey(true);
        return storeTempResourceIds(ids);
    }

}