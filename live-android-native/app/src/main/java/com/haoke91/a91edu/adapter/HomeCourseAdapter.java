package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.gaosiedu.live.sdk.android.api.course.aggregation.LiveCourseAggregationResponse;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/19 下午5:39
 * 修改人：weiyimeng
 * 修改时间：2018/7/19 下午5:39
 * 修改备注：
 */
public class HomeCourseAdapter extends QuickWithPositionAdapter<LiveCourseAggregationResponse.ListData> {
    public HomeCourseAdapter(Context context, List<LiveCourseAggregationResponse.ListData> dates) {
        super(context, R.layout.item_subjectlist_main, dates);
    }
    
    @Override
    protected void convert(BaseAdapterHelper helper, LiveCourseAggregationResponse.ListData item, int position) {
        TextView tv_course_money = helper.getTextView(R.id.tv_course_money);
        SpanUtils spanUtils = new SpanUtils();
        SpannableStringBuilder spannableStringBuilder = spanUtils.append("¥").setFontSize(12, true).setForegroundColor(context.getResources().getColor(R.color.FF4F00))
            .appendSpace(4).append(String.valueOf(item.getMinPrice())).setFontSize(20, true).setForegroundColor(context.getResources().getColor(R.color.FF4F00))
            .appendSpace(4).append("起").setFontSize(12, true).setForegroundColor(context.getResources().getColor(R.color.FF4F00)).create();
        tv_course_money.setText(spannableStringBuilder);
        helper.getTextView(R.id.tv_course_name).setText(item.getName());
        helper.getTextView(R.id.tv_course_tag).setText(item.getSubjectName().substring(0, 1));
        if (TextUtils.isEmpty(Utils.getHolidayByNumber(item.getTerm(),null))) {
            helper.getTextView(R.id.tv_course_holiday).setVisibility(View.GONE);
        } else {
            helper.getTextView(R.id.tv_course_holiday).setVisibility(View.VISIBLE);
            helper.getTextView(R.id.tv_course_holiday).setText(Utils.getHolidayByNumber(item.getTerm(),helper.getTextView(R.id.tv_course_holiday)));
        }
        helper.getTextView(R.id.tv_course_canchoice).setText(context.getString(R.string.choice_course_count, item.getCourseCount()));
        helper.getTextView(R.id.tv_sign_num).setText(context.getString(R.string.sign_count, item.getSignUpCount()));
        
    }
}
