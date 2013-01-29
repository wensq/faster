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
import org.faster.orm.criteria.GenericCriteria;
import org.faster.orm.model.GenericEntity;
import org.faster.orm.util.Condition;
import org.faster.orm.util.OrmUtils;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

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

		update(Arrays.asList(pos));
	}

	@Override
	public void update(Collection<PO> pos) {
		if (pos == null || pos.isEmpty()) {
			return;
		}

		if (pos.size() == 1) {
			update(pos.iterator().next());
			return;
		}

		int count = pos.size();
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

	protected void doUpdate(Collection<PO> pos) {
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
		if (attributes == null || attributes.isEmpty()) {
			return;
		}

		if (attributes.size() == 1) {
			Entry<String, ?> entry = attributes.entrySet().iterator().next();
			updateAttribute(id, entry.getKey(), entry.getValue());
			return;
		}

		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			log.debug("Updating {}#{} by {}...", new Object[] { persistClassName, id, attributes });
			sw = new StopWatch();
			sw.start();
		}

		Condition condition = OrmUtils.buildUpdateHQL(persistClassName, idFieldName, id, attributes);

		execute(condition.getStatement(), condition.getValues());

		if (log.isDebugEnabled()) {
			logComplete("Updated", id, sw.getTime());
		}
	}

    @Override
    public void updateAttribute(Collection<ID> ids, String attributeName, Object attributeValue) {
        // TODO Auto-generated method stub
    }

    @Override
    public void updateAttribute(ID[] ids, String attributeName, Object attributeValue) {
        // TODO Auto-generated method stub
    }

    @Override
    public void updateAttributes(Collection<ID> ids, Map<String, ?> attributes) {
        // TODO Auto-generated method stub
    }

    @Override
    public void updateAttributes(ID[] ids, Map<String, ?> attributes) {
        // TODO Auto-generated method stub
    }

    @Override
    public int updateAttribute(DetachedCriteria dc, String attributeName, Object attributeValue) {
        return 0; // TODO Auto-generated method stub
    }

    @Override
    public int updateAttributes(DetachedCriteria dc, Map<String, ?> attributes) {
        return 0; // TODO Auto-generated method stub
    }

    @Override
	public int updateAttribute(GenericCriteria<PO> gc, String attributeName, Object attributeValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateAttributes(GenericCriteria<PO> gc, Map<String, ?> attributes) {
		// TODO Auto-generated method stub
		return 0;
	}

}
