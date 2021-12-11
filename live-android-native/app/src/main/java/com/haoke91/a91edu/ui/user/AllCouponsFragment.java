package com.haoke91.a91edu.ui.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.gaosiedu.live.sdk.android.api.user.coupon.list.LiveUserCouponListRequest;
import com.gaosiedu.live.sdk.android.api.user.coupon.list.LiveUserCouponListResponse;
import com.google.gson.Gson;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.MyCouponsAdapter;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.order.BaseLoadMoreFragment;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.baselibrary.recycleview.adapter.BaseQuickWithPositionAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/10 下午7:03
 * 修改人：weiyimeng
 * 修改时间：2018/7/10 下午7:03
 * 修改备注：
 */
public class AllCouponsFragment extends BaseLoadMoreFragment {
    public static final String TYPE = "Type";
    public static final String not_use = "1";  //未使用
    public static final String used = "3";//已使用
    public static final String cant_use = "2";//已过期
    
    public static AllCouponsFragment newInstance(String orderType) {
        AllCouponsFragment fragment = new AllCouponsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, orderType);
        
        fragment.setArguments(bundle);
        return fragment;
    }
    
    /**
     * 3 未使用
     * 4 已使用
     * 5 已过期
     */
    private String orderType;
    private MyCouponsAdapter orderAdapter;
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        orderType = getArguments().getString(TYPE);
        super.onViewCreated(view, savedInstanceState);
        
    }
    
    @Override
    public void fetchData() {
        empty_view.showLoading();
        empty_view.builder().setEmptyDrawable(R.mipmap.empty_image).setEmptyText(getString(R.string.no_coupons));
        int layoutId;
        if (not_use.equalsIgnoreCase(orderType) || used.equalsIgnoreCase(orderType)) {
            layoutId = R.layout.item_my_conpans;
        } else {
            layoutId = R.layout.item_my_conpans_end;
        }
        orderAdapter = new MyCouponsAdapter(mContext, layoutId, null);
        LiveUserCouponListRequest request = new LiveUserCouponListRequest();
        request.setUserId(UserManager.getInstance().getUserId());
        request.setStatus(Integer.parseInt(orderType));
        Api.getInstance().post(request, LiveUserCouponListResponse.class, new ResponseCallback<LiveUserCouponListResponse>() {
            @Override
            public void onResponse(LiveUserCouponListResponse date, boolean isFromCache) {
                empty_view.showContent();
                orderAdapter.setData(date.getData().getList());
            }
            
            @Override
            public void onEmpty(LiveUserCouponListResponse date, boolean isFromCache) {
                empty_view.showEmpty();
                
            }
            
            @Override
            public void onError() {
                empty_view.showError();
                
            }
        }, "");
        
        
        orderAdapter.setType(orderType);
        rv_list.setAdapter(orderAdapter);
    }
    
    public void addCoupon(LiveUserCouponListResponse.ListData data) {
        if (orderAdapter != null) {
            orderAdapter.add(data);
        }
    }
}
