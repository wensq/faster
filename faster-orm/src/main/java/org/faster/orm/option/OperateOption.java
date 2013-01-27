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
package org.faster.orm.option;

/**
 * 除查询外的操作选项封装
 *
 * @author sqwen
 */
public class OperateOption {

	private boolean validationEnabled;

	public OperateOption enableValidation() {
		this.validationEnabled = true;
		return this;
	}

	public boolean isValidationEnabled() {
		return validationEnabled;
	}

	public OperateOption enableValidation(boolean validateEnabled) {
		this.validationEnabled = validateEnabled;
		return this;
	}

}
