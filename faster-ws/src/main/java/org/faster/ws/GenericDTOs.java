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
package org.faster.ws;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.faster.orm.pagination.PagedList;

/**
 * 通用范型DTO列表基类
 * <p>
 * 子类需要覆盖构造方法和<tt>getValue</tt>的返回XmlElement定义
 *
 * @author sqwen
 */
@XmlRootElement(name = "Resources")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "startNo", "count", "totalCount", "pageNo", "pageCount", "pageSize", "hasPrevious", "hasNext",
		"values" })
public class GenericDTOs<DTO> implements Cloneable, Serializable {

	private static final long serialVersionUID = 246720674451418194L;

	// 本页数
	@XmlAttribute
	protected Integer count;

	// 总数
	@XmlAttribute
	protected Integer totalCount;

	// 页号
	@XmlAttribute
	protected Integer pageNo;

	// 总页数
	@XmlAttribute
	protected Integer pageCount;

	// 页大小
	@XmlAttribute
	protected Integer pageSize;

	@XmlTransient
	protected List<DTO> values;

	public GenericDTOs() {}

	public GenericDTOs(List<DTO> dtos) {
		this.values = dtos;
		if (dtos != null) {
			count = dtos.size();
		}
	}

	public GenericDTOs(PagedList<DTO> dtos) {
		this.values = dtos;
		if (dtos != null) {
			count = dtos.size();
			totalCount = dtos.getTotalRecordCount();
			pageNo = dtos.getCurrentPageIndex() + 1;
			pageCount = dtos.getPageCount();
			pageSize = dtos.getPageSize();
		}
	}

	public void setValues(List<DTO> values) {
		if (values == null) {
			this.values = null;
			count = 0;
			return;
		}

		this.values = values;
		count = values.size();
		if (values instanceof PagedList) {
			PagedList<DTO> dtos = (PagedList<DTO>) values;
			totalCount = dtos.getTotalRecordCount();
			pageNo = dtos.getCurrentPageIndex() + 1;
			pageCount = dtos.getPageCount();
			pageSize = dtos.getPageSize();
		}
	}

	@XmlAttribute
	public Integer getStartNo() {
		if (!isPaged()) {
			return null;
		}
		return (pageNo - 1) * pageSize + 1;
	}

	private boolean isPaged() {
		if (pageNo == null || pageSize == null || pageSize == 0 || pageSize == Integer.MAX_VALUE) {
			return false;
		}
		return true;
	}

	@XmlAttribute
	public Boolean getHasPrevious() {
		if (!isPaged()) {
			return null;
		}
		return pageNo > 1;
	}

	@XmlAttribute
	public Boolean getHasNext() {
		if (!isPaged()) {
			return null;
		}
		return pageNo < pageCount;
	}

	@XmlElement(name = "Resource")
	public List<DTO> getValues() {
		return values;
	}

	public Integer getCount() {
		return count;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
