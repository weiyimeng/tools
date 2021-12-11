package com.haoke91.a91edu.ui.order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gaosiedu.live.sdk.android.api.order.create.LiveOrderCreateRequest;
import com.gaosiedu.live.sdk.android.api.order.create.LiveOrderCreateResponse;
import com.gaosiedu.live.sdk.android.api.order.pre.LivePreCreateOrderResponse;
import com.gaosiedu.live.sdk.android.api.user.address.list.LiveUserAddressListResponse;
import com.gaosiedu.live.sdk.android.api.user.balance.info.LiveUserBalanceInfoRequest;
import com.gaosiedu.live.sdk.android.api.user.balance.info.LiveUserBalanceInfoResponse;
import com.gaosiedu.live.sdk.android.domain.CourseCouponDomain;
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.ConfirmOrderClassAdapter;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.ui.address.AddressManagerActivity;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.NoDoubleClickListener;
import com.haoke91.baselibrary.event.RxBus;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.baselibrary.views.TipDialog;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang3.text.StrBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/22 下午7:53
 * 修改人：weiyimeng
 * 修改时间：2018/7/22 下午7:53
 * 修改备注：
 */
public class ConfirmOrderActivity extends BaseActivity {
    private static final String DATE = "date";
    //    private ImageView iv_order_choice_address;
    
    private TextView tv_order_left_count, tv_coupon_use, tv_order_recipient, tv_pay_count, tv_order_city, tv_order_address;
    // private RelativeLayout rl_ali_pay, rl_wx_pay;
    private CheckBox cb_balance_pay;
    private TextView tv_balance_save;
    
