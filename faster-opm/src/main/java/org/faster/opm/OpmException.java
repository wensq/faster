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
package org.faster.opm;

/**
 * OPM的异常封装类
 *
 * @author sqwen
 * @date 2012-4-22
 */
public class OpmException extends RuntimeException {

    private static final long serialVersionUID = -6913236216428291681L;

    public OpmException() {
        super();
    }

    public OpmException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpmException(String message) {
        super(message);
    }

    public OpmException(Throwable cause) {
        super(cause);
    }

}
