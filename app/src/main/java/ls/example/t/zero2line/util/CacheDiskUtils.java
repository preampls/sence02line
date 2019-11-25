package ls.example.t.zero2line.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.blankj.utilcode.constant.CacheConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/05/24
 *     desc  : utils about disk cache
 * </pre>
 */
public final class CacheDiskUtils implements CacheConstants {

    private static final long   DEFAULT_MAX_SIZE  = Long.MAX_VALUE;
    private static final int    DEFAULT_MAX_COUNT = Integer.MAX_VALUE;
    private static final String CACHE_PREFIX      = "cdu_";
    private static final String TYPE_BYTE         = "by_";
    private static final String TYPE_STRING       = "st_";
    private static final String TYPE_JSON_OBJECT  = "jo_";
    private static final String TYPE_JSON_ARRAY   = "ja_";
    private static final String TYPE_BITMAP       = "bi_";
    private static final String TYPE_DRAWABLE     = "dr_";
    private static final String TYPE_PARCELABLE   = "pa_";
    private static final String TYPE_SERIALIZABLE = "se_";

    private static final java.util.Map<String, CacheDiskUtils> CACHE_MAP = new java.util.HashMap<>();

    private final String           mCacheKey;
    private final java.io.File             mCacheDir;
    private final long             mMaxSize;
    private final int              mMaxCount;
    private       ls.example.t.zero2line.util.CacheDiskUtils.DiskCacheManager mDiskCacheManager;

    /**
     * Return the single {@link CacheDiskUtils} instance.
     * <p>cache directory: /data/data/package/cache/cacheUtils</p>
     * <p>cache size: unlimited</p>
     * <p>cache count: unlimited</p>
     *
     * @return the single {@link CacheDiskUtils} instance
     */
    public static ls.example.t.zero2line.util.CacheDiskUtils getInstance() {
        return getInstance("", DEFAULT_MAX_SIZE, DEFAULT_MAX_COUNT);
    }

    /**
     * Return the single {@link CacheDiskUtils} instance.
     * <p>cache directory: /data/data/package/cache/cacheUtils</p>
     * <p>cache size: unlimited</p>
     * <p>cache count: unlimited</p>
     *
     * @param cacheName The name of cache.
     * @return the single {@link CacheDiskUtils} instance
     */
    public static ls.example.t.zero2line.util.CacheDiskUtils getInstance(final String cacheName) {
        return getInstance(cacheName, DEFAULT_MAX_SIZE, DEFAULT_MAX_COUNT);
    }

    /**
     * Return the single {@link CacheDiskUtils} instance.
     * <p>cache directory: /data/data/package/cache/cacheUtils</p>
     *
     * @param maxSize  The max size of cache, in bytes.
     * @param maxCount The max count of cache.
     * @return the single {@link CacheDiskUtils} instance
     */
    public static ls.example.t.zero2line.util.CacheDiskUtils getInstance(final long maxSize, final int maxCount) {
        return getInstance("", maxSize, maxCount);
    }

    /**
     * Return the single {@link CacheDiskUtils} instance.
     * <p>cache directory: /data/data/package/cache/cacheName</p>
     *
     * @param cacheName The name of cache.
     * @param maxSize   The max size of cache, in bytes.
     * @param maxCount  The max count of cache.
     * @return the single {@link CacheDiskUtils} instance
     */
    public static ls.example.t.zero2line.util.CacheDiskUtils getInstance(String cacheName, final long maxSize, final int maxCount) {
        if (isSpace(cacheName)) cacheName = "cacheUtils";
        java.io.File file = new java.io.File(Utils.getApp().getCacheDir(), cacheName);
        return getInstance(file, maxSize, maxCount);
    }

    /**
     * Return the single {@link CacheDiskUtils} instance.
     * <p>cache size: unlimited</p>
     * <p>cache count: unlimited</p>
     *
     * @param cacheDir The directory of cache.
     * @return the single {@link CacheDiskUtils} instance
     */
    public static ls.example.t.zero2line.util.CacheDiskUtils getInstance(@android.support.annotation.NonNull final java.io.File cacheDir) {
        return getInstance(cacheDir, DEFAULT_MAX_SIZE, DEFAULT_MAX_COUNT);
    }

