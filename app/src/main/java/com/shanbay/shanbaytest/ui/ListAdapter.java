package com.shanbay.shanbaytest.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanbay.shanbaytest.R;
import com.shanbay.shanbaytest.data.Lesson;

import java.util.List;

/**
 * Created by Liujilong on 2016/6/15.
 * liujilong.me@gmail.com
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {
    private List<Lesson> mLessonList;
    private Context mContext;

    public ListAdapter(Context context){
        mContext = context;
    }

    public void setLessonList(List<Lesson> lessonList){
        mLessonList = lessonList;
    }

    @Override
    public ListAdapter.ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.aty_list_adapter,parent,false);
        ListHolder listHolder = new ListHolder(v);
        return listHolder;
    }

    @Override
    public void onBindViewHolder(ListAdapter.ListHolder holder, int position) {
        holder.tv.setText(mLessonList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mLessonList==null ? 0 : mLessonList.size();
    }

    class ListHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public ListHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }
    }
}
