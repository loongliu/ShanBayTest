package com.shanbay.shanbaytest.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.shanbay.shanbaytest.eventbus.TitleEvent;
import com.shanbay.shanbaytest.util.DataUtils;
import com.shanbay.shanbaytest.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Liujilong on 2016/6/15.
 * liujilong.me@gmail.com
 */
public class DataService extends Service {

    private final String TAG = this.getClass().getSimpleName()+" ljl";

    public static final String INTENT_LEVEL = "intent_level";
    public static final String INTENT_TITLE = "intent_title";
    public static final String INTENT_INDEX = "intent_index";
    public static final String INTENT_ARTICLE = "intent_article";

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    // we only need at most 2 background threads.
    private static Executor executor = Executors.newFixedThreadPool(2);
    public void onCreate() {
        super.onCreate();
        LogUtils.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int returnValue = START_NOT_STICKY ;
        LogUtils.d(TAG,"onStartCommand");
        boolean isTitle = intent.getBooleanExtra(INTENT_TITLE,false);
        if(isTitle){
            executor.execute(titleTask);
        }

        return returnValue;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Runnable titleTask = new Runnable() {
        @Override
        public void run() {
            DataUtils.prepareTitle();
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new TitleEvent("finish"));
                    LogUtils.d(TAG, "EventBus event finish sent");
                }
            });
        }
    };


}
