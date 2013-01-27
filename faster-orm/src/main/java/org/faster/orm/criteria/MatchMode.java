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
package org.faster.orm.criteria;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sqwen
 */
public enum MatchMode {

	NULL, NOT_NULL,
	EMPTY, NOT_EMPTY,
	EQ,
	LIKE, LIKE_START, LIKE_END, LIKE_ANY,
	ILIKE, ILIKE_START, ILIKE_END, ILIKE_ANY,
	LT, LE,
	GT, GE,
	NE,
	BETWEEN,
	OR;

	public static final MatchMode fromString(String matchMode) {
		return isBlank(matchMode) ? EQ : MatchMode.valueOf(matchMode.toUpperCase());
	}

}
