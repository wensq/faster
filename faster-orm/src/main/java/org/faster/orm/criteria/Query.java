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
package org.faster.orm.criteria;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sqwen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Query {

	/**
	 * 实际查询对应的字段，可以为子对象
	 * <p>
	 * 默认为空，表示字段名与自身相同
	 * <p>
	 * 子对象的字段需要用<tt>.</tt>分隔
	 * <p>
	 * 支持子对象级联
	 *
	 * @return 实际查询字段
	 */
	String field() default "";

	/**
	 * 查询字段实际数据类型
	 * <p>
	 * 默认为：AUTO，表示自动检测
	 *
	 * @return 实际数据类型
	 */
	DataType type() default DataType.AUTO;

	/**
	 * 如果未提供匹配模式的话，默认的匹配方式
	 */
	MatchMode matchMode() default MatchMode.EQ;

    /**
     * 多值字段的分隔符
     */
    String delimiter() default ",";

	/**
	 * 是否忽略空值
	 */
	boolean ignoreNull() default true;

	/**
	 * 是否忽略空字符串或值为0的数值
	 */
	boolean ignoreEmptyOrZero() default true;

}
