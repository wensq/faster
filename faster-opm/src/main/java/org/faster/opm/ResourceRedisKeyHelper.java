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

/**
 * Resource存在Redis中的key构造器
 *
 * @author sqwen
 * @date 2012-4-23
 */
public class ResourceRedisKeyHelper {

    private ResourceRedisKeyHelper() {}

    /**
     * 资源的全局标识位对应的键值，表明资源是否已全部加载
     */
    public static final String getResourceGlobalKey(String resourceType) {
        return resourceType + ":LOADTIME";
    }

    /**
     * 保存某个资源所有对象的hash结构所对应的键值
     */
    public static final String getResourceHashKey(String resourceType) {
        return resourceType;
    }

    /**
     * 某个资源已建索引的属性所保存的set对应的键值
     */
    public static final String getIndexedPropertyKey(String resourceType) {
        return resourceType + ":INDEXED_PROPERTY";
    }

    /**
     * 获取某个资源某个索引名称和索引值对所对应的键值
     * <p>
     * 后台会自动根据相应的索引是否为唯一索引而返回不同的键值
     *
     * @param resourceType 资源类型
     * @param indexName 索引名称，一般为属性标识
     * @param indexValue 索引值，一般为属性值
     * @return 键值
     */
    public static final String getPropertyIndexKey(String resourceType, String indexName, String indexValue) {
        return resourceType + ":" + indexName + ":" + indexValue;
    }

    /**
     * 某个资源所有已建属性索引键值的集合所对应的键值
     */
    public static final String getPropertyIndexSetKey(String resourceType) {
        return resourceType + ":INDEX";
    }

}
