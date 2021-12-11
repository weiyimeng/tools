package com.haoke91.a91edu.ui.learn;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.gaosiedu.scc.sdk.android.api.user.task.complete.list.SccGetUserTasksCompleteListRequest;
import com.gaosiedu.scc.sdk.android.api.user.task.complete.list.SccGetUserTasksCompleteListResponse;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.DailyWorkAdapter;
import com.haoke91.a91edu.adapter.DailyWorkHistoryAdapter;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseLoadMoreActivity;
import com.haoke91.a91edu.ui.user.BalanceRecordActivity;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
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
public class DailyWorkHistoryActivity extends BaseLoadMoreActivity {
    //  private WrapRecyclerView wr_daily_work;
    private static final int INITSTART = 1;
    private static int mCurrentPage;
    DailyWorkHistoryAdapter mDailyWorkHistoryAdapter;
    
    public static void start(Context context){
        Intent intent = new Intent(context, DailyWorkHistoryActivity.class);
        context.startActivity(intent);
    }
    
    //    @Override
    //    public int getLayout() {
    //        return R.layout.activit_daily_work;
    //    }
    
    @Override
    public void initialize(){
        super.initialize();
        empty_view.showContent();
        empty_view.builder().setEmptyText("这里什么都没有")
            .setEmptyDrawable(R.mipmap.empty_image);
        mDailyWorkHistoryAdapter = new DailyWorkHistoryAdapter(this, new ArrayList<SccGetUserTasksCompleteListResponse.ListData>());
        rv_story_list.setAdapter(mDailyWorkHistoryAdapter);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.autoRefresh(200);
    }
    
    @Override
    public void onRefresh(RefreshLayout refreshLayout){
        super.onRefresh(refreshLayout);
        mCurrentPage = INITSTART;
        refreshLayout.setEnableLoadMore(true);
        networkForHistoryList(mCurrentPage, false);
    }
    
    @Override
    public void onLoadMore(RefreshLayout refreshLayout){
        super.onLoadMore(refreshLayout);
        mCurrentPage++;
        networkForHistoryList(mCurrentPage, true);
    }
    
    /**
     * 获取历史任务列表
     *
     * @param page 分页页码
     */
    public void networkForHistoryList(int page, final boolean isLoadMore){
        SccGetUserTasksCompleteListRequest request = new SccGetUserTasksCompleteListRequest();
        request.setUserId(UserManager.getInstance().getUserId() + "");
        request.setPageNum(page);
        request.setPageSize(10);
        Api.getInstance().postScc(request, SccGetUserTasksCompleteListResponse.class, new ResponseCallback<SccGetUserTasksCompleteListResponse>() {
            @Override
            public void onResponse(SccGetUserTasksCompleteListResponse date, boolean isFromCache){
                List<SccGetUserTasksCompleteListResponse.ListData> list = date.getData().getList();
                if (isLoadMore){
                    mDailyWorkHistoryAdapter.addAll(list);
                } else{
                    mDailyWorkHistoryAdapter.setData(list);
                    refreshLayout.finishRefresh();
                }
                if (date.getData().isLastPage()){
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else{
                    refreshLayout.finishLoadMore();
                }
            }
            
            @Override
            public void onEmpty(SccGetUserTasksCompleteListResponse date, boolean isFromCache){
                super.onEmpty(date, isFromCache);
                if (!isLoadMore){
                    empty_view.showEmpty();
                }
            }
            
            @Override
            public void onError(){
                super.onError();
                if (!isLoadMore){
                    empty_view.showError();
                }
            }
        }, "goto get history list");
        
    }
    
}
