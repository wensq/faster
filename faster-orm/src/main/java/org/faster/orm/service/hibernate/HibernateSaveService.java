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
public abstract class HibernateSaveService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernatePersistService<PO, ID> {

	@Override
	public ID saveFromMap(Map<String, ?> attributes) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			logBegin("Saving", attributes);
		}

		ID id = doSave(attributes);
		flush();

		if (log.isDebugEnabled()) {
			logComplete("Saved", id, sw.getTime());
		}

		return id;
	}

	protected ID doSave(Map<String, ?> attributes) {
		PO po = buildFromMap(attributes);
		return doSave(po);
	}

	@Override
	public List<ID> saveFromMaps(Collection<Map<String, ?>> attributesCollection) {
		if (attributesCollection == null || attributesCollection.isEmpty()) {
			return Collections.emptyList();
		}

		if (attributesCollection.size() == 1) {
			ID id = saveFromMap(attributesCollection.iterator().next());
			return Collections.singletonList(id);
		}

		StopWatch sw = null;
		int count = attributesCollection.size();
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			logMultiBegin("Saving", count);
		}

		List<ID> ids = new ArrayList<ID>(attributesCollection.size());
		for (Map<String, ?> map : attributesCollection) {
			ids.add(saveFromMap(map));
		}
		flush();

		if (log.isDebugEnabled()) {
			logMultiComplete("Saved", count, sw.getTime());
		}
		return ids;
	}

	@Override
	public ID save(PO po) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			logBegin("Saving", po);
		}

		ID id = doSave(po);
		flush();

		if (log.isDebugEnabled()) {
			logComplete("Saved", id, sw.getTime());
		}

		return id;
	}

	@SuppressWarnings("unchecked")
	protected ID doSave(PO po) {
		beforeCreate(po);
		return (ID) getSession().save(po);
	}

	@Override
	public List<ID> save(PO... pos) {
		if (pos == null || pos.length == 0) {
			log.debug("No {} require to be saved.", persistClassName);
			return Collections.emptyList();
		}

		if (pos.length == 1) {
			ID id = save(pos[0]);
			return Collections.singletonList(id);
		}

		return save(Arrays.asList(pos));
	}

	@Override
	public List<ID> save(Collection<PO> pos) {
		if (pos == null || pos.isEmpty()) {
			log.debug("No {} require to be saved.", persistClassName);
			return Collections.emptyList();
		}

		if (pos.size() == 1) {
			ID id = save(pos.iterator().next());
			return Collections.singletonList(id);
		}

		StopWatch sw = null;
		int count = pos.size();
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			logMultiBegin("Saving", count);
		}

		List<ID> ids = new ArrayList<ID>(pos.size());
		for (PO po : pos) {
			ID id = save(po);
			ids.add(id);
		}
		flush();

		if (log.isDebugEnabled()) {
			logMultiComplete("Saved", pos.size(), sw.getTime());
		}
		return ids;
	}

}
