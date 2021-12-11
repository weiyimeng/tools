package com.haoke91.a91edu.ui.homework;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gaosiedu.scc.sdk.android.api.user.exercise.list.LiveSccUserExerciseListRequest;
import com.gaosiedu.scc.sdk.android.api.user.exercise.list.LiveSccUserExerciseListResponse;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.HomeworkListAdapter;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.order.AllOrderFragment;
import com.haoke91.a91edu.ui.order.BaseLoadMoreFragment;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.baselibrary.event.MessageItem;
import com.haoke91.baselibrary.event.RxBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/25 下午4:46
 * 修改人：weiyimeng
 * 修改时间：2018/7/25 下午4:46
 * 修改备注：
 */
public class WaitUploadFragment extends BaseLoadMoreFragment {
    private static final String KEY = "type";
    public static final int wait = 1;
    public static final int checking = 2;
    public static final int checked = 3;
    private HomeworkListAdapter mHomeworkListAdapter;
    private int currentPage = 1;
    private int currentTag = wait;
    
    public static WaitUploadFragment newInstance(int type) {
        
        WaitUploadFragment fragment = new WaitUploadFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY, type);
        fragment.setArguments(bundle);
        return fragment;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        empty_view.builder().setEmptyDrawable(R.mipmap.empty_image_work)
            .setErrorDrawable(R.mipmap.net_error)
            .setErrorText("网络连接失败！")
            .setEmptyText("暂无作业哦 ~");
        
        int key = getArguments().getInt(KEY);
        currentTag = key;
        mHomeworkListAdapter = new HomeworkListAdapter(mContext, new ArrayList<LiveSccUserExerciseListResponse.ListData>(), key);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        
        rv_list.setLayoutManager(layoutManager);
        rv_list.setAdapter(mHomeworkListAdapter);
        empty_view.showLoading();
        //        networkForHomeWorkList(currentPage, false, key);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        initRxBus();
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public void fetchData() {
        //        refreshLayout.autoRefresh(200);
        onRefresh();
    }
    //    @Override
    //    public boolean prepareFetchData(){
    //        return prepareFetchData(true);
    //    }
    
    /**
     * 刷新
     */
    private void initRxBus() {
        RxBus.getIntanceBus().doSubscribe(MyHomeworkActivity.class, MessageItem.class, new Consumer<MessageItem>() {
            @Override
            public void accept(MessageItem item) throws Exception {
                if (item.getType() == MessageItem.REFRESH_HOMEWORK) {
                    if (WaitUploadFragment.this.isVisible()) {
                        refreshLayout.autoRefresh();
                    } else {
                        isDataInitiated = false;
                    }
                }
            }
        });
    }
    
    /**
     * 获取作业列表
     */
    private void networkForHomeWorkList(int page, final boolean isMore, int key) {
        LiveSccUserExerciseListRequest request = new LiveSccUserExerciseListRequest();
        request.setUserId(UserManager.getInstance().getUserId() + "");
        if (key == 1) {
            request.setStatus(LiveSccUserExerciseListRequest.TAG_UNSUBMITED);
        } else if (key == 2) {
            request.setStatus(LiveSccUserExerciseListRequest.TAG_CORRECTING);
        } else {
            request.setStatus(LiveSccUserExerciseListRequest.TAG_CORRECTED);
        }
        request.setPageNum(page);
        request.setPageSize(10);
        Api.getInstance().postScc(request, LiveSccUserExerciseListResponse.class, new ResponseCallback<LiveSccUserExerciseListResponse>() {
            @Override
            public void onResponse(LiveSccUserExerciseListResponse date, boolean isFromCache) {
                if (date.getData() == null) {
                    return;
                }
                List<LiveSccUserExerciseListResponse.ListData> list = date.getData().getList();
                if (list != null && list.size() > 0) {
                    empty_view.showContent();
                    if (isMore) {
                        mHomeworkListAdapter.addAll(list);
                        if (date.getData().isLastPage()) {
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                    } else {
                        mHomeworkListAdapter.setData(list);
                        refreshLayout.finishRefresh();
                    }
                } else {
                    empty_view.showEmpty();
                }
                if (date.getData().isLastPage()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }
            
            @Override
            public void onEmpty(LiveSccUserExerciseListResponse date, boolean isFromCache) {
                super.onEmpty(date, isFromCache);
                if (!isMore) {
                    empty_view.showEmpty();
                }
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }
            
            @Override
            public void onError() {
                super.onError();
                empty_view.showError();
                if (isMore) {
                    refreshLayout.finishLoadMore();
                } else {
                    refreshLayout.finishRefresh();
                }
            }
        }, "homework list");
        
    }
    
    @Override
    public void onRefresh() {
        super.onRefresh();
        currentPage = 1;
        //        refreshLayout.setEnableLoadMore(true);
        networkForHomeWorkList(currentPage, false, currentTag);
    }
    
    @Override
    public void onLoadMore() {
        super.onLoadMore();
        currentPage++;
        networkForHomeWorkList(currentPage, true, currentTag);
    }
}
