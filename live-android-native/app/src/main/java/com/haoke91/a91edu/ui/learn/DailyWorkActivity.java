package com.haoke91.a91edu.ui.learn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.gaosiedu.scc.sdk.android.api.user.task.list.SccGetUserTasksRequest;
import com.gaosiedu.scc.sdk.android.api.user.task.list.SccGetUserTasksResponse;
import com.gaosiedu.scc.sdk.android.api.user.task.sign.SccTaskSignRequest;
import com.gaosiedu.scc.sdk.android.api.user.task.sign.SccTaskSignResponse;
import com.gaosiedu.scc.sdk.android.domain.SccUserTaskList;
import com.google.gson.Gson;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.DailyWorkAdapter;
import com.haoke91.a91edu.adapter.SearchResultAdapter;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.ui.BaseLoadMoreActivity;
import com.haoke91.a91edu.ui.user.BalanceRecordActivity;
import com.haoke91.a91edu.ui.user.MyBalanceActivity;
import com.haoke91.a91edu.ui.user.UserImgActivity;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
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
public class DailyWorkActivity extends BaseLoadMoreActivity {
    
    DailyWorkAdapter mDailyWorkAdapter;
    
    public static void start(Context context){
        Intent intent = new Intent(context, DailyWorkActivity.class);
        context.startActivity(intent);
    }
    
    @Override
    public void initialize(){
        super.initialize();
        empty_view.builder().setEmptyText("这里什么都没有")
            .setErrorText("网络连接异常")
            .setEmptyDrawable(R.mipmap.empty_image);
        
        ImageView toolbar_more = findViewById(R.id.toolbar_more);
        toolbar_more.setBackgroundResource(R.drawable.ic_study_nav_icon_taskdetail);
        toolbar_more.setVisibility(View.VISIBLE);
        toolbar_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                DailyWorkHistoryActivity.start(DailyWorkActivity.this);
            }
        });
        empty_view.showLoading();
        //        List<String> strings = Arrays.asList("", "", "", "", "");
        mDailyWorkAdapter = new DailyWorkAdapter(this, new ArrayList<SccUserTaskList>());
        rv_story_list.setAdapter(mDailyWorkAdapter);
        refreshLayout.setEnableLoadMore(false);
        onRefresh(null);
    }
    
    @Override
    public void onRefresh(RefreshLayout refreshLayout){
        networkForList();
    }
    
    /**
     * for list of tasks
     */
    private void networkForList(){
        SccGetUserTasksRequest request = new SccGetUserTasksRequest();
        request.setUserId(String.valueOf(UserManager.getInstance().getUserId()));
        Api.getInstance().postScc(request, SccGetUserTasksResponse.class, new ResponseCallback<SccGetUserTasksResponse>() {
            @Override
            public void onResponse(SccGetUserTasksResponse date, boolean isFromCache){
                new Gson().toJson(date);
                refreshLayout.finishRefresh();
                empty_view.showContent();
                if (!Utils.isSuccess(date.getCode())){
                    empty_view.showEmpty();
                    return;
                }
                List<SccUserTaskList> list = date.getData().getList();
                mDailyWorkAdapter.setData(list);
                
            }
            
            @Override
            public void onEmpty(SccGetUserTasksResponse date, boolean isFromCache){
                super.onEmpty(date, isFromCache);
                empty_view.showEmpty();
            }
            
            @Override
            public void onError(){
                empty_view.showError();
            }
        }, "");
    }
    
}
