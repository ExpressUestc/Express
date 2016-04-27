package com.uestc.express.avtivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.uestc.express.Constants;
import com.uestc.express.R;

public class QRCodeActivity extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener {

    public static int QRCODE_REQUEST_CODE = 0;

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, QRCodeActivity.class);
        activity.startActivityForResult(intent, QRCODE_REQUEST_CODE);
    }

    QRCodeReaderView mQRCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        initView();

    }

    private void initView() {
        mQRCodeView = (QRCodeReaderView) findViewById(R.id.qrcode);
        mQRCodeView.setOnQRCodeReadListener(this);
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_QRCODE_TEXT, text);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mQRCodeView.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mQRCodeView.getCameraManager().stopPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mQRCodeView.getCameraManager().stopPreview();
    }
}
