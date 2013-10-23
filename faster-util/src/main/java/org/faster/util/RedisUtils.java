/*
 * RedisUtils.java was created on 2012-4-22 下午2:24:50
 *
 * Copyright (c) 2012 EASTCOM Software Technology Co.,Ltd. All rights reserved.
 */
package org.faster.util;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.SortParameters.Order;
import org.springframework.data.redis.connection.SortParameters.Range;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 对常用的Redis操作接口进行封装，提供工具方法
 *
 * @author sqwen
 * @date 2012-4-22
 */
public class RedisUtils {

    private static final Log log = LogFactory.getLog(RedisUtils.class);

    private RedisUtils() {}

    /**
     * 拷贝一个键的值到另一个键中
     *
     * @param key
     *            原先的键
     * @param newKey
     *            新键
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static final void copy(RedisTemplate redis, final String key, String newKey) {
        DataType dt = redis.type(key);
        switch (dt) {
            case STRING:
                redis.opsForValue().set(newKey, redis.opsForValue().get(key));
                break;
            case LIST:
                redis.sort(new SortQuery<String>() {
                    public Order getOrder() {
                        return null;
                    }

                    public Boolean isAlphabetic() {
                        return null;
                    }

                    public Range getLimit() {
                        return null;
                    }

                    public String getKey() {
                        return key;
                    }

                    public String getBy() {
                        return "nosort";
                    }

                    public List<String> getGetPattern() {
                        return null;
                    }
                }, newKey);
                break;
            case SET:
                redis.opsForSet().unionAndStore(key, Collections.emptyList(), newKey);
                break;
            case ZSET:
                redis.opsForZSet().unionAndStore(key, Collections.emptyList(), newKey);
            case HASH:
                redis.opsForHash().putAll(newKey, redis.opsForHash().entries(key));
                break;
        }
    }

    /**
     * 获取某个zset中指定score对应的对象
     *
     * @param key
     *            键
     * @param score
     *            排序值
     * @return 指定排序上的值
     */
    @SuppressWarnings("rawtypes")
    public static final Object zgetByScore(RedisTemplate redis, String key, long score) {
        return zgetByScore(redis, key, Double.valueOf(score + ""));
    }

    /**
     * 获取某个zset中指定score对应的对象
     *
     * @param key
     *            键
     * @param score
     *            排序值
     * @return 指定排序上的值
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static final Object zgetByScore(RedisTemplate redis, String key, double score) {
        return getFirst(redis.opsForZSet().rangeByScore(key, score, score));
    }

    /**
     * 获取某个zset中指定排名对应的对象
     *
     * @param key
     *            键
     * @param rank
     *            排序值
     * @return 指定排序上的值
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static final Object zgetByRank(RedisTemplate redis, String key, long rank) {
        return getFirst(redis.opsForZSet().range(key, rank, rank));
    }

    /**
     * 获取某个zset第一个对象
     *
     * @param key
     *            键
     * @return 第一个对象
     */
    @SuppressWarnings("rawtypes")
    public static final Object zgetFirst(RedisTemplate redis, String key) {
        return zgetByRank(redis, key, 0);
    }

    /**
     * 获取某个zset最后一个对象
     *
     * @param key
     *            键
     * @return 最后一个对象
     */
    @SuppressWarnings("rawtypes")
    public static final Object zgetLast(RedisTemplate redis, String key) {
        return zgetByRank(redis, key, -1);
    }

    private static final Object getFirst(Collection<?> c) {
        if (c == null || c.isEmpty()) {
            return null;
        }
        return c.iterator().next();
    }

    /**
     * 根据指定的键值批量获取对应的hash结构数据
     *
     * @param keys
     *            键集合
     * @return 对应一批hash数据
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static final List<Map<String, Object>> multiGetHash(RedisTemplate redis, Collection<String> keys) {
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>(keys.size());
        for (String key : keys) {
            ret.add(redis.opsForHash().entries(key));
        }
        return ret;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final int saddAll(final RedisTemplate redis, final String key,
                                    final Collection<? extends Object> values) {
        StopWatch sw = new StopWatch();
        sw.start();
        int count = (Integer) redis.execute(new RedisCallback<Integer>() {
            int count = 0;

            public Integer doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer keySerializer = redis.getKeySerializer() == null ? redis.getDefaultSerializer()
                        : redis.getKeySerializer();
                RedisSerializer valueSerializer = redis.getValueSerializer() == null ? redis.getDefaultSerializer()
                        : redis.getValueSerializer();
                for (Object value : values) {
                    byte[] rawKey = keySerializer.serialize(key);
                    byte[] rawValue = valueSerializer.serialize(value);
                    Long adds = connection.sAdd(rawKey, rawValue);
                    if (adds != null && adds > 0) {
                        count++;
                    }
                }
                return count;
            }
        }, false, true);
        int failedCount = values.size() - count;
        log.info("Add " + values.size() + " values to Redis Set[" + key + "], success=" + count + ", failed="
                + failedCount + ". (" + sw.getTime() + " ms)");
        return count;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final int sremAll(final RedisTemplate redis, final String key,
                                    final Collection<? extends Object> values) {
        StopWatch sw = new StopWatch();
        sw.start();
        int count = (Integer) redis.execute(new RedisCallback<Integer>() {
            int count = 0;

            public Integer doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer keySerializer = redis.getKeySerializer() == null ? redis.getDefaultSerializer()
                        : redis.getKeySerializer();
                RedisSerializer valueSerializer = redis.getValueSerializer() == null ? redis.getDefaultSerializer()
                        : redis.getValueSerializer();
                for (Object value : values) {
                    byte[] rawKey = keySerializer.serialize(key);
                    byte[] rawValue = valueSerializer.serialize(value);
                    Long removes = connection.sRem(rawKey, rawValue);
                    if (removes != null && removes > 0) {
                        count++;
                    }
                }
                return count;
            }
        }, false, true);
        int failedCount = values.size() - count;
        log.info("Removed " + values.size() + " values to Redis Set[" + key + "], success=" + count + ", failed="
                + failedCount + ". (" + sw.getTime() + " ms)");
        return count;
    }

}
