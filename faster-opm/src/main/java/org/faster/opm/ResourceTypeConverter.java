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
 * 资源类型转换接口
 * <p>
 * 提供外部资源类型向内部资源类型的转换
 * <p>
 * 一个外部资源可能会转换为多个内部资源
 *
 * @author sqwen
 * @version 1.0, 2012-5-24
 */
public interface ResourceTypeConverter {

    List<String> convert(String rawResourceType);

}
