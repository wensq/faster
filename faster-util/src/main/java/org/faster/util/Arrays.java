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
package org.faster.util;

import java.lang.reflect.Array;

/**
 * @author sqwen
 */
public final class Arrays {

    private Arrays() {}

    public static <T> T[] sliceProperty(T[] objs, String... slicePropertyNames) {
        return sliceProperty(objs, true, slicePropertyNames);
    }

    public static <T> T[] sliceProperty(T[] objs, boolean ignoreNullValue, String... slicePropertyNames) {
        if (objs == null || objs.length == 0) {
            return null;
        }

        if (slicePropertyNames == null || slicePropertyNames.length == 0) {
            return objs;
        }

        T[] ret = java.util.Arrays.copyOfRange(objs, 0, objs.length);
        for (int i = 0; i < objs.length; i++) {
            ret[i] = Beans.slice(objs[i], ignoreNullValue, slicePropertyNames);
        }
        return ret;
    }

    public static <T> T[] toArray(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        T[] array = (T[]) Array.newInstance(clazz, 1);
        array[0] = obj;
        return array;
    }

}
