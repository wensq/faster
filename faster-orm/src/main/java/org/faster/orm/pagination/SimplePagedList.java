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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 分页列表实现类
 *
 * @author sqwen
 */
@SuppressWarnings("ALL")
public class SimplePagedList<T> implements PagedList<T> {

	private static final long serialVersionUID = -8683219147485315010L;

	protected List<T> content;

	private final Page page;

	private int totalCount;

	private int firstIndex;

	private int pageSize;

	/**
	 * 分页列表构造行数
	 *
	 * @param totalCount
	 *            总记录数
	 * @param firstIndex
	 *            开始索引号
	 * @param pageSize
	 *            每页记录数
	 * @param content
	 *            当前也记录内容
	 */
	public SimplePagedList(int totalCount, int firstIndex, int pageSize, List<T> content) {
		this.content = content;
		page = new Page(totalCount, firstIndex, pageSize);
	}

	private Page getPage() {
		return page;
	}

    @Override
    public String toString() {
        return page.toString();
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (content == null ? 0 : content.hashCode());
		result = prime * result + firstIndex;
		result = prime * result + (page == null ? 0 : page.hashCode());
		result = prime * result + pageSize;
		result = prime * result + totalCount;
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SimplePagedList other = (SimplePagedList) obj;
		if (content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!content.equals(other.content)) {
			return false;
		}
		if (firstIndex != other.firstIndex) {
			return false;
		}
		if (page == null) {
			if (other.page != null) {
				return false;
			}
		} else if (!page.equals(other.page)) {
			return false;
		}
		if (pageSize != other.pageSize) {
			return false;
		}
		if (totalCount != other.totalCount) {
			return false;
		}
		return true;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public int getFirstIndex() {
		return firstIndex;
	}

	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}

	@Override
	public int getTotalRecordCount() {
		return getPage().getTotalRecordCount();
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public int size() {
		return content.size();
	}

	@Override
	public boolean isEmpty() {
		return content.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return content.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return content.iterator();
	}

	@Override
	public Object[] toArray() {
		return content.toArray();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object[] toArray(Object[] objects) {
		return content.toArray(objects);
	}

	@Override
	public boolean add(T o) {
		return content.add(o);
	}

	@Override
	public boolean remove(Object o) {
		return content.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return content.containsAll(collection);
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		return content.addAll(collection);
	}

	@Override
	public boolean addAll(int i, Collection<? extends T> collection) {
		return content.addAll(i, collection);
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return content.removeAll(collection);
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		return content.retainAll(collection);
	}

	@Override
	public void clear() {
		content.clear();
	}

	@Override
	public T get(int i) {
		return content.get(i);
	}

	@Override
	public T set(int i, T o) {
		return content.set(i, o);
	}

	@Override
	public void add(int i, T o) {
		content.add(i, o);
	}

	@Override
	public T remove(int i) {
		return content.remove(i);
	}

	@Override
	public int indexOf(Object o) {
		return content.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return content.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return content.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int i) {
		return content.listIterator(i);
	}

	@Override
	public List<T> subList(int i, int i1) {
		return content.subList(i, i1);
	}

	@Override
	public int getCurrentPageRecordCount() {
		return getPage().getCurrentPageRecordCount();
	}

	public List<?> getCurrentPageRecords() {
		return content;
	}

	@Override
	public int getFirstIndexOfCurrentPage() {
		return firstIndex;
	}

	@Override
	public int getFirstIndexOfPage(int pageIndex) {
		return getPage().getFirstIndexOfPage(pageIndex);
	}

	@Override
	public int getLastIndexOfCurrentPage() {
		return firstIndex + pageSize - 1;
	}

	@Override
	public int getLastIndexOfPage(int pageIndex) {
		return getPage().getLastIndexOfPage(pageIndex);
	}

	@Override
	public int getPageCount() {
		return getPage().getPageCount();
	}

	@Override
	public int getPageSize() {
		return getPage().getPageSize();
	}

	@Override
	public boolean hasNextPage() {
		return getPage().hasNextPage();
	}

	@Override
	public boolean hasPreviousPage() {
		return getPage().hasPreviousPage();
	}

	@Override
	public boolean isFirstPage() {
		return getPage().isFirstPage();
	}

	@Override
	public boolean isLastPage() {
		return getPage().isLastPage();
	}

	@Override
	public int getCurrentPageIndex() {
		return getPage().getCurrentPageIndex();
	}

	@Override
	public int getFirstIndexOfPreviousPage() {
		return getPage().getFirstIndexOfPreviousPage();
	}

	@Override
	public int getFirstIndexOfNextPage() {
		return getPage().getFirstIndexOfNextPage();
	}

	@Override
	public int getPageNo() {
		return getCurrentPageIndex() + 1;
	}

}
