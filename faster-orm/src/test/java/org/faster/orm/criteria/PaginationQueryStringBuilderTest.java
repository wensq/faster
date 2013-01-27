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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * @author sqwen
 */
public class PaginationQueryStringBuilderTest {

	@Test
	public void test() {
		SampleCriteria c = new SampleCriteria();
		PaginationQueryStringBuilder builder = new PaginationQueryStringBuilder(c, 1, 1);
		PaginationQueryString urls = builder.build();
		assertNull(urls.getPrevious());
		assertNull(urls.getNext());
		assertNull(urls.getFirst());
		assertNull(urls.getLast());
		assertNull(urls.getPages());

		builder.setPageCount(8);
		urls = builder.build();
		assertNull(urls.getPrevious());
		assertNotNull(urls.getNext());
		assertNull(urls.getFirst());
		assertNull(urls.getLast());
		assertNotNull(urls.getPages());
		assertEquals(8, urls.getPages().size());

		builder.setPageNo(2);
		urls = builder.build();
		assertNotNull(urls.getPrevious());
		assertNotNull(urls.getNext());
		assertNull(urls.getFirst());
		assertNull(urls.getLast());
		assertNotNull(urls.getPages());
		assertEquals(8, urls.getPages().size());

		builder.setPageNo(6);
		builder.setPageCount(20);
		urls = builder.withFirstLast().build();
		assertNotNull(urls.getPrevious());
		assertNotNull(urls.getNext());
		assertNotNull(urls.getFirst());
		assertNotNull(urls.getLast());
		assertNotNull(urls.getPages());
		assertEquals(10, urls.getPages().size());
	}

}
