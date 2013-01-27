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

import java.io.Serializable;

/**
 * 分页接口
 *
 * @author sqwen
 */
public interface Pagination extends Serializable {

	/**
	 * 是否是首页
	 *
	 * @return 首页标识
	 */
	boolean isFirstPage();

	/**
	 * 是否是最后一页
	 *
	 * @return 末页标识
	 */
	boolean isLastPage();

	/**
	 * 是否有上一页
	 *
	 * @return 上一页标识
	 */
	boolean hasPreviousPage();

	/**
	 * 是否有下一页
	 *
	 * @return 下一页标识
	 */
	boolean hasNextPage();

	/**
	 * 获取当前页索引
	 *
	 * @return 当前页索引
	 */
	int getCurrentPageIndex();

	/**
	 * 获取页号，从1开始计数
	 *
	 * @return 从1开始计数的页号
	 */
	int getPageNo();

	/**
	 * 获取总页数
	 *
	 * @return 总页数
	 */
	int getPageCount();

	/**
	 * 每一页显示的条目数
	 *
	 * @return 每一页显示的条目数
	 */
	int getPageSize();

	/**
	 * 获取当前页记录数
	 *
	 * @return 当前页记录数
	 */
	int getCurrentPageRecordCount();

	/**
	 * 获取总记录数
	 *
	 * @return 总记录数
	 */
	int getTotalRecordCount();

	/**
	 * 获取当前页第一条记录索引
	 *
	 * @return 当前页第一条记录索引
	 */
	int getFirstIndexOfCurrentPage();

	/**
	 * 获取当前页的最后一条记录索引
	 *
	 * @return 当前页的最后一条记录索引
	 */
	int getLastIndexOfCurrentPage();

	/**
	 * 获取上一页的开始索引
	 *
	 * @return 上一页的开始索引
	 */
	int getFirstIndexOfPreviousPage();

	/**
	 * 获取上一页的开始索引
	 *
	 * @return 上一页的开始索引
	 */
	int getFirstIndexOfNextPage();

	/**
	 * 获取指定页的第一条记录索引
	 *
	 * @param pageIndex 页号
	 * @return 指定页的第一条记录索引
	 */
	int getFirstIndexOfPage(int pageIndex);

	/**
	 * 获取指定页的最后一条记录索引
	 *
	 * @param pageIndex 页号
	 * @return 指定页的最后一条记录索引
	 */
	int getLastIndexOfPage(int pageIndex);

}
