package ls.example.t.zero2line.util;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/06/18
 *     desc  : utils about view
 * </pre>
 */
public class ViewUtils {

    /**
     * Set the enabled state of this view.
     *
     * @param view    The view.
     * @param enabled True to enabled, false otherwise.
     */
    public static void setViewEnabled(android.view.View view, boolean enabled) {
        setViewEnabled(view, enabled, (android.view.View) null);
    }

    /**
     * Set the enabled state of this view.
     *
     * @param view     The view.
     * @param enabled  True to enabled, false otherwise.
     * @param excludes The excludes.
     */
    public static void setViewEnabled(android.view.View view, boolean enabled, android.view.View... excludes) {
        if (view == null) return;
        if (excludes != null) {
            for (android.view.View exclude : excludes) {
                if (view == exclude) return;
            }
        }
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                setViewEnabled(viewGroup.getChildAt(i), enabled, excludes);
            }
        }
        view.setEnabled(enabled);
    }

    /**
     * @param runnable The runnable
     */
    public static void runOnUiThread(final Runnable runnable) {
        Utils.runOnUiThread(runnable);
    }

    /**
     * @param runnable    The runnable.
     * @param delayMillis The delay (in milliseconds) until the Runnable will be executed.
     */
    public static void runOnUiThreadDelayed(final Runnable runnable, long delayMillis) {
        Utils.runOnUiThreadDelayed(runnable, delayMillis);
    }

    /**
     * Return whether horizontal layout direction of views are from Right to Left.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isLayoutRtl() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            java.util.Locale primaryLocale;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                primaryLocale = Utils.getApp().getResources().getConfiguration().getLocales().get(0);
            } else {
                primaryLocale = Utils.getApp().getResources().getConfiguration().locale;
            }
            return android.text.TextUtils.getLayoutDirectionFromLocale(primaryLocale) == android.view.View.LAYOUT_DIRECTION_RTL;
        }
        return false;
    }

    /**
     * Fix the problem of topping the ScrollView nested ListView/GridView/WebView/RecyclerView.
     *
     * @param view The root view inner of ScrollView.
     */
    public static void fixScrollViewTopping(android.view.View view) {
        view.setFocusable(false);
        android.view.ViewGroup viewGroup = null;
        if (view instanceof android.view.ViewGroup) {
            viewGroup = (android.view.ViewGroup) view;
        }
        if (viewGroup == null) {
            return;
        }
        for (int i = 0, n = viewGroup.getChildCount(); i < n; i++) {
            android.view.View childAt = viewGroup.getChildAt(i);
            childAt.setFocusable(false);
            if (childAt instanceof android.view.ViewGroup) {
                fixScrollViewTopping(childAt);
            }
        }
    }
}