    /**
     * Return the single {@link CacheDiskUtils} instance.
     *
     * @param cacheDir The directory of cache.
     * @param maxSize  The max size of cache, in bytes.
     * @param maxCount The max count of cache.
     * @return the single {@link CacheDiskUtils} instance
     */
    public static ls.example.t.zero2line.util.CacheDiskUtils getInstance(@android.support.annotation.NonNull final java.io.File cacheDir,
                                                                         final long maxSize,
                                                                         final int maxCount) {
        final String cacheKey = cacheDir.getAbsoluteFile() + "_" + maxSize + "_" + maxCount;
        ls.example.t.zero2line.util.CacheDiskUtils cache = CACHE_MAP.get(cacheKey);
        if (cache == null) {
            synchronized (ls.example.t.zero2line.util.CacheDiskUtils.class) {
                cache = CACHE_MAP.get(cacheKey);
                if (cache == null) {
                    cache = new ls.example.t.zero2line.util.CacheDiskUtils(cacheKey, cacheDir, maxSize, maxCount);
                    CACHE_MAP.put(cacheKey, cache);
                }
            }
        }
        return cache;
    }

    private CacheDiskUtils(final String cacheKey,
                           final java.io.File cacheDir,
                           final long maxSize,
                           final int maxCount) {
        mCacheKey = cacheKey;
        mCacheDir = cacheDir;
        mMaxSize = maxSize;
        mMaxCount = maxCount;
    }

    private ls.example.t.zero2line.util.CacheDiskUtils.DiskCacheManager getDiskCacheManager() {
        if (mCacheDir.exists()) {
            if (mDiskCacheManager == null) {
                mDiskCacheManager = new ls.example.t.zero2line.util.CacheDiskUtils.DiskCacheManager(mCacheDir, mMaxSize, mMaxCount);
            }
        } else {
            if (mCacheDir.mkdirs()) {
                mDiskCacheManager = new ls.example.t.zero2line.util.CacheDiskUtils.DiskCacheManager(mCacheDir, mMaxSize, mMaxCount);
            } else {
                android.util.Log.e("CacheDiskUtils", "can't make dirs in " + mCacheDir.getAbsolutePath());
            }
        }
        return mDiskCacheManager;
    }

    @Override
    public String toString() {
        return mCacheKey + "@" + Integer.toHexString(hashCode());
    }

