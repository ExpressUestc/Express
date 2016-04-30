package com.uestc.express.avtivity.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.uestc.express.R;
import com.uestc.express.avtivity.BaseActivity;

import java.util.HashMap;

public class CustomerRcvMessageActivity extends BaseActivity {

    public static void startActivity(Activity activity, String pkgID, String rcvName, String rcvPhone) {
        Intent intent = new Intent(activity, CustomerRcvMessageActivity.class);
        intent.putExtra("pkgID", pkgID);
        intent.putExtra("rcvName", rcvName);
        intent.putExtra("rcvPhone", rcvPhone);
        activity.startActivity(intent);
    }

    private TextView tv_pkgID;
    private TextView tv_rcvName;
    private TextView tv_rcvPhone;
    private EditText et_verifyCode;
    private Button btn_getCode;
    private Button btn_rcv;

    private String rcvPkgID;
    private String rcvName;
    private String rcvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_rcv_message);

        rcvPkgID = getIntent().getStringExtra("pkgID");
        rcvName = getIntent().getStringExtra("rcvName");
        rcvPhone = getIntent().getStringExtra("rcvPhone");

        initView();
    }

    private void initView() {
        tv_pkgID = (TextView) findViewById(R.id.pkg_id);
        tv_rcvName = (TextView) findViewById(R.id.rcv_name);
        tv_rcvPhone = (TextView) findViewById(R.id.rcv_phone);
        et_verifyCode = (EditText) findViewById(R.id.verify_code);
        btn_getCode = (Button) findViewById(R.id.get_code);
        btn_rcv = (Button) findViewById(R.id.btn_rcv);

        tv_pkgID.setText(rcvPkgID);
        tv_rcvName.setText(rcvName);
        tv_rcvPhone.setText(rcvPhone);
        btn_getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_getCode.setEnabled(false);
                doPostVerifyMsg();
            }
        });
        btn_rcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("正在操作，请稍后...");
                doReceive();
            }
        });

        setGetCodeButton();
    }

    private void setGetCodeButton() {
        btn_getCode.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
        btn_getCode.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 60; i > 0; i--) {
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_getCode.setText(finalI + "s");
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn_getCode.setBackgroundColor(ContextCompat.getColor(CustomerRcvMessageActivity.this,
                                R.color.orange));
                        btn_getCode.setEnabled(true);
                    }
                });
            }
        }).start();
    }

    private void doPostVerifyMsg() {
        HashMap<String, String> map = new HashMap<>();
        map.put("code", rcvPkgID);
        addRequest(getRequestManager().request("", map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setGetCodeButton();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CustomerRcvMessageActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                btn_getCode.setEnabled(true);
            }
        }));
    }

    private void doReceive() {
        HashMap<String, String> map = new HashMap<>();
        map.put("code", rcvPkgID);
        map.put("verify", et_verifyCode.getText().toString());
        addRequest(getRequestManager().request("", map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgress();
                AlertDialog dialog = new AlertDialog.Builder(CustomerRcvMessageActivity.this).setMessage("快递签收成功！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).create();
                dialog.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgress();
                Toast.makeText(CustomerRcvMessageActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }));
    }

}
