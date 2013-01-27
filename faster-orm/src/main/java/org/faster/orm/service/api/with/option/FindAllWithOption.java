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
package org.faster.orm.service.api.with.option;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.faster.orm.criteria.GenericCriteria;
import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.QueryOption;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;

/**
 * @author sqwen
 */
public interface FindAllWithOption<PO extends GenericEntity<ID>, ID extends Serializable> {

	List<PO> findAll(QueryOption queryOption);

	List<PO> findAllByCriteria(DetachedCriteria criteria, QueryOption queryOption);

	List<PO> findAllByCriteria(GenericCriteria<PO> criteria, QueryOption queryOption);

	List<PO> findAllByExample(PO example, QueryOption queryOption);

	List<PO> findAllByPropertyAndValue(String propertyName, Object propertyValue, QueryOption queryOption);

	List<PO> findAllByPropertyAndValueLike(String propertyName, String likeName, MatchMode matchMode,
			QueryOption queryOption);

	List<PO> findAllByPropertyAndValueIlike(String propertyName, String likeName, MatchMode matchMode,
			QueryOption queryOption);

	List<PO> findAllByPropertyIn(String propertyName, List<?> propertyValues, QueryOption queryOption);

	List<PO> findAllByPropertyIn(String propertyName, Object[] propertyValues, QueryOption queryOption);

	List<PO> findAllByPropertyValueMap(Map<String, Object> propertyValueMap, QueryOption queryOption);

	List<PO> findAllByEachOther(String aPropertyName, Object aPropertyValue,
			String bPropertyName, Object bPropertyValue, QueryOption queryOption);

}
