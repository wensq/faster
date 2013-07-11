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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sqwen
 * @date 2012-4-22
 */
@XmlRootElement(name = "property")
@XmlAccessorType(XmlAccessType.FIELD)
public class Property implements Serializable, Cloneable {

    private static final long serialVersionUID = -6078213559490052059L;

    @XmlAttribute
    private String id;

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String value;

    @XmlAttribute
    private String group;

    @XmlTransient
    private transient Set<String> groupNames;

    @XmlTransient
    private transient boolean changed = false;

    // --------------------
    // feature methods
    // --------------------

    public Property() {}

    public Property(String id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public Property(String id, String name, String value, String group) {
        this(id, name, value);
        this.group = group;
    }

    public boolean hasGroup(String groupName) {
        if (isBlank(groupName)) {
            return isBlank(group);
        }

        if (isBlank(group)) {
            return false;
        }

        if (groupNames == null || changed) {
            groupNames = new HashSet<String>();
            String[] arr = group.split(",");
            for (String gn : arr) {
                groupNames.add(gn.trim());
            }
            changed = false;
        }
        return groupNames.contains(groupName.trim());
    }

    public void setGroup(String group) {
        this.group = group;
        changed = true;
    }

    public void filterAttribute(Set<String> attributeFilterValues) {
        if (!attributeFilterValues.contains("id")) {
            id = null;
        }
        if (!attributeFilterValues.contains("name")) {
            name = null;
        }
        if (!attributeFilterValues.contains("value")) {
            value = null;
        }
        if (!attributeFilterValues.contains("group")) {
            group = null;
        }
    }

    @Override
    public Property clone() {
        try {
            return (Property) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Can't clone " + this, e);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (group == null ? 0 : group.hashCode());
        result = prime * result + (id == null ? 0 : id.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (value == null ? 0 : value.hashCode());
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
        Property other = (Property) obj;
        if (group == null) {
            if (other.group != null) {
                return false;
            }
        } else if (!group.equals(other.group)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    // --------------------
    // getter/setter
    // --------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGroup() {
        return group;
    }

}
