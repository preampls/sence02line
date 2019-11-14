package ls.example.t.zero2line;

import ls.example.t.zero2line.base.BaseActivity;
import android.os.Bundle;
import ls.t.zero2line.R;

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
            com.blankj.utilcode.utils.LogUtils.d("UnitTestActivity",true);
        }else {
            com.blankj.utilcode.utils.LogUtils.d("UnitTestActivity",false);
        }
    }


    
}
