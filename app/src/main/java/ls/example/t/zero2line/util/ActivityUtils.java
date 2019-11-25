package ls.example.t.zero2line.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/23
 *     desc  : utils about activity
 * </pre>
 */
public final class ActivityUtils {

    private ActivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return the activity by view.
     *
     * @param view The view.
     * @return the activity by view.
     */
    public static android.app.Activity getActivityByView(@android.support.annotation.NonNull android.view.View view) {
        return getActivityByContext(view.getContext());
    }

    /**
     * Return the activity by context.
     *
     * @param context The context.
     * @return the activity by context.
     */
    public static android.app.Activity getActivityByContext(android.content.Context context) {
        if (context instanceof android.app.Activity) return (android.app.Activity) context;
        while (context instanceof android.content.ContextWrapper) {
            if (context instanceof android.app.Activity) {
                return (android.app.Activity) context;
            }
            context = ((android.content.ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /**
     * Return whether the activity exists.
     *
     * @param pkg The name of the package.
     * @param cls The name of the class.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isActivityExists(@android.support.annotation.NonNull final String pkg,
                                           @android.support.annotation.NonNull final String cls) {
        android.content.Intent intent = new android.content.Intent();
        intent.setClassName(pkg, cls);
        return !(Utils.getApp().getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(Utils.getApp().getPackageManager()) == null ||
                Utils.getApp().getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }

    /**
     * Start the activity.
     *
     * @param clz The activity class.
     */
    public static void startActivity(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz) {
        android.content.Context context = Utils.getTopActivityOrApp();
        startActivity(context, null, context.getPackageName(), clz.getName(), null);
    }

    /**
     * Start the activity.
     *
     * @param clz     The activity class.
     * @param options Additional options for how the Activity should be started.
     */
    public static void startActivity(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                     @android.support.annotation.Nullable final android.os.Bundle options) {
        android.content.Context context = Utils.getTopActivityOrApp();
        startActivity(context, null, context.getPackageName(), clz.getName(), options);
    }

    /**
     * Start the activity.
     *
     * @param clz       The activity class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivity(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                     @android.support.annotation.AnimRes final int enterAnim,
                                     @android.support.annotation.AnimRes final int exitAnim) {
        android.content.Context context = Utils.getTopActivityOrApp();
        startActivity(context, null, context.getPackageName(), clz.getName(),
                getOptionsBundle(context, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN && context instanceof android.app.Activity) {
            ((android.app.Activity) context).overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start the activity.
     *
     * @param activity The activity.
     * @param clz      The activity class.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz) {
        startActivity(activity, null, activity.getPackageName(), clz.getName(), null);
    }

    /**
     * Start the activity.
     *
     * @param activity The activity.
     * @param clz      The activity class.
     * @param options  Additional options for how the Activity should be started.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                     @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivity(activity, null, activity.getPackageName(), clz.getName(), options);
    }

    /**
     * Start the activity.
     *
     * @param activity       The activity.
     * @param clz            The activity class.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                     final android.view.View... sharedElements) {
        startActivity(activity, null, activity.getPackageName(), clz.getName(),
                getOptionsBundle(activity, sharedElements));
    }

    /**
     * Start the activity.
     *
     * @param activity  The activity.
     * @param clz       The activity class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                     @android.support.annotation.AnimRes final int enterAnim,
                                     @android.support.annotation.AnimRes final int exitAnim) {
        startActivity(activity, null, activity.getPackageName(), clz.getName(),
                getOptionsBundle(activity, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start the activity.
     *
     * @param extras The Bundle of extras to add to this intent.
     * @param clz    The activity class.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.os.Bundle extras,
                                     @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz) {
        android.content.Context context = Utils.getTopActivityOrApp();
        startActivity(context, extras, context.getPackageName(), clz.getName(), null);
    }

    /**
     * Start the activity.
     *
     * @param extras  The Bundle of extras to add to this intent.
     * @param clz     The activity class.
     * @param options Additional options for how the Activity should be started.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.os.Bundle extras,
                                     @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                     @android.support.annotation.Nullable final android.os.Bundle options) {
        android.content.Context context = Utils.getTopActivityOrApp();
        startActivity(context, extras, context.getPackageName(), clz.getName(), options);
    }

    /**
     * Start the activity.
     *
     * @param extras    The Bundle of extras to add to this intent.
     * @param clz       The activity class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.os.Bundle extras,
                                     @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                     @android.support.annotation.AnimRes final int enterAnim,
                                     @android.support.annotation.AnimRes final int exitAnim) {
        android.content.Context context = Utils.getTopActivityOrApp();
        startActivity(context, extras, context.getPackageName(), clz.getName(),
                getOptionsBundle(context, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN && context instanceof android.app.Activity) {
            ((android.app.Activity) context).overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start the activity.
     *
     * @param extras   The Bundle of extras to add to this intent.
     * @param activity The activity.
     * @param clz      The activity class.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.os.Bundle extras,
                                     @android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz) {
        startActivity(activity, extras, activity.getPackageName(), clz.getName(), null);
    }

    /**
     * Start the activity.
     *
     * @param extras   The Bundle of extras to add to this intent.
     * @param activity The activity.
     * @param clz      The activity class.
     * @param options  Additional options for how the Activity should be started.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.os.Bundle extras,
                                     @android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                     @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivity(activity, extras, activity.getPackageName(), clz.getName(), options);
    }

    /**
     * Start the activity.
     *
     * @param extras         The Bundle of extras to add to this intent.
     * @param activity       The activity.
     * @param clz            The activity class.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.os.Bundle extras,
                                     @android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                     final android.view.View... sharedElements) {
        startActivity(activity, extras, activity.getPackageName(), clz.getName(),
                getOptionsBundle(activity, sharedElements));
    }

    /**
     * Start the activity.
     *
     * @param extras    The Bundle of extras to add to this intent.
     * @param activity  The activity.
     * @param clz       The activity class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.os.Bundle extras,
                                     @android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                     @android.support.annotation.AnimRes final int enterAnim,
                                     @android.support.annotation.AnimRes final int exitAnim) {
        startActivity(activity, extras, activity.getPackageName(), clz.getName(),
                getOptionsBundle(activity, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start the activity.
     *
     * @param pkg The name of the package.
     * @param cls The name of the class.
     */
    public static void startActivity(@android.support.annotation.NonNull final String pkg,
                                     @android.support.annotation.NonNull final String cls) {
        startActivity(Utils.getTopActivityOrApp(), null, pkg, cls, null);
    }

    /**
     * Start the activity.
     *
     * @param pkg     The name of the package.
     * @param cls     The name of the class.
     * @param options Additional options for how the Activity should be started.
     */
    public static void startActivity(@android.support.annotation.NonNull final String pkg,
                                     @android.support.annotation.NonNull final String cls,
                                     @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivity(Utils.getTopActivityOrApp(), null, pkg, cls, options);
    }

    /**
     * Start the activity.
     *
     * @param pkg       The name of the package.
     * @param cls       The name of the class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivity(@android.support.annotation.NonNull final String pkg,
                                     @android.support.annotation.NonNull final String cls,
                                     @android.support.annotation.AnimRes final int enterAnim,
                                     @android.support.annotation.AnimRes final int exitAnim) {
        android.content.Context context = Utils.getTopActivityOrApp();
        startActivity(context, null, pkg, cls, getOptionsBundle(context, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN && context instanceof android.app.Activity) {
            ((android.app.Activity) context).overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start the activity.
     *
     * @param activity The activity.
     * @param pkg      The name of the package.
     * @param cls      The name of the class.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final String pkg,
                                     @android.support.annotation.NonNull final String cls) {
        startActivity(activity, null, pkg, cls, null);
    }

    /**
     * Start the activity.
     *
     * @param activity The activity.
     * @param pkg      The name of the package.
     * @param cls      The name of the class.
     * @param options  Additional options for how the Activity should be started.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final String pkg,
                                     @android.support.annotation.NonNull final String cls,
                                     @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivity(activity, null, pkg, cls, options);
    }

    /**
     * Start the activity.
     *
     * @param activity       The activity.
     * @param pkg            The name of the package.
     * @param cls            The name of the class.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final String pkg,
                                     @android.support.annotation.NonNull final String cls,
                                     final android.view.View... sharedElements) {
        startActivity(activity, null, pkg, cls, getOptionsBundle(activity, sharedElements));
    }

    /**
     * Start the activity.
     *
     * @param activity  The activity.
     * @param pkg       The name of the package.
     * @param cls       The name of the class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final String pkg,
                                     @android.support.annotation.NonNull final String cls,
                                     @android.support.annotation.AnimRes final int enterAnim,
                                     @android.support.annotation.AnimRes final int exitAnim) {
        startActivity(activity, null, pkg, cls, getOptionsBundle(activity, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start the activity.
     *
     * @param extras The Bundle of extras to add to this intent.
     * @param pkg    The name of the package.
     * @param cls    The name of the class.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.os.Bundle extras,
                                     @android.support.annotation.NonNull final String pkg,
                                     @android.support.annotation.NonNull final String cls) {
        startActivity(Utils.getTopActivityOrApp(), extras, pkg, cls, null);
    }

    /**
     * Start the activity.
     *
     * @param extras  The Bundle of extras to add to this intent.
     * @param pkg     The name of the package.
     * @param cls     The name of the class.
     * @param options Additional options for how the Activity should be started.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.os.Bundle extras,
                                     @android.support.annotation.NonNull final String pkg,
                                     @android.support.annotation.NonNull final String cls,
                                     @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivity(Utils.getTopActivityOrApp(), extras, pkg, cls, options);
    }

    /**
     * Start the activity.
     *
     * @param extras    The Bundle of extras to add to this intent.
     * @param pkg       The name of the package.
     * @param cls       The name of the class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.os.Bundle extras,
                                     @android.support.annotation.NonNull final String pkg,
                                     @android.support.annotation.NonNull final String cls,
                                     @android.support.annotation.AnimRes final int enterAnim,
                                     @android.support.annotation.AnimRes final int exitAnim) {
        android.content.Context context = Utils.getTopActivityOrApp();
        startActivity(context, extras, pkg, cls, getOptionsBundle(context, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN && context instanceof android.app.Activity) {
            ((android.app.Activity) context).overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start the activity.
     *
     * @param activity The activity.
     * @param extras   The Bundle of extras to add to this intent.
     * @param pkg      The name of the package.
     * @param cls      The name of the class.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.os.Bundle extras,
                                     @android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final String pkg,
                                     @android.support.annotation.NonNull final String cls) {
        startActivity(activity, extras, pkg, cls, null);
    }

    /**
     * Start the activity.
     *
     * @param extras   The Bundle of extras to add to this intent.
     * @param activity The activity.
     * @param pkg      The name of the package.
     * @param cls      The name of the class.
     * @param options  Additional options for how the Activity should be started.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.os.Bundle extras,
                                     @android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final String pkg,
                                     @android.support.annotation.NonNull final String cls,
                                     @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivity(activity, extras, pkg, cls, options);
    }

    /**
     * Start the activity.
     *
     * @param extras         The Bundle of extras to add to this intent.
     * @param activity       The activity.
     * @param pkg            The name of the package.
     * @param cls            The name of the class.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.os.Bundle extras,
                                     @android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final String pkg,
                                     @android.support.annotation.NonNull final String cls,
                                     final android.view.View... sharedElements) {
        startActivity(activity, extras, pkg, cls, getOptionsBundle(activity, sharedElements));
    }

    /**
     * Start the activity.
     *
     * @param extras    The Bundle of extras to add to this intent.
     * @param pkg       The name of the package.
     * @param cls       The name of the class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.os.Bundle extras,
                                     @android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final String pkg,
                                     @android.support.annotation.NonNull final String cls,
                                     @android.support.annotation.AnimRes final int enterAnim,
                                     @android.support.annotation.AnimRes final int exitAnim) {
        startActivity(activity, extras, pkg, cls, getOptionsBundle(activity, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start the activity.
     *
     * @param intent The description of the activity to start.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean startActivity(@android.support.annotation.NonNull final android.content.Intent intent) {
        return startActivity(intent, Utils.getTopActivityOrApp(), null);
    }

    /**
     * Start the activity.
     *
     * @param intent  The description of the activity to start.
     * @param options Additional options for how the Activity should be started.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean startActivity(@android.support.annotation.NonNull final android.content.Intent intent,
                                        @android.support.annotation.Nullable final android.os.Bundle options) {
        return startActivity(intent, Utils.getTopActivityOrApp(), options);
    }

    /**
     * Start the activity.
     *
     * @param intent    The description of the activity to start.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean startActivity(@android.support.annotation.NonNull final android.content.Intent intent,
                                        @android.support.annotation.AnimRes final int enterAnim,
                                        @android.support.annotation.AnimRes final int exitAnim) {
        android.content.Context context = Utils.getTopActivityOrApp();
        boolean isSuccess = startActivity(intent, context, getOptionsBundle(context, enterAnim, exitAnim));
        if (isSuccess) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN && context instanceof android.app.Activity) {
                ((android.app.Activity) context).overridePendingTransition(enterAnim, exitAnim);
            }
        }
        return isSuccess;
    }

    /**
     * Start the activity.
     *
     * @param activity The activity.
     * @param intent   The description of the activity to start.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final android.content.Intent intent) {
        startActivity(intent, activity, null);
    }

    /**
     * Start the activity.
     *
     * @param activity The activity.
     * @param intent   The description of the activity to start.
     * @param options  Additional options for how the Activity should be started.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final android.content.Intent intent,
                                     @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivity(intent, activity, options);
    }

    /**
     * Start the activity.
     *
     * @param activity       The activity.
     * @param intent         The description of the activity to start.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final android.content.Intent intent,
                                     final android.view.View... sharedElements) {
        startActivity(intent, activity, getOptionsBundle(activity, sharedElements));
    }

    /**
     * Start the activity.
     *
     * @param activity  The activity.
     * @param intent    The description of the activity to start.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                     @android.support.annotation.NonNull final android.content.Intent intent,
                                     @android.support.annotation.AnimRes final int enterAnim,
                                     @android.support.annotation.AnimRes final int exitAnim) {
        startActivity(intent, activity, getOptionsBundle(activity, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start the activity.
     *
     * @param activity    The activity.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode) {
        startActivityForResult(activity, null, activity.getPackageName(), clz.getName(),
                requestCode, null);
    }

    /**
     * Start the activity.
     *
     * @param activity    The activity.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param options     Additional options for how the Activity should be started.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode,
                                              @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivityForResult(activity, null, activity.getPackageName(), clz.getName(),
                requestCode, options);
    }

    /**
     * Start the activity.
     *
     * @param activity       The activity.
     * @param clz            The activity class.
     * @param requestCode    if &gt;= 0, this code will be returned in
     *                       onActivityResult() when the activity exits.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode,
                                              final android.view.View... sharedElements) {
        startActivityForResult(activity, null, activity.getPackageName(), clz.getName(),
                requestCode, getOptionsBundle(activity, sharedElements));
    }

    /**
     * Start the activity.
     *
     * @param activity    The activity.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param enterAnim   A resource ID of the animation resource to use for the
     *                    incoming activity.
     * @param exitAnim    A resource ID of the animation resource to use for the
     *                    outgoing activity.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode,
                                              @android.support.annotation.AnimRes final int enterAnim,
                                              @android.support.annotation.AnimRes final int exitAnim) {
        startActivityForResult(activity, null, activity.getPackageName(), clz.getName(),
                requestCode, getOptionsBundle(activity, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start the activity.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param activity    The activity.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode) {
        startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(),
                requestCode, null);
    }

    /**
     * Start the activity.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param activity    The activity.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param options     Additional options for how the Activity should be started.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode,
                                              @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(),
                requestCode, options);
    }

    /**
     * Start the activity.
     *
     * @param extras         The Bundle of extras to add to this intent.
     * @param activity       The activity.
     * @param clz            The activity class.
     * @param requestCode    if &gt;= 0, this code will be returned in
     *                       onActivityResult() when the activity exits.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode,
                                              final android.view.View... sharedElements) {
        startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(),
                requestCode, getOptionsBundle(activity, sharedElements));
    }

    /**
     * Start the activity.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param activity    The activity.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param enterAnim   A resource ID of the animation resource to use for the
     *                    incoming activity.
     * @param exitAnim    A resource ID of the animation resource to use for the
     *                    outgoing activity.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode,
                                              @android.support.annotation.AnimRes final int enterAnim,
                                              @android.support.annotation.AnimRes final int exitAnim) {
        startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(),
                requestCode, getOptionsBundle(activity, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start the activity for result.
     *
     * @param activity    The activity.
     * @param extras      The Bundle of extras to add to this intent.
     * @param pkg         The name of the package.
     * @param cls         The name of the class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final String pkg,
                                              @android.support.annotation.NonNull final String cls,
                                              final int requestCode) {
        startActivityForResult(activity, extras, pkg, cls, requestCode, null);
    }

    /**
     * Start the activity for result.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param activity    The activity.
     * @param pkg         The name of the package.
     * @param cls         The name of the class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param options     Additional options for how the Activity should be started.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final String pkg,
                                              @android.support.annotation.NonNull final String cls,
                                              final int requestCode,
                                              @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivityForResult(activity, extras, pkg, cls, requestCode, options);
    }

    /**
     * Start the activity for result.
     *
     * @param extras         The Bundle of extras to add to this intent.
     * @param activity       The activity.
     * @param pkg            The name of the package.
     * @param cls            The name of the class.
     * @param requestCode    if &gt;= 0, this code will be returned in
     *                       onActivityResult() when the activity exits.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final String pkg,
                                              @android.support.annotation.NonNull final String cls,
                                              final int requestCode,
                                              final android.view.View... sharedElements) {
        startActivityForResult(activity, extras, pkg, cls,
                requestCode, getOptionsBundle(activity, sharedElements));
    }

    /**
     * Start the activity for result.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param pkg         The name of the package.
     * @param cls         The name of the class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param enterAnim   A resource ID of the animation resource to use for the
     *                    incoming activity.
     * @param exitAnim    A resource ID of the animation resource to use for the
     *                    outgoing activity.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final String pkg,
                                              @android.support.annotation.NonNull final String cls,
                                              final int requestCode,
                                              @android.support.annotation.AnimRes final int enterAnim,
                                              @android.support.annotation.AnimRes final int exitAnim) {
        startActivityForResult(activity, extras, pkg, cls,
                requestCode, getOptionsBundle(activity, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start the activity for result.
     *
     * @param activity    The activity.
     * @param intent      The description of the activity to start.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final android.content.Intent intent,
                                              final int requestCode) {
        startActivityForResult(intent, activity, requestCode, null);
    }

    /**
     * Start the activity for result.
     *
     * @param activity    The activity.
     * @param intent      The description of the activity to start.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param options     Additional options for how the Activity should be started.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final android.content.Intent intent,
                                              final int requestCode,
                                              @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivityForResult(intent, activity, requestCode, options);
    }

    /**
     * Start the activity for result.
     *
     * @param activity       The activity.
     * @param intent         The description of the activity to start.
     * @param requestCode    if &gt;= 0, this code will be returned in
     *                       onActivityResult() when the activity exits.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final android.content.Intent intent,
                                              final int requestCode,
                                              final android.view.View... sharedElements) {
        startActivityForResult(intent, activity,
                requestCode, getOptionsBundle(activity, sharedElements));
    }

    /**
     * Start the activity for result.
     *
     * @param activity    The activity.
     * @param intent      The description of the activity to start.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param enterAnim   A resource ID of the animation resource to use for the
     *                    incoming activity.
     * @param exitAnim    A resource ID of the animation resource to use for the
     *                    outgoing activity.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.app.Activity activity,
                                              @android.support.annotation.NonNull final android.content.Intent intent,
                                              final int requestCode,
                                              @android.support.annotation.AnimRes final int enterAnim,
                                              @android.support.annotation.AnimRes final int exitAnim) {
        startActivityForResult(intent, activity,
                requestCode, getOptionsBundle(activity, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start the activity.
     *
     * @param fragment    The fragment.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode) {
        startActivityForResult(fragment, null, Utils.getApp().getPackageName(), clz.getName(),
                requestCode, null);
    }

    /**
     * Start the activity.
     *
     * @param fragment    The fragment.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param options     Additional options for how the Activity should be started.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode,
                                              @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivityForResult(fragment, null, Utils.getApp().getPackageName(), clz.getName(),
                requestCode, options);
    }

    /**
     * Start the activity.
     *
     * @param fragment       The fragment.
     * @param clz            The activity class.
     * @param requestCode    if &gt;= 0, this code will be returned in
     *                       onActivityResult() when the activity exits.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode,
                                              final android.view.View... sharedElements) {
        startActivityForResult(fragment, null, Utils.getApp().getPackageName(), clz.getName(),
                requestCode, getOptionsBundle(fragment, sharedElements));
    }

    /**
     * Start the activity.
     *
     * @param fragment    The fragment.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param enterAnim   A resource ID of the animation resource to use for the
     *                    incoming activity.
     * @param exitAnim    A resource ID of the animation resource to use for the
     *                    outgoing activity.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode,
                                              @android.support.annotation.AnimRes final int enterAnim,
                                              @android.support.annotation.AnimRes final int exitAnim) {
        startActivityForResult(fragment, null, Utils.getApp().getPackageName(), clz.getName(),
                requestCode, getOptionsBundle(fragment, enterAnim, exitAnim));
    }

    /**
     * Start the activity.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param fragment    The fragment.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode) {
        startActivityForResult(fragment, extras, Utils.getApp().getPackageName(), clz.getName(),
                requestCode, null);
    }

    /**
     * Start the activity.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param fragment    The fragment.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param options     Additional options for how the Activity should be started.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode,
                                              @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivityForResult(fragment, extras, Utils.getApp().getPackageName(), clz.getName(),
                requestCode, options);
    }

    /**
     * Start the activity.
     *
     * @param extras         The Bundle of extras to add to this intent.
     * @param fragment       The fragment.
     * @param clz            The activity class.
     * @param requestCode    if &gt;= 0, this code will be returned in
     *                       onActivityResult() when the activity exits.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode,
                                              final android.view.View... sharedElements) {
        startActivityForResult(fragment, extras, Utils.getApp().getPackageName(), clz.getName(),
                requestCode, getOptionsBundle(fragment, sharedElements));
    }

    /**
     * Start the activity.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param fragment    The fragment.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param enterAnim   A resource ID of the animation resource to use for the
     *                    incoming activity.
     * @param exitAnim    A resource ID of the animation resource to use for the
     *                    outgoing activity.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                              final int requestCode,
                                              @android.support.annotation.AnimRes final int enterAnim,
                                              @android.support.annotation.AnimRes final int exitAnim) {
        startActivityForResult(fragment, extras, Utils.getApp().getPackageName(), clz.getName(),
                requestCode, getOptionsBundle(fragment, enterAnim, exitAnim));
    }

    /**
     * Start the activity for result.
     *
     * @param fragment    The fragment.
     * @param extras      The Bundle of extras to add to this intent.
     * @param pkg         The name of the package.
     * @param cls         The name of the class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final String pkg,
                                              @android.support.annotation.NonNull final String cls,
                                              final int requestCode) {
        startActivityForResult(fragment, extras, pkg, cls, requestCode, null);
    }

    /**
     * Start the activity for result.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param fragment    The fragment.
     * @param pkg         The name of the package.
     * @param cls         The name of the class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param options     Additional options for how the Activity should be started.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final String pkg,
                                              @android.support.annotation.NonNull final String cls,
                                              final int requestCode,
                                              @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivityForResult(fragment, extras, pkg, cls, requestCode, options);
    }

    /**
     * Start the activity for result.
     *
     * @param extras         The Bundle of extras to add to this intent.
     * @param fragment       The fragment.
     * @param pkg            The name of the package.
     * @param cls            The name of the class.
     * @param requestCode    if &gt;= 0, this code will be returned in
     *                       onActivityResult() when the activity exits.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final String pkg,
                                              @android.support.annotation.NonNull final String cls,
                                              final int requestCode,
                                              final android.view.View... sharedElements) {
        startActivityForResult(fragment, extras, pkg, cls,
                requestCode, getOptionsBundle(fragment, sharedElements));
    }

    /**
     * Start the activity for result.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param pkg         The name of the package.
     * @param cls         The name of the class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param enterAnim   A resource ID of the animation resource to use for the
     *                    incoming activity.
     * @param exitAnim    A resource ID of the animation resource to use for the
     *                    outgoing activity.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.os.Bundle extras,
                                              @android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final String pkg,
                                              @android.support.annotation.NonNull final String cls,
                                              final int requestCode,
                                              @android.support.annotation.AnimRes final int enterAnim,
                                              @android.support.annotation.AnimRes final int exitAnim) {
        startActivityForResult(fragment, extras, pkg, cls,
                requestCode, getOptionsBundle(fragment, enterAnim, exitAnim));
    }

    /**
     * Start the activity for result.
     *
     * @param fragment    The fragment.
     * @param intent      The description of the activity to start.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final android.content.Intent intent,
                                              final int requestCode) {
        startActivityForResult(intent, fragment, requestCode, null);
    }

    /**
     * Start the activity for result.
     *
     * @param fragment    The fragment.
     * @param intent      The description of the activity to start.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param options     Additional options for how the Activity should be started.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final android.content.Intent intent,
                                              final int requestCode,
                                              @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivityForResult(intent, fragment, requestCode, options);
    }

    /**
     * Start the activity for result.
     *
     * @param fragment       The fragment.
     * @param intent         The description of the activity to start.
     * @param requestCode    if &gt;= 0, this code will be returned in
     *                       onActivityResult() when the activity exits.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final android.content.Intent intent,
                                              final int requestCode,
                                              final android.view.View... sharedElements) {
        startActivityForResult(intent, fragment,
                requestCode, getOptionsBundle(fragment, sharedElements));
    }

    /**
     * Start the activity for result.
     *
     * @param fragment    The fragment.
     * @param intent      The description of the activity to start.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param enterAnim   A resource ID of the animation resource to use for the
     *                    incoming activity.
     * @param exitAnim    A resource ID of the animation resource to use for the
     *                    outgoing activity.
     */
    public static void startActivityForResult(@android.support.annotation.NonNull final android.support.v4.app.Fragment fragment,
                                              @android.support.annotation.NonNull final android.content.Intent intent,
                                              final int requestCode,
                                              @android.support.annotation.AnimRes final int enterAnim,
                                              @android.support.annotation.AnimRes final int exitAnim) {
        startActivityForResult(intent, fragment,
                requestCode, getOptionsBundle(fragment, enterAnim, exitAnim));
    }

    /**
     * Start activities.
     *
     * @param intents The descriptions of the activities to start.
     */
    public static void startActivities(@android.support.annotation.NonNull final android.content.Intent[] intents) {
        startActivities(intents, Utils.getTopActivityOrApp(), null);
    }

    /**
     * Start activities.
     *
     * @param intents The descriptions of the activities to start.
     * @param options Additional options for how the Activity should be started.
     */
    public static void startActivities(@android.support.annotation.NonNull final android.content.Intent[] intents,
                                       @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivities(intents, Utils.getTopActivityOrApp(), options);
    }

    /**
     * Start activities.
     *
     * @param intents   The descriptions of the activities to start.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivities(@android.support.annotation.NonNull final android.content.Intent[] intents,
                                       @android.support.annotation.AnimRes final int enterAnim,
                                       @android.support.annotation.AnimRes final int exitAnim) {
        android.content.Context context = Utils.getTopActivityOrApp();
        startActivities(intents, context, getOptionsBundle(context, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN && context instanceof android.app.Activity) {
            ((android.app.Activity) context).overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start activities.
     *
     * @param activity The activity.
     * @param intents  The descriptions of the activities to start.
     */
    public static void startActivities(@android.support.annotation.NonNull final android.app.Activity activity,
                                       @android.support.annotation.NonNull final android.content.Intent[] intents) {
        startActivities(intents, activity, null);
    }

    /**
     * Start activities.
     *
     * @param activity The activity.
     * @param intents  The descriptions of the activities to start.
     * @param options  Additional options for how the Activity should be started.
     */
    public static void startActivities(@android.support.annotation.NonNull final android.app.Activity activity,
                                       @android.support.annotation.NonNull final android.content.Intent[] intents,
                                       @android.support.annotation.Nullable final android.os.Bundle options) {
        startActivities(intents, activity, options);
    }

    /**
     * Start activities.
     *
     * @param activity  The activity.
     * @param intents   The descriptions of the activities to start.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivities(@android.support.annotation.NonNull final android.app.Activity activity,
                                       @android.support.annotation.NonNull final android.content.Intent[] intents,
                                       @android.support.annotation.AnimRes final int enterAnim,
                                       @android.support.annotation.AnimRes final int exitAnim) {
        startActivities(intents, activity, getOptionsBundle(activity, enterAnim, exitAnim));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Start home activity.
     */
    public static void startHomeActivity() throws SecurityException {
        android.content.Intent homeIntent = new android.content.Intent(android.content.Intent.ACTION_MAIN);
        homeIntent.addCategory(android.content.Intent.CATEGORY_HOME);
        homeIntent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
    }

    /**
     * Start the launcher activity.
     */
    public static void startLauncherActivity() {
        startLauncherActivity(Utils.getApp().getPackageName());
    }

    /**
     * Start the launcher activity.
     *
     * @param pkg The name of the package.
     */
    public static void startLauncherActivity(@android.support.annotation.NonNull final String pkg) {
        String launcherActivity = getLauncherActivity(pkg);
        if (android.text.TextUtils.isEmpty(launcherActivity)) return;
        startActivity(pkg, launcherActivity);
    }

    /**
     * Return the list of activity.
     *
     * @return the list of activity
     */
    public static java.util.List<android.app.Activity> getActivityList() {
        return Utils.getActivityList();
    }

    /**
     * Return the name of launcher activity.
     *
     * @return the name of launcher activity
     */
    public static String getLauncherActivity() {
        return getLauncherActivity(Utils.getApp().getPackageName());
    }

    /**
     * Return the name of launcher activity.
     *
     * @param pkg The name of the package.
     * @return the name of launcher activity
     */
    public static String getLauncherActivity(@android.support.annotation.NonNull final String pkg) {
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_MAIN, null);
        intent.addCategory(android.content.Intent.CATEGORY_LAUNCHER);
        intent.setPackage(pkg);
        android.content.pm.PackageManager pm = Utils.getApp().getPackageManager();
        java.util.List<android.content.pm.ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        int size = info.size();
        if (size == 0) return "";
        for (int i = 0; i < size; i++) {
            android.content.pm.ResolveInfo ri = info.get(i);
            if (ri.activityInfo.processName.equals(pkg)) {
                return ri.activityInfo.name;
            }
        }
        return info.get(0).activityInfo.name;
    }

    /**
     * Return the list of main activities.
     *
     * @return the list of main activities
     */
    public static java.util.List<String> getMainActivities() {
        return getMainActivities(Utils.getApp().getPackageName());
    }

    /**
     * Return the list of main activities.
     *
     * @param pkg The name of the package.
     * @return the list of main activities
     */
    public static java.util.List<String> getMainActivities(@android.support.annotation.NonNull final String pkg) {
        java.util.List<String> ret = new java.util.ArrayList<>();
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_MAIN, null);
        intent.setPackage(pkg);
        android.content.pm.PackageManager pm = Utils.getApp().getPackageManager();
        java.util.List<android.content.pm.ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        int size = info.size();
        if (size == 0) return ret;
        for (int i = 0; i < size; i++) {
            android.content.pm.ResolveInfo ri = info.get(i);
            if (ri.activityInfo.processName.equals(pkg)) {
                ret.add(ri.activityInfo.name);
            }
        }
        return ret;
    }

    /**
     * Return the top activity in activity's stack.
     *
     * @return the top activity in activity's stack
     */
    public static android.app.Activity getTopActivity() {
        return Utils.getActivityLifecycle().getTopActivity();
    }

    /**
     * Return whether the activity is alive.
     *
     * @param context The context.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isActivityAlive(final android.content.Context context) {
        return isActivityAlive(getActivityByContext(context));
    }

    /**
     * Return whether the activity is alive.
     *
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isActivityAlive(final android.app.Activity activity) {
        return activity != null && !activity.isFinishing()
                && (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 || !activity.isDestroyed());
    }

    /**
     * Return whether the activity exists in activity's stack.
     *
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isActivityExistsInStack(@android.support.annotation.NonNull final android.app.Activity activity) {
        java.util.List<android.app.Activity> activities = Utils.getActivityList();
        for (android.app.Activity aActivity : activities) {
            if (aActivity.equals(activity)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return whether the activity exists in activity's stack.
     *
     * @param clz The activity class.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isActivityExistsInStack(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz) {
        java.util.List<android.app.Activity> activities = Utils.getActivityList();
        for (android.app.Activity aActivity : activities) {
            if (aActivity.getClass().equals(clz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finish the activity.
     *
     * @param activity The activity.
     */
    public static void finishActivity(@android.support.annotation.NonNull final android.app.Activity activity) {
        finishActivity(activity, false);
    }

    /**
     * Finish the activity.
     *
     * @param activity   The activity.
     * @param isLoadAnim True to use animation for the outgoing activity, false otherwise.
     */
    public static void finishActivity(@android.support.annotation.NonNull final android.app.Activity activity, final boolean isLoadAnim) {
        activity.finish();
        if (!isLoadAnim) {
            activity.overridePendingTransition(0, 0);
        }
    }

    /**
     * Finish the activity.
     *
     * @param activity  The activity.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void finishActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                      @android.support.annotation.AnimRes final int enterAnim,
                                      @android.support.annotation.AnimRes final int exitAnim) {
        activity.finish();
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * Finish the activity.
     *
     * @param clz The activity class.
     */
    public static void finishActivity(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz) {
        finishActivity(clz, false);
    }

    /**
     * Finish the activity.
     *
     * @param clz        The activity class.
     * @param isLoadAnim True to use animation for the outgoing activity, false otherwise.
     */
    public static void finishActivity(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                      final boolean isLoadAnim) {
        java.util.List<android.app.Activity> activities = Utils.getActivityList();
        for (android.app.Activity activity : activities) {
            if (activity.getClass().equals(clz)) {
                activity.finish();
                if (!isLoadAnim) {
                    activity.overridePendingTransition(0, 0);
                }
            }
        }
    }

    /**
     * Finish the activity.
     *
     * @param clz       The activity class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void finishActivity(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                      @android.support.annotation.AnimRes final int enterAnim,
                                      @android.support.annotation.AnimRes final int exitAnim) {
        java.util.List<android.app.Activity> activities = Utils.getActivityList();
        for (android.app.Activity activity : activities) {
            if (activity.getClass().equals(clz)) {
                activity.finish();
                activity.overridePendingTransition(enterAnim, exitAnim);
            }
        }
    }

    /**
     * Finish to the activity.
     *
     * @param activity      The activity.
     * @param isIncludeSelf True to include the activity, false otherwise.
     */
    public static boolean finishToActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                           final boolean isIncludeSelf) {
        return finishToActivity(activity, isIncludeSelf, false);
    }

    /**
     * Finish to the activity.
     *
     * @param activity      The activity.
     * @param isIncludeSelf True to include the activity, false otherwise.
     * @param isLoadAnim    True to use animation for the outgoing activity, false otherwise.
     */
    public static boolean finishToActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                           final boolean isIncludeSelf,
                                           final boolean isLoadAnim) {
        java.util.List<android.app.Activity> activities = Utils.getActivityList();
        for (int i = activities.size() - 1; i >= 0; --i) {
            android.app.Activity aActivity = activities.get(i);
            if (aActivity.equals(activity)) {
                if (isIncludeSelf) {
                    finishActivity(aActivity, isLoadAnim);
                }
                return true;
            }
            finishActivity(aActivity, isLoadAnim);
        }
        return false;
    }

