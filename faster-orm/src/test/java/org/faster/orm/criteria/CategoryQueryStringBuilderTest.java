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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * @author sqwen
 */
public class CategoryQueryStringBuilderTest {

	@Test
	public void testBuildWithoutAll() {
		SampleCriteria sc = new SampleCriteria();
		List<Province> values = new LinkedList<Province>();

		CategoryQueryStringBuilder builder = new CategoryQueryStringBuilder(sc, values, "provinceId");
		Map<String, String> urls = builder.build();
		assertEquals(1, urls.size());
		assertEquals("", urls.values().iterator().next());

		urls = builder.withoutAll().build();
		assertEquals(0, urls.size());
	}

	@Test
	public void testBuildInclude() {
		SampleCriteria sc = new SampleCriteria();
		List<Province> values = new LinkedList<Province>();
		Map<String, String> urls = new CategoryQueryStringBuilder(sc, values, "provinceId").build();
		assertEquals(1, urls.size());
		assertTrue(urls.containsValue(""));

		sc.setPage(2);
		urls = new CategoryQueryStringBuilder(sc, values, "provinceId").include("chain").build();
		assertTrue(urls.containsValue(""));

		urls = new CategoryQueryStringBuilder(sc, values, "provinceId").include("page").build();
		assertTrue(urls.containsValue("?page=2"));

		sc.setChain(true);
		sc.setProvinceId(1);

		urls = new CategoryQueryStringBuilder(sc, values, "provinceId").include("page").build();
		assertTrue(urls.containsValue("?page=2"));

		urls = new CategoryQueryStringBuilder(sc, values, "provinceId").include("page", "chain").build();
		assertTrue(urls.containsValue("?page=2&chain=true"));

		sc.setChain(null);
		urls = new CategoryQueryStringBuilder(sc, values, "provinceId").include("page", "chain", "other").build();
		assertTrue(urls.containsValue("?page=2"));
	}

	@Test
	public void testBuildWithValue() {
		SampleCriteria sc = new SampleCriteria();
		List<Province> values = new LinkedList<Province>();
		values.add(new Province(1, "hz"));

		Map<String, String> urls = new CategoryQueryStringBuilder(sc, values, "provinceId").build();
		assertEquals(2, urls.size());
		assertTrue(urls.containsValue("?provinceId=1"));
		values.add(new Province(2, "nb"));
		urls = new CategoryQueryStringBuilder(sc, values, "provinceId").build();
		assertEquals(3, urls.size());
		assertTrue(urls.containsValue("?provinceId=2"));
	}

	@Test
	public void testBuildWithPrefix() {
		SampleCriteria sc = new SampleCriteria();
		List<Province> values = new LinkedList<Province>();
		values.add(new Province(1, "hz"));

		Map<String, String> urls = new CategoryQueryStringBuilder(sc, values, "provinceId").withPrefix("hz/stores")
				.build();
		assertEquals(2, urls.size());
		assertTrue(urls.containsValue("hz/stores"));
		assertTrue(urls.containsValue("hz/stores?provinceId=1"));
		values.add(new Province(2, "nb"));
		urls = new CategoryQueryStringBuilder(sc, values, "provinceId").withPrefix("hz/stores").build();
		System.out.println(urls);
		assertEquals(3, urls.size());
		assertTrue(urls.containsValue("hz/stores?provinceId=2"));
	}

}
