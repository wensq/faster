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

/**
 * @author sqwen
 */
public class PaginationQueryStringBuilder extends AbstractQueryStringBuilder {

	private int pageNo;

	private int pageCount;

	private int maxDisplayPages = 10;

	// 是否需要“上一页”、“下一页”的查询参数
	private boolean needPreviousNext = true;

	private boolean needFirstLast = false;

	public PaginationQueryStringBuilder(Object criteria, int pageNo, int pageCount) {
		this.criteria = criteria;
		this.pageNo = pageNo;
		this.pageCount = pageCount;
		this.applyField("page");
	}

	void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public PaginationQueryStringBuilder setDefaultLimit(int limit) {
		return setDefaultValue("limit", limit + "");
	}

	@Override
	public PaginationQueryStringBuilder setDefaultValue(String fieldName, String defaultValue) {
		super.setDefaultValue(fieldName, defaultValue);
		return this;
	}

	public PaginationQueryStringBuilder withoutPreviousNext() {
		needPreviousNext = false;
		return this;
	}

	public PaginationQueryStringBuilder withFirstLast() {
		needFirstLast = true;
		return this;
	}

	public PaginationQueryStringBuilder maxDisplayPages(int maxDisplayPages) {
		this.maxDisplayPages = maxDisplayPages;
		return this;
	}

	@Override
	public PaginationQueryStringBuilder exclude(String... excludeFieldNames) {
		super.exclude(excludeFieldNames);
		return this;
	}

	@Override
	public PaginationQueryStringBuilder exclude(Collection<String> excludeFieldNames) {
		super.exclude(excludeFieldNames);
		return this;
	}

	@Override
	public PaginationQueryStringBuilder include(String... includeFieldNames) {
		super.include(includeFieldNames);
		return this;
	}

	@Override
	public PaginationQueryStringBuilder include(Collection<String> includeFieldNames) {
		super.include(includeFieldNames);
		return this;
	}

	@Override
	public PaginationQueryStringBuilder withPrefix(String prefix) {
		super.withPrefix(prefix);
		return this;
	}

	@Override
	public PaginationQueryStringBuilder applyField(String applyQueryFieldName) {
		super.applyField(applyQueryFieldName);
		return this;
	}

	public PaginationQueryString build() {
		PaginationQueryString ret = new PaginationQueryString();
		ret.setCurrentPageNo(pageNo);
		ret.setPageCount(pageCount);

		if (pageCount <= 1) {
			return ret;
		}

		if (needPreviousNext) {
			addPreviousNextPage(ret);
		}
		addPages(ret);

		return ret;
	}

	private void addPreviousNextPage(PaginationQueryString urls) {
		if (pageNo > 1) {
			urls.setPrevious(buildUrl(pageNo - 1));
		}
		if (pageNo < pageCount) {
			urls.setNext(buildUrl(pageNo + 1));
		}
	}

	private void addPages(PaginationQueryString pqs) {
		if (pageCount <= maxDisplayPages) {
			for (int i = 1; i <= pageCount; i++) {
				pqs.addPage(buildUrl(i));
			}
			pqs.setStartPageNo(1);
			pqs.setEndPageNo(pageCount);
			return;
		}

		int halfDisplayPages = maxDisplayPages / 2;
		int leftCount = pageNo < halfDisplayPages ? pageNo - 1 : halfDisplayPages - 1;
		int startPageNo = pageNo - leftCount;
		int rightCount = pageCount - pageNo >= halfDisplayPages ? halfDisplayPages : pageCount - pageNo;
		if (rightCount < halfDisplayPages) {
			startPageNo = startPageNo + rightCount - halfDisplayPages > 1
					? startPageNo + rightCount - halfDisplayPages : 1;
			leftCount = pageNo - startPageNo;
		}

		pqs.setStartPageNo(startPageNo);
		if (needFirstLast && startPageNo > 1) {
			pqs.setFirst(buildUrl(1));
		}

		for (int i = startPageNo; i <= pageNo; i++) {
			pqs.addPage(buildUrl(i));
		}

		int endPageNo = startPageNo + maxDisplayPages > pageCount ? pageCount : startPageNo + maxDisplayPages - 1;
		pqs.setEndPageNo(endPageNo);
		for (int i = pageNo + 1; i <= endPageNo; i++) {
			pqs.addPage(buildUrl(i));
		}

		if (needFirstLast && endPageNo < pageCount) {
			pqs.setLast(buildUrl(pageCount));
		}
	}

}
