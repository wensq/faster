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

import java.util.*;

/**
 * @author sqwen
 */
public class CollectionUtils {

	public static final <T> Set<T> toSet(T... elements) {
		if (elements == null) {
			return Collections.emptySet();
		}

		Set<T> set = new HashSet<T>(elements.length);
		Collections.addAll(set, elements);
		return set;
	}

	public static final <T> Set<T> toSet(Collection<T> elements) {
		if (elements == null) {
			return Collections.emptySet();
		}

		return new LinkedHashSet<T>(elements);
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
            ret.add(BeanUtils.slice(obj, ignoreNullValue, slicePropertyNames));
        }
        return ret;
    }

}
