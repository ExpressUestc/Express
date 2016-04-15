package com.uestc.express.avtivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uestc.express.R;

public class CustomerHomeActivity extends Activity {

    static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, CustomerHomeActivity.class);
        activity.startActivity(intent);
    }

    Button send;
    Button receive;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        initView();

    }

    private void initView() {
        send = (Button) findViewById(R.id.send);
        receive = (Button) findViewById(R.id.receive);
        search = (Button) findViewById(R.id.search);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
