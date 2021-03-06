package org.faster.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sqwen
 */
public final class Maps {

    private Maps() {}

    public static <K, V> Map<K, V> map(K key, V value) {
        return Collections.singletonMap(key, value);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Map<?, ?> map(Object... values) {
        if (values == null || values.length == 0) {
            return Collections.emptyMap();
        }

        if (values.length % 2 != 0) {
            throw new IllegalArgumentException("map parameters must be key-value pair.");
        }

        Map ret = new HashMap();
        for (int i = 0; i < values.length; i += 2) {
            ret.put(values[i], values[i + 1]);
        }
        return ret;
    }

    public static Map<String, ?> slice(Map<String, ?> map, String... keys) {
        return slice(map, true, keys);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Map<String, ?> slice(Map<String, ?> map, boolean ignoreNullValue, String... keys) {
        if (keys == null || keys.length == 0) {
            return map;
        }

        Map ret = new HashMap();
        for (String key : keys) {
            Object value = map.get(key);
            if (ignoreNullValue && Beans.isNullOrEmpty(value)) {
                continue;
            }
            ret.put(key, value);
        }
        return ret;
    }

}
