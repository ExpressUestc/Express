package com.uestc.express.avtivity.express;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
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
import com.uestc.express.avtivity.customer.CustomerQueryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendMessageActivity extends BaseActivity {
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, SendMessageActivity.class);
        activity.startActivity(intent);
    }

    private ImageView btnGetCode;
    private EditText phone;
    private Button btnSendCode;
    private TextView tvPackageID, tvMessage;
    private String pkgID;
    private Boolean hasID = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        init();
    }

    private void init() {
        btnGetCode = (ImageView) findViewById(R.id.buttonGetCode);
        phone = (EditText) findViewById(R.id.phone);
        btnSendCode = (Button) findViewById(R.id.buttonSendCode);
        tvPackageID = (TextView) findViewById(R.id.textViewpackageID);
        tvMessage = (TextView) findViewById(R.id.textViewMessage);
        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeActivity.startActivity(SendMessageActivity.this);
            }
        });
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasID) {
                    showProgress("正在提交，请稍后");
                    Map<String, String> map = new HashMap<>();
                    map.put("code", pkgID);
                    map.put("deliverPhone", phone.getText().toString());
                    addRequest(getRequestManager().getRequest("distribute", map, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            hasID=false;
                            pkgID="";
                            dismissProgress();
                            tvMessage.setTextColor(ContextCompat.getColor(SendMessageActivity.this,R.color.douban_green));
                            tvMessage.setText(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissProgress();
                            tvMessage.setTextColor(ContextCompat.getColor(SendMessageActivity.this,R.color.douban_red));
                            tvMessage.setText(error.toString());
                        }
                    }));
                } else {
                    Toast.makeText(SendMessageActivity.this, "请先扫码获得ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QRCodeActivity.QRCODE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra(Constants.KEY_QRCODE_TEXT);
                try {
                    JSONObject jsn=new JSONObject(result);
                    pkgID = jsn.getString("code");
                    tvPackageID.setText(jsn.getString("code"));
                    tvMessage.setText("尚未发送信息");
                    tvMessage.setTextColor(ContextCompat.getColor(SendMessageActivity.this,R.color.dark_gray));
                    hasID = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
