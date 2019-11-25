package ls.example.t.zero2line.chapater2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ls.example.t.zero2line.base.BaseActivity;
import ls.t.zero2line.R;


public class Chapater2Activity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapater2);

        findViewById(R.id.radio_button_test).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radio_button_test:
//                jumpActivity(Chapater2_1Activity.class);
                break;
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
