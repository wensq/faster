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
package org.faster.opm;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.faster.util.NestParam;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 资源查询条件封装类
 *
 * @author sqwen
 * @version 1.0, 2012-5-11
 */
public class ResourceCriteria implements Serializable {

    private static final long serialVersionUID = -7042288763897901952L;

    // 资源类型
    private String resourceType;

    // 资源过滤键值对
    // key: propertyId
    // value: propertyValues
    // 不同key之间是**与**的关系，同一个key不同值之间是**或**的关系
    private Map<String, String> resourceFilterParams;

    // 多个资源过滤值的分隔符号
    private String resourceFilterValueDelimiter = ",";

    // 属性过滤方式
    private PropertyFilterType propertyFilterType = PropertyFilterType.NONE;

    // 属性过滤值
    private String[] propertyFilterValues;

    // 需要同时返回的动态属性标记
    private List<NestParam> dynamicPropertyParams;

    // 返回的属性字段过滤，过滤的属性字段值将设置为null
    private Set<String> attributeFilterValues;

    // --------------------
    // feature methods
    // --------------------

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }

    public boolean existResourceFilter() {
        return resourceFilterParams != null && !resourceFilterParams.isEmpty();
    }

    public boolean isPropertyFilterRequired() {
        return propertyFilterType != null && propertyFilterType != PropertyFilterType.NONE;
    }

    public boolean isAttributeFilterRequired() {
        return attributeFilterValues != null && !attributeFilterValues.isEmpty();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (attributeFilterValues == null ? 0 : attributeFilterValues.hashCode());
        result = prime * result + (dynamicPropertyParams == null ? 0 : dynamicPropertyParams.hashCode());
        result = prime * result + (propertyFilterType == null ? 0 : propertyFilterType.hashCode());
        result = prime * result + Arrays.hashCode(propertyFilterValues);
        result = prime * result + (resourceFilterParams == null ? 0 : resourceFilterParams.hashCode());
        result = prime * result
                + (resourceFilterValueDelimiter == null ? 0 : resourceFilterValueDelimiter.hashCode());
        result = prime * result + (resourceType == null ? 0 : resourceType.hashCode());
        return result;
    }

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
        ResourceCriteria other = (ResourceCriteria) obj;
        if (attributeFilterValues == null) {
            if (other.attributeFilterValues != null) {
                return false;
            }
        } else if (!attributeFilterValues.equals(other.attributeFilterValues)) {
            return false;
        }
        if (dynamicPropertyParams == null) {
            if (other.dynamicPropertyParams != null) {
                return false;
            }
        } else if (!dynamicPropertyParams.equals(other.dynamicPropertyParams)) {
            return false;
        }
        if (propertyFilterType != other.propertyFilterType) {
            return false;
        }
        if (!Arrays.equals(propertyFilterValues, other.propertyFilterValues)) {
            return false;
        }
        if (resourceFilterParams == null) {
            if (other.resourceFilterParams != null) {
                return false;
            }
        } else if (!resourceFilterParams.equals(other.resourceFilterParams)) {
            return false;
        }
        if (resourceFilterValueDelimiter == null) {
            if (other.resourceFilterValueDelimiter != null) {
                return false;
            }
        } else if (!resourceFilterValueDelimiter.equals(other.resourceFilterValueDelimiter)) {
            return false;
        }
        if (resourceType == null) {
            if (other.resourceType != null) {
                return false;
            }
        } else if (!resourceType.equals(other.resourceType)) {
            return false;
        }
        return true;
    }

    // --------------------
    // getter/setter
    // --------------------

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public PropertyFilterType getPropertyFilterType() {
        return propertyFilterType;
    }

    public void setPropertyFilterType(PropertyFilterType propertyFilterType) {
        this.propertyFilterType = propertyFilterType;
    }

    public String[] getPropertyFilterValues() {
        return propertyFilterValues;
    }

    public void setPropertyFilterValues(String[] propertyFilterValues) {
        this.propertyFilterValues = propertyFilterValues;
    }

    public Map<String, String> getResourceFilterParams() {
        return resourceFilterParams;
    }

    public void setResourceFilterParams(Map<String, String> resourceFilterParams) {
        this.resourceFilterParams = resourceFilterParams;
    }

    public List<NestParam> getDynamicPropertyParams() {
        return dynamicPropertyParams;
    }

    public void setDynamicPropertyParams(List<NestParam> dynamicPropertyParams) {
        this.dynamicPropertyParams = dynamicPropertyParams;
    }

    public String getResourceFilterValueDelimiter() {
        return resourceFilterValueDelimiter;
    }

    public void setResourceFilterValueDelimiter(String resourceFilterValueDelimiter) {
        this.resourceFilterValueDelimiter = resourceFilterValueDelimiter;
    }

    public Set<String> getAttributeFilterValues() {
        return attributeFilterValues;
    }

    public void setAttributeFilterValues(Set<String> attributeFilterValues) {
        this.attributeFilterValues = attributeFilterValues;
    }

}
