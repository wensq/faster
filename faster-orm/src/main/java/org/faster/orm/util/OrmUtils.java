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
package org.faster.orm.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author sqwen
 */
public class OrmUtils {

	public static final int getCountValue(Object count) {
		if (count instanceof Long) {
			return ((Long) count).intValue();
		}
		return (Integer) count;
	}

	public static final Condition buildUpdateHQL(String persistClassName, String wherePropertyName,
			Object[] wherePropertyValues, Map<String, ?> setAttributes) {
		StringBuilder hql = new StringBuilder("update ")
				.append(persistClassName)
				.append(" set ");

		List<Object> values = new ArrayList<Object>(setAttributes.size() + 1);
		for (Entry<String, ?> entry : setAttributes.entrySet()) {
			hql.append(entry.getKey()).append(" = ? and");
			values.add(entry.getValue());
		}
		hql.delete(hql.length() - 3, hql.length());
		hql.append("where ");
        if (wherePropertyValues.length == 1) {
            hql.append(wherePropertyName).append(" = ?");
            values.add(wherePropertyValues[0]);
        } else {
            hql.append(wherePropertyName).append(" in ?");
            values.add(wherePropertyValues);
        }

		return new Condition(hql.toString(), values);
	}

}
