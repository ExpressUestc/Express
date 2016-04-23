package com.uestc.express.avtivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uestc.express.network.RequestManager;

/**
 * Created by Tobb_Huang on 16/4/19.
 */
public class BaseActivity extends Activity {

    RequestQueue mQueue;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public RequestManager getRequestManager() {
        return RequestManager.getRequestManager();
    }

    public void addRequest(StringRequest request) {
        if (mQueue != null) {
            mQueue.add(request);
        }
    }

    public void showProgress(String msg) {
        if (pd == null) {
            pd = new ProgressDialog(this);
        }
        pd.setMessage(msg);
        pd.show();
    }

    public void dismissProgress() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

}
