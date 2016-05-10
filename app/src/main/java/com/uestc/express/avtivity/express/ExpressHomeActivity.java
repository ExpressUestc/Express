package com.uestc.express.avtivity.express;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.uestc.express.R;
import com.uestc.express.avtivity.BaseActivity;

public class ExpressHomeActivity extends BaseActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ExpressHomeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_home);

        initView();

    }

    private void initView() {

        LinearLayout print = (LinearLayout) findViewById(R.id.print);
        LinearLayout track = (LinearLayout) findViewById(R.id.track);
        LinearLayout send_message = (LinearLayout) findViewById(R.id.send_message);

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackPositionActivity.startActivity(ExpressHomeActivity.this);
            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageActivity.startActivity(ExpressHomeActivity.this);
            }
        });

    }
}
