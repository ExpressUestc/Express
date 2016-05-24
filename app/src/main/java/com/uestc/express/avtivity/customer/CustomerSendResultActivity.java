package com.uestc.express.avtivity.customer;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.uestc.express.R;
import com.uestc.express.avtivity.BaseActivity;
import com.uestc.express.util.RsaManager;
import com.uestc.express.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomerSendResultActivity extends BaseActivity {

    ImageView qrcode;
    TextView code;

    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_send_result);
        imageLoader = ImageLoader.getInstance();

        initView();
    }

    private void initView() {
        qrcode = (ImageView) findViewById(R.id.qrcode);
        code = (TextView) findViewById(R.id.code);

        JSONObject jsn = new JSONObject();
        try {
            jsn.put("code", getIntent().getStringExtra("code"));
            jsn.put("rcvPhone", getIntent().getStringExtra("rcvPhone"));
            qrcode.setImageBitmap(Utils.createQRCode(RsaManager.getrsaManager().encrypt(jsn.toString()), 200));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
        code.setText("快件ID：" + getIntent().getStringExtra("code"));
    }
}
