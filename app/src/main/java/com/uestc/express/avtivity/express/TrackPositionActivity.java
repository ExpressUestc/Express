package com.uestc.express.avtivity.express;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
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

public class TrackPositionActivity extends BaseActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, TrackPositionActivity.class);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, String deliverID, String deliverPhone) {
        Intent intent = new Intent(activity, TrackPositionActivity.class);
        intent.putExtra("deliverID", deliverID);
        intent.putExtra("deliverPhone", deliverPhone);
        activity.startActivity(intent);
    }

    private TextView packageID;
    private TextView position;
    private EditText phone;
    private TextView trackResult;
    private Button track;

    private String message;
    private boolean hasPosition = false;
    private String address;
    private String city;

    private String deliverPhone;
    private String deliverID;

    private String key;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_position);

        deliverID = getIntent().getStringExtra("deliverID");
        deliverPhone = getIntent().getStringExtra("deliverPhone");

        initView();
        startLocation();
    }

    private void startLocation() {
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        address = amapLocation.getAddress();
                        position.setText(amapLocation.getAddress());
                        city = amapLocation.getCity();
                        city = city.replace("市", "");
                        hasPosition = true;
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                        position.setText(amapLocation.getErrorInfo());
                        hasPosition = false;
                    }
                }
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(10000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    private void initView() {
        packageID = (TextView) findViewById(R.id.packageID);
        position = (TextView) findViewById(R.id.position);
        phone = (EditText) findViewById(R.id.phone);
        trackResult = (TextView) findViewById(R.id.track_result);
        track = (Button) findViewById(R.id.track);

        phone.setText(deliverPhone);

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPosition) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(TrackPositionActivity.this);
                    builder.setItems(R.array.scan_item, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    QRCodeActivity.startActivity(TrackPositionActivity.this);
                                    break;
                                case 1:
                                    NFCReaderActivity.startActivity(TrackPositionActivity.this);
                                    break;
                            }
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(TrackPositionActivity.this, "请先等待完成定位", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == QRCodeActivity.QRCODE_REQUEST_CODE) {
                message = data.getStringExtra(Constants.KEY_QRCODE_TEXT);
            } else if (requestCode == NFCReaderActivity.NFCREADER_REQUEST_CODE) {
                message = data.getStringExtra(Constants.KEY_NFC_READER);
            }
            packageID.setText(message);
            trackResult.setText("正在操作，请稍后...");
            showProgress("正在操作，请稍后...");
            doTrack();
        }
    }

    private void doTrack() {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        map.put("pos", RsaManager.encrypt(address));
        map.put("city", RsaManager.encrypt(city));
        map.put("deliverPhone", RsaManager.encrypt(phone.getText().toString()));
        map.put("deliverID", RsaManager.encrypt(deliverID));
        key = Utils.getRandomString(16);
        map.put("key", RsaManager.encrypt(key));
        addRequest(getRequestManager().postRequest("sending", map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgress();
                try {
                    JSONObject jsn = new JSONObject(Utils.unicode2utf8(response));
                    trackResult.setText(Utils.aesDecrypt(jsn.getString("feedback"), key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgress();
                trackResult.setText("操作失败 " + error.toString());
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();
    }
}
