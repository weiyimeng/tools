package com.haoke91.a91edu.ui.course

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.blankj.utilcode.util.ObjectUtils
import com.gaosiedu.live.sdk.android.api.course.list.LiveCourseListRequest
import com.gaosiedu.live.sdk.android.api.course.list.LiveCourseListResponse
import com.gaosiedu.live.sdk.android.domain.DictionaryDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.SpecialClassAdapter
import com.haoke91.a91edu.fragment.BaseFragment
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.learn.DropDownFragment
import com.haoke91.a91edu.ui.learn.GardenFragment
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.dropdownlayout.ClassifyCourseView
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import kotlinx.android.synthetic.main.fragment_searchresult.*

import java.util.ArrayList

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/16 下午4:38
 * 修改人：weiyimeng
 * 修改时间：2018/7/16 下午4:38
 * 修改备注：
 */
class SearchResultFragment : BaseFragment() {
    private lateinit var searchResultAdapter: SpecialClassAdapter
    
    private val onLoadMoreListener = OnLoadMoreListener { refreshLayout ->
        num++
        val request = LiveCourseListRequest()
        request.pageNum = num
        //            request.setType(String.valueOf(1));
        request.keywords = keyword
        if (subjectId != 0) {
            request.subjectId = subjectId
        }
        if (grade != 0) {
            request.grade = grade
        }
        if (term != 0) {
            request.term = term
        }
        Api.getInstance().post(request, LiveCourseListResponse::class.java, object : ResponseCallback<LiveCourseListResponse>() {
            override fun onResponse(date: LiveCourseListResponse, isFromCache: Boolean) {
                setCourseDate(date.data.list, true)
                refreshLayout?.finishLoadMore()
                if (date.data.isLastPage) {
                    refreshLayout?.finishLoadMoreWithNoMoreData()
                }
            }
            
            override fun onEmpty(date: LiveCourseListResponse, isFromCache: Boolean) {
                refreshLayout?.finishLoadMoreWithNoMoreData()
                
            }
            
            
        }, "")
    }
    private var keyword: String? = null
    private var num = 1
    private var grade: Int = 0
    private var term: Int = 0
    private var subjectId: Int = 0
    private val OnItemClickListener = DropDownFragment.OnItemSelectedClickListener { v, tag, _ ->
        val item = v.tag as DictionaryDomain
        if (ObjectUtils.isEmpty(item)) {
            return@OnItemSelectedClickListener
        }
        when (tag) {
            0 -> {
                subjectId = Integer.parseInt(item.dicValue)
                view_classify.setText("", item.dicName, "")
                
                
            }
            1 -> {
                term = Integer.parseInt(item.dicValue)
                view_classify.setText("", "", item.dicName)
                
            }
            3 -> {
                grade = Integer.parseInt(item.dicValue)
                view_classify.setText(item.dicName, "", "")
                
            }
        }
        dropdown_layout.closeMenu()
        getDate()
    }
    
    override fun getLayout(): Int {
        return R.layout.fragment_searchresult
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keyword = arguments!!.getString("keyword")
        val fragments = ArrayList<Fragment>()
        //        fragments.add(DropDownFragment.instance(Arrays.asList(getResources().getStringArray(R.array.allClass))));
        //        //  fragments.add(new StudyFragment());
        //        fragments.add(DropDownFragment.instance(Arrays.asList(getResources().getStringArray(R.array.all_course))));
        //        fragments.add(DropDownFragment.instance(Arrays.asList(getResources().getStringArray(R.array.all_data))));
        
        fragments.add(GardenFragment.instance().setOnItemClickListener(OnItemClickListener, 3))
        val courseInfo = UserManager.getInstance().courseInfo
        fragments.add(DropDownFragment.getInstance(courseInfo).setOnItemClickListener(OnItemClickListener, 0))
        fragments.add(DropDownFragment.getInstance(UserManager.getInstance().termInfo).setOnItemClickListener(OnItemClickListener, 1))
        
        menuLayout.setFragmentManager(activity!!.supportFragmentManager)
        menuLayout.bindFragments(fragments)
        val linearLayoutManager = LinearLayoutManager(activity)
        rv_course_list.layoutManager = linearLayoutManager
        getDate()
        searchResultAdapter = SpecialClassAdapter(context!!, null)
        searchResultAdapter.setOnItemClickListener { _, _ -> CourseDetailActivity.start(mContext, 0) }
        rv_course_list.adapter = searchResultAdapter
        view_classify.setOnClassifyChangeListener(object : ClassifyCourseView.ClassifyChangeListener {
            override fun onClassifyChange(position: Int) {
                dropdown_layout.showMenuAt(position)
            }
        })
        refreshLayout.setOnLoadMoreListener(onLoadMoreListener)
        menuLayout.setCloseMenuListner({ view_classify.resetStatus() })
        
    }
    
    private fun getDate() {
        emptyView.showLoading()
        val request = LiveCourseListRequest()
        request.pageNum = num
        if (subjectId != 0) {
            request.subjectId = subjectId
        }
        if (grade != 0) {
            request.grade = grade
        }
        if (term != 0) {
            request.term = term
        }
        //        request.setType(String.valueOf(2));
        request.keywords = keyword
        Api.getInstance().post(request, LiveCourseListResponse::class.java, object : ResponseCallback<LiveCourseListResponse>() {
            override fun onResponse(date: LiveCourseListResponse, isFromCache: Boolean) {
                //                val s = Gson().toJson(date)
                emptyView?.showContent()
                setCourseDate(date.data.list, false)
            }
            
            override fun onEmpty(date: LiveCourseListResponse?, isFromCache: Boolean) {
                emptyView?.showEmpty()
            }
            
            override fun onError() {
                emptyView?.showError()
            }
        }, "")
        
    }
    
    private fun setCourseDate(date: List<LiveCourseListResponse.ListData>, loadMore: Boolean) {
        if (loadMore) {
            searchResultAdapter.addAll(date)
        } else {
            searchResultAdapter.setData(date)
        }
    }
    
    companion object {
        
        fun newInstance(keyword: String): SearchResultFragment {
            val fragment = SearchResultFragment()
            val bundle = Bundle()
            bundle.putString("keyword", keyword)
            fragment.arguments = bundle
            return fragment
        }
    }
}
