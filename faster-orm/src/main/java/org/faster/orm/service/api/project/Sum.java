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
public interface Sum<PO extends GenericEntity<ID>, ID extends Serializable> {

    <T> T sum(String sumPropertyName);

    <T> T sum(String sumPropertyName, PO... pos);

    <T> T sum(String sumPropertyName, Collection<PO> pos);

    <T> T sumById(String sumPropertyName, ID... ids);

    <T> T sumById(String sumPropertyName, Collection<ID> ids);

    <T> T sumByCriteria(String sumPropertyName, DetachedCriteria dc);

    <T> T sumByPropertyAndValue(String sumPropertyName, String propertyName, Object propertyValue);

    <T> T sumByPropertyValueMap(String sumPropertyName, Map<String, Object> propertyValueMap);

    <T> T sumByByExample(String sumPropertyName, PO example);

}
