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

import org.apache.commons.lang3.time.StopWatch;
import org.faster.orm.model.GenericEntity;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author sqwen
 */
public abstract class HibernateDeleteService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateUpdateService<PO, ID> {

	@Override
	public int delete(PO po) {
		if (po == null) {
			return 0;
		}

		StopWatch sw = new StopWatch();
		sw.start();
		logBegin("Deleting", po);

		doDelete(po);
		flush();

		logComplete("Deleted", po.getId(), sw.getTime());
		return 1;
	}

	@Override
	public int delete(ID id) {
		if (id == null) {
			return 0;
		}

		StopWatch sw = new StopWatch();
		sw.start();
		logBegin("Deleting", persistClassName + "#" + id);

		doDelete(id);

		logComplete("Deleted", id, sw.getTime());
		return 1;
	}

	@Override
	public int deleteAll() {
		try {
			return execute("delete from " + persistClassName);
		} catch (Exception e) {
			return deleteByCriteria(buildCriteria());
		}
	}

	@Override
	public int delete(PO... pos) {
		if (pos == null || pos.length == 0) {
			log.debug("No {} require to be deleted.", persistClassName);
			return 0;
		}

		if (pos.length == 1) {
			return delete(pos[0]);
		}

		return delete(Arrays.asList(pos));
	}

	@Override
	public int delete(Collection<PO> pos) {
		if (pos == null || pos.isEmpty()) {
			log.debug("No {} require to be deleted.", persistClassName);
			return 0;
		}

		if (pos.size() == 1) {
			return delete(pos.iterator().next());
		}

		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			logMultiBegin("Deleting", pos.size());
		}

		doDelete(pos);
		flush();

		int count = pos.size();

		logMultiComplete("Deleted", count, sw.getTime());
		return count;
	}

	private void doDelete(PO po) {
		getSession().delete(po);
	}

	private void doDelete(ID id) {
        try {
            execute("delete from " + persistClassName + " where " + idFieldName + " = ?", id);
        } catch (Exception e) {
            PO po = get(id);
            if (po != null) {
                doDelete(po);
            }
        }
	}

	private void doDelete(Collection<PO> pos) {
		for (PO po : pos) {
			doDelete(po);
		}
	}

	@Override
	public int delete(ID... ids) {
		if (ids == null || ids.length == 0) {
			log.debug("No {} require to be deleted.", persistClassName);
			return 0;
		}

		if (ids.length == 1) {
			return delete(ids[0]);
		}

		return deleteById(Arrays.asList(ids));
	}

	@Override
	public int deleteById(Collection<ID> ids) {
		if (ids == null || ids.isEmpty()) {
			log.debug("No {} require to be deleted.", persistClassName);
			return 0;
		}

		if (ids.size() == 1) {
			return delete(ids.iterator().next());
		}

		StopWatch sw = new StopWatch();
		sw.start();
		logMultiBegin("Deleting", ids.size());

		int count;
		try {
			count = execute("delete from " + persistClassName + " where " + idFieldName + " in ?", ids);
		} catch (Exception e) {
			log.debug("Delete " + persistClassName + " in id list failed, try again...", e);
			List<PO> pos = new ArrayList<PO>(ids.size());
			for (ID id : ids) {
				PO po = build(idFieldName, id);
				pos.add(po);
			}
			doDelete(pos);
			count = pos.size();
		}

		logMultiComplete("Deleted", count, sw.getTime());
		return count;
	}

	@Override
	public int deleteByCriteria(DetachedCriteria criteria) {
		// TODO 考虑性能优化，尽量在不加载数据的情况下进行批量删除
		List<ID> ids = projectIdByCriteria(criteria);
		return deleteById(ids);
	}

	@Override
	public int deleteByExample(PO example) {
		DetachedCriteria dc = buildCriteriaByExample(example);
		return deleteByCriteria(dc);
	}

	@Override
	public int deleteByPropertyAndValue(String propertyName, Object propertyValue) {
		DetachedCriteria dc = buildCriteriaByPropertyAndValue(propertyName, propertyValue);
		return deleteByCriteria(dc);
	}

	@Override
	public int deleteByPropertyValueMap(Map<String, Object> propertyValueMap) {
		DetachedCriteria dc = buildCriteriaByPropertyValueMap(propertyValueMap);
		return deleteByCriteria(dc);
	}

}
