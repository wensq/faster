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
package org.faster.orm.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

/**
 * @author sqwen
 */
public class GenericValidator<PO> {

	private Validator validator;

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public boolean isValid(PO po) {
		return isValid(po, Default.class);
	}

	public boolean isValid(PO po, Class<?>... groups) {
		for (Class<?> group : groups) {
			Errors<PO> errors = validate(po, group);
			if (errors.hasErrors()) {
				return false;
			}
		}
		return true;
	}

	public Errors<PO> validate(PO po) {
		return validate(po, Default.class);
	}

	public Errors<PO> validate(PO po, Class<?>... groups) {
		Set<ConstraintViolation<PO>> constraintViolations = validator.validate(po, groups);
		return new Errors<PO>(constraintViolations);
	}

}
