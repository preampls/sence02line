package ls.example.t.zero2line.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/29
 *     desc  : utils about toast
 * </pre>
 */
public final class ToastUtils {

    private static final int    COLOR_DEFAULT = 0xFEFFFFFF;
    private static final String NULL          = "null";

    private static ls.example.t.zero2line.util.ToastUtils.IToast iToast;
    private static int    sGravity     = -1;
    private static int    sXOffset     = -1;
    private static int    sYOffset     = -1;
    private static int    sBgColor     = COLOR_DEFAULT;
    private static int    sBgResource  = -1;
    private static int    sMsgColor    = COLOR_DEFAULT;
    private static int    sMsgTextSize = -1;

    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Set the gravity.
     *
     * @param gravity The gravity.
     * @param xOffset X-axis offset, in pixel.
     * @param yOffset Y-axis offset, in pixel.
     */
    public static void setGravity(final int gravity, final int xOffset, final int yOffset) {
        sGravity = gravity;
        sXOffset = xOffset;
        sYOffset = yOffset;
    }

    /**
     * Set the color of background.
     *
     * @param backgroundColor The color of background.
     */
    public static void setBgColor(@android.support.annotation.ColorInt final int backgroundColor) {
        sBgColor = backgroundColor;
    }

    /**
     * Set the resource of background.
     *
     * @param bgResource The resource of background.
     */
    public static void setBgResource(@android.support.annotation.DrawableRes final int bgResource) {
        sBgResource = bgResource;
    }

    /**
     * Set the color of message.
     *
     * @param msgColor The color of message.
     */
    public static void setMsgColor(@android.support.annotation.ColorInt final int msgColor) {
        sMsgColor = msgColor;
    }

    /**
     * Set the text size of message.
     *
     * @param textSize The text size of message.
     */
    public static void setMsgTextSize(final int textSize) {
        sMsgTextSize = textSize;
    }

    /**
     * Show the toast for a short period of time.
     *
     * @param text The text.
     */
    public static void showShort(final CharSequence text) {
        show(text == null ? NULL : text, android.widget.Toast.LENGTH_SHORT);
    }

    /**
     * Show the toast for a short period of time.
     *
     * @param resId The resource id for text.
     */
    public static void showShort(@android.support.annotation.StringRes final int resId) {
        show(resId, android.widget.Toast.LENGTH_SHORT);
    }

    /**
     * Show the toast for a short period of time.
     *
     * @param resId The resource id for text.
     * @param args  The args.
     */
    public static void showShort(@android.support.annotation.StringRes final int resId, final Object... args) {
        show(resId, android.widget.Toast.LENGTH_SHORT, args);
    }

    /**
     * Show the toast for a short period of time.
     *
     * @param format The format.
     * @param args   The args.
     */
    public static void showShort(final String format, final Object... args) {
        show(format, android.widget.Toast.LENGTH_SHORT, args);
    }

    /**
     * Show the toast for a long period of time.
     *
     * @param text The text.
     */
    public static void showLong(final CharSequence text) {
        show(text == null ? NULL : text, android.widget.Toast.LENGTH_LONG);
    }

    /**
     * Show the toast for a long period of time.
     *
     * @param resId The resource id for text.
     */
    public static void showLong(@android.support.annotation.StringRes final int resId) {
        show(resId, android.widget.Toast.LENGTH_LONG);
    }

    /**
     * Show the toast for a long period of time.
     *
     * @param resId The resource id for text.
     * @param args  The args.
     */
    public static void showLong(@android.support.annotation.StringRes final int resId, final Object... args) {
        show(resId, android.widget.Toast.LENGTH_LONG, args);
    }

    /**
     * Show the toast for a long period of time.
     *
     * @param format The format.
     * @param args   The args.
     */
    public static void showLong(final String format, final Object... args) {
        show(format, android.widget.Toast.LENGTH_LONG, args);
    }

    /**
     * Show custom toast for a short period of time.
     *
     * @param layoutId ID for an XML layout resource to load.
     */
    public static android.view.View showCustomShort(@android.support.annotation.LayoutRes final int layoutId) {
        return showCustomShort(getView(layoutId));
    }

