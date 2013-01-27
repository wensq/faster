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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * @author sqwen
 */
public abstract class HibernateExistsService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateCountService<PO, ID> {

	@Override
	public boolean exists() {
		return countAll() > 0;
	}

	@Override
	public boolean exists(ID id) {
		DetachedCriteria dc = buildCriteria();
		dc.add(Restrictions.idEq(id));
		return existsByCriteria(dc);
	}

	@Override
	public boolean existsByCriteria(DetachedCriteria criteria) {
		return countByCriteria(criteria) > 0;
	}

	@Override
	public boolean existsByExample(PO example) {
		return countByExample(example) > 0;
	}

	@Override
	public boolean existsByPropertyAndValue(String propertyName, Object propertyValue) {
		return countByPropertyAndValue(propertyName, propertyValue) > 0;
	}

	@Override
	public boolean existsByPropertyValueMap(Map<String, Object> propertyValueMap) {
		return countByPropertyValueMap(propertyValueMap) > 0;
	}

	@Override
	public boolean existsByCriteria(GenericCriteria<PO> criteria) {
		return countByCriteria(criteria) > 0;
	}

}
