package com.haoke91.a91edu.adapter

import android.content.Context
import android.view.View
import com.blankj.utilcode.util.TimeUtils
import com.gaosiedu.live.sdk.android.domain.CourseKnowledgeDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.utils.Utils
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter
import java.text.SimpleDateFormat
import java.util.*

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/9/13 10:38
 */
class CoursePlanAdapter(context: Context) : QuickWithPositionAdapter<CourseKnowledgeDomain>(context, R.layout.item_courseplan, ArrayList()) {
    
    override fun convert(helper: BaseAdapterHelper, item: CourseKnowledgeDomain, position: Int) {
        val tvTitle = helper.getTextView(R.id.tv_title)
        val tvName = helper.getTextView(R.id.tv_name)
        val tvAudition = helper.getTextView(R.id.tv_audition)
        val tvStatus = helper.getTextView(R.id.tv_status)
        val tvTime = helper.getTextView(R.id.tv_time)
        tvTitle.text = "第${item.displayOrder}课  ${item.name}"
        tvName.text = "主讲  ${item.teacherName}"
        if (item.auditionTime > 0 && (item.supportPlatform == "aliyun" || item.supportPlatform == "talk")) {
            tvAudition.visibility = View.VISIBLE
        } else {
            tvAudition.visibility = View.GONE
        }
        tvStatus.text = if ("complete" == item.flag) "已结束" else if ("wating" == item.flag) "未开始" else "直播中" //living
        tvTime.text = "周" + Utils.dateToWeek(item.startTime) + "  " + Utils.datetoStringJudge(item.startTime, SimpleDateFormat("MM月dd日 HH：mm"), SimpleDateFormat("yyyy年MM月dd日 HH：mm")) + " - " + Utils.dateToString(item.endTime, "HH:mm")
    }
}
