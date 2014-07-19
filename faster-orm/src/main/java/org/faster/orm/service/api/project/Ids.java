/*
 * Copyright (c) 2014 @iSQWEN. All rights reserved.
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

package org.faster.orm.service.api.project;

import org.faster.orm.model.GenericEntity;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author sqwen
 */
public interface Ids<PO extends GenericEntity<ID>, ID extends Serializable> {

    List<ID> ids();

    List<ID> ids(Collection<PO> pos);

    List<ID> ids(PO... pos);

    List<ID> idsByCriteria(DetachedCriteria dc);

    List<ID> idsByPropertyAndValue(String propertyName, Object propertyValue);

    List<ID> idsByPropertyValueMap(Map<String, Object> propertyValueMap);

    List<ID> idsByByExample(PO example);

}