    /**
     * Finish to the activity.
     *
     * @param activity      The activity.
     * @param isIncludeSelf True to include the activity, false otherwise.
     * @param enterAnim     A resource ID of the animation resource to use for the
     *                      incoming activity.
     * @param exitAnim      A resource ID of the animation resource to use for the
     *                      outgoing activity.
     */
    public static boolean finishToActivity(@android.support.annotation.NonNull final android.app.Activity activity,
                                           final boolean isIncludeSelf,
                                           @android.support.annotation.AnimRes final int enterAnim,
                                           @android.support.annotation.AnimRes final int exitAnim) {
        java.util.List<android.app.Activity> activities = Utils.getActivityList();
        for (int i = activities.size() - 1; i >= 0; --i) {
            android.app.Activity aActivity = activities.get(i);
            if (aActivity.equals(activity)) {
                if (isIncludeSelf) {
                    finishActivity(aActivity, enterAnim, exitAnim);
                }
                return true;
            }
            finishActivity(aActivity, enterAnim, exitAnim);
        }
        return false;
    }

    /**
     * Finish to the activity.
     *
     * @param clz           The activity class.
     * @param isIncludeSelf True to include the activity, false otherwise.
     */
    public static boolean finishToActivity(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                           final boolean isIncludeSelf) {
        return finishToActivity(clz, isIncludeSelf, false);
    }

