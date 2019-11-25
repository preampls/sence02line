package ls.example.t.zero2line.constant;

import android.Manifest;
import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/12/29
 *     desc  : constants of permission
 * </pre>
 */
@android.annotation.SuppressLint("InlinedApi")
public final class PermissionConstants {

    public static final String CALENDAR   = android.Manifest.permission_group.CALENDAR;
    public static final String CAMERA     = android.Manifest.permission_group.CAMERA;
    public static final String CONTACTS   = android.Manifest.permission_group.CONTACTS;
    public static final String LOCATION   = android.Manifest.permission_group.LOCATION;
    public static final String MICROPHONE = android.Manifest.permission_group.MICROPHONE;
    public static final String PHONE      = android.Manifest.permission_group.PHONE;
    public static final String SENSORS    = android.Manifest.permission_group.SENSORS;
    public static final String SMS        = android.Manifest.permission_group.SMS;
    public static final String STORAGE    = android.Manifest.permission_group.STORAGE;

    private static final String[] GROUP_CALENDAR      = {
            android.Manifest.permission.READ_CALENDAR, android.Manifest.permission.WRITE_CALENDAR
    };
    private static final String[] GROUP_CAMERA        = {
            android.Manifest.permission.CAMERA
    };
    private static final String[] GROUP_CONTACTS      = {
            android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS, android.Manifest.permission.GET_ACCOUNTS
    };
    private static final String[] GROUP_LOCATION      = {
            android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final String[] GROUP_MICROPHONE    = {
            android.Manifest.permission.RECORD_AUDIO
    };
    private static final String[] GROUP_PHONE         = {
            android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_PHONE_NUMBERS, android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.READ_CALL_LOG, android.Manifest.permission.WRITE_CALL_LOG, android.Manifest.permission.ADD_VOICEMAIL,
            android.Manifest.permission.USE_SIP, android.Manifest.permission.PROCESS_OUTGOING_CALLS, android.Manifest.permission.ANSWER_PHONE_CALLS
    };
    private static final String[] GROUP_PHONE_BELOW_O = {
            android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_PHONE_NUMBERS, android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.READ_CALL_LOG, android.Manifest.permission.WRITE_CALL_LOG, android.Manifest.permission.ADD_VOICEMAIL,
            android.Manifest.permission.USE_SIP, android.Manifest.permission.PROCESS_OUTGOING_CALLS
    };
    private static final String[] GROUP_SENSORS       = {
            android.Manifest.permission.BODY_SENSORS
    };
    private static final String[] GROUP_SMS           = {
            android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.READ_SMS,
            android.Manifest.permission.RECEIVE_WAP_PUSH, android.Manifest.permission.RECEIVE_MMS,
    };
    private static final String[] GROUP_STORAGE       = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @android.support.annotation.StringDef({CALENDAR, CAMERA, CONTACTS, LOCATION, MICROPHONE, PHONE, SENSORS, SMS, STORAGE,})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Permission {
    }

    public static String[] getPermissions(@ls.example.t.zero2line.constant.PermissionConstants.Permission final String permission) {
        switch (permission) {
            case CALENDAR:
                return GROUP_CALENDAR;
            case CAMERA:
                return GROUP_CAMERA;
            case CONTACTS:
                return GROUP_CONTACTS;
            case LOCATION:
                return GROUP_LOCATION;
            case MICROPHONE:
                return GROUP_MICROPHONE;
            case PHONE:
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
                    return GROUP_PHONE_BELOW_O;
                } else {
                    return GROUP_PHONE;
                }
            case SENSORS:
                return GROUP_SENSORS;
            case SMS:
                return GROUP_SMS;
            case STORAGE:
                return GROUP_STORAGE;
        }
        return new String[]{permission};
    }
}
