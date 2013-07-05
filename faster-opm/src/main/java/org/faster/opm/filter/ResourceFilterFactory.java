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

/**
 * 资源过滤器工厂接口
 *
 * @author sqwen
 * @version 1.0, 2012-5-16
 */
public interface ResourceFilterFactory<T extends ResourceFilter> {

    /**
     * 判定是否支持对指定资源、指定过滤方式的过滤
     *
     * @param resourceType 资源类型
     * @param resFilterId 资源过滤标识（一般为资源属性标识或者自定义标识）
     * @return 是否支持资源过滤
     */
    boolean canFilterReource(String resourceType, String resFilterId);

    /**
     * 根据指定的资源类型、资源过滤参数新建具体的资源过滤器
     *
     * @param resourceType 资源类型
     * @param resFilterId 资源过滤标识（一般为资源属性标识或者自定义标识）
     * @param resFilterValue 资源过滤值（一般为资源属性值或者自定义格式的值）
     * @param resFilterValueDelimiter 资源过滤值存在多个值的情况下，使用的分隔符
     * @return 构建具体的资源过滤器
     */
    T newResourceFilter(String resourceType, String resFilterId, String resFilterValue, String resFilterValueDelimiter);

}
