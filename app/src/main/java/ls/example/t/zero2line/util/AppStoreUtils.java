package ls.example.t.zero2line.util;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import java.util.List;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/05/20
 *     desc  : utils about app store
 * </pre>
 */
public class AppStoreUtils {

    private static final String TAG = "AppStoreUtils";

    private static final String GOOGLE_PLAY_APP_STORE_PACKAGE_NAME = "com.android.vending";

    /**
     * 获取跳转到应用商店的 Intent
     *
     * @return 跳转到应用商店的 Intent
     */
    public static android.content.Intent getAppStoreIntent() {
        return getAppStoreIntent(Utils.getApp().getPackageName(), false);
    }

    /**
     * 获取跳转到应用商店的 Intent
     *
     * @param isIncludeGooglePlayStore 是否包括 Google Play 商店
     * @return 跳转到应用商店的 Intent
     */
    public static android.content.Intent getAppStoreIntent(boolean isIncludeGooglePlayStore) {
        return getAppStoreIntent(Utils.getApp().getPackageName(), isIncludeGooglePlayStore);
    }

    /**
     * 获取跳转到应用商店的 Intent
     *
     * @param packageName 包名
     * @return 跳转到应用商店的 Intent
     */
    public static android.content.Intent getAppStoreIntent(final String packageName) {
        return getAppStoreIntent(packageName, false);
    }

    /**
     * 获取跳转到应用商店的 Intent
     * <p>优先跳转到手机自带的应用市场</p>
     *
     * @param packageName              包名
     * @param isIncludeGooglePlayStore 是否包括 Google Play 商店
     * @return 跳转到应用商店的 Intent
     */
    public static android.content.Intent getAppStoreIntent(final String packageName, boolean isIncludeGooglePlayStore) {
        if (RomUtils.isSamsung()) {// 三星单独处理跳转三星市场
            android.content.Intent samsungAppStoreIntent = getSamsungAppStoreIntent(packageName);
            if (samsungAppStoreIntent != null) return samsungAppStoreIntent;
        }
        if (RomUtils.isLeeco()) {// 乐视单独处理跳转乐视市场
            android.content.Intent leecoAppStoreIntent = getLeecoAppStoreIntent(packageName);
            if (leecoAppStoreIntent != null) return leecoAppStoreIntent;
        }

        android.net.Uri uri = android.net.Uri.parse("market://details?id=" + packageName);
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, uri);
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = Utils.getApp().getPackageManager()
                .queryIntentActivities(intent, android.content.pm.PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfos == null || resolveInfos.size() == 0) {
            android.util.Log.e(TAG, "No app store!");
            return null;
        }
        android.content.Intent googleIntent = null;
        for (android.content.pm.ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.packageName;
            if (!GOOGLE_PLAY_APP_STORE_PACKAGE_NAME.equals(pkgName)) {
                if (isAppSystem(pkgName)) {
                    intent.setPackage(pkgName);
                    return intent;
                }
            } else {
                intent.setPackage(GOOGLE_PLAY_APP_STORE_PACKAGE_NAME);
                googleIntent = intent;
            }
        }
        if (isIncludeGooglePlayStore && googleIntent != null) {
            return googleIntent;
        }

        intent.setPackage(resolveInfos.get(0).activityInfo.packageName);
        return intent;
    }

    private static boolean go2NormalAppStore(String packageName) {
        android.content.Intent intent = getNormalAppStoreIntent();
        if (intent == null) return false;
        intent.setData(android.net.Uri.parse("market://details?id=" + packageName));
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getApp().startActivity(intent);
        return true;
    }

    private static android.content.Intent getNormalAppStoreIntent() {
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
        android.net.Uri uri = android.net.Uri.parse("market://details?id=" + Utils.getApp().getPackageName());
        intent.setData(uri);
        if (getAvailableIntentSize(intent) > 0) {
            return intent;
        }
        return null;
    }

    private static android.content.Intent getSamsungAppStoreIntent(final String packageName) {
        android.content.Intent intent = new android.content.Intent();
        intent.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
        intent.setData(android.net.Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + packageName));
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        if (getAvailableIntentSize(intent) > 0) {
            return intent;
        }
        return null;
    }

    private static android.content.Intent getLeecoAppStoreIntent(final String packageName) {
        android.content.Intent intent = new android.content.Intent();
        intent.setClassName("com.letv.app.appstore", "com.letv.app.appstore.appmodule.details.DetailsActivity");
        intent.setAction("com.letv.app.appstore.appdetailactivity");
        intent.putExtra("packageName", packageName);
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        if (getAvailableIntentSize(intent) > 0) {
            return intent;
        }
        return null;
    }

    private static int getAvailableIntentSize(final android.content.Intent intent) {
        return Utils.getApp().getPackageManager()
                .queryIntentActivities(intent, android.content.pm.PackageManager.MATCH_DEFAULT_ONLY)
                .size();
    }

    private static boolean isAppSystem(final String packageName) {
        if (android.text.TextUtils.isEmpty(packageName)) return false;
        try {
            android.content.pm.PackageManager pm = Utils.getApp().getPackageManager();
            android.content.pm.ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
