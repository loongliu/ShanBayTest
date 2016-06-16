package com.shanbay.shanbaytest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Liujilong on 2016/6/16.
 * liujilong.me@gmail.com
 */
public class ArticleView extends View {

    private int mTextSize;
    private int mHorizontalGap;
    private int mVerticalGap;



    private String mArticleString;
    private String[] mParagraphs;

    public ArticleView(Context context) {
        this(context, null);
    }

    public ArticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setText(String text){
        mArticleString = text;
        mParagraphs = text.split("\n");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
