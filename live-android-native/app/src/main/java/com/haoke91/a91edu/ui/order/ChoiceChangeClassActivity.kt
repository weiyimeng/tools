package com.haoke91.a91edu.ui.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ObjectUtils
import com.gaosiedu.live.sdk.android.api.course.change.LiveCourseChangeListRequest
import com.gaosiedu.live.sdk.android.api.course.change.LiveCourseChangeListResponse
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.ApplyBackOrderAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseLoadMoreActivity
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.baselibrary.event.RxBus
import com.haoke91.baselibrary.recycleview.HorizontalDividerItemDecoration
import com.haoke91.baselibrary.utils.ACallBack
import com.scwang.smartrefresh.layout.api.RefreshLayout
import java.util.ArrayList

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/21 9:51 AM
 * 修改人：weiyimeng
 * 修改时间：2018/11/21 9:51 AM
 * 修改备注：
 * @version
 */
class ChoiceChangeClassActivity : BaseLoadMoreActivity() {
    var course: OrderItemDomain? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        course = intent.getSerializableExtra(courseKey) as? OrderItemDomain
        super.onCreate(savedInstanceState)
        
    }
    
    lateinit var adapter: ApplyBackOrderAdapter
    override fun initialize() {
        super.initialize()
        empty_view.builder().setEmptyText("没有可调课程")
        refreshLayout.setEnableRefresh(false)
        empty_view.showLoading()
        adapter = ApplyBackOrderAdapter(this, null)
        adapter.setBoxVisibility(View.GONE)
        rv_story_list.adapter = adapter
        rv_story_list.addItemDecoration(HorizontalDividerItemDecoration.Builder(this).color(
                resources.getColor(R.color.FBFBFB))
                .size(resources.getDimension(R.dimen.dp_1).toInt())
                .build())
        adapter.setOnOrderChangeListener(ACallBack { item ->
            if (ObjectUtils.isEmpty(item)) {
                return@ACallBack
            }
            if (intent.getBooleanExtra("isFirst", true)) {
                item.orderNo = course?.orderNo
                item.id = course?.id ?: 0
                ChangeClassActivity.start(this@ChoiceChangeClassActivity, course, item)
            } else {
                RxBus.getIntanceBus().post(item)
            }
            finish()
            
        })
    }
    
    override fun initEvent() {
        onLoadMore(null)
    }
    
    var courseList = ArrayList<OrderItemDomain>()
    private var pagerNum: Int = 1
    override fun onLoadMore(refreshLayout: RefreshLayout?) {
        val request = LiveCourseChangeListRequest()
        request.userId = UserManager.getInstance().userId
        request.pageNum = pagerNum++
        request.courseId = if (ObjectUtils.isEmpty(course)) {
            intent.getIntExtra(courseId, 0)
        } else {
            course!!.courseId
        }
        Api.getInstance().post(request, LiveCourseChangeListResponse::class.java, object : ResponseCallback<LiveCourseChangeListResponse>() {
            override fun onResponse(date: LiveCourseChangeListResponse, isFromCache: Boolean) {
                empty_view.showContent()
                for (item in date.data.list) {
                    val courseItem = OrderItemDomain()
                    courseItem.course = item
                    courseItem.courseId = item.id
                    courseItem.price = item.price
                    courseList.add(courseItem)
                }
                adapter.addAll(courseList)
                
                if (date.data.isLastPage) {
                    refreshLayout?.finishLoadMoreWithNoMoreData()
                } else {
                    refreshLayout?.finishLoadMore()
                }
            }
            
            override fun onEmpty(date: LiveCourseChangeListResponse?, isFromCache: Boolean) {
                if (ObjectUtils.isEmpty(adapter.all)) {
                    empty_view.showEmpty()
                }
                refreshLayout?.finishLoadMoreWithNoMoreData()
                
            }
        }, "")
    }
    
    companion object {
        var courseKey = "course"
        var courseId = "courseId"
        fun start(context: Context, courseId: OrderItemDomain?) {
            val intent = Intent(context, ChoiceChangeClassActivity::class.java)
            intent.putExtra(courseKey, courseId)
            intent.putExtra("isFirst", true)
            context.startActivity(intent)
        }
        
        fun start(context: Context, courseId: Int?) {
            val intent = Intent(context, ChoiceChangeClassActivity::class.java)
            intent.putExtra(ChoiceChangeClassActivity.courseId, courseId)
            intent.putExtra("isFirst", false)
            context.startActivity(intent)
        }
    }
}
