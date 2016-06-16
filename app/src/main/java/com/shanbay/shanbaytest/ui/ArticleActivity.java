package com.shanbay.shanbaytest.ui;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shanbay.shanbaytest.R;
import com.shanbay.shanbaytest.eventbus.ArticleEvent;
import com.shanbay.shanbaytest.eventbus.LevelEvent;
import com.shanbay.shanbaytest.service.DataService;
import com.shanbay.shanbaytest.util.DataUtils;
import com.shanbay.shanbaytest.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Liujilong on 2016/6/15.
 * liujilong.me@gmail.com
 */
public class ArticleActivity extends BaseActivity {

    int index;
    String mTitle;

    public static final String INTENT_TITLE = "intent_title";
    public static final String INTENT_INDEX = "intent_index";

    private TextView tvTitle;
    private FrameLayout mProgress;
    private NestedScrollView mScrollView;
    private TextView tvArticle;

    @Override
    protected void initView() {
        setContentView(R.layout.aty_article);
        tvTitle = (TextView) findViewById(R.id.aty_article_title);
        mProgress = (FrameLayout) findViewById(R.id.aty_article_progress);
        mScrollView = (NestedScrollView) findViewById(R.id.aty_article_scroll_view);
        tvArticle = (TextView) findViewById(R.id.aty_article_text);
    }

    @Override
    protected void initData() {
        index = getIntent().getIntExtra(INTENT_INDEX, -1);
        mTitle = getIntent().getStringExtra(INTENT_TITLE);
        tvTitle.setText(mTitle);
        EventBus.getDefault().register(this);


        boolean articlePrepared = DataUtils.articlePrepared(index);
        boolean levelPrepared = DataUtils.isWordLevelPrepared;

        if(articlePrepared && levelPrepared){
            showArticle();
        }else {
            if (!articlePrepared) {
                Intent i = new Intent(this, DataService.class);
                i.putExtra(DataService.INTENT_ARTICLE, true);
                i.putExtra(DataService.INTENT_INDEX, index);
                i.putExtra(DataService.INTENT_ARTICLE_TITLE,mTitle);
                startService(i);
            }
            if(!levelPrepared){
                Intent i = new Intent(this, DataService.class);
                i.putExtra(DataService.INTENT_LEVEL,true);
                startService(i);
            }
        }


    }

    private void eventReceived(){
        if(DataUtils.articlePrepared(index) && DataUtils.isWordLevelPrepared){
            showArticle();
        }
    }


    private void showArticle(){
        mProgress.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
        tvArticle.setText(DataUtils.getLessonList().get(index).getArticle());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onEventMainThread(LevelEvent event){
        LogUtils.d(TAG, "onEventMainThread TitleEvent: " + event.getMsg());
        eventReceived();
    }

    @Subscribe
    public void onEventMainThread(ArticleEvent event){
        LogUtils.d(TAG, "onEventMainThread TitleEvent: " + event.getIndex());
        if(event.getIndex()!=index) return;
        eventReceived();
    }
}
