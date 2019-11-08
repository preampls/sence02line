package ls.example.t.zero2line.chapaterSource;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ls.example.t.zero2line.base.BaseActivity;
import ls.example.t.zero2line.view.MyPaintSource;
import ls.t.zero2line.R;

public class ChapaterSourceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyPaintSource(this));
    }
}
