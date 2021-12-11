package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.gaosiedu.scc.sdk.android.domain.CourseKnowledgeBean;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.ui.liveroom.LivePlayerActivity;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.a91edu.widget.NoDoubleClickListener;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/16 下午4:56
 * 修改人：weiyimeng
 * 修改时间：2018/7/16 下午4:56
 * 修改备注：
 */
public class TodayCourseAdapter extends QuickWithPositionAdapter<CourseKnowledgeBean> {
    public TodayCourseAdapter(Context context, List<CourseKnowledgeBean> dates){
        super(context, R.layout.item_today_course, dates);
    }
    
    @Override
    protected void convert(BaseAdapterHelper helper, final CourseKnowledgeBean item, int position){
        if (item == null){
            return;
        }
        
        TextView tv_look_course = helper.getTextView(R.id.tv_look_course);
        tv_look_course.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onDoubleClick(@NotNull View v) {
                Utils.startLiveRoom(context, item.getId() + "");
    
            }
        });
        TextView holiday = helper.getTextView(R.id.tv_order_holiday);
        if (0 == item.getTerm()){
            holiday.setVisibility(View.GONE);
        } else{
            holiday.setVisibility(View.VISIBLE);
            holiday.setText(Utils.getHolidayByNumber(item.getTerm(), holiday));
        }
        helper.getTextView(R.id.tv_order_tag).setText(item.getCourseSubjectNames() == null ? "" : item.getCourseSubjectNames().substring(0, 1));
        helper.getTextView(R.id.tv_order_course_name).setText(Html.fromHtml(item.getCourseName()));
        if (item.getStartTime() != null && item.getEndTime() != null){
            String startTime = new SimpleDateFormat("HH:mm").format(item.getStartTime());
            String endTime = new SimpleDateFormat("HH:mm").format(item.getEndTime());
            TextView tv_courseTime = helper.getTextView(R.id.tv_order_course_time);
            tv_courseTime.setText(startTime + " - " + endTime + "直播");
            if (item.isKnowledgeJoin()){
                tv_courseTime.setSelected(true);
            } else{
                tv_courseTime.setSelected(false);
            }
        }
        String flag = item.getFlag();
        TextView status = helper.getTextView(R.id.tv_look_course);
        if ("wating".equals(flag)){
            status.setText("直播待开始");
            tv_look_course.setClickable(false);
        } else if ("living".equals(flag)){
            status.setText("直播进行中");
            tv_look_course.setClickable(true);
        } else if ("complete".equals(flag)){
            status.setText(Html.fromHtml("<p style='color:#979797'>直播已结束</p>"));
            tv_look_course.setClickable(true);
        }
        
    }
}
