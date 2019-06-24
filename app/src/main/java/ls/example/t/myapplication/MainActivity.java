package ls.example.t.myapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ls.example.t.myapplication.base.BaseActivity;
import ls.example.t.myapplication.chapater1.Chapater1_Activity;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        findViewById(R.id.main_activity_001).setOnClickListener(this);
        findViewById(R.id.main_activity_002).setOnClickListener(this);
        findViewById(R.id.main_activity_003).setOnClickListener(this);
        findViewById(R.id.main_activity_004).setOnClickListener(this);

        findViewById(R.id.main_activity_005).setOnClickListener(this);
        findViewById(R.id.main_activity_006).setOnClickListener(this);
        findViewById(R.id.main_activity_007).setOnClickListener(this);
        findViewById(R.id.main_activity_008).setOnClickListener(this);

        findViewById(R.id.main_activity_009).setOnClickListener(this);
        findViewById(R.id.main_activity_0010).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_activity_001:
                jumpActivity(Chapater1_Activity.class);
                break;
            case R.id.main_activity_002:
                break;
            case R.id.main_activity_003:
                break;
            case R.id.main_activity_004:
                break;
            case R.id.main_activity_005:
                break;
            case R.id.main_activity_006:
                break;
            case R.id.main_activity_007:
                break;
            case R.id.main_activity_008:
                break;
            case R.id.main_activity_009:
                break;
            case R.id.main_activity_0010:
                break;
        }
    }
}
