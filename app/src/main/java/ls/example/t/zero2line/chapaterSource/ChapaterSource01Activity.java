package ls.example.t.zero2line.chapaterSource;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ls.example.t.zero2line.view.La_Tombola;
import ls.example.t.zero2line.view.XfermodeView;
import ls.t.zero2line.R;

public class ChapaterSource01Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new La_Tombola(this));
    }
}
