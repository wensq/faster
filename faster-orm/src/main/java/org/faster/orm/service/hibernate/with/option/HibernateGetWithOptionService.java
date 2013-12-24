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
package org.faster.orm.service.hibernate.with.option;

import org.apache.commons.lang3.time.StopWatch;
import org.faster.orm.model.GenericEntity;
import org.faster.orm.option.QueryOption;
import org.faster.orm.service.hibernate.HibernateFindPageService;
import org.hibernate.CacheMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author sqwen
 */
public abstract class HibernateGetWithOptionService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateFindPageService<PO, ID> {

	@SuppressWarnings("unchecked")
	@Override
	public PO get(ID id, QueryOption queryOption) {
		StopWatch sw = null;
		if (log.isDebugEnabled()) {
			log.debug("Getting {}#{} with cache {}...",
					new Object[] { persistClassName, id, getCacheDisplay(queryOption.isCacheEnabled()) });
			sw = new StopWatch();
			sw.start();
		}

		PO ret = null;
		if (id != null) {
			CacheMode origCacheMode = getSession().getCacheMode();
			getSession().setCacheMode(queryOption.isCacheEnabled() ? CacheMode.NORMAL : CacheMode.IGNORE);
			ret = (PO) getSession().get(persistClass, id);
			getSession().setCacheMode(origCacheMode);
            if (ret != null) {
                postLoad(ret);
            }
		}

		if (log.isDebugEnabled()) {
			log.debug("{}#{}{} found. ({} ms)",
					new Object[] { persistClassName, id, ret == null ? " not" : "", sw.getTime() });
		}
		return ret;
	}

	@Override
	public List<PO> get(QueryOption queryOption, ID... ids) {
		return get(Arrays.asList(ids), queryOption);
	}

	@Override
	public List<PO> get(Collection<ID> ids, QueryOption queryOption) {
		List<PO> ret = new ArrayList<PO>(ids.size());
		for (ID id : ids) {
			PO po = get(id, queryOption);
			if (po != null) {
				ret.add(po);
			}
		}
		return ret;
	}

}
