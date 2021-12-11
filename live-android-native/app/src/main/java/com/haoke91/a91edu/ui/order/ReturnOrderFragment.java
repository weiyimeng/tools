package com.haoke91.a91edu.ui.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.ObjectUtils;
import com.gaosiedu.live.sdk.android.api.user.order.returned.list.LiveUserOrderReturnedListRequest;
import com.gaosiedu.live.sdk.android.api.user.order.returned.list.LiveUserOrderReturnedListResponse;
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain;
import com.gaosiedu.live.sdk.android.domain.OrderReturnItemDomain;
import com.google.gson.Gson;
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

import java.math.BigDecimal;
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
public class ReturnOrderFragment extends BaseLoadMoreFragment {
    public static final String ORDER_TYPE = "orderType";
    public static final String all_order = "all"; //所有
    public static final String wait_pay = "unPay"; //待支付
    public static final String have_pay = "payed"; //已支付
    public static final String cancel_order = "cancel"; //已取消
    public static final String back_order = "refund";//已退款
    
    public static ReturnOrderFragment newInstance(String orderType) {
        ReturnOrderFragment fragment = new ReturnOrderFragment();
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
    
    
    private List<MulitOrderBean> mulitOrderBeans;
    private OrderAdapter orderAdapter;
    private int pageNum = 1; //当前页码
    
    @Override
    public void fetchData() {
        registeredEvent();
        empty_view.builder().setEmptyDrawable(R.mipmap.empty_image);
        LiveUserOrderReturnedListRequest liveUserOrderListRequest = new LiveUserOrderReturnedListRequest();
        liveUserOrderListRequest.setUserId(UserManager.getInstance().getUserId());
        //        liveUserOrderListRequest.setSearchStatus(orderType);
        liveUserOrderListRequest.setPageNum(pageNum);
        mulitOrderBeans = new ArrayList<>();
        orderAdapter = new OrderAdapter(mContext, mulitOrderBeans);
        rv_list.setAdapter(orderAdapter);
        orderAdapter.setType("back");
        empty_view.showLoading();
        Api.getInstance().post(liveUserOrderListRequest, LiveUserOrderReturnedListResponse.class, new ResponseCallback<LiveUserOrderReturnedListResponse>() {
            @Override
            public void onResponse(LiveUserOrderReturnedListResponse date, boolean isFromCache) {
                //                                String s = new Gson().toJson(date);
                setOrderDate(date);
                empty_view.showContent();
                if (date.getData().isLastPage()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
            
            @Override
            public void onEmpty(LiveUserOrderReturnedListResponse date, boolean isFromCache) {
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
                switch (mulitOrderBeans.get(position).getDate().status) {
                    case 15:
                        OrderDetailBackActivity.Companion.start(mContext, AllOrderFragment.back_order_ing, String.valueOf(mulitOrderBeans.get(position).getDate().returnNo));
                        break;
                    case 16:
                        OrderDetailBackActivity.Companion.start(mContext, AllOrderFragment.back_order, String.valueOf(mulitOrderBeans.get(position).getDate().returnNo));
                        break;
                    case 12:
                        OrderDetailBackActivity.Companion.start(mContext, AllOrderFragment.wait_back, String.valueOf(mulitOrderBeans.get(position).getDate().returnNo));
                        break;
                    case 10:
                        OrderDetailBackActivity.Companion.start(mContext, AllOrderFragment.reject_back, String.valueOf(mulitOrderBeans.get(position).getDate().returnNo));
                        break;
                    case 14:
                        OrderDetailBackActivity.Companion.start(mContext, AllOrderFragment.check_back, String.valueOf(mulitOrderBeans.get(position).getDate().returnNo));
                        break;
                    case 13:
                        OrderDetailBackActivity.Companion.start(mContext, AllOrderFragment.cancel_back, String.valueOf(mulitOrderBeans.get(position).getDate().returnNo));
                        break;
                }
                
            }
            
            //                if (wait_pay.equalsIgnoreCase(orderType) || have_pay.equalsIgnoreCase(orderType)) {
            //                    OrderDetailPayActivity.start(mContext, orderType);
            //                } else if (back_order.equalsIgnoreCase(orderType)) {
            //                    OrderDetailBackActivity.start(mContext, orderType);
            //                } else if (cancel_order.equalsIgnoreCase(orderType)) {
            //                    OrderDetailCancelActivity.start(mContext, orderType);
            //                }
            //        }
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
                if (messageItem.getType() == MessageItem.order_change && (orderType.equalsIgnoreCase(messageItem.getMessage()))) {
                    refreshLayout.autoRefresh();
                }
            }
        });
        
    }
    
    /**
     * @param date //     * @param type 1 onLoadMore 2 onRefresh  3 init
     */
    private void setOrderDate(LiveUserOrderReturnedListResponse date) {
        empty_view.showContent();
        if (ObjectUtils.isEmpty(date.getData().getList())) {
            return;
        }
        for (LiveUserOrderReturnedListResponse.ListData orderItem : date.getData().getList()) {
            MulitOrderBean<MulitOrderBean.OrderHead> orderHeadBean = new MulitOrderBean(MulitOrderBean.head);
            MulitOrderBean.OrderHead orderHead = new MulitOrderBean.OrderHead();
            orderHead.orderNo = orderItem.getOrderReturnNo();
            orderHead.status = getReturnOrderStaus(orderItem.getStatus());
            orderHead.returnNo = orderItem.getId();
            orderHeadBean.setDate(orderHead);
            mulitOrderBeans.add(orderHeadBean);
            List<OrderReturnItemDomain> orderItemList = orderItem.getRefundItems();
            if (!ObjectUtils.isEmpty(orderItemList)) {
                for (OrderReturnItemDomain courserItem : orderItemList) {
                    MulitOrderBean<OrderItemDomain> orderBodyBean = new MulitOrderBean(MulitOrderBean.body);
                    OrderItemDomain orderItemDomain = new OrderItemDomain();
                    orderItemDomain.orderNo = orderItem.getOrderReturnNo();
                    orderItemDomain.status = getReturnOrderStaus(orderItem.getStatus());
                    orderItemDomain.returnNo = orderItem.getId();
                    
                    orderItemDomain.setCourse(courserItem.getCourseDomain());
                    orderBodyBean.setDate(orderItemDomain);
                    mulitOrderBeans.add(orderBodyBean);
                }
            }
            MulitOrderBean<MulitOrderBean.OrderTail> orderTailBean = new MulitOrderBean(MulitOrderBean.tail);
            MulitOrderBean.OrderTail orderTail = new MulitOrderBean.OrderTail();
            //            if (orderItem.getOldDueAmount() != null) {
            if (ObjectUtils.isEmpty(orderItem.getReturnPrice())) {
                orderTail.money = BigDecimal.ZERO;
            } else {
                orderTail.money = orderItem.getReturnPrice();
            }
            orderTail.status = getReturnOrderStaus(orderItem.getStatus());
            orderTail.returnNo = orderItem.getId();
            orderTail.orderNo = orderItem.getOrderReturnNo();
            
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
    
    /**
     * status  0过期待退款 1未过期待退款  2退款完成 3转账中 -1驳回
     *
     * @param oristatus
     * @return
     * @魏艺蒙 退货状态（0：待审核，1：同意（退货中），2：已退货 ,-1：驳回，3：用户取消退款)
     */
    private int getReturnOrderStaus(int oristatus) {
        int status = 0;
        switch (oristatus) {
            case 3:
                status = 13;
                break;
            case 2:
                status = 16;
                break;
            case -1:
                status = 10;
                break;
            case 0:
                status = 14;
                break;
            case 1:
                status = 15;
                break;
        }
        return status;
    }
    
    @Override
    protected void onLoadMore() {
        
        LiveUserOrderReturnedListRequest liveUserOrderListRequest = new LiveUserOrderReturnedListRequest();
        liveUserOrderListRequest.setUserId(UserManager.getInstance().getUserId());
        //        liveUserOrderListRequest.setSearchStatus(orderType);
        pageNum++;
        liveUserOrderListRequest.setPageNum(pageNum);
        Api.getInstance().post(liveUserOrderListRequest, LiveUserOrderReturnedListResponse.class, new ResponseCallback<LiveUserOrderReturnedListResponse>() {
            @Override
            public void onResponse(LiveUserOrderReturnedListResponse date, boolean isFromCache) {
                //                String s = new Gson().toJson(date);
                //                ArrayList<MulitOrderBean> loadMoreDate = new ArrayList<>();
                setOrderDate(date);
                refreshLayout.finishLoadMore();
                if (date.getData().isLastPage()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
            
            @Override
            public void onEmpty(LiveUserOrderReturnedListResponse date, boolean isFromCache) {
                refreshLayout.finishLoadMoreWithNoMoreData();
                
            }
        }, "");
    }
    
    @Override
    protected void onRefresh() {
        LiveUserOrderReturnedListRequest liveUserOrderListRequest = new LiveUserOrderReturnedListRequest();
        liveUserOrderListRequest.setUserId(UserManager.getInstance().getUserId());
        //        liveUserOrderListRequest.setSearchStatus(orderType);
        pageNum = 1;
        liveUserOrderListRequest.setPageNum(pageNum);
        Api.getInstance().post(liveUserOrderListRequest, LiveUserOrderReturnedListResponse.class, new ResponseCallback<LiveUserOrderReturnedListResponse>() {
            @Override
            public void onResponse(LiveUserOrderReturnedListResponse date, boolean isFromCache) {
                //                String s = new Gson().toJson(date);
                //                ArrayList<MulitOrderBean> loadMoreDate = new ArrayList<>();
                mulitOrderBeans.clear();
                setOrderDate(date);
                refreshLayout.finishRefresh();
            }
        }, "");
        
    }
}
