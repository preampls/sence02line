package ls.example.t.myapplication.base;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ls.example.t.myapplication.R;

public class BaseActivity extends AppCompatActivity {

    private Activity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mContext=this;
    }

    public void jumpActivity(Class<?> className){
        Intent intent=new Intent(mContext,className);
        startActivity(intent);
    }
}
