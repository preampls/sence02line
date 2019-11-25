package ls.example.t.zero2line.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/01/04
 *     desc  : utils about disk cache
 * </pre>
 */
public final class CacheDiskStaticUtils {

    private static CacheDiskUtils sDefaultCacheDiskUtils;

    /**
     * Set the default instance of {@link CacheDiskUtils}.
     *
     * @param cacheDiskUtils The default instance of {@link CacheDiskUtils}.
     */
    public static void setDefaultCacheDiskUtils(final CacheDiskUtils cacheDiskUtils) {
        sDefaultCacheDiskUtils = cacheDiskUtils;
    }

    /**
     * Put bytes in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public static void put(@android.support.annotation.NonNull final String key, final byte[] value) {
        put(key, value, getDefaultCacheDiskUtils());
    }

    /**
     * Put bytes in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public static void put(@android.support.annotation.NonNull final String key, final byte[] value, final int saveTime) {
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    /**
     * Return the bytes in cache.
     *
     * @param key The key of cache.
     * @return the bytes if cache exists or null otherwise
     */
    public static byte[] getBytes(@android.support.annotation.NonNull final String key) {
        return getBytes(key, getDefaultCacheDiskUtils());
    }

    /**
     * Return the bytes in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bytes if cache exists or defaultValue otherwise
     */
    public static byte[] getBytes(@android.support.annotation.NonNull final String key, final byte[] defaultValue) {
        return getBytes(key, defaultValue, getDefaultCacheDiskUtils());
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
    public static void put(@android.support.annotation.NonNull final String key, final String value) {
        put(key, value, getDefaultCacheDiskUtils());
    }

    /**
     * Put string value in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public static void put(@android.support.annotation.NonNull final String key, final String value, final int saveTime) {
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    /**
     * Return the string value in cache.
     *
     * @param key The key of cache.
     * @return the string value if cache exists or null otherwise
     */
    public static String getString(@android.support.annotation.NonNull final String key) {
        return getString(key, getDefaultCacheDiskUtils());
    }

    /**
     * Return the string value in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the string value if cache exists or defaultValue otherwise
     */
    public static String getString(@android.support.annotation.NonNull final String key, final String defaultValue) {
        return getString(key, defaultValue, getDefaultCacheDiskUtils());
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
    public static void put(@android.support.annotation.NonNull final String key, final org.json.JSONObject value) {
        put(key, value, getDefaultCacheDiskUtils());
    }

    /**
     * Put JSONObject in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final org.json.JSONObject value,
                           final int saveTime) {
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    /**
     * Return the JSONObject in cache.
     *
     * @param key The key of cache.
     * @return the JSONObject if cache exists or null otherwise
     */
    public static org.json.JSONObject getJSONObject(@android.support.annotation.NonNull final String key) {
        return getJSONObject(key, getDefaultCacheDiskUtils());
    }

    /**
     * Return the JSONObject in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the JSONObject if cache exists or defaultValue otherwise
     */
    public static org.json.JSONObject getJSONObject(@android.support.annotation.NonNull final String key, final org.json.JSONObject defaultValue) {
        return getJSONObject(key, defaultValue, getDefaultCacheDiskUtils());
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
    public static void put(@android.support.annotation.NonNull final String key, final org.json.JSONArray value) {
        put(key, value, getDefaultCacheDiskUtils());
    }

    /**
     * Put JSONArray in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public static void put(@android.support.annotation.NonNull final String key, final org.json.JSONArray value, final int saveTime) {
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    /**
     * Return the JSONArray in cache.
     *
     * @param key The key of cache.
     * @return the JSONArray if cache exists or null otherwise
     */
    public static org.json.JSONArray getJSONArray(@android.support.annotation.NonNull final String key) {
        return getJSONArray(key, getDefaultCacheDiskUtils());
    }

    /**
     * Return the JSONArray in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the JSONArray if cache exists or defaultValue otherwise
     */
    public static org.json.JSONArray getJSONArray(@android.support.annotation.NonNull final String key, final org.json.JSONArray defaultValue) {
        return getJSONArray(key, defaultValue, getDefaultCacheDiskUtils());
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
    public static void put(@android.support.annotation.NonNull final String key, final android.graphics.Bitmap value) {
        put(key, value, getDefaultCacheDiskUtils());
    }

    /**
     * Put bitmap in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public static void put(@android.support.annotation.NonNull final String key, final android.graphics.Bitmap value, final int saveTime) {
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    /**
     * Return the bitmap in cache.
     *
     * @param key The key of cache.
     * @return the bitmap if cache exists or null otherwise
     */
    public static android.graphics.Bitmap getBitmap(@android.support.annotation.NonNull final String key) {
        return getBitmap(key, getDefaultCacheDiskUtils());
    }

    /**
     * Return the bitmap in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    public static android.graphics.Bitmap getBitmap(@android.support.annotation.NonNull final String key, final android.graphics.Bitmap defaultValue) {
        return getBitmap(key, defaultValue, getDefaultCacheDiskUtils());
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
    public static void put(@android.support.annotation.NonNull final String key, final android.graphics.drawable.Drawable value) {
        put(key, value, getDefaultCacheDiskUtils());
    }

    /**
     * Put drawable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public static void put(@android.support.annotation.NonNull final String key, final android.graphics.drawable.Drawable value, final int saveTime) {
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    /**
     * Return the drawable in cache.
     *
     * @param key The key of cache.
     * @return the drawable if cache exists or null otherwise
     */
    public static android.graphics.drawable.Drawable getDrawable(@android.support.annotation.NonNull final String key) {
        return getDrawable(key, getDefaultCacheDiskUtils());
    }

    /**
     * Return the drawable in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the drawable if cache exists or defaultValue otherwise
     */
    public static android.graphics.drawable.Drawable getDrawable(@android.support.annotation.NonNull final String key, final android.graphics.drawable.Drawable defaultValue) {
        return getDrawable(key, defaultValue, getDefaultCacheDiskUtils());
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
    public static void put(@android.support.annotation.NonNull final String key, final android.os.Parcelable value) {
        put(key, value, getDefaultCacheDiskUtils());
    }

    /**
     * Put parcelable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public static void put(@android.support.annotation.NonNull final String key, final android.os.Parcelable value, final int saveTime) {
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    /**
     * Return the parcelable in cache.
     *
     * @param key     The key of cache.
     * @param creator The creator.
     * @param <T>     The value type.
     * @return the parcelable if cache exists or null otherwise
     */
    public static <T> T getParcelable(@android.support.annotation.NonNull final String key,
                                      @android.support.annotation.NonNull final android.os.Parcelable.Creator<T> creator) {
        return getParcelable(key, creator, getDefaultCacheDiskUtils());
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
    public static <T> T getParcelable(@android.support.annotation.NonNull final String key,
                                      @android.support.annotation.NonNull final android.os.Parcelable.Creator<T> creator,
                                      final T defaultValue) {
        return getParcelable(key, creator, defaultValue, getDefaultCacheDiskUtils());
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
    public static void put(@android.support.annotation.NonNull final String key, final java.io.Serializable value) {
        put(key, value, getDefaultCacheDiskUtils());
    }

    /**
     * Put serializable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public static void put(@android.support.annotation.NonNull final String key, final java.io.Serializable value, final int saveTime) {
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    /**
     * Return the serializable in cache.
     *
     * @param key The key of cache.
     * @return the bitmap if cache exists or null otherwise
     */
    public static Object getSerializable(@android.support.annotation.NonNull final String key) {
        return getSerializable(key, getDefaultCacheDiskUtils());
    }

    /**
     * Return the serializable in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    public static Object getSerializable(@android.support.annotation.NonNull final String key, final Object defaultValue) {
        return getSerializable(key, defaultValue, getDefaultCacheDiskUtils());
    }

    /**
     * Return the size of cache, in bytes.
     *
     * @return the size of cache, in bytes
     */
    public static long getCacheSize() {
        return getCacheSize(getDefaultCacheDiskUtils());
    }

    /**
     * Return the count of cache.
     *
     * @return the count of cache
     */
    public static int getCacheCount() {
        return getCacheCount(getDefaultCacheDiskUtils());
    }

    /**
     * Remove the cache by key.
     *
     * @param key The key of cache.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean remove(@android.support.annotation.NonNull final String key) {
        return remove(key, getDefaultCacheDiskUtils());
    }

    /**
     * Clear all of the cache.
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean clear() {
        return clear(getDefaultCacheDiskUtils());
    }

    ///////////////////////////////////////////////////////////////////////////
    // dividing line
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put bytes in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final byte[] value,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value);
    }

    /**
     * Put bytes in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final byte[] value,
                           final int saveTime,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * Return the bytes in cache.
     *
     * @param key            The key of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the bytes if cache exists or null otherwise
     */
    public static byte[] getBytes(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getBytes(key);
    }

    /**
     * Return the bytes in cache.
     *
     * @param key            The key of cache.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the bytes if cache exists or defaultValue otherwise
     */
    public static byte[] getBytes(@android.support.annotation.NonNull final String key,
                                  final byte[] defaultValue,
                                  @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getBytes(key, defaultValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about String
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put string value in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final String value,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value);
    }

    /**
     * Put string value in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final String value,
                           final int saveTime,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * Return the string value in cache.
     *
     * @param key            The key of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the string value if cache exists or null otherwise
     */
    public static String getString(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getString(key);
    }

    /**
     * Return the string value in cache.
     *
     * @param key            The key of cache.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the string value if cache exists or defaultValue otherwise
     */
    public static String getString(@android.support.annotation.NonNull final String key,
                                   final String defaultValue,
                                   @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getString(key, defaultValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about JSONObject
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put JSONObject in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final org.json.JSONObject value,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value);
    }

    /**
     * Put JSONObject in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final org.json.JSONObject value,
                           final int saveTime,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * Return the JSONObject in cache.
     *
     * @param key            The key of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the JSONObject if cache exists or null otherwise
     */
    public static org.json.JSONObject getJSONObject(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getJSONObject(key);
    }

    /**
     * Return the JSONObject in cache.
     *
     * @param key            The key of cache.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the JSONObject if cache exists or defaultValue otherwise
     */
    public static org.json.JSONObject getJSONObject(@android.support.annotation.NonNull final String key,
                                                    final org.json.JSONObject defaultValue,
                                                    @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getJSONObject(key, defaultValue);
    }


    ///////////////////////////////////////////////////////////////////////////
    // about JSONArray
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put JSONArray in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final org.json.JSONArray value,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value);
    }

    /**
     * Put JSONArray in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final org.json.JSONArray value,
                           final int saveTime,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * Return the JSONArray in cache.
     *
     * @param key            The key of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the JSONArray if cache exists or null otherwise
     */
    public static org.json.JSONArray getJSONArray(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getJSONArray(key);
    }

    /**
     * Return the JSONArray in cache.
     *
     * @param key            The key of cache.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the JSONArray if cache exists or defaultValue otherwise
     */
    public static org.json.JSONArray getJSONArray(@android.support.annotation.NonNull final String key,
                                                  final org.json.JSONArray defaultValue,
                                                  @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getJSONArray(key, defaultValue);
    }


    ///////////////////////////////////////////////////////////////////////////
    // about Bitmap
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put bitmap in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final android.graphics.Bitmap value,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value);
    }

    /**
     * Put bitmap in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final android.graphics.Bitmap value,
                           final int saveTime,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * Return the bitmap in cache.
     *
     * @param key            The key of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the bitmap if cache exists or null otherwise
     */
    public static android.graphics.Bitmap getBitmap(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getBitmap(key);
    }

    /**
     * Return the bitmap in cache.
     *
     * @param key            The key of cache.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    public static android.graphics.Bitmap getBitmap(@android.support.annotation.NonNull final String key,
                                                    final android.graphics.Bitmap defaultValue,
                                                    @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getBitmap(key, defaultValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about Drawable
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put drawable in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final android.graphics.drawable.Drawable value,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value);
    }

    /**
     * Put drawable in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final android.graphics.drawable.Drawable value,
                           final int saveTime,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * Return the drawable in cache.
     *
     * @param key            The key of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the drawable if cache exists or null otherwise
     */
    public static android.graphics.drawable.Drawable getDrawable(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getDrawable(key);
    }

    /**
     * Return the drawable in cache.
     *
     * @param key            The key of cache.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the drawable if cache exists or defaultValue otherwise
     */
    public static android.graphics.drawable.Drawable getDrawable(@android.support.annotation.NonNull final String key,
                                                                 final android.graphics.drawable.Drawable defaultValue,
                                                                 @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getDrawable(key, defaultValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about Parcelable
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put parcelable in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final android.os.Parcelable value,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value);
    }

    /**
     * Put parcelable in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final android.os.Parcelable value,
                           final int saveTime,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * Return the parcelable in cache.
     *
     * @param key            The key of cache.
     * @param creator        The creator.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @param <T>            The value type.
     * @return the parcelable if cache exists or null otherwise
     */
    public static <T> T getParcelable(@android.support.annotation.NonNull final String key,
                                      @android.support.annotation.NonNull final android.os.Parcelable.Creator<T> creator,
                                      @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getParcelable(key, creator);
    }

    /**
     * Return the parcelable in cache.
     *
     * @param key            The key of cache.
     * @param creator        The creator.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @param <T>            The value type.
     * @return the parcelable if cache exists or defaultValue otherwise
     */
    public static <T> T getParcelable(@android.support.annotation.NonNull final String key,
                                      @android.support.annotation.NonNull final android.os.Parcelable.Creator<T> creator,
                                      final T defaultValue,
                                      @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getParcelable(key, creator, defaultValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about Serializable
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put serializable in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final java.io.Serializable value,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value);
    }

    /**
     * Put serializable in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final java.io.Serializable value,
                           final int saveTime,
                           @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        cacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * Return the serializable in cache.
     *
     * @param key            The key of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the bitmap if cache exists or null otherwise
     */
    public static Object getSerializable(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getSerializable(key);
    }

    /**
     * Return the serializable in cache.
     *
     * @param key            The key of cache.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    public static Object getSerializable(@android.support.annotation.NonNull final String key,
                                         final Object defaultValue,
                                         @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getSerializable(key, defaultValue);
    }

    /**
     * Return the size of cache, in bytes.
     *
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the size of cache, in bytes
     */
    public static long getCacheSize(@android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getCacheSize();
    }

    /**
     * Return the count of cache.
     *
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return the count of cache
     */
    public static int getCacheCount(@android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.getCacheCount();
    }

    /**
     * Remove the cache by key.
     *
     * @param key            The key of cache.
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean remove(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.remove(key);
    }

    /**
     * Clear all of the cache.
     *
     * @param cacheDiskUtils The instance of {@link CacheDiskUtils}.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean clear(@android.support.annotation.NonNull final CacheDiskUtils cacheDiskUtils) {
        return cacheDiskUtils.clear();
    }

    private static CacheDiskUtils getDefaultCacheDiskUtils() {
        return sDefaultCacheDiskUtils != null ? sDefaultCacheDiskUtils : CacheDiskUtils.getInstance();
    }
}