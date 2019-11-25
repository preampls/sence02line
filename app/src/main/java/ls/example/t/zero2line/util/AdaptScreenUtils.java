package ls.example.t.zero2line.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2018/11/15
 *     desc  : utils about adapt screen
 * </pre>
 */
public final class AdaptScreenUtils {

    private static java.util.List<java.lang.reflect.Field> sMetricsFields;

    private AdaptScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Adapt for the horizontal screen, and call it in {@link android.app.Activity#getResources()}.
     */
    public static android.content.res.Resources adaptWidth(final android.content.res.Resources resources, final int designWidth) {
        float newXdpi = (resources.getDisplayMetrics().widthPixels * 72f) / designWidth;
        applyDisplayMetrics(resources, newXdpi);
        return resources;
    }

    /**
     * Adapt for the vertical screen, and call it in {@link android.app.Activity#getResources()}.
     */
    public static android.content.res.Resources adaptHeight(final android.content.res.Resources resources, final int designHeight) {
        return adaptHeight(resources, designHeight, false);
    }

    /**
     * Adapt for the vertical screen, and call it in {@link android.app.Activity#getResources()}.
     */
    public static android.content.res.Resources adaptHeight(final android.content.res.Resources resources, final int designHeight, final boolean includeNavBar) {
        float screenHeight = (resources.getDisplayMetrics().heightPixels
                + (includeNavBar ? getNavBarHeight(resources) : 0)) * 72f;
        float newXdpi = screenHeight / designHeight;
        applyDisplayMetrics(resources, newXdpi);
        return resources;
    }

    private static int getNavBarHeight(final android.content.res.Resources resources) {
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return resources.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * @param resources The resources.
     * @return the resource
     */
    public static android.content.res.Resources closeAdapt(final android.content.res.Resources resources) {
        float newXdpi = android.content.res.Resources.getSystem().getDisplayMetrics().density * 72f;
        applyDisplayMetrics(resources, newXdpi);
        return resources;
    }

    /**
     * Value of pt to value of px.
     *
     * @param ptValue The value of pt.
     * @return value of px
     */
    public static int pt2Px(final float ptValue) {
        android.util.DisplayMetrics metrics = Utils.getApp().getResources().getDisplayMetrics();
        return (int) (ptValue * metrics.xdpi / 72f + 0.5);
    }

    /**
     * Value of px to value of pt.
     *
     * @param pxValue The value of px.
     * @return value of pt
     */
    public static int px2Pt(final float pxValue) {
        android.util.DisplayMetrics metrics = Utils.getApp().getResources().getDisplayMetrics();
        return (int) (pxValue * 72 / metrics.xdpi + 0.5);
    }

    private static void applyDisplayMetrics(final android.content.res.Resources resources, final float newXdpi) {
        resources.getDisplayMetrics().xdpi = newXdpi;
        Utils.getApp().getResources().getDisplayMetrics().xdpi = newXdpi;
        applyOtherDisplayMetrics(resources, newXdpi);
    }

    private static void applyOtherDisplayMetrics(final android.content.res.Resources resources, final float newXdpi) {
        if (sMetricsFields == null) {
            sMetricsFields = new java.util.ArrayList<>();
            Class resCls = resources.getClass();
            java.lang.reflect.Field[] declaredFields = resCls.getDeclaredFields();
            while (declaredFields != null && declaredFields.length > 0) {
                for (java.lang.reflect.Field field : declaredFields) {
                    if (field.getType().isAssignableFrom(android.util.DisplayMetrics.class)) {
                        field.setAccessible(true);
                        android.util.DisplayMetrics tmpDm = getMetricsFromField(resources, field);
                        if (tmpDm != null) {
                            sMetricsFields.add(field);
                            tmpDm.xdpi = newXdpi;
                        }
                    }
                }
                resCls = resCls.getSuperclass();
                if (resCls != null) {
                    declaredFields = resCls.getDeclaredFields();
                } else {
                    break;
                }
            }
        } else {
            applyMetricsFields(resources, newXdpi);
        }
    }

    private static void applyMetricsFields(final android.content.res.Resources resources, final float newXdpi) {
        for (java.lang.reflect.Field metricsField : sMetricsFields) {
            try {
                android.util.DisplayMetrics dm = (android.util.DisplayMetrics) metricsField.get(resources);
                if (dm != null) dm.xdpi = newXdpi;
            } catch (Exception e) {
                android.util.Log.e("AdaptScreenUtils", "applyMetricsFields: " + e);
            }
        }
    }

    private static android.util.DisplayMetrics getMetricsFromField(final android.content.res.Resources resources, final java.lang.reflect.Field field) {
        try {
            return (android.util.DisplayMetrics) field.get(resources);
        } catch (Exception e) {
            android.util.Log.e("AdaptScreenUtils", "getMetricsFromField: " + e);
            return null;
        }
    }
}