    /**
     * Finish to the activity.
     *
     * @param clz           The activity class.
     * @param isIncludeSelf True to include the activity, false otherwise.
     * @param isLoadAnim    True to use animation for the outgoing activity, false otherwise.
     */
    public static boolean finishToActivity(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                           final boolean isIncludeSelf,
                                           final boolean isLoadAnim) {
        java.util.List<android.app.Activity> activities = Utils.getActivityList();
        for (int i = activities.size() - 1; i >= 0; --i) {
            android.app.Activity aActivity = activities.get(i);
            if (aActivity.getClass().equals(clz)) {
                if (isIncludeSelf) {
                    finishActivity(aActivity, isLoadAnim);
                }
                return true;
            }
            finishActivity(aActivity, isLoadAnim);
        }
        return false;
    }

    /**
     * Finish to the activity.
     *
     * @param clz           The activity class.
     * @param isIncludeSelf True to include the activity, false otherwise.
     * @param enterAnim     A resource ID of the animation resource to use for the
     *                      incoming activity.
     * @param exitAnim      A resource ID of the animation resource to use for the
     *                      outgoing activity.
     */
    public static boolean finishToActivity(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                           final boolean isIncludeSelf,
                                           @android.support.annotation.AnimRes final int enterAnim,
                                           @android.support.annotation.AnimRes final int exitAnim) {
        java.util.List<android.app.Activity> activities = Utils.getActivityList();
        for (int i = activities.size() - 1; i >= 0; --i) {
            android.app.Activity aActivity = activities.get(i);
            if (aActivity.getClass().equals(clz)) {
                if (isIncludeSelf) {
                    finishActivity(aActivity, enterAnim, exitAnim);
                }
                return true;
            }
            finishActivity(aActivity, enterAnim, exitAnim);
        }
        return false;
    }

