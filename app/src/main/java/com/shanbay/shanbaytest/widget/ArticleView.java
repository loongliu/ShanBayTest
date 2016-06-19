package com.shanbay.shanbaytest.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.shanbay.shanbaytest.APP;
import com.shanbay.shanbaytest.R;
import com.shanbay.shanbaytest.util.DimensionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Liujilong on 2016/6/16.
 * liujilong.me@gmail.com
 */
public class ArticleView extends View {

    private int mTextSize;  // textSize in px, default equals to 18sp
    private int textSizeToHorizontalGap = 4;
    private int mHorizontalGap;
    private int textSizeToVerticalGap = 4;
    private int mVerticalGap;

    private int mTextColor;

    private Context mContext;


    private String mArticleString;
    private List<String> mParagraphs;
    private List<List<String>> mWords;
    private List<List<Integer>> mIndexOfWords;
    private List<List<Float>> mGapsOfLine;

    private Paint mTextPaint;

    private static final int[] ATTRS = new int[] {
            android.R.attr.textSize,
            android.R.attr.textColor
    };


    private Rect bound = new Rect();

    public ArticleView(Context context) {
        this(context, null);
    }

    public ArticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,18,dm);
        mHorizontalGap = mTextSize/textSizeToHorizontalGap;
        mVerticalGap = mTextSize/textSizeToVerticalGap;
        mTextColor = getResources().getColor(R.color.textDay);

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        mTextSize = a.getDimensionPixelSize(0, mTextSize);
        mTextColor = a.getColor(1, mTextColor);

        a.recycle();


        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

    }

    public void setTextColor(int color){
        mTextColor = color;
        invalidate();
    }



    public void setText(String text){
        mArticleString = text;
        mParagraphs = Arrays.asList(text.split("\n"));
        mWords = new ArrayList<>();
        mIndexOfWords = new ArrayList<>();
        mGapsOfLine = new ArrayList<>();
        for(String s : mParagraphs){
            List<String> list = Arrays.asList(s.split(" "));
            mWords.add(list);
        }
        requestLayout();
    }

    public void setTextSize(int textSize){
        mTextSize = 80;
        mHorizontalGap = mTextSize/textSizeToHorizontalGap;
        mVerticalGap = mTextSize/textSizeToVerticalGap;

        mTextPaint.setTextSize(mTextSize);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int height,width = 0;
        int screenWidth = DimensionUtils.getDisplay(APP.context()).widthPixels;
        switch (widthMode){
            case MeasureSpec.UNSPECIFIED:
                width = screenWidth;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(screenWidth,widthSize);
                break;
            case MeasureSpec.EXACTLY:
                width = widthSize;
        }

        height = getHeightForWidth(width);
        switch (heightMode){
            case MeasureSpec.AT_MOST:
                height = Math.min(height,heightSize);
                break;
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
        }
        setMeasuredDimension(width, height);
    }

    private int getHeightForWidth(int width){
        if(mArticleString == null || mWords == null || mIndexOfWords == null) return mVerticalGap;
        width -= (getPaddingLeft() + getPaddingRight());
        mTextPaint.getTextBounds("fg",0,2,bound);
        int lineHeight = bound.height();
        float height = mVerticalGap;
        for(List<String> para : mWords){
            float currentWidth = 0;
            List<Integer> indexOfWord = new ArrayList<>();
            List<Float> gaps = new ArrayList<>();
            int lastIndex = 0;
            for(int i = 0; i<para.size(); i++){
                String word = para.get(i);
                float length = mTextPaint.measureText(word);
                float w = currentWidth+mHorizontalGap + length;
                if(w>width){
                    indexOfWord.add(i);
                    float gap = (width-currentWidth)/(i-lastIndex-1);
                    lastIndex = i;
                    gaps.add(gap);
                    height += (lineHeight+mVerticalGap);
                    currentWidth = length;
                }else{
                   currentWidth = w;
                }
            }
            mIndexOfWords.add(indexOfWord);
            mGapsOfLine.add(gaps);
            height+=(lineHeight+mVerticalGap);
        }
        return (int) height+mVerticalGap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mTextPaint.getTextBounds("fg",0,2,bound);
        int lineHeight = bound.height();

        int height = mVerticalGap;
        for(int wordIndex = 0; wordIndex<mWords.size(); wordIndex++){
            List<String> para = mWords.get(wordIndex);

            List<Integer> indexOfWord = mIndexOfWords.get(wordIndex);
            List<Float> gaps = mGapsOfLine.get(wordIndex);

            float currentWidth = getPaddingLeft();
            int index = 0;
            float gapOfLine = gaps.get(0);
            for(int i = 0; i<para.size(); i++){
                String word = para.get(i);
                float length = mTextPaint.measureText(word);

                if(index < indexOfWord.size() &&  i == indexOfWord.get(index) ){
                    currentWidth = getPaddingLeft();
                    height += (lineHeight + mVerticalGap);
                    canvas.drawText(word,currentWidth,height+lineHeight,mTextPaint);
                    index++;
                    if(index>=gaps.size()){
                        gapOfLine = 0;
                    }else{
                        gapOfLine = gaps.get(index);
                    }
                }else{
                    canvas.drawText(word,currentWidth,height+lineHeight,mTextPaint);
                }
                currentWidth += (length+mHorizontalGap + gapOfLine);
            }
            height += (lineHeight + mVerticalGap);
        }
    }
}
