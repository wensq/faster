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

package org.faster.commons.exception;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author sqwen
 */
public class ApplicationExceptionTest {

    @Test
    public void testGetMessage() {
        ApplicationException ae = new ApplicationException();
        assertEquals(null, ae.getMessage());

        ae.setErrorCode(HttpErrorCode.OK);
        assertEquals("{ErrorCode=OK(200)}", ae.getMessage());

        ae.setErrorCode(null);
        ae.setAttribute("name", "sqwen");
        assertEquals("{name=sqwen}", ae.getMessage());

        ae.setErrorCode(HttpErrorCode.OK);
        assertEquals("{ErrorCode=OK(200), name=sqwen}", ae.getMessage());

        ae = new ApplicationException("内部错误!").setErrorCode(HttpErrorCode.INTERNAL_ERROR).setAttribute("name", "faster");
        assertEquals("内部错误! {ErrorCode=INTERNAL_ERROR(500), name=faster}", ae.getMessage());
    }

}
