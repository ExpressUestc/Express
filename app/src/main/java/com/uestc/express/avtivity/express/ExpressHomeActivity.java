package com.uestc.express.avtivity.express;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        Button printPaper = (Button) findViewById(R.id.print_express_paper);
        Button trackPosition = (Button) findViewById(R.id.track_position);
        Button sendNotice = (Button) findViewById(R.id.send_notice);

        printPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        trackPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackPositionActivity.startActivity(ExpressHomeActivity.this);
            }
        });

        sendNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageActivity.startActivity(ExpressHomeActivity.this);
            }
        });

    }
}
