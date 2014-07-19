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

import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.QueryOption;
import org.faster.orm.pagination.PagedList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;

import java.io.Serializable;
import java.util.List;

/**
 * @author sqwen
 */
public abstract class HibernateProjectPageWithOptionService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateProjectWithOptionService<PO, ID> {

    @Override
    public <T> T project(Projection projection, QueryOption queryOption) {
        return super.project(projection, queryOption);
    }

    @Override
    public <T> T projectByCriteria(Projection projection, DetachedCriteria criteria, QueryOption queryOption) {
        return super.projectByCriteria(projection, criteria, queryOption);
    }

    @Override
    public List<ID> projectId(QueryOption queryOption) {
        return super.projectId(queryOption);
    }

    @Override
    public List<ID> projectIdByCriteria(DetachedCriteria dc, QueryOption queryOption) {
        return super.projectIdByCriteria(dc, queryOption);
    }

    @Override
    public <T> PagedList<T> project(String propertyName, int firstResult, int maxResults, QueryOption queryOption) {
        return null;
    }

    @Override
    public <T> PagedList<T> project(String propertyName, DetachedCriteria dc, int firstResult, int maxResults, QueryOption queryOption) {
        return null;
    }
}
