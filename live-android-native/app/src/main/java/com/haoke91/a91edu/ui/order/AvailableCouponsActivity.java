package com.haoke91.a91edu.ui.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.ArrayMap;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.gaosiedu.live.sdk.android.api.order.coupon.use.LiveOrderCouponUseRequest;
import com.gaosiedu.live.sdk.android.api.order.coupon.use.LiveOrderCouponUseResponse;
import com.gaosiedu.live.sdk.android.domain.CourseCouponDomain;
import com.gaosiedu.live.sdk.android.domain.ShoppingCarDomain;
import com.google.gson.Gson;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.AvailableCouponsAdapter;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.utils.ArithUtils;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.PinnedHeaderDecoration;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.baselibrary.recycleview.adapter.BaseQuickWithPositionAdapter;
import com.haoke91.baselibrary.views.emptyview.EmptyView;

import org.apache.commons.lang3.text.StrBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：91haoke
 * 类描述：  可用优惠券
 * 创建人：weiyimeng
 * 创建时间：2018/7/20 下午5:21
 * 修改人：weiyimeng
 * 修改时间：2018/7/20 下午5:21
 * 修改备注：
 */
public class AvailableCouponsActivity extends BaseActivity {
    public static final String DATE = "date";
    public static int RequestCode = 111;
    private TextView tv_save_money;
    
    public static void start(Activity context, ArrayList<CourseCouponDomain> courseCouponDomains, String orderIds, int userBalance) {
        Intent intent = new Intent(context, AvailableCouponsActivity.class);
        intent.putExtra(DATE, courseCouponDomains);
        intent.putExtra("orderIds", orderIds);
        intent.putExtra("userBalance", userBalance);
        context.startActivityForResult(intent, RequestCode);
    }
    
    
    private String orderIds;
    private int userBalance;
    
    @Override
    public int getLayout() {
        return R.layout.activity_availcoupons;
    }
    
    private BigDecimal totalMoney = BigDecimal.ZERO;
    private AvailableCouponsAdapter availableCouponsAdapter;
    
