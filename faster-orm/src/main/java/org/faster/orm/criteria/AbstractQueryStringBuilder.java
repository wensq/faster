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
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.faster.commons.Collections;

/**
 * @author sqwen
 */
public class AbstractQueryStringBuilder {

	protected Object criteria;

	protected Set<String> excludeFieldNames;

	protected Set<String> includeFieldNames;

	// 针对的查询字段名
	protected String applyQueryFieldName;

	protected String prefix;

	private Map<String, String> defaultValues;

	protected AbstractQueryStringBuilder setDefaultValue(String fieldName, String defaultValue) {
		if (defaultValues == null) {
			defaultValues = new HashMap<String, String>();
		}
		defaultValues.put(fieldName, defaultValue);
		return this;
	}

	protected AbstractQueryStringBuilder exclude(String... excludeFieldNames) {
		this.excludeFieldNames = Collections.toSet(excludeFieldNames);
		return this;
	}

	protected AbstractQueryStringBuilder exclude(Collection<String> excludeFieldNames) {
		this.excludeFieldNames = Collections.toSet(excludeFieldNames);
		return this;
	}

	protected AbstractQueryStringBuilder include(String... includeFieldNames) {
		this.includeFieldNames = Collections.toSet(includeFieldNames);
		return this;
	}

	protected AbstractQueryStringBuilder include(Collection<String> includeFieldNames) {
		this.includeFieldNames = Collections.toSet(includeFieldNames);
		return this;
	}

	protected AbstractQueryStringBuilder withPrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	protected AbstractQueryStringBuilder applyField(String applyQueryFieldName) {
		this.applyQueryFieldName = applyQueryFieldName;
		return this;
	}

	protected String buildUrl(Object value) {
		return buildUrl(applyQueryFieldName, value);
	}

	private String buildUrl(String queryParamName, Object value) {
        return (prefix == null ? "" : prefix) + constructQueryUrl(queryParamName, value);
	}

	private String constructQueryUrl(String fieldName, Object fieldValue) {
		StringBuilder sb = new StringBuilder();
		List<Field> fields = QueryParamHelper.getQueryParamFields(criteria.getClass());
		try {
			for (Field f : fields) {
				String fn = f.getName();
				if (!fn.equals(fieldName) && includeFieldNames != null && !includeFieldNames.isEmpty()) {
					if (!includeFieldNames.contains(fn)) {
						continue;
					}
				}

				if (!fn.equals(fieldName) && excludeFieldNames != null && !excludeFieldNames.isEmpty()) {
					if (excludeFieldNames.contains(fn)) {
						continue;
					}
				}

				Object fv = fn.equals(fieldName) ? fieldValue : f.get(criteria);
				if (fv == null) {
					continue;
				}

				String defaultValue = hasDefaultValue(fn)
						? getDefaultValue(fn)
						: QueryParamHelper.getDefaultValue(f);

				if (fv.toString().equals(defaultValue)) {
					continue;
				}

				String queryParamName = QueryParamHelper.getQueryParamName(f);
				sb.append('&').append(queryParamName).append("=")
						.append(URLEncoder.encode(fv.toString(), "UTF-8"));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (sb.length() == 0) {
			return "";
		}
		sb.setCharAt(0, '?');
		return sb.toString();
	}

	private boolean hasDefaultValue(String fieldName) {
		return defaultValues != null && defaultValues.containsKey(fieldName);
	}

	private String getDefaultValue(String fieldName) {
		return defaultValues.get(fieldName);
	}

}
