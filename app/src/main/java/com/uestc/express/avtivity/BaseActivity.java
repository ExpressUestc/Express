package com.uestc.express.avtivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uestc.express.R;
import com.uestc.express.network.RequestManager;

/**
 * Created by Tobb_Huang on 16/4/19.
 */
public class BaseActivity extends AppCompatActivity {

    RequestQueue mQueue;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
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

    public void showDialog(String msg, DialogInterface.OnClickListener lister) {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.DialogStyle).setMessage(msg)
                .setPositiveButton("确定", lister).create();
        dialog.show();
    }

}
