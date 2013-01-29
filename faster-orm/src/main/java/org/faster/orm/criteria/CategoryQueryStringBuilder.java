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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.faster.commons.Beans;

/**
 * @author sqwen
 */
public class CategoryQueryStringBuilder extends AbstractQueryStringBuilder {

	private final Collection<?> values;

	// 针对的查询字段名
	private String valueFieldName = "id";

	// 是否构建“所有”或“不限”的查询字符串
	private boolean withoutAll = false;

	public CategoryQueryStringBuilder(Object criteria, Collection<?> values, String applyFieldName) {
		this.criteria = criteria;
		this.values = values;
		this.applyQueryFieldName = applyFieldName;
	}

	public CategoryQueryStringBuilder atField(String key) {
		valueFieldName = key;
		return this;
	}

	public CategoryQueryStringBuilder withoutAll() {
		withoutAll = true;
		return this;
	}

	@Override
	public CategoryQueryStringBuilder setDefaultValue(String fieldName, String defaultValue) {
		super.setDefaultValue(fieldName, defaultValue);
		return this;
	}

	@Override
	public CategoryQueryStringBuilder exclude(String... excludeFieldNames) {
		super.exclude(excludeFieldNames);
		return this;
	}

	@Override
	public CategoryQueryStringBuilder exclude(Collection<String> excludeFieldNames) {
		super.exclude(excludeFieldNames);
		return this;
	}

	@Override
	public CategoryQueryStringBuilder include(String... includeFieldNames) {
		super.include(includeFieldNames);
		return this;
	}

	@Override
	public CategoryQueryStringBuilder include(Collection<String> includeFieldNames) {
		super.include(includeFieldNames);
		return this;
	}

	@Override
	public CategoryQueryStringBuilder withPrefix(String prefix) {
		super.withPrefix(prefix);
		return this;
	}

	@Override
	public CategoryQueryStringBuilder applyField(String applyQueryFieldName) {
		super.applyField(applyQueryFieldName);
		return this;
	}

	public Map<String, String> build() {
		Map<String, String> map = new HashMap<String, String>();
		if (!withoutAll) {
			map.put("all", buildUrl(null));
		}

		if (values == null || values.isEmpty()) {
			return map;
		}

		for (Object value : values) {
			Object fv = Beans.getProperty(value, valueFieldName);
			String url = buildUrl(fv);
			String key = String.valueOf(fv);
			map.put(key, url);
		}
		return map;
	}

}
