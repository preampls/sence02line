package ls.example.t.zero2line;

import ls.example.t.zero2line.base.BaseActivity;
import android.os.Bundle;
import ls.t.zero2line.R;

import  ls.example.t.zero2line.util.*;
/**
 *
 * @Author Administrator
 * @time 2019-11-08 16:19
 */

public class UnitTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_test);
        double v = Math.toRadians(1) * 10;
        double v1 = Math.random() * 10;
        double rint = Math.rint(v1);

        Double d1=100.0;
        Double d2=100.0;
        if (d1.doubleValue()==d2.doubleValue()){
           LogUtils.d("UnitTestActivity",true);
        }else {
            LogUtils.d("UnitTestActivity",false);
        }
    }


    @Override
    public void initData(@android.support.annotation.Nullable android.os.Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView(@android.support.annotation.Nullable android.os.Bundle savedInstanceState, @android.support.annotation.Nullable android.view.View contentView) {

    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onDebouncingClick(@android.support.annotation.NonNull android.view.View view) {

    }
}
