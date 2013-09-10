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
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sqwen
 */
public class QueryHelper {

	// 资源ID字段对应表
	private final static Map<Class<?>, List<Field>> queryFieldMap = new ConcurrentHashMap<Class<?>, List<Field>>();

	private QueryHelper() {}

	public static final boolean isQueryField(Field field) {
		return !field.isAnnotationPresent(NoQuery.class) && !Modifier.isStatic(field.getModifiers());
	}

	public static final List<Field> getQueryFields(Class<?> clazz) {
		List<Field> ret = queryFieldMap.get(clazz);
		if (ret == null) {
			synchronized (queryFieldMap) {
				ret = queryFieldMap.get(clazz);
				if (ret == null) {
					buildQueryFields(clazz);
					ret = queryFieldMap.get(clazz);
				}
			}
		}
		return ret;
	}

	private static void buildQueryFields(Class<?> clazz) {
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
				if (isQueryField(field)) {
					queryFields.add(field);
				}
			}
		}
		queryFieldMap.put(clazz, queryFields);
	}

	public static final String getFieldName(Field f) {
		Query p = f.getAnnotation(Query.class);
		return p == null || isBlank(p.field()) ? f.getName() : p.field();
	}

	public static final boolean isIgnoreNull(Field f) {
		Query p = f.getAnnotation(Query.class);
		return p == null || p.ignoreNull();
	}

	public static final boolean isIgnoreEmptyOrZero(Field f) {
		Query p = f.getAnnotation(Query.class);
		return p == null || p.ignoreEmptyOrZero();
	}

	public static final DataType getDataType(Field f) {
		Query p = f.getAnnotation(Query.class);
		if (p == null || p.type() == DataType.AUTO) {
			Class<?> clazz = f.getType();
			if (clazz == Integer.class) {
				return DataType.INTEGER;
			} else if (clazz == Long.class) {
				return DataType.LONG;
			} else if (clazz == String.class) {
				return DataType.STRING;
			} else if (clazz == Date.class) {
				return DataType.DATE;
			} else if (clazz == Boolean.class) {
				return DataType.BOOLEAN;
			}
		}
		return p == null ? DataType.STRING : p.type();
	}

	public static final MatchMode getMatchMode(Field f) {
		Query query = f.getAnnotation(Query.class);
		return query == null ? MatchMode.EQ : query.matchMode();
	}

    public static final String getDelimiter(Field f) {
        Query query = f.getAnnotation(Query.class);
        return query == null ? "," : query.delimiter();
    }

}
