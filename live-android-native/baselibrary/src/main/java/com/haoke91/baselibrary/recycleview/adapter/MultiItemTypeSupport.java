package com.haoke91.baselibrary.recycleview.adapter;

/**
 * Created by yuliyan on 17/12/09.
 */
public interface MultiItemTypeSupport<T> {

    int getLayoutId(int viewType);

    int getItemViewType(int position, T t);

}
