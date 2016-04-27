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

import java.util.HashMap;
import java.util.Map;

public class CustomerQueryActivity extends BaseActivity {

    private Button btnGetCode, btnSubmit;
    private EditText etName, etPhone, etCode;
    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custiomer_query);
        initView();
    }

    private void initView() {
        btnGetCode = (Button) findViewById(R.id.buttonGetCode);
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
                    Toast.makeText(CustomerQueryActivity.this, "请输入验证码~", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("name",etName.getText().toString());
                    map.put("phone",etPhone.getText().toString());
                    map.put("code",etCode.getText().toString());
                    addRequest(getRequestManager().request("test",map, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("response", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("error", error.toString());
                        }
                    }));
                }
            }
        });
        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etName.getText())) {
                    Toast.makeText(CustomerQueryActivity.this, "请输入姓名~", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etPhone.getText())) {
                    Toast.makeText(CustomerQueryActivity.this, "请输入手机号~", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("name",etName.getText().toString());
                    map.put("phone",etPhone.getText().toString());
 //                   jsonObjectRequestPost(map);
                    addRequest(getRequestManager().request("",map, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("response", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("error", error.toString());
                        }
                    }));
                }
            }
        });
    }

    //--------------------------------------------------------------------------------------------------
//    String ip = "";
//    Socket socket = null;
//    BufferedReader reader;
//    BufferedWriter writer;
//
//    private void submit() {
//        AsyncTask<Void, String, Void> send = new AsyncTask<Void, String, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//                    socket = new Socket(ip, 928);
//                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    publishProgress("@success");
//                } catch (IOException e) {
//                    Toast.makeText(CustomerQueryActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onProgressUpdate(String... values) {
//                super.onProgressUpdate(values);
//                if (values[0].equals("@success")) {
//                    try {
//                        Toast.makeText(CustomerQueryActivity.this, "成功连接", Toast.LENGTH_SHORT).show();
//                        writer.write("233");
//                        writer.flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        send.execute();
//    }

    //----------------------------------------------------------------------------------------------------
//    public void getJSONVolley() {
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String JSONDataUrl = "http://www.baidu.com/";
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSONDataUrl, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        System.out.println("response=" + response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        System.out.println("error");
//                    }
//                }
//        );
//        requestQueue.add(jsonObjectRequest);
//    }

//    private void jsonObjectRequestPost(Map<String,String> map) {
//        String url = "";
//        Map<String, String> params = map;
//        final String mRequestBody = appendParameter(url, params);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                responseText.setText(response.toString());
//                System.out.println(response.toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                responseText.setText(error.getMessage());
//            }
//        }) {
//            @Override
//            public byte[] getBody() {
//                return mRequestBody.getBytes();
//            }
//        };
//        mQueue.add(jsonObjectRequest);
//    }
//
//    private String appendParameter(String url, Map<String, String> params) {
//        Uri uri = Uri.parse(url);
//        Uri.Builder builder = uri.buildUpon();
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            builder.appendQueryParameter(entry.getKey(), entry.getValue());
//        }
//        return builder.build().getQuery();
//    }

}
