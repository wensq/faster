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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.faster.commons.Beans;
import org.faster.orm.model.GenericEntity;
import org.faster.orm.validation.Errors;
import org.faster.orm.validation.GroupValidator;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * @author sqwen
 */
public abstract class HibernateValidateService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends HibernateStatsService<PO, ID> {

	protected Validator validator;

	protected List<GroupValidator<PO>> groupValidators;

	private Map<Class<?>, GroupValidator<PO>> map;

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public void setGroupValidators(List<GroupValidator<PO>> groupValidators) {
		this.groupValidators = groupValidators;
	}

	private GroupValidator<PO> getGroupValidator(Class<?> group) {
		if (map == null) {
			initGroupValidators();
		}
		return map.get(group);
	}

	protected void initGroupValidators() {
		map = new HashMap<Class<?>, GroupValidator<PO>>();
		if (groupValidators == null) {
			return;
		}

		for (GroupValidator<PO> gv : groupValidators) {
			map.put(gv.validateForGroup(), gv);
		}
	}

	protected void addGroupValidator(GroupValidator<PO> gv) {
		map.put(gv.validateForGroup(), gv);
	}

	@Override
	public boolean isValid(PO po) {
		return isValid(po, Default.class);
	}

	@Override
	public boolean isValid(PO po, Class<?>... groups) {
		for (Class<?> group : groups) {
			Errors<PO> errors = validate(po, group);
			if (errors.hasErrors()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Errors<PO> validate(PO po) {
		return validate(po, Default.class);
	}

	@Override
	public Errors<PO> validate(PO po, Class<?>... groups) {
		Set<ConstraintViolation<PO>> constraintViolations = validator.validate(po, groups);
		Errors<PO> errors = new Errors<PO>(constraintViolations);
		for (Class<?> group : groups) {
			GroupValidator<PO> gv = getGroupValidator(group);
			if (gv != null) {
				gv.validate(po, errors);
			}
		}
		return errors;
	}

	@Override
	public void validatesUniquenessOf(PO po, String propertyName, Errors<PO> errors) {
		validatesUniquenessOf(po, propertyName, null, errors);
	}

	@Override
	public void validatesUniquenessOf(PO po, String[] propertyNames, Errors<PO> errors) {
		for (String propertyName : propertyNames) {
			validatesUniquenessOf(po, propertyName, errors);
		}
	}

	@Override
	public void validatesUniquenessOf(PO po, String propertyName, ID excludeId, Errors<PO> errors) {
		Object value = Beans.getProperty(po, propertyName);
		if (Beans.isNullOrEmpty(value)) {
			return;
		}

		DetachedCriteria dc = buildCriteriaByPropertyAndValue(propertyName, value);
		if (excludeId != null) {
			dc.add(Restrictions.not(Restrictions.idEq(excludeId)));
		}

		if (existsByCriteria(dc)) {
			errors.addErrorCode(propertyName.toUpperCase() + "_ALREADY_EXISTS");
		}
	}

	@Override
	public void validatesUniquenessOf(PO po, String[] propertyNames, ID excludeId, Errors<PO> errors) {
		for (String propertyName : propertyNames) {
			validatesUniquenessOf(po, propertyName, excludeId, errors);
		}
	}

}
