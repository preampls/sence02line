package ls.example.t.zero2line.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/07/10
 *     desc  : utils about messenger
 * </pre>
 */
public class MessengerUtils {

    private static java.util.concurrent.ConcurrentHashMap<String, ls.example.t.zero2line.util.MessengerUtils.MessageCallback> subscribers = new java.util.concurrent.ConcurrentHashMap<>();

    private static java.util.Map<String, ls.example.t.zero2line.util.MessengerUtils.Client> sClientMap = new java.util.HashMap<>();
    private static ls.example.t.zero2line.util.MessengerUtils.Client              sLocalClient;

    private static final int    WHAT_SUBSCRIBE   = 0x00;
    private static final int    WHAT_UNSUBSCRIBE = 0x01;
    private static final int    WHAT_SEND        = 0x02;
    private static final String KEY_STRING       = "MESSENGER_UTILS";

    public static void register() {
        if (isMainProcess()) {
            if (isServiceRunning(ls.example.t.zero2line.util.MessengerUtils.ServerService.class.getName())) {
                android.util.Log.i("MessengerUtils", "Server service is running.");
                return;
            }
            android.content.Intent intent = new android.content.Intent(Utils.getApp(), ls.example.t.zero2line.util.MessengerUtils.ServerService.class);
            Utils.getApp().startService(intent);
            return;
        }
        if (sLocalClient == null) {
            ls.example.t.zero2line.util.MessengerUtils.Client client = new ls.example.t.zero2line.util.MessengerUtils.Client(null);
            if (client.bind()) {
                sLocalClient = client;
            } else {
                android.util.Log.e("MessengerUtils", "Bind service failed.");
            }
        } else {
            android.util.Log.i("MessengerUtils", "The client have been bind.");
        }
    }

    public static void unregister() {
        if (isMainProcess()) {
            if (!isServiceRunning(ls.example.t.zero2line.util.MessengerUtils.ServerService.class.getName())) {
                android.util.Log.i("MessengerUtils", "Server service isn't running.");
                return;
            }
            android.content.Intent intent = new android.content.Intent(Utils.getApp(), ls.example.t.zero2line.util.MessengerUtils.ServerService.class);
            Utils.getApp().stopService(intent);
        }
        if (sLocalClient != null) {
            sLocalClient.unbind();
        }
    }

    public static void register(final String pkgName) {
        if (sClientMap.containsKey(pkgName)) {
            android.util.Log.i("MessengerUtils", "register: client registered: " + pkgName);
            return;
        }
        ls.example.t.zero2line.util.MessengerUtils.Client client = new ls.example.t.zero2line.util.MessengerUtils.Client(pkgName);
        if (client.bind()) {
            sClientMap.put(pkgName, client);
        } else {
            android.util.Log.e("MessengerUtils", "register: client bind failed: " + pkgName);
        }
    }

    public static void unregister(final String pkgName) {
        if (sClientMap.containsKey(pkgName)) {
            ls.example.t.zero2line.util.MessengerUtils.Client client = sClientMap.get(pkgName);
            client.unbind();
        } else {
            android.util.Log.i("MessengerUtils", "unregister: client didn't register: " + pkgName);
        }
    }

    public static void subscribe(@android.support.annotation.NonNull final String key, @android.support.annotation.NonNull final ls.example.t.zero2line.util.MessengerUtils.MessageCallback callback) {
        subscribers.put(key, callback);
    }

    public static void unsubscribe(@android.support.annotation.NonNull final String key) {
        subscribers.remove(key);
    }

    public static void post(@android.support.annotation.NonNull String key, @android.support.annotation.NonNull android.os.Bundle data) {
        data.putString(KEY_STRING, key);
        if (sLocalClient != null) {
            sLocalClient.sendMsg2Server(data);
        } else {
            android.content.Intent intent = new android.content.Intent(Utils.getApp(), ls.example.t.zero2line.util.MessengerUtils.ServerService.class);
            intent.putExtras(data);
            Utils.getApp().startService(intent);
        }
        for (ls.example.t.zero2line.util.MessengerUtils.Client client : sClientMap.values()) {
            client.sendMsg2Server(data);
        }
    }

    private static boolean isMainProcess() {
        return Utils.getApp().getPackageName().equals(Utils.getCurrentProcessName());
    }

