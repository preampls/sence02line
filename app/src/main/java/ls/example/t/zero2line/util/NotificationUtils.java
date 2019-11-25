package ls.example.t.zero2line.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/02/20
 *     desc  : utils about notification
 * </pre>
 */
public class NotificationUtils {

    private NotificationUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void create(android.content.Context context, int id, android.content.Intent intent, int smallIcon, String contentTitle, String contentText) {
        android.app.NotificationManager manager =
                (android.app.NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        // Intent para disparar o broadcast
        android.app.PendingIntent p = android.app.PendingIntent.getActivity(Utils.getApp(), 0, intent, android.app.PendingIntent.FLAG_UPDATE_CURRENT);

        // Cria a notification
        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context)
                .setContentIntent(p)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(smallIcon)
                .setAutoCancel(true);

        // Dispara a notification
        android.app.Notification n = builder.build();
        manager.notify(id, n);
    }

    public static void createStackNotification(android.content.Context context, int id, String groupId, android.content.Intent intent, int smallIcon, String contentTitle, String contentText) {
        android.app.NotificationManager manager =
                (android.app.NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        // Intent para disparar o broadcast
        android.app.PendingIntent p = intent != null ? android.app.PendingIntent.getActivity(Utils.getApp(), 0, intent, android.app.PendingIntent.FLAG_UPDATE_CURRENT) : null;

        // Cria a notification
        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context)
                .setContentIntent(p)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(smallIcon)
                .setGroup(groupId)
                .setAutoCancel(true);

        // Dispara a notification
        android.app.Notification n = builder.build();
        manager.notify(id, n);
    }

    // Notificação simples sem abrir intent (usada para alertas, ex: no wear)
    public static void create(int smallIcon, String contentTitle, String contentText) {
        android.app.NotificationManager manager =
                (android.app.NotificationManager) Utils.getApp().getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        // Cria a notification
        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(Utils.getApp())
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(smallIcon)
                .setAutoCancel(true);

        // Dispara a notification
        android.app.Notification n = builder.build();
        manager.notify(0, n);
    }

    public static void cancel(@android.support.annotation.Nullable String tag, final int id) {
        android.support.v4.app.NotificationManagerCompat.from(Utils.getApp()).cancel(tag, id);
    }

    public static void cancel(final int id) {
        android.support.v4.app.NotificationManagerCompat.from(Utils.getApp()).cancel(id);
    }

    public static void cancelAll() {
        android.support.v4.app.NotificationManagerCompat.from(Utils.getApp()).cancelAll();
    }
}
