package ls.example.t.zero2line.util;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.Manifest.permission.KILL_BACKGROUND_PROCESSES;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/10/18
 *     desc  : utils about process
 * </pre>
 */
public final class ProcessUtils {

    private ProcessUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return the foreground process name.
     * <p>Target APIs greater than 21 must hold
     * {@code <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />}</p>
     *
     * @return the foreground process name
     */
    public static String getForegroundProcessName() {
        android.app.ActivityManager am =
                (android.app.ActivityManager) Utils.getApp().getSystemService(android.content.Context.ACTIVITY_SERVICE);
        //noinspection ConstantConditions
        java.util.List<android.app.ActivityManager.RunningAppProcessInfo> pInfo = am.getRunningAppProcesses();
        if (pInfo != null && pInfo.size() > 0) {
            for (android.app.ActivityManager.RunningAppProcessInfo aInfo : pInfo) {
                if (aInfo.importance
                        == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return aInfo.processName;
                }
            }
        }
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            android.content.pm.PackageManager pm = Utils.getApp().getPackageManager();
            android.content.Intent intent = new android.content.Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
            java.util.List<android.content.pm.ResolveInfo> list =
                    pm.queryIntentActivities(intent, android.content.pm.PackageManager.MATCH_DEFAULT_ONLY);
            android.util.Log.i("ProcessUtils", list.toString());
            if (list.size() <= 0) {
                android.util.Log.i("ProcessUtils",
                        "getForegroundProcessName: noun of access to usage information.");
                return "";
            }
            try {// Access to usage information.
                android.content.pm.ApplicationInfo info =
                        pm.getApplicationInfo(Utils.getApp().getPackageName(), 0);
                android.app.AppOpsManager aom =
                        (android.app.AppOpsManager) Utils.getApp().getSystemService(android.content.Context.APP_OPS_SERVICE);
                //noinspection ConstantConditions
                if (aom.checkOpNoThrow(android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
                        info.uid,
                        info.packageName) != android.app.AppOpsManager.MODE_ALLOWED) {
                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                    Utils.getApp().startActivity(intent);
                }
                if (aom.checkOpNoThrow(android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
                        info.uid,
                        info.packageName) != android.app.AppOpsManager.MODE_ALLOWED) {
                    android.util.Log.i("ProcessUtils",
                            "getForegroundProcessName: refuse to device usage stats.");
                    return "";
                }
                android.app.usage.UsageStatsManager usageStatsManager = (android.app.usage.UsageStatsManager) Utils.getApp()
                        .getSystemService(android.content.Context.USAGE_STATS_SERVICE);
                java.util.List<android.app.usage.UsageStats> usageStatsList = null;
                if (usageStatsManager != null) {
                    long endTime = System.currentTimeMillis();
                    long beginTime = endTime - 86400000 * 7;
                    usageStatsList = usageStatsManager
                            .queryUsageStats(android.app.usage.UsageStatsManager.INTERVAL_BEST,
                                    beginTime, endTime);
                }
                if (usageStatsList == null || usageStatsList.isEmpty()) return "";
                android.app.usage.UsageStats recentStats = null;
                for (android.app.usage.UsageStats usageStats : usageStatsList) {
                    if (recentStats == null
                            || usageStats.getLastTimeUsed() > recentStats.getLastTimeUsed()) {
                        recentStats = usageStats;
                    }
                }
                return recentStats == null ? null : recentStats.getPackageName();
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * Return all background processes.
     * <p>Must hold {@code <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />}</p>
     *
     * @return all background processes
     */
    @android.support.annotation.RequiresPermission(KILL_BACKGROUND_PROCESSES)
    public static java.util.Set<String> getAllBackgroundProcesses() {
        android.app.ActivityManager am =
                (android.app.ActivityManager) Utils.getApp().getSystemService(android.content.Context.ACTIVITY_SERVICE);
        //noinspection ConstantConditions
        java.util.List<android.app.ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        java.util.Set<String> set = new java.util.HashSet<>();
        if (info != null) {
            for (android.app.ActivityManager.RunningAppProcessInfo aInfo : info) {
                java.util.Collections.addAll(set, aInfo.pkgList);
            }
        }
        return set;
    }

    /**
     * Kill all background processes.
     * <p>Must hold {@code <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />}</p>
     *
     * @return background processes were killed
     */
    @android.support.annotation.RequiresPermission(KILL_BACKGROUND_PROCESSES)
    public static java.util.Set<String> killAllBackgroundProcesses() {
        android.app.ActivityManager am =
                (android.app.ActivityManager) Utils.getApp().getSystemService(android.content.Context.ACTIVITY_SERVICE);
        //noinspection ConstantConditions
        java.util.List<android.app.ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        java.util.Set<String> set = new java.util.HashSet<>();
        if (info == null) return set;
        for (android.app.ActivityManager.RunningAppProcessInfo aInfo : info) {
            for (String pkg : aInfo.pkgList) {
                am.killBackgroundProcesses(pkg);
                set.add(pkg);
            }
        }
        info = am.getRunningAppProcesses();
        for (android.app.ActivityManager.RunningAppProcessInfo aInfo : info) {
            for (String pkg : aInfo.pkgList) {
                set.remove(pkg);
            }
        }
        return set;
    }

    /**
     * Kill background processes.
     * <p>Must hold {@code <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />}</p>
     *
     * @param packageName The name of the package.
     * @return {@code true}: success<br>{@code false}: fail
     */
    @android.support.annotation.RequiresPermission(KILL_BACKGROUND_PROCESSES)
    public static boolean killBackgroundProcesses(@android.support.annotation.NonNull final String packageName) {
        android.app.ActivityManager am =
                (android.app.ActivityManager) Utils.getApp().getSystemService(android.content.Context.ACTIVITY_SERVICE);
        //noinspection ConstantConditions
        java.util.List<android.app.ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) return true;
        for (android.app.ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (java.util.Arrays.asList(aInfo.pkgList).contains(packageName)) {
                am.killBackgroundProcesses(packageName);
            }
        }
        info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) return true;
        for (android.app.ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (java.util.Arrays.asList(aInfo.pkgList).contains(packageName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return whether app running in the main process.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isMainProcess() {
        return Utils.getApp().getPackageName().equals(Utils.getCurrentProcessName());
    }

    /**
     * Return the name of current process.
     *
     * @return the name of current process
     */
    public static String getCurrentProcessName() {
        return Utils.getCurrentProcessName();
    }
}
