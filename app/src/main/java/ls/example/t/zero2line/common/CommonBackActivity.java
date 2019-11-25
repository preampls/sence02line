package ls.example.t.zero2line.common;

import com.blankj.swipepanel.*;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/06/05
 *     desc  :
 * </pre>
 */
public abstract class CommonBackActivity extends ls.example.t.zero2line.base.BaseActivity {

    public abstract boolean isSwipeBack();

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(android.R.id.content).setBackgroundColor(getResources().getColor(ls.t.zero2line.R.color.mediumGray));
        initSwipeBack();
    }

    private void initSwipeBack() {
        if (isSwipeBack()) {
            final SwipePanel swipeLayout = new SwipePanel(this);
            swipeLayout.setLeftDrawable(ls.t.zero2line.R.drawable.base_back);
            swipeLayout.setLeftEdgeSize(ls.example.t.zero2line.util.SizeUtils.dp2px(100));
            swipeLayout.setLeftSwipeColor(getResources().getColor(ls.t.zero2line.R.color.colorPrimary));
            swipeLayout.wrapView(findViewById(android.R.id.content));
            swipeLayout.setOnFullSwipeListener(new SwipePanel.OnFullSwipeListener() {
                @Override
                public void onFullSwipe(int direction) {
                    swipeLayout.close(direction);
                    finish();
                }
            });
        }
    }
}
