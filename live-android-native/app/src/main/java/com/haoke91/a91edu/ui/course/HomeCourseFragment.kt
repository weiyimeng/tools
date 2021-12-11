package com.haoke91.a91edu.ui.course

import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup

import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.SpanUtils
import com.gaosiedu.live.sdk.android.api.course.aggregation.LiveCourseAggregationRequest
import com.gaosiedu.live.sdk.android.api.course.aggregation.LiveCourseAggregationResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.R.string.garden
import com.haoke91.a91edu.adapter.HomeCourseAdapter
import com.haoke91.a91edu.fragment.BaseFragment
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.lzy.okgo.cache.CacheMode
import kotlinx.android.synthetic.main.fragment_search_recommend.*


/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/19 下午4:17
 * 修改人：weiyimeng
 * 修改时间：2018/7/19 下午4:17
 * 修改备注：
 */
class HomeCourseFragment : BaseFragment() {
    private val TAG = "homeFragmentList==="
    // SmartRefreshLayout refreshLayout;
    //课程id
    private var courseType: String? = null
    
    private var searchResultAdapter: HomeCourseAdapter? = null
    
    private var garden: String? = null
    
    override fun getLayout(): Int {
        return R.layout.fragment_search_recommend
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        empty_view.container.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        empty_view.progressBar.y = 140f
        courseType = arguments?.getString(ORDER_TYPE)
        var errorButtonText = SpanUtils().append("请检查网络设置，或尝试").append("刷新页面").setClickSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                getDate()
            }
            
            override fun updateDrawState(ds: TextPaint) {
                ds.color = Color.parseColor("#75C82B")
                ds.bgColor = Color.WHITE
                //                ds.isUnderlineText = true
            }
        }).create()
        empty_view.builder().setErrorButtonText(errorButtonText)
        empty_view?.showLoading()
        
    }
    
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isViewInitiated && isVisibleToUser) {
            //            if (searchResultAdapter != null && searchResultAdapter.all != null && searchResultAdapter.all.size > 0) {
            calculateRecyclerViewHeight()
            //            }
        }
    }
    
    fun calculateRecyclerViewHeight() {
        empty_view?.post {
            var height = 0
            if (rv_search_recommend.layoutManager.itemCount > 0) {
                var child = rv_search_recommend.layoutManager.findViewByPosition(0)
                var childHeight = 0
                childHeight = if (child == null) {
                    var v = layoutInflater.inflate(R.layout.item_subjectlist_main, null)
                    var w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    var h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    v.measure(w, h)
                    v.measuredHeight
                } else {
                    child.measuredHeight
                }
//                Log.e("home===", "chilcHeight==" + childHeight + (child == null))
                height = childHeight * rv_search_recommend.layoutManager.itemCount
            }
            if (height > 0) {
                val layoutParams = rv_search_recommend.layoutParams
                layoutParams.height = height
                rv_search_recommend.layoutParams = layoutParams
                var lp = empty_view.layoutParams
                lp.height = height
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT
                empty_view.layoutParams = lp
                
            } else {
                var lp = empty_view.layoutParams
                height = (300 * mContext.resources.displayMetrics.density).toInt()
                lp.height = height
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT
                empty_view.layoutParams = lp
            }
            var parent = empty_view.parent
            while (parent !is ViewPager) {
                parent = parent.parent
            }
            var lp = parent.layoutParams
            lp.height = height
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT
            parent.layoutParams = lp
//            Log.e(TAG, "height==" + height)
        }
    }
    
    override fun fetchData() {
        searchResultAdapter = HomeCourseAdapter(mContext, null)
        rv_search_recommend.adapter = searchResultAdapter
        rv_search_recommend.setHasFixedSize(true)
        rv_search_recommend.isNestedScrollingEnabled = false
        val linearLayoutManager = LinearLayoutManager(activity)
        rv_search_recommend.layoutManager = linearLayoutManager
        //        (rv_search_recommend.layoutParams as RelativeLayout.LayoutParams).height
        searchResultAdapter?.setOnItemClickListener { _, position ->
            //                CourseDetailActivity.Companion.start(mContext);
            val all = searchResultAdapter!!.all
            SeriesClassActivity.start(mContext, all[position])
        }
        
        getDate()
        
        
    }
    
    private fun getDate() {
        empty_view?.showLoading()
        val request = LiveCourseAggregationRequest()
        //        request.setSubject();
        request.subject = courseType
        if (!TextUtils.isEmpty(garden) && !"0".equals(garden, ignoreCase = true)) {
            request.grade = garden
        }
        Api.getInstance().post(request, LiveCourseAggregationResponse::class.java, object : ResponseCallback<LiveCourseAggregationResponse>() {
            override fun onResponse(date: LiveCourseAggregationResponse, isFromCache: Boolean) {
                //                String s = new Gson().toJson(date);
                empty_view?.showContent()
                searchResultAdapter?.setData(date.data.list)
                if (isVisibleToUser) {
                    calculateRecyclerViewHeight()
                }
            }
            
            override fun onEmpty(date: LiveCourseAggregationResponse, isFromCache: Boolean) {
                empty_view?.showEmpty()
                if (isVisibleToUser)
                    calculateRecyclerViewHeight()
            }
            
            override fun onFail(date: LiveCourseAggregationResponse?, isFromCache: Boolean) {
                super.onFail(date, isFromCache)
                if (isVisibleToUser)
                    calculateRecyclerViewHeight()
            }
            
            override fun onError() {
                if (searchResultAdapter?.all?.isEmpty() == true) {
                    empty_view?.showError()
                } else {
                    empty_view?.showContent()
                }
                if (isVisibleToUser)
                    calculateRecyclerViewHeight()
                
            }
        }, CacheMode.FIRST_CACHE_THEN_REQUEST, ConvertUtils.timeSpan2Millis(5, TimeConstants.HOUR), courseType + garden)
    }
    
    fun refreshDate(garden: String) {
        this.garden = garden
        getDate()
    }
    
    fun prepareRefreshDate(garden: String) {
        this.garden = garden
        isDataInitiated = false
    }
    
    companion object {
        private val ORDER_TYPE = "dicValue"
        
        fun newInstance(dicValue: String): HomeCourseFragment {
            val fragment = HomeCourseFragment()
            val bundle = Bundle()
            bundle.putString(ORDER_TYPE, dicValue)
            fragment.arguments = bundle
            return fragment
        }
    }
}
