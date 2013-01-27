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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.QueryOption;
import org.faster.orm.pagination.PagedList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;

/**
 * @author sqwen
 */
public interface NameWithOption<PO extends GenericEntity<ID>, ID extends Serializable> {

	PO findByName(String name, QueryOption queryOption);

	List<PO> findAllByName(String name, QueryOption queryOption);

	List<PO> findAllByNameLike(String name, MatchMode matchMode, QueryOption queryOption);

	List<PO> findAllByNameIlike(String name, MatchMode matchMode, QueryOption queryOption);

	PagedList<PO> findPageByNameLike(String likeValue, MatchMode matchMode, int page, int limit,
			QueryOption queryOption);

	PagedList<PO> findPageByNameIlike(String likeValue, MatchMode matchMode, int page, int limit,
			QueryOption queryOption);

	List<PO> findAllByNameIn(QueryOption queryOption, String... names);

	List<PO> findAllByNameIn(Collection<String> names, QueryOption queryOption);

	List<String> projectNameById(QueryOption queryOption, ID... ids);

	List<String> projectNameById(Collection<ID> ids, QueryOption queryOption);

	List<String> projectNameByCriteria(DetachedCriteria c, QueryOption queryOption);

}
