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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.faster.opm.ResourceRedisKeyHelper.getPropertyIndexKey;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 已建属性索引的资源过滤器实现
 *
 * @author sqwen
 * @version 1.0, 2012-5-12
 */
public class IndexedPropertyResourceFilter extends AbstractResourceFilter {

    @SuppressWarnings("rawtypes")
    public IndexedPropertyResourceFilter(RedisTemplate redis) {
        super(redis);
    }

    @SuppressWarnings("rawtypes")
    public IndexedPropertyResourceFilter(RedisTemplate redis, String resourceType, String resourceFilterId, String resourceFilterValue, String resourceFilterValueDelimiter) {
        super(redis, resourceType, resourceFilterId, resourceFilterValue, resourceFilterValueDelimiter);
    }

    public boolean canDirectLoadAll() {
        return isBlank(resourceFilterValue);
    }

    @SuppressWarnings("unchecked")
    public Set<String> getDirectFilteredResourceIds() {
        Set<String> pvs = getResourceFilterValues();
        if (pvs.size() == 1) {
            String key = getPropertyIndexKey(resourceType, resourceFilterId, resourceFilterValue);
            return redis.opsForSet().members(key);
        }

        Set<String> ret = new HashSet<String>();
        for (String pv : pvs) {
            String key = getPropertyIndexKey(resourceType, resourceFilterId, pv);
            ret.addAll(redis.opsForSet().members(key));
        }
        return ret;
    }

    public String getChainedFilterKey() {
        Set<String> pvs = getResourceFilterValues();
        if (pvs.size() == 1) {
            setTempKey(false);
            return getPropertyIndexKey(resourceType, resourceFilterId, resourceFilterValue);
        }

        List<String> keys = new ArrayList<String>(pvs.size());
        for (String pv : pvs) {
            String key = getPropertyIndexKey(resourceType, resourceFilterId, pv);
            keys.add(key);
        }
        return storeMultiKeyValuesToTempKey(keys);
    }

}
