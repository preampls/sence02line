package ls.example.t.zero2line.util;

import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;

import static android.hardware.Camera.Parameters.FLASH_MODE_OFF;
import static android.hardware.Camera.Parameters.FLASH_MODE_TORCH;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2018/04/27
 *     desc  : utils about flashlight
 * </pre>
 */
public final class FlashlightUtils {

    private static android.hardware.Camera         mCamera;
    private static android.graphics.SurfaceTexture mSurfaceTexture;

    private FlashlightUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return whether the device supports flashlight.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isFlashlightEnable() {
        return Utils.getApp()
                .getPackageManager()
                .hasSystemFeature(android.content.pm.PackageManager.FEATURE_CAMERA_FLASH);
    }

    /**
     * Return whether the flashlight is working.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isFlashlightOn() {
        if (!init()) return false;
        android.hardware.Camera.Parameters parameters = mCamera.getParameters();
        return FLASH_MODE_TORCH.equals(parameters.getFlashMode());
    }

    /**
     * Turn on or turn off the flashlight.
     *
     * @param isOn True to turn on the flashlight, false otherwise.
     */
    public static void setFlashlightStatus(final boolean isOn) {
        if (!init()) return;
        final android.hardware.Camera.Parameters parameters = mCamera.getParameters();
        if (isOn) {
            if (!FLASH_MODE_TORCH.equals(parameters.getFlashMode())) {
                try {
                    mCamera.setPreviewTexture(mSurfaceTexture);
                    mCamera.startPreview();
                    parameters.setFlashMode(FLASH_MODE_TORCH);
                    mCamera.setParameters(parameters);
                } catch (java.io.IOException e) {
                    android.util.Log.e("FlashlightUtils", "setFlashlightStatusOn: ", e);
                }
            }
        } else {
            if (!FLASH_MODE_OFF.equals(parameters.getFlashMode())) {
                parameters.setFlashMode(FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            }
        }
    }

    /**
     * Destroy the flashlight.
     */
    public static void destroy() {
        if (mCamera == null) return;
        mCamera.release();
        mSurfaceTexture = null;
        mCamera = null;
    }

    private static boolean init() {
        if (mCamera == null) {
            try {
                mCamera = android.hardware.Camera.open(0);
                mSurfaceTexture = new android.graphics.SurfaceTexture(0);
            } catch (Throwable t) {
                android.util.Log.e("FlashlightUtils", "init failed: ", t);
                return false;
            }
        }
        if (mCamera == null) {
            android.util.Log.e("FlashlightUtils", "init failed.");
            return false;
        }
        return true;
    }
}
