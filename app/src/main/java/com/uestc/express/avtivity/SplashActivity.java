package com.uestc.express.avtivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.uestc.express.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.startActivity(SplashActivity.this);
                finish();
            }
        }, 2000);

    }
}
