package com.haoke91.a91edu.ui;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;

import com.haoke91.a91edu.R;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.baselibrary.views.emptyview.EmptyView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/9 上午9:11
 * 修改人：weiyimeng
 * 修改时间：2018/8/9 上午9:11
 * 修改备注：
 */
public class BaseLoadMoreActivity extends BaseActivity {
    
    protected WrapRecyclerView rv_story_list;
    protected EmptyView empty_view;
    public SmartRefreshLayout refreshLayout;
    
    @Override
    public int getLayout(){
        return R.layout.activity_base_loadmore;
    }
    
    @Override
    public void initialize(){
        empty_view = findViewById(R.id.empty_view);
        empty_view.showLoading();
        refreshLayout = findViewById(R.id.refreshLayout);
        rv_story_list = findViewById(R.id.rv_story_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_story_list.setLayoutManager(layoutManager);
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        refreshLayout.setOnRefreshListener(refreshlistener);
        refreshLayout.setOnLoadMoreListener(loadmoreListener);
        
    }
    
    private OnRefreshListener refreshlistener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull final RefreshLayout refreshLayout){
            BaseLoadMoreActivity.this.onRefresh(refreshLayout);
        }
    };
    private OnLoadMoreListener loadmoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull final RefreshLayout refreshLayout){
            BaseLoadMoreActivity.this.onLoadMore(refreshLayout);
        }
    };
    
    public void onRefresh(RefreshLayout refreshLayout){
    
    }
    
    public void onLoadMore(RefreshLayout refreshLayout){
    
    }
}
