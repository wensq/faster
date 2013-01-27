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

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.faster.orm.criteria.GenericCriteria;
import org.faster.orm.model.GenericEntity;
import org.hibernate.criterion.DetachedCriteria;

/**
 * @author sqwen
 */
public interface Update<PO extends GenericEntity<ID>, ID extends Serializable> {

	// 根据指定的持久化对象更新

	void update(PO po);

	void update(PO... pos);

	void update(Collection<PO> pos);

	void updateAttribute(PO po, String attributeName, Object attributeValue);

	void updateAttributes(PO po, Map<String, ?> attributes);

	// 根据对象标识更新

	void updateAttribute(ID id, String attributeName, Object attributeValue);

	void updateAttributes(ID id, Map<String, ?> attributes);

	// 根据查询结果更新

	int updateAttribute(DetachedCriteria dc, String attributeName, Object attributeValue);

	int updateAttributes(DetachedCriteria dc, Map<String, ?> attributes);

	// 根据查询结果更新

	int updateAttribute(GenericCriteria<PO> gc, String attributeName, Object attributeValue);

	int updateAttributes(GenericCriteria<PO> gc, Map<String, ?> attributes);

}
