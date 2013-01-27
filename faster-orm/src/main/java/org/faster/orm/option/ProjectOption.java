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
 * @author sqwen
 */
public class ProjectOption {

	private boolean distinct;

	private boolean filterNull;

	public ProjectOption distinct() {
		return distinct(true);
	}

	public ProjectOption distinct(boolean distinct) {
		this.distinct = distinct;
		return this;
	}

	public ProjectOption filterNull() {
		return distinct(true);
	}

	public ProjectOption filterNull(boolean filterNull) {
		this.filterNull = filterNull;
		return this;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public boolean isFilterNull() {
		return filterNull;
	}

}
