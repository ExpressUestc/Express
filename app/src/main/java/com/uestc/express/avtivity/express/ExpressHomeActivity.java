package com.uestc.express.avtivity.express;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uestc.express.R;
import com.uestc.express.avtivity.BaseActivity;

public class ExpressHomeActivity extends BaseActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ExpressHomeActivity.class);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, String deliverID, String deliverPhone) {
        Intent intent = new Intent(activity, ExpressHomeActivity.class);
        intent.putExtra("deliverID", deliverID);
        intent.putExtra("deliverPhone", deliverPhone);
        activity.startActivity(intent);
    }

    private String deliverID;
    private String deliverPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_home);

        deliverID = getIntent().getStringExtra("deliverID");
        deliverPhone = getIntent().getStringExtra("deliverPhone");

        initView();
    }

    private void initView() {
        TextView info=(TextView)findViewById(R.id.deliver_info);
        info.setText("我的信息:\n手机号 "+deliverPhone+"\nID "+deliverID);

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
                TrackPositionActivity.startActivity(ExpressHomeActivity.this, deliverID, deliverPhone);
            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageActivity.startActivity(ExpressHomeActivity.this, deliverID, deliverPhone);
            }
        });

    }
}
