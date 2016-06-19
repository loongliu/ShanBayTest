package com.shanbay.shanbaytest.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.shanbay.shanbaytest.APP;
import com.shanbay.shanbaytest.R;
import com.shanbay.shanbaytest.util.DimensionUtils;
import com.shanbay.shanbaytest.util.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    private Map<String,Integer> mWordLevel;

    private Paint mTextPaint;
    private Paint mHighLightPaint;
    private RectF mHighLightRect;

    private static final int[] ATTRS = new int[] {
            android.R.attr.textSize,
            android.R.attr.textColor
    };


    private Rect bound = new Rect();

    boolean mHighLight = false;
    int mHighLightLevel = 10;

    String mHighLightWord;

    int mLineHeight;

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

        mHighLightPaint = new Paint();
        mHighLightPaint.setAntiAlias(true);
        mHighLightPaint.setColor(0x5FFFFF00);

        mHighLightRect = new RectF();
    }

    public void setTextColor(int color){
        mTextColor = color;
        invalidate();
    }

    public void setWordLevel(Map<String,Integer> level){
        mWordLevel = level;
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

    public boolean getHighLight(){
        return mHighLight;
    }

    public void setHighLight(boolean high, int level){
        mHighLight = high;
        mHighLightLevel = level;
        mHighLightWord = null;
        invalidate();
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
        mLineHeight = bound.height();
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
                    height += (mLineHeight +mVerticalGap);
                    currentWidth = length;
                }else{
                   currentWidth = w;
                }
            }
            mIndexOfWords.add(indexOfWord);
            mGapsOfLine.add(gaps);
            height+=(mLineHeight +mVerticalGap);
        }
        return (int) height+mVerticalGap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mTextPaint.getTextBounds("fg",0,2,bound);
        mLineHeight = bound.height();

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
                    height += (mLineHeight + mVerticalGap);
                    drawText(canvas, word, currentWidth, height, currentWidth + length, height + mLineHeight);
                    index++;
                    if(index>=gaps.size()){
                        gapOfLine = 0;
                    }else{
                        gapOfLine = gaps.get(index);
                    }
                }else{
                    drawText(canvas, word, currentWidth, height, currentWidth + length, height + mLineHeight);
                }
                currentWidth += (length+mHorizontalGap + gapOfLine);
            }
            height += (mLineHeight + mVerticalGap);
        }
    }

    private String getCurrentWord(float x, float y){
        LogUtils.d("ArticleView", "x: " + x + " y: " + y);
        int line = (int) (y/(mVerticalGap + mLineHeight));
        int pase = 0;
        for(int ii = 0; ii<mIndexOfWords.size(); ii++){
            List<Integer> index = mIndexOfWords.get(ii);
            if(index.size()<line){
                line -= (index.size()+1);
                pase++;
            }else{
                List<String> words = mWords.get(pase);
                int i = line==0 ? 0 : index.get(line-1);
                for(; i<words.size(); i++){
                    String word = words.get(i);
                    List<Float> gaps = mGapsOfLine.get(ii);
                    float gap = line == gaps.size() ? 0 : gaps.get(line);
                    float wordLength = mTextPaint.measureText(word)+ mHorizontalGap + gap;
                    if(wordLength<x){
                        x-=wordLength;
                    }else{
                        LogUtils.d("ArticleView","Word found: " + word);
                        mHighLightWord = word;
                        invalidate();
                        return word;
                    }
                }
            }
        }
        return "";
    }

    private void drawText(Canvas canvas, String word, float left, float top, float right, float bottom ){
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        canvas.drawText(word, left, bottom-fontMetrics.bottom, mTextPaint);
        if(shouldHighLight(word)) {
            int radius = DimensionUtils.dp2px(1, mContext);
            mHighLightRect.left = left-radius;
            mHighLightRect.top = top-radius;
            mHighLightRect.right = right+radius;
            mHighLightRect.bottom = bottom+radius;
            canvas.drawRoundRect(mHighLightRect, radius, radius, mHighLightPaint);
        }
    }

    private boolean shouldHighLight(String word) {
        return word.equals(mHighLightWord) || (mHighLight && mWordLevel != null && mWordLevel.containsKey(word) && mWordLevel.get(word) <= mHighLightLevel);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    GestureDetector mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            String word = getCurrentWord(e.getX(), e.getY());
            if(mWordLevel.containsKey(word)){
                setHighLight(true, mWordLevel.get(word));
            }else{
                mHighLight = false;
            }
            return super.onSingleTapUp(e);
        }
    });

}
