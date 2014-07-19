/*
 * Copyright (c) 2014 @iSQWEN. All rights reserved.
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

package org.faster.orm.service.api.project;

import org.faster.orm.model.GenericEntity;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author sqwen
 */
public interface Max<PO extends GenericEntity<ID>, ID extends Serializable> {

    <T> T max(String maxPropertyName);

    <T> T max(String maxPropertyName, PO... pos);

    <T> T max(String maxPropertyName, Collection<PO> pos);

    <T> T maxById(String maxPropertyName, ID... ids);

    <T> T maxById(String maxPropertyName, Collection<ID> ids);

    <T> T maxByCriteria(String maxPropertyName, DetachedCriteria dc);

    <T> T maxByPropertyAndValue(String maxPropertyName, String propertyName, Object propertyValue);

    <T> T maxByPropertyValueMap(String maxPropertyName, Map<String, Object> propertyValueMap);

    <T> T maxByByExample(String maxPropertyName, PO example);

}
