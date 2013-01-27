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
package org.faster.orm.service.hibernate.with.option;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;
import org.faster.orm.criteria.GenericCriteria;
import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.QueryOption;
import org.faster.orm.pagination.PagedList;
import org.faster.orm.pagination.SimplePagedList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;

/**
 * @author sqwen
 */
public abstract class HibernateFindPageWithOptionService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateFindAllWithOptionService<PO, ID> {

	@Override
	public PagedList<PO> findPage(int page, int limit, QueryOption queryOption) {
		return findPageByCriteria(buildCriteria(), page, limit, queryOption);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PagedList<PO> findPageByCriteria(DetachedCriteria criteria, int page, int limit,
			QueryOption queryOption) {
		int count = countByCriteria(criteria);
		log.info("Finding {} [page={} limit={}] by {}", new Object[] { persistClassName, page, limit, criteria });
		StopWatch sw = new StopWatch();
		sw.start();

		if (limit <= 0) {
			limit = Integer.MAX_VALUE;
		}

		List<PO> data = fetchPage(criteria, page, limit, queryOption);
		int firstResult = (page - 1) * limit;
		int expectSize;
		if (count - firstResult >= limit) {
			expectSize = limit;
		} else if (count < firstResult) {
			expectSize = 0;
		} else {
			expectSize = count - firstResult;
		}
		if (data.size() != expectSize) {
			log.warn("{}'s model should be fixed, the FetchMode of OneToMany/ManyToMany should set to Lazy.",
					persistClassName);
		}
		postLoad(data);

		PagedList<PO> ret = new SimplePagedList<PO>(count, firstResult, limit, data);

		log.info("Find {} complete. {total={} page={} limit={} got={}} ({} ms)",
				new Object[] { persistClassName, count, page, limit, data.size(), sw.getTime() });
		return ret;
	}

	@Override
	public PagedList<PO> findPageByCriteria(GenericCriteria<PO> criteria, QueryOption queryOption) {
		return findPageByCriteria(criteria.buildCriteria(), criteria.getPage(), criteria.getLimit(), queryOption);
	}

	@Override
	public PagedList<PO> findPageByExample(PO example, int page, int limit, QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByExample(example);
		return findPageByCriteria(dc, page, limit, queryOption);
	}

	@Override
	public PagedList<PO> findPageByPropertyAndValue(String propertyName, Object propertyValue, int page,
			int limit, QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByPropertyAndValue(propertyName, propertyValue);
		return findPageByCriteria(dc, page, limit, queryOption);
	}

	@Override
	public PagedList<PO> findPageByPropertyAndValueLike(String propertyName, String likeValue, MatchMode matchMode,
			int page, int limit, QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByPropertyAndValueLike(propertyName, likeValue, false, matchMode);
		return findPageByCriteria(dc, page, limit, queryOption);
	}

	@Override
	public PagedList<PO> findPageByPropertyAndValueIlike(String propertyName, String likeValue, MatchMode matchMode,
			int page, int limit, QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByPropertyAndValueLike(propertyName, likeValue, true, matchMode);
		return findPageByCriteria(dc, page, limit, queryOption);
	}

	@Override
	public PagedList<PO> findPageByPropertyValueMap(Map<String, Object> propertyValueMap, int page,
			int limit, QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByPropertyValueMap(propertyValueMap);
		return findPageByCriteria(dc, page, limit, queryOption);
	}

}
