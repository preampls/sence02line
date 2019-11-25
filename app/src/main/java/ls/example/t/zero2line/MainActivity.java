package ls.example.t.zero2line;

import android.os.Bundle;
import android.view.View;

import ls.example.t.zero2line.base.BaseActivity;
import ls.example.t.zero2line.chapater2.Chapater2Activity;
import ls.example.t.zero2line.chapaterSource.ChapaterSource002Activity;
import ls.example.t.zero2line.chapaterSource.ChapaterSource003Activity;
import ls.example.t.zero2line.chapaterSource.ChapaterSource01Activity;
import ls.example.t.zero2line.chapaterSource.ChapaterSourceActivity;
import ls.t.zero2line.R;

import android.support.v7.widget.*;

import ls.example.t.zero2line.adapter.MainActivityGridAdapter;
import ls.example.t.zero2line.bean.MainActivityGridBean;
import com.chad.library.adapter.base.*;
import java.util.List;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import com.chad.library.adapter.base.animation.*;
import android.util.*;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private List<MainActivityGridBean> mItems = new java.util.ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        RecyclerView recycleView = findViewById(ls.t.zero2line.R.id.activity_main_recycle_view);
        recycleView.setLayoutManager(new GridLayoutManager(mActivity, 5));

        for (int i = 0; i < 28; i++) {
            MainActivityGridBean item = new MainActivityGridBean(false,"title"+i);
            item.setName("第" + i + "章");
            mItems.add(item);
        }

        MainActivityGridAdapter actitivyGridAdapter = new MainActivityGridAdapter(R.layout.adapter_main_activity_grid_item, mItems);
        recycleView.setAdapter(actitivyGridAdapter);
        actitivyGridAdapter.isFirstOnly(false);
        actitivyGridAdapter.setNotDoAnimationCount(3);
        actitivyGridAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);//默认提供5种方法（渐显、缩放、从下到上，从左到右、从右到左）
        //自定义
        actitivyGridAdapter.openLoadAnimation(new BaseAnimation() {
                    @Override
                    public Animator[] getAnimators(View view) {
                        return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0f)
                                ,ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.0f)};
                    }
        });

        actitivyGridAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position){
                    case 0:
                        jumpActivity(ls.hencoder.second.SecondModelMainActivity.class);
                        break;
                    case 1:
                        jumpActivity(Chapater2Activity.class);
                        break;
                    case 2:
                        jumpActivity(ChapaterSourceActivity.class);
                        break;
                    case 3:
                        jumpActivity(ChapaterSource01Activity.class);
                        break;
                    case 4:
                        jumpActivity(ChapaterSource002Activity.class);
                        break;
                    case 5:
                        jumpActivity(ChapaterSource003Activity.class);
                        break;
                    case 6:
                        //
                        jumpActivity(UnitTestActivity.class);
                        break;
                    case 7:
                        jumpActivity(ls.example.t.zero2line.hen_coder_activity.SelfDrawViewActivity.class);
                        break;
                }
            }
        });

        float unitTestOne = ls.example.t.zero2line.util.UnitTextClass.getInstance().UnitTestOne();
        Log.d("unitUtil:=", "  " + unitTestOne);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.main_activity_001:
//                jumpActivity(ls.hencoder.second.SecondModelMainActivity.class);
//                break;
//            case R.id.main_activity_002:
//                jumpActivity(Chapater2Activity.class);
//                break;
//            case R.id.main_activity_003:
//                jumpActivity(ChapaterSourceActivity.class);
//                break;
//            case R.id.main_activity_004:
//                jumpActivity(ChapaterSource01Activity.class);
//                break;
//            case R.id.main_activity_005:
//                jumpActivity(ChapaterSource002Activity.class);
//                break;
//            case R.id.main_activity_006:
//                jumpActivity(ChapaterSource003Activity.class);
//                break;
//            case R.id.main_activity_007:
//                //
//                jumpActivity(UnitTestActivity.class);
//                break;
//            case R.id.main_activity_008:
//                break;
//            case R.id.main_activity_009:
//                break;
//            case R.id.main_activity_0010:
//                break;
        }
    }

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onDebouncingClick(View view) {

    }
}
