package com.uestc.express.avtivity.customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Map;

public class CustomerSendActivity extends BaseActivity {
    private Button btnSend;
    private EditText etMyName, etMyPhone,etMyCity, etMyAddress, etMyPostalCode, etExtraPrice, etRcvName, etRcvPhone,
            etRcvCity, etRcvAddress, etRcvPostalCode, etGoods, etExpressCompany, etRemarks;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_send);
        initView();
    }

    private void initView() {
        etMyName = (EditText) findViewById(R.id.editTextMyName);
        etMyPhone = (EditText) findViewById(R.id.editTextMyPhone);
        etMyCity = (EditText) findViewById(R.id.editTextMyCity);
        etMyAddress = (EditText) findViewById(R.id.editTextMyAddress);
        etMyPostalCode = (EditText) findViewById(R.id.editTextMyPostalCode);
        etExtraPrice = (EditText) findViewById(R.id.editTextExtraPrice);
        etRcvName = (EditText) findViewById(R.id.editTextRcvName);
        etRcvPhone = (EditText) findViewById(R.id.editTextRcvPhone);
        etRcvCity = (EditText) findViewById(R.id.editTextRcvCity);
        etRcvAddress = (EditText) findViewById(R.id.editTextRcvAddress);
        etRcvPostalCode = (EditText) findViewById(R.id.editTextRcvPostalCode);
        etGoods = (EditText) findViewById(R.id.editTextGoods);
        etExpressCompany = (EditText) findViewById(R.id.editTextExpressCompany);
        etRemarks = (EditText) findViewById(R.id.editTextRemarks);
        btnSend = (Button) findViewById(R.id.buttonSubmit);
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
                    map.put("myName", RsaManager.encrypt(etMyName.getText().toString()));
                    map.put("myPhone", RsaManager.encrypt(etMyPhone.getText().toString()));
                    map.put("sendCity", RsaManager.encrypt(etMyCity.getText().toString()));
                    map.put("myAddress", RsaManager.encrypt(etMyCity.getText().toString() + etMyAddress.getText()
                            .toString()));
                    map.put("myPostcode", RsaManager.encrypt(etMyPostalCode.getText().toString()));
                    map.put("extraPrice", RsaManager.encrypt(etExtraPrice.getText().toString()));
                    map.put("rcvName", RsaManager.encrypt(etRcvName.getText().toString()));
                    map.put("rcvPhone", RsaManager.encrypt(etRcvPhone.getText().toString()));
                    map.put("rcvCity", RsaManager.encrypt(etRcvCity.getText().toString()));
                    map.put("rcvAddress", RsaManager.encrypt(etRcvCity.getText().toString() + etRcvAddress.getText()
                            .toString()));
                    map.put("rcvPostcode", RsaManager.encrypt(etRcvPostalCode.getText().toString()));
                    map.put("goods", RsaManager.encrypt(etGoods.getText().toString()));
                    map.put("expressCompany", RsaManager.encrypt(etExpressCompany.getText().toString()));
                    map.put("remarks", RsaManager.encrypt(etRemarks.getText().toString()));
                    key = Utils.getRandomString(16);
                    map.put("key", RsaManager.encrypt(key));
                    addRequest(getRequestManager().postRequest("", map, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dismissProgress();
                            try {
                                JSONObject jsn = new JSONObject(response);
                                Intent intent = new Intent(CustomerSendActivity.this, CustomerSendResultActivity.class);
                                intent.putExtra("code", Utils.aesDecrypt(jsn.getString("code"), key));
                                intent.putExtra("rcvName", etRcvName.getText().toString());
                                intent.putExtra("rcvPhone", etRcvPhone.getText().toString());
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissProgress();
                            Toast.makeText(CustomerSendActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }));
                }
            }

            ;
        });
    }
}