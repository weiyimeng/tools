package gaosi.com.learn.studentapp.main.adapter

import android.graphics.Color
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gsbaselib.utils.LangUtil
import gaosi.com.learn.R
import gaosi.com.learn.bean.main.ClassInfo
import gaosi.com.learn.bean.main.StudyTaskInfoBean
import gaosi.com.learn.studentapp.classlesson.status.SubjectId
import gaosi.com.learn.view.CountDownTimeTextView
import java.util.*

/**
 * description:
 * created by huangshan on 2020/6/9 下午5:42
 */
class MoreLessonAdapter: BaseQuickAdapter<StudyTaskInfoBean, BaseViewHolder>(R.layout.item_more_lesson) {

    override fun convert(helper: BaseViewHolder?, data: StudyTaskInfoBean?) {
        helper?.apply {
            val ivSubject = getView<ImageView>(R.id.ivSubject)
            val tvLessonName = getView<TextView>(R.id.tvLessonName)
            val tvClassName = getView<TextView>(R.id.tvClassName)
            val tvTimeTitle = getView<TextView>(R.id.tvTimeTitle)
            val tvTime = getView<CountDownTimeTextView>(R.id.tvTime)
            val tvLessonStatus = getView<TextView>(R.id.tvLessonStatus)
            val llLiving = getView<LinearLayout>(R.id.llLiving)
            val ivLiveStatus = getView<ImageView>(R.id.ivLiveStatus)
            val tvLiveStatus = getView<TextView>(R.id.tvLiveStatus)
            val studyTaskInfoLayout = getView<LinearLayout>(R.id.studyTaskInfoLayout)

            //关闭
            tvTime?.stop()

            addOnClickListener(R.id.llLiving)
            addOnClickListener(R.id.rlLessonInfo)

            if (data?.subjectId == 0) {
                //多学科不显示
                ivSubject?.visibility = View.GONE
            } else {
                ivSubject?.visibility = View.VISIBLE
                ivSubject?.setImageResource(SubjectId.getSubjectLabel(data?.subjectId))
            }
            tvLessonName?.text = "第" + data?.lessonNum + "讲 " + data?.lessonName
            tvClassName?.text = data?.className

            var classInfo: ClassInfo? = null
            when (data?.type) {
                1 -> classInfo = data.axxOnlineTol
                2 -> classInfo = data.axxOnline
                3, 4 -> classInfo = data.dtLive
                5 -> classInfo = data.foreignCourse
                6 -> classInfo = data.axxOffline
            }
            tvTimeTitle?.text = "上课时间"
            tvTimeTitle?.visibility = View.VISIBLE
            tvTime?.visibility = View.VISIBLE
            val beginTimeDate = Date(classInfo?.lessonBeginTime ?: 0)
            val endTimeDate = Date(classInfo?.lessonEndTime ?: 0)
            val beginHHmmTimeString = LangUtil.date2String("HH:mm", beginTimeDate)
            val endHHmmTimeString = LangUtil.date2String("HH:mm", endTimeDate)
            tvTime?.text = "$beginHHmmTimeString-$endHHmmTimeString"
            if (data?.type == 6) {
                //线下课
                tvLessonStatus?.visibility = View.VISIBLE
                llLiving?.visibility = View.GONE
                when (classInfo?.lessonStatus) {
                    1 -> {
                        tvLessonStatus?.text = "线下课未开始"
                        tvLessonStatus?.setTextColor(Color.parseColor("#FAAD14"))
                    }
                    3 -> {
                        tvLessonStatus?.text = "线下课进行中"
                        tvLessonStatus?.setTextColor(Color.parseColor("#FAAD14"))
                    }
                    6 -> {
                        tvLessonStatus?.text = "线下课已结束"
                        tvLessonStatus?.setTextColor(Color.parseColor("#939CB6"))
                    }
                    else -> {
                        studyTaskInfoLayout?.visibility = View.INVISIBLE
                    }
                }
            } else {
                when (classInfo?.lessonStatus) {
                    1 -> {
                        tvLessonStatus?.setTextColor(Color.parseColor("#FAAD14"))
                        tvLessonStatus?.visibility = View.VISIBLE
                        llLiving?.visibility = View.GONE
                        when (data?.type) {
                            1,2 -> {
                                //在线课TOL 在线小班
                                tvLessonStatus?.text = "直播未开始"
                            }
                            3 -> {
                                //双师课
                                tvLessonStatus?.text = "双师课未开始"
                            }
                            4 -> {
                                //AI好课
                                tvLessonStatus?.text = "AI好课未开始"
                            }
                            5 -> {
                                //外教课
                                tvLessonStatus?.text = "外教课未开始"
                            }
                        }
                    }
                    2 -> {
                        tvLessonStatus.visibility = View.GONE
                        llLiving.visibility = View.VISIBLE
                        Glide.with(mContext).asGif().load(R.drawable.app_icon_living).into(ivLiveStatus)
                        tvLiveStatus.text = "进入直播"
                        when (data?.type) {
                            4 -> {
                                //AI好课
                                tvTimeTitle.text = "即将开课"
                            }
                            1,2,3,5 -> {
                                //在线课TOL 在线小班 双师课 外教课
                                tvTimeTitle.text = "距离上课"
                                val elapseTime = classInfo.lessonBeginTime?.minus(System.currentTimeMillis())
                                        ?: 0L
                                tvTime.start(elapseTime, object : CountDownTimeTextView.OnFinishListener {
                                    override fun onFinish() {
                                        tvTime.stop()
                                        tvTimeTitle.text = "上课时间"
                                        tvTime.text = "$beginHHmmTimeString-$endHHmmTimeString"
                                    }
                                })
                                tvTime.start()
                            }
                        }
                    }
                    3 -> {
                        tvTimeTitle?.text = "正在直播"
                        tvLessonStatus?.visibility = View.GONE
                        llLiving.visibility = View.VISIBLE
                        Glide.with(mContext).asGif().load(R.drawable.app_icon_living).into(ivLiveStatus)
                        tvLiveStatus.text = "进入直播"
                    }
                    4 -> {
                        tvLessonStatus?.text = "回放生成中"
                        tvLessonStatus?.setTextColor(Color.parseColor("#939CB6"))
                        tvLessonStatus?.visibility = View.VISIBLE
                        llLiving?.visibility = View.GONE
                    }
                    5 -> {
                        tvLessonStatus?.visibility = View.GONE
                        llLiving.visibility = View.VISIBLE
                        ivLiveStatus.setImageResource(R.drawable.app_icon_live_play)
                        tvLiveStatus.text = "观看回放"
                    }
                }
            }
        }
    }
}