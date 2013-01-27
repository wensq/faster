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
import java.util.List;
import java.util.Map;

import org.faster.orm.model.GenericEntity;

/**
 * <code>create</code>是<code>add</code>操作的一种: 需要返回持久化后的对象
 *
 * @author sqwen
 */
public interface Create<PO extends GenericEntity<ID>, ID extends Serializable> {

	PO createFromMap(Map<String, ?> attributes);

	List<PO> createFromMaps(Collection<Map<String, ?>> attributesCollection);

	PO create(PO po);

	List<PO> create(PO... pos);

	List<PO> create(Collection<PO> pos);

}
