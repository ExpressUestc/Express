package com.uestc.express.avtivity.customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.uestc.express.R;
import com.uestc.express.avtivity.BaseActivity;

public class CustomerHomeActivity extends BaseActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, CustomerHomeActivity.class);
        activity.startActivity(intent);
    }

    LinearLayout send;
    LinearLayout receive;
    LinearLayout search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        initView();

    }

    private void initView() {
        send = (LinearLayout) findViewById(R.id.send);
        receive = (LinearLayout) findViewById(R.id.receive);
        search = (LinearLayout) findViewById(R.id.search);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHomeActivity.this, CustomerSendActivity.class));
            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerRcvQrcodeActivity.startActivity(CustomerHomeActivity.this);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHomeActivity.this, CustomerQueryActivity.class));
            }
        });

    }
}
