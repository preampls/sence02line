package ls.example.t.zero2line.util;

import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import com.blankj.utilcode.constant.CacheConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/05/24
 *     desc  : utils about memory cache
 * </pre>
 */
public final class CacheMemoryUtils implements CacheConstants {

    private static final int DEFAULT_MAX_COUNT = 256;

    private static final java.util.Map<String, CacheMemoryUtils> CACHE_MAP = new java.util.HashMap<>();

    private final String                       mCacheKey;
    private final android.support.v4.util.LruCache<String, ls.example.t.zero2line.util.CacheMemoryUtils.CacheValue> mMemoryCache;

    /**
     * Return the single {@link CacheMemoryUtils} instance.
     *
     * @return the single {@link CacheMemoryUtils} instance
     */
    public static ls.example.t.zero2line.util.CacheMemoryUtils getInstance() {
        return getInstance(DEFAULT_MAX_COUNT);
    }

    /**
     * Return the single {@link CacheMemoryUtils} instance.
     *
     * @param maxCount The max count of cache.
     * @return the single {@link CacheMemoryUtils} instance
     */
    public static ls.example.t.zero2line.util.CacheMemoryUtils getInstance(final int maxCount) {
        return getInstance(String.valueOf(maxCount), maxCount);
    }

    /**
     * Return the single {@link CacheMemoryUtils} instance.
     *
     * @param cacheKey The key of cache.
     * @param maxCount The max count of cache.
     * @return the single {@link CacheMemoryUtils} instance
     */
    public static ls.example.t.zero2line.util.CacheMemoryUtils getInstance(final String cacheKey, final int maxCount) {
        ls.example.t.zero2line.util.CacheMemoryUtils cache = CACHE_MAP.get(cacheKey);
        if (cache == null) {
            synchronized (ls.example.t.zero2line.util.CacheMemoryUtils.class) {
                cache = CACHE_MAP.get(cacheKey);
                if (cache == null) {
                    cache = new ls.example.t.zero2line.util.CacheMemoryUtils(cacheKey, new android.support.v4.util.LruCache<String, ls.example.t.zero2line.util.CacheMemoryUtils.CacheValue>(maxCount));
                    CACHE_MAP.put(cacheKey, cache);
                }
            }
        }
        return cache;
    }

    private CacheMemoryUtils(String cacheKey, android.support.v4.util.LruCache<String, ls.example.t.zero2line.util.CacheMemoryUtils.CacheValue> memoryCache) {
        mCacheKey = cacheKey;
        mMemoryCache = memoryCache;
    }

    @Override
    public String toString() {
        return mCacheKey + "@" + Integer.toHexString(hashCode());
    }

    /**
     * Put bytes in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@android.support.annotation.NonNull final String key, final Object value) {
        put(key, value, -1);
    }

    /**
     * Put bytes in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@android.support.annotation.NonNull final String key, final Object value, int saveTime) {
        if (value == null) return;
        long dueTime = saveTime < 0 ? -1 : System.currentTimeMillis() + saveTime * 1000;
        mMemoryCache.put(key, new ls.example.t.zero2line.util.CacheMemoryUtils.CacheValue(dueTime, value));
    }

    /**
     * Return the value in cache.
     *
     * @param key The key of cache.
     * @param <T> The value type.
     * @return the value if cache exists or null otherwise
     */
    public <T> T get(@android.support.annotation.NonNull final String key) {
        return get(key, null);
    }

    /**
     * Return the value in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @param <T>          The value type.
     * @return the value if cache exists or defaultValue otherwise
     */
    public <T> T get(@android.support.annotation.NonNull final String key, final T defaultValue) {
        ls.example.t.zero2line.util.CacheMemoryUtils.CacheValue val = mMemoryCache.get(key);
        if (val == null) return defaultValue;
        if (val.dueTime == -1 || val.dueTime >= System.currentTimeMillis()) {
            //noinspection unchecked
            return (T) val.value;
        }
        mMemoryCache.remove(key);
        return defaultValue;
    }

    /**
     * Return the count of cache.
     *
     * @return the count of cache
     */
    public int getCacheCount() {
        return mMemoryCache.size();
    }

    /**
     * Remove the cache by key.
     *
     * @param key The key of cache.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public Object remove(@android.support.annotation.NonNull final String key) {
        ls.example.t.zero2line.util.CacheMemoryUtils.CacheValue remove = mMemoryCache.remove(key);
        if (remove == null) return null;
        return remove.value;
    }

    /**
     * Clear all of the cache.
     */
    public void clear() {
        mMemoryCache.evictAll();
    }

    private static final class CacheValue {
        long   dueTime;
        Object value;

        CacheValue(long dueTime, Object value) {
            this.dueTime = dueTime;
            this.value = value;
        }
    }
}