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
public interface Avg<PO extends GenericEntity<ID>, ID extends Serializable> {

    <T> T avg(String avgPropertyName);

    <T> T avg(String avgPropertyName, PO... pos);

    <T> T avg(String avgPropertyName, Collection<PO> pos);

    <T> T avgById(String avgPropertyName, ID... ids);

    <T> T avgById(String avgPropertyName, Collection<ID> ids);

    <T> T avgByCriteria(String avgPropertyName, DetachedCriteria dc);

    <T> T avgByPropertyAndValue(String avgPropertyName, String propertyName, Object propertyValue);

    <T> T avgByPropertyValueMap(String avgPropertyName, Map<String, Object> propertyValueMap);

    <T> T avgByByExample(String avgPropertyName, PO example);

}
