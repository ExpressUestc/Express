package com.uestc.express.avtivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.uestc.express.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.startActivity(SplashActivity.this);
                finish();
            }
        }, 2000);

    }
}
