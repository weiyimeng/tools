package com.haoke91.a91edu.ui.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.haoke91.a91edu.R;
import com.haoke91.a91edu.fragment.BaseFragment;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.baselibrary.views.emptyview.EmptyView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/10 下午6:49
 * 修改人：weiyimeng
 * 修改时间：2018/7/10 下午6:49
 * 修改备注：
 */
public class BaseLoadMoreFragment extends BaseFragment {
    protected WrapRecyclerView rv_list;
    protected EmptyView empty_view;
    public SmartRefreshLayout refreshLayout;
    protected View gotoTopBtn;
    
    @Override
    public int getLayout() {
        return R.layout.fragment_base_order;
    }
    
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
    }
    
    private void initView(View view) {
        empty_view = view.findViewById(R.id.emptyView);
        gotoTopBtn = view.findViewById(R.id.gotoTopBtn);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        rv_list = view.findViewById(R.id.rv_story_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(layoutManager);
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        //        rv_list.setHasFixedSize(true);
        //        rv_list.setNestedScrollingEnabled(false);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(loadMoreListener);
        refreshLayout.setOnRefreshListener(refreshListener);
        gotoTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTop = false;
                layoutManager.smoothScrollToPosition(rv_list, null, 0);
                gotoTopBtn.setVisibility(View.GONE);
            }
        });
        
        rv_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int scrollState = recyclerView.getScrollState();
                if (layoutManager.findFirstVisibleItemPosition() >= 4) {
                    if (toTop) {
                        gotoTopBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    gotoTopBtn.setVisibility(View.GONE);
                }
                
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    toTop = true;
                }
            }
        });
    }
    
    private boolean toTop = true;
    //
    private OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
            BaseLoadMoreFragment.this.onRefresh();
            //            new Handler().postDelayed(new Runnable() {
            //                @Override
            //                public void run() {
            //                    refreshLayout.finishRefresh();
            //
            //                }
            //            }, 100);
        }
    };
    private OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
            BaseLoadMoreFragment.this.onLoadMore();
            //            new Handler().postDelayed(new Runnable() {
            //                @Override
            //                public void run() {
            //                    refreshLayout.finishLoadMoreWithNoMoreData();
            //
            //                }
            //            }, 100);
        }
    };
    
    
    protected void onRefresh() {
    
    }
    
    protected void onLoadMore() {
    
    }
}
