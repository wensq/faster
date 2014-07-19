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
package org.faster.orm.service.api.with.option;

import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.QueryOption;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author sqwen
 */
public interface ProjectWithOption<PO extends GenericEntity<ID>, ID extends Serializable> {

    <T> T project(Projection projection, QueryOption queryOption);

    <T> T projectByCriteria(Projection projection, DetachedCriteria dc, QueryOption queryOption);

	<T> List<T> projectById(String propertyName, ID[] ids, QueryOption queryOption);

	<T> List<T> projectById(String propertyName, Collection<ID> ids, QueryOption queryOption);

	<T> List<T> projectAllByCriteria(String propertyName, DetachedCriteria dc, QueryOption queryOption);

    <T> T projectByCriteria(String propertyName, DetachedCriteria dc, QueryOption queryOption);

	List<ID> projectId(QueryOption queryOption);

	List<ID> projectIdByCriteria(DetachedCriteria dc, QueryOption queryOption);

}
