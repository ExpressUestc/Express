package com.uestc.express.avtivity.customer;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.Map;

public class CustomerQueryActivity extends BaseActivity {

    private Button btnSubmit;
    private EditText etName, etPhone, etCode;
    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custiomer_query);
        initView();
    }

    private void initView() {
        btnSubmit = (Button) findViewById(R.id.buttonSubmit);
        etName = (EditText) findViewById(R.id.editTextName);
        etPhone = (EditText) findViewById(R.id.editTextPhone);
        etCode = (EditText) findViewById(R.id.editTextCode);
        responseText = (TextView) findViewById(R.id.textView);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etName.getText())) {
                    Toast.makeText(CustomerQueryActivity.this, "请输入姓名~", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etPhone.getText())) {
                    Toast.makeText(CustomerQueryActivity.this, "请输入手机号~", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etCode.getText())) {
                    Toast.makeText(CustomerQueryActivity.this, "请输入CODE~", Toast.LENGTH_SHORT).show();
                } else {
                    showProgress("正在提交，请稍后");
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("rcvName", RsaManager.encrypt(etName.getText().toString()));
                    map.put("rcvPhone", RsaManager.encrypt(etPhone.getText().toString()));
                    map.put("code", RsaManager.encrypt(etCode.getText().toString()));

                    addRequest(getRequestManager().postRequest("find", map, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dismissProgress();
                            try {
                                JSONObject jsn=new JSONObject(Utils.unicode2utf8(response));
                                if (jsn.has("pos")) {
                                    responseText.setText("最新位置：" + jsn.getString("pos"));
                                } else {
                                    responseText.setText(jsn.getString("feedback"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissProgress();
                            responseText.setText("查询失败");
                            error.printStackTrace();
                        }
                    }));
                }
            }
        });
    }
}