    ///////////////////////////////////////////////////////////////////////////
    // about bytes
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put bytes in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@android.support.annotation.NonNull final String key, final byte[] value) {
        put(key, value, -1);
    }

    /**
     * Put bytes in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@android.support.annotation.NonNull final String key, final byte[] value, final int saveTime) {
        realPutBytes(TYPE_BYTE + key, value, saveTime);
    }

    private void realPutBytes(final String key, byte[] value, int saveTime) {
        if (value == null) return;
        ls.example.t.zero2line.util.CacheDiskUtils.DiskCacheManager diskCacheManager = getDiskCacheManager();
        if (diskCacheManager == null) return;
        if (saveTime >= 0) value = ls.example.t.zero2line.util.CacheDiskUtils.DiskCacheHelper.newByteArrayWithTime(saveTime, value);
        java.io.File file = diskCacheManager.getFileBeforePut(key);
        writeFileFromBytes(file, value);
        diskCacheManager.updateModify(file);
        diskCacheManager.put(file);
    }


    /**
     * Return the bytes in cache.
     *
     * @param key The key of cache.
     * @return the bytes if cache exists or null otherwise
     */
    public byte[] getBytes(@android.support.annotation.NonNull final String key) {
        return getBytes(key, null);
    }

    /**
     * Return the bytes in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bytes if cache exists or defaultValue otherwise
     */
    public byte[] getBytes(@android.support.annotation.NonNull final String key, final byte[] defaultValue) {
        return realGetBytes(TYPE_BYTE + key, defaultValue);
    }

    private byte[] realGetBytes(@android.support.annotation.NonNull final String key) {
        return realGetBytes(key, null);
    }

    private byte[] realGetBytes(@android.support.annotation.NonNull final String key, final byte[] defaultValue) {
        ls.example.t.zero2line.util.CacheDiskUtils.DiskCacheManager diskCacheManager = getDiskCacheManager();
        if (diskCacheManager == null) return defaultValue;
        final java.io.File file = diskCacheManager.getFileIfExists(key);
        if (file == null) return defaultValue;
        byte[] data = readFile2Bytes(file);
        if (ls.example.t.zero2line.util.CacheDiskUtils.DiskCacheHelper.isDue(data)) {
            diskCacheManager.removeByKey(key);
            return defaultValue;
        }
        diskCacheManager.updateModify(file);
        return ls.example.t.zero2line.util.CacheDiskUtils.DiskCacheHelper.getDataWithoutDueTime(data);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about String
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put string value in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@android.support.annotation.NonNull final String key, final String value) {
        put(key, value, -1);
    }

    /**
     * Put string value in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@android.support.annotation.NonNull final String key, final String value, final int saveTime) {
        realPutBytes(TYPE_STRING + key, string2Bytes(value), saveTime);
    }

    /**
     * Return the string value in cache.
     *
     * @param key The key of cache.
     * @return the string value if cache exists or null otherwise
     */
    public String getString(@android.support.annotation.NonNull final String key) {
        return getString(key, null);
    }

    /**
     * Return the string value in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the string value if cache exists or defaultValue otherwise
     */
    public String getString(@android.support.annotation.NonNull final String key, final String defaultValue) {
        byte[] bytes = realGetBytes(TYPE_STRING + key);
        if (bytes == null) return defaultValue;
        return bytes2String(bytes);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about JSONObject
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put JSONObject in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@android.support.annotation.NonNull final String key, final org.json.JSONObject value) {
        put(key, value, -1);
    }

    /**
     * Put JSONObject in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@android.support.annotation.NonNull final String key,
                    final org.json.JSONObject value,
                    final int saveTime) {
        realPutBytes(TYPE_JSON_OBJECT + key, jsonObject2Bytes(value), saveTime);
    }

    /**
     * Return the JSONObject in cache.
     *
     * @param key The key of cache.
     * @return the JSONObject if cache exists or null otherwise
     */
    public org.json.JSONObject getJSONObject(@android.support.annotation.NonNull final String key) {
        return getJSONObject(key, null);
    }

    /**
     * Return the JSONObject in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the JSONObject if cache exists or defaultValue otherwise
     */
    public org.json.JSONObject getJSONObject(@android.support.annotation.NonNull final String key, final org.json.JSONObject defaultValue) {
        byte[] bytes = realGetBytes(TYPE_JSON_OBJECT + key);
        if (bytes == null) return defaultValue;
        return bytes2JSONObject(bytes);
    }


    ///////////////////////////////////////////////////////////////////////////
    // about JSONArray
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put JSONArray in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@android.support.annotation.NonNull final String key, final org.json.JSONArray value) {
        put(key, value, -1);
    }

    /**
     * Put JSONArray in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@android.support.annotation.NonNull final String key, final org.json.JSONArray value, final int saveTime) {
        realPutBytes(TYPE_JSON_ARRAY + key, jsonArray2Bytes(value), saveTime);
    }

    /**
     * Return the JSONArray in cache.
     *
     * @param key The key of cache.
     * @return the JSONArray if cache exists or null otherwise
     */
    public org.json.JSONArray getJSONArray(@android.support.annotation.NonNull final String key) {
        return getJSONArray(key, null);
    }

    /**
     * Return the JSONArray in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the JSONArray if cache exists or defaultValue otherwise
     */
    public org.json.JSONArray getJSONArray(@android.support.annotation.NonNull final String key, final org.json.JSONArray defaultValue) {
        byte[] bytes = realGetBytes(TYPE_JSON_ARRAY + key);
        if (bytes == null) return defaultValue;
        return bytes2JSONArray(bytes);
    }


    ///////////////////////////////////////////////////////////////////////////
    // about Bitmap
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put bitmap in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@android.support.annotation.NonNull final String key, final android.graphics.Bitmap value) {
        put(key, value, -1);
    }

    /**
     * Put bitmap in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@android.support.annotation.NonNull final String key, final android.graphics.Bitmap value, final int saveTime) {
        realPutBytes(TYPE_BITMAP + key, bitmap2Bytes(value), saveTime);
    }

    /**
     * Return the bitmap in cache.
     *
     * @param key The key of cache.
     * @return the bitmap if cache exists or null otherwise
     */
    public android.graphics.Bitmap getBitmap(@android.support.annotation.NonNull final String key) {
        return getBitmap(key, null);
    }

    /**
     * Return the bitmap in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    public android.graphics.Bitmap getBitmap(@android.support.annotation.NonNull final String key, final android.graphics.Bitmap defaultValue) {
        byte[] bytes = realGetBytes(TYPE_BITMAP + key);
        if (bytes == null) return defaultValue;
        return bytes2Bitmap(bytes);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about Drawable
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put drawable in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@android.support.annotation.NonNull final String key, final android.graphics.drawable.Drawable value) {
        put(key, value, -1);
    }

    /**
     * Put drawable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@android.support.annotation.NonNull final String key, final android.graphics.drawable.Drawable value, final int saveTime) {
        realPutBytes(TYPE_DRAWABLE + key, drawable2Bytes(value), saveTime);
    }

    /**
     * Return the drawable in cache.
     *
     * @param key The key of cache.
     * @return the drawable if cache exists or null otherwise
     */
    public android.graphics.drawable.Drawable getDrawable(@android.support.annotation.NonNull final String key) {
        return getDrawable(key, null);
    }

    /**
     * Return the drawable in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the drawable if cache exists or defaultValue otherwise
     */
    public android.graphics.drawable.Drawable getDrawable(@android.support.annotation.NonNull final String key, final android.graphics.drawable.Drawable defaultValue) {
        byte[] bytes = realGetBytes(TYPE_DRAWABLE + key);
        if (bytes == null) return defaultValue;
        return bytes2Drawable(bytes);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about Parcelable
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put parcelable in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@android.support.annotation.NonNull final String key, final android.os.Parcelable value) {
        put(key, value, -1);
    }

    /**
     * Put parcelable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@android.support.annotation.NonNull final String key, final android.os.Parcelable value, final int saveTime) {
        realPutBytes(TYPE_PARCELABLE + key, parcelable2Bytes(value), saveTime);
    }

    /**
     * Return the parcelable in cache.
     *
     * @param key     The key of cache.
     * @param creator The creator.
     * @param <T>     The value type.
     * @return the parcelable if cache exists or null otherwise
     */
    public <T> T getParcelable(@android.support.annotation.NonNull final String key,
                               @android.support.annotation.NonNull final android.os.Parcelable.Creator<T> creator) {
        return getParcelable(key, creator, null);
    }

    /**
     * Return the parcelable in cache.
     *
     * @param key          The key of cache.
     * @param creator      The creator.
     * @param defaultValue The default value if the cache doesn't exist.
     * @param <T>          The value type.
     * @return the parcelable if cache exists or defaultValue otherwise
     */
    public <T> T getParcelable(@android.support.annotation.NonNull final String key,
                               @android.support.annotation.NonNull final android.os.Parcelable.Creator<T> creator,
                               final T defaultValue) {
        byte[] bytes = realGetBytes(TYPE_PARCELABLE + key);
        if (bytes == null) return defaultValue;
        return bytes2Parcelable(bytes, creator);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about Serializable
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put serializable in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@android.support.annotation.NonNull final String key, final java.io.Serializable value) {
        put(key, value, -1);
    }

    /**
     * Put serializable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@android.support.annotation.NonNull final String key, final java.io.Serializable value, final int saveTime) {
        realPutBytes(TYPE_SERIALIZABLE + key, serializable2Bytes(value), saveTime);
    }

    /**
     * Return the serializable in cache.
     *
     * @param key The key of cache.
     * @return the bitmap if cache exists or null otherwise
     */
    public Object getSerializable(@android.support.annotation.NonNull final String key) {
        return getSerializable(key, null);
    }

    /**
     * Return the serializable in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    public Object getSerializable(@android.support.annotation.NonNull final String key, final Object defaultValue) {
        byte[] bytes = realGetBytes(TYPE_SERIALIZABLE + key);
        if (bytes == null) return defaultValue;
        return bytes2Object(bytes);
    }

    /**
     * Return the size of cache, in bytes.
     *
     * @return the size of cache, in bytes
     */
    public long getCacheSize() {
        ls.example.t.zero2line.util.CacheDiskUtils.DiskCacheManager diskCacheManager = getDiskCacheManager();
        if (diskCacheManager == null) return 0;
        return diskCacheManager.getCacheSize();
    }

    /**
     * Return the count of cache.
     *
     * @return the count of cache
     */
    public int getCacheCount() {
        ls.example.t.zero2line.util.CacheDiskUtils.DiskCacheManager diskCacheManager = getDiskCacheManager();
        if (diskCacheManager == null) return 0;
        return diskCacheManager.getCacheCount();
    }

    /**
     * Remove the cache by key.
     *
     * @param key The key of cache.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public boolean remove(@android.support.annotation.NonNull final String key) {
        ls.example.t.zero2line.util.CacheDiskUtils.DiskCacheManager diskCacheManager = getDiskCacheManager();
        if (diskCacheManager == null) return true;
        return diskCacheManager.removeByKey(TYPE_BYTE + key)
                && diskCacheManager.removeByKey(TYPE_STRING + key)
                && diskCacheManager.removeByKey(TYPE_JSON_OBJECT + key)
                && diskCacheManager.removeByKey(TYPE_JSON_ARRAY + key)
                && diskCacheManager.removeByKey(TYPE_BITMAP + key)
                && diskCacheManager.removeByKey(TYPE_DRAWABLE + key)
                && diskCacheManager.removeByKey(TYPE_PARCELABLE + key)
                && diskCacheManager.removeByKey(TYPE_SERIALIZABLE + key);
    }

    /**
     * Clear all of the cache.
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public boolean clear() {
        ls.example.t.zero2line.util.CacheDiskUtils.DiskCacheManager diskCacheManager = getDiskCacheManager();
        if (diskCacheManager == null) return true;
        return diskCacheManager.clear();
    }

    private static final class DiskCacheManager {
        private final java.util.concurrent.atomic.AtomicLong      cacheSize;
        private final java.util.concurrent.atomic.AtomicInteger   cacheCount;
        private final long            sizeLimit;
        private final int             countLimit;
        private final java.util.Map<java.io.File, Long> lastUsageDates
                = java.util.Collections.synchronizedMap(new java.util.HashMap<java.io.File, Long>());
        private final java.io.File            cacheDir;
        private final Thread          mThread;

        private DiskCacheManager(final java.io.File cacheDir, final long sizeLimit, final int countLimit) {
            this.cacheDir = cacheDir;
            this.sizeLimit = sizeLimit;
            this.countLimit = countLimit;
            cacheSize = new java.util.concurrent.atomic.AtomicLong();
            cacheCount = new java.util.concurrent.atomic.AtomicInteger();
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int size = 0;
                    int count = 0;
                    final java.io.File[] cachedFiles = cacheDir.listFiles(new java.io.FilenameFilter() {
                        @Override
                        public boolean accept(java.io.File dir, String name) {
                            return name.startsWith(CACHE_PREFIX);
                        }
                    });
                    if (cachedFiles != null) {
                        for (java.io.File cachedFile : cachedFiles) {
                            size += cachedFile.length();
                            count += 1;
                            lastUsageDates.put(cachedFile, cachedFile.lastModified());
                        }
                        cacheSize.getAndAdd(size);
                        cacheCount.getAndAdd(count);
                    }
                }
            });
            mThread.start();
        }

        private long getCacheSize() {
            wait2InitOk();
            return cacheSize.get();
        }

        private int getCacheCount() {
            wait2InitOk();
            return cacheCount.get();
        }

        private java.io.File getFileBeforePut(final String key) {
            wait2InitOk();
            java.io.File file = new java.io.File(cacheDir, getCacheNameByKey(key));
            if (file.exists()) {
                cacheCount.addAndGet(-1);
                cacheSize.addAndGet(-file.length());
            }
            return file;
        }

        private void wait2InitOk() {
            try {
                mThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private java.io.File getFileIfExists(final String key) {
            java.io.File file = new java.io.File(cacheDir, getCacheNameByKey(key));
            if (!file.exists()) return null;
            return file;
        }

        private String getCacheNameByKey(final String key) {
            return CACHE_PREFIX + key.substring(0, 3) + key.substring(3).hashCode();
        }

        private void put(final java.io.File file) {
            cacheCount.addAndGet(1);
            cacheSize.addAndGet(file.length());
            while (cacheCount.get() > countLimit || cacheSize.get() > sizeLimit) {
                cacheSize.addAndGet(-removeOldest());
                cacheCount.addAndGet(-1);
            }
        }

        private void updateModify(final java.io.File file) {
            Long millis = System.currentTimeMillis();
            file.setLastModified(millis);
            lastUsageDates.put(file, millis);
        }

        private boolean removeByKey(final String key) {
            java.io.File file = getFileIfExists(key);
            if (file == null) return true;
            if (!file.delete()) return false;
            cacheSize.addAndGet(-file.length());
            cacheCount.addAndGet(-1);
            lastUsageDates.remove(file);
            return true;
        }

        private boolean clear() {
            java.io.File[] files = cacheDir.listFiles(new java.io.FilenameFilter() {
                @Override
                public boolean accept(java.io.File dir, String name) {
                    return name.startsWith(CACHE_PREFIX);
                }
            });
            if (files == null || files.length <= 0) return true;
            boolean flag = true;
            for (java.io.File file : files) {
                if (!file.delete()) {
                    flag = false;
                    continue;
                }
                cacheSize.addAndGet(-file.length());
                cacheCount.addAndGet(-1);
                lastUsageDates.remove(file);
            }
            if (flag) {
                lastUsageDates.clear();
                cacheSize.set(0);
                cacheCount.set(0);
            }
            return flag;
        }

        /**
         * Remove the oldest files.
         *
         * @return the size of oldest files, in bytes
         */
        private long removeOldest() {
            if (lastUsageDates.isEmpty()) return 0;
            Long oldestUsage = Long.MAX_VALUE;
            java.io.File oldestFile = null;
            java.util.Set<java.util.Map.Entry<java.io.File, Long>> entries = lastUsageDates.entrySet();
            synchronized (lastUsageDates) {
                for (java.util.Map.Entry<java.io.File, Long> entry : entries) {
                    Long lastValueUsage = entry.getValue();
                    if (lastValueUsage < oldestUsage) {
                        oldestUsage = lastValueUsage;
                        oldestFile = entry.getKey();
                    }
                }
            }
            if (oldestFile == null) return 0;
            long fileSize = oldestFile.length();
            if (oldestFile.delete()) {
                lastUsageDates.remove(oldestFile);
                return fileSize;
            }
            return 0;
        }
    }

    private static final class DiskCacheHelper {

        static final int TIME_INFO_LEN = 14;

        private static byte[] newByteArrayWithTime(final int second, final byte[] data) {
            byte[] time = createDueTime(second).getBytes();
            byte[] content = new byte[time.length + data.length];
            System.arraycopy(time, 0, content, 0, time.length);
            System.arraycopy(data, 0, content, time.length, data.length);
            return content;
        }

        /**
         * Return the string of due time.
         *
         * @param seconds The seconds.
         * @return the string of due time
         */
        private static String createDueTime(final int seconds) {
            return String.format(
                    java.util.Locale.getDefault(), "_$%010d$_",
                    System.currentTimeMillis() / 1000 + seconds
            );
        }

        private static boolean isDue(final byte[] data) {
            long millis = getDueTime(data);
            return millis != -1 && System.currentTimeMillis() > millis;
        }

        private static long getDueTime(final byte[] data) {
            if (hasTimeInfo(data)) {
                String millis = new String(copyOfRange(data, 2, 12));
                try {
                    return Long.parseLong(millis) * 1000;
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
            return -1;
        }

        private static byte[] getDataWithoutDueTime(final byte[] data) {
            if (hasTimeInfo(data)) {
                return copyOfRange(data, TIME_INFO_LEN, data.length);
            }
            return data;
        }

        private static byte[] copyOfRange(final byte[] original, final int from, final int to) {
            int newLength = to - from;
            if (newLength < 0) throw new IllegalArgumentException(from + " > " + to);
            byte[] copy = new byte[newLength];
            System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
            return copy;
        }

        private static boolean hasTimeInfo(final byte[] data) {
            return data != null
                    && data.length >= TIME_INFO_LEN
                    && data[0] == '_'
                    && data[1] == '$'
                    && data[12] == '$'
                    && data[13] == '_';
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // other utils methods
    ///////////////////////////////////////////////////////////////////////////

    private static byte[] string2Bytes(final String string) {
        if (string == null) return null;
        return string.getBytes();
    }

    private static String bytes2String(final byte[] bytes) {
        if (bytes == null) return null;
        return new String(bytes);
    }

    private static byte[] jsonObject2Bytes(final org.json.JSONObject jsonObject) {
        if (jsonObject == null) return null;
        return jsonObject.toString().getBytes();
    }

    private static org.json.JSONObject bytes2JSONObject(final byte[] bytes) {
        if (bytes == null) return null;
        try {
            return new org.json.JSONObject(new String(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] jsonArray2Bytes(final org.json.JSONArray jsonArray) {
        if (jsonArray == null) return null;
        return jsonArray.toString().getBytes();
    }

    private static org.json.JSONArray bytes2JSONArray(final byte[] bytes) {
        if (bytes == null) return null;
        try {
            return new org.json.JSONArray(new String(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] parcelable2Bytes(final android.os.Parcelable parcelable) {
        if (parcelable == null) return null;
        android.os.Parcel parcel = android.os.Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    private static <T> T bytes2Parcelable(final byte[] bytes,
                                          final android.os.Parcelable.Creator<T> creator) {
        if (bytes == null) return null;
        android.os.Parcel parcel = android.os.Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        T result = creator.createFromParcel(parcel);
        parcel.recycle();
        return result;
    }

    private static byte[] serializable2Bytes(final java.io.Serializable serializable) {
        if (serializable == null) return null;
        java.io.ByteArrayOutputStream baos;
        java.io.ObjectOutputStream oos = null;
        try {
            oos = new java.io.ObjectOutputStream(baos = new java.io.ByteArrayOutputStream());
            oos.writeObject(serializable);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Object bytes2Object(final byte[] bytes) {
        if (bytes == null) return null;
        java.io.ObjectInputStream ois = null;
        try {
            ois = new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static byte[] bitmap2Bytes(final android.graphics.Bitmap bitmap) {
        if (bitmap == null) return null;
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private static android.graphics.Bitmap bytes2Bitmap(final byte[] bytes) {
        return (bytes == null || bytes.length <= 0)
                ? null
                : android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private static byte[] drawable2Bytes(final android.graphics.drawable.Drawable drawable) {
        return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable));
    }

    private static android.graphics.drawable.Drawable bytes2Drawable(final byte[] bytes) {
        return bytes == null ? null : bitmap2Drawable(bytes2Bitmap(bytes));
    }

    private static android.graphics.Bitmap drawable2Bitmap(final android.graphics.drawable.Drawable drawable) {
        if (drawable instanceof android.graphics.drawable.BitmapDrawable) {
            android.graphics.drawable.BitmapDrawable bitmapDrawable = (android.graphics.drawable.BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        android.graphics.Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = android.graphics.Bitmap.createBitmap(
                    1,
                    1,
                    drawable.getOpacity() != android.graphics.PixelFormat.OPAQUE
                            ? android.graphics.Bitmap.Config.ARGB_8888
                            : android.graphics.Bitmap.Config.RGB_565
            );
        } else {
            bitmap = android.graphics.Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != android.graphics.PixelFormat.OPAQUE
                            ? android.graphics.Bitmap.Config.ARGB_8888
                            : android.graphics.Bitmap.Config.RGB_565
            );
        }
        android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private static android.graphics.drawable.Drawable bitmap2Drawable(final android.graphics.Bitmap bitmap) {
        return bitmap == null
                ? null
                : new android.graphics.drawable.BitmapDrawable(Utils.getApp().getResources(), bitmap);
    }


    private static void writeFileFromBytes(final java.io.File file, final byte[] bytes) {
        java.nio.channels.FileChannel fc = null;
        try {
            fc = new java.io.FileOutputStream(file, false).getChannel();
            fc.write(java.nio.ByteBuffer.wrap(bytes));
            fc.force(true);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static byte[] readFile2Bytes(final java.io.File file) {
        java.nio.channels.FileChannel fc = null;
        try {
            fc = new java.io.RandomAccessFile(file, "r").getChannel();
            int size = (int) fc.size();
            java.nio.MappedByteBuffer mbb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, size).load();
            byte[] data = new byte[size];
            mbb.get(data, 0, size);
            return data;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}