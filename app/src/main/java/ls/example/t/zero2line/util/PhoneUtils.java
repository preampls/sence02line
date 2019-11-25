package ls.example.t.zero2line.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/02
 *     desc  : utils about phone
 * </pre>
 */
public final class PhoneUtils {

    private PhoneUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return whether the device is phone.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isPhone() {
        android.telephony.TelephonyManager tm = getTelephonyManager();
        return tm.getPhoneType() != android.telephony.TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * Return the unique device id.
     * <p>If the version of SDK is greater than 28, it will return an empty string.</p>
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the unique device id
     */
    @android.annotation.SuppressLint("HardwareIds")
    @android.support.annotation.RequiresPermission(READ_PHONE_STATE)
    public static String getDeviceId() {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        android.telephony.TelephonyManager tm = getTelephonyManager();
        String deviceId = tm.getDeviceId();
        if (!android.text.TextUtils.isEmpty(deviceId)) return deviceId;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String imei = tm.getImei();
            if (!android.text.TextUtils.isEmpty(imei)) return imei;
            String meid = tm.getMeid();
            return android.text.TextUtils.isEmpty(meid) ? "" : meid;
        }
        return "";
    }

    /**
     * Return the serial of device.
     *
     * @return the serial of device
     */
    @android.annotation.SuppressLint("HardwareIds")
    @android.support.annotation.RequiresPermission(READ_PHONE_STATE)
    public static String getSerial() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ? android.os.Build.getSerial() : android.os.Build.SERIAL;
    }

    /**
     * Return the IMEI.
     * <p>If the version of SDK is greater than 28, it will return an empty string.</p>
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the IMEI
     */
    @android.support.annotation.RequiresPermission(READ_PHONE_STATE)
    public static String getIMEI() {
        return getImeiOrMeid(true);
    }

    /**
     * Return the MEID.
     * <p>If the version of SDK is greater than 28, it will return an empty string.</p>
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the MEID
     */
    @android.support.annotation.RequiresPermission(READ_PHONE_STATE)
    public static String getMEID() {
        return getImeiOrMeid(false);
    }

    @android.annotation.SuppressLint("HardwareIds")
    @android.support.annotation.RequiresPermission(READ_PHONE_STATE)
    public static String getImeiOrMeid(boolean isImei) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        android.telephony.TelephonyManager tm = getTelephonyManager();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (isImei) {
                return getMinOne(tm.getImei(0), tm.getImei(1));
            } else {
                return getMinOne(tm.getMeid(0), tm.getMeid(1));
            }
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            String ids = getSystemPropertyByReflect(isImei ? "ril.gsm.imei" : "ril.cdma.meid");
            if (!android.text.TextUtils.isEmpty(ids)) {
                String[] idArr = ids.split(",");
                if (idArr.length == 2) {
                    return getMinOne(idArr[0], idArr[1]);
                } else {
                    return idArr[0];
                }
            }

            String id0 = tm.getDeviceId();
            String id1 = "";
            try {
                java.lang.reflect.Method method = tm.getClass().getMethod("getDeviceId", int.class);
                id1 = (String) method.invoke(tm,
                        isImei ? android.telephony.TelephonyManager.PHONE_TYPE_GSM
                                : android.telephony.TelephonyManager.PHONE_TYPE_CDMA);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (java.lang.reflect.InvocationTargetException e) {
                e.printStackTrace();
            }
            if (isImei) {
                if (id0 != null && id0.length() < 15) {
                    id0 = "";
                }
                if (id1 != null && id1.length() < 15) {
                    id1 = "";
                }
            } else {
                if (id0 != null && id0.length() == 14) {
                    id0 = "";
                }
                if (id1 != null && id1.length() == 14) {
                    id1 = "";
                }
            }
            return getMinOne(id0, id1);
        } else {
            String deviceId = tm.getDeviceId();
            if (isImei) {
                if (deviceId != null && deviceId.length() >= 15) {
                    return deviceId;
                }
            } else {
                if (deviceId != null && deviceId.length() == 14) {
                    return deviceId;
                }
            }
        }
        return "";
    }

    private static String getMinOne(String s0, String s1) {
        boolean empty0 = android.text.TextUtils.isEmpty(s0);
        boolean empty1 = android.text.TextUtils.isEmpty(s1);
        if (empty0 && empty1) return "";
        if (!empty0 && !empty1) {
            if (s0.compareTo(s1) <= 0) {
                return s0;
            } else {
                return s1;
            }
        }
        if (!empty0) return s0;
        return s1;
    }

    private static String getSystemPropertyByReflect(String key) {
        try {
            @android.annotation.SuppressLint("PrivateApi")
            Class<?> clz = Class.forName("android.os.SystemProperties");
            java.lang.reflect.Method getMethod = clz.getMethod("get", String.class, String.class);
            return (String) getMethod.invoke(clz, key, "");
        } catch (Exception e) {/**/}
        return "";
    }

    /**
     * Return the IMSI.
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the IMSI
     */
    @android.annotation.SuppressLint("HardwareIds")
    @android.support.annotation.RequiresPermission(READ_PHONE_STATE)
    public static String getIMSI() {
        return getTelephonyManager().getSubscriberId();
    }

    /**
     * Returns the current phone type.
     *
     * @return the current phone type
     * <ul>
     * <li>{@link TelephonyManager#PHONE_TYPE_NONE}</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_GSM }</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_CDMA}</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_SIP }</li>
     * </ul>
     */
    public static int getPhoneType() {
        android.telephony.TelephonyManager tm = getTelephonyManager();
        return tm.getPhoneType();
    }

    /**
     * Return whether sim card state is ready.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isSimCardReady() {
        android.telephony.TelephonyManager tm = getTelephonyManager();
        return tm.getSimState() == android.telephony.TelephonyManager.SIM_STATE_READY;
    }

    /**
     * Return the sim operator name.
     *
     * @return the sim operator name
     */
    public static String getSimOperatorName() {
        android.telephony.TelephonyManager tm = getTelephonyManager();
        return tm.getSimOperatorName();
    }

    /**
     * Return the sim operator using mnc.
     *
     * @return the sim operator
     */
    public static String getSimOperatorByMnc() {
        android.telephony.TelephonyManager tm = getTelephonyManager();
        String operator = tm.getSimOperator();
        if (operator == null) return "";
        switch (operator) {
            case "46000":
            case "46002":
            case "46007":
            case "46020":
                return "中国移动";
            case "46001":
            case "46006":
            case "46009":
                return "中国联通";
            case "46003":
            case "46005":
            case "46011":
                return "中国电信";
            default:
                return operator;
        }
    }

    /**
     * Skip to dial.
     *
     * @param phoneNumber The phone number.
     * @return {@code true}: operate successfully<br>{@code false}: otherwise
     */
    public static boolean dial(final String phoneNumber) {
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse("tel:" + phoneNumber));
        if (isIntentAvailable(intent)) {
            Utils.getApp().startActivity(intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        }
        return false;
    }

    /**
     * Make a phone call.
     * <p>Must hold {@code <uses-permission android:name="android.permission.CALL_PHONE" />}</p>
     *
     * @param phoneNumber The phone number.
     * @return {@code true}: operate successfully<br>{@code false}: otherwise
     */
    @android.support.annotation.RequiresPermission(CALL_PHONE)
    public static boolean call(final String phoneNumber) {
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_CALL, android.net.Uri.parse("tel:" + phoneNumber));
        if (isIntentAvailable(intent)) {
            Utils.getApp().startActivity(intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        }
        return false;
    }

    /**
     * Send sms.
     *
     * @param phoneNumber The phone number.
     * @param content     The content.
     * @return {@code true}: operate successfully<br>{@code false}: otherwise
     */
    public static boolean sendSms(final String phoneNumber, final String content) {
        android.net.Uri uri = android.net.Uri.parse("smsto:" + phoneNumber);
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_SENDTO, uri);
        if (isIntentAvailable(intent)) {
            intent.putExtra("sms_body", content);
            Utils.getApp().startActivity(intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        }
        return false;
    }

    private static android.telephony.TelephonyManager getTelephonyManager() {
        return (android.telephony.TelephonyManager) Utils.getApp().getSystemService(android.content.Context.TELEPHONY_SERVICE);
    }

    private static boolean isIntentAvailable(final android.content.Intent intent) {
        return Utils.getApp()
                .getPackageManager()
                .queryIntentActivities(intent, android.content.pm.PackageManager.MATCH_DEFAULT_ONLY)
                .size() > 0;
    }
}