    private static boolean isAppInstalled(@android.support.annotation.NonNull final String pkgName) {
        android.content.pm.PackageManager packageManager = Utils.getApp().getPackageManager();
        try {
            return packageManager.getApplicationInfo(pkgName, 0) != null;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isServiceRunning(final String className) {
        android.app.ActivityManager am =
                (android.app.ActivityManager) Utils.getApp().getSystemService(android.content.Context.ACTIVITY_SERVICE);
        //noinspection ConstantConditions
        java.util.List<android.app.ActivityManager.RunningServiceInfo> info = am.getRunningServices(0x7FFFFFFF);
        if (info == null || info.size() == 0) return false;
        for (android.app.ActivityManager.RunningServiceInfo aInfo : info) {
            if (className.equals(aInfo.service.getClassName())) return true;
        }
        return false;
    }

    private static boolean isAppRunning(@android.support.annotation.NonNull final String pkgName) {
        int uid;
        android.content.pm.PackageManager packageManager = Utils.getApp().getPackageManager();
        try {
            android.content.pm.ApplicationInfo ai = packageManager.getApplicationInfo(pkgName, 0);
            if (ai == null) return false;
            uid = ai.uid;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        android.app.ActivityManager am = (android.app.ActivityManager) Utils.getApp().getSystemService(android.content.Context.ACTIVITY_SERVICE);
        if (am != null) {
            java.util.List<android.app.ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(Integer.MAX_VALUE);
            if (taskInfo != null && taskInfo.size() > 0) {
                for (android.app.ActivityManager.RunningTaskInfo aInfo : taskInfo) {
                    if (pkgName.equals(aInfo.baseActivity.getPackageName())) {
                        return true;
                    }
                }
            }
            java.util.List<android.app.ActivityManager.RunningServiceInfo> serviceInfo = am.getRunningServices(Integer.MAX_VALUE);
            if (serviceInfo != null && serviceInfo.size() > 0) {
                for (android.app.ActivityManager.RunningServiceInfo aInfo : serviceInfo) {
                    if (uid == aInfo.uid) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static class Client {

        String             mPkgName;
        android.os.Messenger          mServer;
        java.util.LinkedList<android.os.Bundle> mCached = new java.util.LinkedList<>();
        @android.annotation.SuppressLint("HandlerLeak")
        android.os.Handler mReceiveServeMsgHandler = new android.os.Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                android.os.Bundle data = msg.getData();
                if (data != null) {
                    String key = data.getString(KEY_STRING);
                    if (key != null) {
                        ls.example.t.zero2line.util.MessengerUtils.MessageCallback callback = subscribers.get(key);
                        if (callback != null) {
                            callback.messageCall(data);
                        }
                    }
                }
            }
        };
        android.os.Messenger         mClient = new android.os.Messenger(mReceiveServeMsgHandler);
        android.content.ServiceConnection mConn   = new android.content.ServiceConnection() {

            @Override
            public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
                android.util.Log.d("MessengerUtils", "client service connected " + name);
                mServer = new android.os.Messenger(service);
                int key = Utils.getCurrentProcessName().hashCode();
                android.os.Message msg = android.os.Message.obtain(mReceiveServeMsgHandler, WHAT_SUBSCRIBE, key, 0);
                msg.replyTo = mClient;
                try {
                    mServer.send(msg);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e("MessengerUtils", "onServiceConnected: ", e);
                }
                sendCachedMsg2Server();
            }

            @Override
            public void onServiceDisconnected(android.content.ComponentName name) {
                android.util.Log.w("MessengerUtils", "client service disconnected:" + name);
                mServer = null;
                if (!bind()) {
                    android.util.Log.e("MessengerUtils", "client service rebind failed: " + name);
                }
            }
        };

        Client(String pkgName) {
            this.mPkgName = pkgName;
        }

        boolean bind() {
            if (android.text.TextUtils.isEmpty(mPkgName)) {
                android.content.Intent intent = new android.content.Intent(Utils.getApp(), ls.example.t.zero2line.util.MessengerUtils.ServerService.class);
                return Utils.getApp().bindService(intent, mConn, android.content.Context.BIND_AUTO_CREATE);
            }
            if (isAppInstalled(mPkgName)) {
                if (isAppRunning(mPkgName)) {
                    android.content.Intent intent = new android.content.Intent(mPkgName + ".messenger");
                    intent.setPackage(mPkgName);
                    return Utils.getApp().bindService(intent, mConn, android.content.Context.BIND_AUTO_CREATE);
                } else {
                    android.util.Log.e("MessengerUtils", "bind: the app is not running -> " + mPkgName);
                    return false;
                }
            } else {
                android.util.Log.e("MessengerUtils", "bind: the app is not installed -> " + mPkgName);
                return false;
            }
        }

        void unbind() {
            android.os.Message msg = android.os.Message.obtain(mReceiveServeMsgHandler, WHAT_UNSUBSCRIBE);
            msg.replyTo = mClient;
            try {
                mServer.send(msg);
            } catch (android.os.RemoteException e) {
                android.util.Log.e("MessengerUtils", "unbind: ", e);
            }
            try {
                Utils.getApp().unbindService(mConn);
            } catch (Exception ignore) {/*ignore*/}
        }

        void sendMsg2Server(android.os.Bundle bundle) {
            if (mServer == null) {
                mCached.addFirst(bundle);
                android.util.Log.i("MessengerUtils", "save the bundle " + bundle);
            } else {
                sendCachedMsg2Server();
                if (!send2Server(bundle)) {
                    mCached.addFirst(bundle);
                }
            }
        }

        private void sendCachedMsg2Server() {
            if (mCached.isEmpty()) return;
            for (int i = mCached.size() - 1; i >= 0; --i) {
                if (send2Server(mCached.get(i))) {
                    mCached.remove(i);
                }
            }
        }

        private boolean send2Server(android.os.Bundle bundle) {
            android.os.Message msg = android.os.Message.obtain(mReceiveServeMsgHandler, WHAT_SEND);
            msg.setData(bundle);
            msg.replyTo = mClient;
            try {
                mServer.send(msg);
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Log.e("MessengerUtils", "send2Server: ", e);
                return false;
            }
        }
    }

    public static class ServerService extends android.app.Service {

        private final java.util.concurrent.ConcurrentHashMap<Integer, android.os.Messenger> mClientMap = new java.util.concurrent.ConcurrentHashMap<>();

        @android.annotation.SuppressLint("HandlerLeak")
        private final android.os.Handler mReceiveClientMsgHandler = new android.os.Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case WHAT_SUBSCRIBE:
                        mClientMap.put(msg.arg1, msg.replyTo);
                        break;
                    case WHAT_SEND:
                        sendMsg2Client(msg);
                        consumeServerProcessCallback(msg);
                        break;
                    case WHAT_UNSUBSCRIBE:
                        mClientMap.remove(msg.arg1);
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };

        private final android.os.Messenger messenger = new android.os.Messenger(mReceiveClientMsgHandler);

        @android.support.annotation.Nullable
        @Override
        public android.os.IBinder onBind(android.content.Intent intent) {
            return messenger.getBinder();
        }

        @Override
        public int onStartCommand(android.content.Intent intent, int flags, int startId) {
            if (intent != null) {
                android.os.Bundle extras = intent.getExtras();
                if (extras != null) {
                    android.os.Message msg = android.os.Message.obtain(mReceiveClientMsgHandler, WHAT_SEND);
                    msg.replyTo = messenger;
                    msg.setData(extras);
                    sendMsg2Client(msg);
                    consumeServerProcessCallback(msg);
                }
            }
            return START_NOT_STICKY;
        }

        private void sendMsg2Client(final android.os.Message msg) {
            for (android.os.Messenger client : mClientMap.values()) {
                try {
                    if (client != null) {
                        client.send(msg);
                    }
                } catch (android.os.RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        private void consumeServerProcessCallback(final android.os.Message msg) {
            android.os.Bundle data = msg.getData();
            if (data != null) {
                String key = data.getString(KEY_STRING);
                if (key != null) {
                    ls.example.t.zero2line.util.MessengerUtils.MessageCallback callback = subscribers.get(key);
                    if (callback != null) {
                        callback.messageCall(data);
                    }
                }
            }
        }
    }

    public interface MessageCallback {
        void messageCall(android.os.Bundle data);
    }
}
