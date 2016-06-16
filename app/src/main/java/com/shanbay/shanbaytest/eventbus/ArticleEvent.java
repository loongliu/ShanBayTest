package com.shanbay.shanbaytest.eventbus;

/**
 * Created by Liujilong on 2016/6/16.
 * liujilong.me@gmail.com
 */
public class ArticleEvent {
    int index;
    public ArticleEvent(int i){
        index = i;
    }
    public int getIndex(){
        return index;
    }
}
