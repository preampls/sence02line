package ls.example.t.zero2line.common;

import ls.example.t.zero2line.util.*;
/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/03/14
 *     desc  : base about task activity
 * </pre>
 */
public abstract class CommonTaskActivity<T> extends CommonTitleActivity {

    public abstract T doInBackground();

    public abstract void runOnUiThread(T data);

    private android.widget.ProgressBar loadingView;

    private ls.example.t.zero2line.util.ThreadUtils.SimpleTask<T> bgTask = new ThreadUtils.SimpleTask<T>() {
        @Override
        public T doInBackground() throws Throwable {
            return ls.example.t.zero2line.common.CommonTaskActivity.this.doInBackground();
        }

        @Override
        public void onSuccess(T result) {
            runOnUiThread(result);
            setLoadingVisibility(false);
        }
    };

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLoadingVisibility(true);
        ThreadUtils.executeByIo(bgTask);
    }

    public void setLoadingVisibility(boolean isVisible) {
        if (loadingView == null) {
            loadingView = new android.widget.ProgressBar(this, null, android.R.attr.progressBarStyle);
            android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = android.view.Gravity.CENTER;
            baseTitleContentView.addView(loadingView, params);
        }
        loadingView.setVisibility(isVisible ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThreadUtils.cancel(bgTask);
    }
}
