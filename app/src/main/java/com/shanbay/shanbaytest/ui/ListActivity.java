package com.shanbay.shanbaytest.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.shanbay.shanbaytest.R;
import com.shanbay.shanbaytest.eventbus.TitleEvent;
import com.shanbay.shanbaytest.service.DataService;
import com.shanbay.shanbaytest.util.DataUtils;
import com.shanbay.shanbaytest.util.LogUtils;
import com.shanbay.shanbaytest.widget.VerticalSpaceItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Liujilong on 2016/6/15.
 * liujilong.me@gmail.com
 */
public class ListActivity extends BaseActivity {
    RecyclerView mRecyclerView;
    ListAdapter mAdapter;
    FrameLayout mProgress;

    @Override
    protected void initView() {
        setContentView(R.layout.aty_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.aty_list_recycler);
        mProgress = (FrameLayout) findViewById(R.id.aty_list_progress);
        mRecyclerView.addItemDecoration(
                new VerticalSpaceItemDecoration(this, R.drawable.divider));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        if(DataUtils.isTitlePrepared){
            configRecyclerView();
        }else {
            Intent i = new Intent(this, DataService.class);
            i.putExtra(DataService.INTENT_TITLE, true);
            startService(i);
        }
    }

    private void configRecyclerView(){
        mProgress.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter = new ListAdapter(this);
        mAdapter.setLessonList(DataUtils.getLessonList());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Subscribe
    public void onEventMainThread(TitleEvent event){
        LogUtils.d(TAG, "onEventMainThread TitleEvent: " + event.getMsg());
        configRecyclerView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
