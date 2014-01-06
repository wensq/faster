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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author sqwen
 */
public abstract class HibernateBuildService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateExecuteService<PO, ID> {

	@Override
	public PO build() {
		try {
			return persistClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Can't build Instance: " + persistClass, e);
		}
	}

	@Override
	public PO build(String propertyName, Object propertyValue) {
		PO po = build();
		po.updateAttribute(propertyName, propertyValue);
		return po;
	}

	@Override
	public PO buildFromMap(Map<String, ?> attributes) {
		PO po = build();
		if (attributes != null && !attributes.isEmpty()) {
			po.updateAttributes(attributes);
		}

		return po;
	}

	@Override
	public List<PO> buildFromMaps(Collection<Map<String, ?>> attributesCollection) {
		if (attributesCollection == null || attributesCollection.isEmpty()) {
			return Collections.emptyList();
		}

		List<PO> pos = new ArrayList<PO>(attributesCollection.size());
		for (Map<String, ?> map : attributesCollection) {
			pos.add(buildFromMap(map));
		}
		return pos;
	}

	@Override
	public PO build(Object dto) {
		PO po = build();
		po.updateAttributes(dto);
		return po;
	}

    @Override
    public List<PO> buildFromDTOs(Collection<?> dtos, String[] permitPropertyNames) {
        List<PO> pos = new ArrayList<PO>(dtos.size());
        for (Object dto : dtos) {
            pos.add(build(dto, permitPropertyNames));
        }
        return pos;
    }

    @Override
    public PO build(Object dto, String[] permitPropertyNames) {
        PO po = build();
        po.updateAttributes(dto, permitPropertyNames);
        return po;
    }

    @Override
	public List<PO> buildFromDTOs(Collection<?> dtos) {
		if (dtos == null || dtos.isEmpty()) {
			return Collections.emptyList();
		}

		if (dtos.size() == 1) {
			PO po = build(dtos.iterator().next());
			return Collections.singletonList(po);
		}

		List<PO> pos = new ArrayList<PO>(dtos.size());
		for (Object dto : dtos) {
			pos.add(build(dto));
		}
		return pos;
	}

    protected void postLoad(PO po) {
        if (po == null) {
            return;
        }

        if (isInitialRequired()) {
            initialize(po);
        }
    }

}
