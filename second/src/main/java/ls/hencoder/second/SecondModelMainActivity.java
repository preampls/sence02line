package ls.hencoder.second;

import android.os.Bundle;

public class SecondModelMainActivity extends android.app.Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new android.content.Intent(this, ls.hencoder.second.base.SecondBaseActivity.class));
    }
}
