package ls.example.t.zero2line.util;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/01/04
 *     desc  : utils about shared preference
 * </pre>
 */
public final class SPStaticUtils {

    private static SPUtils sDefaultSPUtils;

    /**
     * Set the default instance of {@link SPUtils}.
     *
     * @param spUtils The default instance of {@link SPUtils}.
     */
    public static void setDefaultSPUtils(final SPUtils spUtils) {
        sDefaultSPUtils = spUtils;
    }

    /**
     * Put the string value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public static void put(@android.support.annotation.NonNull final String key, final String value) {
        put(key, value, getDefaultSPUtils());
    }

    /**
     * Put the string value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public static void put(@android.support.annotation.NonNull final String key, final String value, final boolean isCommit) {
        put(key, value, isCommit, getDefaultSPUtils());
    }


    /**
     * Return the string value in sp.
     *
     * @param key The key of sp.
     * @return the string value if sp exists or {@code ""} otherwise
     */
    public static String getString(@android.support.annotation.NonNull final String key) {
        return getString(key, getDefaultSPUtils());
    }

    /**
     * Return the string value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the string value if sp exists or {@code defaultValue} otherwise
     */
    public static String getString(@android.support.annotation.NonNull final String key, final String defaultValue) {
        return getString(key, defaultValue, getDefaultSPUtils());
    }


    /**
     * Put the int value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public static void put(@android.support.annotation.NonNull final String key, final int value) {
        put(key, value, getDefaultSPUtils());
    }

    /**
     * Put the int value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public static void put(@android.support.annotation.NonNull final String key, final int value, final boolean isCommit) {
        put(key, value, isCommit, getDefaultSPUtils());
    }

    /**
     * Return the int value in sp.
     *
     * @param key The key of sp.
     * @return the int value if sp exists or {@code -1} otherwise
     */
    public static int getInt(@android.support.annotation.NonNull final String key) {
        return getInt(key, getDefaultSPUtils());
    }

    /**
     * Return the int value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the int value if sp exists or {@code defaultValue} otherwise
     */
    public static int getInt(@android.support.annotation.NonNull final String key, final int defaultValue) {
        return getInt(key, defaultValue, getDefaultSPUtils());
    }

    /**
     * Put the long value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public static void put(@android.support.annotation.NonNull final String key, final long value) {
        put(key, value, getDefaultSPUtils());
    }

    /**
     * Put the long value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public static void put(@android.support.annotation.NonNull final String key, final long value, final boolean isCommit) {
        put(key, value, isCommit, getDefaultSPUtils());
    }

    /**
     * Return the long value in sp.
     *
     * @param key The key of sp.
     * @return the long value if sp exists or {@code -1} otherwise
     */
    public static long getLong(@android.support.annotation.NonNull final String key) {
        return getLong(key, getDefaultSPUtils());
    }

    /**
     * Return the long value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the long value if sp exists or {@code defaultValue} otherwise
     */
    public static long getLong(@android.support.annotation.NonNull final String key, final long defaultValue) {
        return getLong(key, defaultValue, getDefaultSPUtils());
    }

    /**
     * Put the float value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public static void put(@android.support.annotation.NonNull final String key, final float value) {
        put(key, value, getDefaultSPUtils());
    }

    /**
     * Put the float value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public static void put(@android.support.annotation.NonNull final String key, final float value, final boolean isCommit) {
        put(key, value, isCommit, getDefaultSPUtils());
    }

    /**
     * Return the float value in sp.
     *
     * @param key The key of sp.
     * @return the float value if sp exists or {@code -1f} otherwise
     */
    public static float getFloat(@android.support.annotation.NonNull final String key) {
        return getFloat(key, getDefaultSPUtils());
    }

    /**
     * Return the float value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the float value if sp exists or {@code defaultValue} otherwise
     */
    public static float getFloat(@android.support.annotation.NonNull final String key, final float defaultValue) {
        return getFloat(key, defaultValue, getDefaultSPUtils());
    }

    /**
     * Put the boolean value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public static void put(@android.support.annotation.NonNull final String key, final boolean value) {
        put(key, value, getDefaultSPUtils());
    }

    /**
     * Put the boolean value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public static void put(@android.support.annotation.NonNull final String key, final boolean value, final boolean isCommit) {
        put(key, value, isCommit, getDefaultSPUtils());
    }

    /**
     * Return the boolean value in sp.
     *
     * @param key The key of sp.
     * @return the boolean value if sp exists or {@code false} otherwise
     */
    public static boolean getBoolean(@android.support.annotation.NonNull final String key) {
        return getBoolean(key, getDefaultSPUtils());
    }

    /**
     * Return the boolean value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the boolean value if sp exists or {@code defaultValue} otherwise
     */
    public static boolean getBoolean(@android.support.annotation.NonNull final String key, final boolean defaultValue) {
        return getBoolean(key, defaultValue, getDefaultSPUtils());
    }

