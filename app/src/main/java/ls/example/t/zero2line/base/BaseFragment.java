package ls.example.t.zero2line.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ls.example.t.zero2line.util.*;
/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/03/28
 *     desc  : base about v4-fragment
 * </pre>
 */
public abstract class BaseFragment extends android.support.v4.app.Fragment
        implements IBaseView {

    private static final String TAG                  = "BaseFragment";
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    private android.view.View.OnClickListener mClickListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            onDebouncingClick(v);
        }
    };

    protected android.app.Activity       mActivity;
    protected android.view.LayoutInflater mInflater;
    protected android.view.View           mContentView;

    @Override
    public void onAttach(android.content.Context context) {
        super.onAttach(context);
        mActivity = (android.app.Activity) context;
    }

    @Override
    public void onCreate(@android.support.annotation.Nullable android.os.Bundle savedInstanceState) {
        android.util.Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commitAllowingStateLoss();
        }
    }

    @android.support.annotation.Nullable
    @Override
    public android.view.View onCreateView(@android.support.annotation.NonNull android.view.LayoutInflater inflater, @android.support.annotation.Nullable android.view.ViewGroup container, @android.support.annotation.Nullable android.os.Bundle savedInstanceState) {
        android.util.Log.d(TAG, "onCreateView: ");
        mInflater = inflater;
        setRootLayout(bindLayout());
        return mContentView;
    }

    @android.annotation.SuppressLint("ResourceType")
    @Override
    public void setRootLayout(@android.support.annotation.LayoutRes int layoutId) {
        if (layoutId <= 0) return;
        mContentView = mInflater.inflate(layoutId, null);
    }

    @Override
    public void onViewCreated(@android.support.annotation.NonNull android.view.View view, @android.support.annotation.Nullable android.os.Bundle savedInstanceState) {
        android.util.Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        android.os.Bundle bundle = getArguments();
        initData(bundle);
    }

    @Override
    public void onActivityCreated(@android.support.annotation.Nullable android.os.Bundle savedInstanceState) {
        android.util.Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        initView(savedInstanceState, mContentView);
        doBusiness();
    }

    @Override
    public void onDestroyView() {
        android.util.Log.d(TAG, "onDestroyView: ");
        if (mContentView != null) {
            ((android.view.ViewGroup) mContentView.getParent()).removeView(mContentView);
        }
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@android.support.annotation.NonNull android.os.Bundle outState) {
        android.util.Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public void onDestroy() {
        android.util.Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    public void applyDebouncingClickListener(android.view.View... views) {
        ClickUtils.applyGlobalDebouncing(views, mClickListener);
    }

    public <T extends android.view.View> T findViewById(@android.support.annotation.IdRes int id) {
        if (mContentView == null) throw new NullPointerException("ContentView is null.");
        return mContentView.findViewById(id);
    }
}
