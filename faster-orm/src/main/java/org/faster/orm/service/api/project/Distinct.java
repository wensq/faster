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
import java.util.List;
import java.util.Map;

/**
 * @author sqwen
 */
public interface Distinct<PO extends GenericEntity<ID>, ID extends Serializable> {

    <T> List<T> distinct(String distinctPropertyName);

    <T> List<T> distinct(String distinctPropertyName, PO... pos);

    <T> List<T> distinct(String distinctPropertyName, Collection<PO> pos);

    <T> List<T> distinctById(String distinctPropertyName, ID... ids);

    <T> List<T> distinctById(String distinctPropertyName, Collection<ID> ids);

    <T> List<T> distinctByCriteria(String distinctPropertyName, DetachedCriteria dc);

    <T> List<T> distinctByPropertyAndValue(String distinctPropertyName, String propertyName, Object propertyValue);

    <T> List<T> distinctByPropertyValueMap(String distinctPropertyName, Map<String, Object> propertyValueMap);

    <T> List<T> distinctByByExample(String distinctPropertyName, PO example);

}
