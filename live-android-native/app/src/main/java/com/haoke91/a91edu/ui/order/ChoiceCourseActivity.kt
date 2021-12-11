package com.haoke91.a91edu.ui.order

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.gaosiedu.live.sdk.android.api.course.transfer.list.LiveCourseTransferListRequest
import com.gaosiedu.live.sdk.android.api.course.transfer.list.LiveCourseTransferListResponse
import com.gaosiedu.live.sdk.android.api.user.transfer.create.LiveUserTransferCreateRequest
import com.gaosiedu.live.sdk.android.base.ApiResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.ChoiceCourseAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.haoke91.a91edu.widget.flowlayout.FlowLayout
import com.haoke91.a91edu.widget.flowlayout.TagAdapter
import com.haoke91.baselibrary.views.richEditText.TopicModel
import kotlinx.android.synthetic.main.activity_choice_course.*
import java.util.*

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
class ChoiceCourseActivity : BaseActivity() {
    private var displayId: Int = 0
    private var courseId: Int = 0
    private var oldKnowledgeId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        displayId = intent.getIntExtra("displayId", -1)
        courseId = intent.getIntExtra("course", -1)
        oldKnowledgeId = intent.getIntExtra("oldKnowledgeId", -1)
        super.onCreate(savedInstanceState)
    }
    
    lateinit var adapter: ChoiceCourseAdapter
    override fun initialize() {
        empty_view.builder().setEmptyText("没有可换课程")
        var layoutManager = LinearLayoutManager(this)
        rv_order_list.layoutManager = layoutManager
        rv_order_list.setHasFixedSize(true)
        rv_order_list.isNestedScrollingEnabled = false
        adapter = ChoiceCourseAdapter(this, null)
        rv_order_list.adapter = adapter
        
        var strings = resources.getStringArray(R.array.change_course_reason)
        val tagAdapter = object : TagAdapter<String>(strings) {
            override fun getView(parent: FlowLayout, position: Int, s: String?): View {
                val tv = LayoutInflater.from(Utils.getApp()).inflate(R.layout.item_tag_select, parent, false) as TextView
                tv.text = s
                return tv
            }
            
            override fun onSelected(position: Int, view: View) {
                
                //                view.setBackgroundResource(R.drawable.bg_tag_gray_20)
                (view as TextView).setTextColor(Color.parseColor("#FF4F00"))
                //                addReason(strings[position])
            }
            
            override fun unSelected(position: Int, view: View) {
                //                addReason(strings[position])
                (view as TextView).setTextColor(Color.parseColor("#363636"))
                //                view.setBackgroundResource(R.drawable.bg_tag_gray_20)
                
            }
        }
        tf_content.adapter = tagAdapter
        
        tv_commit.setOnClickListener(onclickListener)
        tv_cancel.setOnClickListener(onclickListener)
    }
    
    
    override fun initEvent() {
        empty_view.showLoading()
        
        var request = LiveCourseTransferListRequest()
        request.userId = UserManager.getInstance().userId
        request.courseId = courseId
        request.displayOrder = displayId
        Api.getInstance().post(request, LiveCourseTransferListResponse::class.java, object : ResponseCallback<LiveCourseTransferListResponse>() {
            override fun onResponse(date: LiveCourseTransferListResponse?, isFromCache: Boolean) {
                //                val toJson = Gson().toJson(date)
                adapter.addAll(date?.data?.list)
                empty_view.showContent()
                
            }
            
            override fun onEmpty(date: LiveCourseTransferListResponse?, isFromCache: Boolean) {
                empty_view.showEmpty()
                tv_statement.visibility = View.GONE
            }
            
            override fun onError() {
                empty_view.showError()
                
            }
        }, "")
    }
    
    private var onclickListener = object : NoDoubleClickListener() {
        override fun onDoubleClick(v: View) {
            when (v.id) {
                R.id.tv_commit -> commit()
                R.id.tv_cancel -> finish()
            }
        }
        
    }
    
    private fun commit() {
        val checkCourse = adapter?.getCheckCourse()
        if (ObjectUtils.isEmpty(checkCourse) || checkCourse == -1) {
            ToastUtils.showShort("请选择相应课次")
            return
        }
        val checkReason = tf_content.selectedList
        
        var reason = et_evaluate.text?.toString()?.trim()
        if (ObjectUtils.isEmpty(checkReason) && ObjectUtils.isEmpty(reason)) {
            ToastUtils.showShort("请填写调班原因")
            return
        }
        var strings = resources.getStringArray(R.array.change_course_reason)
        for (value in checkReason) {
            reason += if (TextUtils.isEmpty(reason)) {
                "${strings[value]}"
            } else {
                ",${strings[value]}"
            }
        }
        val request = LiveUserTransferCreateRequest()
        request.userId = UserManager.getInstance().userId
        request.changeReason = reason
        request.oldCourseId = courseId
        request.oldKnowledgeId = oldKnowledgeId
        request.newCourseId = adapter.all[checkCourse!!].id
        request.newKnowledgeId = adapter.all[checkCourse].knowledges[0].id
        Api.getInstance().post(request, ApiResponse::class.java, object : ResponseCallback<ApiResponse>() {
            override fun onResponse(date: ApiResponse?, isFromCache: Boolean) {
                finish()
                PaySuccessActivity.start(this@ChoiceCourseActivity, "", true)
            }
        }, "")
    }
    
    
    //添加调课理由
    private fun addReason(s: String) {
        var topic = TopicModel()
        topic.topicName = s
        et_evaluate.resolveTopicResult(topic)
    }
    
    
    override fun getLayout(): Int {
        return R.layout.activity_choice_course
        
    }
    
    
    companion object {
        fun start(context: Context, courseId: Int, displayId: Int?, oldKnowledgeId: Int?) {
            val intent = Intent(context, ChoiceCourseActivity::class.java)
            intent.putExtra("course", courseId)
            intent.putExtra("displayId", displayId)
            intent.putExtra("oldKnowledgeId", oldKnowledgeId)
            context.startActivity(intent)
        }
    }
}
