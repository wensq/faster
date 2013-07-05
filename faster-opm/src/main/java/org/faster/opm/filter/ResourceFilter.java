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

import java.util.Set;

/**
 * 资源过滤器接口
 *
 * @author sqwen
 * @version 1.0, 2012-5-16
 */
public interface ResourceFilter {

    /**
     * 是否可以直接加载所有设备
     * <p>
     * 在过滤器不能过滤任何资源的情况下，返回true
     *
     * @return 是否可以直接加载所有设备
     */
    boolean canDirectLoadAll();

    /**
     * 对资源按照属性值进行直接过滤操作，需要返回的资源ID集合
     *
     * @return 进行直接过滤后的资源ID集合
     */
    Set<String> getDirectFilteredResourceIds();

    /**
     * 对某个属性进行过滤后生成的链式过滤键值，供后续处理
     */
    String getChainedFilterKey();

    /**
     * 是否为临时生成的key，如果是临时生成的key，后续处理完成后会被自动删除
     * <p>
     * 一般在生成链式过滤键值后，同时设置是否为临时键值
     */
    boolean isTempKey();

}
