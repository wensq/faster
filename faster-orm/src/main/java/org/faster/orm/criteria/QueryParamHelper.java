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

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

/**
 * @author sqwen
 */
public class QueryParamHelper {

	// 查询参数字段对应表
	private final static Map<Class<?>, List<Field>> queryParamFieldMap = new ConcurrentHashMap<Class<?>, List<Field>>();

	private QueryParamHelper() {}

	public static final boolean isQueryParamField(Field field) {
		return field.isAnnotationPresent(QueryParam.class);
	}

	public static final List<Field> getQueryParamFields(Class<?> clazz) {
		List<Field> ret = queryParamFieldMap.get(clazz);
		if (ret == null) {
			synchronized (queryParamFieldMap) {
				ret = queryParamFieldMap.get(clazz);
				if (ret == null) {
					buildQueryParamFields(clazz);
					ret = queryParamFieldMap.get(clazz);
				}
			}
		}
		return ret;
	}

	private static void buildQueryParamFields(Class<?> clazz) {
		List<Class<?>> classes = new LinkedList<Class<?>>();
		Class<?> searchClass = clazz;
		do {
			classes.add(0, searchClass);
		} while ((searchClass = searchClass.getSuperclass()) != null);

		List<Field> queryFields = new LinkedList<Field>();
		for (Class<?> cls : classes) {
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (isQueryParamField(field)) {
					queryFields.add(field);
				}
			}
		}
		queryParamFieldMap.put(clazz, queryFields);
	}

	public static final String getQueryParamName(Field f) {
		return f.getAnnotation(QueryParam.class).value();
	}

	public static final String getDefaultValue(Field f) {
		DefaultValue dv = f.getAnnotation(DefaultValue.class);
		return dv == null ? null : dv.value();
	}

}
