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

import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.Options;
import org.faster.orm.service.hibernate.with.option.HibernateFindPageWithOptionService;
import org.faster.util.Beans;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sqwen
 */
public abstract class HibernateProjectService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateFindPageWithOptionService<PO, ID> {

    @Override
    public <T> List<T> project(String propertyName, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> List<T> projectById(String propertyName, ID[] ids, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> List<T> projectById(String propertyName, List<ID> ids, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> List<T> projectAllByCriteria(String propertyName, DetachedCriteria dc, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T projectByCriteria(String propertyName, DetachedCriteria dc, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> List<T> project(String propertyName, List<PO> pos, Class<T> clazz) {
        List values = new ArrayList();
		for (PO po : pos) {
			Object value = Beans.getProperty(po, propertyName);
			if (value != null) {
				values.add(value);
			}
		}
		return values;
    }

	@Override
	public List<ID> projectId() {
		return projectId(Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<ID> projectIdByCriteria(DetachedCriteria dc) {
		return projectIdByCriteria(dc, Options.getCacheEnabledQueryOption(cacheEnabled));
	}

	@Override
	public List<ID> projectId(List<PO> pos) {
		List<ID> values = new ArrayList<ID>();
		for (PO po : pos) {
			values.add(po.getId());
		}
		return values;
	}

}
