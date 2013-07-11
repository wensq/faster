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
import org.hibernate.criterion.MatchMode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 从某一记录序号开始获取一批对象，记录序号从0开始计数
 *
 * @author sqwen
 */
public interface FindList<PO extends GenericEntity<ID>, ID extends Serializable> {

    List<PO> findList(int firstIndex, int limit);

    List<PO> findListByCriteria(DetachedCriteria criteria, int firstIndex, int limit);

    List<PO> findListByExample(PO example, int firstIndex, int limit);

    List<PO> findListByPropertyAndValue(String propertyName, Object propertyValue, int firstIndex, int limit);

    List<PO> findListByPropertyAndValueLike(String propertyName, String likeValue, MatchMode matchMode,
                                             int firstIndex, int limit);

    List<PO> findListByPropertyAndValueIlike(String propertyName, String likeValue, MatchMode matchMode,
                                              int firstIndex, int limit);

    List<PO> findListByPropertyValueMap(Map<String, Object> propertyValueMap, int firstIndex, int limit);

}
