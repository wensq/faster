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
package org.faster.orm.service.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.faster.orm.criteria.GenericCriteria;
import org.faster.orm.model.GenericEntity;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;

/**
 * @author sqwen
 */
public interface FindAll<PO extends GenericEntity<ID>, ID extends Serializable> {

	List<PO> findAll();

	List<PO> findAllByCriteria(DetachedCriteria criteria);

	List<PO> findAllByCriteria(GenericCriteria<PO> criteria);

	List<PO> findAllByExample(PO example);

	List<PO> findAllByPropertyAndValue(String propertyName, Object propertyValue);

	List<PO> findAllByPropertyAndValueLike(String propertyName, String likeName, MatchMode matchMode);

	List<PO> findAllByPropertyAndValueIlike(String propertyName, String likeName, MatchMode matchMode);

	List<PO> findAllByPropertyIn(String propertyName, List<?> propertyValues);

	List<PO> findAllByPropertyIn(String propertyName, Object... propertyValues);

	List<PO> findAllByPropertyValueMap(Map<String, Object> propertyValueMap);

	List<PO> findAllByEachOther(String aPropertyName, Object aPropertyValue, String bPropertyName, Object bPropertyValue);

}
