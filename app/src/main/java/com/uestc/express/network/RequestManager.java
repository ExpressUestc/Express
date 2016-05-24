package com.uestc.express.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.uestc.express.Constants;

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

    public StringRequest getRequest(String apiStr, final Map<String, String> params, Response.Listener<String> listener,
                                     Response.ErrorListener errorListener) {
        String url = Constants.URL + apiStr;
        boolean flag = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (flag) {
                flag = false;
                url += "?";
            } else {
                url += "&";
            }
            url += entry.getKey();
            url += "=";
            url += entry.getValue();
        }
        Log.i("express",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);
        return stringRequest;
    }

    public StringRequest postRequest(String apiStr, final Map<String, String> params, Response.Listener<String> listener,
                                 Response.ErrorListener errorListener) {
        String url = Constants.URL + apiStr;
        if (!apiStr.equals("")) {
            url += "/";
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        return stringRequest;
    }

}
