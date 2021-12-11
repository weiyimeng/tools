package com.haoke91.a91edu.ui.learn

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.SpanUtils

import com.gaosiedu.scc.sdk.android.api.user.course.detail.LiveSccUserCourseDetailRequest
import com.gaosiedu.scc.sdk.android.api.user.course.detail.LiveSccUserCourseDetailResponse
import com.gaosiedu.scc.sdk.android.api.user.course.list.LiveSccUserCourseListRequest
import com.gaosiedu.scc.sdk.android.api.user.course.list.LiveSccUserCourseListResponse
import com.gaosiedu.scc.sdk.android.domain.CourseKnowledgeBean
import com.gaosiedu.scc.sdk.android.domain.TeacherBean
import com.google.gson.Gson
import com.haoke91.a91edu.GlobalConfig
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.CourseOrderAdapter
import com.haoke91.a91edu.adapter.DailyWorkAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.baselibrary.recycleview.WrapRecyclerView
import kotlinx.android.synthetic.main.activit_course_order.*

import java.util.ArrayList
import java.util.Arrays

/**
 * 项目名称：91haoke
 * 类描述： 课次详情
 * 创建人：weiyimeng
 * 创建时间：2018/7/30 下午4:51
 * 修改人：weiyimeng
 * 修改时间：2018/7/30 下午4:51
 * 修改备注：
 */
class CourseOrderActivity : BaseActivity() {
    private var wr_daily_work: WrapRecyclerView? = null
    private var ll_root: LinearLayout? = null
    private var mCourseOrderAdapter: CourseOrderAdapter? = null
    private var mtv_term: TextView? = null
    private var mtv_subject: TextView? = null
    private var mtv_courseName: TextView? = null
    private var mtv_time: TextView? = null
    private var mtv_status: TextView? = null
    private var mtv_courseProgress: TextView? = null
    
    
    override fun getLayout(): Int {
        return R.layout.activit_course_order
    }
    
    override fun initialize() {
        var courseId = 1 //课程id
        var userCourseId = 2 //用户课程
        if (intent.hasExtra("ids")) {
            try {
                val ids = intent.getStringExtra("ids")
                val split = ids.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (split != null && split.size > 1) {
                    courseId = Integer.parseInt(split[0])
                    userCourseId = Integer.parseInt(split[1])
                }
            } catch (e: Exception) {
            }
            
        }
        val header = View.inflate(this, R.layout.layout_course_order_tittle, null)
        ll_root = findViewById(R.id.ll_root)
        wr_daily_work = findViewById(R.id.wr_daily_work)
        mtv_term = header.findViewById(R.id.tv_order_holiday)
        mtv_subject = header.findViewById(R.id.tv_order_tag)
        mtv_courseName = header.findViewById(R.id.tv_order_course_name)
        mtv_time = header.findViewById(R.id.tv_limitTime)
        mtv_status = header.findViewById(R.id.tv_status)
        mtv_courseProgress = header.findViewById(R.id.tv_course_progress)
        
        val linearLayoutManager = LinearLayoutManager(this)
        wr_daily_work!!.layoutManager = linearLayoutManager
        mCourseOrderAdapter = CourseOrderAdapter(this, ArrayList())
        wr_daily_work!!.addHeaderView(header)
        wr_daily_work!!.adapter = mCourseOrderAdapter
        networkForList(courseId, userCourseId)
    }
    
    /**
     * 课次列表
     */
    private fun networkForList(courseId: Int, userCourseId: Int) {
        mEmptyView?.showLoading()
        val request = LiveSccUserCourseDetailRequest()
        request.userId = UserManager.getInstance().userId.toString()
        request.courseId = courseId
        request.id = userCourseId
        Api.getInstance().postScc(request, LiveSccUserCourseDetailResponse::class.java, object : ResponseCallback<LiveSccUserCourseDetailResponse>() {
            override fun onResponse(date: LiveSccUserCourseDetailResponse, isFromCache: Boolean) {
                if (date.data == null) {
                    mEmptyView?.showEmpty()
                    return
                }
                mEmptyView?.showContent()
                //                val s = Gson().toJson(date)
                val data = date.data
                val term1 = data.term
                mtv_term?.text = Utils.getHolidayByNumber(term1, mtv_term)
                mtv_subject?.text = data.courseSubjectNames?.substring(0, 1)
                var num = if (mtv_subject?.visibility == View.VISIBLE && mtv_term?.visibility == View.VISIBLE) {
                    mtv_courseName!!.textSize * 3
                } else {
                    mtv_courseName!!.textSize * 2 - ConvertUtils.dp2px(5f)
                }
                val sp = SpanUtils()
                sp.append(Html.fromHtml(data.name)).setLeadingMargin(num.toInt(), 0)
                mtv_courseName?.text = sp.create()
                
                //                mtv_courseName?.text = Html.fromHtml(data.name)
                val userCourseStatus = data.userCourseStatus //-1：退班，1：未完结，2：已完结  3:冻结状态 4 开课中
                if (userCourseStatus != null) {
                    when (userCourseStatus) {
                        -1 -> {
                            mtv_status!!.text = "已退课"
                            mtv_status!!.isSelected = false
                        }
                        1 -> {
                            mtv_status!!.text = "未完结"
                            mtv_status!!.isSelected = false
                        }
                        2 -> {
                            mtv_status!!.text = "已完结"
                            mtv_status!!.isSelected = true
                        }
                        3 -> {
                            mtv_status!!.text = "冻结中"
                            mtv_status!!.isSelected = true
                        }
                        4 -> {
                            mtv_status!!.text = "开课中"
                            mtv_status!!.isSelected = true
                        }
                    }
                }
                mtv_time!!.text = Html.fromHtml(data.timeremark)
                val knowledges = data.knowledges
                val total = knowledges?.size ?: 0
                mtv_courseProgress!!.text = String.format("开课%s/%s", if (data.completedCount == null) 0 else data.completedCount, total)
                mCourseOrderAdapter!!.setData(knowledges)
                //主教头像
                val assistants = data.assistants
                if (assistants != null && assistants.size > 0) {
                    val headUrl = assistants[0].headUrl
                    mCourseOrderAdapter!!.setAssistantHead(headUrl)
                }
            }
            
            override fun onEmpty(date: LiveSccUserCourseDetailResponse?, isFromCache: Boolean) {
                super.onEmpty(date, isFromCache)
                mEmptyView?.showEmpty()
            }
            
            override fun onError() {
                super.onError()
                mEmptyView?.showError()
            }
        }, "list of course order")
    }
    
    companion object {
        
        fun start(context: Context, ids: String) {
            val intent = Intent(context, CourseOrderActivity::class.java)
            intent.putExtra("ids", ids)
            context.startActivity(intent)
        }
        
        /**
         *
         * @param context
         * @param courseId 课程id
         * @param knowledgeId 课次id
         */
        fun start(context: Context, courseId: String, knowledgeId: String) {}
    }
}
