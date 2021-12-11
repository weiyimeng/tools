package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.gaosiedu.live.sdk.android.api.user.coupon.list.LiveUserCouponListResponse;
import com.gaosiedu.live.sdk.android.domain.DictionaryDomain;
import com.haoke91.a91edu.MainActivity;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.ui.course.CourseDetailActivity;
import com.haoke91.a91edu.ui.course.SpecialClassActivity;
import com.haoke91.a91edu.ui.user.AllCouponsFragment;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.baselibrary.event.MessageItem;
import com.haoke91.baselibrary.event.RxBus;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;
import com.haoke91.videolibrary.videoplayer.VideoPlayer;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/9 上午10:49
 * 修改人：weiyimeng
 * 修改时间：2018/8/9 上午10:49
 * 修改备注：
 */
public class MyCouponsAdapter extends QuickWithPositionAdapter<LiveUserCouponListResponse.ListData> {
    //    public MyCouponsAdapter(Context context, List<LiveUserCouponListResponse.ListData> dates) {
    //        super(context, R.layout.item_my_conpans, dates);
    //    }
    
    public MyCouponsAdapter(Context context, int item_my_coupons, List<LiveUserCouponListResponse.ListData> dates) {
        super(context, item_my_coupons, dates);
    }
    
    @Override
    protected void convert(final BaseAdapterHelper helper, final LiveUserCouponListResponse.ListData item, int position) {
        TextView tv_coupon_use = helper.getView(R.id.tv_coupon_use);
        if (AllCouponsFragment.used.equalsIgnoreCase(type)) {
            tv_coupon_use.setVisibility(View.GONE);
            helper.getView(R.id.tv_coupon_endless).setVisibility(View.GONE);
            helper.getView(R.id.iv_coupon_flag).setVisibility(View.VISIBLE);
        }
        if (AllCouponsFragment.not_use.equalsIgnoreCase(type)) {
            helper.getView(R.id.iv_coupon_flag).setVisibility(View.INVISIBLE);
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // * 优惠券类型1.全场通用,2.课程体系通用,3.课程适用,4.产品适用,5:学科通用,6:年级通用
                    
                    if (item.getType() == 1) {
                        ActivityUtils.finishToActivity(MainActivity.class, false, true);
                        postEvent(String.valueOf(0));
                    } else if (item.getType() == 3) {
                        CourseDetailActivity.Companion.start(context, item.getCourseId());
                    } else if (item.getType() == 5) {
                        if (item.getCourseId() != 0) {
                            CourseDetailActivity.Companion.start(context, item.getCourseId());
                        } else if (!ObjectUtils.isEmpty(item.getSubjectId())) {
                            ArrayList<DictionaryDomain> courseInfo = UserManager.getInstance().getCourseInfo();
                            for (DictionaryDomain classItem : courseInfo) {
                                if (item.getSubjectId().equalsIgnoreCase(classItem.getDicValue())) {
                                    int i = courseInfo.indexOf(classItem);
                                    ActivityUtils.finishToActivity(MainActivity.class, false, true);
                                    postEvent(String.valueOf(0), i);
                                    return;
                                }
                            }
                            ActivityUtils.finishToActivity(MainActivity.class, false, true);
                            postEvent(String.valueOf(0));
                        }
                    }
                }
            });
            long timeSpanByNow = TimeUtils.getTimeSpanByNow(item.getEndTime(), TimeConstants.DAY);
            if (timeSpanByNow < 5) {
                helper.getView(R.id.tv_coupon_endless).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.tv_coupon_endless).setVisibility(View.GONE);
                
            }
        }
        helper.getTextView(R.id.tv_coupon_name).setText(item.getName());
        helper.getTextView(R.id.tv_coupon_end).setText("截止日期：" + item.getEndTime());
        if (item.getType() == 1) {
            helper.getTextView(R.id.tv_coupon_range).setText("全场通用");
        } else if (item.getType() == 3) {
            helper.getTextView(R.id.tv_coupon_range).setText("课程专用");
        } else if (item.getType() == 5) {
            helper.getTextView(R.id.tv_coupon_range).setText("学科专用");
            
        }
        helper.getTextView(R.id.tv_coupon_money).setText("¥" + item.getResume().abs().setScale(0, BigDecimal.ROUND_DOWN));
        
    }
    
    private void postEvent(String index) {
        MessageItem messageItem = new MessageItem(MessageItem.change_tab, index);
        RxBus.getIntanceBus().post(messageItem);
    }
    
    private void postEvent(String index, int currentTab) {
        MessageItem messageItem = new MessageItem(MessageItem.change_tab, index, currentTab);
        RxBus.getIntanceBus().post(messageItem);
    }
    
    private String type;
    
    public void setType(String type) {
        this.type = type;
    }
}
