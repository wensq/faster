package org.faster.commons;

import java.util.Arrays;

/**
 * @Author sqwen
 */
public class ArrayUtils {

    private ArrayUtils() {}

    public static final <T> T[] slice(T[] objs, String... slicePropertyNames) {
        return slice(objs, true, slicePropertyNames);
    }

    public static final <T> T[] slice(T[] objs, boolean ignoreNullValue, String... slicePropertyNames) {
        if (objs == null || objs.length == 0) {
            return null;
        }

        if (slicePropertyNames == null || slicePropertyNames.length == 0) {
            return objs;
        }

        T[] ret = Arrays.copyOfRange(objs, 0, objs.length);
        for (int i = 0; i < objs.length; i++) {
            ret[i] = BeanUtils.slice(objs[i], ignoreNullValue, slicePropertyNames);
        }
        return ret;
    }

}
