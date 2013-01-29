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
package org.faster.commons;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author sqwen
 */
public class BeansTest {

	@Test
	public void testPopulate() {
		TestBean tb1 = new TestBean();
		TestBean tb2 = new TestBean();
		tb2.setName("sqwen");
		Beans.populate(tb1, tb2);
		assertNotNull(tb1.getName());
		assertEquals(tb1.getName(), tb2.getName());

		tb1.setAge(100);
		Beans.populate(tb1, tb2);
		assertEquals(tb1.getAge(), tb2.getAge());

	}

	@Test
	public void testSlice() {
		TestBean tb1 = new TestBean();
		TestBean tb2 = new TestBean();
		tb2.setName("");
		tb2.setAge(28);
		Beans.slicePopulate(tb1, tb2, "name");
		assertNull(tb1.getName());
		assertEquals(tb1.getAge(), 0);

		tb2.setName("sqwen");
		Beans.slicePopulate(tb1, tb2, "name");
		assertNotNull(tb1.getName());
		assertEquals(tb1.getName(), tb2.getName());
		assertEquals(tb1.getAge(), 0);

		Beans.slicePopulate(tb1, tb2, "age");
		assertEquals(tb1.getAge(), tb2.getAge());

		tb1.setName(null);
		tb2.setAge(0);

		tb2.setName("sqwen");
		tb2.setAge(28);

		Beans.slicePopulate(tb1, tb2, "age", "name");
		assertEquals(tb1, tb2);
	}

}
