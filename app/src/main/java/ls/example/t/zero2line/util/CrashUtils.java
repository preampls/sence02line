package ls.example.t.zero2line.util;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/27
 *     desc  : utils about crash
 * </pre>
 */
public final class CrashUtils {

    private static String defaultDir;
    private static String dir;
    private static String versionName;
    private static int    versionCode;

    private static final String FILE_SEP = System.getProperty("file.separator");
    @android.annotation.SuppressLint("SimpleDateFormat")
    private static final java.text.Format FORMAT   = new java.text.SimpleDateFormat("MM-dd_HH-mm-ss");

    private static final java.lang.Thread.UncaughtExceptionHandler DEFAULT_UNCAUGHT_EXCEPTION_HANDLER;
    private static final java.lang.Thread.UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER;

    private static ls.example.t.zero2line.util.CrashUtils.OnCrashListener sOnCrashListener;

    static {
        try {
            android.content.pm.PackageInfo pi = Utils.getApp()
                    .getPackageManager()
                    .getPackageInfo(Utils.getApp().getPackageName(), 0);
            if (pi != null) {
                versionName = pi.versionName;
                versionCode = pi.versionCode;
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        DEFAULT_UNCAUGHT_EXCEPTION_HANDLER = Thread.getDefaultUncaughtExceptionHandler();

        UNCAUGHT_EXCEPTION_HANDLER = new java.lang.Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                if (e == null) {
                    if (DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null) {
                        DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(t, null);
                    } else {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                    return;
                }

                final String time = FORMAT.format(new java.util.Date(System.currentTimeMillis()));
                final StringBuilder sb = new StringBuilder();
                final String head = "************* Log Head ****************" +
                        "\nTime Of Crash      : " + time +
                        "\nDevice Manufacturer: " + android.os.Build.MANUFACTURER +
                        "\nDevice Model       : " + android.os.Build.MODEL +
                        "\nAndroid Version    : " + android.os.Build.VERSION.RELEASE +
                        "\nAndroid SDK        : " + android.os.Build.VERSION.SDK_INT +
                        "\nApp VersionName    : " + versionName +
                        "\nApp VersionCode    : " + versionCode +
                        "\n************* Log Head ****************\n\n";
                sb.append(head)
                        .append(ThrowableUtils.getFullStackTrace(e));
                final String crashInfo = sb.toString();
                final String fullPath = (dir == null ? defaultDir : dir) + time + ".txt";
                if (createOrExistsFile(fullPath)) {
                    input2File(crashInfo, fullPath);
                } else {
                    android.util.Log.e("CrashUtils", "create " + fullPath + " failed!");
                }

                if (sOnCrashListener != null) {
                    sOnCrashListener.onCrash(crashInfo, e);
                }

                if (DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null) {
                    DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(t, e);
                }
            }
        };
    }

    private CrashUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Initialization.
     * <p>Must hold {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />}</p>
     */
    @android.annotation.SuppressLint("MissingPermission")
    public static void init() {
        init("");
    }

    /**
     * Initialization
     * <p>Must hold {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />}</p>
     *
     * @param crashDir The directory of saving crash information.
     */
    @android.support.annotation.RequiresPermission(WRITE_EXTERNAL_STORAGE)
    public static void init(@android.support.annotation.NonNull final java.io.File crashDir) {
        init(crashDir.getAbsolutePath(), null);
    }

    /**
     * Initialization
     * <p>Must hold {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />}</p>
     *
     * @param crashDirPath The directory's path of saving crash information.
     */
    @android.support.annotation.RequiresPermission(WRITE_EXTERNAL_STORAGE)
    public static void init(final String crashDirPath) {
        init(crashDirPath, null);
    }

    /**
     * Initialization
     * <p>Must hold {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />}</p>
     *
     * @param onCrashListener The crash listener.
     */
    @android.annotation.SuppressLint("MissingPermission")
    public static void init(final ls.example.t.zero2line.util.CrashUtils.OnCrashListener onCrashListener) {
        init("", onCrashListener);
    }

    /**
     * Initialization
     * <p>Must hold {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />}</p>
     *
     * @param crashDir        The directory of saving crash information.
     * @param onCrashListener The crash listener.
     */
    @android.support.annotation.RequiresPermission(WRITE_EXTERNAL_STORAGE)
    public static void init(@android.support.annotation.NonNull final java.io.File crashDir, final ls.example.t.zero2line.util.CrashUtils.OnCrashListener onCrashListener) {
        init(crashDir.getAbsolutePath(), onCrashListener);
    }

    /**
     * Initialization
     * <p>Must hold {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />}</p>
     *
     * @param crashDirPath    The directory's path of saving crash information.
     * @param onCrashListener The crash listener.
     */
    @android.support.annotation.RequiresPermission(WRITE_EXTERNAL_STORAGE)
    public static void init(final String crashDirPath, final ls.example.t.zero2line.util.CrashUtils.OnCrashListener onCrashListener) {
        if (isSpace(crashDirPath)) {
            dir = null;
        } else {
            dir = crashDirPath.endsWith(FILE_SEP) ? crashDirPath : crashDirPath + FILE_SEP;
        }
        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())
                && Utils.getApp().getExternalCacheDir() != null)
            defaultDir = Utils.getApp().getExternalCacheDir() + FILE_SEP + "crash" + FILE_SEP;
        else {
            defaultDir = Utils.getApp().getCacheDir() + FILE_SEP + "crash" + FILE_SEP;
        }
        sOnCrashListener = onCrashListener;
        Thread.setDefaultUncaughtExceptionHandler(UNCAUGHT_EXCEPTION_HANDLER);
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////

    public interface OnCrashListener {
        void onCrash(String crashInfo, Throwable e);
    }

    ///////////////////////////////////////////////////////////////////////////
    // other utils methods
    ///////////////////////////////////////////////////////////////////////////

    private static void input2File(final String input, final String filePath) {
        java.util.concurrent.Future<Boolean> submit = java.util.concurrent.Executors.newSingleThreadExecutor().submit(new java.util.concurrent.Callable<Boolean>() {
            @Override
            public Boolean call() {
                java.io.BufferedWriter bw = null;
                try {
                    bw = new java.io.BufferedWriter(new java.io.FileWriter(filePath, true));
                    bw.write(input);
                    return true;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        try {
            if (submit.get()) return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
        }
        android.util.Log.e("CrashUtils", "write crash info to " + filePath + " failed!");
    }

    private static boolean createOrExistsFile(final String filePath) {
        java.io.File file = new java.io.File(filePath);
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createOrExistsDir(final java.io.File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
