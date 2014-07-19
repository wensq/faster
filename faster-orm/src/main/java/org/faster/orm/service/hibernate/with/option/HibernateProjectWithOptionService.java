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
import org.faster.orm.service.hibernate.HibernateProjectPageService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author sqwen
 */
public abstract class HibernateProjectWithOptionService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateProjectPageService<PO, ID> {

    @Override
    public <T> T project(Projection projection, QueryOption queryOption) {
        return projectByCriteria(projection, buildCriteria(), queryOption);
    }

    @Override
    public <T> T projectByCriteria(Projection projection, DetachedCriteria criteria, QueryOption queryOption) {
        log.info("Projecting {} of {} with cache {} by {}...",
                projection, persistClassName, getCacheDisplay(queryOption.isCacheEnabled()), criteria);
        StopWatch sw = new StopWatch();
        sw.start();

        renderCriteria(criteria);

        T ret = (T) fetchSingle(criteria, queryOption);

        log.info("Project {}. ({} ms)", ret == null ? "Not found" : ret, sw.getTime());
        return ret;
    }

//	@Override
//	public List<?> projectById(String propertyName, ID[] ids, QueryOption queryOption) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<?> projectByCriteria(String propertyName, DetachedCriteria dc, QueryOption queryOption) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<?> projectById(String propertyName, Collection<ID> ids, QueryOption queryOption) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
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

    @Override
    public <T> List<T> projectById(String propertyName, ID[] ids, QueryOption queryOption) {
        return null;
    }

    @Override
    public <T> List<T> projectById(String propertyName, Collection<ID> ids, QueryOption queryOption) {
        return null;
    }

    @Override
    public <T> List<T> projectAllByCriteria(String propertyName, DetachedCriteria dc, QueryOption queryOption) {
        return null;
    }

    @Override
    public <T> T projectByCriteria(String propertyName, DetachedCriteria dc, QueryOption queryOption) {
        return null;
    }
}
