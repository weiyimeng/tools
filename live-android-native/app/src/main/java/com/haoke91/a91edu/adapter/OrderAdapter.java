package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.drm.DrmStore;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.gaosiedu.live.sdk.android.domain.CourseDomain;
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.entities.MulitOrderBean;
import com.haoke91.a91edu.ui.order.ConfirmOrderActivity;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.a91edu.utils.imageloader.GlideUtils;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.MultiItemTypeSupport;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;

import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/1 下午7:03
 * 修改人：weiyimeng
 * 修改时间：2018/8/1 下午7:03
 * 修改备注：
 */
public class OrderAdapter extends QuickWithPositionAdapter<MulitOrderBean> {
    public OrderAdapter(Context context, List<MulitOrderBean> dates) {
        super(context, new MultiItemTypeSupport<MulitOrderBean>() {
            @Override
            public int getLayoutId(int viewType) {
                switch (viewType) {
                    case MulitOrderBean.head:
                        return R.layout.layout_order_head;
                    case MulitOrderBean.body://一个主讲
                        return R.layout.layout_order_body;
                    case MulitOrderBean.tail:
                        return R.layout.layout_order_tail;
                    case 4://两个主讲 更多主讲
                        return R.layout.layout_order_body_two;
                    default:
                        return R.layout.layout_order_head;
                }
            }
            
            @Override
            public int getItemViewType(int position, MulitOrderBean mulitOrderBean) {
                if (MulitOrderBean.body == mulitOrderBean.getType()) {
                    CourseDomain date = ((OrderItemDomain) mulitOrderBean.getDate()).getCourse();
                    if (ObjectUtils.isEmpty(date.getTeachers())) {
                        return mulitOrderBean.getType();
                    }
                    int size = date.getTeachers().size();
                    if (size == 1) {
                        return mulitOrderBean.getType();
                    } else {
                        return 4;
                    }
                }
                return mulitOrderBean.getType();
            }
        }, dates);
    }
    
