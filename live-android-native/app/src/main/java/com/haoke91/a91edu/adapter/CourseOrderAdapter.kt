package com.haoke91.a91edu.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.blankj.utilcode.util.ObjectUtils
import com.gaosiedu.Constant
import com.gaosiedu.scc.sdk.android.api.user.studyReport.detail.LiveSccStudyReportDetailRequest
import com.gaosiedu.scc.sdk.android.base.LiveSccSdkConsts
import com.gaosiedu.scc.sdk.android.domain.CourseKnowledgeBean
import com.gaosiedu.scc.sdk.android.domain.ExerciseResultBean
import com.haoke91.a91edu.GlobalConfig
import com.haoke91.a91edu.R
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.ui.GeneralWebViewActivity
import com.haoke91.a91edu.ui.homework.HomeworkResultActivity
import com.haoke91.a91edu.ui.homework.LookHomeworkActivity
import com.haoke91.a91edu.ui.homework.UpLoadHomeworkActivity
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter
import java.util.*

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/16 下午4:56
 * 修改人：weiyimeng
 * 修改时间：2018/7/16 下午4:56
 * 修改备注：
 */
class CourseOrderAdapter(context: Context, dates: List<CourseKnowledgeBean>) : QuickWithPositionAdapter<CourseKnowledgeBean>(context, R.layout.item_course_order, dates) {
    //    private var mAssistantHead: String? = null
    