    public static void start(Context context, LivePreCreateOrderResponse.ResultData date) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        intent.putExtra(DATE, date);
        context.startActivity(intent);
    }
    
    public static void start(Context context) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        context.startActivity(intent);
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_confirmorder;
    }
    
    ////订单数据
    private LivePreCreateOrderResponse.ResultData date;
    
    
    @Override
    public void initialize() {
        date = (LivePreCreateOrderResponse.ResultData) getIntent().getSerializableExtra(DATE);
        if (ObjectUtils.isEmpty(date)) {
            return;
        }
        ViewGroup cl_choice_address = findViewById(R.id.cl_choice_address);
        tv_balance_save = findViewById(R.id.tv_balance_save);
        tv_order_city = findViewById(R.id.tv_order_city);
        tv_order_recipient = findViewById(R.id.tv_order_recipient);
        tv_order_address = findViewById(R.id.tv_order_address);
        TextView tv_activity_save = findViewById(R.id.tv_activity_save);
        tv_activity_save.setText("¥" + date.getActivityAmount().abs().setScale(2, BigDecimal.ROUND_DOWN));
        //        cb_ali_pay = findViewById(R.id.cb_ali_pay);
        ViewGroup vg_balance_pay = findViewById(R.id.vg_blance_pay);
        vg_balance_pay.setOnClickListener(OnClickListener);
        cb_balance_pay = findViewById(R.id.cb_balance_pay);
        //        cb_wx_pay = findViewById(R.id.cb_wx_pay);
        WrapRecyclerView rv_order_list = findViewById(R.id.rv_order_list);
        tv_pay_count = findViewById(R.id.tv_pay_count);
        originPayMoney = ObjectUtils.isEmpty(date.getOrderAmount()) ? BigDecimal.ZERO : date.getOrderAmount();
        activitySave = ObjectUtils.isEmpty(date.getActivityAmount()) ? BigDecimal.ZERO : date.getActivityAmount();
        tv_coupon_use = findViewById(R.id.tv_coupon_use);
        ((TextView) findViewById(R.id.tv_order_count)).setText(String.valueOf(date.getOrderItemList().size()));
        ((TextView) findViewById(R.id.tv_order_money)).setText("¥" + date.getOrderAmount().setScale(2, BigDecimal.ROUND_DOWN));
        rv_order_list.setHasFixedSize(true);
        rv_order_list.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_order_list.setLayoutManager(layoutManager);
        RelativeLayout rl_order_coupons = findViewById(R.id.rl_order_choice_coupons);
        TextView tv_order_pay = findViewById(R.id.tv_order_pay);
        tv_order_left_count = findViewById(R.id.tv_order_left_count);
        findViewById(R.id.toolbar_back).setOnClickListener(OnClickListener);
        //        iv_order_choice_address = findViewById(R.id.iv_order_choice_address);
        //        iv_order_choice_address.setOnClickListener(OnClickListener);
        tv_pay_count.setText("¥" + calculateTotalMoney());
        
        tv_order_left_count.setText(getString(R.string.left_count, "0", "0"));
        
        if (date.isNeedAddress()) {
            cl_choice_address.setVisibility(View.VISIBLE);
            cl_choice_address.setOnClickListener(OnClickListener);
            findViewById(R.id.tv_order_statement).setVisibility(View.VISIBLE);
            if (ObjectUtils.isEmpty(date.getOrderAddressDomain())) {
                findViewById(R.id.tv_order_noAddress).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_order_noAddress).setOnClickListener(OnClickListener);
            } else {
                addressDate = new LiveUserAddressListResponse.ListData();
                addressDate.setId(date.getOrderAddressDomain().getId());
                tv_order_city.setText(new StringBuffer().append(date.getOrderAddressDomain().getCity()).append(" ").append(date.getOrderAddressDomain().getArea()).toString());
                tv_order_address.setText(date.getOrderAddressDomain().getAddress());
                tv_order_recipient.setText(new StringBuffer().append(date.getOrderAddressDomain().getContacts()).append(" ").append(date.getOrderAddressDomain().getContactNumber()));
            }
        } else {
            findViewById(R.id.tv_order_statement).setVisibility(View.GONE);
            cl_choice_address.setVisibility(View.GONE);
        }
        
        
        rl_order_coupons.setOnClickListener(OnClickListener);
        tv_order_pay.setOnClickListener(OnClickListener);
        ConfirmOrderClassAdapter confirmOrderClassAdapter = new ConfirmOrderClassAdapter(this, date.getOrderItemList());
        rv_order_list.setAdapter(confirmOrderClassAdapter);
        cb_balance_pay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BigDecimal total = calculateTotalMoney();
                if (total.doubleValue() >= 0) {
                    tv_pay_count.setText("¥" + total);
                } else {
                    tv_pay_count.setText("¥0.00");
                }
                
                if (isChecked) {
                    tv_balance_save.setText("¥" + getBalanceCast());
                } else {
                    tv_balance_save.setText("¥0.00");
                    
                }
            }
        });
        
        if (originPayMoney.compareTo(BigDecimal.ZERO) == 0) {
            vg_balance_pay.setVisibility(View.GONE);
            rl_order_coupons.setVisibility(View.GONE);
        } else {
            vg_balance_pay.setVisibility(View.VISIBLE);
            rl_order_coupons.setVisibility(View.VISIBLE);
        }
        //        getDefaultAddress();
        
        getBalance();
    }
    
    
    private BigDecimal originPayMoney = BigDecimal.ZERO;//商品原价格
    
    private BigDecimal balanceSave = BigDecimal.ZERO;//余额抵扣(实际)
    
    private BigDecimal activitySave = BigDecimal.ZERO;//活动抵扣
    
    private BigDecimal couponsSave = BigDecimal.ZERO;//优惠券抵扣
    
    private BigDecimal availBalance = BigDecimal.ZERO;//可用余额
    
    private LiveUserAddressListResponse.ListData addressDate;//收货地址信息
    
    private HashMap<Integer, CourseCouponDomain> couponMap;//优惠券集合
    
    
    private void getBalance() {
        LiveUserBalanceInfoRequest liveUserBalanceInfoRequest = new LiveUserBalanceInfoRequest();
        liveUserBalanceInfoRequest.setUserId(UserManager.getInstance().getUserId());
        Api.getInstance().post(liveUserBalanceInfoRequest, LiveUserBalanceInfoResponse.class, new ResponseCallback<LiveUserBalanceInfoResponse>() {
            @Override
            public void onResponse(LiveUserBalanceInfoResponse date, boolean isFromCache) {
                availBalance = date.getData().getAvailableBalance();
                if (ObjectUtils.isEmpty(availBalance)) {
                    availBalance = BigDecimal.ZERO;
                }
                BigDecimal payMoney = getBalanceLeft();
                if (payMoney.doubleValue() >= 0) {
                    balanceSave = ConfirmOrderActivity.this.originPayMoney;
                    tv_order_left_count.setText(getString(R.string.left_count, availBalance.toString(), payMoney.abs().toString()));
                    //                    ((TextView) findViewById(R.id.tv_balance_save)).setText("¥" + originPayMoney);
                    tv_pay_count.setText("¥" + 0.00);
                } else {
                    balanceSave = date.getData().getAvailableBalance();
                    tv_order_left_count.setText(getString(R.string.left_count, availBalance.toString(), "0.00"));
                    tv_pay_count.setText("¥" + payMoney);
                }
                if (cb_balance_pay.isChecked()) {
                    tv_balance_save.setText("¥" + getBalanceCast());
                } else {
                    tv_balance_save.setText("¥" + 0.00);
                    
                }
                cb_balance_pay.setChecked(true);
            }
        }, "");
    }
    
    
    @Override
    protected void registeredEvent() {
        mRxBus = RxBus.getIntanceBus();
        mRxBus.doSubscribe(ConfirmOrderActivity.class, LiveUserAddressListResponse.ListData.class, new Consumer<LiveUserAddressListResponse.ListData>() {
            @Override
            public void accept(LiveUserAddressListResponse.ListData messageItem) throws Exception {
                addressDate = messageItem;
                if (ObjectUtils.isEmpty(messageItem)) {
                    return;
                }
                findViewById(R.id.tv_order_noAddress).setVisibility(View.GONE);
                tv_order_city.setText(new StringBuffer().append(messageItem.getCity()).append(" ").append(messageItem.getArea()).toString());
                tv_order_address.setText(messageItem.getAddress());
                tv_order_recipient.setText(new StringBuffer().append(messageItem.getContactPeople()).append(" ").append(messageItem.getContactMobile()).toString());
            }
        });
    }
    
    private View.OnClickListener OnClickListener = new NoDoubleClickListener() {
        @Override
        public void onDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.rl_order_choice_coupons:
                    ArrayList<CourseCouponDomain> courseCouponDomains = new ArrayList<>();
                    List<CourseCouponDomain> couponList = date.getCouponList();//通用优惠券
                    if (!ObjectUtils.isEmpty(couponList)) {
                        for (CourseCouponDomain item : couponList) {
                            item.setCourseId(-1);
                            if (!ObjectUtils.isEmpty(couponMap) && couponMap.containsKey(item.getCourseId())) {
                                int couponId = couponMap.get(item.getCourseId()).getId();
                                if (couponId == item.getId()) {
                                    item.setCheck(true);
                                } else {
                                    item.setCheck(false);
                                }
                            } else {
                                item.setCheck(false);
                            }
                        }
                        CourseCouponDomain courseCouponDomain = new CourseCouponDomain();
                        courseCouponDomain.setCouponType(1);
                        courseCouponDomain.setName("订单通用");
                        courseCouponDomains.add(courseCouponDomain);
                        courseCouponDomains.addAll(couponList);
                    }
                    List<CourseCouponDomain> subjectCouponList = date.getSubjectCouponList();//学科优惠券
                    if (!ObjectUtils.isEmpty(subjectCouponList)) {
                        for (CourseCouponDomain item : subjectCouponList) {
                            item.setCourseId(-2);
                            if (!ObjectUtils.isEmpty(couponMap) && couponMap.containsKey(item.getCourseId())) {
                                int couponId = couponMap.get(item.getCourseId()).getId();
                                if (couponId == item.getId()) {
                                    item.setCheck(true);
                                } else {
                                    item.setCheck(false);
                                }
                            } else {
                                item.setCheck(false);
                            }
                        }
                        CourseCouponDomain courseCouponDomain = new CourseCouponDomain();
                        courseCouponDomain.setCouponType(1);
                        courseCouponDomain.setName("学科通用");
                        courseCouponDomains.add(courseCouponDomain);
                        courseCouponDomains.addAll(subjectCouponList);
                    }
                    
                    
                    List<OrderItemDomain> orderItemList = date.getOrderItemList();
                    for (OrderItemDomain course : orderItemList) {
                        List<CourseCouponDomain> couponList1 = course.getCourse().getCouponList();
                        if (!ObjectUtils.isEmpty(couponList1)) {
                            for (CourseCouponDomain item : couponList1) {
                                item.setCourseId(course.getCourseId());
                                if (!ObjectUtils.isEmpty(couponMap) && couponMap.containsKey(item.getCourseId())) {
                                    int couponId = couponMap.get(item.getCourseId()).getId();
                                    if (couponId == item.getId()) {
                                        item.setCheck(true);
                                    } else {
                                        item.setCheck(false);
                                    }
                                } else {
                                    item.setCheck(false);
                                }
                            }
                            CourseCouponDomain courseCouponDomain = new CourseCouponDomain();
                            courseCouponDomain.setCouponType(1);
                            courseCouponDomain.setName(course.getCourse().getName());
                            courseCouponDomains.add(courseCouponDomain);
                            courseCouponDomains.addAll(couponList1);
                        }
                    }
                    StrBuilder orderId = new StrBuilder();
                    for (OrderItemDomain item : date.getOrderItemList()) {
                        orderId.append(item.getCourseId()).append(",");
                    }
                    String orderIds = orderId.toString().substring(0, orderId.toString().length() - 1);
                    
                    AvailableCouponsActivity.start(ConfirmOrderActivity.this, courseCouponDomains, orderIds, cb_balance_pay.isChecked() ? 1 : 0);
                    break;
                case R.id.cl_choice_address:
                case R.id.tv_order_noAddress:
                    AddressManagerActivity.start(ConfirmOrderActivity.this, false);
                    break;
                case R.id.tv_order_pay:
                    commitOrder();
                    break;
                //                case R.id.rl_ali_pay:
                //                    cb_ali_pay.performClick();
                //                    break;
                //                case R.id.rl_wx_pay:
                //                    cb_wx_pay.performClick();
                //                break;
                case R.id.vg_blance_pay:
                    cb_balance_pay.performClick();
                    break;
                case R.id.toolbar_back:
                    onBackPressed();
                    break;
                default:
            }
        }
    };
    
    //生成订单
    private void commitOrder() {
        if (date.isNeedAddress() && ObjectUtils.isEmpty(addressDate)) {
            ToastUtils.showShort("请选择收货地址");
            return;
        }
        showLoadingDialog();
        LiveOrderCreateRequest liveOrderCreateRequest = new LiveOrderCreateRequest();
        //        if (!TextUtils.isEmpty(payMethod)) {
        //            liveOrderCreateRequest.setPayWayId(payMethod);
        //        }
        liveOrderCreateRequest.setUserId(UserManager.getInstance().getUserId());
        if (date.isNeedAddress() && !ObjectUtils.isEmpty(addressDate)) {
            liveOrderCreateRequest.setAddressId(String.valueOf(addressDate.getId()));
        }
        liveOrderCreateRequest.setUseBalance(cb_balance_pay.isChecked() ? 1 : 0);
        StrBuilder orderId = new StrBuilder();
        for (OrderItemDomain item : date.getOrderItemList()) {
            //            Map.Entry<Integer, ShoppingCarDomain> entry = (Map.Entry) set;
            orderId.append(item.getCourseId()).append(",");
        }
        String s = orderId.toString().substring(0, orderId.toString().length() - 1 < 0 ? 0 : orderId.toString().length() - 1);
        liveOrderCreateRequest.setCourseIdsStr(s);
        
        if (!ObjectUtils.isEmpty(couponMap)) {
            StrBuilder couponId = new StrBuilder();
            
            for (Object set : couponMap.entrySet()) {
                Map.Entry<Integer, CourseCouponDomain> entry = (Map.Entry) set;
                CourseCouponDomain value = entry.getValue();
                if (value.getCourseId() == -1) {//订单通用
                    liveOrderCreateRequest.setOrderCouponIdsStr(String.valueOf(value.getId()));
                } else if (value.getCourseId() == -2) {//学科专用
                    liveOrderCreateRequest.setSubjectCouponIdsStr(String.valueOf(value.getId()));
                } else {//课程专用
                    couponId.append(value.getCourseId()).append("_").append(value.getId()).append(",");
                }
            }
            if (couponId.toString().length() > 0) {
                String ss = couponId.toString().substring(0, couponId.toString().length() - 1);
                liveOrderCreateRequest.setCourseAndCouponIdsStr(ss);
            }
        }
        Api.getInstance().post(liveOrderCreateRequest, LiveOrderCreateResponse.class, new ResponseCallback<LiveOrderCreateResponse>() {
            @Override
            public void onResponse(LiveOrderCreateResponse date, boolean isFromCache) {
                if (isDestroyed()) {
                    return;
                }
                dismissLoadingDialog();
                pay(date);
                
            }
            
            @Override
            public void onError() {
                if (isDestroyed()) {
                    return;
                }
                dismissLoadingDialog();
            }
        }, "");
        
    }
    
    //支付
    private void pay(LiveOrderCreateResponse date) {
        if (date.getData().getHasNoPayOrder()) {
            hasNoPayOrder();
        } else {
            if (date.getData().getDueAmount().doubleValue() > 0) {
                
                PayActivity.Companion.start(ConfirmOrderActivity.this, date.getData());
                finish();
            } else {
                PaySuccessActivity.Companion.start(ConfirmOrderActivity.this, tv_balance_save.getText().toString());
                finish();
            }
        }
    }
    
    /**
     * 存在未支付订单 下单失败
     */
    private void hasNoPayOrder() {
        
        TipDialog dialog = new TipDialog(this);
        dialog.setTextDes("存在未支付订单");
        dialog.setButton1(getString(R.string.go_pay), new TipDialog.DialogButtonOnClickListener() {
            
            @Override
            public void onClick(View button, TipDialog dialog) {
                OrderCenterActivity.start(ConfirmOrderActivity.this, 1);
                dialog.dismiss();
                finish();
            }
        });
        dialog.setButton2(getString(R.string.cancel), new TipDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, TipDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AvailableCouponsActivity.RequestCode && resultCode == RESULT_OK) {
            //            int num = data.getIntExtra("num", 0);
            couponMap = (HashMap<Integer, CourseCouponDomain>) data.getSerializableExtra("num");
            couponsSave = (BigDecimal) data.getSerializableExtra("money");
            ((TextView) findViewById(R.id.tv_coupon_save)).setText("¥" + couponsSave.abs());
            if (ObjectUtils.isEmpty(couponMap)) {
                tv_coupon_use.setText(getString(R.string.coupon_use, 0, couponsSave.toString().replace("-", "")));
            } else {
                tv_coupon_use.setText(getString(R.string.coupon_use, couponMap.size(), couponsSave.toString().replace("-", "")));
            }
            BigDecimal payMoney = getBalanceLeft();
            if (payMoney.doubleValue() >= 0) {
                tv_order_left_count.setText(getString(R.string.left_count, availBalance.toString(), payMoney.toString()));
            } else {
                tv_order_left_count.setText(getString(R.string.left_count, availBalance.toString(), "0.00"));
                //                    originPayMoney = availBalance.subtract(balanceSave).subtract(couponsSave).subtract(activitySave);
            }
            BigDecimal total = calculateTotalMoney();
            if (cb_balance_pay.isChecked()) {
                tv_balance_save.setText("¥" + getBalanceCast());
            } else {
                tv_balance_save.setText("¥0.00");
                
            }
            if (total.doubleValue() >= 0) {
                tv_pay_count.setText("¥" + total);
            } else {
                tv_pay_count.setText("¥0.00");
            }
        }
    }
    
    //剩余余额
    private BigDecimal getBalanceLeft() {
        return availBalance.subtract(originPayMoney).subtract(couponsSave).subtract(activitySave);
    }
    
    //余额抵扣
    private BigDecimal getBalanceCast() {
        if (getBalanceLeft().doubleValue() >= 0) {
            BigDecimal add = originPayMoney.add(couponsSave).add(activitySave).setScale(2, BigDecimal.ROUND_DOWN);
            if (add.doubleValue() < 0) {
                return BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN);
            }
            return add;
        } else {
            return availBalance.setScale(2, BigDecimal.ROUND_DOWN);
        }
        
    }
    
    //应付金额
    private BigDecimal calculateTotalMoney() {
        if (cb_balance_pay.isChecked()) {
            return originPayMoney.subtract(balanceSave).add(couponsSave).add(activitySave).setScale(2, BigDecimal.ROUND_DOWN);
        } else {
            return originPayMoney.add(couponsSave).add(activitySave).setScale(2, BigDecimal.ROUND_DOWN);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRxBus != null) {
            mRxBus.unSubscribe(ConfirmOrderActivity.class);
        }
    }
    
    @Override
    public void onBackPressed() {
        confirmCancel();
    }
    
    private void confirmCancel() {
        
        TipDialog dialog = new TipDialog(this);
        dialog.setTextDes("确认取消订单？");
        dialog.setButton1(getString(R.string.commit), new TipDialog.DialogButtonOnClickListener() {
            
            @Override
            public void onClick(View button, TipDialog dialog) {
                ConfirmOrderActivity.super.onBackPressed();
            }
        });
        dialog.setButton2(getString(R.string.cancel), new TipDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, TipDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
}
