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
package org.faster.orm.pagination;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 分页封装类
 *
 * @author sqwen
 */
public class Page implements Pagination {

	private static final long serialVersionUID = -1950200411752637394L;

	private final int totalRecordCount;

	private int startIndexOfCurrentPage;

	private int pageSize = 50;

	private int currentPageIndex = 0;

	private int pageCount = 0;

	/**
	 * 以总记录数和页大小构造页面对象
	 *
	 * @param totalCount 总记录数
     * @param startIndexOfCurrentPage 当前页开始索引号
	 * @param pageSize 每页记录数
	 */
	public Page(int totalCount, int startIndexOfCurrentPage, int pageSize) {
		if (totalCount < 0) {
			throw new IllegalArgumentException("Record count can't be negative!");
		}
		if (startIndexOfCurrentPage < 0) {
			throw new IllegalArgumentException("StartIndexOfCurrentPage count can't be negative!");
		}
		if (pageSize <= 0) {
			throw new IllegalArgumentException("Page size must be positive!");
		}

		this.totalRecordCount = totalCount;
		this.pageSize = pageSize;
		if (totalCount == 0) {
			this.startIndexOfCurrentPage = 0;
			currentPageIndex = 0;
			pageCount = 0;
		} else {
			// startIndexOfCurrentPage总是正确的情况
			this.startIndexOfCurrentPage = startIndexOfCurrentPage;
			currentPageIndex = startIndexOfCurrentPage / pageSize;
			if (totalCount % pageSize == 0) {
				pageCount = totalCount / pageSize;
			} else {
				pageCount = totalCount / pageSize + 1;
			}

			// startIndexOfCurrentPage可能不正确的情况
			// if (startIndexOfCurrentPage == 0) {
			// currentPageIndex = 0;
			// } else if (startIndexOfCurrentPage < pageSize) {
			// currentPageIndex = 1;
			// } else {
			// if ((startIndexOfCurrentPage) % pageSize == 0) {
			// currentPageIndex = (startIndexOfCurrentPage + 1) / pageSize ;
			// } else {
			// currentPageIndex = (startIndexOfCurrentPage + 1) / pageSize + 1;
			// }
			// }
			//
			// if (totalCount - startIndexOfCurrentPage - 1 <= pageSize) {
			// pageCount = currentPageIndex + 1;
			// } else {
			// if ((totalCount - startIndexOfCurrentPage - 1) % pageSize == 0) {
			// pageCount = currentPageIndex + (totalCount -
			// startIndexOfCurrentPage - 1) / pageSize;
			// } else {
			// pageCount = currentPageIndex + (totalCount -
			// startIndexOfCurrentPage - 1) / pageSize + 1;
			// }
			// }
		}

	}

	/**
	 * 是否是首页
	 *
	 * @return 首页标识
	 */
	@Override
	public boolean isFirstPage() {
		return pageCount == 0 || currentPageIndex == 0;
	}

	/**
	 * 是否是最后一页
	 *
	 * @return 末页标识
	 */
	@Override
	public boolean isLastPage() {
		return pageCount == 0 || currentPageIndex == pageCount - 1;
	}

	/**
	 * 是否有上一页
	 *
	 * @return 上一页标识
	 */
	@Override
	public boolean hasPreviousPage() {
		return !isFirstPage();
	}

	/**
	 * 是否有下一页
	 *
	 * @return 下一页标识
	 */
	@Override
	public boolean hasNextPage() {
		return !isLastPage();
	}

	/**
	 * 获取当前页索引
	 *
	 * @return 当前页索引
	 */
	@Override
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	/**
	 * 翻到上一页
	 */
	public void previousPage() {
		if (isFirstPage()) {
			throw new IllegalStateException("No previous page!");
		}
		currentPageIndex--;
		resetCurrentPage();
	}

	private void resetCurrentPage() {
		startIndexOfCurrentPage = currentPageIndex * pageSize;
	}

	/**
	 * 翻到下一页
	 */
	public void nextPage() {
		if (isLastPage()) {
			throw new IllegalStateException("No next page!");
		}
		currentPageIndex++;
		resetCurrentPage();
	}

	public void turnPageTo(int pageIndex) {
		verifyPageIndex(pageIndex);
		if (currentPageIndex != pageIndex) {
			currentPageIndex = pageIndex;
			resetCurrentPage();
		}
	}

	private void verifyPageIndex(int pageIndex) {
		if (pageIndex >= pageCount) {
			throw new IllegalArgumentException("Page index '" + pageIndex + "' is out of valid page index range[0,"
					+ (pageCount - 1) + "].");
		}
	}

	/**
	 * 获取总页数
	 *
	 * @return 总页数
	 */
	@Override
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * 每一页显示的条目数
	 *
	 * @return 每一页显示的条目数
	 */
	@Override
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 获取当前页记录数
	 */
	@Override
	public int getCurrentPageRecordCount() {
		if (!isLastPage()) {
			return pageSize;
		}
		return getLastIndexOfCurrentPage() - getFirstIndexOfCurrentPage() + 1;
	}

	/**
	 * 获取总记录数
	 */
	@Override
	public int getTotalRecordCount() {
		return totalRecordCount;
	}

	/**
	 * 获取当前页第一条记录索引
	 *
	 * @return 当前页第一条记录索引
	 */
	@Override
	public int getFirstIndexOfCurrentPage() {
		return startIndexOfCurrentPage;
	}

	/**
	 * 获取当前页的最后一条记录索引
	 *
	 * @return 当前页的最后一条记录索引
	 */
	@Override
	public int getLastIndexOfCurrentPage() {
		if (isLastPage()) {
			return totalRecordCount - 1;
		}
		return getFirstIndexOfCurrentPage() + pageSize - 1;
	}

	/**
	 * 获取指定页的第一条记录索引
	 *
	 * @param pageIndex 页号
	 * @return 指定页的第一条记录索引
	 */
	@Override
	public int getFirstIndexOfPage(int pageIndex) {
		verifyPageIndex(pageIndex);
		return pageIndex * pageSize;
	}

	/**
	 * 获取指定页的最后一条记录索引
	 *
	 * @param pageIndex 页号
	 * @return 指定页的最后一条记录索引
	 */
	@Override
	public int getLastIndexOfPage(int pageIndex) {
		verifyPageIndex(pageIndex);
		if (pageIndex == pageCount - 1) {
			return totalRecordCount - 1;
		}
		return (pageIndex + 1) * pageSize - 1;
	}

	@Override
	public String toString() {
		ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		return new ToStringBuilder(this).append("TotalRecordCount", totalRecordCount).append("PageCount", pageCount)
				.append("PageSize", pageSize).append("CurrentPageIndex", currentPageIndex)
				.append("hasPreviousPage", hasPreviousPage()).append("hasNextPage", hasNextPage()).toString();
	}

	@Override
	public int getFirstIndexOfNextPage() {
		return getFirstIndexOfPage(getCurrentPageIndex() + 1);
	}

	@Override
	public int getFirstIndexOfPreviousPage() {
		return getFirstIndexOfPage(getCurrentPageIndex() - 1);
	}

	@Override
	public int getPageNo() {
		return getCurrentPageIndex() + 1;
	}

}
