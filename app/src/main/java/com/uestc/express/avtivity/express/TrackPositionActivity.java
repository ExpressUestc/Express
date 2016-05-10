package com.uestc.express.avtivity.express;

import android.app.Activity;
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
import com.uestc.express.avtivity.QRCodeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TrackPositionActivity extends BaseActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, TrackPositionActivity.class);
        activity.startActivity(intent);
    }

    private TextView packageID;
    private TextView position;
    private EditText phone;
    private TextView trackResult;
    private Button track;

    private String pkgID;
    private boolean hasPosition = false;
    private String address;

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
//                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                        amapLocation.getLatitude();//获取纬度
//                        amapLocation.getLongitude();//获取经度
//                        amapLocation.getAccuracy();//获取精度信息
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        Date date = new Date(amapLocation.getTime());
//                        df.format(date);//定位时间
//                        amapLocation.getCountry();//国家信息
//                        amapLocation.getProvince();//省信息
//                        amapLocation.getCity();//城市信息
//                        amapLocation.getDistrict();//城区信息
//                        amapLocation.getStreet();//街道信息
//                        amapLocation.getStreetNum();//街道门牌号信息
//                        amapLocation.getCityCode();//城市编码
//                        amapLocation.getAdCode();//地区编码
                        address = amapLocation.getAddress();
                        position.setText(amapLocation.getAddress());
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

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPosition)
                    QRCodeActivity.startActivity(TrackPositionActivity.this);
                else
                    Toast.makeText(TrackPositionActivity.this, "请先等待完成定位", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QRCodeActivity.QRCODE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra(Constants.KEY_QRCODE_TEXT);
                try {
                    JSONObject jsn=new JSONObject(result);
                    pkgID=jsn.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                packageID.setText(pkgID);
                trackResult.setText("正在操作，请稍后...");
                showProgress("正在操作，请稍后...");
                doTrack();
            }
        }
    }

    private void doTrack() {
        Map<String, String> map = new HashMap<>();
        map.put("code", pkgID);
        map.put("pos", address);
        map.put("deliverPhone", phone.getText().toString());
        addRequest(getRequestManager().getRequest("sending", map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgress();
                try {
                    JSONObject jsn = new JSONObject(response);
                    trackResult.setText(jsn.getString("feedback"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgress();
                trackResult.setText(error.getMessage());
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();
    }
}
