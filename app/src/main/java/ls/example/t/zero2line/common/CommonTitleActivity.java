package ls.example.t.zero2line.common;

import ls.example.t.zero2line.util.*;
import ls.t.zero2line.R;
/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2018/11/16
 *     desc  : base about title activity
 * </pre>
 */
public abstract class CommonTitleActivity extends CommonBackActivity {

    public abstract CharSequence bindTitle();

    protected boolean           isSupportScroll = true;
    protected android.support.design.widget.CoordinatorLayout baseTitleRootLayout;
    protected android.support.v7.widget.Toolbar           baseTitleToolbar;
    protected android.widget.FrameLayout       baseTitleContentView;
    protected android.view.ViewStub          mViewStub;

    @Override
    public boolean isSwipeBack() {
        return true;
    }

    @android.annotation.SuppressLint("ResourceType")
    @Override
    public void setRootLayout(@android.support.annotation.LayoutRes int layoutId) {
        super.setRootLayout(R.layout.common_activity_title);
        baseTitleRootLayout = findViewById(R.id.baseTitleRootLayout);
        baseTitleToolbar = findViewById(R.id.baseTitleToolbar);
        if (layoutId > 0) {
            if (isSupportScroll) {
                mViewStub = findViewById(R.id.baseTitleStubScroll);
            } else {
                mViewStub = findViewById(R.id.baseTitleStubNoScroll);
            }
            mViewStub.setVisibility(android.view.View.VISIBLE);
            baseTitleContentView = findViewById(R.id.commonTitleContentView);
            android.view.LayoutInflater.from(this).inflate(layoutId, baseTitleContentView);
        }
        setTitleBar();
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.colorPrimary));
        BarUtils.addMarginTopEqualStatusBarHeight(baseTitleRootLayout);
    }

    private void setTitleBar() {
        setSupportActionBar(baseTitleToolbar);
        android.support.v7.app.ActionBar titleBar = getSupportActionBar();
        if (titleBar != null) {
            titleBar.setDisplayHomeAsUpEnabled(true);
            titleBar.setTitle(bindTitle());
        }
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
