package com.uestc.express.avtivity.express;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.uestc.express.R;
import com.uestc.express.avtivity.BaseActivity;
import com.uestc.express.util.RsaManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ExpressVerifyActivity extends BaseActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ExpressVerifyActivity.class);
        activity.startActivity(intent);
    }

    EditText deliverPhone;
    EditText deliverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_verify);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_express_verify, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.verify:
                if (deliverPhone.getText().length() == 11 && !deliverId.getText().toString().equals("")) {
                    showProgress("正在操作，请稍后...");
                    doVerify();
                } else{
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        deliverPhone = (EditText) findViewById(R.id.deliver_phone);
        deliverId = (EditText) findViewById(R.id.deliver_id);
    }

    private void doVerify(){
        Map<String, String> map = new HashMap<String,String>();
        map.put("deliverPhone", RsaManager.encrypt(deliverPhone.getText().toString()));
        map.put("deliverID", RsaManager.encrypt(deliverId.getText().toString()));
        addRequest(getRequestManager().postRequest("authDeliver", map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgress();
                try {
                    JSONObject jsn=new JSONObject(response);
                    int flag=jsn.getInt("flag");
                    if(flag==1){
                        ExpressHomeActivity.startActivity(ExpressVerifyActivity.this, deliverId.getText().toString(),
                                deliverPhone.getText().toString());
                        finish();
                    } else{
                        showDialog("验证失败", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgress();
                showDialog("验证失败", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        }));

    }

}