    /**
     * Finish the activities whose type not equals the activity class.
     *
     * @param clz The activity class.
     */
    public static void finishOtherActivities(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz) {
        finishOtherActivities(clz, false);
    }


    /**
     * Finish the activities whose type not equals the activity class.
     *
     * @param clz        The activity class.
     * @param isLoadAnim True to use animation for the outgoing activity, false otherwise.
     */
    public static void finishOtherActivities(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                             final boolean isLoadAnim) {
        java.util.List<android.app.Activity> activities = Utils.getActivityList();
        for (int i = activities.size() - 1; i >= 0; i--) {
            android.app.Activity activity = activities.get(i);
            if (!activity.getClass().equals(clz)) {
                finishActivity(activity, isLoadAnim);
            }
        }
    }

    /**
     * Finish the activities whose type not equals the activity class.
     *
     * @param clz       The activity class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void finishOtherActivities(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz,
                                             @android.support.annotation.AnimRes final int enterAnim,
                                             @android.support.annotation.AnimRes final int exitAnim) {
        java.util.List<android.app.Activity> activities = Utils.getActivityList();
        for (int i = activities.size() - 1; i >= 0; i--) {
            android.app.Activity activity = activities.get(i);
            if (!activity.getClass().equals(clz)) {
                finishActivity(activity, enterAnim, exitAnim);
            }
        }
    }

    /**
     * Finish all of activities.
     */
    public static void finishAllActivities() {
        finishAllActivities(false);
    }

