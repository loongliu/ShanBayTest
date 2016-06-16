package com.shanbay.shanbaytest.data;

/**
 * Created by Liujilong on 2016/6/15.
 * liujilong.me@gmail.com
 */
public class Lesson {
    private final String title;
    private final String titleChinese;
    private String article;

    public Lesson(String t, String tC,String a){
        title = t;
        titleChinese = tC;
        article = a;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleChinese(){
        return titleChinese;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String a){
        article = a;
    }
}
