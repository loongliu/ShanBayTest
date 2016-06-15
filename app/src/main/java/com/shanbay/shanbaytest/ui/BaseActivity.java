package com.shanbay.shanbaytest.ui;

import android.app.Activity;
import android.os.Bundle;

import com.shanbay.shanbaytest.util.LogUtils;

/**
 * Created by Liujilong on 2016/6/15.
 * liujilong.me@gmail.com
 */
public abstract class BaseActivity extends Activity {
    protected final String TAG = this.getClass().getSimpleName()+" ljl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG,"onCreate()");
        initView();
        initData();
    }

    protected abstract void initView();
    protected abstract void initData();

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.d(TAG, "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy()");
    }

}
