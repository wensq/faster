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

import org.faster.orm.pagination.PagedList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

/**
 * 通用范型DTO列表基类
 *
 * @author sqwen
 */
@XmlRootElement(name = "Collection")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "startNo", "count", "totalCount", "pageNo", "pageCount", "pageSize",
        "hasPrevious", "hasNext", "dataType", "data"})
public class DataCollection<DATA> implements Cloneable, Serializable {

	private static final long serialVersionUID = 246720674451418194L;

    @XmlAttribute
    private int status = 200;

    @XmlAttribute
    private String statusCode = "OK";

    @XmlAttribute
    private String message;

    public static final DataCollection SUCCESS = new DataCollection(200, "OK");
    public static final DataCollection NO_CONTENT = new DataCollection(204, "No Content");

    public static final DataCollection error(String message) {
        DataCollection ret = new DataCollection(500, "ERROR");
        ret.setMessage(message);
        return ret;
    }

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

    // 值类型
    @XmlAttribute
    protected String dataType;

	protected List<DATA> data;

	public DataCollection() {}

    public DataCollection(int status, String statusCode) {
        this.status = status;
        this.statusCode = statusCode;
    }

	public DataCollection(List<DATA> data) {
		this.data = data;
		if (data != null) {
			count = data.size();
		}
        initValueType(data);
	}

    private void initValueType(List<DATA> data) {
        if (data == null || data.isEmpty()) {
            status = 204;
            statusCode = "No Content";
            return;
        }

        for (DATA item : data) {
            if (item != null) {
                dataType = item.getClass().getCanonicalName();
                return;
            }
        }
    }

	public DataCollection(PagedList<DATA> data) {
		this.data = data;
        initByPagedList(data);
        initValueType(data);
	}

    private void initByPagedList(PagedList<DATA> data) {
        if (data == null) {
            return;
        }
        count = data.size();
        totalCount = data.getTotalRecordCount();
        pageNo = data.getCurrentPageIndex() + 1;
        pageCount = data.getPageCount();
        pageSize = data.getPageSize();
    }

	public void setData(List<DATA> data) {
		if (data == null || data.isEmpty()) {
			this.data = null;
			count = 0;
            status = 204;
            statusCode = "No Content";
			return;
		}

		this.data = data;
		count = data.size();
        initValueType(data);
    }

    @XmlElement
    public List<DATA> getData() {
        return data;
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
