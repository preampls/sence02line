package ls.example.t.zero2line.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils.OnRationaleListener.ShouldRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.blankj.utilcode.constant.PermissionConstants.Permission;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/12/29
 *     desc  : utils about permission
 * </pre>
 */
public final class PermissionUtils {

    private static final java.util.List<String> PERMISSIONS = getPermissions();

    private static ls.example.t.zero2line.util.PermissionUtils sInstance;

    private ls.example.t.zero2line.util.PermissionUtils.OnRationaleListener mOnRationaleListener;
    private ls.example.t.zero2line.util.PermissionUtils.SimpleCallback      mSimpleCallback;
    private ls.example.t.zero2line.util.PermissionUtils.FullCallback        mFullCallback;
    private ls.example.t.zero2line.util.PermissionUtils.ThemeCallback       mThemeCallback;
    private java.util.Set<String>         mPermissions;
    private java.util.List<String>        mPermissionsRequest;
    private java.util.List<String>        mPermissionsGranted;
    private java.util.List<String>        mPermissionsDenied;
    private java.util.List<String>        mPermissionsDeniedForever;

    private static ls.example.t.zero2line.util.PermissionUtils.SimpleCallback sSimpleCallback4WriteSettings;
    private static ls.example.t.zero2line.util.PermissionUtils.SimpleCallback sSimpleCallback4DrawOverlays;

    /**
     * Return the permissions used in application.
     *
     * @return the permissions used in application
     */
    public static java.util.List<String> getPermissions() {
        return getPermissions(Utils.getApp().getPackageName());
    }

