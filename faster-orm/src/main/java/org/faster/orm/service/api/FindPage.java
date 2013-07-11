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

import org.faster.orm.criteria.GenericCriteria;
import org.faster.orm.model.GenericEntity;
import org.faster.orm.pagination.PagedList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;

import java.io.Serializable;
import java.util.Map;

/**
 * 分页查找接口，页号从1开始计数
 *
 * @author sqwen
 */
public interface FindPage<PO extends GenericEntity<ID>, ID extends Serializable> {

	PagedList<PO> findPage(int page, int limit);

	PagedList<PO> findPageByCriteria(DetachedCriteria criteria, int page, int limit);

	PagedList<PO> findPageByCriteria(GenericCriteria<PO> criteria);

	PagedList<PO> findPageByExample(PO example, int page, int limit);

	PagedList<PO> findPageByPropertyAndValue(String propertyName, Object propertyValue, int page, int limit);

	PagedList<PO> findPageByPropertyAndValueLike(String propertyName, String likeValue, MatchMode matchMode,
			int page, int limit);

	PagedList<PO> findPageByPropertyAndValueIlike(String propertyName, String likeValue, MatchMode matchMode,
			int page, int limit);

	PagedList<PO> findPageByPropertyValueMap(Map<String, Object> propertyValueMap, int page, int limit);

}
