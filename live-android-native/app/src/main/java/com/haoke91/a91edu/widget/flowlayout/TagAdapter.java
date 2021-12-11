package com.haoke91.a91edu.widget.flowlayout;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/16 上午11:09
 * 修改人：weiyimeng
 * 修改时间：2018/7/16 上午11:09
 * 修改备注：
 */

import android.util.Log;
import android.view.View;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class TagAdapter<T> {
    private List<T> mTagDates;
    private OnDataChangedListener mOnDataChangedListener;
    @Deprecated
    private HashSet<Integer> mCheckedPosList = new HashSet();
    
    public TagAdapter(List<T> dates) {
        mTagDates = dates;
    }
    
    @Deprecated
    public TagAdapter(T[] dates) {
        mTagDates = new ArrayList<>(Arrays.asList(dates));
    }
    
    interface OnDataChangedListener {
        void onChanged();
    }
    
    void setOnDataChangedListener(OnDataChangedListener listener) {
        mOnDataChangedListener = listener;
    }
    
    @Deprecated
    public void setSelectedList(int... poses) {
        Set<Integer> set = new HashSet<>();
        for (int pos : poses) {
            set.add(pos);
        }
        setSelectedList(set);
    }
    
    @Deprecated
    public void setSelectedList(Set<Integer> set) {
        mCheckedPosList.clear();
        if (set != null) {
            mCheckedPosList.addAll(set);
        }
        notifyDataChanged();
    }
    
    @Deprecated
   public HashSet<Integer> getPreCheckedList() {
        return mCheckedPosList;
    }
    
    
    public int getCount() {
        return mTagDates == null ? 0 : mTagDates.size();
    }
    
    public void notifyDataChanged() {
        if (mOnDataChangedListener != null) {
            mOnDataChangedListener.onChanged();
        }
    }
    
    public void setDate(List<T> datas) {
        mTagDates = datas;
    }
    
    public T getItem(int position) {
        return mTagDates.get(position);
    }
    
    public abstract View getView(FlowLayout parent, int position, T t);
    
    
    public void onSelected(int position, View view) {
        Log.d("zhy", "onSelected " + position);
    }
    
    public void unSelected(int position, View view) {
        Log.d("zhy", "unSelected " + position);
    }
    
    public boolean setSelected(int position, T t) {
        return false;
    }
    
    
}
