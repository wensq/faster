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

package org.faster.orm.service.api;

import org.faster.orm.model.GenericEntity;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 将符合条件的持久化对象的指定属性置空
 * @author sqwen
 */
public interface NullifyProperty<PO extends GenericEntity<ID>, ID extends Serializable> {

    int nullifyProperty(String... propertyNames);

    int nullifyProperty(PO po, String... propertyNames);

    int nullifyProperty(PO[] pos, String... propertyNames);

    int nullifyProperty(Collection<PO> pos, String... propertyNames);

    int nullifyProperty(ID id, String... propertyNames);

    int nullifyProperty(ID[] ids, String... propertyNames);

    int nullifyPropertyById(Collection<ID> ids, String... propertyNames);

    int nullifyPropertyByCriteria(DetachedCriteria criteria, String... propertyNames);

    int nullifyPropertyByExample(PO example, String... propertyNames);

    int nullifyPropertyByPropertyAndValue(String propertyName, Object propertyValue, String... propertyNames);

    int nullifyPropertyByPropertyValueMap(Map<String, Object> propertyValueMap, String... propertyNames);

}
