/*
 * Copyright (c) 2014 @iSQWEN. All rights reserved.
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

package org.faster.test;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class UserTest {

    @Test
    public void testList() throws Exception {
        List<String> orginal = Arrays.asList("spam", "sausage", "spam", "spam", "bacon", "spam", "tomato", "spam");
        List<String> ret = User.list(orginal);
        System.out.println(ret);
    }

}
