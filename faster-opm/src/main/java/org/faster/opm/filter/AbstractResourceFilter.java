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

import org.apache.commons.lang3.time.StopWatch;
import org.faster.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.faster.util.RedisUtils.saddAll;

/**
 * 资源过滤器实现类基类
 * <p>
 * 为子类提供资源注入和通用方法
 *
 * @author sqwen
 * @version 1.0, 2012-5-16
 */
public abstract class AbstractResourceFilter implements ResourceFilter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    protected RedisTemplate redis;

    protected String resourceType;

    protected String resourceFilterId;

    protected String resourceFilterValue;

    protected String resourceFilterValueDelimiter;

    protected boolean tempKey;

    @SuppressWarnings("rawtypes")
    public AbstractResourceFilter(RedisTemplate redis) {
        this.redis = redis;
    }

    @SuppressWarnings("rawtypes")
    public AbstractResourceFilter(RedisTemplate redis, String resourceType, String resourceFilterId, String resourceFilterValue, String resourceFilterValueDelimiter) {
        this.redis = redis;
        this.resourceType = resourceType;
        this.resourceFilterId = resourceFilterId;
        this.resourceFilterValue = resourceFilterValue;
        this.resourceFilterValueDelimiter = resourceFilterValueDelimiter;
    }

    protected String storeTempResourceIds(final Set<String> ids) {
        StopWatch sw = new StopWatch();
        sw.start();
        String tempKey = UUID.randomUUID().toString();
        int count = saddAll(redis, tempKey, ids);
        setTempKey(true);
        log.info("Saved " + count + " resource IDs to temp redis key[" + tempKey + "]. (" + sw.getTime() + " ms)");
        return tempKey;
    }

    @SuppressWarnings("unchecked")
    protected String storeMultiKeyValuesToTempKey(Collection<String> keys) {
        StopWatch sw = new StopWatch();
        sw.start();
        String tempKey = UUID.randomUUID().toString();
        redis.opsForSet().unionAndStore(tempKey, keys, tempKey);
        setTempKey(true);
        log.info("Union and Store " + keys.size() + " resource Keys to temp redis key[" + tempKey + "]. ("
                + sw.getTime() + " ms)");
        return tempKey;
    }

    protected boolean isMultiResourceFilterValues() {
        return isNotBlank(resourceFilterValueDelimiter) && resourceFilterValue.contains(resourceFilterValueDelimiter);
    }

    protected Set<String> getResourceFilterValues() {
        return isMultiResourceFilterValues() ? Strings.toStringSet(resourceFilterValue,
                resourceFilterValueDelimiter) : Collections.singleton(resourceFilterValue);
    }

    @SuppressWarnings("rawtypes")
    public void setRedis(RedisTemplate redis) {
        this.redis = redis;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceFilterId() {
        return resourceFilterId;
    }

    public void setResourceFilterId(String resourceFilterId) {
        this.resourceFilterId = resourceFilterId;
    }

    public String getResourceFilterValue() {
        return resourceFilterValue;
    }

    public void setResourceFilterValue(String resourceFilterValue) {
        this.resourceFilterValue = resourceFilterValue;
    }

    public boolean isTempKey() {
        return tempKey;
    }

    public void setTempKey(boolean tempKey) {
        this.tempKey = tempKey;
    }

}
