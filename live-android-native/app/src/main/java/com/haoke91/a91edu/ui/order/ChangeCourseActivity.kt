package com.haoke91.a91edu.ui.order

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ToastUtils
import com.gaosiedu.live.sdk.android.api.user.transfer.courseDetail.LiveUserTransferCourseDetailRequest
import com.gaosiedu.live.sdk.android.api.user.transfer.courseDetail.LiveUserTransferCourseDetailResponse
import com.gaosiedu.live.sdk.android.domain.CourseDomain
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.ChoiceCourseOrderAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.ui.course.CourseDetailActivity
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.NoDoubleClickListener
import kotlinx.android.synthetic.main.activity_change_order.*
import kotlinx.android.synthetic.main.layout_change_course_head.*

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/13 11:31 AM
 * 修改人：weiyimeng
 * 修改时间：2018/11/13 11:31 AM
 * 修改备注： 调课
 * @version
 */
class ChangeCourseActivity : BaseActivity() {
    private lateinit var orderItem: OrderItemDomain
    
    override fun initialize() {
        orderItem = intent.getSerializableExtra(ORDER) as OrderItemDomain
        val request = LiveUserTransferCourseDetailRequest()
        empty_view.showLoading()
        request.userId = UserManager.getInstance().userId
        request.courseId = orderItem.courseId
        Api.getInstance().post(request, LiveUserTransferCourseDetailResponse::class.java, object : ResponseCallback<LiveUserTransferCourseDetailResponse>() {
            override fun onResponse(date: LiveUserTransferCourseDetailResponse, isFromCache: Boolean) {
                empty_view.showContent()
                setDate(date)
            }
            
            override fun onEmpty(date: LiveUserTransferCourseDetailResponse?, isFromCache: Boolean) {
                empty_view.showEmpty()
            }
            
            override fun onError() {
                empty_view.showError()
            }
        }, "")
        val header = View.inflate(this, R.layout.layout_change_course_head, null)
        rv_order_list.addHeaderView(header)
        setHeadDate(orderItem.course, header)
        tv_cancel.setOnClickListener(onclickListener)
    }
    
    private fun setHeadDate(course: CourseDomain, header: View) {
        header.findViewById<TextView>(R.id.tv_order_tag).text = course.courseSubjectNames?.substring(0, 1)
        //        helper.getTextView(R.id.tv_order_holiday).text = Utils.getHolidayByNumber(course.term)
        val tvHoliday = header.findViewById<TextView>(R.id.tv_order_holiday)
        if (TextUtils.isEmpty(com.haoke91.a91edu.utils.Utils.getHolidayByNumber(course.term, tvHoliday))) {
            tvHoliday.visibility = View.GONE
        } else {
            tvHoliday.visibility = View.VISIBLE
            tvHoliday.text = com.haoke91.a91edu.utils.Utils.getHolidayByNumber(course.term, tvHoliday)
        }
        header.findViewById<TextView>(R.id.tv_order_course_name).text = Html.fromHtml(course.name)
        header.findViewById<TextView>(R.id.tv_order_course_time).text = course.timeremark
        if (!ObjectUtils.isEmpty(course.teachers)) {
            GlideUtils.loadHead(this, course.teachers[0].headUrl, header.findViewById(R.id.iv_order_teacher_icon))
            header.findViewById<TextView>(R.id.tv_order_teacher_name).text = course.teachers[0].realname
        }
        if (!ObjectUtils.isEmpty(course.courseClassDomain)) {
            header.findViewById<View>(R.id.iv_order_assistant_icon).visibility = View.VISIBLE
            header.findViewById<View>(R.id.tv_assistant_name).visibility = View.VISIBLE
            header.findViewById<View>(R.id.tv_assistant).visibility = View.VISIBLE
            GlideUtils.loadHead(this, course.courseClassDomain.headUrl, header.findViewById(R.id.iv_order_assistant_icon))
            header.findViewById<TextView>(R.id.tv_assistant_name).text = course.courseClassDomain.teacherName
        } else {
            header.findViewById<View>(R.id.iv_order_assistant_icon).visibility = View.INVISIBLE
            header.findViewById<View>(R.id.tv_assistant_name).visibility = View.INVISIBLE
            header.findViewById<View>(R.id.tv_assistant).visibility = View.INVISIBLE
        }
        
        if (course.teachers.size >= 2) {
            GlideUtils.load(this, course.teachers[1].headUrl, header.findViewById(R.id.iv_teacher_two_icon))
            header.findViewById<TextView>(R.id.tv_order_teacher_name_two).text = course.teachers[1].realname
            if (course.teachers.size == 2) {
                header.findViewById<View>(R.id.iv_more).visibility = View.GONE
            } else {
                header.findViewById<View>(R.id.iv_more).visibility = View.VISIBLE
            }
        } else {
            header.findViewById<View>(R.id.iv_teacher_two_icon).visibility = View.GONE
            header.findViewById<View>(R.id.tv_order_teacher_name_two).visibility = View.GONE
            header.findViewById<View>(R.id.tv_main_teacher).visibility = View.GONE
        }
        header.findViewById<View>(R.id.cl_class_head).setOnClickListener(onclickListener)
        tv_cancel.setOnClickListener(onclickListener)
        tv_next_step.setOnClickListener(onclickListener)
        
        
    }
    
    private var onclickListener = object : NoDoubleClickListener() {
        override fun onDoubleClick(v: View) {
            when (v.id) {
                R.id.tv_cancel -> finish()
                R.id.cl_class_head -> CourseDetailActivity.start(this@ChangeCourseActivity, orderItem.course.id)
                R.id.tv_cancel -> finish()
                R.id.tv_next_step -> nextStep()
                
            }
        }
        
    }
    
    private fun nextStep() {
        val checkCourse = adapter?.getCheckCourse()
        if (ObjectUtils.isEmpty(checkCourse) || checkCourse == -1) {
            ToastUtils.showShort("请选择相应课次")
            return
        }
        ChoiceCourseActivity.start(this, orderItem.course.id, adapter?.getCheckCourse(), adapter?.getCourseId())
    }
    
    private var adapter: ChoiceCourseOrderAdapter? = null
    private fun setDate(date: LiveUserTransferCourseDetailResponse) {
        
        var layoutManager = LinearLayoutManager(this)
        rv_order_list.layoutManager = layoutManager
        adapter = ChoiceCourseOrderAdapter(this, date.data.knowledges)
        rv_order_list.adapter = adapter
    }
    
    
    override fun getLayout(): Int {
        return R.layout.activity_change_order
        
    }
    
    
    companion object {
        private val ORDER = "orderItem"
        
        fun start(context: Context, orderItem: OrderItemDomain) {
            val intent = Intent(context, ChangeCourseActivity::class.java)
            intent.putExtra(ORDER, orderItem)
            context.startActivity(intent)
        }
    }
}