    /**
     * Show custom toast for a short period of time.
     *
     * @param view The view of toast.
     */
    public static android.view.View showCustomShort(final android.view.View view) {
        show(view, android.widget.Toast.LENGTH_SHORT);
        return view;
    }

    /**
     * Show custom toast for a long period of time.
     *
     * @param layoutId ID for an XML layout resource to load.
     */
    public static android.view.View showCustomLong(@android.support.annotation.LayoutRes final int layoutId) {
        return showCustomLong(getView(layoutId));
    }

    /**
     * Show custom toast for a long period of time.
     *
     * @param view The view of toast.
     */
    public static android.view.View showCustomLong(final android.view.View view) {
        show(view, android.widget.Toast.LENGTH_LONG);
        return view;
    }

    /**
     * Cancel the toast.
     */
    public static void cancel() {
        if (iToast != null) {
            iToast.cancel();
        }
    }

    private static void show(final int resId, final int duration) {
        try {
            CharSequence text = Utils.getApp().getResources().getText(resId);
            show(text, duration);
        } catch (Exception ignore) {
            show(String.valueOf(resId), duration);
        }
    }

    private static void show(final int resId, final int duration, final Object... args) {
        try {
            CharSequence text = Utils.getApp().getResources().getText(resId);
            String format = String.format(text.toString(), args);
            show(format, duration);
        } catch (Exception ignore) {
            show(String.valueOf(resId), duration);
        }
    }

    private static void show(final String format, final int duration, final Object... args) {
        String text;
        if (format == null) {
            text = NULL;
        } else {
            text = String.format(format, args);
            if (text == null) {
                text = NULL;
            }
        }
        show(text, duration);
    }

    private static void show(final CharSequence text, final int duration) {
        Utils.runOnUiThread(new Runnable() {
            @android.annotation.SuppressLint("ShowToast")
            @Override
            public void run() {
                cancel();
                iToast = ls.example.t.zero2line.util.ToastUtils.ToastFactory.makeToast(Utils.getApp(), text, duration);
                final android.view.View toastView = iToast.getView();
                if (toastView == null) return;
                final android.widget.TextView tvMessage = toastView.findViewById(android.R.id.message);
                if (sMsgColor != COLOR_DEFAULT) {
                    tvMessage.setTextColor(sMsgColor);
                }
                if (sMsgTextSize != -1) {
                    tvMessage.setTextSize(sMsgTextSize);
                }
                if (sGravity != -1 || sXOffset != -1 || sYOffset != -1) {
                    iToast.setGravity(sGravity, sXOffset, sYOffset);
                }
                setBg(tvMessage);
                iToast.show();
            }
        });
    }

