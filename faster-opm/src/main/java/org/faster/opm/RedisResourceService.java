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

package org.faster.opm;

import org.apache.commons.lang3.time.StopWatch;
import org.faster.opm.filter.IdPropertyResourceFilterFactory;
import org.faster.opm.filter.IndexedPropertyResourceFilterFactory;
import org.faster.opm.filter.ResourceFilter;
import org.faster.opm.filter.ResourceFilterFactory;
import org.faster.util.DateTimes;
import org.faster.util.NestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.faster.opm.ResourceRedisKeyHelper.getIndexedPropertyKey;
import static org.faster.opm.ResourceRedisKeyHelper.getPropertyIndexKey;
import static org.faster.opm.ResourceRedisKeyHelper.getPropertyIndexSetKey;
import static org.faster.opm.ResourceRedisKeyHelper.getResourceGlobalKey;
import static org.faster.opm.ResourceRedisKeyHelper.getResourceHashKey;
import static org.faster.util.RedisUtils.sremAll;

/**
 * @author sqwen
 */
public class RedisResourceService implements ResourceService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    protected RedisTemplate redis;

    private List<ResourceFilterFactory<?>> resourceFilterFactories;

    private List<DynamicPropertyHandler> dynamicPropertyHandlers;

    private ResourceTypeConverter resourceTypeConverter;

    @SuppressWarnings("rawtypes")
    public void setRedis(RedisTemplate redis) {
        this.redis = redis;
    }

    public void setResourceFilterFactories(List<ResourceFilterFactory<?>> resourceFilterFactories) {
        this.resourceFilterFactories = resourceFilterFactories;
    }

    public void setDynamicPropertyHandlers(List<DynamicPropertyHandler> dynamicPropertyHandlers) {
        this.dynamicPropertyHandlers = dynamicPropertyHandlers;
    }

    public void setResourceTypeConverter(ResourceTypeConverter resourceTypeConverter) {
        this.resourceTypeConverter = resourceTypeConverter;
    }

    /**
     * 初始化操作，自动注入默认的ID资源过滤器和已建索引字段资源过滤器
     */
    public void init() {
        if (resourceFilterFactories == null) {
            resourceFilterFactories = new ArrayList<ResourceFilterFactory<?>>(2);
        }
        resourceFilterFactories.add(new IdPropertyResourceFilterFactory(redis));
        resourceFilterFactories.add(new IndexedPropertyResourceFilterFactory(redis));
        logger.info("RedisResourceService init complete[" + resourceFilterFactories.size() + " ResourceFilterHandlers, "
                + getDynamicPropertyHandlerCount() + " DynamicPropertyHandlers].");
    }

    private boolean existResourceFilterHandler() {
        return resourceFilterFactories != null && !resourceFilterFactories.isEmpty();
    }

    private boolean existDynamicPropertyHandler() {
        return dynamicPropertyHandlers != null && !dynamicPropertyHandlers.isEmpty();
    }

    public int getDynamicPropertyHandlerCount() {
        return dynamicPropertyHandlers == null ? 0 : dynamicPropertyHandlers.size();
    }

    @SuppressWarnings("unchecked")
    public void saveResource(Resource res) {
        String key = getResourceHashKey(res.getType());
        redis.opsForHash().put(key, res.getId(), res);
    }

    @SuppressWarnings("unchecked")
    public void saveResources(List<Resource> resources) {
        StopWatch sw = new StopWatch();
        sw.start();
        logger.info("Saving {} Resources to Redis...", resources.size());

        Map<String, Map<String, Resource>> resTypeMap = buildResourceTypeMap(resources);
        for (Map.Entry<String, Map<String, Resource>> entry : resTypeMap.entrySet()) {
            if (logger.isInfoEnabled()) {
                sw.split();
            }
            String key = getResourceHashKey(entry.getKey());
            redis.opsForHash().putAll(key, entry.getValue());
            if (logger.isInfoEnabled()) {
                logger.info("+ {} {} saved. ({} ms)",
                        new Object[] {entry.getValue().size(), entry.getKey(), sw.getTime() - sw.getSplitTime()});
            }
        }

        logger.info("Total {} Resources saved to Redis. ({} ms)", resources.size(), sw.getTime());
    }

    @SuppressWarnings("unchecked")
    public void indexResource(Resource res, Map<String, String> index) {
        if (index == null || index.isEmpty()) {
            return;
        }

        for (Map.Entry<String, String> entry : index.entrySet()) {
            addIndexedPropertyId(res.getType(), entry.getKey());
            String indexKey = getPropertyIndexKey(res.getType(), entry.getKey(), entry.getValue());
            redis.opsForSet().add(indexKey, res.getId());
            addIndexSet(res.getType(), indexKey);
        }
    }

    @SuppressWarnings("unchecked")
    private void addIndexSet(String resourceType, String indexKey) {
        String indexRefKey = getPropertyIndexSetKey(resourceType);
        redis.opsForSet().add(indexRefKey, indexKey);
    }

    @SuppressWarnings("unchecked")
    private void addIndexedPropertyId(String resourceType, String propertyId) {
        String key = getIndexedPropertyKey(resourceType);
        redis.opsForSet().add(key, propertyId);
    }

    public void indexResources(List<Resource> resources, List<Map<String, String>> indexes) {
        if (indexes == null || indexes.isEmpty()) {
            return;
        }

        logger.info("Building {} Resources' indexes to Redis...", resources.size());
        StopWatch sw = new StopWatch();
        sw.start();

        int count = 0;
        for (int i = 0; i < resources.size(); i++) {
            count += indexes.get(i).size();
            indexResource(resources.get(i), indexes.get(i));
        }

        logger.info("Total {} Resource indexes saved to Redis. ({} ms)", count, sw.getTime());
    }

    public void indexResources(String resourceType, String... fieldIds) {
        if (fieldIds == null || fieldIds.length == 0) {
            logger.info("No index fieldName specified for {}, ignore index operation.", resourceType);
            return;
        }

        StopWatch sw = new StopWatch();
        sw.start();
        if (logger.isInfoEnabled()) {
            String fields = org.apache.commons.lang3.StringUtils.join(fieldIds, ",");
            logger.info("Building index for {} by fields: {}", resourceType, fields);
        }

        List<Resource> all = loadAllResource(resourceType);
        if (all == null || all.isEmpty()) {
            logger.info("No Resource[{}] found, ignore index operation.", resourceType);
            return;
        }

        List<String> fids = new LinkedList<String>();
        Resource sample = all.get(0);
        for (String fid : fieldIds) { // filter property
            if (sample.existProperty(fid)) {
                addIndexedPropertyId(resourceType, fid);
                fids.add(fid);
            }
        }

        logger.info("Starting save index to redis...");
        sw.split();
        int count = indexWithPipeline(resourceType, all, fids);
        logger.info("{} indexes saved to redis. ({} ms)", count, sw.getTime() - sw.getSplitTime());

        logger.info("Total {} {} indexes saved to Redis. ({} ms)", new Object[] {count, resourceType, sw.getTime()});
    }

    @SuppressWarnings("unchecked")
    private int indexWithPipeline(final String resourceType, final List<Resource> all, final Collection<String> fieldIds) {
        return (Integer) redis.execute(new RedisCallback<Integer>() {
            int count = 0;

            public Integer doInRedis(RedisConnection connection) throws DataAccessException {
                for (Resource res : all) {
                    for (String fid : fieldIds) {
                        String value = res.getPropertyValue(fid);
                        if (isNotBlank(value)) {
                            String indexKey = getPropertyIndexKey(resourceType, fid, value);

                            connection.sAdd(redis.getDefaultSerializer().serialize(indexKey), redis
                                    .getDefaultSerializer().serialize(res.getId()));
                            String indexRefKey = getPropertyIndexSetKey(resourceType);
                            connection.sAdd(redis.getDefaultSerializer().serialize(indexRefKey), redis
                                    .getDefaultSerializer().serialize(indexKey));
                            count++;
                        }
                    }
                }
                return count;
            }
        }, false, true);
    }

    private Map<String, Map<String, Resource>> buildResourceTypeMap(List<Resource> resources) {
        Map<String, Map<String, Resource>> resTypeMap = new LinkedHashMap<String, Map<String, Resource>>();
        for (Resource res : resources) {
            Map<String, Resource> idMap = resTypeMap.get(res.getType());
            if (idMap == null) {
                idMap = new LinkedHashMap<String, Resource>();
                resTypeMap.put(res.getType(), idMap);
            }
            idMap.put(res.getId(), res);
        }
        return resTypeMap;
    }

    @SuppressWarnings("unchecked")
    public boolean isAllResourceLoaded(String resourceType) {
        return redis.hasKey(getResourceGlobalKey(resourceType));
    }

    public List<Resource> loadAllResource(String resourceType) {
        return loadAllResource(resourceType, true);
    }

    @SuppressWarnings("unchecked")
    public List<Resource> loadAllResource(String resourceType, boolean convertResourceType) {
        StopWatch sw = new StopWatch();
        sw.start();
        logger.info("Loading all {} Resources from Redis...", resourceType);

        List<Resource> ret = null;
        if (convertResourceType && hasResourceTypeConverter()) {
            List<String> internalResourceTypes = getInternalResourceType(resourceType);
            for (String resType : internalResourceTypes) {
                String key = getResourceHashKey(resType);
                ret.addAll(redis.opsForHash().values(key));
            }
        } else {
            String key = getResourceHashKey(resourceType);
            ret = new ArrayList<Resource>(redis.opsForHash().values(key));
        }

        logger.info("{} {} Resources loaded from Redis. ({} ms)", new Object[] {ret.size(), resourceType, sw.getTime()});
        return ret;
    }

    private boolean hasResourceTypeConverter() {
        return resourceTypeConverter != null;
    }

    private List<String> getInternalResourceType(String outResourceType) {
        return resourceTypeConverter == null ? Collections.singletonList(outResourceType) : resourceTypeConverter.convert(outResourceType);
    }

    public List<Resource> loadAllResource(String resourceType, PropertyFilterType propertyFilterType,
                                          String[] propertyFilterValues) {
        return loadAllResource(resourceType, propertyFilterType, propertyFilterValues, null);
    }

    public List<Resource> loadAllResource(String resourceType, PropertyFilterType propertyFilterType,
                                          String[] propertyFilterValues, List<NestParam> dynamicProperties) {
        List<Resource> list = loadAllResource(resourceType);
        return processResourceProperties(list, resourceType, propertyFilterType, propertyFilterValues,
                dynamicProperties);
    }

    private List<Resource> processResourceProperties(List<Resource> list, String resourceType, PropertyFilterType pft,
                                                     String[] propertyFilterValues, List<NestParam> dynamicPropertyTags) {
        List<Resource> filteredResources = filterProperties(list, resourceType, pft, propertyFilterValues);
        addDynamicProperties(filteredResources, resourceType, dynamicPropertyTags);
        return filteredResources;
    }

    private List<Resource> filterProperties(List<Resource> list, String resourceType,
                                            PropertyFilterType propertyFilterType, String[] propertyFilterValues) {
        if (list == null) {
            return Collections.emptyList();
        }

        if (!isPropertyFilterRequired(propertyFilterType, propertyFilterValues)) {
            return list;
        }

        StopWatch sw = new StopWatch();
        sw.start();
        String values = null;
        if (logger.isInfoEnabled()) {
            values = org.apache.commons.lang3.StringUtils.join(propertyFilterValues, ",");
            logger.info("Property filtered required, building {} {} Resources by {}[{}]...",
                    new Object[] {list.size(), resourceType, propertyFilterType, values});
        }

        List<Resource> ret = new ArrayList<Resource>(list.size());
        for (Resource resource : list) {
            if (resource != null) {
                ret.add(resource.newResource(propertyFilterType, propertyFilterValues));
            }
        }

        if (logger.isInfoEnabled()) {
            logger.info("{} {} Resources property filtered by {}[{}]. ({} ms)",
                    new Object[] {ret.size(), resourceType, propertyFilterType, values, sw.getTime()});
        }
        return ret;
    }

    private void addDynamicProperties(List<Resource> list, String resourceType, List<NestParam> dynamicPropertiesTag) {
        if (list == null || list.isEmpty()) {
            return;
        }

        if (dynamicPropertiesTag == null || dynamicPropertiesTag.isEmpty()) {
            return;
        }

        if (!existDynamicPropertyHandler()) {
            return;
        }

        logger.info("Adding {} dynamic properties...", resourceType);
        StopWatch sw = new StopWatch();
        sw.start();

        int total = 0;
        for (NestParam tag : dynamicPropertiesTag) {
            for (DynamicPropertyHandler handler : dynamicPropertyHandlers) {
                if (handler.canHandle(resourceType, tag)) {
                    logger.info("Adding {}'s dynamic property[{}] by {}",
                            new Object[] {resourceType, tag.getName(), handler.getClass().getSimpleName()});
                    sw.split();
                    int count = handler.addDynamicProperty(resourceType, list, tag);
                    logger.info("{} {} dynamic properties[{}] added. ({} ms)",
                            new Object[] {count, resourceType, tag.getName(), sw.getTime() - sw.getSplitTime()});
                    total += count;
                    break;
                }
            }
        }

        logger.info("Total {} {} dynamic properties added. ({} ms)", new Object[] {total, resourceType, sw.getTime()});
    }

    private boolean isPropertyFilterRequired(PropertyFilterType propertyFilterType,
                                             String[] propertyFilterValues) {
        return propertyFilterType != null && propertyFilterType != PropertyFilterType.NONE
                && propertyFilterValues != null && propertyFilterValues.length > 0;
    }

    public List<Resource> loadAllResourceWithGroup(String resourceType, String groupName) {
        return loadAllResource(resourceType, PropertyFilterType.GROUP, new String[] { groupName });
    }

    public List<Resource> loadAllResourceWithGroups(String resourceType, String groupNames, String delimiter) {
        return loadAllResource(resourceType, PropertyFilterType.GROUP, groupNames.split(delimiter));
    }

    public List<Resource> loadAllResourceWithGroups(String resourceType, String[] groupNames) {
        return loadAllResource(resourceType, PropertyFilterType.GROUP, groupNames);
    }

    @SuppressWarnings("unchecked")
    public void markAllResourceLoaded(String resourceType) {
        redis.opsForValue().set(getResourceGlobalKey(resourceType),
                DateTimes.formatDateToTimestampString(new Date()));
    }

    @SuppressWarnings("unchecked")
    public void unmarkAllResourceLoaded(String resourceType) {
        redis.delete(getResourceGlobalKey(resourceType));
    }

    @SuppressWarnings("unchecked")
    public void flushAllResource(String resourceType) {
        logger.info("Flush all {}...", resourceType);
        StopWatch sw = new StopWatch();
        sw.start();
        unmarkAllResourceLoaded(resourceType);
        String key = getResourceHashKey(resourceType);
        Long count = redis.opsForHash().size(key);
        redis.delete(key);
        logger.info("{} {} flushed. ({} ms)", new Object[] {count, resourceType, sw.getTime()});

        sw.split();
        key = getPropertyIndexSetKey(resourceType);
        Set<String> indexKeys = redis.opsForSet().members(key);
        for (String indexKey : indexKeys) {
            redis.delete(indexKey);
        }
        redis.delete(key);
        logger.info("{} {} indexes flushed. ({} ms)", new Object[] {indexKeys.size(), resourceType, sw.getTime() - sw.getSplitTime()});
    }

    @SuppressWarnings("unchecked")
    public boolean existResource(String resourceType, String id) {
        String key = getResourceHashKey(resourceType);
        return redis.opsForHash().hasKey(key, id);
    }

    public Resource loadResource(String resourceType, String id) {
        return loadResource(resourceType, id, true);
    }

    @SuppressWarnings("unchecked")
    public Resource loadResource(String resourceType, String id, boolean convertResourceType) {
        StopWatch sw = null;
        if (logger.isDebugEnabled()) {
            sw = new StopWatch();
        }

        Resource ret = null;
        if (convertResourceType && hasResourceTypeConverter()) {
            List<String>  internalResourceTypes = getInternalResourceType(resourceType);
            for (String resType : internalResourceTypes) {
                String key = getResourceHashKey(resType);
                ret = (Resource) redis.opsForHash().get(key, id);
                if (ret != null) {
                    break;
                }
            }
        } else {
            String key = getResourceHashKey(resourceType);
            ret = (Resource) redis.opsForHash().get(key, id);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Load {}#{} complete[found={}]. ({} ms)", new Object[] {resourceType, id, ret != null, sw.getTime()});
        }
        return ret;
    }

    public Resource loadResource(String resourceType, String id, PropertyFilterType propertyFilterType,
                                 String[] propertyFilterValues) {
        return loadResource(resourceType, id, propertyFilterType, propertyFilterValues, null);
    }

    public Resource loadResource(String resourceType, String id, PropertyFilterType propertyFilterType,
                                 String[] propertyFilterValues, List<NestParam> dynamicProperties) {
        Resource resource = loadResource(resourceType, id);
        if (resource == null) {
            return resource;
        }

        List<Resource> res = processResourceProperties(Collections.singletonList(resource), resourceType,
                propertyFilterType, propertyFilterValues, dynamicProperties);
        return res.get(0);
    }

    public Resource loadResourceWithGroup(String resourceType, String id, String groupName) {
        Resource resource = loadResource(resourceType, id);
        return resource == null ? null : resource.newResourceWithGroup(groupName);
    }

    public Resource loadResourceWithGroups(String resourceType, String id, String groupNames, String delimiter) {
        Resource resource = loadResource(resourceType, id);
        return resource == null ? null : resource.newResourceWithGroups(groupNames, delimiter);
    }

    public Resource loadResourceWithGroups(String resourceType, String id, String[] groupNames) {
        Resource resource = loadResource(resourceType, id);
        return resource == null ? null : resource.newResourceWithGroups(groupNames);
    }

    @SuppressWarnings("unchecked")
    public void flushResource(String resourceType, String id) {
        String key = getResourceHashKey(resourceType);
        redis.opsForHash().delete(key, id);
        flushResourceIndexes(resourceType, id);
        unmarkAllResourceLoaded(resourceType);
    }

    @SuppressWarnings("unchecked")
    private void flushResourceIndexes(String resourceType, String id) {
        String key = getPropertyIndexSetKey(resourceType);
        Set<String> indexKeys = redis.opsForSet().members(key);
        if (indexKeys != null && !indexKeys.isEmpty()) {
            for (String indexKey : indexKeys) {
                redis.opsForSet().remove(indexKey, id);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void flushResourcesIndexes(String resourceType, Collection<String> ids) {
        String key = getPropertyIndexSetKey(resourceType);
        Set<String> indexKeys = redis.opsForSet().members(key);
        if (indexKeys == null || indexKeys.isEmpty()
                || ids == null || ids.isEmpty()) {
            return;
        }

        for (String indexKey : indexKeys) {
            sremAll(redis, indexKey, ids);
        }
    }

    public List<Resource> loadResources(String resourceType, Collection<String> ids) {
        return loadResources(resourceType, ids, true);
    }

    @SuppressWarnings("unchecked")
    public List<Resource> loadResources(String resourceType, Collection<String> ids, boolean convertResourceType) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        StopWatch sw = new StopWatch();
        sw.start();
        logger.info("Loading {} {} Resources by ID from Redis...", ids.size(), resourceType);

        List<Resource> ret = null;
        if (convertResourceType && hasResourceTypeConverter()) {
            List<String> resTypes = getInternalResourceType(resourceType);
            ret = new LinkedList<Resource>();
            for (String resType : resTypes) {
                ret.addAll((List<Resource>) redis.opsForHash().multiGet(resType, ids));
            }
        } else {
            ret = (List<Resource>) redis.opsForHash().multiGet(resourceType, ids);
        }

        logger.info("{} {} Resources loaded from Redis by ID. ({} ms)", new Object[] {ret.size(), resourceType, sw.getTime()});
        return ret;
    }

    public List<Resource> loadResources(String resourceType, Collection<String> ids,
                                        PropertyFilterType propertyFilterType,
                                        String[] propertyFilterValues) {
        return loadResources(resourceType, ids, propertyFilterType, propertyFilterValues, null);
    }

    public List<Resource> loadResources(String resourceType, Collection<String> ids,
                                        PropertyFilterType propertyFilterType, String[] propertyFilterValues, List<NestParam> dynamicProperties) {
        List<Resource> list = loadResources(resourceType, ids);
        if (isPropertyFilterRequired(propertyFilterType, propertyFilterValues)) {
            list = filterProperties(list, resourceType, propertyFilterType, propertyFilterValues);
        }

        if (dynamicProperties != null && !dynamicProperties.isEmpty()) {
            addDynamicProperties(list, resourceType, dynamicProperties);
        }

        return list;
    }

    public List<Resource> loadResourcesWithGroup(String resourceType, Collection<String> ids, String groupName) {
        return loadResourcesWithGroups(resourceType, ids, groupName, ",");
    }

    public List<Resource> loadResourcesWithGroups(String resourceType, Collection<String> ids, String groupNames,
                                                  String delimiter) {
        String[] gns = isBlank(groupNames) ? new String[0] : groupNames.split(delimiter);
        return loadResourcesWithGroups(resourceType, ids, gns);
    }

    public List<Resource> loadResourcesWithGroups(String resourceType, Collection<String> ids, String[] groupNames) {
        return loadResources(resourceType, ids, PropertyFilterType.GROUP, groupNames);
    }

    @SuppressWarnings("unchecked")
    public void flushResources(String resourceType, List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        String key = getResourceHashKey(resourceType);
        for (String id : ids) {
            redis.opsForHash().delete(key, id);
        }
        flushResourcesIndexes(resourceType, ids);
        unmarkAllResourceLoaded(resourceType);
    }

    public List<Resource> loadResourcesByFilter(String resourceType, Map<String, String> resFilters) {
        return loadResourcesByFilterWithGroups(resourceType, resFilters, null, ",");
    }

    public List<Resource> loadResourcesByFilterWithGroups(String resourceType, Map<String, String> resFilters,
                                                          String groupName) {
        return loadResourcesByFilterWithGroups(resourceType, resFilters, groupName, ",");
    }

    public List<Resource> loadResourcesByFilterWithGroups(String resourceType, Map<String, String> resFilters,
                                                          String groupNames, String delimiter) {
        String[] gns = isBlank(groupNames) ? new String[0] : groupNames.split(delimiter);
        return loadResourcesByFilterWithGroups(resourceType, resFilters, gns);
    }

    public List<Resource> loadResourcesByFilterWithGroups(String resourceType, Map<String, String> resFilters,
                                                          String[] groupNames) {
        ResourceCriteria rc = new ResourceCriteria();
        rc.setResourceType(resourceType);
        rc.setResourceFilterParams(resFilters);
        rc.setPropertyFilterType(PropertyFilterType.GROUP);
        rc.setPropertyFilterValues(groupNames);
        return loadResources(rc);
    }

    public Map<String, Resource> loadResourcesMapByFilter(String resourceType, Map<String, String> resFilters) {
        return loadResourcesMapByFilterWithGroups(resourceType, resFilters, null, ",");
    }

    private Map<String, Resource> buildResourceMap(List<Resource> resources) {
        if (resources == null || resources.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Resource> map = new HashMap<String, Resource>();
        for (Resource res : resources) {
            map.put(res.getId(), res);
        }

        return map;
    }

    public Map<String, Resource> loadResourcesMapByFilterWithGroups(String resourceType,
                                                                    Map<String, String> resFilters, String groupName) {
        return loadResourcesMapByFilterWithGroups(resourceType, resFilters, groupName, ",");
    }

    public Map<String, Resource> loadResourcesMapByFilterWithGroups(String resourceType,
                                                                    Map<String, String> resFilters, String groupNames, String delimiter) {
        StopWatch sw = new StopWatch();
        sw.start();
        logger.info("Loading {} Resources Map by filter[{}] with group[{}] from Redis...", new Object[] {resourceType, resFilters, groupNames});

        List<Resource> list = loadResourcesByFilterWithGroups(resourceType, resFilters, groupNames, delimiter);
        Map<String, Resource> ret = buildResourceMap(list);

        logger.info("Loaded {} {} Resources Map by filter[{}] with group[{}] from Redis. ({} ms)",
                new Object[] {ret.size(), resourceType, resFilters, groupNames, sw.getTime()});
        return ret;
    }

    public Map<String, Resource> loadResourcesMapByFilterWithGroups(String resourceType,
                                                                    Map<String, String> resFilters, String[] groupNames) {
        String gns = org.apache.commons.lang3.StringUtils.join(groupNames, ",");
        return loadResourcesMapByFilterWithGroups(resourceType, resFilters, gns, ",");
    }

    public List<Resource> loadResources(ResourceCriteria criteria) {
        StopWatch sw = new StopWatch();
        sw.start();
        logger.info("Loading Resources from Redis by Criteria: " + criteria);

        List<Resource> list = null;
        String resourceType = criteria.getResourceType();

        if (hasResourceTypeConverter()) {
            List<String> resTypes = getInternalResourceType(resourceType);
            list = new LinkedList<Resource>();
            for (String resType : resTypes) {
                list.addAll(loadResourceByInternalType(criteria, resourceType));
            }
        } else {
            list = loadResourceByInternalType(criteria, resourceType);
        }

        if (criteria.isAttributeFilterRequired()) {
            for (Resource res : list) {
                res.filterAttribute(criteria.getAttributeFilterValues());
            }
        }

        logger.info("Loaded {} {} Resources by Criteria: {}. ({} ms)", new Object[] {list.size(), resourceType, criteria, sw.getTime()});
        return list;
    }

    private List<Resource> loadResourceByInternalType(ResourceCriteria criteria, String resourceType) {
        List<Resource> list = null;
        if (!criteria.existResourceFilter() || !existResourceFilterHandler()) {
            list = loadAllResource(resourceType, false);
        } else if (criteria.getResourceFilterParams().size() == 1) {
            Map.Entry<String, String> entry = criteria.getResourceFilterParams().entrySet().iterator().next();
            list = filterResourcesBySingleFilter(resourceType, entry.getKey(), entry.getValue(),
                    criteria.getResourceFilterValueDelimiter());
        } else {
            list = filterResourcesByMultiFilters(resourceType, criteria.getResourceFilterParams(),
                    criteria.getResourceFilterValueDelimiter());
        }

        return processResourceProperties(list, resourceType, criteria.getPropertyFilterType(),
                criteria.getPropertyFilterValues(), criteria.getDynamicPropertyParams());
    }

    @SuppressWarnings("rawtypes")
    private List<Resource> filterResourcesBySingleFilter(String resourceType, String resourceFilterId,
                                                         String resourceFilterValues, String resourceFilterValuesDelimiter) {
        Set<String> ids = null;
        for (ResourceFilterFactory resourceFilterFactory : resourceFilterFactories) {
            if (resourceFilterFactory.canFilterReource(resourceType, resourceFilterId)) {
                ResourceFilter resourceFilter = resourceFilterFactory.newResourceFilter(resourceType, resourceFilterId,
                        resourceFilterValues, resourceFilterValuesDelimiter);
                logger.info("Direct filter {}[{}={}] by {}", new Object[] {resourceType, resourceFilterId, resourceFilterValues, resourceFilter});
                if (resourceFilter.canDirectLoadAll()) {
                    return loadAllResource(resourceType);
                } else {
                    ids = resourceFilter.getDirectFilteredResourceIds();
                    logger.info("Obtain {} resource IDs.", ids.size());
                    return loadResources(resourceType, ids);
                }
            }
        }

        return loadAllResource(resourceType, false);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private List<Resource> filterResourcesByMultiFilters(String resourceType, Map<String, String> resourceFilterParams,
                                                         String resourceFilterValueDelimiter) {
        StopWatch sw = new StopWatch();
        sw.start();
        Set<String> ids = null;
        List<String> keys = new ArrayList<String>(resourceFilterFactories.size());
        List<String> tempKeys = new LinkedList<String>();
        for (Map.Entry<String, String> entry : resourceFilterParams.entrySet()) {
            for (ResourceFilterFactory resourceFilterFactory : resourceFilterFactories) {
                if (resourceFilterFactory.canFilterReource(resourceType, entry.getKey())) {
                    ResourceFilter resourceFilter = resourceFilterFactory.newResourceFilter(resourceType,
                            entry.getKey(), entry.getValue(), resourceFilterValueDelimiter);
                    logger.info("Handle chained filter {}[{}={}] by {}",
                            new Object[] {resourceType, entry.getKey(), entry.getValue(), resourceFilterFactory});
                    String key = resourceFilter.getChainedFilterKey();
                    logger.info("Obtain chained redis key: {}", key);
                    keys.add(key);
                    if (resourceFilter.isTempKey()) {
                        tempKeys.add(key);
                    }
                    break;
                }
            }
        }
        logger.info("All chained redis key obtained[total={}, temp={}]. ({} ms)",
                new Object[] {keys.size(), tempKeys.size(), sw.getTime()});

        if (keys.isEmpty()) {
            return loadAllResource(resourceType);
        }

        sw.split();
        String destTempKey = UUID.randomUUID().toString();
        redis.opsForSet().intersectAndStore(keys.get(0), keys.subList(1, keys.size()), destTempKey);
        ids = redis.opsForSet().members(destTempKey);
        logger.info("Intersect {} {} ID sets, obtain {} IDs. ({} ms)",
                new Object[] {keys.size(), resourceType, ids.size(), sw.getTime() - sw.getSplitTime()});

        tempKeys.add(destTempKey);
        redis.delete(tempKeys);

        return loadResources(resourceType, ids, false);
    }

    public Map<String, Resource> loadResourcesMap(ResourceCriteria criteria) {
        List<Resource> list = loadResources(criteria);
        return buildResourceMap(list);
    }
}
