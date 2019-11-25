package ls.example.t.zero2line.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import  ls.example.t.zero2line.util.*;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/10/24
 *     desc  : base about activity
 * </pre>
 */
public abstract class BaseActivity extends android.support.v7.app.AppCompatActivity
        implements IBaseView {

    private android.view.View.OnClickListener mClickListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            onDebouncingClick(v);
        }
    };

    protected android.view.View     mContentView;
    protected android.app.Activity mActivity;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
        initData(getIntent().getExtras());
        setRootLayout(bindLayout());
        initView(savedInstanceState, mContentView);
        doBusiness();
    }

    @android.annotation.SuppressLint("ResourceType")
    @Override
    public void setRootLayout(@android.support.annotation.LayoutRes int layoutId) {
        if (layoutId <= 0) return;
        setContentView(mContentView = android.view.LayoutInflater.from(this).inflate(layoutId, null));
    }

    public void applyDebouncingClickListener(android.view.View... views) {
       ClickUtils.applyGlobalDebouncing(views, mClickListener);
        ClickUtils.applyScale(views);
    }

    public void jumpActivity(Class<?> className){
        android.content.Intent intent=new android.content.Intent(mActivity,className);
        startActivity(intent);
    }

}

