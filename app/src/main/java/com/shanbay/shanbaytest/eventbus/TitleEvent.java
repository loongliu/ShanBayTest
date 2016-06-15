package com.shanbay.shanbaytest.eventbus;

/**
 * Created by Liujilong on 2016/6/15.
 * liujilong.me@gmail.com
 */
public class TitleEvent {
    private String mMsg;
    public TitleEvent(String msg) {
        mMsg = msg;
    }
    public String getMsg(){
        return mMsg;
    }
}
