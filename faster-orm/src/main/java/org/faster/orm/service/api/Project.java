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
package org.faster.orm.service.api;

import org.faster.orm.model.GenericEntity;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

/**
 * @author sqwen
 */
public interface Project<PO extends GenericEntity<ID>, ID extends Serializable> {

	<T> List<T> project(String propertyName, Class<T> clazz);

	<T> List<T> projectById(String propertyName, ID[] ids, Class<T> clazz);

	<T> List<T> projectById(String propertyName, List<ID> ids, Class<T> clazz);

	<T> List<T> projectAllByCriteria(String propertyName, DetachedCriteria dc, Class<T> clazz);

    <T> T projectByCriteria(String propertyName, DetachedCriteria dc, Class<T> clazz);

	<T> List<T> project(String propertyName, List<PO> pos, Class<T> clazz);

	List<ID> projectId();

	List<ID> projectIdByCriteria(DetachedCriteria dc);

	List<ID> projectId(List<PO> pos);

}
