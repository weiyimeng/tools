package com.haoke91.a91edu.ui.mycollect;

import com.gaosiedu.live.sdk.android.api.user.collection.list.LiveUserCollectionListRequest;
import com.gaosiedu.live.sdk.android.api.user.collection.list.LiveUserCollectionListResponse;
import com.google.gson.Gson;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.CollectCourseAdapter;
import com.haoke91.a91edu.adapter.OrderAdapter;
import com.haoke91.a91edu.adapter.SpecialClassAdapter;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.order.BaseLoadMoreFragment;
import com.haoke91.a91edu.utils.manager.UserManager;

import java.util.Arrays;
import java.util.List;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/22 下午6:06
 * 修改人：weiyimeng
 * 修改时间：2018/8/22 下午6:06
 * 修改备注：
 */
public class MyCollectCourseFragment extends BaseLoadMoreFragment {
    
    private CollectCourseAdapter adapter;
    private int num = 1;
    
    @Override
    public void fetchData(){
        empty_view.showLoading();
        empty_view.builder().setEmptyDrawable(R.mipmap.empty_image).setEmptyText(getString(R.string.no_collect));
        adapter = new CollectCourseAdapter(mContext, null);
        rv_list.setAdapter(adapter);
        LiveUserCollectionListRequest request = new LiveUserCollectionListRequest();
        request.setUserId(UserManager.getInstance().getUserId());
        request.setPageNum(num);
        request.setType(1);
        Api.getInstance().post(request, LiveUserCollectionListResponse.class, new ResponseCallback<LiveUserCollectionListResponse>() {
            @Override
            public void onResponse(LiveUserCollectionListResponse date, boolean isFromCache){
                //                new Gson().toJson(date);
                empty_view.showContent();
                adapter.setData(date.getData().getList());
            }
            
            @Override
            public void onEmpty(LiveUserCollectionListResponse date, boolean isFromCache){
                empty_view.showEmpty();
            }
            
            @Override
            public void onError(){
                empty_view.showError();
            }
        }, "");
        
        
    }
    
    @Override
    protected void onLoadMore(){
        LiveUserCollectionListRequest request = new LiveUserCollectionListRequest();
        request.setUserId(UserManager.getInstance().getUserId());
        request.setPageNum(num++);
        request.setType(1);
        Api.getInstance().post(request, LiveUserCollectionListResponse.class, new ResponseCallback<LiveUserCollectionListResponse>() {
            @Override
            public void onResponse(LiveUserCollectionListResponse date, boolean isFromCache){
                refreshLayout.finishLoadMore();
                adapter.addAll(date.getData().getList());
                
            }
            
            @Override
            public void onEmpty(LiveUserCollectionListResponse date, boolean isFromCache){
                refreshLayout.finishLoadMoreWithNoMoreData();
            }
            
            @Override
            public void onError(){
                refreshLayout.finishLoadMore();
            }
        }, "");
        
    }
}
