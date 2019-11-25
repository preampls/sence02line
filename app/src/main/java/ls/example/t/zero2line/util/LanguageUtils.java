package ls.example.t.zero2line.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.List;
import java.util.Locale;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/06/20
 *     desc  : utils about language
 * </pre>
 */
public class LanguageUtils {

    private static final String KEY_LOCALE          = "KEY_LOCALE";
    private static final String VALUE_FOLLOW_SYSTEM = "VALUE_FOLLOW_SYSTEM";

    private LanguageUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Apply the system language.
     *
     * @param activityClz The class of activity will be started after apply system language.
     */
    public static void applySystemLanguage(final Class<? extends android.app.Activity> activityClz) {
        applyLanguage(android.content.res.Resources.getSystem().getConfiguration().locale, activityClz, true);
    }

    /**
     * Apply the system language.
     *
     * @param activityClassName The full class name of activity will be started after apply system language.
     */
    public static void applySystemLanguage(final String activityClassName) {
        applyLanguage(android.content.res.Resources.getSystem().getConfiguration().locale, activityClassName, true);
    }

    /**
     * Apply the language.
     *
     * @param locale      The language of locale.
     * @param activityClz The class of activity will be started after apply system language.
     *                    It will start the launcher activity if the class is null.
     */
    public static void applyLanguage(@android.support.annotation.NonNull final java.util.Locale locale,
                                     final Class<? extends android.app.Activity> activityClz) {
        applyLanguage(locale, activityClz, false);
    }

    /**
     * Apply the language.
     *
     * @param locale            The language of locale.
     * @param activityClassName The class of activity will be started after apply system language.
     *                          It will start the launcher activity if the class name is null.
     */
    public static void applyLanguage(@android.support.annotation.NonNull final java.util.Locale locale,
                                     final String activityClassName) {
        applyLanguage(locale, activityClassName, false);
    }

    private static void applyLanguage(@android.support.annotation.NonNull final java.util.Locale locale,
                                      final Class<? extends android.app.Activity> activityClz,
                                      final boolean isFollowSystem) {
        if (activityClz == null) {
            applyLanguage(locale, "", isFollowSystem);
            return;
        }
        applyLanguage(locale, activityClz.getName(), isFollowSystem);
    }

    private static void applyLanguage(@android.support.annotation.NonNull final java.util.Locale locale,
                                      final String activityClassName,
                                      final boolean isFollowSystem) {
        if (isFollowSystem) {
            Utils.getSpUtils4Utils().put(KEY_LOCALE, VALUE_FOLLOW_SYSTEM);
        } else {
            String localLanguage = locale.getLanguage();
            String localCountry = locale.getCountry();
            Utils.getSpUtils4Utils().put(KEY_LOCALE, localLanguage + "$" + localCountry);
        }

        updateLanguage(Utils.getApp(), locale);

        android.content.Intent intent = new android.content.Intent();
        String realActivityClassName
                = android.text.TextUtils.isEmpty(activityClassName) ? getLauncherActivity() : activityClassName;
        intent.setComponent(new android.content.ComponentName(Utils.getApp(), realActivityClassName));
        intent.addFlags(
                android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                        | android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        Utils.getApp().startActivity(intent);
    }

    static void applyLanguage(@android.support.annotation.NonNull final android.app.Activity activity) {
        final String spLocale = Utils.getSpUtils4Utils().getString(KEY_LOCALE);
        if (android.text.TextUtils.isEmpty(spLocale)) {
            return;
        }

        if (VALUE_FOLLOW_SYSTEM.equals(spLocale)) {
            java.util.Locale sysLocale = android.content.res.Resources.getSystem().getConfiguration().locale;
            updateLanguage(Utils.getApp(), sysLocale);
            updateLanguage(activity, sysLocale);
            return;
        }

        String[] language_country = spLocale.split("\\$");
        if (language_country.length != 2) {
            android.util.Log.e("LanguageUtils", "The string of " + spLocale + " is not in the correct format.");
            return;
        }

        java.util.Locale settingLocale = new java.util.Locale(language_country[0], language_country[1]);
        updateLanguage(Utils.getApp(), settingLocale);
        updateLanguage(activity, settingLocale);
    }

    private static void updateLanguage(android.content.Context context, java.util.Locale locale) {
        android.content.res.Resources resources = context.getResources();
        android.content.res.Configuration config = resources.getConfiguration();
        java.util.Locale contextLocale = config.locale;
        if (equals(contextLocale.getLanguage(), locale.getLanguage())
                && equals(contextLocale.getCountry(), locale.getCountry())) {
            return;
        }
        android.util.DisplayMetrics dm = resources.getDisplayMetrics();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            context.createConfigurationContext(config);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, dm);
    }

    private static boolean equals(final CharSequence s1, final CharSequence s2) {
        if (s1 == s2) return true;
        int length;
        if (s1 != null && s2 != null && (length = s1.length()) == s2.length()) {
            if (s1 instanceof String && s2 instanceof String) {
                return s1.equals(s2);
            } else {
                for (int i = 0; i < length; i++) {
                    if (s1.charAt(i) != s2.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    private static String getLauncherActivity() {
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_MAIN, null);
        intent.addCategory(android.content.Intent.CATEGORY_LAUNCHER);
        intent.setPackage(Utils.getApp().getPackageName());
        android.content.pm.PackageManager pm = Utils.getApp().getPackageManager();
        java.util.List<android.content.pm.ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        android.content.pm.ResolveInfo next = info.iterator().next();
        if (next != null) {
            return next.activityInfo.name;
        }
        return "no launcher activity";
    }
}
