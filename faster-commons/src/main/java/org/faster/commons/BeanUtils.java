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
package org.faster.commons;

import java.util.Map;

import org.apache.commons.beanutils.WrapDynaBean;

/**
 * @author sqwen
 */
public class BeanUtils {

	private BeanUtils() {}

	public static final void populate(Object bean, Map<String, ?> map) {
		try {
			org.apache.commons.beanutils.BeanUtils.populate(bean, map);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static final void slicePopulate(Object bean, Map<String, ?> map, String... slicePropertyNames) {
		Map<String, ?> sliceMap = CollectionUtils.sliceMap(map, slicePropertyNames);
		populate(bean, sliceMap);
	}

	public static final Object getProperty(Object bean, String propertyName) {
		return new WrapDynaBean(bean).get(propertyName);
	}

	public static final void setProperty(Object bean, String propertyName, Object propertyValue) {
		try {
			org.apache.commons.beanutils.BeanUtils.setProperty(bean, propertyName, propertyValue);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static final void populate(Object dest, Object orig) {
		try {
			org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 从源对象拷贝指定属性的值到目标对象，忽略原值为空的值
	 *
	 * @param dest 目标对象
	 * @param orig 原对象
	 * @param slicePropertyNames 需要拷贝的属性列表
	 */
	public static final void slicePopulate(Object dest, Object orig, String... slicePropertyNames) {
		slicePopulate(dest, orig, true, slicePropertyNames);
	}

	public static final boolean isNullOrEmpty(Object value) {
		if (value == null) {
			return true;
		}

		if (value instanceof String) {
			if (((String) value).trim().isEmpty()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 从源对象拷贝指定属性的值到目标对象
	 *
	 * @param dest 目标对象
	 * @param orig 原对象
	 * @param ignoreNullValue 是否忽略空值（包括字符串的空字符串）
	 * @param propertyNames 需要拷贝的属性列表
	 */
	public static final void slicePopulate(Object dest, Object orig, boolean ignoreNullValue, String... propertyNames) {
		WrapDynaBean destBean = new WrapDynaBean(dest);
		WrapDynaBean origBean = new WrapDynaBean(orig);
		for (String propertyName : propertyNames) {
			Object value = origBean.get(propertyName);
			if (ignoreNullValue && isNullOrEmpty(value)) {
				continue;
			}

			destBean.set(propertyName, value);
		}
	}

}
