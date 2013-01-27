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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.faster.orm.criteria.GenericCriteria;
import org.faster.orm.model.GenericEntity;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;

/**
 * @author sqwen
 */
public abstract class HibernateCountService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateDeleteService<PO, ID> {

	@Override
	public int countAll() {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			log.debug("Counting all {}...", persistClassName);
			sw = new StopWatch();
			sw.start();
		}

		int count = doCount(buildCriteria());

		if (log.isDebugEnabled()) {
			log.debug("Count all {} complete[count={}]. ({} ms)",
					new Object[] { persistClassName, count, sw.getTime() });
		}
		return count;
	}

	@SuppressWarnings("rawtypes")
	private int doCount(DetachedCriteria criteria) {
		// 为了不改变原先的规则，需要克隆
		DetachedCriteria clone = SerializationUtils.clone(criteria);
		clone.setProjection(Projections.rowCount());
		Object value = null;
		try {
			value = fetchSingle(clone);
		} catch (Exception e) { // 有异常，说明有order，需要用反射去掉排序
			Field criteriaImplFiled; // 以下处理有order的场景
			try {
				criteriaImplFiled = DetachedCriteria.class.getDeclaredField("impl");
				criteriaImplFiled.setAccessible(true);
				CriteriaImpl criteriaImpl = (CriteriaImpl) criteriaImplFiled.get(clone);

				Field orderField = CriteriaImpl.class.getDeclaredField("orderEntries");
				orderField.setAccessible(true);
				List<?> orderEntries = (List<?>) orderField.get(criteriaImpl); // 保存原有的order条件

				orderField.set(criteriaImpl, new ArrayList(0)); // 去掉order条件
				try {
					value = fetchSingle(clone);
				} finally {
					orderField.set(criteriaImpl, orderEntries); // 恢复原有的order条件
				}
			} catch (Exception ee) {
				throw new RuntimeException("Can't count " + persistClassName + "![" + criteria
						+ "]", ee);
			}
		}

		return value == null ? 0 : getIntValue(value);
	}

	@Override
	public int countByCriteria(DetachedCriteria criteria) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			log.debug("Counting {}: {}...", persistClassName, criteria);
			sw = new StopWatch();
			sw.start();
		}

		int count = doCount(criteria);

		if (log.isDebugEnabled()) {
			log.debug("Count {} complete[count={}]. ({} ms)", new Object[] { persistClassName, count, sw.getTime() });
		}
		return count;
	}

	@Override
	public int countByExample(PO example) {
		Map<String, Object> map = buildPersistPropertyValueMap(example);
		return countByPropertyValueMap(map);
	}

	@Override
	public int countByPropertyAndValue(String propertyName, Object propertyValue) {
		return countByPropertyValueMap(Collections.singletonMap(propertyName, propertyValue));
	}

	@Override
	public int countByPropertyValueMap(Map<String, Object> propertyValueMap) {
		DetachedCriteria dc = buildCriteriaByPropertyValueMap(propertyValueMap);
		return countByCriteria(dc);
	}

	@Override
	public int countByCriteria(GenericCriteria<PO> criteria) {
		DetachedCriteria dc = criteria.buildCriteria();
		return countByCriteria(dc);
	}

}
