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
import com.uestc.express.util.RsaManager;
import com.uestc.express.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CustomerRcvMessageActivity extends BaseActivity {

    public static void startActivity(Activity activity, String rcvPhone, String message) {
        Intent intent = new Intent(activity, CustomerRcvMessageActivity.class);
        intent.putExtra("rcvPhone", rcvPhone);
        intent.putExtra("message", message);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, String pkgID, String rcvName, String rcvPhone) {
        Intent intent = new Intent(activity, CustomerRcvMessageActivity.class);
        intent.putExtra("pkgID", pkgID);
        intent.putExtra("rcvName", rcvName);
        intent.putExtra("rcvPhone", rcvPhone);
        activity.startActivity(intent);
    }

    private TextView tv_pkgInfo;
    private TextView tv_rcvPhone;
    private EditText et_verifyCode;
    private Button btn_getCode;
    private Button btn_rcv;

    private String rcvPhone;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_rcv_message);

        rcvPhone = getIntent().getStringExtra("rcvPhone");
        message = getIntent().getStringExtra("message");

        initView();
    }

    private void initView() {
        tv_pkgInfo = (TextView) findViewById(R.id.pkg_info);
        tv_rcvPhone = (TextView) findViewById(R.id.rcv_phone);
        et_verifyCode = (EditText) findViewById(R.id.verify_code);
        btn_getCode = (Button) findViewById(R.id.get_code);
        btn_rcv = (Button) findViewById(R.id.btn_rcv);

        tv_pkgInfo.setText(message);
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
                        btn_getCode.setText("重新获取");
                    }
                });
            }
        }).start();
    }

    private void doPostVerifyMsg() {
        HashMap<String, String> map = new HashMap<>();
        map.put("message", message);
        addRequest(getRequestManager().postRequest("getVerify", map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsn=new JSONObject(Utils.unicode2utf8(response));
                    Toast.makeText(CustomerRcvMessageActivity.this,jsn.getString("feedback"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        map.put("message", message);
        map.put("verify", RsaManager.encrypt(et_verifyCode.getText().toString()));
        addRequest(getRequestManager().postRequest("authVerify", map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgress();
                String feedback = "";
                try {
                    JSONObject jsn=new JSONObject(Utils.unicode2utf8(response));
                    feedback=jsn.getString("feedback");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                showDialog(feedback, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
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
