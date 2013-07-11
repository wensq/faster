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
package org.faster.orm.service.hibernate.with.property;

import org.apache.commons.lang3.time.StopWatch;
import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.Options;
import org.faster.orm.option.QueryOption;
import org.faster.orm.service.GenericServiceWithTimestamp;
import org.faster.orm.service.hibernate.HibernateGenericService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author sqwen
 */
public abstract class HibernateGenericServiceWithTimestamp<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateGenericService<PO, ID>
		implements GenericServiceWithTimestamp<PO, ID> {

	protected abstract String getFieldNameOfTimestamp();

    @Override
	public Date findLastUpdateTime() {
		return findLastUpdateTime(Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public Date findLastUpdateTime(DetachedCriteria criteria) {
		return findLastUpdateTime(Options.getCacheEnabledQueryOption(cacheEnabled));
	}

    @Override
    public PO findLast() {
        return findLast(Options.getCacheEnabledQueryOption(cacheEnabled));
    }

	@Override
	public PO findLastByCriteria(DetachedCriteria criteria) {
		return findLastByCriteria(criteria, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PO findLastByPropertyAndValue(String propertyName, Object propertyValue) {
		return findLastByPropertyAndValue(propertyName, propertyValue, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PO findLastByExample(PO example) {
		return findLastByExample(example, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PO findLastByPropertyValueMap(Map<String, Object> propertyValueMap) {
		return findLastByPropertyValueMap(propertyValueMap, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public Date findLastUpdateTime(QueryOption queryOption) {
		return findLastUpdateTime(buildCriteria(), queryOption);
	}

	@Override
	public Date findLastUpdateTime(DetachedCriteria criteria, QueryOption queryOption) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			log.debug("Finding {} last update time with cache {} by {}",
					new Object[] { persistClassName, getCacheDisplay(queryOption.isCacheEnabled()), criteria });
			sw = new StopWatch();
			sw.start();
		}

		criteria.setProjection(Projections.max(getFieldNameOfTimestamp()));
		Date ret = (Date) fetchSingle(criteria, queryOption);

		if (log.isDebugEnabled()) {
			log.debug("{} last update time is {}. ({} ms)", new Object[] { persistClassName, ret, sw.getTime() });
		}
		return ret;
	}

    @Override
    public PO findLast(QueryOption queryOption) {
        return findLastByCriteria(buildCriteria(), queryOption);
    }

    @SuppressWarnings("unchecked")
	@Override
	public PO findLastByCriteria(DetachedCriteria criteria, QueryOption queryOption) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			log.debug("Finding last {} with cache {} by {}",
					new Object[] { persistClassName, getCacheDisplay(queryOption.isCacheEnabled()), criteria });
			sw = new StopWatch();
			sw.start();
		}

		criteria.addOrder(Order.desc(getFieldNameOfTimestamp()));
		PO ret = (PO) fetchSingle(criteria, queryOption);

		if (log.isDebugEnabled()) {
			log.debug("Last {} is {}. ({} ms)", new Object[] { persistClassName, ret, sw.getTime() });
		}
		return ret;
	}

	@Override
	public PO findLastByPropertyAndValue(String propertyName, Object propertyValue, QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByPropertyAndValue(propertyName, propertyValue);
		return findLastByCriteria(dc, queryOption);
	}

	@Override
	public PO findLastByExample(PO example, QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByExample(example);
		return findLastByCriteria(dc, queryOption);
	}

	@Override
	public PO findLastByPropertyValueMap(Map<String, Object> propertyValueMap, QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByPropertyValueMap(propertyValueMap);
		return findLastByCriteria(dc, queryOption);
	}

}