    private static void show(final android.view.View view, final int duration) {
        Utils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cancel();
                iToast = ls.example.t.zero2line.util.ToastUtils.ToastFactory.newToast(Utils.getApp());
                iToast.setView(view);
                iToast.setDuration(duration);
                if (sGravity != -1 || sXOffset != -1 || sYOffset != -1) {
                    iToast.setGravity(sGravity, sXOffset, sYOffset);
                }
                setBg();
                iToast.show();
            }
        });
    }

    private static void setBg() {
        if (sBgResource != -1) {
            final android.view.View toastView = iToast.getView();
            toastView.setBackgroundResource(sBgResource);
        } else if (sBgColor != COLOR_DEFAULT) {
            final android.view.View toastView = iToast.getView();
            android.graphics.drawable.Drawable background = toastView.getBackground();
            if (background != null) {
                background.setColorFilter(
                        new android.graphics.PorterDuffColorFilter(sBgColor, android.graphics.PorterDuff.Mode.SRC_IN)
                );
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    toastView.setBackground(new android.graphics.drawable.ColorDrawable(sBgColor));
                } else {
                    toastView.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(sBgColor));
                }
            }
        }
    }

    private static void setBg(final android.widget.TextView tvMsg) {
        if (sBgResource != -1) {
            final android.view.View toastView = iToast.getView();
            toastView.setBackgroundResource(sBgResource);
            tvMsg.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        } else if (sBgColor != COLOR_DEFAULT) {
            final android.view.View toastView = iToast.getView();
            android.graphics.drawable.Drawable tvBg = toastView.getBackground();
            android.graphics.drawable.Drawable msgBg = tvMsg.getBackground();
            if (tvBg != null && msgBg != null) {
                tvBg.setColorFilter(new android.graphics.PorterDuffColorFilter(sBgColor, android.graphics.PorterDuff.Mode.SRC_IN));
                tvMsg.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            } else if (tvBg != null) {
                tvBg.setColorFilter(new android.graphics.PorterDuffColorFilter(sBgColor, android.graphics.PorterDuff.Mode.SRC_IN));
            } else if (msgBg != null) {
                msgBg.setColorFilter(new android.graphics.PorterDuffColorFilter(sBgColor, android.graphics.PorterDuff.Mode.SRC_IN));
            } else {
                toastView.setBackgroundColor(sBgColor);
            }
        }
    }

    private static android.view.View getView(@android.support.annotation.LayoutRes final int layoutId) {
        android.view.LayoutInflater inflate =
                (android.view.LayoutInflater) Utils.getApp().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        //noinspection ConstantConditions
        return inflate.inflate(layoutId, null);
    }

    static class ToastFactory {

        static ls.example.t.zero2line.util.ToastUtils.IToast makeToast(android.content.Context context, CharSequence text, int duration) {
            if (android.support.v4.app.NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                return new ls.example.t.zero2line.util.ToastUtils.SystemToast(makeNormalToast(context, text, duration));
            }
            return new ls.example.t.zero2line.util.ToastUtils.ToastWithoutNotification(makeNormalToast(context, text, duration));
        }

        static ls.example.t.zero2line.util.ToastUtils.IToast newToast(android.content.Context context) {
            if (android.support.v4.app.NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                return new ls.example.t.zero2line.util.ToastUtils.SystemToast(new android.widget.Toast(context));
            }
            return new ls.example.t.zero2line.util.ToastUtils.ToastWithoutNotification(new android.widget.Toast(context));
        }

        private static android.widget.Toast makeNormalToast(android.content.Context context, CharSequence text, int duration) {
            @android.annotation.SuppressLint("ShowToast")
            android.widget.Toast toast = android.widget.Toast.makeText(context, "", duration);
            toast.setText(text);
            return toast;
        }
    }

    static class SystemToast extends ls.example.t.zero2line.util.ToastUtils.AbsToast {

        SystemToast(android.widget.Toast toast) {
            super(toast);
            if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.N_MR1) {
                try {
                    //noinspection JavaReflectionMemberAccess
                    java.lang.reflect.Field mTNField = android.widget.Toast.class.getDeclaredField("mTN");
                    mTNField.setAccessible(true);
                    Object mTN = mTNField.get(toast);
                    java.lang.reflect.Field mTNmHandlerField = mTNField.getType().getDeclaredField("mHandler");
                    mTNmHandlerField.setAccessible(true);
                    android.os.Handler tnHandler = (android.os.Handler) mTNmHandlerField.get(mTN);
                    mTNmHandlerField.set(mTN, new ls.example.t.zero2line.util.ToastUtils.SystemToast.SafeHandler(tnHandler));
                } catch (Exception ignored) {/**/}
            }
        }

        @Override
        public void show() {
            mToast.show();
        }

        @Override
        public void cancel() {
            mToast.cancel();
        }

        static class SafeHandler extends android.os.Handler {
            private android.os.Handler impl;

            SafeHandler(android.os.Handler impl) {
                this.impl = impl;
            }

            @Override
            public void handleMessage(android.os.Message msg) {
                impl.handleMessage(msg);
            }

            @Override
            public void dispatchMessage(android.os.Message msg) {
                try {
                    impl.dispatchMessage(msg);
                } catch (Exception e) {
                    android.util.Log.e("ToastUtils", e.toString());
                }
            }
        }
    }

    static class ToastWithoutNotification extends ls.example.t.zero2line.util.ToastUtils.AbsToast {

        private android.view.View          mView;
        private android.view.WindowManager mWM;

        private android.view.WindowManager.LayoutParams mParams = new android.view.WindowManager.LayoutParams();

        private static final Utils.OnActivityDestroyedListener LISTENER =
                new Utils.OnActivityDestroyedListener() {
                    @Override
                    public void onActivityDestroyed(android.app.Activity activity) {
                        if (iToast == null) return;
                        activity.getWindow().getDecorView().setVisibility(android.view.View.GONE);
                        iToast.cancel();
                    }
                };

        ToastWithoutNotification(android.widget.Toast toast) {
            super(toast);
        }

        @Override
        public void show() {
            Utils.runOnUiThreadDelayed(new Runnable() {
                @Override
                public void run() {
                    realShow();
                }
            }, 300);
        }

        private void realShow() {
            if (mToast == null) return;
            mView = mToast.getView();
            if (mView == null) return;
            final android.content.Context context = mToast.getView().getContext();
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N_MR1) {
                mWM = (android.view.WindowManager) context.getSystemService(android.content.Context.WINDOW_SERVICE);
                mParams.type = android.view.WindowManager.LayoutParams.TYPE_TOAST;
            } else {
                android.content.Context topActivityOrApp = Utils.getTopActivityOrApp();
                if (!(topActivityOrApp instanceof android.app.Activity)) {
                    android.util.Log.e("ToastUtils", "Couldn't get top Activity.");
                    return;
                }
                android.app.Activity topActivity = (android.app.Activity) topActivityOrApp;
                if (topActivity.isFinishing() || topActivity.isDestroyed()) {
                    android.util.Log.e("ToastUtils", topActivity + " is useless");
                    return;
                }
                mWM = topActivity.getWindowManager();
                mParams.type = android.view.WindowManager.LayoutParams.LAST_APPLICATION_WINDOW;
                Utils.getActivityLifecycle().addOnActivityDestroyedListener(topActivity, LISTENER);
            }

            mParams.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.width = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.format = android.graphics.PixelFormat.TRANSLUCENT;
            mParams.windowAnimations = android.R.style.Animation_Toast;
            mParams.setTitle("ToastWithoutNotification");
            mParams.flags = android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            mParams.packageName = Utils.getApp().getPackageName();

            mParams.gravity = mToast.getGravity();
            if ((mParams.gravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK) == android.view.Gravity.FILL_HORIZONTAL) {
                mParams.horizontalWeight = 1.0f;
            }
            if ((mParams.gravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) == android.view.Gravity.FILL_VERTICAL) {
                mParams.verticalWeight = 1.0f;
            }

            mParams.x = mToast.getXOffset();
            mParams.y = mToast.getYOffset();
            mParams.horizontalMargin = mToast.getHorizontalMargin();
            mParams.verticalMargin = mToast.getVerticalMargin();

            try {
                if (mWM != null) {
                    mWM.addView(mView, mParams);
                }
            } catch (Exception ignored) {/**/}

            Utils.runOnUiThreadDelayed(new Runnable() {
                @Override
                public void run() {
                    cancel();
                }
            }, mToast.getDuration() == android.widget.Toast.LENGTH_SHORT ? 2000 : 3500);
        }

        @Override
        public void cancel() {
            try {
                if (mWM != null) {
                    mWM.removeViewImmediate(mView);
                }
            } catch (Exception ignored) {/**/}
            mView = null;
            mWM = null;
            mToast = null;
        }
    }

    static abstract class AbsToast implements ls.example.t.zero2line.util.ToastUtils.IToast {

        android.widget.Toast mToast;

        AbsToast(android.widget.Toast toast) {
            mToast = toast;
        }

        @Override
        public void setView(android.view.View view) {
            mToast.setView(view);
        }

        @Override
        public android.view.View getView() {
            return mToast.getView();
        }

        @Override
        public void setDuration(int duration) {
            mToast.setDuration(duration);
        }

        @Override
        public void setGravity(int gravity, int xOffset, int yOffset) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }

        @Override
        public void setText(int resId) {
            mToast.setText(resId);
        }

        @Override
        public void setText(CharSequence s) {
            mToast.setText(s);
        }
    }

    interface IToast {

        void show();

        void cancel();

        void setView(android.view.View view);

        android.view.View getView();

        void setDuration(int duration);

        void setGravity(int gravity, int xOffset, int yOffset);

        void setText(@android.support.annotation.StringRes int resId);

        void setText(CharSequence s);
    }
}