    override fun convert(helper: BaseAdapterHelper, item: CourseKnowledgeBean, position: Int) {
        val tvCourseTitle = helper.getTextView(R.id.tv_courser_tittle)
        val tvLimitTime = helper.getTextView(R.id.tv_course_data)
        val ivTeacherHead = helper.getImageView(R.id.iv_order_teacher_icon)
        val tvTeacherName = helper.getTextView(R.id.tv_order_teacher_name)
        val ivAssistantHead = helper.getImageView(R.id.iv_order_assistant_icon)
        val tvAssistantName = helper.getTextView(R.id.tv_assistant_name)
        val tvAssistantRole = helper.getTextView(R.id.tv_assistant)
        val tvManStatus = helper.getTextView(R.id.tv_man_status)
        val tvHomeworkStatus = helper.getTextView(R.id.tv_homework_status)
        val tvClztestStatus = helper.getTextView(R.id.tv_clztest_status)
        val tvReportStatus = helper.getTextView(R.id.tv_report_status)
        val tvVideoSourceStatus = helper.getTextView(R.id.tv_videosource_status)
        tvCourseTitle.text = String.format("第%s讲  %s", Utils.convertLowerCaseToUpperCase(item.displayOrder), item.name) //名称
        tvLimitTime.text = String.format("%s-%s", Utils.dateToString(item.startTime, "yyyy-MM-dd HH:mm"), Utils.dateToString(item.endTime, "HH:mm"))
        GlideUtils.load(getContext(), if (item.tearcher == null) "" else item.tearcher.headUrl, ivTeacherHead)
        GlideUtils.load(getContext(), item.assistant?.headUrl ?: "", ivAssistantHead)
        if (item.assistant == null) {
            ivAssistantHead.visibility = View.INVISIBLE
            tvAssistantName.visibility = View.INVISIBLE
            tvAssistantRole.visibility = View.INVISIBLE
        } else {
            ivAssistantHead.visibility = View.VISIBLE
            tvAssistantName.visibility = View.VISIBLE
            tvAssistantRole.visibility = View.VISIBLE
        }
        tvTeacherName.text = item.teacherName
        tvAssistantName.text = item.assistant?.realname ?: ""
        //        tvManStatus.visibility = if (item.isKnowledgeJoin) View.GONE else View.VISIBLE //是否缺勤
        val flag = item.flag
        when (flag) {
            GlobalConfig.LIVE_WAITING -> {
                tvVideoSourceStatus.text = "直播待开始"
                setImage(tvVideoSourceStatus, R.mipmap.study_detailpage_icon_end)
            }
            GlobalConfig.LIVE_LIVING -> {
                tvVideoSourceStatus.text = "直播进行中"
                setImage(tvVideoSourceStatus, R.mipmap.study_detailpage_icon_replay_sel)
            }
            GlobalConfig.LIVE_COMPLETED -> {
                tvVideoSourceStatus.text = "直播已结束"
                setImage(tvVideoSourceStatus, R.mipmap.study_detailpage_icon_end)
            }
            else -> {
                tvVideoSourceStatus.text = "回放已生成"
                setImage(tvVideoSourceStatus, R.mipmap.study_detailpage_icon_full1)
            }
        }
        //        var platform = item.supportPlatform
        tvVideoSourceStatus.setOnClickListener(object : NoDoubleClickListener() {
            override fun onDoubleClick(v: View) {
                Utils.startLiveRoom(context, item.id.toString())
            }
        })
        if (GlobalConfig.LIVE_COMPLETED == flag && item.isHasHomeWork) {
            tvHomeworkStatus.visibility = View.VISIBLE
            tvHomeworkStatus.isClickable = true
            if (!item.isSubmitAble) {
                tvHomeworkStatus.text = "作业未提交"
                tvHomeworkStatus.setOnClickListener {
                    UpLoadHomeworkActivity.start(getContext(), item.homeworkExercise?.exercise?.id
                            ?: 0)
                }
                setImage(tvHomeworkStatus, R.mipmap.study_detailpage_icon_underway)
            } else {
                val exercise = item.homeworkExercise
                if (exercise == null) {
                    tvHomeworkStatus.text = "作业未提交"
                    tvHomeworkStatus.setOnClickListener {
                        UpLoadHomeworkActivity.start(getContext(), item.homeworkExercise?.exercise?.id
                                ?: 0)
                    }
                    setImage(tvHomeworkStatus, R.mipmap.study_detailpage_icon_underway)
                    return
                }
                when {
                    exercise.exerciseResultStatus == 3 -> {
                        tvHomeworkStatus.text = "作业未提交"
                        tvHomeworkStatus.setOnClickListener {
                            UpLoadHomeworkActivity.start(getContext(), item.homeworkExercise?.exercise?.id
                                    ?: 0)
                        }
                        setImage(tvHomeworkStatus, R.mipmap.study_detailpage_icon_underway)
                    }
                    exercise.exerciseResultStatus == 2 -> {
                        tvHomeworkStatus.text = "作业已批改"
                        tvHomeworkStatus.setOnClickListener {
                            HomeworkResultActivity.start(getContext(), item.homeworkExercise?.exerciseResultId
                                    ?: 0)
                        }
                        setImage(tvHomeworkStatus, R.mipmap.study_detailpage_icon_full)
                    }
                    else -> {
                        tvHomeworkStatus.text = "作业批改中"
                        tvHomeworkStatus.setOnClickListener {
                            LookHomeworkActivity.start(getContext(), item.homeworkExercise?.exerciseResultId
                                    ?: 0)
                        }
                        setImage(tvHomeworkStatus, R.mipmap.study_detailpage_icon_full)
                    }
                }
            }
            
        } else {
            tvHomeworkStatus.text = "无作业"
            tvHomeworkStatus.isClickable = false
            setImage(tvHomeworkStatus, R.mipmap.study_detailpage_icon_underway)
        }
        //学习报告
        //        var userid,courseId,knowlegeId,token,headurl,appid
        
//        var comStr = StringBuffer()
//        var random = UUID.randomUUID().toString().replace("_".toRegex(), "")
//        var stamp = System.currentTimeMillis().toString()
//        comStr
//                .append("&courseId=${item.courseId}")
//                .append("&courseKnowledgeId=${item.id}")
//                .append("&token=${UserManager.getInstance().token}")
//                .append("&headUrl=${UserManager.getInstance().loginUser.smallHeadimg}")
//                .append("&userId=${UserManager.getInstance().userId}")
//                .append("&uri=/sccApi/${LiveSccStudyReportDetailRequest().path}")
//        //        val appSign = com.haoke91.a91edu.utils.rsa.Md5Utils.MD5_LOWERCASE(comStr.toString() + "&appKey=${}")
//        //        comStr.append("&appSign=${Api.getInstance().getSign(LiveSccStudyReportDetailRequest().path, random.toString(), stamp.toString())}")
//        var url = "${item.studyReportAppUrl}${Api.getInstance().getSign("/sccApi/" + LiveSccStudyReportDetailRequest().path, random, stamp)}${comStr.toString()}"
        //        Log.e("study===",comStr.toString())
        tvReportStatus.setOnClickListener {
            var comStr = StringBuffer()
            var random = UUID.randomUUID().toString().replace("_".toRegex(), "")
            var stamp = System.currentTimeMillis().toString()
            comStr
                    .append("&courseId=${item.courseId}")
                    .append("&courseKnowledgeId=${item.id}")
                    .append("&token=${UserManager.getInstance().token}")
                    .append("&headUrl=${UserManager.getInstance().loginUser.smallHeadimg}")
                    .append("&userId=${UserManager.getInstance().userId}")
                    .append("&uri=/sccApi/${LiveSccStudyReportDetailRequest().path}")
            var url = "${item.studyReportAppUrl}${Api.getInstance().getSign("/sccApi/" + LiveSccStudyReportDetailRequest().path, random, stamp)}${comStr.toString()}"
            GeneralWebViewActivity.start(context, url)
        }
        tvReportStatus.isEnabled = item.isHasStudyReport
        if (item.isHasStudyReport) {
            tvReportStatus.text = "学习报告"
        } else {
            tvReportStatus.text = "学习报告未生成"
        }
    }
    
    fun setAssistantHead(url: String) {
        //        mAssistantHead = url
    }
    
    /**
     * 设置textview 图片
     *
     * @param textView
     * @param id
     */
    private fun setImage(textView: TextView, @DrawableRes id: Int) {
        val drawable = ContextCompat.getDrawable(textView.context, id)
        drawable!!.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        textView.setCompoundDrawables(null, drawable, null, null)
    }
}
