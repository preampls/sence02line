package ls.example.t.zero2line.util;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.SimpleArrayMap;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/12/24
 *     desc  : utils about object
 * </pre>
 */
public final class ObjectUtils {

    private ObjectUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return whether object is empty.
     *
     * @param obj The object.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isEmpty(final Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj.getClass().isArray() && java.lang.reflect.Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof CharSequence && obj.toString().length() == 0) {
            return true;
        }
        if (obj instanceof java.util.Collection && ((java.util.Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof java.util.Map && ((java.util.Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof android.support.v4.util.SimpleArrayMap && ((android.support.v4.util.SimpleArrayMap) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof android.util.SparseArray && ((android.util.SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof android.util.SparseBooleanArray && ((android.util.SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof android.util.SparseIntArray && ((android.util.SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof android.util.SparseLongArray && ((android.util.SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        if (obj instanceof android.support.v4.util.LongSparseArray && ((android.support.v4.util.LongSparseArray) obj).size() == 0) {
            return true;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            if (obj instanceof android.util.LongSparseArray
                    && ((android.util.LongSparseArray) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(final CharSequence obj) {
        return obj == null || obj.toString().length() == 0;
    }

    public static boolean isEmpty(final java.util.Collection obj) {
        return obj == null || obj.isEmpty();
    }

    public static boolean isEmpty(final java.util.Map obj) {
        return obj == null || obj.isEmpty();
    }

    public static boolean isEmpty(final android.support.v4.util.SimpleArrayMap obj) {
        return obj == null || obj.isEmpty();
    }

    public static boolean isEmpty(final android.util.SparseArray obj) {
        return obj == null || obj.size() == 0;
    }

    public static boolean isEmpty(final android.util.SparseBooleanArray obj) {
        return obj == null || obj.size() == 0;
    }

    public static boolean isEmpty(final android.util.SparseIntArray obj) {
        return obj == null || obj.size() == 0;
    }

    public static boolean isEmpty(final android.support.v4.util.LongSparseArray obj) {
        return obj == null || obj.size() == 0;
    }

    @android.support.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static boolean isEmpty(final android.util.SparseLongArray obj) {
        return obj == null || obj.size() == 0;
    }

    @android.support.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isEmpty(final android.util.LongSparseArray obj) {
        return obj == null || obj.size() == 0;
    }

    /**
     * Return whether object is not empty.
     *
     * @param obj The object.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isNotEmpty(final Object obj) {
        return !isEmpty(obj);
    }


    public static boolean isNotEmpty(final CharSequence obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final java.util.Collection obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final java.util.Map obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final android.support.v4.util.SimpleArrayMap obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final android.util.SparseArray obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final android.util.SparseBooleanArray obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final android.util.SparseIntArray obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final android.support.v4.util.LongSparseArray obj) {
        return !isEmpty(obj);
    }

    @android.support.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static boolean isNotEmpty(final android.util.SparseLongArray obj) {
        return !isEmpty(obj);
    }

    @android.support.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isNotEmpty(final android.util.LongSparseArray obj) {
        return !isEmpty(obj);
    }

    /**
     * Return whether object1 is equals to object2.
     *
     * @param o1 The first object.
     * @param o2 The second object.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean equals(final Object o1, final Object o2) {
        return o1 == o2 || (o1 != null && o1.equals(o2));
    }

    /**
     * Require the objects are not null.
     *
     * @param objects The object.
     * @throws NullPointerException if any object is null in objects
     */
    public static void requireNonNull(final Object... objects) {
        if (objects == null) throw new NullPointerException();
        for (Object object : objects) {
            if (object == null) throw new NullPointerException();
        }
    }

    /**
     * Return the nonnull object or default object.
     *
     * @param object        The object.
     * @param defaultObject The default object to use with the object is null.
     * @param <T>           The value type.
     * @return the nonnull object or default object
     */
    public static <T> T getOrDefault(final T object, final T defaultObject) {
        if (object == null) {
            return defaultObject;
        }
        return object;
    }

    /**
     * Return the hash code of object.
     *
     * @param o The object.
     * @return the hash code of object
     */
    public static int hashCode(final Object o) {
        return o != null ? o.hashCode() : 0;
    }
}
