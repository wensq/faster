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
public interface Min<PO extends GenericEntity<ID>, ID extends Serializable> {

    <T> T min(String minPropertyName);

    <T> T min(String minPropertyName, PO... pos);

    <T> T min(String minPropertyName, Collection<PO> pos);

    <T> T minById(String minPropertyName, ID... ids);

    <T> T minById(String minPropertyName, Collection<ID> ids);

    <T> T minByCriteria(String minPropertyName, DetachedCriteria dc);

    <T> T minByPropertyAndValue(String minPropertyName, String propertyName, Object propertyValue);

    <T> T minByPropertyValueMap(String minPropertyName, Map<String, Object> propertyValueMap);

    <T> T minByByExample(String minPropertyName, PO example);

}
