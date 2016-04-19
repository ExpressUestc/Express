package com.uestc.express.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

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

    public StringRequest demo(final String params, Response.Listener<String> listener, Response.ErrorListener
            errorListener) {
        String url = "https://www.baidu.com";
        StringRequest stringRequest = new StringRequest(url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("params", params);
                return map;
            }
        };

        return stringRequest;
    }

}
