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
package org.faster.orm.service.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.Options;

/**
 * @author sqwen
 */
public abstract class HibernateGetService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateExistsService<PO, ID> {

	@Override
	public PO get(ID id) {
		return get(id, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<PO> get(ID... ids) {
		return get(Options.getCacheEnabledQueryOption(cacheEnabled), ids);
	}

	@Override
	public List<PO> get(Collection<ID> ids) {
		return get(ids, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

}