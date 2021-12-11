package com.haoke91.a91edu.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.ui.course.SearchActivity;

import java.util.ArrayList;

import me.drakeet.multitype.ItemViewBinder;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/2 上午11:58
 * 修改人：weiyimeng
 * 修改时间：2018/7/2 上午11:58
 * 修改备注：
 */
public abstract class SearchTittleProvider extends ItemViewBinder<String, SearchTittleProvider.ViewHolder> {
    public SearchTittleProvider() {
        super();
    }
    
    @NonNull
    @Override
    protected SearchTittleProvider.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_search_tittle, parent, false);
        return new ViewHolder(root);
        
    }
    
    @Override
    protected void onBindViewHolder(@NonNull final SearchTittleProvider.ViewHolder holder, @NonNull String s) {
        holder.iv_search_tittle.setText(s);
        holder.iv_search_tittle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("" + holder.getAdapterPosition());
            }
        });
        if ("历史搜索".equalsIgnoreCase(s)) {
            holder.iv_search_delete.setVisibility(View.VISIBLE);
            holder.iv_search_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.iv_search_delete.setVisibility(View.GONE);
                    CacheDiskUtils.getInstance().put(SearchActivity.search_history, new ArrayList<>());
                    onDelete(holder.getAdapterPosition());
                }
            });
        } else {
            holder.iv_search_delete.setVisibility(View.GONE);
        }
        
    }
    
    public abstract void onDelete(int position);
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView iv_search_tittle;
        ImageView iv_search_delete;
        
        public ViewHolder(View itemView) {
            super(itemView);
            iv_search_tittle = itemView.findViewById(R.id.iv_search_tittle);
            iv_search_delete = itemView.findViewById(R.id.iv_search_delete);
            
        }
    }
}
