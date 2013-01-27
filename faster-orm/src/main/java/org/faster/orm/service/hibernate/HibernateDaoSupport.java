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
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.Options;
import org.faster.orm.option.QueryOption;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sqwen
 */
public class HibernateDaoSupport<PO extends GenericEntity<ID>, ID extends Serializable> {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected SessionFactory sessionFactory;

	protected final Class<PO> persistClass;

	protected final String persistClassName;

	private volatile List<Field> persistentFields;

	protected boolean cacheEnabled = false;

	protected String cacheRegion;

	protected int maxBatchQuerySize = 1000;

	protected String idFieldName;

	@SuppressWarnings("unchecked")
	public HibernateDaoSupport() {
		persistClass = (Class<PO>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Cache cache = persistClass.getAnnotation(Cache.class);
        cacheEnabled = cache != null && cache.usage() != CacheConcurrencyStrategy.NONE;
		persistClassName = persistClass.getSimpleName();
		parsePersistentFields();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setCacheEnabled(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
	}

	public void setCacheRegion(String cacheRegion) {
		this.cacheRegion = cacheRegion;
	}

	public void setMaxBatchQuerySize(int maxBatchQuerySize) {
		this.maxBatchQuerySize = maxBatchQuerySize;
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	protected String getCacheDisplay(boolean isCacheEnabled) {
		return isCacheEnabled ? "enabled" : "disabled";
	}

	// =============================
	// 供子类覆盖的方法，实现自定义业务逻辑
	// =============================

	protected void beforeCreate(PO po) {}

	protected void beforeUpdate(PO po) {}

	protected boolean isDistinctRootEntity() {
		return false;
	}

	protected boolean isInitialRequired() {
		return false;
	}

	/**
	 * 修饰查询条件，子类覆盖这个方法，可以添加通用的过滤条件
	 */
	protected void renderCriteria(DetachedCriteria criteria) {
		if (isDistinctRootEntity()) {
			criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		}
	}

	/**
	 * 构造空查询条件，方便子类调用
	 *
	 * @return 默认空查询条件
	 */
	protected DetachedCriteria buildCriteria() {
		return DetachedCriteria.forClass(persistClass);
	}

	protected int getIntValue(Object count) {
		if (count instanceof Long) {
			return ((Long) count).intValue();
		}
		return (Integer) count;
	}

	private void parsePersistentFields() {
		persistentFields = new LinkedList<Field>();
		Class<?> superClass = persistClass;
		while (superClass != null) {
			Field[] superFields = superClass.getDeclaredFields();
			for (Field f : superFields) {
				if (Modifier.isStatic(f.getModifiers())) {
					continue;
				}
				if (f.isAnnotationPresent(Transient.class)) {
					continue;
				}
				f.setAccessible(true);
				persistentFields.add(f);
				if (f.isAnnotationPresent(Id.class)) {
					idFieldName = f.getName();
				}
			}
			superClass = superClass.getSuperclass();
		}
	}

	protected void flush() {
		getSession().flush();
	}

	protected Object fetchSingle(final DetachedCriteria criteria) {
		return fetchSingle(criteria, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@SuppressWarnings("rawtypes")
	protected Object fetchSingle(final DetachedCriteria criteria, QueryOption queryOption) {
		List list = fetchPage(criteria, 1, 1, queryOption);
		return list.isEmpty() ? null : list.get(0);
	}

	@SuppressWarnings("rawtypes")
	protected List fetchAll(final DetachedCriteria criteria) {
		return fetchPage(criteria, -1, -1, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@SuppressWarnings("rawtypes")
	protected List fetchAll(final DetachedCriteria criteria, QueryOption queryOption) {
		if (queryOption.isPaginationEnabled()) {
			return fetchPage(criteria, queryOption.getPage(), queryOption.getLimit(), queryOption);
		} else {
			return fetchPage(criteria, -1, -1, queryOption);
		}
	}

	@SuppressWarnings("rawtypes")
	protected List fetchPage(final DetachedCriteria criteria, final int page, final int limit) {
		return fetchPage(criteria, page, limit, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@SuppressWarnings("rawtypes")
	protected List fetchPage(final DetachedCriteria criteria, final int page, final int limit, QueryOption queryOption) {
		renderCriteria(criteria);
		Criteria executableCriteria = criteria.getExecutableCriteria(getSession());
		if (queryOption != null) {
			executableCriteria.setCacheable(queryOption.isCacheEnabled());
			if (cacheRegion != null) {
				executableCriteria.setCacheRegion(cacheRegion);
			}
		}
		if (page >= 1) {
			executableCriteria.setFirstResult((page - 1) * limit);
		}
		if (limit > 0) {
			executableCriteria.setMaxResults(limit);
		}
		return executableCriteria.list();
	}

	protected DetachedCriteria buildCriteriaByExample(PO example) {
		Map<String, Object> map = buildPersistPropertyValueMap(example);
		return buildCriteriaByPropertyValueMap(map);
	}

	protected DetachedCriteria buildCriteriaByPropertyAndValue(String propertyName, Object propertyValue) {
		return buildCriteriaByPropertyValueMap(Collections.singletonMap(propertyName, propertyValue));
	}

	protected DetachedCriteria buildCriteriaByPropertyAndValueLike(String propertyName, String likeValue,
			boolean isIlike, MatchMode matchMode) {
		DetachedCriteria dc = buildCriteria();
		if (matchMode == null) {
			matchMode = MatchMode.START;
		}
		if (isIlike) {
			dc.add(Restrictions.ilike(propertyName, likeValue, matchMode));
		} else {
			dc.add(Restrictions.like(propertyName, likeValue, matchMode));
		}
		return dc;
	}

	protected DetachedCriteria buildCriteriaByPropertyValueMap(Map<String, Object> propertyValueMap) {
		DetachedCriteria dc = buildCriteria();
		if (propertyValueMap != null) {
			for (String propertyName : propertyValueMap.keySet()) {
				Object propertyValue = propertyValueMap.get(propertyName);
				renderCriteriaByPropertyAndValue(dc, propertyName, propertyValue);
			}
		}
		return dc;
	}

	private void renderCriteriaByPropertyAndValue(DetachedCriteria dc, String propertyName, Object propertyValue) {
		if (propertyValue == null) {
			dc.add(Restrictions.isNull(propertyName));
		} else {
			dc.add(Restrictions.eq(propertyName, propertyValue));
		}
	}

	protected Map<String, Object> buildPersistPropertyValueMap(PO example) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for (Field f : persistentFields) {
				Object value = f.get(example);
				if (value != null) {
					map.put(f.getName(), value);
				}
			}
		} catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
		return map;
	}

	public int execute(String queryString) {
		return execute(queryString, (Object[]) null);
	}

	public int execute(String queryString, Object value) {
		return execute(queryString, new Object[] { value });
	}

	public int execute(final String queryString, final Object... values) {
		Query queryObject = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}

		return queryObject.executeUpdate();
	}

	public int execute(final String queryString, final List<?> values) {
		Query queryObject = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				queryObject.setParameter(i, values.get(i));
			}
		}

		return queryObject.executeUpdate();
	}

	protected void logBegin(String actionName, Object obj) {
		log.info("{} {} by {}...", new Object[] { actionName, persistClassName, obj });
	}

	protected void logComplete(String actionName, long elapsed) {
		log.info("{} a {}. ({} ms)", new Object[] { actionName, persistClassName, elapsed });
	}

	protected void logComplete(String actionName, ID id, long elapsed) {
		log.info("{} {}#{}. ({} ms)", new Object[] { actionName, persistClassName, id, elapsed });
	}

	protected void logMultiBegin(String actionName, int count) {
		log.info("{} {} {}...", new Object[] { actionName, count, persistClassName });
	}

	// 日志常用操作方法封装
	protected void logMultiComplete(String actionName, int count, long elapsed) {
		log.info("{} {} {}. ({} ms)", new Object[] { actionName, count, persistClassName, elapsed });
	}

}
