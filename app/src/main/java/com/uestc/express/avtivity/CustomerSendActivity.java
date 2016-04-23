package com.uestc.express.avtivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.uestc.express.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerSendActivity extends BaseActivity {
    private Button btnSend;
    private EditText etMyName, etMyPhone, etMyAddress, etMyPostalCode, etExtraPrice, etRcvName, etRcvPhone, etRcvAddress, etRcvPostalCode, etExpressCompany, etRemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_send);
        initView();
    }

    private void initView() {
        etMyName = (EditText) findViewById(R.id.editTextMyName);
        etMyPhone = (EditText) findViewById(R.id.editTextMyPhone);
        etMyAddress = (EditText) findViewById(R.id.editTextMyAddress);
        etMyPostalCode = (EditText) findViewById(R.id.editTextMyPostalCode);
        etExtraPrice = (EditText) findViewById(R.id.editTextExtraPrice);
        etRcvName = (EditText) findViewById(R.id.editTextRcvName);
        etRcvAddress = (EditText) findViewById(R.id.editTextRcvAddress);
        etRcvPostalCode = (EditText) findViewById(R.id.editTextRcvPostalCode);
        etExpressCompany = (EditText) findViewById(R.id.editTextExpressCompany);
        etRemarks = (EditText) findViewById(R.id.editTextRemarks);
        btnSend= (Button) findViewById(R.id.buttonSubmit);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etMyName.getText())) {
                    Toast.makeText(CustomerSendActivity.this, "请输入寄件人姓名~", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etMyPhone.getText())) {
                    Toast.makeText(CustomerSendActivity.this, "请输入寄件人电话~", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etMyAddress.getText())) {
                    Toast.makeText(CustomerSendActivity.this, "请输入寄件人地址~", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etMyPostalCode.getText())) {
                    Toast.makeText(CustomerSendActivity.this, "请输入寄件人邮编~", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etExtraPrice.getText())) {
                    Toast.makeText(CustomerSendActivity.this, "请输入保价金额~", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etRcvName.getText())) {
                    Toast.makeText(CustomerSendActivity.this, "请输入收件人姓名~", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etRcvAddress.getText())) {
                    Toast.makeText(CustomerSendActivity.this, "请输入收件人地址~", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etRcvPostalCode.getText())) {
                    Toast.makeText(CustomerSendActivity.this, "请输入收件人邮编~", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etExpressCompany.getText())) {
                    Toast.makeText(CustomerSendActivity.this, "请输入快递公司编号~", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etRemarks.getText())) {
                    Toast.makeText(CustomerSendActivity.this, "请输入备注信息~", Toast.LENGTH_SHORT).show();
                } else {
                    showProgress("正在提交，请稍后");

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("MyName",etMyName.getText().toString());
                    map.put("MyPhone",etMyPhone.getText().toString());
                    map.put("MyAddress",etMyAddress.getText().toString());
                    map.put("MyPostalCode",etMyPostalCode.getText().toString());
                    map.put("ExtraPrice",etExtraPrice.getText().toString());
                    map.put("RcvName",etRcvName.getText().toString());
                    map.put("RcvAddress",etRcvAddress.getText().toString());
                    map.put("RcvPostalCode",etRcvPostalCode.getText().toString());
                    map.put("ExpressCompany",etExpressCompany.getText().toString());
                    map.put("Remarks",etRemarks.getText().toString());
//                    addRequest(getRequestManager().request("test", map, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            dismissProgress();
//                            try {
//                                JSONObject jsn=new JSONObject(response);
//                                Intent intent=new Intent(CustomerSendActivity.this,CustomerSendResultActivity.class);
//                                intent.putExtra("code",jsn.getString("code"));
//                                intent.putExtra("url",jsn.getString("url"));
//                                startActivity(intent);
//                                finish();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            dismissProgress();
//                            Toast.makeText(CustomerSendActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
//                            Log.i("error", error.toString());
//                        }
//                    }));

                    dismissProgress();
                    Intent intent=new Intent(CustomerSendActivity.this,CustomerSendResultActivity.class);
                    intent.putExtra("code","code");
                    intent.putExtra("url","url");
                    startActivity(intent);
                    finish();

                }
            }

            ;
        });
    }
}