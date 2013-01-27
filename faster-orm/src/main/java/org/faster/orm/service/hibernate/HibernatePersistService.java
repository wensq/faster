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
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;
import org.faster.orm.model.GenericEntity;

/**
 * @author sqwen
 */
public abstract class HibernatePersistService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateBuildService<PO, ID> {

	@Override
	public void persistFromMap(Map<String, ?> attributes) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			logBegin("Persisting", attributes);
		}

		doPersist(attributes);
		flush();

		if (log.isDebugEnabled()) {
			logComplete("Persisted", sw.getTime());
		}
	}

	private void doPersist(Map<String, ?> attributes) {
		PO po = buildFromMap(attributes);
		doPersist(po);
	}

	@Override
	public void persistFromMaps(Collection<Map<String, ?>> attributesCollection) {
		if (attributesCollection == null || attributesCollection.isEmpty()) {
			return;
		}

		if (attributesCollection.size() == 1) {
			persistFromMap(attributesCollection.iterator().next());
			return;
		}

		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			logMultiBegin("Persisting", attributesCollection.size());
		}

		for (Map<String, ?> map : attributesCollection) {
			persistFromMap(map);
		}
		flush();

		if (log.isDebugEnabled()) {
			logMultiComplete("Persisted", attributesCollection.size(), sw.getTime());
		}
	}

	@Override
	public void persist(PO po) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			logBegin("Persisting", po);
		}

		doPersist(po);
		flush();

		if (log.isDebugEnabled()) {
			logComplete("Persisted", sw.getTime());
		}
	}

	private void doPersist(PO po) {
		beforeCreate(po);
		getSession().persist(po);
	}

	@Override
	public void persist(PO... pos) {
		if (pos == null || pos.length == 0) {
			log.debug("No {} require to be persisted.", persistClassName);
			return;
		}

		if (pos.length == 1) {
			persist(pos[0]);
			return;
		}

		persist(Arrays.asList(pos));
	}

	@Override
	public void persist(Collection<PO> pos) {
		if (pos == null || pos.isEmpty()) {
			log.debug("No {} require to be persisted.", persistClassName);
			return;
		}

		if (pos.size() == 1) {
			persist(pos.iterator().next());
			return;
		}

		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			logMultiBegin("Persisting", pos.size());
		}

		for (PO po : pos) {
			doPersist(po);
		}
		flush();

		if (log.isDebugEnabled()) {
			logMultiComplete("Persisted", pos.size(), sw.getTime());
		}
	}

}