    /**
     * Return the permissions used in application.
     *
     * @param packageName The name of the package.
     * @return the permissions used in application
     */
    public static java.util.List<String> getPermissions(final String packageName) {
        android.content.pm.PackageManager pm = Utils.getApp().getPackageManager();
        try {
            String[] permissions = pm.getPackageInfo(packageName, android.content.pm.PackageManager.GET_PERMISSIONS).requestedPermissions;
            if (permissions == null) return java.util.Collections.emptyList();
            return java.util.Arrays.asList(permissions);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Return whether <em>you</em> have been granted the permissions.
     *
     * @param permissions The permissions.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isGranted(final String... permissions) {
        for (String permission : permissions) {
            if (!isGranted(permission)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isGranted(final String permission) {
        return android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M
                || android.content.pm.PackageManager.PERMISSION_GRANTED
                == android.support.v4.content.ContextCompat.checkSelfPermission(Utils.getApp(), permission);
    }

    /**
     * Return whether the app can modify system settings.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @android.support.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.M)
    public static boolean isGrantedWriteSettings() {
        return android.provider.Settings.System.canWrite(Utils.getApp());
    }

    @android.support.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.M)
    public static void requestWriteSettings(final ls.example.t.zero2line.util.PermissionUtils.SimpleCallback callback) {
        if (isGrantedWriteSettings()) {
            if (callback != null) callback.onGranted();
            return;
        }
        sSimpleCallback4WriteSettings = callback;
        ls.example.t.zero2line.util.PermissionUtils.PermissionActivity.start(Utils.getApp(), ls.example.t.zero2line.util.PermissionUtils.PermissionActivity.TYPE_WRITE_SETTINGS);
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.M)
    private static void startWriteSettingsActivity(final android.app.Activity activity, final int requestCode) {
        android.content.Intent intent = new android.content.Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(android.net.Uri.parse("package:" + Utils.getApp().getPackageName()));
        if (!isIntentAvailable(intent)) {
            launchAppDetailsSettings();
            return;
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Return whether the app can draw on top of other apps.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @android.support.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.M)
    public static boolean isGrantedDrawOverlays() {
        return android.provider.Settings.canDrawOverlays(Utils.getApp());
    }

    @android.support.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.M)
    public static void requestDrawOverlays(final ls.example.t.zero2line.util.PermissionUtils.SimpleCallback callback) {
        if (isGrantedDrawOverlays()) {
            if (callback != null) callback.onGranted();
            return;
        }
        sSimpleCallback4DrawOverlays = callback;
        ls.example.t.zero2line.util.PermissionUtils.PermissionActivity.start(Utils.getApp(), ls.example.t.zero2line.util.PermissionUtils.PermissionActivity.TYPE_DRAW_OVERLAYS);
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.M)
    private static void startOverlayPermissionActivity(final android.app.Activity activity, final int requestCode) {
        android.content.Intent intent = new android.content.Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(android.net.Uri.parse("package:" + Utils.getApp().getPackageName()));
        if (!isIntentAvailable(intent)) {
            launchAppDetailsSettings();
            return;
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Launch the application's details settings.
     */
    public static void launchAppDetailsSettings() {
        android.content.Intent intent = new android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(android.net.Uri.parse("package:" + Utils.getApp().getPackageName()));
        if (!isIntentAvailable(intent)) return;
        Utils.getApp().startActivity(intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * Set the permissions.
     *
     * @param permissions The permissions.
     * @return the single {@link PermissionUtils} instance
     */
    public static ls.example.t.zero2line.util.PermissionUtils permission(@Permission final String... permissions) {
        return new ls.example.t.zero2line.util.PermissionUtils(permissions);
    }

    private static boolean isIntentAvailable(final android.content.Intent intent) {
        return Utils.getApp()
                .getPackageManager()
                .queryIntentActivities(intent, android.content.pm.PackageManager.MATCH_DEFAULT_ONLY)
                .size() > 0;
    }

    private PermissionUtils(final String... permissions) {
        mPermissions = new java.util.LinkedHashSet<>();
        for (String permission : permissions) {
            for (String aPermission : PermissionConstants.getPermissions(permission)) {
                if (PERMISSIONS.contains(aPermission)) {
                    mPermissions.add(aPermission);
                }
            }
        }
        sInstance = this;
    }

    /**
     * Set rationale listener.
     *
     * @param listener The rationale listener.
     * @return the single {@link PermissionUtils} instance
     */
    public ls.example.t.zero2line.util.PermissionUtils rationale(final ls.example.t.zero2line.util.PermissionUtils.OnRationaleListener listener) {
        mOnRationaleListener = listener;
        return this;
    }

    /**
     * Set the simple call back.
     *
     * @param callback the simple call back
     * @return the single {@link PermissionUtils} instance
     */
    public ls.example.t.zero2line.util.PermissionUtils callback(final ls.example.t.zero2line.util.PermissionUtils.SimpleCallback callback) {
        mSimpleCallback = callback;
        return this;
    }

    /**
     * Set the full call back.
     *
     * @param callback the full call back
     * @return the single {@link PermissionUtils} instance
     */
    public ls.example.t.zero2line.util.PermissionUtils callback(final ls.example.t.zero2line.util.PermissionUtils.FullCallback callback) {
        mFullCallback = callback;
        return this;
    }

    /**
     * Set the theme callback.
     *
     * @param callback The theme callback.
     * @return the single {@link PermissionUtils} instance
     */
    public ls.example.t.zero2line.util.PermissionUtils theme(final ls.example.t.zero2line.util.PermissionUtils.ThemeCallback callback) {
        mThemeCallback = callback;
        return this;
    }

    /**
     * Start request.
     */
    public void request() {
        mPermissionsGranted = new java.util.ArrayList<>();
        mPermissionsRequest = new java.util.ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            mPermissionsGranted.addAll(mPermissions);
            requestCallback();
        } else {
            for (String permission : mPermissions) {
                if (isGranted(permission)) {
                    mPermissionsGranted.add(permission);
                } else {
                    mPermissionsRequest.add(permission);
                }
            }
            if (mPermissionsRequest.isEmpty()) {
                requestCallback();
            } else {
                startPermissionActivity();
            }
        }
    }

    @android.support.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.M)
    private void startPermissionActivity() {
        mPermissionsDenied = new java.util.ArrayList<>();
        mPermissionsDeniedForever = new java.util.ArrayList<>();
        ls.example.t.zero2line.util.PermissionUtils.PermissionActivity.start(Utils.getApp(), ls.example.t.zero2line.util.PermissionUtils.PermissionActivity.TYPE_RUNTIME);
    }

    @android.support.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.M)
    private boolean rationale(final android.app.Activity activity) {
        boolean isRationale = false;
        if (mOnRationaleListener != null) {
            for (String permission : mPermissionsRequest) {
                if (activity.shouldShowRequestPermissionRationale(permission)) {
                    getPermissionsStatus(activity);
                    mOnRationaleListener.rationale(new ls.example.t.zero2line.util.PermissionUtils.OnRationaleListener.ShouldRequest() {
                        @Override
                        public void again(boolean again) {
                            activity.finish();
                            if (again) {
                                startPermissionActivity();
                            } else {
                                requestCallback();
                            }
                        }
                    });
                    isRationale = true;
                    break;
                }
            }
            mOnRationaleListener = null;
        }
        return isRationale;
    }

    private void getPermissionsStatus(final android.app.Activity activity) {
        for (String permission : mPermissionsRequest) {
            if (isGranted(permission)) {
                mPermissionsGranted.add(permission);
            } else {
                mPermissionsDenied.add(permission);
                if (!activity.shouldShowRequestPermissionRationale(permission)) {
                    mPermissionsDeniedForever.add(permission);
                }
            }
        }
    }

    private void requestCallback() {
        if (mSimpleCallback != null) {
            if (mPermissionsRequest.size() == 0
                    || mPermissions.size() == mPermissionsGranted.size()) {
                mSimpleCallback.onGranted();
            } else {
                if (!mPermissionsDenied.isEmpty()) {
                    mSimpleCallback.onDenied();
                }
            }
            mSimpleCallback = null;
        }
        if (mFullCallback != null) {
            if (mPermissionsRequest.size() == 0
                    || mPermissions.size() == mPermissionsGranted.size()) {
                mFullCallback.onGranted(mPermissionsGranted);
            } else {
                if (!mPermissionsDenied.isEmpty()) {
                    mFullCallback.onDenied(mPermissionsDeniedForever, mPermissionsDenied);
                }
            }
            mFullCallback = null;
        }
        mOnRationaleListener = null;
        mThemeCallback = null;
    }

    private void onRequestPermissionsResult(final android.app.Activity activity) {
        getPermissionsStatus(activity);
        requestCallback();
    }


    @android.support.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.M)
    public static class PermissionActivity extends android.app.Activity {

        private static final String TYPE                = "TYPE";
        public static final  int    TYPE_RUNTIME        = 0x01;
        public static final  int    TYPE_WRITE_SETTINGS = 0x02;
        public static final  int    TYPE_DRAW_OVERLAYS  = 0x03;

        public static void start(final android.content.Context context, int type) {
            android.content.Intent starter = new android.content.Intent(context, ls.example.t.zero2line.util.PermissionUtils.PermissionActivity.class);
            starter.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            starter.putExtra(TYPE, type);
            context.startActivity(starter);
        }

        @Override
        protected void onCreate(@android.support.annotation.Nullable android.os.Bundle savedInstanceState) {
            getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | android.view.WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            int byteExtra = getIntent().getIntExtra(TYPE, TYPE_RUNTIME);
            if (byteExtra == TYPE_RUNTIME) {
                if (sInstance == null) {
                    super.onCreate(savedInstanceState);
                    android.util.Log.e("PermissionUtils", "request permissions failed");
                    finish();
                    return;
                }
                if (sInstance.mThemeCallback != null) {
                    sInstance.mThemeCallback.onActivityCreate(this);
                }
                super.onCreate(savedInstanceState);

                if (sInstance.rationale(this)) {
                    return;
                }
                if (sInstance.mPermissionsRequest != null) {
                    int size = sInstance.mPermissionsRequest.size();
                    if (size <= 0) {
                        finish();
                        return;
                    }
                    requestPermissions(sInstance.mPermissionsRequest.toArray(new String[size]), 1);
                }
            } else if (byteExtra == TYPE_WRITE_SETTINGS) {
                super.onCreate(savedInstanceState);
                startWriteSettingsActivity(this, TYPE_WRITE_SETTINGS);
            } else if (byteExtra == TYPE_DRAW_OVERLAYS) {
                super.onCreate(savedInstanceState);
                startOverlayPermissionActivity(this, TYPE_DRAW_OVERLAYS);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               @android.support.annotation.NonNull String[] permissions,
                                               @android.support.annotation.NonNull int[] grantResults) {
            if (sInstance != null && sInstance.mPermissionsRequest != null) {
                sInstance.onRequestPermissionsResult(this);
            }
            finish();
        }

        @Override
        public boolean dispatchTouchEvent(android.view.MotionEvent ev) {
            finish();
            return true;
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
            if (requestCode == TYPE_WRITE_SETTINGS) {
                if (sSimpleCallback4WriteSettings == null) return;
                if (isGrantedWriteSettings()) {
                    sSimpleCallback4WriteSettings.onGranted();
                } else {
                    sSimpleCallback4WriteSettings.onDenied();
                }
                sSimpleCallback4WriteSettings = null;
            } else if (requestCode == TYPE_DRAW_OVERLAYS) {
                if (sSimpleCallback4DrawOverlays == null) return;
                Utils.runOnUiThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isGrantedDrawOverlays()) {
                            sSimpleCallback4DrawOverlays.onGranted();
                        } else {
                            sSimpleCallback4DrawOverlays.onDenied();
                        }
                        sSimpleCallback4DrawOverlays = null;
                    }
                }, 100);
            }
            finish();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////

    public interface OnRationaleListener {

        void rationale(ls.example.t.zero2line.util.PermissionUtils.OnRationaleListener.ShouldRequest shouldRequest);

        interface ShouldRequest {
            void again(boolean again);
        }
    }

    public interface SimpleCallback {
        void onGranted();

        void onDenied();
    }

    public interface FullCallback {
        void onGranted(java.util.List<String> permissionsGranted);

        void onDenied(java.util.List<String> permissionsDeniedForever, java.util.List<String> permissionsDenied);
    }

    public interface ThemeCallback {
        void onActivityCreate(android.app.Activity activity);
    }
}