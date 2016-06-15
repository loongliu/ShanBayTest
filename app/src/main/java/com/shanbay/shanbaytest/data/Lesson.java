package com.shanbay.shanbaytest.data;

/**
 * Created by Liujilong on 2016/6/15.
 * liujilong.me@gmail.com
 */
public class Lesson {
    private final String title;
    private final String atricle;

    public Lesson(String t,String a){
        title = t;
        atricle = a;
    }

    public String getTitle() {
        return title;
    }

    public String getAtricle() {
        return atricle;
    }
}
