package com.shanbay.shanbaytest.util;

import com.shanbay.shanbaytest.APP;
import com.shanbay.shanbaytest.R;
import com.shanbay.shanbaytest.data.Lesson;

import java.io.InputStream;
import java.util.ArrayList;
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
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scanner.close();
        sLessonList = list;
        integerArray = new AtomicIntegerArray(list.size());
        isTitlePrepared = true;
    }


}
