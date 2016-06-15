package com.shanbay.shanbaytest;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.shanbay.shanbaytest.service.DataService;

/**
 * Created by Liujilong on 2016/6/15.
 * liujilong.me@gmail.com
 */
public class APP extends Application {
    private static APP mSingleton;
    public static Context context(){
        return mSingleton.getApplicationContext();
    }

    public void onCreate(){
        super.onCreate();
        mSingleton = this;

        // start service to prepare word level
        Intent i = new Intent(this, DataService.class);
        i.putExtra(DataService.INTENT_LEVEL,true);
        startService(i);
    }
}
