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

import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.Options;
import org.faster.orm.option.QueryOption;
import org.faster.orm.pagination.PagedList;
import org.faster.orm.service.GenericServiceWithName;
import org.faster.orm.service.hibernate.HibernateGenericService;
import org.faster.util.Beans;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author sqwen
 */
public class HibernateGenericServiceWithName<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateGenericService<PO, ID>
		implements GenericServiceWithName<PO, ID> {

	/**
	 * name字段名称，子类通过覆盖这个字段，适合特定的name字段需求
	 */
	protected String getFieldNameOfName() {
		return "name";
	}

	@Override
	public PO createByName(String name) {
		return createFromMap(Collections.singletonMap(getFieldNameOfName(), name));
	}

	@Override
	public int changeName(String oldName, String newName) {
		return execute("update " + persistClassName + " set " + getFieldNameOfName() + " = ? where "
				+ getFieldNameOfName() + " = ?", newName, oldName);
	}

	@Override
	public boolean existsByName(String name) {
		return existsByPropertyAndValue(getFieldNameOfName(), name);
	}

	@Override
	public PO findByName(String name) {
		return findByPropertyAndValue(getFieldNameOfName(), name, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> findAllByName(String name) {
		return findAllByName(name, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> findAllByNameLike(String name, MatchMode matchMode) {
		return findAllByNameLike(name, matchMode, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> findAllByNameIlike(String name, MatchMode matchMode) {
		return findAllByNameIlike(name, matchMode, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PagedList<PO> findPageByNameLike(String likeValue, MatchMode matchMode, int page, int limit) {
		return findPageByNameLike(likeValue, matchMode, page, limit, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public PagedList<PO> findPageByNameIlike(String likeValue, MatchMode matchMode, int page, int limit) {
		return findPageByNameIlike(likeValue, matchMode, page, limit, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> findAllByNameIn(String... names) {
		return findAllByPropertyIn(getFieldNameOfName(), new Object[] { names });
	}

	@Override
	public List<PO> findAllByNameIn(Collection<String> names) {
		return findAllByPropertyIn(getFieldNameOfName(), names);
	}

	@Override
	public List<String> projectNameById(ID... ids) {
		return projectNameById(Arrays.asList(ids));
	}

	@Override
	public List<String> projectNameById(Collection<ID> ids) {
		DetachedCriteria dc = buildCriteria().add(Restrictions.in(idFieldName, ids));
		return projectNameByCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> projectNameByCriteria(DetachedCriteria dc) {
		dc.setProjection(Projections.distinct(Projections.property(getFieldNameOfName())));
		return fetchAll(dc);
	}

	@Override
	public List<String> projectName(PO... pos) {
		return projectName(Arrays.asList(pos));
	}

	@Override
	public List<String> projectName(Collection<PO> pos) {
		List<String> names = new ArrayList<String>();
		for (PO po : pos) {
			String name = (String) Beans.getProperty(po, getFieldNameOfName());
			if (name != null) {
				names.add(name);
			}
		}
		return names;
	}

	@Override
	public PO findByName(String name, QueryOption queryOption) {
		return findByPropertyAndValue(getFieldNameOfName(), name, queryOption);
	}

	@Override
	public List<PO> findAllByName(String name, QueryOption queryOption) {
		return findAllByPropertyAndValue(getFieldNameOfName(), name, queryOption);
	}

	@Override
	public List<PO> findAllByNameLike(String name, MatchMode matchMode, QueryOption queryOption) {
		return findAllByPropertyAndValueLike(getFieldNameOfName(), name, matchMode, queryOption);
	}

	@Override
	public List<PO> findAllByNameIlike(String name, MatchMode matchMode, QueryOption queryOption) {
		return findAllByPropertyAndValueIlike(getFieldNameOfName(), name, matchMode, queryOption);
	}

	@Override
	public PagedList<PO> findPageByNameLike(String likeValue, MatchMode matchMode, int page, int limit,
			QueryOption queryOption) {
		return findPageByPropertyAndValueLike(getFieldNameOfName(), likeValue, matchMode, page, limit, queryOption);
	}

	@Override
	public PagedList<PO> findPageByNameIlike(String likeValue, MatchMode matchMode, int page, int limit,
			QueryOption queryOption) {
		return findPageByPropertyAndValueIlike(getFieldNameOfName(), likeValue, matchMode, page, limit, queryOption);
	}

	@Override
	public List<PO> findAllByNameIn(QueryOption queryOption, String... names) {
		return findAllByNameIn(Arrays.asList(names), queryOption);
	}

	@Override
	public List<PO> findAllByNameIn(Collection<String> names, QueryOption queryOption) {
		return findAllByPropertyIn(getFieldNameOfName(), names, queryOption);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> projectNameById(QueryOption queryOption, ID... ids) {
//		return (List<String>) this.<String>projectById(getFieldNameOfName(), ids, queryOption);
        return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> projectNameById(Collection<ID> ids, QueryOption queryOption) {
        return null;
//		return (List<String>) this.<String>projectById(getFieldNameOfName(), ids, queryOption);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> projectNameByCriteria(DetachedCriteria dc, QueryOption queryOption) {
//		return (List<String>) projectByCriteria(getFieldNameOfName(), dc, queryOption);
        return null;
	}

}
