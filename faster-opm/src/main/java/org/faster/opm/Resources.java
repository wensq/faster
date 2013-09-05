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

import org.faster.orm.pagination.PagedList;
import org.faster.ws.DataCollection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author sqwen
 * @date 2012-4-23
 */
@XmlRootElement(name = "Resources")
@XmlAccessorType(XmlAccessType.NONE)
public class Resources extends DataCollection<Resource> {

    private static final long serialVersionUID = -2208633315714602241L;

    public static final Resources SUCCESS = new Resources(ResourceStatus.SUCCESS);
    public static final Resources NOT_FOUND = new Resources(ResourceStatus.NOT_FOUND);
    public static final Resources INTERNAL_ERROR = new Resources(ResourceStatus.INTERNAL_ERROR);

    // 返回码，含义类似于：HTTP Status
    @XmlAttribute
    private Integer status = ResourceStatus.SUCCESS;

    public Resources(int status) {
        this.status = status;
    }

    public Resources() {
        super();
    }

    public Resources(List<Resource> dtos) {
        super(dtos);
        sanitizeStatus(dtos);
    }

    private void sanitizeStatus(List<Resource> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            status = ResourceStatus.NOT_FOUND;
        }
    }

    public Resources(PagedList<Resource> dtos) {
        super(dtos);
        sanitizeStatus(dtos);
    }

    @XmlElement(name = "Resource")
    @Override
    public List<Resource> getData() {
        return super.getData();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