    /**
     * Put the set of string value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public static void put(@android.support.annotation.NonNull final String key, final java.util.Set<String> value) {
        put(key, value, getDefaultSPUtils());
    }

    /**
     * Put the set of string value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final java.util.Set<String> value,
                           final boolean isCommit) {
        put(key, value, isCommit, getDefaultSPUtils());
    }

    /**
     * Return the set of string value in sp.
     *
     * @param key The key of sp.
     * @return the set of string value if sp exists
     * or {@code Collections.<String>emptySet()} otherwise
     */
    public static java.util.Set<String> getStringSet(@android.support.annotation.NonNull final String key) {
        return getStringSet(key, getDefaultSPUtils());
    }

    /**
     * Return the set of string value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the set of string value if sp exists or {@code defaultValue} otherwise
     */
    public static java.util.Set<String> getStringSet(@android.support.annotation.NonNull final String key,
                                                     final java.util.Set<String> defaultValue) {
        return getStringSet(key, defaultValue, getDefaultSPUtils());
    }

    /**
     * Return all values in sp.
     *
     * @return all values in sp
     */
    public static java.util.Map<String, ?> getAll() {
        return getAll(getDefaultSPUtils());
    }

    /**
     * Return whether the sp contains the preference.
     *
     * @param key The key of sp.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean contains(@android.support.annotation.NonNull final String key) {
        return contains(key, getDefaultSPUtils());
    }

    /**
     * Remove the preference in sp.
     *
     * @param key The key of sp.
     */
    public static void remove(@android.support.annotation.NonNull final String key) {
        remove(key, getDefaultSPUtils());
    }

    /**
     * Remove the preference in sp.
     *
     * @param key      The key of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public static void remove(@android.support.annotation.NonNull final String key, final boolean isCommit) {
        remove(key, isCommit, getDefaultSPUtils());
    }

    /**
     * Remove all preferences in sp.
     */
    public static void clear() {
        clear(getDefaultSPUtils());
    }

    /**
     * Remove all preferences in sp.
     *
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public static void clear(final boolean isCommit) {
        clear(isCommit, getDefaultSPUtils());
    }

    ///////////////////////////////////////////////////////////////////////////
    // dividing line
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put the string value in sp.
     *
     * @param key     The key of sp.
     * @param value   The value of sp.
     * @param spUtils The instance of {@link SPUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key, final String value, @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.put(key, value);
    }

    /**
     * Put the string value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     * @param spUtils  The instance of {@link SPUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final String value,
                           final boolean isCommit,
                           @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.put(key, value, isCommit);
    }


    /**
     * Return the string value in sp.
     *
     * @param key     The key of sp.
     * @param spUtils The instance of {@link SPUtils}.
     * @return the string value if sp exists or {@code ""} otherwise
     */
    public static String getString(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final SPUtils spUtils) {
        return spUtils.getString(key);
    }

    /**
     * Return the string value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @param spUtils      The instance of {@link SPUtils}.
     * @return the string value if sp exists or {@code defaultValue} otherwise
     */
    public static String getString(@android.support.annotation.NonNull final String key,
                                   final String defaultValue,
                                   @android.support.annotation.NonNull final SPUtils spUtils) {
        return spUtils.getString(key, defaultValue);
    }


    /**
     * Put the int value in sp.
     *
     * @param key     The key of sp.
     * @param value   The value of sp.
     * @param spUtils The instance of {@link SPUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key, final int value, @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.put(key, value);
    }

    /**
     * Put the int value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     * @param spUtils  The instance of {@link SPUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final int value,
                           final boolean isCommit,
                           @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.put(key, value, isCommit);
    }

    /**
     * Return the int value in sp.
     *
     * @param key     The key of sp.
     * @param spUtils The instance of {@link SPUtils}.
     * @return the int value if sp exists or {@code -1} otherwise
     */
    public static int getInt(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final SPUtils spUtils) {
        return spUtils.getInt(key);
    }

    /**
     * Return the int value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @param spUtils      The instance of {@link SPUtils}.
     * @return the int value if sp exists or {@code defaultValue} otherwise
     */
    public static int getInt(@android.support.annotation.NonNull final String key, final int defaultValue, @android.support.annotation.NonNull final SPUtils spUtils) {
        return spUtils.getInt(key, defaultValue);
    }

    /**
     * Put the long value in sp.
     *
     * @param key     The key of sp.
     * @param value   The value of sp.
     * @param spUtils The instance of {@link SPUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key, final long value, @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.put(key, value);
    }

    /**
     * Put the long value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     * @param spUtils  The instance of {@link SPUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final long value,
                           final boolean isCommit,
                           @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.put(key, value, isCommit);
    }

    /**
     * Return the long value in sp.
     *
     * @param key     The key of sp.
     * @param spUtils The instance of {@link SPUtils}.
     * @return the long value if sp exists or {@code -1} otherwise
     */
    public static long getLong(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final SPUtils spUtils) {
        return spUtils.getLong(key);
    }

    /**
     * Return the long value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @param spUtils      The instance of {@link SPUtils}.
     * @return the long value if sp exists or {@code defaultValue} otherwise
     */
    public static long getLong(@android.support.annotation.NonNull final String key, final long defaultValue, @android.support.annotation.NonNull final SPUtils spUtils) {
        return spUtils.getLong(key, defaultValue);
    }

