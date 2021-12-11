package com.haoke91.a91edu.ui.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.ObjectUtils;
import com.gaosiedu.live.sdk.android.api.user.order.list.LiveUserOrderListRequest;
import com.gaosiedu.live.sdk.android.api.user.order.list.LiveUserOrderListResponse;
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.OrderAdapter;
import com.haoke91.a91edu.entities.MulitOrderBean;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.baselibrary.event.MessageItem;
import com.haoke91.baselibrary.event.RxBus;
import com.haoke91.baselibrary.recycleview.adapter.BaseQuickWithPositionAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/10 下午7:03
 * 修改人：weiyimeng
 * 修改时间：2018/7/10 下午7:03
 * 修改备注：
 */
public class AllOrderFragment extends BaseLoadMoreFragment {
    public static final String ORDER_TYPE = "orderType";
    public static final String all_order = "all"; //所有
    public static final String wait_pay = "unPay"; //待支付
    public static final String have_pay = "payed"; //已支付
    public static final String cancel_order = "cancel"; //已取消
    public static final String back_order = "refund";//全部退款
    public static final String back_order_ing = "back_order_ing";//退款中
    public static final String back_order_some = "back_order_some";//部分退款
    public static final String wait_back = "wait_back";//待退款
    public static final String reject_back = "reject_back";//退款驳回
    public static final String check_back = "check_back";//退款审核中
    public static final String cancel_back = "cancel_back";//取消退款
    
    
    public static AllOrderFragment newInstance(String orderType) {
        AllOrderFragment fragment = new AllOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_TYPE, orderType);
        fragment.setArguments(bundle);
        return fragment;
    }
    
    /**
     * 1  全部
     * 2 待支付
     * 3 已支付
     * 4 已取消
     * 5 已退款
     */
    private String orderType;
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderType = getArguments().getString(ORDER_TYPE);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setEnableRefresh(true);
    }
    
    
    private ArrayList<MulitOrderBean> mulitOrderBeans;
    private OrderAdapter orderAdapter;
    private int pageNum = 1; //当前页码
    private long lastClickTime;
    
    @Override
    public void fetchData() {
        registeredEvent();
        empty_view.builder().setEmptyDrawable(R.mipmap.empty_image);
        LiveUserOrderListRequest liveUserOrderListRequest = new LiveUserOrderListRequest();
        liveUserOrderListRequest.setUserId(UserManager.getInstance().getUserId());
        liveUserOrderListRequest.setSearchStatus(orderType);
        liveUserOrderListRequest.setPageNum(pageNum);
        mulitOrderBeans = new ArrayList<>();
        orderAdapter = new OrderAdapter(mContext, mulitOrderBeans);
        rv_list.setAdapter(orderAdapter);
        empty_view.showLoading();
        Api.getInstance().post(liveUserOrderListRequest, LiveUserOrderListResponse.class, new ResponseCallback<LiveUserOrderListResponse>() {
            @Override
            public void onResponse(LiveUserOrderListResponse date, boolean isFromCache) {
                setOrderDate(date);
                if (date.getData().isLastPage()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
                
            }
            
            @Override
            public void onEmpty(LiveUserOrderListResponse date, boolean isFromCache) {
                empty_view.showEmpty();
            }
            
            @Override
            public void onError() {
                empty_view.showError();
            }
        }, "");
        orderAdapter.setOnItemClickListener(new BaseQuickWithPositionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // * status   0 未付款 1 已付款 -1  已取消 2支付中 3退款中 4全部退款 5部分退款 6换货中8换货完成9换货驳回
                if (System.currentTimeMillis() - lastClickTime < 500) {
                    return;
                }
                lastClickTime = System.currentTimeMillis();
                switch (mulitOrderBeans.get(position).getDate().status) {
                    case 0:
                    case 2:
                        //                        orderType = wait_pay;
                        OrderDetailPayActivity.Companion.start(mContext, wait_pay, mulitOrderBeans.get(position).getDate().orderNo);
                        break;
                    case 1:
                        //                        orderType = have_pay;
                        OrderDetailPayActivity.Companion.start(mContext, have_pay, mulitOrderBeans.get(position).getDate().orderNo);
                        break;
                    case 3:
                        OrderDetailPayActivity.Companion.start(mContext, back_order_ing, mulitOrderBeans.get(position).getDate().orderNo);
                        break;
                    case 4:
                        OrderDetailPayActivity.Companion.start(mContext, back_order, mulitOrderBeans.get(position).getDate().orderNo);
                        break;
                    case 5:
                        OrderDetailPayActivity.Companion.start(mContext, back_order_some, mulitOrderBeans.get(position).getDate().orderNo);
                        break;
                    case -1:
                        OrderDetailCancelActivity.Companion.start(mContext, orderType, mulitOrderBeans.get(position).getDate().orderNo);
                        break;
                    
                }
                
            }
        });
    }
    
    private void registeredEvent() {
        mRxBus = RxBus.getIntanceBus();
        mRxBus.doSubscribe(mContext.getClass(), MessageItem.class, new Consumer<MessageItem>() {
            @Override
            public void accept(MessageItem messageItem) throws Exception {
                if (ObjectUtils.isEmpty(messageItem)) {
                    return;
                }
                Logger.e("messageItem===" + messageItem.toString());
                if (messageItem.getType() == MessageItem.order_change && (orderType.equalsIgnoreCase(messageItem.getMessage()) || orderType.equalsIgnoreCase(all_order))) {
                    if (AllOrderFragment.this.isVisible()) {
                        refreshLayout.autoRefresh();
                    } else {
                        isDataInitiated = false;
                    }
                }
            }
        });
        
    }
    
    /**
     * @param date //     * @param type 1 onLoadMore 2 onRefresh  3 init
     */
    private void setOrderDate(LiveUserOrderListResponse date) {
        empty_view.showContent();
        if (date.getData().isLastPage()) {
            refreshLayout.finishLoadMoreWithNoMoreData();
        }
        if (ObjectUtils.isEmpty(date.getData().getList())) {
            return;
        }
        for (LiveUserOrderListResponse.ListData orderItem : date.getData().getList()) {
            MulitOrderBean<MulitOrderBean.OrderHead> orderHeadBean = new MulitOrderBean(MulitOrderBean.head);
            MulitOrderBean.OrderHead orderHead = new MulitOrderBean.OrderHead();
            orderHead.orderNo = orderItem.getOrderNo();
            orderHead.status = orderItem.getStatus();
            orderHeadBean.setDate(orderHead);
            mulitOrderBeans.add(orderHeadBean);
            List<OrderItemDomain> orderItemList = orderItem.getOrderItemList();
            if (!ObjectUtils.isEmpty(orderItemList)) {
                for (OrderItemDomain courserItem : orderItemList) {
                    MulitOrderBean<OrderItemDomain> orderBodyBean = new MulitOrderBean(MulitOrderBean.body);
                    courserItem.orderNo = orderItem.getOrderNo();
                    courserItem.status = orderItem.getStatus();
                    orderBodyBean.setDate(courserItem);
                    mulitOrderBeans.add(orderBodyBean);
                }
            }
            MulitOrderBean<MulitOrderBean.OrderTail> orderTailBean = new MulitOrderBean(MulitOrderBean.tail);
            MulitOrderBean.OrderTail orderTail = new MulitOrderBean.OrderTail();
            //待支付 orderItem
            //            orderItem.getDueAmount();
            if (0 == orderItem.getStatus() || 2 == orderItem.getStatus()) {
                orderTail.money = orderItem.getDueAmount();
            } else {
                orderTail.money = orderItem.getTotalAmount();
                
            }
            orderTail.status = orderItem.getStatus();
            orderTail.orderNo = orderItem.getOrderNo();
            
            orderTailBean.setDate(orderTail);
            mulitOrderBeans.add(orderTailBean);
            //            MulitOrderBean<> orderHeadBean = new MulitOrderBean<MulitOrderBean.OrderHead>(MulitOrderBean.head);
        }
        //        if (type == 3) {
        orderAdapter.setData(mulitOrderBeans);
        //        } else if (type == 1) {
        //            orderAdapter.addAll();
        //        }
    }
    
    @Override
    protected void onLoadMore() {
        
        LiveUserOrderListRequest liveUserOrderListRequest = new LiveUserOrderListRequest();
        liveUserOrderListRequest.setUserId(UserManager.getInstance().getUserId());
        liveUserOrderListRequest.setSearchStatus(orderType);
        pageNum++;
        liveUserOrderListRequest.setPageNum(pageNum);
        Api.getInstance().post(liveUserOrderListRequest, LiveUserOrderListResponse.class, new ResponseCallback<LiveUserOrderListResponse>() {
            @Override
            public void onResponse(LiveUserOrderListResponse date, boolean isFromCache) {
                //                String s = new Gson().toJson(date);
                //                ArrayList<MulitOrderBean> loadMoreDate = new ArrayList<>();
                setOrderDate(date);
                refreshLayout.finishLoadMore();
                if (date.getData().isLastPage()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
            
            @Override
            public void onEmpty(LiveUserOrderListResponse date, boolean isFromCache) {
                refreshLayout.finishLoadMoreWithNoMoreData();
            }
        }, "");
    }
    
    @Override
    protected void onRefresh() {
        LiveUserOrderListRequest liveUserOrderListRequest = new LiveUserOrderListRequest();
        liveUserOrderListRequest.setUserId(UserManager.getInstance().getUserId());
        liveUserOrderListRequest.setSearchStatus(orderType);
        pageNum = 1;
        liveUserOrderListRequest.setPageNum(pageNum);
        Api.getInstance().post(liveUserOrderListRequest, LiveUserOrderListResponse.class, new ResponseCallback<LiveUserOrderListResponse>() {
            @Override
            public void onResponse(LiveUserOrderListResponse date, boolean isFromCache) {
                //                String s = new Gson().toJson(date);
                //                ArrayList<MulitOrderBean> loadMoreDate = new ArrayList<>();
                mulitOrderBeans.clear();
                setOrderDate(date);
                refreshLayout.finishRefresh();
                if (date.getData().isLastPage()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
            
            @Override
            public void onEmpty(LiveUserOrderListResponse date, boolean isFromCache) {
                mulitOrderBeans.clear();
                empty_view.showEmpty();
                orderAdapter.notifyDataSetChanged();
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMoreWithNoMoreData();
                
            }
        }, "");
        
    }
}
