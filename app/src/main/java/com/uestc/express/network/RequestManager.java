package com.uestc.express.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tobb_Huang on 16/4/19.
 */
public class RequestManager {

    private static RequestManager Instance;

    public static RequestManager getRequestManager() {
        if (Instance == null) {
            Instance = new RequestManager();
        }
        return Instance;
    }

    public StringRequest demo(final Map<String, String> params, Response.Listener<String> listener, Response.ErrorListener
            errorListener) {
        String url = "http://192.16.137.1:928/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//               Map<String, String> map = new HashMap<>();
//                map.put("params", params);
                return params;
            }
        };

        return stringRequest;
    }

}
