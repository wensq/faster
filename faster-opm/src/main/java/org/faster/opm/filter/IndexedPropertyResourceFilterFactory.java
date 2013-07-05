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

import static org.faster.opm.ResourceRedisKeyHelper.getIndexedPropertyKey;

/**
 * 已建属性索引的资源过滤器工厂实现
 *
 * @author sqwen
 * @version 1.0, 2012-5-17
 */
public class IndexedPropertyResourceFilterFactory extends AbstractResourceFilterFactory<IndexedPropertyResourceFilter> {

    public IndexedPropertyResourceFilterFactory() {
        super();
    }

    @SuppressWarnings("rawtypes")
    public IndexedPropertyResourceFilterFactory(RedisTemplate redis) {
        super(redis);
    }

    @SuppressWarnings("unchecked")
    public boolean canFilterReource(String resourceType, String resourceFilterId) {
        String indexedPropertyKey = getIndexedPropertyKey(resourceType);
        return redis.opsForSet().isMember(indexedPropertyKey, resourceFilterId);
    }

    public IndexedPropertyResourceFilter newResourceFilter(String resourceType, String resourceFilterId,
                                                           String resourceFilterValue, String resourceFilterValueDelimiter) {
        return new IndexedPropertyResourceFilter(redis, resourceType, resourceFilterId, resourceFilterValue,
                resourceFilterValueDelimiter);
    }

}
