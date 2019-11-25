package ls.example.t.zero2line.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.blankj.utilcode.util.Utils;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/25
 *     desc  : 剪贴板相关工具类
 * </pre>
 */
public final class ClipboardUtils {

    private ClipboardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 复制文本到剪贴板
     *
     * @param text 文本
     */
    public static void copyText(final CharSequence text) {
        android.content.ClipboardManager cm = (android.content.ClipboardManager) Utils.getApp().getSystemService(android.content.Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        cm.setPrimaryClip(android.content.ClipData.newPlainText("text", text));
    }

    /**
     * 获取剪贴板的文本
     *
     * @return 剪贴板的文本
     */
    public static CharSequence getText() {
        android.content.ClipboardManager cm = (android.content.ClipboardManager) Utils.getApp().getSystemService(android.content.Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        android.content.ClipData clip = cm.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(Utils.getApp());
        }
        return null;
    }

    /**
     * 复制uri到剪贴板
     *
     * @param uri uri
     */
    public static void copyUri(final android.net.Uri uri) {
        android.content.ClipboardManager cm = (android.content.ClipboardManager) Utils.getApp().getSystemService(android.content.Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        cm.setPrimaryClip(android.content.ClipData.newUri(Utils.getApp().getContentResolver(), "uri", uri));
    }

    /**
     * 获取剪贴板的uri
     *
     * @return 剪贴板的uri
     */
    public static android.net.Uri getUri() {
        android.content.ClipboardManager cm = (android.content.ClipboardManager) Utils.getApp().getSystemService(android.content.Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        android.content.ClipData clip = cm.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getUri();
        }
        return null;
    }

    /**
     * 复制意图到剪贴板
     *
     * @param intent 意图
     */
    public static void copyIntent(final android.content.Intent intent) {
        android.content.ClipboardManager cm = (android.content.ClipboardManager) Utils.getApp().getSystemService(android.content.Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        cm.setPrimaryClip(android.content.ClipData.newIntent("intent", intent));
    }

    /**
     * 获取剪贴板的意图
     *
     * @return 剪贴板的意图
     */
    public static android.content.Intent getIntent() {
        android.content.ClipboardManager cm = (android.content.ClipboardManager) Utils.getApp().getSystemService(android.content.Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        android.content.ClipData clip = cm.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getIntent();
        }
        return null;
    }
}
