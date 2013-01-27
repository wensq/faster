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
import java.util.Collection;
import java.util.List;

import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.QueryOption;
import org.faster.orm.service.hibernate.HibernateProjectPageService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;

/**
 * @author sqwen
 */
public abstract class HibernateProjectWithOptionService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateProjectPageService<PO, ID> {

	@Override
	public List<?> project(String propertyName, QueryOption queryOption) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<?> projectById(String propertyName, ID[] ids, QueryOption queryOption) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<?> projectByCriteria(String propertyName, DetachedCriteria dc, QueryOption queryOption) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<?> projectById(String propertyName, Collection<ID> ids, QueryOption queryOption) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ID> projectId(QueryOption queryOption) {
		return projectIdByCriteria(buildCriteria(), queryOption);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ID> projectIdByCriteria(DetachedCriteria dc, QueryOption queryOption) {
		dc.setProjection(Projections.id());
		return fetchAll(dc, queryOption);
	}

}