    /**
     * Finish all of activities.
     *
     * @param isLoadAnim True to use animation for the outgoing activity, false otherwise.
     */
    public static void finishAllActivities(final boolean isLoadAnim) {
        java.util.List<android.app.Activity> activityList = Utils.getActivityList();
        for (int i = activityList.size() - 1; i >= 0; --i) {// remove from top
            android.app.Activity activity = activityList.get(i);
            // sActivityList remove the index activity at onActivityDestroyed
            activity.finish();
            if (!isLoadAnim) {
                activity.overridePendingTransition(0, 0);
            }
        }
    }

    /**
     * Finish all of activities.
     *
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void finishAllActivities(@android.support.annotation.AnimRes final int enterAnim,
                                           @android.support.annotation.AnimRes final int exitAnim) {
        java.util.List<android.app.Activity> activityList = Utils.getActivityList();
        for (int i = activityList.size() - 1; i >= 0; --i) {// remove from top
            android.app.Activity activity = activityList.get(i);
            // sActivityList remove the index activity at onActivityDestroyed
            activity.finish();
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * Finish all of activities except the newest activity.
     */
    public static void finishAllActivitiesExceptNewest() {
        finishAllActivitiesExceptNewest(false);
    }

    /**
     * Finish all of activities except the newest activity.
     *
     * @param isLoadAnim True to use animation for the outgoing activity, false otherwise.
     */
    public static void finishAllActivitiesExceptNewest(final boolean isLoadAnim) {
        java.util.List<android.app.Activity> activities = Utils.getActivityList();
        for (int i = activities.size() - 2; i >= 0; i--) {
            finishActivity(activities.get(i), isLoadAnim);
        }
    }

