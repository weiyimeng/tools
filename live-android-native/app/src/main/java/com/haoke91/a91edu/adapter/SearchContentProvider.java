package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haoke91.a91edu.R;
import com.haoke91.a91edu.widget.flowlayout.FlowLayout;
import com.haoke91.a91edu.widget.flowlayout.TagAdapter;
import com.haoke91.a91edu.widget.flowlayout.TagFlowLayout;

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
public abstract class SearchContentProvider extends ItemViewBinder<ArrayList, SearchContentProvider.ViewHolder> {
    private Context context;
    
    public SearchContentProvider(Context context) {
        super();
        this.context = context;
    }
    
    @NonNull
    @Override
    protected SearchContentProvider.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_search_content, parent, false);
        return new ViewHolder(root);
        
    }
    
    @Override
    protected void onBindViewHolder(@NonNull final SearchContentProvider.ViewHolder holder, @NonNull final ArrayList s) {
        holder.flowLayout.setAdapter(new TagAdapter<String>(s) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_search_content_text, holder.flowLayout, false);
                tv.setText(s);
                return tv;
            }
            
        });
        holder.flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                SearchContentProvider.this.onTagClick(view, position, (String) (s.get(position)));
                return false;
            }
        });
    }
    
    public abstract void onTagClick(View view, int position, String parent);
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TagFlowLayout flowLayout;
        
        public ViewHolder(View itemView) {
            super(itemView);
            flowLayout = (TagFlowLayout) itemView;
        }
    }
}
