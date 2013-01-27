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
package org.faster.orm.service.hibernate;

import java.io.Serializable;
import java.util.Map;

import org.faster.orm.criteria.GenericCriteria;
import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.Options;
import org.faster.orm.pagination.PagedList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;

/**
 * @author sqwen
 */
public abstract class HibernateFindPageService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateFindAllService<PO, ID> {

	@Override
	public PagedList<PO> findPage(int page, int limit) {
		return findPage(page, limit, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PagedList<PO> findPageByCriteria(DetachedCriteria criteria, int page, int limit) {
		return findPageByCriteria(criteria, page, limit, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PagedList<PO> findPageByCriteria(GenericCriteria<PO> criteria) {
		return findPageByCriteria(criteria, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PagedList<PO> findPageByExample(PO example, int page, int limit) {
		return findPageByExample(example, page, limit, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PagedList<PO> findPageByPropertyAndValue(String propertyName, Object propertyValue, int page,
			int limit) {
		return findPageByPropertyAndValue(propertyName, propertyValue, page, limit,
				Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PagedList<PO> findPageByPropertyAndValueLike(String propertyName, String likeValue, MatchMode matchMode,
			int page, int limit) {
		return findPageByPropertyAndValueLike(propertyName, likeValue, matchMode, page, limit,
				Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PagedList<PO> findPageByPropertyAndValueIlike(String propertyName, String likeValue, MatchMode matchMode,
			int page, int limit) {
		return findPageByPropertyAndValueIlike(propertyName, likeValue, matchMode, page, limit,
				Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PagedList<PO> findPageByPropertyValueMap(Map<String, Object> propertyValueMap, int page,
			int limit) {
		return findPageByPropertyValueMap(propertyValueMap, page, limit,
				Options.getCacheEnabledQueryOption(cacheEnabled));
	}

}
