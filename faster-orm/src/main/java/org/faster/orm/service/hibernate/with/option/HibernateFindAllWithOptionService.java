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

import org.apache.commons.lang3.time.StopWatch;
import org.faster.orm.criteria.GenericCriteria;
import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.QueryOption;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author sqwen
 */
public abstract class HibernateFindAllWithOptionService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateFindWithOptionService<PO, ID> {

	@Override
	public List<PO> findAll(QueryOption queryOption) {
		log.info("Finding all {} with cache {}...", persistClassName, getCacheDisplay(cacheEnabled));
		StopWatch sw = new StopWatch();
		sw.start();

		List<PO> ret = findAllByCriteria(buildCriteria(), queryOption);

		log.info("Total {} {} found. ({} ms)", new Object[] { ret.size(), persistClassName, sw.getTime() });
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PO> findAllByCriteria(DetachedCriteria criteria, QueryOption queryOption) {
		log.info("Finding all {} with cache {} by {}...",
				new Object[] { persistClassName, getCacheDisplay(cacheEnabled), criteria });
		StopWatch sw = new StopWatch();
		sw.start();

		List<PO> ret = fetchAll(criteria, queryOption);
        for (PO po : ret) {
            postLoad(po);
        }

		logMultiComplete("Found", ret.size(), sw.getTime());
		return ret;
	}

	@Override
	public List<PO> findAllByCriteria(GenericCriteria<PO> criteria, QueryOption queryOption) {
		return findAllByCriteria(criteria.buildCriteria(), queryOption);
	}

	@Override
	public List<PO> findAllByExample(PO example, QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByExample(example);
		return findAllByCriteria(dc, queryOption);
	}

	@Override
	public List<PO> findAllByPropertyAndValue(String propertyName, Object propertyValue, QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByPropertyAndValue(propertyName, propertyValue);
		return findAllByCriteria(dc, queryOption);
	}

	@Override
	public List<PO> findAllByPropertyAndValueLike(String propertyName, String likeName, MatchMode matchMode,
			QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByPropertyAndValueLike(propertyName, likeName, false, matchMode);
		return findAllByCriteria(dc, queryOption);
	}

	@Override
	public List<PO> findAllByPropertyAndValueIlike(String propertyName, String likeName, MatchMode matchMode,
			QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByPropertyAndValueLike(propertyName, likeName, true, matchMode);
		return findAllByCriteria(dc, queryOption);
	}

	@Override
	public List<PO> findAllByPropertyIn(String propertyName, List<?> propertyValues, QueryOption queryOption) {
		if (propertyValues == null || propertyValues.isEmpty()) {
			return new ArrayList<PO>();
		}

		if (propertyValues.size() <= maxBatchQuerySize) {
			return findAllByCriteria(buildCriteriaByPropertyIn(propertyName, propertyValues));
		}

		List<PO> result = new ArrayList<PO>();
		int lastLength = propertyValues.size() % maxBatchQuerySize;
		int time = propertyValues.size() / maxBatchQuerySize + (lastLength == 0 ? 0 : 1);
		for (int i = 0; i < time; i++) {
			int firstResult = i * maxBatchQuerySize;
			int maxResults = maxBatchQuerySize;
			if (i == time - 1) {
				maxResults = lastLength;
			}
			List<?> subset = propertyValues.subList(firstResult, maxResults + firstResult);
			DetachedCriteria dc = buildCriteriaByPropertyIn(propertyName, subset);
			result.addAll(findAllByCriteria(dc));
		}
		return result;
	}

	private DetachedCriteria buildCriteriaByPropertyIn(String propertyName, List<?> propertyValues) {
		return buildCriteria().add(Restrictions.in(propertyName, propertyValues));
	}

	@Override
	public List<PO> findAllByPropertyIn(String propertyName, Object[] propertyValues, QueryOption queryOption) {
		return findAllByPropertyIn(propertyName, Arrays.asList(propertyValues), queryOption);
	}

	@Override
	public List<PO> findAllByPropertyValueMap(Map<String, Object> propertyValueMap, QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByPropertyValueMap(propertyValueMap);
		return findAllByCriteria(dc, queryOption);
	}

	@Override
	public List<PO> findAllByEachOther(String aPropertyName, Object aPropertyValue, String bPropertyName,
			Object bPropertyValue, QueryOption queryOption) {
		// TODO 改造以提升效率
		DetachedCriteria dc = buildCriteriaByEachOther(aPropertyName, aPropertyValue, bPropertyName, bPropertyValue);
		return findAllByCriteria(dc, queryOption);
	}

	private DetachedCriteria buildCriteriaByEachOther(String aPropertyName, Object aPropertyValue,
			String bPropertyName, Object bPropertyValue) {
		return buildCriteria().add(
				Restrictions.or(
						Restrictions.and(
								Restrictions.eq(aPropertyName, aPropertyValue),
								Restrictions.eq(bPropertyName, bPropertyValue)),
						Restrictions.and(
								Restrictions.eq(bPropertyName, aPropertyValue),
								Restrictions.eq(aPropertyName, bPropertyValue))));
	}

}