    @Override
    public void initialize() {
        ArrayList<CourseCouponDomain> courseCouponDomains = (ArrayList<CourseCouponDomain>) getIntent().getSerializableExtra(DATE);
        orderIds = getIntent().getStringExtra("orderIds");
        userBalance = getIntent().getIntExtra("userBalance", 1);
        int count = 0;
        
        for (CourseCouponDomain item : courseCouponDomains) {
            if (item.isCheck()) {
                count++;
                //                totalMoney = totalMoney.add(new BigDecimal(item.getResume()));
            }
        }
        
        SpannableStringBuilder spannableStringBuilder = new SpanUtils().append("已选择").setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.color_363636))
            .append(String.valueOf(count)).setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.FF4F00))
            .append("张优惠券").setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.color_363636))
            //            .append(totalMoney.abs().setScale(2, BigDecimal.ROUND_DOWN).toString()).setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.FF4F00))
            //            .append("元").setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.color_363636))
            .create();
        EmptyView empty_view = findViewById(R.id.emptyview);
        tv_save_money = findViewById(R.id.tv_save_money);
        tv_save_money.setText(spannableStringBuilder);
        WrapRecyclerView rv_coupons_list = findViewById(R.id.rv_coupons_list);
        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        rv_coupons_list.addItemDecoration(decoration);
        
        rv_coupons_list.setLayoutManager(new LinearLayoutManager(this));
        if (ObjectUtils.isEmpty(courseCouponDomains)) {
            empty_view.showEmpty();
        } else {
            empty_view.showContent();
        }
        availableCouponsAdapter = new AvailableCouponsAdapter(this, courseCouponDomains);
        rv_coupons_list.setAdapter(availableCouponsAdapter);
        availableCouponsAdapter.setOnItemClickListener(new BaseQuickWithPositionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HashMap choiceMap = availableCouponsAdapter.getChocieMap();
                if (!ObjectUtils.isEmpty(choiceMap)) {
                    totalMoney = BigDecimal.ZERO;
                    for (Object set : choiceMap.entrySet()) {
                        Map.Entry<Integer, CourseCouponDomain> entry = (Map.Entry) set;
                        CourseCouponDomain value = entry.getValue();
                        totalMoney = totalMoney.add(new BigDecimal(value.getResume()));
                    }
                    
                    SpannableStringBuilder spannableStringBuilder = new SpanUtils().append("已选择").setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.color_363636))
                        .append(String.valueOf(choiceMap.size())).setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.FF4F00))
                        .append("张优惠券").setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.color_363636))
                        //                        .append(totalMoney.abs().setScale(2, BigDecimal.ROUND_DOWN).toString()).setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.FF4F00))
                        //                        .append("元").setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.color_363636))
                        .create();
                    tv_save_money.setText(spannableStringBuilder);
                } else {
                    totalMoney = BigDecimal.ZERO;
                    SpannableStringBuilder spannableStringBuilder = new SpanUtils().append("已选择").setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.color_363636))
                        .append(String.valueOf(0)).setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.FF4F00))
                        .append("张优惠券").setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.color_363636))
                        //                        .append("0.00").setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.FF4F00))
                        //                        .append("元").setFontSize(12, true).setForegroundColor(getResources().getColor(R.color.color_363636))
                        .create();
                    tv_save_money.setText(spannableStringBuilder);
                }
                
                
            }
        });
        findViewById(R.id.tv_order_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                getCouponsSave();
                //
                //                Intent intent = getIntent();
                //                intent.putExtra("num", availableCouponsAdapter.getChocieMap());
                //                intent.putExtra("money", totalMoney);
                //                setResult(RESULT_OK, intent);
                //                finish();
            }
        });
        
        
    }
    
    private void getCouponsSave() {
        LiveOrderCouponUseRequest request = new LiveOrderCouponUseRequest();
        request.setUserId(UserManager.getInstance().getUserId());
        request.setCourseIdsStr(orderIds);
        request.setUseBalance(userBalance);
        HashMap couponMap = availableCouponsAdapter.getChocieMap();
        if (ObjectUtils.isEmpty(couponMap)) {
            Intent intent = getIntent();
            intent.putExtra("num", availableCouponsAdapter.getChocieMap());
            intent.putExtra("money", totalMoney);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            showLoadingDialog();
            StrBuilder couponId = new StrBuilder();
            for (Object set : couponMap.entrySet()) {
                Map.Entry<Integer, CourseCouponDomain> entry = (Map.Entry) set;
                CourseCouponDomain value = entry.getValue();
                if (value.getCourseId() == -1) {//订单通用
                    request.setOrderCouponIdsStr(String.valueOf(value.getId()));
                } else if (value.getCourseId() == -2) {//学科专用
                    request.setSubjectCouponIdsStr(String.valueOf(value.getId()));
                } else {//课程专用
                    couponId.append(value.getCourseId()).append("_").append(value.getId()).append(",");
                }
            }
            if (couponId.toString().length() > 0) {
                String ss = couponId.toString().substring(0, couponId.toString().length() - 1);
                request.setCourseAndCouponIdsStr(ss);
            }
            Api.getInstance().post(request, LiveOrderCouponUseResponse.class, new ResponseCallback<LiveOrderCouponUseResponse>() {
                @Override
                public void onResponse(LiveOrderCouponUseResponse date, boolean isFromCache) {
                    if (isDestroyed()) {
                        return;
                    }
                    dismissLoadingDialog();
                    Intent intent = getIntent();
                    intent.putExtra("num", availableCouponsAdapter.getChocieMap());
                    intent.putExtra("money", date.getData().getCouponAmount());
                    setResult(RESULT_OK, intent);
                    finish();
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
        
    }
    
    
}
