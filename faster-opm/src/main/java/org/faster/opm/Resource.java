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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sqwen
 * @date 2012-4-22
 */
@XmlRootElement(name = "Resource")
@XmlAccessorType(XmlAccessType.FIELD)
public class Resource implements Serializable, Cloneable {

    private static final long serialVersionUID = 8370201719306362332L;

    public static final Resource SUCCESS = new Resource(ResourceStatus.SUCCESS);
    public static final Resource NOT_FOUND = new Resource(ResourceStatus.NOT_FOUND);
    public static final Resource INTERNAL_ERROR = new Resource(ResourceStatus.INTERNAL_ERROR);

    // 返回码，含义类似于：HTTP Status
    @XmlAttribute
    private Integer status = 200;

    @XmlAttribute
    private String type;

    @XmlAttribute
    private String id;

    @XmlElement(name = "property")
    private List<Property> propertis;

    @XmlTransient
    private transient Map<String, Property> propertyMap;

    // --------------------
    // feature methods
    // --------------------

    public Resource(int status) {
        this.status = status;
    }

    public Resource() {}

    public Resource(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public Resource newResource(PropertyFilterType propertyFilterType, String[] propertyFilterValus) {
        if (propertyFilterType == null || propertyFilterType == PropertyFilterType.NONE
                || propertyFilterValus == null || propertyFilterValus.length == 0) {
            return this;
        }

        Resource ret = new Resource(type, id);
        if (!existProperty()) {
            return ret;
        }

        renderResourceProperties(ret, propertyFilterType, propertyFilterValus);

        return ret;
    }

    public void filterAttribute(Set<String> attributeFilterValues) {
        if (attributeFilterValues == null || attributeFilterValues.isEmpty()) {
            return;
        }

        if (propertis == null || propertis.isEmpty()) {
            return;
        }

        for (Property p : propertis) {
            p.filterAttribute(attributeFilterValues);
        }
    }

    private void renderResourceProperties(Resource resource, PropertyFilterType propertyFilterType,
                                          String[] propertyFilterValues) {
        List<Property> properties = new LinkedList<Property>();
        List<String> values = Arrays.asList(propertyFilterValues);
        for (Property p : propertis) {
            switch (propertyFilterType) {
                case GROUP:
                    for (String value : values) {
                        if (p.hasGroup(value)) {
                            properties.add(p);
                            continue;
                        }
                    }
                    break;
                case ID:
                    if (values.contains(p.getId())) {
                        properties.add(p);
                    }
                    break;
                case NAME:
                    if (values.contains(p.getName())) {
                        properties.add(p);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown PropertyFilterType: " + propertyFilterType);
            }
        }
        resource.setPropertis(properties);
    }

    /**
     * 抽取属性中特定组名的属性组成新的Resource对象返回
     *
     * @param groupName
     * @return
     */
    public Resource newResourceWithGroup(String groupName) {
        return newResource(PropertyFilterType.GROUP, new String[] { groupName });
    }

    public Resource newResourceWithGroups(String groupNames, String delimiter) {
        if (isBlank(groupNames)) {
            return this;
        }

        if (isBlank(delimiter)) {
            throw new IllegalArgumentException("delimiter for groupNames is required.");
        }

        String[] groups = groupNames.split(delimiter);
        return newResourceWithGroups(groups);
    }

    public Resource newResourceWithGroups(String[] groupNames) {
        return newResource(PropertyFilterType.GROUP, groupNames);
    }

    public void addProperty(Property p) {
        if (propertis == null) {
            propertis = new LinkedList<Property>();
        }
        propertis.add(p);
    }

    public boolean existProperty() {
        return propertis != null && !propertis.isEmpty();
    }

    public boolean existProperty(String propertyId) {
        if (propertyMap == null) {
            buildPropertyMap();
        }
        return propertyMap.containsKey(propertyId);
    }

    public String getPropertyValue(String propertyId) {
        if (propertyMap == null) {
            buildPropertyMap();
        }
        Property p = propertyMap.get(propertyId);
        return p == null ? null : p.getValue();
    }

    private void buildPropertyMap() {
        propertyMap = new HashMap<String, Property>();
        if (propertis != null) {
            for (Property p : propertis) {
                propertyMap.put(p.getId(), p);
            }
        }
    }

    @Override
    public Resource clone() {
        try {
            Resource res = (Resource) super.clone();
            if (propertis != null) {
                List<Property> ps = new ArrayList<Property>();
                for (Property p : propertis) {
                    ps.add(p.clone());
                }
                res.setPropertis(ps);
            }
            return res;
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Can't clone " + this, e);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (propertis == null ? 0 : propertis.hashCode());
        result = prime * result + (type == null ? 0 : type.hashCode());
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
        Resource other = (Resource) obj;
        if (propertis == null) {
            if (other.propertis != null) {
                return false;
            }
        } else if (!propertis.equals(other.propertis)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

    // --------------------
    // getter/setter
    // --------------------

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Property> getPropertis() {
        return propertis;
    }

    public void setPropertis(List<Property> propertis) {
        this.propertis = propertis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
