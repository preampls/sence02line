package ls.example.t.zero2line.common;

import  ls.t.zero2line.R;
/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/06/27
 *     desc  : base about drawer activity
 * </pre>
 */
public abstract class CommonDrawerActivity extends CommonBackActivity {

    protected android.support.v4.widget.DrawerLayout mBaseDrawerRootLayout;
    protected android.widget.FrameLayout  mBaseDrawerContainerView;

    private android.support.design.widget.NavigationView.OnNavigationItemSelectedListener mListener = new android.support.design.widget.NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@android.support.annotation.NonNull android.view.MenuItem item) {
            int id = item.getItemId();
            if (id ==R.id.baseDrawerActionGitHub) {
                return goWeb(ls.t.zero2line.R.string.github);
            } else if (id == R.id.baseDrawerActionBlog) {
                return goWeb(R.string.blog);
            }
            return false;
        }
    };

    @Override
    public boolean isSwipeBack() {
        return false;
    }

    @android.annotation.SuppressLint("ResourceType")
    @Override
    public void setRootLayout(@android.support.annotation.LayoutRes int layoutId) {
        super.setRootLayout(R.layout.common_activity_drawer);
        mBaseDrawerRootLayout = findViewById(R.id.baseDrawerRootLayout);
        mBaseDrawerContainerView = findViewById(R.id.baseDrawerContainerView);
        if (layoutId > 0) {
            android.view.LayoutInflater.from(this).inflate(layoutId, mBaseDrawerContainerView);
        }
        android.support.design.widget.NavigationView nav = findViewById(R.id.baseDrawerNavView);
        nav.setNavigationItemSelectedListener(mListener);
    }

    private boolean goWeb(@android.support.annotation.StringRes int id) {
        return ls.example.t.zero2line.util.ActivityUtils.startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(getString(id))));
    }
}
