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
import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.QueryOption;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.Map;

/**
 * @author sqwen
 */
public abstract class HibernateFindWithOptionService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateGetWithOptionService<PO, ID> {

    @Override
    public PO findFirst(QueryOption queryOption) {
        return findByCriteria(buildCriteria(), queryOption);
    }

    @SuppressWarnings("unchecked")
	@Override
	public PO findByCriteria(DetachedCriteria criteria, QueryOption queryOption) {
		log.info("Finding one {} with cache {} by {}...",
				new Object[] { persistClassName, getCacheDisplay(queryOption.isCacheEnabled()), criteria });
		StopWatch sw = new StopWatch();
		sw.start();

		renderCriteria(criteria);

		PO ret = (PO) fetchSingle(criteria, queryOption);
		postLoad(ret);

		log.info("{} a {}. ({} ms)",
				new Object[] { ret == null ? "Not found" : "Found", persistClassName, sw.getTime() });
		return ret;
	}

	@Override
	public PO findByExample(PO example, QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByExample(example);
		return findByCriteria(dc, queryOption);
	}

	@Override
	public PO findByPropertyAndValue(String propertyName, Object propertyValue, QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByPropertyAndValue(propertyName, propertyValue);
		return findByCriteria(dc, queryOption);
	}

	@Override
	public PO findByPropertyValueMap(Map<String, Object> propertyValueMap, QueryOption queryOption) {
		DetachedCriteria dc = buildCriteriaByPropertyValueMap(propertyValueMap);
		return findByCriteria(dc, queryOption);
	}

}
