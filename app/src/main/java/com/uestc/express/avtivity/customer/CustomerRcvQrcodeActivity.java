package com.uestc.express.avtivity.customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.uestc.express.Constants;
import com.uestc.express.R;
import com.uestc.express.avtivity.BaseActivity;
import com.uestc.express.avtivity.QRCodeActivity;
import com.uestc.express.util.RsaManager;
import com.uestc.express.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CustomerRcvQrcodeActivity extends BaseActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, CustomerRcvQrcodeActivity.class);
        activity.startActivity(intent);
    }

    private MenuItem verifyItem;

    private TextView pkgInfo;
    private ImageView scan;
    private TextView tvRcvPhone;
    private EditText etRcvPhone;
    private TextView notice;
    private TextView error_notice;

    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_rcv_qrcode);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_customer_rcv_qrcode, menu);
        verifyItem = menu.findItem(R.id.verify);
        verifyItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.verify:
                if (etRcvPhone.getText().length() == 11) {
                    showProgress("正在操作，请稍后...");
                    doVerify();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        pkgInfo = (TextView) findViewById(R.id.pkg_info);
        scan = (ImageView) findViewById(R.id.scan);
        tvRcvPhone = (TextView) findViewById(R.id.tv_rcv_phone);
        etRcvPhone = (EditText) findViewById(R.id.et_rcv_phone);
        notice = (TextView) findViewById(R.id.notice);
        error_notice = (TextView) findViewById(R.id.error_notice);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeActivity.startActivity(CustomerRcvQrcodeActivity.this);
            }
        });

    }

    private void doVerify() {
        HashMap<String, String> map = new HashMap<>();
        map.put("rcvPhone", RsaManager.encrypt(etRcvPhone.getText().toString()));
        map.put("message", message);
        addRequest(getRequestManager().postRequest("auth", map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsn=new JSONObject(Utils.unicode2utf8(response));
                    if(jsn.getInt("flag")==1){
                        doPostVerifyMsg();
                    } else{
                        dismissProgress();
                        error_notice.setText(jsn.getString("response"));
                        error_notice.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgress();
                Toast.makeText(CustomerRcvQrcodeActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }));
    }

    private void doPostVerifyMsg() {
        HashMap<String, String> map = new HashMap<>();
        map.put("message", message);
        addRequest(getRequestManager().postRequest("getVerify", map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgress();
                try {
                    JSONObject jsn=new JSONObject(Utils.unicode2utf8(response));
                    Toast.makeText(CustomerRcvQrcodeActivity.this, jsn.getString("feedback"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CustomerRcvMessageActivity.startActivity(CustomerRcvQrcodeActivity.this, etRcvPhone.getText().toString(), message);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgress();
                Toast.makeText(CustomerRcvQrcodeActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QRCodeActivity.QRCODE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                message = data.getStringExtra(Constants.KEY_QRCODE_TEXT);
                pkgInfo.setText(message);
                tvRcvPhone.setVisibility(View.VISIBLE);
                etRcvPhone.setVisibility(View.VISIBLE);
                notice.setVisibility(View.VISIBLE);
                verifyItem.setVisible(true);
            }
        }
    }
}