    @Override
    protected void convert(BaseAdapterHelper helper, MulitOrderBean item, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == MulitOrderBean.head) {//头部
            setHeadView(helper, item);
        } else if (itemViewType == MulitOrderBean.body) {//内容
            setBodyView(helper, item);
        } else if (itemViewType == MulitOrderBean.tail) {//底部
            setTailView(helper, item);
        } else if (itemViewType == 4) {
            setBodyView(helper, item);
            setSecondView(helper, item);
        }
    }
    
    private void setSecondView(BaseAdapterHelper helper, MulitOrderBean item) {
        CourseDomain date = ((OrderItemDomain) item.getDate()).getCourse();
        helper.getTextView(R.id.tv_order_teacher_name_two).setText(date.getTeachers().get(1).getRealname());
        GlideUtils.loadHead(context, date.getTeachers().get(1).getHeadUrl(), helper.getImageView(R.id.iv_teacher_two_icon));
        if (date.getTeachers().size() > 2) {
            helper.getView(R.id.iv_more).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.iv_more).setVisibility(View.GONE);
        }
    }
    
    private void setTailView(BaseAdapterHelper helper, MulitOrderBean item) {
        MulitOrderBean.OrderTail date = (MulitOrderBean.OrderTail) item.getDate();
        if (TextUtils.isEmpty(Utils.getOrderActionByNumber(date.status))) {
            helper.getTextView(R.id.tv_order_action).setVisibility(View.GONE);
        } else {
            helper.getTextView(R.id.tv_order_action).setVisibility(View.VISIBLE);
            helper.getTextView(R.id.tv_order_action).setText(Utils.getOrderActionByNumber(date.status));
        }
        if (date.status == -1) {//重新购买
            helper.getTextView(R.id.tv_order_detail).setVisibility(View.INVISIBLE);
            helper.getTextView(R.id.tv_pay_method).setVisibility(View.GONE);
            helper.getTextView(R.id.tv_order_money).setVisibility(View.GONE);
            helper.getView(R.id.line).setVisibility(View.GONE);
        } else {
            helper.getTextView(R.id.tv_order_detail).setVisibility(View.VISIBLE);
            helper.getTextView(R.id.tv_pay_method).setVisibility(View.VISIBLE);
            helper.getTextView(R.id.tv_order_money).setVisibility(View.VISIBLE);
            helper.getView(R.id.line).setVisibility(View.VISIBLE);
            
        }
        if ("back".equalsIgnoreCase(type)) {
            helper.getTextView(R.id.tv_order_detail).setText("退款单详情");
            if (date.status == 16) {
                helper.getTextView(R.id.tv_pay_method).setText("已退款: ");
            } else {
                helper.getTextView(R.id.tv_pay_method).setText("预退金额: ");
            }
        } else {
            if (date.status == 0 || date.status == 2) {
                helper.getTextView(R.id.tv_pay_method).setText("应付款: ");
            }
        }
        if ("取消退款".equalsIgnoreCase(helper.getTextView(R.id.tv_order_action).getText().toString().trim())) {
            if (!"back".equalsIgnoreCase(type) || date.status == 3 || date.status == 13 || date.status == 15) {
                helper.getTextView(R.id.tv_order_action).setVisibility(View.GONE);
            } else {
                helper.getTextView(R.id.tv_order_action).setVisibility(View.VISIBLE);
                
            }
        }
        
        
        SpannableStringBuilder spannableStringBuilder = new SpanUtils().append("¥").setFontSize(12, true).setForegroundColor(context.getResources().getColor(R.color.color_363636))
            .appendSpace(2).append(String.valueOf(date.money)).setFontSize(20, true).setForegroundColor(context.getResources().getColor(R.color.color_363636))
            .create();
        helper.getTextView(R.id.tv_order_money).setText(spannableStringBuilder);
        
    }
    
    private void setBodyView(BaseAdapterHelper helper, MulitOrderBean item) {
        CourseDomain date = ((OrderItemDomain) item.getDate()).getCourse();
        helper.getTextView(R.id.tv_order_course_name).setText(Html.fromHtml(date.getName()));
        helper.getTextView(R.id.tv_order_tag).setText(date.getCourseSubjectNames().substring(0, 1));
        TextView tvHoliday = helper.getTextView(R.id.tv_order_holiday);
        if (TextUtils.isEmpty(Utils.getHolidayByNumber(date.getTerm(), tvHoliday))) {
            helper.getTextView(R.id.tv_order_holiday).setVisibility(View.GONE);
        } else {
            helper.getTextView(R.id.tv_order_holiday).setVisibility(View.VISIBLE);
            helper.getTextView(R.id.tv_order_holiday).setText(Utils.getHolidayByNumber(date.getTerm(), tvHoliday));
        }
        helper.getTextView(R.id.tv_order_course_time).setText(date.getTimeremark());
        if (!ObjectUtils.isEmpty(date.getTeachers())) {
            helper.getTextView(R.id.tv_order_teacher_name).setText(date.getTeachers().get(0).getRealname());
            GlideUtils.loadHead(context, date.getTeachers().get(0).getHeadUrl(), helper.getImageView(R.id.iv_order_teacher_icon));
        }
        if (!ObjectUtils.isEmpty(date.getCourseClassDomain()) && !TextUtils.isEmpty(date.getCourseClassDomain().getTeacherName())) {
            helper.getImageView(R.id.iv_order_assistant_icon).setVisibility(View.VISIBLE);
            helper.getTextView(R.id.tv_assistant_name).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_assistant).setVisibility(View.VISIBLE);
            GlideUtils.loadHead(context, date.getCourseClassDomain().getHeadUrl(), helper.getImageView(R.id.iv_order_assistant_icon));
            helper.getTextView(R.id.tv_assistant_name).setText(date.getCourseClassDomain().getTeacherName());
        } else {
            helper.getImageView(R.id.iv_order_assistant_icon).setVisibility(View.INVISIBLE);
            helper.getTextView(R.id.tv_assistant_name).setVisibility(View.INVISIBLE);
            helper.getView(R.id.tv_assistant).setVisibility(View.INVISIBLE);
        }
        
    }
    
    private void setHeadView(BaseAdapterHelper helper, MulitOrderBean item) {
        MulitOrderBean.OrderHead date = (MulitOrderBean.OrderHead) item.getDate();
        helper.getTextView(R.id.tv_order_no).setText(date.orderNo);
        helper.getTextView(R.id.tv_order_status).setText(Utils.getOrderStatusByNumber(date.status));
        if ("back".equalsIgnoreCase(type)) {
            helper.getTextView(R.id.tv_order).setText("退款单号：");
        }
    }
    
    private String type;
    
    public void setType(String type) {
        this.type = type;
    }
}
