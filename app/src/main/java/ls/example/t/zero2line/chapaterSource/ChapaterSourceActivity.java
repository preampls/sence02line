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
