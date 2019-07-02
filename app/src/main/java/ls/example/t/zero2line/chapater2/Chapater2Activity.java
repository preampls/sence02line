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
                jumpActivity(Chapater2_1Activity.class);
                break;
        }
    }
}
