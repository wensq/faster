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

import java.util.List;

/**
 * 动态属性处理接口
 *
 * @author sqwen
 * @version 1.0, 2012-5-13
 */
public interface DynamicPropertyHandler {

    /**
     * 是否可以处理某个动态属性
     *
     * @param resourceType 资源类型
     * @param dynamicPropertyParams 动态属性参数
     * @return 是否可以处理
     */
    boolean canHandle(String resourceType, NestParam dynamicPropertyParams);

    /**
     * 往一批资源里添加动态属性
     * <p>
     * 此操作是在<code>canHandle</code>为<code>true</code>的情况下进行
     *
     * @param resourceType 资源类型
     * @param resources 需要添加动态属性的资源
     * @param dynamicPropertyParams 动态属性参数
     * @return 成功添加的动态属性数量
     */
    int addDynamicProperty(String resourceType, List<Resource> resources, NestParam dynamicPropertyParams);

}
