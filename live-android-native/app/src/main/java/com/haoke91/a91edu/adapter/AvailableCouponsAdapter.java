package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.blankj.utilcode.util.TimeUtils;
import com.gaosiedu.live.sdk.android.domain.CourseCouponDomain;
import com.haoke91.a91edu.R;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.MultiItemTypeSupport;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/20 下午5:38
 * 修改人：weiyimeng
 * 修改时间：2018/7/20 下午5:38
 * 修改备注：
 */
public class AvailableCouponsAdapter extends QuickWithPositionAdapter<CourseCouponDomain> {
    private HashMap<Integer, CourseCouponDomain> choiceMap = new HashMap<>();
    
    public AvailableCouponsAdapter(Context context, List<CourseCouponDomain> dates) {
        super(context, new MultiItemTypeSupport<CourseCouponDomain>() {
            @Override
            public int getLayoutId(int viewType) {
                if (viewType == 1) {
                    return R.layout.item_availcoupons_tittle;
                } else {
                    return R.layout.item_availcoupons_content;
                    
                }
            }
            
            @Override
            public int getItemViewType(int position, CourseCouponDomain coupon) {
                return coupon.getCouponType();
            }
        }, dates);
    }
    
    @Override
    protected void convert(BaseAdapterHelper helper, final CourseCouponDomain item, final int position) {
        final int itemViewType = getItemViewType(position);
        if (itemViewType == 1) {
            helper.getTextView(R.id.tv_coupons_tittle).setText(item.getName());
        } else {
            final CheckBox cb_coupons = helper.getView(R.id.cb_coupons);
            if (item.isCheck()) {
                choiceMap.put(item.getCourseId(), item);
                item.setCheck(false);
            }
            //
            if (choiceMap.containsKey(item.getCourseId())) {
                int couponId = choiceMap.get(item.getCourseId()).getId();
                if (item.getId() == couponId) {
                    cb_coupons.setChecked(true);
                } else {
                    cb_coupons.setChecked(false);
                }
            } else {
                cb_coupons.setChecked(false);
            }
            
            
            helper.getTextView(R.id.tv_coupons_name).setText(item.getName());
            helper.getTextView(R.id.tv_coupons_money).setText("¥" + item.getResume().replace("-", ""));
            helper.getTextView(R.id.tv_coupons_endTime).setText("截止日期：" + TimeUtils.millis2String(item.getEndTime().getTime(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
            if (item.getEndTime().getTime() - item.getStartTime().getTime() > 5 * 24 * 60 * 60 * 1000) {
                helper.getTextView(R.id.tv_coupon_endless).setVisibility(View.GONE);
            } else {
                helper.getTextView(R.id.tv_coupon_endless).setVisibility(View.VISIBLE);
            }
            helper.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cb_coupons.performClick();
                }
            });
            cb_coupons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cb_coupons.isChecked()) {
                        choiceMap.put(item.getCourseId(), item);
                        item.setCheck(true);
                    } else {
                        if (choiceMap.containsKey(item.getCourseId())) {
                            choiceMap.remove(item.getCourseId());
                            item.setCheck(false);
                        }
                    }
                    notifyDataSetChanged();
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            
        }
    }
    
    public HashMap getChocieMap() {
        return choiceMap;
    }
    
    
}