    /**
     * Finish all of activities except the newest activity.
     *
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void finishAllActivitiesExceptNewest(@android.support.annotation.AnimRes final int enterAnim,
                                                       @android.support.annotation.AnimRes final int exitAnim) {
        java.util.List<android.app.Activity> activities = Utils.getActivityList();
        for (int i = activities.size() - 2; i >= 0; i--) {
            finishActivity(activities.get(i), enterAnim, exitAnim);
        }
    }

    /**
     * Return the icon of activity.
     *
     * @param activity The activity.
     * @return the icon of activity
     */
    public static android.graphics.drawable.Drawable getActivityIcon(@android.support.annotation.NonNull final android.app.Activity activity) {
        return getActivityIcon(activity.getComponentName());
    }

    /**
     * Return the icon of activity.
     *
     * @param clz The activity class.
     * @return the icon of activity
     */
    public static android.graphics.drawable.Drawable getActivityIcon(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz) {
        return getActivityIcon(new android.content.ComponentName(Utils.getApp(), clz));
    }

    /**
     * Return the icon of activity.
     *
     * @param activityName The name of activity.
     * @return the icon of activity
     */
    public static android.graphics.drawable.Drawable getActivityIcon(@android.support.annotation.NonNull final android.content.ComponentName activityName) {
        android.content.pm.PackageManager pm = Utils.getApp().getPackageManager();
        try {
            return pm.getActivityIcon(activityName);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Return the logo of activity.
     *
     * @param activity The activity.
     * @return the logo of activity
     */
    public static android.graphics.drawable.Drawable getActivityLogo(@android.support.annotation.NonNull final android.app.Activity activity) {
        return getActivityLogo(activity.getComponentName());
    }

    /**
     * Return the logo of activity.
     *
     * @param clz The activity class.
     * @return the logo of activity
     */
    public static android.graphics.drawable.Drawable getActivityLogo(@android.support.annotation.NonNull final Class<? extends android.app.Activity> clz) {
        return getActivityLogo(new android.content.ComponentName(Utils.getApp(), clz));
    }

    /**
     * Return the logo of activity.
     *
     * @param activityName The name of activity.
     * @return the logo of activity
     */
    public static android.graphics.drawable.Drawable getActivityLogo(@android.support.annotation.NonNull final android.content.ComponentName activityName) {
        android.content.pm.PackageManager pm = Utils.getApp().getPackageManager();
        try {
            return pm.getActivityLogo(activityName);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void startActivity(final android.content.Context context,
                                      final android.os.Bundle extras,
                                      final String pkg,
                                      final String cls,
                                      @android.support.annotation.Nullable final android.os.Bundle options) {
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
        if (extras != null) intent.putExtras(extras);
        intent.setComponent(new android.content.ComponentName(pkg, cls));
        startActivity(intent, context, options);
    }

    private static boolean startActivity(final android.content.Intent intent,
                                         final android.content.Context context,
                                         final android.os.Bundle options) {
        if (!isIntentAvailable(intent)) {
            android.util.Log.e("ActivityUtils", "intent is unavailable");
            return false;
        }
        if (!(context instanceof android.app.Activity)) {
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (options != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivity(intent, options);
        } else {
            context.startActivity(intent);
        }
        return true;
    }

    private static boolean isIntentAvailable(final android.content.Intent intent) {
        return Utils.getApp()
                .getPackageManager()
                .queryIntentActivities(intent, android.content.pm.PackageManager.MATCH_DEFAULT_ONLY)
                .size() > 0;
    }

    private static boolean startActivityForResult(final android.app.Activity activity,
                                                  final android.os.Bundle extras,
                                                  final String pkg,
                                                  final String cls,
                                                  final int requestCode,
                                                  @android.support.annotation.Nullable final android.os.Bundle options) {
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
        if (extras != null) intent.putExtras(extras);
        intent.setComponent(new android.content.ComponentName(pkg, cls));
        return startActivityForResult(intent, activity, requestCode, options);
    }

    private static boolean startActivityForResult(final android.content.Intent intent,
                                                  final android.app.Activity activity,
                                                  final int requestCode,
                                                  @android.support.annotation.Nullable final android.os.Bundle options) {
        if (!isIntentAvailable(intent)) {
            android.util.Log.e("ActivityUtils", "intent is unavailable");
            return false;
        }
        if (options != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            activity.startActivityForResult(intent, requestCode, options);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
        return true;
    }

    private static void startActivities(final android.content.Intent[] intents,
                                        final android.content.Context context,
                                        @android.support.annotation.Nullable final android.os.Bundle options) {
        if (!(context instanceof android.app.Activity)) {
            for (android.content.Intent intent : intents) {
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        }
        if (options != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivities(intents, options);
        } else {
            context.startActivities(intents);
        }
    }

    private static boolean startActivityForResult(final android.support.v4.app.Fragment fragment,
                                                  final android.os.Bundle extras,
                                                  final String pkg,
                                                  final String cls,
                                                  final int requestCode,
                                                  @android.support.annotation.Nullable final android.os.Bundle options) {
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
        if (extras != null) intent.putExtras(extras);
        intent.setComponent(new android.content.ComponentName(pkg, cls));
        return startActivityForResult(intent, fragment, requestCode, options);
    }

    private static boolean startActivityForResult(final android.content.Intent intent,
                                                  final android.support.v4.app.Fragment fragment,
                                                  final int requestCode,
                                                  @android.support.annotation.Nullable final android.os.Bundle options) {
        if (!isIntentAvailable(intent)) {
            android.util.Log.e("ActivityUtils", "intent is unavailable");
            return false;
        }
        if (fragment.getActivity() == null) {
            android.util.Log.e("ActivityUtils", "Fragment " + fragment + " not attached to Activity");
            return false;
        }
        if (options != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            fragment.startActivityForResult(intent, requestCode, options);
        } else {
            fragment.startActivityForResult(intent, requestCode);
        }
        return true;
    }

    private static android.os.Bundle getOptionsBundle(final android.support.v4.app.Fragment fragment,
                                                      final int enterAnim,
                                                      final int exitAnim) {
        android.app.Activity activity = fragment.getActivity();
        if (activity == null) return null;
        return android.support.v4.app.ActivityOptionsCompat.makeCustomAnimation(activity, enterAnim, exitAnim).toBundle();
    }

    private static android.os.Bundle getOptionsBundle(final android.content.Context context,
                                                      final int enterAnim,
                                                      final int exitAnim) {
        return android.support.v4.app.ActivityOptionsCompat.makeCustomAnimation(context, enterAnim, exitAnim).toBundle();
    }

    private static android.os.Bundle getOptionsBundle(final android.support.v4.app.Fragment fragment,
                                                      final android.view.View[] sharedElements) {
        android.app.Activity activity = fragment.getActivity();
        if (activity == null) return null;
        return getOptionsBundle(activity, sharedElements);
    }

    private static android.os.Bundle getOptionsBundle(final android.app.Activity activity,
                                                      final android.view.View[] sharedElements) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) return null;
        if (sharedElements == null) return null;
        int len = sharedElements.length;
        if (len <= 0) return null;
        @SuppressWarnings("unchecked")
        android.support.v4.util.Pair<android.view.View, String>[] pairs = new android.support.v4.util.Pair[len];
        for (int i = 0; i < len; i++) {
            pairs[i] = android.support.v4.util.Pair.create(sharedElements[i], sharedElements[i].getTransitionName());
        }
        return android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs).toBundle();
    }
}
