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

import static org.apache.commons.lang3.StringUtils.join;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

/**
 * @author sqwen
 */
public class Errors<T> {

	private List<String> errorCodes;

	public Errors() {}

	public Errors(Set<ConstraintViolation<T>> constraintViolations) {
		if (constraintViolations.isEmpty()) {
			return;
		}

		errorCodes = new ArrayList<String>(constraintViolations.size());
		for (ConstraintViolation<?> cv : constraintViolations) {
			errorCodes.add(cv.getMessage());
		}
	}

	public boolean hasErrors() {
		return errorCodes != null;
	}

	public int getErrorCount() {
		return errorCodes == null ? 0 : errorCodes.size();
	}

	public Errors<T> addErrorCode(String errorCode) {
		if (errorCodes == null) {
			errorCodes = new LinkedList<String>();
		}

		if (!errorCodes.contains(errorCode)) {
			errorCodes.add(errorCode);
		}
		return this;
	}

	public List<String> getErrorCodes() {
		return errorCodes;
	}

	public String getJoinedErrorCodes() {
		return getJoinedErrorCodes(",");
	}

	public String getJoinedErrorCodes(String delimiter) {
		return join(errorCodes, delimiter);
	}

}
