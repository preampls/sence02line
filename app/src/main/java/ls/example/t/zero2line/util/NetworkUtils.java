package ls.example.t.zero2line.util;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.content.Context.WIFI_SERVICE;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/02
 *     desc  : utils about network
 * </pre>
 */
public final class NetworkUtils {

    private NetworkUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public enum NetworkType {
        NETWORK_ETHERNET,
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    /**
     * Open the settings of wireless.
     */
    public static void openWirelessSettings() {
        Utils.getApp().startActivity(
                new android.content.Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                        .setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }

    /**
     * Return whether network is connected.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: connected<br>{@code false}: disconnected
     */
    @android.support.annotation.RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isConnected() {
        android.net.NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * Return whether network is available.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param callback The callback.
     * @return the task
     */
    @android.support.annotation.RequiresPermission(INTERNET)
    public static Utils.Task<Boolean> isAvailableAsync(@android.support.annotation.NonNull final Utils.Callback<Boolean> callback) {
        return Utils.doAsync(new Utils.Task<Boolean>(callback) {
            @android.support.annotation.RequiresPermission(INTERNET)
            @Override
            public Boolean doInBackground() {
                return isAvailable();
            }
        });
    }

    /**
     * Return whether network is available.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @android.support.annotation.RequiresPermission(INTERNET)
    public static boolean isAvailable() {
        return isAvailableByDns() || isAvailableByPing(null);
    }

    /**
     * Return whether network is available using ping.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     * <p>The default ping ip: 223.5.5.5</p>
     *
     * @param callback The callback.
     */
    @android.support.annotation.RequiresPermission(INTERNET)
    public static void isAvailableByPingAsync(final Utils.Callback<Boolean> callback) {
        isAvailableByPingAsync("", callback);
    }

    /**
     * Return whether network is available using ping.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param ip       The ip address.
     * @param callback The callback.
     * @return the task
     */
    @android.support.annotation.RequiresPermission(INTERNET)
    public static Utils.Task<Boolean> isAvailableByPingAsync(final String ip,
                                                             @android.support.annotation.NonNull final Utils.Callback<Boolean> callback) {
        return Utils.doAsync(new Utils.Task<Boolean>(callback) {
            @android.support.annotation.RequiresPermission(INTERNET)
            @Override
            public Boolean doInBackground() {
                return isAvailableByPing(ip);
            }
        });
    }

    /**
     * Return whether network is available using ping.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     * <p>The default ping ip: 223.5.5.5</p>
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @android.support.annotation.RequiresPermission(INTERNET)
    public static boolean isAvailableByPing() {
        return isAvailableByPing("");
    }

    /**
     * Return whether network is available using ping.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param ip The ip address.
     * @return {@code true}: yes<br>{@code false}: no
     */
    @android.support.annotation.RequiresPermission(INTERNET)
    public static boolean isAvailableByPing(final String ip) {
        final String realIp = android.text.TextUtils.isEmpty(ip) ? "223.5.5.5" : ip;
        ShellUtils.CommandResult result = ShellUtils.execCmd(String.format("ping -c 1 %s", realIp), false);
        return result.result == 0;
    }

    /**
     * Return whether network is available using domain.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param callback The callback.
     */
    @android.support.annotation.RequiresPermission(INTERNET)
    public static void isAvailableByDnsAsync(final Utils.Callback<Boolean> callback) {
        isAvailableByDnsAsync("", callback);
    }

    /**
     * Return whether network is available using domain.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param domain   The name of domain.
     * @param callback The callback.
     * @return the task
     */
    @android.support.annotation.RequiresPermission(INTERNET)
    public static Utils.Task isAvailableByDnsAsync(final String domain,
                                                   @android.support.annotation.NonNull final Utils.Callback<Boolean> callback) {
        return Utils.doAsync(new Utils.Task<Boolean>(callback) {
            @android.support.annotation.RequiresPermission(INTERNET)
            @Override
            public Boolean doInBackground() {
                return isAvailableByDns(domain);
            }
        });
    }

    /**
     * Return whether network is available using domain.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @android.support.annotation.RequiresPermission(INTERNET)
    public static boolean isAvailableByDns() {
        return isAvailableByDns("");
    }

    /**
     * Return whether network is available using domain.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param domain The name of domain.
     * @return {@code true}: yes<br>{@code false}: no
     */
    @android.support.annotation.RequiresPermission(INTERNET)
    public static boolean isAvailableByDns(final String domain) {
        final String realDomain = android.text.TextUtils.isEmpty(domain) ? "www.baidu.com" : domain;
        java.net.InetAddress inetAddress;
        try {
            inetAddress = java.net.InetAddress.getByName(realDomain);
            return inetAddress != null;
        } catch (java.net.UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Return whether mobile data is enabled.
     *
     * @return {@code true}: enabled<br>{@code false}: disabled
     */
    public static boolean getMobileDataEnabled() {
        try {
            android.telephony.TelephonyManager tm =
                    (android.telephony.TelephonyManager) Utils.getApp().getSystemService(android.content.Context.TELEPHONY_SERVICE);
            if (tm == null) return false;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                return tm.isDataEnabled();
            }
            @android.annotation.SuppressLint("PrivateApi")
            java.lang.reflect.Method getMobileDataEnabledMethod =
                    tm.getClass().getDeclaredMethod("getDataEnabled");
            if (null != getMobileDataEnabledMethod) {
                return (boolean) getMobileDataEnabledMethod.invoke(tm);
            }
        } catch (Exception e) {
            android.util.Log.e("NetworkUtils", "getMobileDataEnabled: ", e);
        }
        return false;
    }

    /**
     * Return whether using mobile data.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @android.support.annotation.RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isMobileData() {
        android.net.NetworkInfo info = getActiveNetworkInfo();
        return null != info
                && info.isAvailable()
                && info.getType() == android.net.ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * Return whether using 4G.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @android.support.annotation.RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean is4G() {
        android.net.NetworkInfo info = getActiveNetworkInfo();
        return info != null
                && info.isAvailable()
                && info.getSubtype() == android.telephony.TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * Return whether wifi is enabled.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />}</p>
     *
     * @return {@code true}: enabled<br>{@code false}: disabled
     */
    @android.support.annotation.RequiresPermission(ACCESS_WIFI_STATE)
    public static boolean getWifiEnabled() {
        @android.annotation.SuppressLint("WifiManagerLeak")
        android.net.wifi.WifiManager manager = (android.net.wifi.WifiManager) Utils.getApp().getSystemService(WIFI_SERVICE);
        if (manager == null) return false;
        return manager.isWifiEnabled();
    }

    /**
     * Enable or disable wifi.
     * <p>Must hold {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />}</p>
     *
     * @param enabled True to enabled, false otherwise.
     */
    @android.support.annotation.RequiresPermission(CHANGE_WIFI_STATE)
    public static void setWifiEnabled(final boolean enabled) {
        @android.annotation.SuppressLint("WifiManagerLeak")
        android.net.wifi.WifiManager manager = (android.net.wifi.WifiManager) Utils.getApp().getSystemService(WIFI_SERVICE);
        if (manager == null) return;
        if (enabled == manager.isWifiEnabled()) return;
        manager.setWifiEnabled(enabled);
    }

    /**
     * Return whether wifi is connected.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: connected<br>{@code false}: disconnected
     */
    @android.support.annotation.RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isWifiConnected() {
        android.net.ConnectivityManager cm =
                (android.net.ConnectivityManager) Utils.getApp().getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        android.net.NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.getType() == android.net.ConnectivityManager.TYPE_WIFI;
    }

    /**
     * Return whether wifi is available.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
     * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @return {@code true}: available<br>{@code false}: unavailable
     */
    @android.support.annotation.RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
    public static boolean isWifiAvailable() {
        return getWifiEnabled() && isAvailable();
    }

    /**
     * Return whether wifi is available.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
     * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param callback The callback.
     * @return the task
     */
    @android.support.annotation.RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
    public static Utils.Task<Boolean> isWifiAvailableAsync(@android.support.annotation.NonNull final Utils.Callback<Boolean> callback) {
        return Utils.doAsync(new Utils.Task<Boolean>(callback) {
            @android.support.annotation.RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
            @Override
            public Boolean doInBackground() {
                return isWifiAvailable();
            }
        });
    }

    /**
     * Return the name of network operate.
     *
     * @return the name of network operate
     */
    public static String getNetworkOperatorName() {
        android.telephony.TelephonyManager tm =
                (android.telephony.TelephonyManager) Utils.getApp().getSystemService(android.content.Context.TELEPHONY_SERVICE);
        if (tm == null) return "";
        return tm.getNetworkOperatorName();
    }

    /**
     * Return type of network.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return type of network
     * <ul>
     * <li>{@link NetworkType#NETWORK_ETHERNET} </li>
     * <li>{@link NetworkType#NETWORK_WIFI    } </li>
     * <li>{@link NetworkType#NETWORK_4G      } </li>
     * <li>{@link NetworkType#NETWORK_3G      } </li>
     * <li>{@link NetworkType#NETWORK_2G      } </li>
     * <li>{@link NetworkType#NETWORK_UNKNOWN } </li>
     * <li>{@link NetworkType#NETWORK_NO      } </li>
     * </ul>
     */
    @android.support.annotation.RequiresPermission(ACCESS_NETWORK_STATE)
    public static ls.example.t.zero2line.util.NetworkUtils.NetworkType getNetworkType() {
        if (isEthernet()) {
            return ls.example.t.zero2line.util.NetworkUtils.NetworkType.NETWORK_ETHERNET;
        }
        android.net.NetworkInfo info = getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            if (info.getType() == android.net.ConnectivityManager.TYPE_WIFI) {
                return ls.example.t.zero2line.util.NetworkUtils.NetworkType.NETWORK_WIFI;
            } else if (info.getType() == android.net.ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {
                    case android.telephony.TelephonyManager.NETWORK_TYPE_GSM:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_GPRS:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_CDMA:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_EDGE:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_1xRTT:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_IDEN:
                        return ls.example.t.zero2line.util.NetworkUtils.NetworkType.NETWORK_2G;

                    case android.telephony.TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_UMTS:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_HSUPA:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_HSPA:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_EHRPD:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_HSPAP:
                        return ls.example.t.zero2line.util.NetworkUtils.NetworkType.NETWORK_3G;

                    case android.telephony.TelephonyManager.NETWORK_TYPE_IWLAN:
                    case android.telephony.TelephonyManager.NETWORK_TYPE_LTE:
                        return ls.example.t.zero2line.util.NetworkUtils.NetworkType.NETWORK_4G;

                    default:
                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            return ls.example.t.zero2line.util.NetworkUtils.NetworkType.NETWORK_3G;
                        } else {
                            return ls.example.t.zero2line.util.NetworkUtils.NetworkType.NETWORK_UNKNOWN;
                        }
                }
            } else {
                return ls.example.t.zero2line.util.NetworkUtils.NetworkType.NETWORK_UNKNOWN;
            }
        }
        return ls.example.t.zero2line.util.NetworkUtils.NetworkType.NETWORK_NO;
    }

    /**
     * Return whether using ethernet.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @android.support.annotation.RequiresPermission(ACCESS_NETWORK_STATE)
    private static boolean isEthernet() {
        final android.net.ConnectivityManager cm =
                (android.net.ConnectivityManager) Utils.getApp().getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        final android.net.NetworkInfo info = cm.getNetworkInfo(android.net.ConnectivityManager.TYPE_ETHERNET);
        if (info == null) return false;
        android.net.NetworkInfo.State state = info.getState();
        if (null == state) return false;
        return state == android.net.NetworkInfo.State.CONNECTED || state == android.net.NetworkInfo.State.CONNECTING;
    }

    @android.support.annotation.RequiresPermission(ACCESS_NETWORK_STATE)
    private static android.net.NetworkInfo getActiveNetworkInfo() {
        android.net.ConnectivityManager cm =
                (android.net.ConnectivityManager) Utils.getApp().getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        if (cm == null) return null;
        return cm.getActiveNetworkInfo();
    }

    /**
     * Return the ip address.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param useIPv4  True to use ipv4, false otherwise.
     * @param callback The callback.
     * @return the task
     */
    public static Utils.Task<String> getIPAddressAsync(final boolean useIPv4,
                                                       @android.support.annotation.NonNull final Utils.Callback<String> callback) {
        return Utils.doAsync(new Utils.Task<String>(callback) {
            @android.support.annotation.RequiresPermission(INTERNET)
            @Override
            public String doInBackground() {
                return getIPAddress(useIPv4);
            }
        });
    }

    /**
     * Return the ip address.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param useIPv4 True to use ipv4, false otherwise.
     * @return the ip address
     */
    @android.support.annotation.RequiresPermission(INTERNET)
    public static String getIPAddress(final boolean useIPv4) {
        try {
            java.util.Enumeration<java.net.NetworkInterface> nis = java.net.NetworkInterface.getNetworkInterfaces();
            java.util.LinkedList<java.net.InetAddress> adds = new java.util.LinkedList<>();
            while (nis.hasMoreElements()) {
                java.net.NetworkInterface ni = nis.nextElement();
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp() || ni.isLoopback()) continue;
                java.util.Enumeration<java.net.InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    adds.addFirst(addresses.nextElement());
                }
            }
            for (java.net.InetAddress add : adds) {
                if (!add.isLoopbackAddress()) {
                    String hostAddress = add.getHostAddress();
                    boolean isIPv4 = hostAddress.indexOf(':') < 0;
                    if (useIPv4) {
                        if (isIPv4) return hostAddress;
                    } else {
                        if (!isIPv4) {
                            int index = hostAddress.indexOf('%');
                            return index < 0
                                    ? hostAddress.toUpperCase()
                                    : hostAddress.substring(0, index).toUpperCase();
                        }
                    }
                }
            }
        } catch (java.net.SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Return the ip address of broadcast.
     *
     * @return the ip address of broadcast
     */
    public static String getBroadcastIpAddress() {
        try {
            java.util.Enumeration<java.net.NetworkInterface> nis = java.net.NetworkInterface.getNetworkInterfaces();
            java.util.LinkedList<java.net.InetAddress> adds = new java.util.LinkedList<>();
            while (nis.hasMoreElements()) {
                java.net.NetworkInterface ni = nis.nextElement();
                if (!ni.isUp() || ni.isLoopback()) continue;
                java.util.List<java.net.InterfaceAddress> ias = ni.getInterfaceAddresses();
                for (int i = 0, size = ias.size(); i < size; i++) {
                    java.net.InterfaceAddress ia = ias.get(i);
                    java.net.InetAddress broadcast = ia.getBroadcast();
                    if (broadcast != null) {
                        return broadcast.getHostAddress();
                    }
                }
            }
        } catch (java.net.SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Return the domain address.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param domain   The name of domain.
     * @param callback The callback.
     * @return the task
     */
    @android.support.annotation.RequiresPermission(INTERNET)
    public static Utils.Task<String> getDomainAddressAsync(final String domain,
                                                           @android.support.annotation.NonNull final Utils.Callback<String> callback) {
        return Utils.doAsync(new Utils.Task<String>(callback) {
            @android.support.annotation.RequiresPermission(INTERNET)
            @Override
            public String doInBackground() {
                return getDomainAddress(domain);
            }
        });
    }

    /**
     * Return the domain address.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param domain The name of domain.
     * @return the domain address
     */
    @android.support.annotation.RequiresPermission(INTERNET)
    public static String getDomainAddress(final String domain) {
        java.net.InetAddress inetAddress;
        try {
            inetAddress = java.net.InetAddress.getByName(domain);
            return inetAddress.getHostAddress();
        } catch (java.net.UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Return the ip address by wifi.
     *
     * @return the ip address by wifi
     */
    @android.support.annotation.RequiresPermission(ACCESS_WIFI_STATE)
    public static String getIpAddressByWifi() {
        @android.annotation.SuppressLint("WifiManagerLeak")
        android.net.wifi.WifiManager wm = (android.net.wifi.WifiManager) Utils.getApp().getSystemService(android.content.Context.WIFI_SERVICE);
        if (wm == null) return "";
        return android.text.format.Formatter.formatIpAddress(wm.getDhcpInfo().ipAddress);
    }

    /**
     * Return the gate way by wifi.
     *
     * @return the gate way by wifi
     */
    @android.support.annotation.RequiresPermission(ACCESS_WIFI_STATE)
    public static String getGatewayByWifi() {
        @android.annotation.SuppressLint("WifiManagerLeak")
        android.net.wifi.WifiManager wm = (android.net.wifi.WifiManager) Utils.getApp().getSystemService(android.content.Context.WIFI_SERVICE);
        if (wm == null) return "";
        return android.text.format.Formatter.formatIpAddress(wm.getDhcpInfo().gateway);
    }

    /**
     * Return the net mask by wifi.
     *
     * @return the net mask by wifi
     */
    @android.support.annotation.RequiresPermission(ACCESS_WIFI_STATE)
    public static String getNetMaskByWifi() {
        @android.annotation.SuppressLint("WifiManagerLeak")
        android.net.wifi.WifiManager wm = (android.net.wifi.WifiManager) Utils.getApp().getSystemService(android.content.Context.WIFI_SERVICE);
        if (wm == null) return "";
        return android.text.format.Formatter.formatIpAddress(wm.getDhcpInfo().netmask);
    }

    /**
     * Return the server address by wifi.
     *
     * @return the server address by wifi
     */
    @android.support.annotation.RequiresPermission(ACCESS_WIFI_STATE)
    public static String getServerAddressByWifi() {
        @android.annotation.SuppressLint("WifiManagerLeak")
        android.net.wifi.WifiManager wm = (android.net.wifi.WifiManager) Utils.getApp().getSystemService(android.content.Context.WIFI_SERVICE);
        if (wm == null) return "";
        return android.text.format.Formatter.formatIpAddress(wm.getDhcpInfo().serverAddress);
    }

    /**
     * Register the status of network changed listener.
     *
     * @param listener The status of network changed listener
     */
    public static void registerNetworkStatusChangedListener(ls.example.t.zero2line.util.NetworkUtils.OnNetworkStatusChangedListener listener) {
        ls.example.t.zero2line.util.NetworkUtils.NetworkChangedReceiver.getInstance().registerListener(listener);
    }

    /**
     * unregister the status of network changed listener.
     *
     * @param listener The status of network changed listener
     */
    public static void unregisterNetworkStatusChangedListener(ls.example.t.zero2line.util.NetworkUtils.OnNetworkStatusChangedListener listener) {
        ls.example.t.zero2line.util.NetworkUtils.NetworkChangedReceiver.getInstance().unregisterListener(listener);
    }

    public static final class NetworkChangedReceiver extends android.content.BroadcastReceiver {

        private static ls.example.t.zero2line.util.NetworkUtils.NetworkChangedReceiver getInstance() {
            return ls.example.t.zero2line.util.NetworkUtils.NetworkChangedReceiver.LazyHolder.INSTANCE;
        }

        private ls.example.t.zero2line.util.NetworkUtils.NetworkType                         mType;
        private java.util.Set<ls.example.t.zero2line.util.NetworkUtils.OnNetworkStatusChangedListener> mListeners = new java.util.HashSet<>();

        void registerListener(final ls.example.t.zero2line.util.NetworkUtils.OnNetworkStatusChangedListener listener) {
            if (listener == null) return;
            Utils.runOnUiThread(new Runnable() {
                @android.annotation.SuppressLint("MissingPermission")
                @Override
                public void run() {
                    int preSize = mListeners.size();
                    mListeners.add(listener);
                    if (preSize == 0 && mListeners.size() == 1) {
                        mType = getNetworkType();
                        android.content.IntentFilter intentFilter = new android.content.IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
                        Utils.getApp().registerReceiver(ls.example.t.zero2line.util.NetworkUtils.NetworkChangedReceiver.getInstance(), intentFilter);
                    }
                }
            });
        }

        void unregisterListener(final ls.example.t.zero2line.util.NetworkUtils.OnNetworkStatusChangedListener listener) {
            if (listener == null) return;
            Utils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int preSize = mListeners.size();
                    mListeners.remove(listener);
                    if (preSize == 1 && mListeners.size() == 0) {
                        Utils.getApp().unregisterReceiver(ls.example.t.zero2line.util.NetworkUtils.NetworkChangedReceiver.getInstance());
                    }
                }
            });
        }

        @android.annotation.SuppressLint("MissingPermission")
        @Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (android.net.ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                // debouncing
                Utils.runOnUiThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ls.example.t.zero2line.util.NetworkUtils.NetworkType networkType = ls.example.t.zero2line.util.NetworkUtils.getNetworkType();
                        if (mType == networkType) return;
                        LogUtils.e(networkType);
                        mType = networkType;
                        if (networkType == ls.example.t.zero2line.util.NetworkUtils.NetworkType.NETWORK_NO) {
                            for (ls.example.t.zero2line.util.NetworkUtils.OnNetworkStatusChangedListener listener : mListeners) {
                                listener.onDisconnected();
                            }
                        } else {
                            for (ls.example.t.zero2line.util.NetworkUtils.OnNetworkStatusChangedListener listener : mListeners) {
                                listener.onConnected(networkType);
                            }
                        }
                    }
                }, 1000);
            }
        }

        private static class LazyHolder {
            private static final ls.example.t.zero2line.util.NetworkUtils.NetworkChangedReceiver INSTANCE = new ls.example.t.zero2line.util.NetworkUtils.NetworkChangedReceiver();
        }
    }

    public interface OnNetworkStatusChangedListener {
        void onDisconnected();

        void onConnected(ls.example.t.zero2line.util.NetworkUtils.NetworkType networkType);
    }
}
