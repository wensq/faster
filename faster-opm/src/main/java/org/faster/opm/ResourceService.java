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

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Resource服务接口
 *
 * @author sqwen
 * @date 2012-4-23
 */
public interface ResourceService {

    void saveResource(Resource res);

    void saveResources(List<Resource> resources);

    /**
     * 索引资源
     *
     * @param res 资源
     * @param index 资源的字段索引map，key: 字段名，value：字段值
     */
    void indexResource(Resource res, Map<String, String> index);

    void indexResources(List<Resource> resources, List<Map<String, String>> indexes);

    /**
     * 为某个资源的某些字段建立索引
     *
     * @param resourceType 资源类型
     * @param fieldNames 字段名称数组
     */
    void indexResources(String resourceType, String... fieldNames);

    void flushResources(String resourceType, List<String> ids);

    boolean isAllResourceLoaded(String resourceType);

    List<Resource> loadAllResource(String resourceType);

    List<Resource> loadAllResource(String resourceType, PropertyFilterType propertyFilterType,
                                   String[] propertyFilterValues);

    List<Resource> loadAllResource(String resourceType, PropertyFilterType propertyFilterType,
                                   String[] propertyFilterValues, List<NestParam> dynamicProperties);

    List<Resource> loadAllResourceWithGroup(String resourceType, String groupName);

    List<Resource> loadAllResourceWithGroups(String resourceType, String groupNames, String delimiter);

    List<Resource> loadAllResourceWithGroups(String resourceType, String[] groupNames);

    void markAllResourceLoaded(String resourceType);

    void unmarkAllResourceLoaded(String resourceType);

    void flushAllResource(String resourceType);

    boolean existResource(String resourceType, String id);

    Resource loadResource(String resourceType, String id);

    Resource loadResource(String resourceType, String id, PropertyFilterType propertyFilterType,
                          String[] propertyFilterValues);

    Resource loadResource(String resourceType, String id, PropertyFilterType propertyFilterType,
                          String[] propertyFilterValues, List<NestParam> dynamicProperties);

    Resource loadResourceWithGroup(String resourceType, String id, String groupName);

    Resource loadResourceWithGroups(String resourceType, String id, String groupNames, String delimiter);

    Resource loadResourceWithGroups(String resourceType, String id, String[] groupNames);

    void flushResource(String resourceType, String id);

    List<Resource> loadResources(String resourceType, Collection<String> ids);

    List<Resource> loadResources(String resourceType, Collection<String> ids, PropertyFilterType propertyFilterType,
                                 String[] propertyFilterValues);

    List<Resource> loadResources(String resourceType, Collection<String> ids, PropertyFilterType propertyFilterType,
                                 String[] propertyFilterValues, List<NestParam> dynamicProperties);

    List<Resource> loadResourcesWithGroup(String resourceType, Collection<String> ids, String groupName);

    List<Resource> loadResourcesWithGroups(String resourceType, Collection<String> ids, String groupNames,
                                           String delimiter);

    List<Resource> loadResourcesWithGroups(String resourceType, Collection<String> ids, String[] groupNames);

    List<Resource> loadResources(ResourceCriteria criteria);

    List<Resource> loadResourcesByFilter(String resourceType, Map<String, String> resFilters);

    List<Resource> loadResourcesByFilterWithGroups(String resourceType, Map<String, String> resFilters, String groupName);

    List<Resource> loadResourcesByFilterWithGroups(String resourceType, Map<String, String> resFilters,
                                                   String groupNames, String delimiter);

    List<Resource> loadResourcesByFilterWithGroups(String resourceType, Map<String, String> resFilters,
                                                   String[] groupNames);

    Map<String, Resource> loadResourcesMapByFilter(String resourceType, Map<String, String> resFilters);

    Map<String, Resource> loadResourcesMapByFilterWithGroups(String resourceType, Map<String, String> resFilters,
                                                             String groupName);

    Map<String, Resource> loadResourcesMapByFilterWithGroups(String resourceType, Map<String, String> resFilters,
                                                             String groupNames, String delimiter);

    Map<String, Resource> loadResourcesMapByFilterWithGroups(String resourceType, Map<String, String> resFilters,
                                                             String[] groupNames);

    Map<String, Resource> loadResourcesMap(ResourceCriteria criteria);

}
