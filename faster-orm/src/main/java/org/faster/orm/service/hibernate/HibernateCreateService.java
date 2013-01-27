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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;
import org.faster.orm.model.GenericEntity;

/**
 * @author sqwen
 */
public abstract class HibernateCreateService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateSaveService<PO, ID> {

	@Override
	public PO createFromMap(Map<String, ?> attributes) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			logBegin("Creating", attributes);
		}

		PO ret = doCreate(attributes);
		flush();

		if (log.isDebugEnabled()) {
			logComplete("Created", ret.getId(), sw.getTime());
		}

		return ret;
	}

	protected PO doCreate(Map<String, ?> attributes) {
		PO po = buildFromMap(attributes);
		return doCreate(po);
	}

	@Override
	public List<PO> createFromMaps(Collection<Map<String, ?>> attributesCollection) {
		if (attributesCollection == null || attributesCollection.isEmpty()) {
			return Collections.emptyList();
		}

		if (attributesCollection.size() == 1) {
			PO po = createFromMap(attributesCollection.iterator().next());
			return Collections.singletonList(po);
		}

		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			logMultiBegin("Creating", attributesCollection.size());
		}

		List<PO> ret = new ArrayList<PO>(attributesCollection.size());
		for (Map<String, ?> map : attributesCollection) {
			ret.add(doCreate(map));
		}
		flush();

		if (log.isDebugEnabled()) {
			logMultiComplete("Created", ret.size(), sw.getTime());
		}
		return ret;
	}

	@Override
	public PO create(PO po) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			logBegin("Creating", po);
		}

		PO ret = doCreate(po);
		flush();

		if (log.isDebugEnabled()) {
			logComplete("Created", po.getId(), sw.getTime());
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	protected PO doCreate(PO po) {
		beforeCreate(po);
		return (PO) getSession().merge(persistClassName, po);
	}

	@Override
	public List<PO> create(PO... pos) {
		if (pos == null || pos.length == 0) {
			log.debug("No {} require to be created.", persistClassName);
			return Collections.emptyList();
		}

		if (pos.length == 1) {
			PO po = create(pos[0]);
			return Collections.singletonList(po);
		}

		return create(Arrays.asList(pos));
	}

	@Override
	public List<PO> create(Collection<PO> pos) {
		if (pos == null || pos.isEmpty()) {
			log.debug("No {} require to be created.", persistClassName);
			return Collections.emptyList();
		}

		if (pos.size() == 1) {
			PO po = create(pos.iterator().next());
			return Collections.singletonList(po);
		}

		List<PO> ret = new ArrayList<PO>(pos.size());
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			logMultiBegin("Creating", pos.size());
		}

		for (PO po : pos) {
			PO newPo = create(po);
			ret.add(newPo);
		}
		flush();

		if (log.isDebugEnabled()) {
			logMultiComplete("Created", pos.size(), sw.getTime());
		}
		return ret;
	}

}
