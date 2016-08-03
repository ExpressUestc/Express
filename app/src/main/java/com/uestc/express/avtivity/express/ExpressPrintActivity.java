package com.uestc.express.avtivity.express;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.zxing.WriterException;
import com.uestc.express.R;
import com.uestc.express.avtivity.BaseActivity;
import com.uestc.express.avtivity.NFCWriterActivity;
import com.uestc.express.util.RsaManager;
import com.uestc.express.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ExpressPrintActivity extends BaseActivity {

    public static void startActivity(Activity activity, String msg) {
        Intent intent = new Intent(activity, ExpressPrintActivity.class);
        intent.putExtra("message", msg);
        activity.startActivity(intent);
    }

    ImageView qrcode;
    Button save;
    Button nfc;

    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_print);

        initView();

    }

    private void initView() {
        qrcode = (ImageView) findViewById(R.id.qrcode);
        save = (Button) findViewById(R.id.save);
        nfc = (Button) findViewById(R.id.nfc);

        try {
            qrcode.setImageBitmap(Utils.createQRCode(getIntent().getStringExtra("message"), Utils.QRCODE_SIZE));
        } catch (WriterException e) {
            e.printStackTrace();
        }

        showProgress("正在验证，请稍后...");

        HashMap<String, String> map = new HashMap<>();
        map.put("message", getIntent().getStringExtra("message"));
        key = Utils.getRandomString(16);
        map.put("key", RsaManager.encrypt(key));
        addRequest(getRequestManager().postRequest("authPost", map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgress();
                try {
                    JSONObject jsn = new JSONObject(response);
                    if (jsn.has("flag") && Integer.parseInt(Utils.aesDecrypt(jsn.getString("flag"), key)) == 1) {
                        Toast.makeText(ExpressPrintActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(ExpressPrintActivity.this, "验证失败，请检查快递单合法性", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgress();
                Toast.makeText(ExpressPrintActivity.this, "验证失败 " + error.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrcode.setDrawingCacheEnabled(true);
                Bitmap bm = qrcode.getDrawingCache();

                String path = getIntent().getStringExtra("message").substring(0, 10).replaceAll("/", "");
                if (Utils.saveBitmap(bm, path)) {
                    Toast.makeText(ExpressPrintActivity.this, "已成功保存到" + Utils.CACHE_PATH + path, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(ExpressPrintActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NFCWriterActivity.startActivity(ExpressPrintActivity.this, getIntent().getStringExtra("message"));
            }
        });

    }
}
