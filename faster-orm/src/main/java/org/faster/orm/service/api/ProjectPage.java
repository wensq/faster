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
import org.faster.orm.pagination.PagedList;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;

/**
 * @author sqwen
 */
public interface ProjectPage<PO extends GenericEntity<ID>, ID extends Serializable> {

	<T> PagedList<T> projectPage(String propertyName, int page, int limit, Class<T> clazz);

	<T> PagedList<T> projectPage(String propertyName, DetachedCriteria dc, int page, int limit, Class<T> clazz);

}
