package com.haoke91.a91edu.ui.liveroom.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.drakeet.multitype.ItemViewBinder;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/2 下午4:10
 * 修改人：weiyimeng
 * 修改时间：2018/7/2 下午4:10
 * 修改备注：
 */
public class BaseMessageProvider<C, V extends RecyclerView.ViewHolder> extends ItemViewBinder<C, V> {
    protected MultiMessageAdapter adapter;
    
    public BaseMessageProvider() {
    }
    
    public BaseMessageProvider(MultiMessageAdapter adapter) {
        this.adapter = adapter;
    }
    
    @NonNull
    @Override
    protected V onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return null;
    }
    
    @Override
    protected void onBindViewHolder(@NonNull V holder, @NonNull C c) {
    
    }
}
