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
import java.util.List;
import java.util.Map;

import org.faster.orm.criteria.GenericCriteria;
import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.Options;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;

/**
 * @author sqwen
 */
public abstract class HibernateFindAllService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateFindService<PO, ID> {

	@Override
	public List<PO> findAll() {
		return findAll(Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> findAllByCriteria(DetachedCriteria criteria) {
		return findAllByCriteria(criteria, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> findAllByCriteria(GenericCriteria<PO> criteria) {
		return findAllByCriteria(criteria, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> findAllByExample(PO example) {
		return findAllByExample(example, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> findAllByPropertyAndValue(String propertyName, Object propertyValue) {
		return findAllByPropertyAndValue(propertyName, propertyValue, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> findAllByPropertyAndValueLike(String propertyName, String likeName, MatchMode matchMode) {
		return findAllByPropertyAndValueLike(propertyName, likeName, matchMode,
				Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> findAllByPropertyAndValueIlike(String propertyName, String likeName, MatchMode matchMode) {
		return findAllByPropertyAndValueIlike(propertyName, likeName, matchMode,
				Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> findAllByPropertyIn(String propertyName, List<?> propertyValues) {
		return findAllByPropertyIn(propertyName, propertyValues, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> findAllByPropertyIn(String propertyName, Object... propertyValues) {
		return findAllByPropertyIn(propertyName, propertyValues, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> findAllByPropertyValueMap(Map<String, Object> propertyValueMap) {
		return findAllByPropertyValueMap(propertyValueMap, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> findAllByEachOther(String aPropertyName, Object aPropertyValue,
			String bPropertyName, Object bPropertyValue) {
		return findAllByEachOther(aPropertyName, aPropertyValue, bPropertyName, bPropertyValue,
				Options.getCacheEnabledQueryOption(cacheEnabled));
	}

}