    /**
     * Put the float value in sp.
     *
     * @param key     The key of sp.
     * @param value   The value of sp.
     * @param spUtils The instance of {@link SPUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key, final float value, @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.put(key, value);
    }

    /**
     * Put the float value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     * @param spUtils  The instance of {@link SPUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final float value,
                           final boolean isCommit,
                           @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.put(key, value, isCommit);
    }

    /**
     * Return the float value in sp.
     *
     * @param key     The key of sp.
     * @param spUtils The instance of {@link SPUtils}.
     * @return the float value if sp exists or {@code -1f} otherwise
     */
    public static float getFloat(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final SPUtils spUtils) {
        return spUtils.getFloat(key);
    }

    /**
     * Return the float value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @param spUtils      The instance of {@link SPUtils}.
     * @return the float value if sp exists or {@code defaultValue} otherwise
     */
    public static float getFloat(@android.support.annotation.NonNull final String key, final float defaultValue, @android.support.annotation.NonNull final SPUtils spUtils) {
        return spUtils.getFloat(key, defaultValue);
    }

    /**
     * Put the boolean value in sp.
     *
     * @param key     The key of sp.
     * @param value   The value of sp.
     * @param spUtils The instance of {@link SPUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key, final boolean value, @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.put(key, value);
    }

    /**
     * Put the boolean value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     * @param spUtils  The instance of {@link SPUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final boolean value,
                           final boolean isCommit,
                           @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.put(key, value, isCommit);
    }

    /**
     * Return the boolean value in sp.
     *
     * @param key     The key of sp.
     * @param spUtils The instance of {@link SPUtils}.
     * @return the boolean value if sp exists or {@code false} otherwise
     */
    public static boolean getBoolean(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final SPUtils spUtils) {
        return spUtils.getBoolean(key);
    }

    /**
     * Return the boolean value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @param spUtils      The instance of {@link SPUtils}.
     * @return the boolean value if sp exists or {@code defaultValue} otherwise
     */
    public static boolean getBoolean(@android.support.annotation.NonNull final String key,
                                     final boolean defaultValue,
                                     @android.support.annotation.NonNull final SPUtils spUtils) {
        return spUtils.getBoolean(key, defaultValue);
    }

    /**
     * Put the set of string value in sp.
     *
     * @param key     The key of sp.
     * @param value   The value of sp.
     * @param spUtils The instance of {@link SPUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key, final java.util.Set<String> value, @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.put(key, value);
    }

    /**
     * Put the set of string value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     * @param spUtils  The instance of {@link SPUtils}.
     */
    public static void put(@android.support.annotation.NonNull final String key,
                           final java.util.Set<String> value,
                           final boolean isCommit,
                           @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.put(key, value, isCommit);
    }

    /**
     * Return the set of string value in sp.
     *
     * @param key     The key of sp.
     * @param spUtils The instance of {@link SPUtils}.
     * @return the set of string value if sp exists
     * or {@code Collections.<String>emptySet()} otherwise
     */
    public static java.util.Set<String> getStringSet(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final SPUtils spUtils) {
        return spUtils.getStringSet(key);
    }

    /**
     * Return the set of string value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @param spUtils      The instance of {@link SPUtils}.
     * @return the set of string value if sp exists or {@code defaultValue} otherwise
     */
    public static java.util.Set<String> getStringSet(@android.support.annotation.NonNull final String key,
                                                     final java.util.Set<String> defaultValue,
                                                     @android.support.annotation.NonNull final SPUtils spUtils) {
        return spUtils.getStringSet(key, defaultValue);
    }

    /**
     * Return all values in sp.
     *
     * @param spUtils The instance of {@link SPUtils}.
     * @return all values in sp
     */
    public static java.util.Map<String, ?> getAll(@android.support.annotation.NonNull final SPUtils spUtils) {
        return spUtils.getAll();
    }

    /**
     * Return whether the sp contains the preference.
     *
     * @param key     The key of sp.
     * @param spUtils The instance of {@link SPUtils}.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean contains(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final SPUtils spUtils) {
        return spUtils.contains(key);
    }

    /**
     * Remove the preference in sp.
     *
     * @param key     The key of sp.
     * @param spUtils The instance of {@link SPUtils}.
     */
    public static void remove(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.remove(key);
    }

    /**
     * Remove the preference in sp.
     *
     * @param key      The key of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     * @param spUtils  The instance of {@link SPUtils}.
     */
    public static void remove(@android.support.annotation.NonNull final String key, final boolean isCommit, @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.remove(key, isCommit);
    }

    /**
     * Remove all preferences in sp.
     *
     * @param spUtils The instance of {@link SPUtils}.
     */
    public static void clear(@android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.clear();
    }

    /**
     * Remove all preferences in sp.
     *
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     * @param spUtils  The instance of {@link SPUtils}.
     */
    public static void clear(final boolean isCommit, @android.support.annotation.NonNull final SPUtils spUtils) {
        spUtils.clear(isCommit);
    }

    private static SPUtils getDefaultSPUtils() {
        return sDefaultSPUtils != null ? sDefaultSPUtils : SPUtils.getInstance();
    }
}