package com.shanbay.shanbaytest.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.shanbay.shanbaytest.eventbus.ArticleEvent;
import com.shanbay.shanbaytest.eventbus.LevelEvent;
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
    public static final String INTENT_ARTICLE_TITLE = "intent_article_title";

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
        if(isTitle && !DataUtils.isTitlePrepared){
            executor.execute(titleTask);
        }

        boolean isLevel = intent.getBooleanExtra(INTENT_LEVEL,false);
        if(isLevel && !DataUtils.isWordLevelPrepared){
            executor.execute(levelTask);
        }

        boolean isArticle = intent.getBooleanExtra(INTENT_ARTICLE,false);
        if(isArticle){
            int index = intent.getIntExtra(INTENT_INDEX,-1);
            String title = intent.getStringExtra(INTENT_ARTICLE_TITLE);
            if(index!=-1 && !DataUtils.articlePrepared(index)){
                executor.execute(new ArticleTask(index, title));
            }
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
                    LogUtils.d(TAG, "EventBus TitleEvent finish sent");
                }
            });
        }
    };

    Runnable levelTask = new Runnable() {
        @Override
        public void run() {
            DataUtils.prepareLevel();
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new LevelEvent("finish"));
                    LogUtils.d(TAG, "EventBus LevelEvent finish sent");
                }
            });
        }
    };

    class ArticleTask implements Runnable{
        int index;
        String title;
        public ArticleTask(int i, String title){ index = i; this.title = title;}
        @Override
        public void run() {
            DataUtils.prepareArticle(index, title);
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new ArticleEvent(index));
                    LogUtils.d(TAG, "EventBus LevelEvent finish sent");
                }
            });
        }
    }


}
