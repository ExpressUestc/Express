package com.uestc.express.avtivity.express;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.uestc.express.Constants;
import com.uestc.express.R;
import com.uestc.express.avtivity.BaseActivity;
import com.uestc.express.avtivity.NFCReaderActivity;
import com.uestc.express.avtivity.QRCodeActivity;
import com.uestc.express.util.RsaManager;
import com.uestc.express.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendMessageActivity extends BaseActivity {
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, SendMessageActivity.class);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, String deliverID, String deliverPhone) {
        Intent intent = new Intent(activity, SendMessageActivity.class);
        intent.putExtra("deliverPhone", deliverPhone);
        intent.putExtra("deliverID", deliverID);
        activity.startActivity(intent);
    }

    private ImageView btnGetCode;
    private Button btnSendCode;
    private TextView tvPackageMsg, tvMessage;
    private String message;
    private Boolean hasID = false;

    private String deliverPhone;
    private String deliverID;

    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        deliverPhone = getIntent().getStringExtra("deliverPhone");
        deliverID = getIntent().getStringExtra("deliverID");

        init();
    }

    private void init() {
        btnGetCode = (ImageView) findViewById(R.id.buttonGetCode);
        btnSendCode = (Button) findViewById(R.id.buttonSendCode);
        tvPackageMsg = (TextView) findViewById(R.id.textViewpackageMsg);
        tvMessage = (TextView) findViewById(R.id.textViewMessage);
        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SendMessageActivity.this);
                builder.setItems(R.array.scan_item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                QRCodeActivity.startActivity(SendMessageActivity.this);
                                break;
                            case 1:
                                NFCReaderActivity.startActivity(SendMessageActivity.this);
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasID) {
                    showProgress("正在提交，请稍后");
                    Map<String, String> map = new HashMap<>();
                    map.put("message", message);
                    map.put("deliverPhone", RsaManager.encrypt(deliverPhone));
                    map.put("deliverID", RsaManager.encrypt(deliverID));
                    key = Utils.getRandomString(16);
                    map.put("key", RsaManager.encrypt(key));

                    addRequest(getRequestManager().postRequest("distribute", map, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsn = new JSONObject(Utils.unicode2utf8(response));
                                hasID = false;
                                message = "";
                                dismissProgress();
                                tvMessage.setTextColor(ContextCompat.getColor(SendMessageActivity.this, R.color
                                        .douban_green));
                                tvMessage.setText(Utils.aesDecrypt(jsn.getString("feedback"), key));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissProgress();
                            tvMessage.setTextColor(ContextCompat.getColor(SendMessageActivity.this, R.color.douban_red));
                            tvMessage.setText("发送失败");
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
        if (resultCode == RESULT_OK) {
            if (requestCode == QRCodeActivity.QRCODE_REQUEST_CODE) {
                message = data.getStringExtra(Constants.KEY_QRCODE_TEXT);
            } else if (requestCode == NFCReaderActivity.NFCREADER_REQUEST_CODE) {
                message = data.getStringExtra(Constants.KEY_NFC_READER);
            }
            tvPackageMsg.setText("快递单信息：" + message);
            tvMessage.setText("尚未发送信息");
            tvMessage.setTextColor(ContextCompat.getColor(SendMessageActivity.this, R.color.dark_gray));
            hasID = true;
        }
    }
}
