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
package org.faster.orm.service.api.with.property;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.faster.orm.model.GenericEntity;
import org.faster.orm.pagination.PagedList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;

/**
 * @author sqwen
 */
public interface Name<PO extends GenericEntity<ID>, ID extends Serializable> {

	PO createByName(String name);

	int changeName(String oldName, String newName);

	boolean existsByName(String name);

	PO findByName(String name);

	List<PO> findAllByName(String name);

	List<PO> findAllByNameLike(String name, MatchMode matchMode);

	List<PO> findAllByNameIlike(String name, MatchMode matchMode);

	PagedList<PO> findPageByNameLike(String likeValue, MatchMode matchMode, int page, int limit);

	PagedList<PO> findPageByNameIlike(String likeValue, MatchMode matchMode, int page, int limit);

	List<PO> findAllByNameIn(String... names);

	List<PO> findAllByNameIn(Collection<String> names);

	List<String> projectNameById(ID... ids);

	List<String> projectNameById(Collection<ID> ids);

	List<String> projectNameByCriteria(DetachedCriteria c);

	/**
	 * 获取一组对象的名称列表
	 */
	List<String> projectName(PO... pos);

	/**
	 * 获取一组对象的名称列表
	 */
	List<String> projectName(Collection<PO> pos);

}
