package com.uestc.express.avtivity.customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uestc.express.R;
import com.uestc.express.avtivity.BaseActivity;

public class CustomerHomeActivity extends BaseActivity {

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
//                netTest();
                startActivity(new Intent(CustomerHomeActivity.this, CustomerSendActivity.class));
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
                startActivity(new Intent(CustomerHomeActivity.this, CustomerQueryActivity.class));
            }
        });

    }
    // volley sample
//    void netTest() {
//        StringRequest test = getRequestManager().demo("123", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.i("test", response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("test", error.getMessage());
//            }
//        });
//        addRequest(test);
//    }

}
