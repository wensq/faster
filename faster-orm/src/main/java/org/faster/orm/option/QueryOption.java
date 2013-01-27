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

import java.io.Serializable;

/**
 * 查询选项封装
 *
 * @author sqwen
 */
public class QueryOption implements Serializable {

	private static final long serialVersionUID = 2249638977086958321L;

	private boolean cacheEnabled;

	private boolean paginationEnabled;

	private int page = 1;

	private int limit = 10;

	public boolean isCacheEnabled() {
		return cacheEnabled;
	}

	public QueryOption enableCache() {
		return enableCache(true);
	}

	public QueryOption enableCache(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
		return this;
	}

	public boolean isPaginationEnabled() {
		return paginationEnabled;
	}

	public QueryOption enablePagination() {
		return enablePagination(true);
	}

	public QueryOption enablePagination(boolean paginationEnabled) {
		this.paginationEnabled = paginationEnabled;
		return this;
	}

	public QueryOption page(int page) {
		this.paginationEnabled = true;
		this.page = page;
		return this;
	}

	public QueryOption limit(int limit) {
		this.limit = limit;
		return this;
	}

	public int getPage() {
		return page;
	}

	public int getLimit() {
		return limit;
	}

}
