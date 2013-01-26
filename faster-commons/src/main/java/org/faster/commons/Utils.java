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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.WrapDynaBean;

/**
 * @author sqwen
 */
public class Utils {

	private Utils() {}

	public static final <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static final Map<?, ?> map(Object key, Object value) {
		return Collections.singletonMap(key, value);
	}

	public static final <T> Map<String, T> map(String key, T value) {
		return Collections.singletonMap(key, value);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final Map<?, ?> map(Object... values) {
		if (values == null || values.length == 0) {
			return Collections.emptyMap();
		}

		if (values.length % 2 != 0) {
			throw new IllegalArgumentException("map parameters must be key-value pair.");
		}

		Map ret = new HashMap();
		for (int i = 0; i < values.length; i += 2) {
			ret.put(values[i], values[i + 1]);
		}
		return ret;
	}

	public static final Map<String, ?> slice(Map<String, ?> map, String... keys) {
		return slice(map, true, keys);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final Map<String, ?> slice(Map<String, ?> map, boolean ignoreNullValue, String... keys) {
		if (keys == null || keys.length == 0) {
			return map;
		}

		Map ret = new HashMap();
		for (String key : keys) {
			Object value = map.get(key);
			if (ignoreNullValue && isNullOrEmpty(value)) {
				continue;
			}
			ret.put(key, value);
		}
		return ret;
	}

	public static final <T> T slice(T orig, String... propertyNames) {
		return slice(orig, true, propertyNames);
	}

	@SuppressWarnings("unchecked")
	public static final <T> T slice(T orig, boolean ignoreNullValue, String... propertyNames) {
		if (propertyNames == null || propertyNames.length == 0) {
			return orig;
		}

		T dest = (T) newInstance(orig.getClass());
		WrapDynaBean destBean = new WrapDynaBean(dest);
		WrapDynaBean origBean = new WrapDynaBean(orig);
		for (String propertyName : propertyNames) {
			Object value = origBean.get(propertyName);
			if (ignoreNullValue && isNullOrEmpty(value)) {
				continue;
			}

			destBean.set(propertyName, value);
		}
		return dest;
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

	public static final <T> T[] slice(T[] objs, String... slicePropertyNames) {
		return slice(objs, true, slicePropertyNames);
	}

	public static final <T> T[] slice(T[] objs, boolean ignoreNullValue, String... slicePropertyNames) {
		if (objs == null || objs.length == 0) {
			return null;
		}

		if (slicePropertyNames == null || slicePropertyNames.length == 0) {
			return objs;
		}

		T[] ret = Arrays.copyOfRange(objs, 0, objs.length);
		for (int i = 0; i < objs.length; i++) {
			ret[i] = slice(objs[i], ignoreNullValue, slicePropertyNames);
		}
		return ret;
	}

	public static final <T> List<T> slice(Collection<T> objs, String... slicePropertyNames) {
		return slice(objs, true, slicePropertyNames);
	}

	public static final <T> List<T> slice(Collection<T> objs, boolean ignoreNullValue, String... slicePropertyNames) {
		if (objs == null || objs.isEmpty()) {
			return null;
		}

		if (slicePropertyNames == null || slicePropertyNames.length == 0) {
			return Collections.emptyList();
		}

		List<T> ret = new ArrayList<T>(objs.size());
		for (T obj : objs) {
			ret.add(slice(obj, ignoreNullValue, slicePropertyNames));
		}
		return ret;
	}

}
