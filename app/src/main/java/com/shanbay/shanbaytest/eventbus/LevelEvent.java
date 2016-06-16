package com.shanbay.shanbaytest.eventbus;

/**
 * Created by Liujilong on 2016/6/16.
 * liujilong.me@gmail.com
 */
public class LevelEvent {
    private String mMsg;
    public LevelEvent(String msg) {
        mMsg = msg;
    }
    public String getMsg(){
        return mMsg;
    }
}
