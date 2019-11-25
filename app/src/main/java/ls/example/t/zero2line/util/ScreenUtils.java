package ls.example.t.zero2line.util;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static android.Manifest.permission.WRITE_SETTINGS;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/02
 *     desc  : utils about screen
 * </pre>
 */
public final class ScreenUtils {

    private ScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return the width of screen, in pixel.
     *
     * @return the width of screen, in pixel
     */
    public static int getScreenWidth() {
        android.view.WindowManager wm = (android.view.WindowManager) Utils.getApp().getSystemService(android.content.Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        android.graphics.Point point = new android.graphics.Point();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    /**
     * Return the height of screen, in pixel.
     *
     * @return the height of screen, in pixel
     */
    public static int getScreenHeight() {
        android.view.WindowManager wm = (android.view.WindowManager) Utils.getApp().getSystemService(android.content.Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        android.graphics.Point point = new android.graphics.Point();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    /**
     * Return the application's width of screen, in pixel.
     *
     * @return the application's width of screen, in pixel
     */
    public static int getAppScreenWidth() {
        android.view.WindowManager wm = (android.view.WindowManager) Utils.getApp().getSystemService(android.content.Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        android.graphics.Point point = new android.graphics.Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    /**
     * Return the application's height of screen, in pixel.
     *
     * @return the application's height of screen, in pixel
     */
    public static int getAppScreenHeight() {
        android.view.WindowManager wm = (android.view.WindowManager) Utils.getApp().getSystemService(android.content.Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        android.graphics.Point point = new android.graphics.Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

    /**
     * Return the density of screen.
     *
     * @return the density of screen
     */
    public static float getScreenDensity() {
        return Utils.getApp().getResources().getDisplayMetrics().density;
    }

    /**
     * Return the screen density expressed as dots-per-inch.
     *
     * @return the screen density expressed as dots-per-inch
     */
    public static int getScreenDensityDpi() {
        return Utils.getApp().getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * Set full screen.
     *
     * @param activity The activity.
     */
    public static void setFullScreen(@android.support.annotation.NonNull final android.app.Activity activity) {
        activity.getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Set non full screen.
     *
     * @param activity The activity.
     */
    public static void setNonFullScreen(@android.support.annotation.NonNull final android.app.Activity activity) {
        activity.getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Toggle full screen.
     *
     * @param activity The activity.
     */
    public static void toggleFullScreen(@android.support.annotation.NonNull final android.app.Activity activity) {
        boolean isFullScreen = isFullScreen(activity);
        android.view.Window window = activity.getWindow();
        if (isFullScreen) {
            window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            window.addFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * Return whether screen is full.
     *
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isFullScreen(@android.support.annotation.NonNull final android.app.Activity activity) {
        int fullScreenFlag = android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
        return (activity.getWindow().getAttributes().flags & fullScreenFlag) == fullScreenFlag;
    }

    /**
     * Set the screen to landscape.
     *
     * @param activity The activity.
     */
    public static void setLandscape(@android.support.annotation.NonNull final android.app.Activity activity) {
        activity.setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * Set the screen to portrait.
     *
     * @param activity The activity.
     */
    public static void setPortrait(@android.support.annotation.NonNull final android.app.Activity activity) {
        activity.setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Return whether screen is landscape.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isLandscape() {
        return Utils.getApp().getResources().getConfiguration().orientation
                == android.content.res.Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Return whether screen is portrait.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isPortrait() {
        return Utils.getApp().getResources().getConfiguration().orientation
                == android.content.res.Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * Return the rotation of screen.
     *
     * @param activity The activity.
     * @return the rotation of screen
     */
    public static int getScreenRotation(@android.support.annotation.NonNull final android.app.Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case android.view.Surface.ROTATION_0:
                return 0;
            case android.view.Surface.ROTATION_90:
                return 90;
            case android.view.Surface.ROTATION_180:
                return 180;
            case android.view.Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }

    /**
     * Return the bitmap of screen.
     *
     * @param activity The activity.
     * @return the bitmap of screen
     */
    public static android.graphics.Bitmap screenShot(@android.support.annotation.NonNull final android.app.Activity activity) {
        return screenShot(activity, false);
    }

    /**
     * Return the bitmap of screen.
     *
     * @param activity          The activity.
     * @param isDeleteStatusBar True to delete status bar, false otherwise.
     * @return the bitmap of screen
     */
    public static android.graphics.Bitmap screenShot(@android.support.annotation.NonNull final android.app.Activity activity, boolean isDeleteStatusBar) {
        android.view.View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.setWillNotCacheDrawing(false);
        android.graphics.Bitmap bmp = decorView.getDrawingCache();
        if (bmp == null) return null;
        android.util.DisplayMetrics dm = new android.util.DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        android.graphics.Bitmap ret;
        if (isDeleteStatusBar) {
            android.content.res.Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = resources.getDimensionPixelSize(resourceId);
            ret = android.graphics.Bitmap.createBitmap(
                    bmp,
                    0,
                    statusBarHeight,
                    dm.widthPixels,
                    dm.heightPixels - statusBarHeight
            );
        } else {
            ret = android.graphics.Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        }
        decorView.destroyDrawingCache();
        return ret;
    }

    /**
     * Return whether screen is locked.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isScreenLock() {
        android.app.KeyguardManager km =
                (android.app.KeyguardManager) Utils.getApp().getSystemService(android.content.Context.KEYGUARD_SERVICE);
        if (km == null) return false;
        return km.inKeyguardRestrictedInputMode();
    }

    /**
     * Set the duration of sleep.
     * <p>Must hold {@code <uses-permission android:name="android.permission.WRITE_SETTINGS" />}</p>
     *
     * @param duration The duration.
     */
    @android.support.annotation.RequiresPermission(WRITE_SETTINGS)
    public static void setSleepDuration(final int duration) {
        android.provider.Settings.System.putInt(
                Utils.getApp().getContentResolver(),
                android.provider.Settings.System.SCREEN_OFF_TIMEOUT,
                duration
        );
    }

    /**
     * Return the duration of sleep.
     *
     * @return the duration of sleep.
     */
    public static int getSleepDuration() {
        try {
            return android.provider.Settings.System.getInt(
                    Utils.getApp().getContentResolver(),
                    android.provider.Settings.System.SCREEN_OFF_TIMEOUT
            );
        } catch (android.provider.Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return -123;
        }
    }
}
