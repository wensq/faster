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
import org.faster.commons.Collections;
import org.faster.orm.criteria.GenericCriteria;
import org.faster.orm.model.GenericEntity;
import org.faster.orm.util.Condition;
import org.faster.orm.util.OrmUtils;
import org.hibernate.criterion.DetachedCriteria;

import org.faster.commons.Arrays;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.faster.commons.Maps.map;

/**
 * @author sqwen
 */
public abstract class HibernateUpdateService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateCreateService<PO, ID> {

	@Override
	public void update(PO po) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			sw = new StopWatch();
			sw.start();
			logBegin("Updating", po);
		}

		doUpdate(po);
		flush();

		if (log.isDebugEnabled()) {
			logComplete("Updated", po.getId(), sw.getTime());
		}
	}

	@Override
	public void update(PO... pos) {
		if (pos == null || pos.length == 0) {
			log.debug("No {} require to be updated.", persistClassName);
			return;
		}

		if (pos.length == 1) {
			update(pos[0]);
			return;
		}

        int count = pos.length;
        StopWatch sw = null;
        if (log.isDebugEnabled()) {
            sw = new StopWatch();
            sw.start();
            logMultiBegin("Updating", count);
        }

        doUpdate(pos);
        flush();

        if (log.isDebugEnabled()) {
            logMultiComplete("Updated", count, sw.getTime());
        }
	}

	@Override
	public void update(Collection<PO> pos) {
		if (pos == null || pos.isEmpty()) {
			return;
		}

		update(Collections.toArray(pos));
	}

	protected void doUpdate(PO[] pos) {
		for (PO po : pos) {
			doUpdate(po);
		}
	}

	protected void doUpdate(PO po) {
		beforeUpdate(po);
		getSession().update(po);
	}

	@Override
	public void updateAttribute(PO po, String attributeName, Object attributeValue) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			log.debug("Updating {}: {} => {}...", new Object[] { po, attributeName, attributeValue });
			sw = new StopWatch();
			sw.start();
		}

		po.updateAttribute(attributeName, attributeValue);
		doUpdate(po);
		flush();

		if (log.isDebugEnabled()) {
			logComplete("Updated", po.getId(), sw.getTime());
		}
	}

	@Override
	public void updateAttributes(PO po, Map<String, ?> attributes) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			log.debug("Updating {} by {}...", po, attributes);
			sw = new StopWatch();
			sw.start();
		}

		po.updateAttributes(attributes);
		doUpdate(po);
		flush();

		if (log.isDebugEnabled()) {
			logComplete("Updated", po.getId(), sw.getTime());
		}
	}

	@Override
	public void updateAttribute(ID id, String attributeName, Object attributeValue) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			log.debug("Updating {}#{}: {} => {}", new Object[] { persistClassName, id, attributeName, attributeValue });
			sw = new StopWatch();
			sw.start();
		}

		execute("update " + persistClassName + " set " + attributeName + " ? where id = ?", attributeValue, id);

		if (log.isDebugEnabled()) {
			logComplete("Updated", id, sw.getTime());
		}
	}

	@Override
	public void updateAttributes(ID id, Map<String, ?> attributes) {
        updateAttributes(Arrays.toArray(id), attributes);
    }

    @Override
    public int updateAttribute(Collection<ID> ids, String attributeName, Object attributeValue) {
        return updateAttributes(ids, map(attributeName, attributeValue));
    }

    @Override
    public int updateAttribute(ID[] ids, String attributeName, Object attributeValue) {
        return updateAttributes(ids, map(attributeName, attributeValue));
    }

    @Override
    public int updateAttributes(Collection<ID> ids, Map<String, ?> attributes) {
        return updateAttributes(Collections.toArray(ids), attributes);
    }

    @Override
    public int updateAttributes(ID[] ids, Map<String, ?> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return 0;
        }

        StopWatch sw = null;
        if (log.isDebugEnabled()) {
            if (ids.length == 1) {
                log.debug("Updating {}#{} by {}...", new Object[] { persistClassName, ids[0], attributes });
            } else {
                log.debug("Updating {} {}s by {}...", new Object[] { ids.length, persistClassName, attributes });
            }
            sw = new StopWatch();
            sw.start();
        }

        Condition condition = OrmUtils.buildUpdateHQL(persistClassName, idFieldName, ids, attributes);

        int count = execute(condition.getStatement(), condition.getValues());

        if (log.isDebugEnabled()) {
            if (ids.length == 1) {
                logComplete("Updated", ids[0], sw.getTime());
            } else {
                logMultiComplete("Updated", count, sw.getTime());
            }
        }

        return count;
    }

    @Override
    public int updateAttribute(DetachedCriteria dc, String attributeName, Object attributeValue) {
        List<ID> ids = projectIdByCriteria(dc);
        return updateAttribute(ids, attributeName, attributeValue);
    }

    @Override
    public int updateAttributes(DetachedCriteria dc, Map<String, ?> attributes) {
        List<ID> ids = projectIdByCriteria(dc);
        return updateAttributes(ids, attributes);
    }

    @Override
	public int updateAttribute(GenericCriteria<PO> gc, String attributeName, Object attributeValue) {
		return updateAttribute(gc.buildCriteria(), attributeName, attributeValue);
	}

	@Override
	public int updateAttributes(GenericCriteria<PO> gc, Map<String, ?> attributes) {
		return updateAttributes(gc.buildCriteria(), attributes);
	}

}
