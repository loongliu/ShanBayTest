package com.shanbay.shanbaytest.util;

import android.content.Context;

import com.shanbay.shanbaytest.APP;
import com.shanbay.shanbaytest.R;
import com.shanbay.shanbaytest.data.Lesson;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by Liujilong on 2016/6/15.
 * liujilong.me@gmail.com
 */
public final class DataUtils {




    private DataUtils(){}

    public static volatile boolean isWordLevelPrepared = false;
    public static volatile boolean isTitlePrepared = false;
    public static AtomicIntegerArray integerArray;
    private static Map<String,Integer> sWordLevel;
    private static List<Lesson> sLessonList;

    private static String[] rawArray;
    static{
        rawArray = new String[48];
        for(int i = 0; i<rawArray.length; i++){
            rawArray[i] = "article_"+(i+1);
        }
    }

    public static boolean articlePrepared(int index){
        boolean p = (integerArray.get(index) == 1);
        Lesson l = sLessonList.get(index);
        return p && l.getArticle()!=null;
    }


    public static Map<String,Integer> getWordLevel(){
        return sWordLevel;
    }



    public static List<Lesson> getLessonList(){
        return sLessonList;
    }




    // run on the background thread
    public static void prepareTitle(){
        if(isTitlePrepared && sLessonList!=null){
            return;
        }
        List<Lesson> list = new ArrayList<>();
        InputStream in = APP.context(). getResources().openRawResource(R.raw.lesson_list);
        Scanner scanner = new Scanner(in);
        while(scanner.hasNext()){
            String title = scanner.nextLine();
            if(!scanner.hasNext()) break;
            String chineseTitle = scanner.nextLine();
            list.add(new Lesson(title,chineseTitle,null));
        }
        try {
            // sleep to simulate a time-consuming task
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scanner.close();
        sLessonList = list;
        integerArray = new AtomicIntegerArray(list.size());
        isTitlePrepared = true;
    }

    // run on the background thread
    public static void prepareLevel(){
        if(isWordLevelPrepared && sWordLevel!=null){
            return;
        }
        HashMap<String,Integer> map = new HashMap<>();
        InputStream in = APP.context(). getResources().openRawResource(R.raw.nce4_words);
        Scanner scanner = new Scanner(in);
        while(scanner.hasNext()){
            String line = scanner.nextLine();
            String word = line.substring(0, line.length() - 2);
            int level = Integer.parseInt(line.substring(line.length() - 1));
            map.put(word, level);
        }
        try {
            // sleep to simulate a time-consuming task
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scanner.close();
        sWordLevel = map;
        isWordLevelPrepared = true;
    }

    // run on the background thread
    public static void prepareArticle(int index, String title){
        if(index >= sLessonList.size()){
            throw new IndexOutOfBoundsException("article index is larger than article count");
        }
        Lesson lesson = sLessonList.get(index);
        if(lesson == null){
            lesson = new Lesson(title,null,null);
        }
        if(articlePrepared(index)  && lesson.getArticle()!=null){
            return;
        }
        Context context = APP.context();
        int articleID = context.getResources().getIdentifier(rawArray[index], "raw", context.getPackageName());

        InputStream in = APP.context(). getResources().openRawResource(articleID);
        Scanner s = new Scanner(in).useDelimiter("\\A");
        String article = s.hasNext() ? s.next() : "";
        try {
            // sleep to simulate a time-consuming task
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lesson.setArticle(article);
        sLessonList.set(index, lesson);
        integerArray.set(index,1);
    }

}
