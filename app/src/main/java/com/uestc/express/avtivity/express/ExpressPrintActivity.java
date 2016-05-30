package com.uestc.express.avtivity.express;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.uestc.express.R;
import com.uestc.express.avtivity.BaseActivity;
import com.uestc.express.util.Utils;

public class ExpressPrintActivity extends BaseActivity {

    public static void startActivity(Activity activity, String msg) {
        Intent intent = new Intent(activity, ExpressPrintActivity.class);
        intent.putExtra("message", msg);
        activity.startActivity(intent);
    }

    ImageView qrcode;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_print);

        initView();

    }

    private void initView() {
        qrcode = (ImageView) findViewById(R.id.qrcode);
        save = (Button) findViewById(R.id.save);

        try {
            qrcode.setImageBitmap(Utils.createQRCode(getIntent().getStringExtra("message"), Utils.QRCODE_SIZE));
        } catch (WriterException e) {
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qrcode.setDrawingCacheEnabled(true);
                Bitmap bm = qrcode.getDrawingCache();

                if (Utils.saveBitmap(bm, getIntent().getStringExtra("message").substring(0, 10))) {
                    Toast.makeText(ExpressPrintActivity.this, "已成功保存到"+Utils.CACHE_PATH+getIntent().getStringExtra("message").substring(0, 10), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ExpressPrintActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
