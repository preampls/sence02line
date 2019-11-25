package ls.example.t.zero2line.util;

import android.content.ContentResolver;
import android.provider.Settings;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2018/02/08
 *     desc  : utils about brightness
 * </pre>
 */
public final class BrightnessUtils {

    private BrightnessUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return whether automatic brightness mode is enabled.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAutoBrightnessEnabled() {
        try {
            int mode = android.provider.Settings.System.getInt(
                    Utils.getApp().getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE
            );
            return mode == android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (android.provider.Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Enable or disable automatic brightness mode.
     * <p>Must hold {@code <uses-permission android:name="android.permission.WRITE_SETTINGS" />}</p>
     *
     * @param enabled True to enabled, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean setAutoBrightnessEnabled(final boolean enabled) {
        return android.provider.Settings.System.putInt(
                Utils.getApp().getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                enabled ? android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                        : android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        );
    }

    /**
     * 获取屏幕亮度
     *
     * @return 屏幕亮度 0-255
     */
    public static int getBrightness() {
        try {
            return android.provider.Settings.System.getInt(
                    Utils.getApp().getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS
            );
        } catch (android.provider.Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 设置屏幕亮度
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.WRITE_SETTINGS" />}</p>
     * 并得到授权
     *
     * @param brightness 亮度值
     */
    public static boolean setBrightness(@android.support.annotation.IntRange(from = 0, to = 255) final int brightness) {
        android.content.ContentResolver resolver = Utils.getApp().getContentResolver();
        boolean b = android.provider.Settings.System.putInt(resolver, android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
        resolver.notifyChange(android.provider.Settings.System.getUriFor("screen_brightness"), null);
        return b;
    }

    /**
     * 设置窗口亮度
     *
     * @param window     窗口
     * @param brightness 亮度值
     */
    public static void setWindowBrightness(@android.support.annotation.NonNull final android.view.Window window,
                                           @android.support.annotation.IntRange(from = 0, to = 255) final int brightness) {
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255f;
        window.setAttributes(lp);
    }

    /**
     * 获取窗口亮度
     *
     * @param window 窗口
     * @return 屏幕亮度 0-255
     */
    public static int getWindowBrightness(final android.view.Window window) {
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        float brightness = lp.screenBrightness;
        if (brightness < 0) return getBrightness();
        return (int) (brightness * 255);
    }
}
