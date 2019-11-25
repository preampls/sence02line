package ls.example.t.zero2line.util;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.support.annotation.NonNull;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2018/05/15
 *     desc  : utils about meta-data
 * </pre>
 */
public final class MetaDataUtils {

    private MetaDataUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return the value of meta-data in application.
     *
     * @param key The key of meta-data.
     * @return the value of meta-data in application
     */
    public static String getMetaDataInApp(@android.support.annotation.NonNull final String key) {
        String value = "";
        android.content.pm.PackageManager pm = Utils.getApp().getPackageManager();
        String packageName = Utils.getApp().getPackageName();
        try {
            android.content.pm.ApplicationInfo ai = pm.getApplicationInfo(packageName, android.content.pm.PackageManager.GET_META_DATA);
            value = String.valueOf(ai.metaData.get(key));
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Return the value of meta-data in activity.
     *
     * @param activity The activity.
     * @param key      The key of meta-data.
     * @return the value of meta-data in activity
     */
    public static String getMetaDataInActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                               @android.support.annotation.NonNull final String key) {
        return getMetaDataInActivity(activity.getClass(), key);
    }

    /**
     * Return the value of meta-data in activity.
     *
     * @param clz The activity class.
     * @param key The key of meta-data.
     * @return the value of meta-data in activity
     */
    public static String getMetaDataInActivity(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                               @android.support.annotation.NonNull final String key) {
        String value = "";
        android.content.pm.PackageManager pm = Utils.getApp().getPackageManager();
        android.content.ComponentName componentName = new android.content.ComponentName(Utils.getApp(), clz);
        try {
            android.content.pm.ActivityInfo ai = pm.getActivityInfo(componentName, android.content.pm.PackageManager.GET_META_DATA);
            value = String.valueOf(ai.metaData.get(key));
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Return the value of meta-data in service.
     *
     * @param service The service.
     * @param key     The key of meta-data.
     * @return the value of meta-data in service
     */
    public static String getMetaDataInService(@android.support.annotation.NonNull final android.app.Service service,
                                              @android.support.annotation.NonNull final String key) {
        return getMetaDataInService(service.getClass(), key);
    }

    /**
     * Return the value of meta-data in service.
     *
     * @param clz The service class.
     * @param key The key of meta-data.
     * @return the value of meta-data in service
     */
    public static String getMetaDataInService(@android.support.annotation.NonNull final Class<? extends android.app.Service> clz,
                                              @android.support.annotation.NonNull final String key) {
        String value = "";
        android.content.pm.PackageManager pm = Utils.getApp().getPackageManager();
        android.content.ComponentName componentName = new android.content.ComponentName(Utils.getApp(), clz);
        try {
            android.content.pm.ServiceInfo info = pm.getServiceInfo(componentName, android.content.pm.PackageManager.GET_META_DATA);
            value = String.valueOf(info.metaData.get(key));
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Return the value of meta-data in receiver.
     *
     * @param receiver The receiver.
     * @param key      The key of meta-data.
     * @return the value of meta-data in receiver
     */
    public static String getMetaDataInReceiver(@android.support.annotation.NonNull final android.content.BroadcastReceiver receiver,
                                               @android.support.annotation.NonNull final String key) {
        return getMetaDataInReceiver(receiver, key);
    }

    /**
     * Return the value of meta-data in receiver.
     *
     * @param clz The receiver class.
     * @param key The key of meta-data.
     * @return the value of meta-data in receiver
     */
    public static String getMetaDataInReceiver(@android.support.annotation.NonNull final Class<? extends android.content.BroadcastReceiver> clz,
                                               @android.support.annotation.NonNull final String key) {
        String value = "";
        android.content.pm.PackageManager pm = Utils.getApp().getPackageManager();
        android.content.ComponentName componentName = new android.content.ComponentName(Utils.getApp(), clz);
        try {
            android.content.pm.ActivityInfo info = pm.getReceiverInfo(componentName, android.content.pm.PackageManager.GET_META_DATA);
            value = String.valueOf(info.metaData.get(key));
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }
}
