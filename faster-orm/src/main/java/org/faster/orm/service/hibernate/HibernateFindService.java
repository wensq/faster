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

import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.Options;
import org.hibernate.criterion.DetachedCriteria;

/**
 * @author sqwen
 */
public abstract class HibernateFindService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateGetService<PO, ID> {

	@Override
	public PO findByCriteria(DetachedCriteria criteria) {
		return findByCriteria(criteria, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PO findByExample(PO example) {
		return findByExample(example, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PO findByPropertyAndValue(String propertyName, Object propertyValue) {
		return findByPropertyAndValue(propertyName, propertyValue, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PO findByPropertyValueMap(Map<String, Object> propertyValueMap) {
		return findByPropertyValueMap(propertyValueMap, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

}
