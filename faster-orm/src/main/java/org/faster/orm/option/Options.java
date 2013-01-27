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
package org.faster.orm.option;

/**
 * 常用选项提供类
 *
 * @author sqwen
 */
public class Options {

	public static final QueryOption CACHE_ENABLED_QUERY_OPTION = new QueryOption().enableCache();

	public static final QueryOption CACHE_DISABLED_QUERY_OPTION = new QueryOption().enableCache(false);

	public static final ProjectOption DEFAULT_PROJECT_OPTION = new ProjectOption();

	public static final QueryOption getCacheEnabledQueryOption(boolean isCacheEnabled) {
		return isCacheEnabled ? CACHE_ENABLED_QUERY_OPTION : CACHE_DISABLED_QUERY_OPTION;
	}

	public static final ProjectOption getDefaultProjectOption() {
		return DEFAULT_PROJECT_OPTION;
	}

}
