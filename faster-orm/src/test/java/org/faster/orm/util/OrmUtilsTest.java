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
package org.faster.orm.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sqwen
 */
public class OrmUtilsTest {

	@Test
	public void testBuildUpdateHQL() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "sqwen");

		System.out.println(OrmUtils.buildUpdateHQL("Province", "id", new Integer[] {3}, map));
	}

}
