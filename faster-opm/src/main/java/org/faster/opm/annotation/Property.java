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
package org.faster.opm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 资源的属性标注，如果不标注的话，默认不会作为资源属性
 * <p>
 * 标注同时可以标注在字段和get方法上，方法优先级比字段优先级高
 * <p>
 * 即同一个属性，在字段和对应的get方法均设置了@Property标注，只有方法那个标注生效
 *
 * @author sqwen
 * @date 2012-4-21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Property {

    String id() default "";

    String name() default "";

    String type() default "string";

    String defaultValue() default "";

    String group() default "";

}
