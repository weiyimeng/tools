package com.haoke91.a91edu.adapter

import android.content.Context
import android.text.Html
import android.view.View
import android.widget.TextView

import com.blankj.utilcode.util.TimeUtils
import com.gaosiedu.scc.sdk.android.api.user.exercise.list.LiveSccUserExerciseListResponse
import com.gaosiedu.scc.sdk.android.domain.ExerciseBean
import com.haoke91.a91edu.R
import com.haoke91.a91edu.ui.homework.HomeworkResultActivity
import com.haoke91.a91edu.ui.homework.LookHomeworkActivity
import com.haoke91.a91edu.ui.homework.UpLoadHomeworkActivity
import com.haoke91.a91edu.ui.homework.WaitUploadFragment
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/16 下午4:56
 * 修改人：weiyimeng
 * 修改时间：2018/7/16 下午4:56
 * 修改备注：
 */
class HomeworkListAdapter(context: Context, dates: List<LiveSccUserExerciseListResponse.ListData>, internal var type: Int) : QuickWithPositionAdapter<LiveSccUserExerciseListResponse.ListData>(context, R.layout.item_upload_homework, dates) {
    
    override fun setData(list: List<LiveSccUserExerciseListResponse.ListData>?) {
        if (list == null) {
            this.data.clear()
        } else {
            this.data = list
        }
        notifyDataSetChanged()
    }
    
    override fun convert(helper: BaseAdapterHelper, item: LiveSccUserExerciseListResponse.ListData, position: Int) {
        val textView = helper.getTextView(R.id.tv_homework_action)
        helper.itemView.setOnClickListener(object : NoDoubleClickListener() {
            override fun onDoubleClick(v: View) {
                when (type) {
                    WaitUploadFragment.wait -> UpLoadHomeworkActivity.start(context, v.tag as Int)
                    WaitUploadFragment.checking -> LookHomeworkActivity.start(context, v.tag as Int)
                    WaitUploadFragment.checked -> HomeworkResultActivity.start(context, v.tag as Int)
                }
            }
            
        })
        val exercise = item.exercise ?: return
        helper.itemView.tag = item.exercise.id
        if (type == WaitUploadFragment.checked || type == WaitUploadFragment.checking) { //作业结果使用不同id
            helper.itemView.tag = item.exerciseResultId
        }
        val tv_title = helper.getTextView(R.id.tv_homework_tittle)
        val tv_secondTitle = helper.getTextView(R.id.tv_homework_caption)
        val tv_endTime = helper.getTextView(R.id.tv_homework_endTime)
        tv_title.text = Html.fromHtml(exercise.courseName)
        tv_secondTitle.text = "第${exercise.knowledgeOrder}讲   ${exercise.knowledgeName}"
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        if (this.type == WaitUploadFragment.wait) {
            textView.text = "去提交"
            tv_endTime.text = String.format("截止时间：%s", format.format(exercise.latest))
        } else if (this.type == WaitUploadFragment.checking) {
            textView.text = "查看作业"
            //            tv_endTime.text = String.format("截止时间：%s", TimeUtils.date2String(TimeUtils.string2Date(exer.), SimpleDateFormat("yyyy-MM-dd"))
            //                    ?: item.submitTime)
            tv_endTime.text = "截止时间：${format.format(exercise.latest)}"
        } else {
            textView.text = "作业结果"
            tv_endTime.text = String.format("批改时间：%s", format.format(exercise.latest))
        }
    }
}
