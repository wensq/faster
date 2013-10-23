package org.faster.cache;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static org.faster.util.Hashes.md5;

/**
 * @author sqwen
 */
public abstract class AbstractCacheService implements CacheService {

    public static final int DEFAULT_EXPIRATION = 300;   // 5 minutes

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected ConcurrentMap<String, FutureTask> futureTaskMap = new ConcurrentHashMap<String, FutureTask>();

    // 是否需要对key进行hash处理
    private boolean hashKey = false;

    public void setHashKey(boolean hashKey) {
        this.hashKey = hashKey;
    }

    @Override
    public String buildInternalKey(String outerKey) {
        return hashKey || outerKey.length() > getMaxKeySize() ? md5(outerKey) : outerKey;
    }

    protected int getMaxKeySize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void putInCache(String key, int expiration, Object obj) {
        key = buildInternalKey(key);
        doPutInCache(key, expiration, obj);
    }

    protected abstract void doPutInCache(String key, int expiration, Object obj);

    protected void assertHandlerIsNotNull(CacheMissHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("CacheMissHandler must be provided!");
        }
    }

    @Override
    public Object getFromCache(String key, int expiration, CacheMissHandler handler) {
        String internalKey = buildInternalKey(key);
        if (expiration < 0) {
            log.debug("expirtion({}) is negative, change to default expiration({}).", expiration, DEFAULT_EXPIRATION);
            return directSearch(key, DEFAULT_EXPIRATION, handler);
        }

        return doGetFromCache(internalKey, expiration, handler);
    }

    protected Object directSearch(String key, int expiration, CacheMissHandler handler) {
        assertHandlerIsNotNull(handler);
        StopWatch sw = null;
        if (log.isDebugEnabled()) {
            sw = new StopWatch();
            sw.start();
            log.debug("Direct searching ...");
        }

        FutureTask ft = new FutureTask(new SearchItem(handler));
        FutureTask old = futureTaskMap.putIfAbsent(key, ft);

        if (old == null) {
            ft.run();
            old = ft;
        }

        Object ret = null;
        try {
            ret = old.get();
        } catch (InterruptedException e) {
            // just ignored
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("Direct search completed[found={}]. ({} ms)", ret != null, sw.getTime());
        }


        if (ret != null) {
            doPutInCache(key, expiration, ret);
        }
        futureTaskMap.remove(key, old);

        return ret;
    }

    protected abstract Object doGetFromCache(String internalKey, int expiration, CacheMissHandler handler);

    @Override
    @SuppressWarnings("unchecked")
    public void flush(String key) {
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            doFlush(buildInternalKey(key));
            log.info("Cache[{}] flushed. ({} ms)", key, sw.getTime());
        } catch (Exception e) {
            log.error("Cache[" + key + "] flush failed: " + e.getMessage(), e);
        }
    }

    protected abstract void doFlush(String key) throws Exception;

    @Override
    public void flush(Collection<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return;
        }

        StopWatch sw = new StopWatch();
        sw.start();
        List<String> internalKeys = new ArrayList<String>(keys.size());
        for (String key : keys) {
            internalKeys.add(buildInternalKey(key));
        }

        try {
            doFlush(internalKeys);
            log.info("{} caches flushed. ({} ms)", keys.size(), sw.getTime());
        } catch (Exception e) {
            log.info("Failed to flush " + keys.size() + " caches.", e);
        }
    }

    protected abstract void doFlush(List<String> internalKeys) throws Exception;

}
