package com.haoke91.a91edu.ui.user;

import android.content.Context;
import android.content.Intent;

import com.gaosiedu.live.sdk.android.api.user.balance.record.LiveUserBalanceRecordRequest;
import com.gaosiedu.live.sdk.android.api.user.balance.record.LiveUserBalanceRecordResponse;
import com.google.gson.Gson;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.BalanceRecordAdapter;
import com.haoke91.a91edu.adapter.DailyWorkAdapter;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseLoadMoreActivity;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.baselibrary.recycleview.HorizontalDividerItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.Arrays;
import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/24 下午3:29
 * 修改人：weiyimeng
 * 修改时间：2018/7/24 下午3:29
 * 修改备注：
 */
public class BalanceRecordActivity extends BaseLoadMoreActivity {
    //  private WrapRecyclerView wr_daily_work;
    private BalanceRecordAdapter searchResultAdapter;
    
    public static void start(Context context) {
        Intent intent = new Intent(context, BalanceRecordActivity.class);
        context.startActivity(intent);
    }
    
    
    @Override
    public void initialize() {
        super.initialize();
        empty_view.builder().setEmptyText("没有余额信息")
        .setEmptyDrawable(R.mipmap.empty_image);
        searchResultAdapter = new BalanceRecordAdapter(this, null);
        rv_story_list.setAdapter(searchResultAdapter);
        rv_story_list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(
            getResources().getColor(R.color.FBFBFB))
            .size((int) getResources().getDimension(R.dimen.dp_1))
            .build());
    }
    
    private int num = 1;
    
    @Override
    protected void initEvent() {
        onRefresh(refreshLayout);
    }
    
    @Override
    public void onLoadMore(final RefreshLayout refreshLayout) {
        LiveUserBalanceRecordRequest request = new LiveUserBalanceRecordRequest();
        request.setUserId(UserManager.getInstance().getUserId());
        num++;
        request.setPageNum(num);
        Api.getInstance().post(request, LiveUserBalanceRecordResponse.class, new ResponseCallback<LiveUserBalanceRecordResponse>() {
            @Override
            public void onResponse(LiveUserBalanceRecordResponse date, boolean isFromCache) {
                //                String s = new Gson().toJson(date);
                searchResultAdapter.addAll(date.getData().getList());
                if (date.getData().isLastPage()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }
            
            @Override
            public void onEmpty(LiveUserBalanceRecordResponse date, boolean isFromCache) {
                refreshLayout.finishLoadMoreWithNoMoreData();
                
            }
        }, "");
    }
    
    
    @Override
    public void onRefresh(final RefreshLayout refreshLayout) {
        num = 1;
        LiveUserBalanceRecordRequest request = new LiveUserBalanceRecordRequest();
        request.setUserId(UserManager.getInstance().getUserId());
        request.setPageNum(num);
        Api.getInstance().post(request, LiveUserBalanceRecordResponse.class, new ResponseCallback<LiveUserBalanceRecordResponse>() {
            @Override
            public void onResponse(LiveUserBalanceRecordResponse date, boolean isFromCache) {
                empty_view.showContent();
                //                String s = new Gson().toJson(date);
                searchResultAdapter.setData(date.getData().getList());
                if (date.getData().isLastPage()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
                refreshLayout.finishRefresh();
                
            }
            
            @Override
            public void onEmpty(LiveUserBalanceRecordResponse date, boolean isFromCache) {
                empty_view.showEmpty();
                refreshLayout.finishRefresh();
                
            }
            
            @Override
            public void onError() {
                empty_view.showError();
            }
        }, "");
    }
}
