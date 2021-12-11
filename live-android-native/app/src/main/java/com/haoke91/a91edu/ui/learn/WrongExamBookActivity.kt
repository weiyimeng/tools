package com.haoke91.a91edu.ui.learn

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView

import com.blankj.utilcode.util.ObjectUtils
import com.gaosiedu.live.sdk.android.domain.DictionaryDomain
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.mine.LiveSccUserWrongQuestionBookDetailRequest
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.mine.LiveSccUserWrongQuestionBookDetailResponse
import com.gaosiedu.scc.sdk.android.domain.SccUserWrongQuestionBookBean
import com.haoke91.a91edu.R
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.presenter.WrongExamBookPresenter
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.ui.course.HomeCourseFragment
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.view.WrongExamBookView
import com.haoke91.a91edu.widget.HomeCoursePopup
import com.haoke91.a91edu.widget.dropdownlayout.MaskView
import com.haoke91.baselibrary.recycleview.HorizontalDividerItemDecoration
import com.haoke91.baselibrary.recycleview.WrapRecyclerView
import com.haoke91.baselibrary.views.emptyview.EmptyView
import com.haoke91.baselibrary.views.popwindow.BasePopup
import java.lang.Exception

import java.util.ArrayList
import java.util.Arrays

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/8/29 16:21
 */
class WrongExamBookActivity : BaseActivity(), WrongExamBookView {
    
    private lateinit var mRecyclerViewBook: WrapRecyclerView
    private lateinit var mEmptyView: EmptyView
    private var mGridBooksAdapter: GridBooksAdapter? = null
    private var mMaskView: MaskView? = null
    private var mGradePopWindow: HomeCoursePopup? = null
    private var mFilterGradeBtn: TextView? = null
    
    override fun getLayout(): Int {
        return R.layout.activity_wrongexambook
    }
    
    override fun initialize() {
        //        mPresenter = new WrongExamBookPresenter(null, this);
        mEmptyView = id(R.id.emptyView)
        mRecyclerViewBook = id(R.id.recyclerView)
        mFilterGradeBtn = id(R.id.filtraGradebtn)
        val layoutManager = GridLayoutManager(this, 3)
        mRecyclerViewBook.layoutManager = layoutManager
        mGridBooksAdapter = GridBooksAdapter()
        mRecyclerViewBook.adapter = mGridBooksAdapter
        networkForList(0)
        
    }
    
    override fun onSuccessGetBook() {
        mEmptyView.showContent()
        
    }
    
    fun clickEvent(view: View) {
        when (view.id) {
            R.id.backBtn -> onBackPressed()
            R.id.filtraGradebtn -> {
                val container2 = findViewById<FrameLayout>(android.R.id.content)
                if (mMaskView == null) {
                    mMaskView = MaskView(this)
                }
                if (ObjectUtils.isEmpty(mGradePopWindow)) {
                    mGradePopWindow = HomeCoursePopup()
                            .setContext(this)
                            .setFocusAndOutsideEnable(true)
                            .setOnDismissListener { container2.removeView(mMaskView) }
                            .apply()
                }
                
                val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                params.topMargin = 400
                mMaskView!!.layoutParams = params
                
                if (!mGradePopWindow!!.isShowing) {
                    container2.addView(mMaskView)
                }
                mGradePopWindow!!.showAsDropDown(mFilterGradeBtn, 0, 0)
                mGradePopWindow!!.setOnViewClickListener { item ->
                    mFilterGradeBtn!!.text = item.dicName
                    networkForList(try {
                        item.dicValue.toInt()
                    } catch (e: Exception) {
                        0
                    })
                    mGradePopWindow!!.dismiss()
                }
            }
        } //                mGradePopWindow.setData(Arrays.asList("全部年级", "一年级", "二年级", "三年级", "四年级"
        //                    , "五年级", "六年级", "初一", "初二", "初三", "高一"));
    }
    
    /**
     * network
     * @param gradeId 年级
     */
    private fun networkForList(gradeId: Int) {
        mEmptyView.showLoading()
        val request = LiveSccUserWrongQuestionBookDetailRequest()
        request.userId = UserManager.getInstance().userId.toString()
        request.gradeId = gradeId
        Api.getInstance().postScc(request, LiveSccUserWrongQuestionBookDetailResponse::class.java, object : ResponseCallback<LiveSccUserWrongQuestionBookDetailResponse>() {
            override fun onResponse(date: LiveSccUserWrongQuestionBookDetailResponse, isFromCache: Boolean) {
                if (date.data != null && date.data.list != null) {
                    if (date.data.list.size == 0) {
                        mEmptyView.showEmpty()
                        return
                    }
                    mEmptyView.showContent()
                    mGridBooksAdapter!!.setData(date.data.list)
                } else {
                    mEmptyView.showEmpty()
                }
            }
            
            override fun onEmpty(date: LiveSccUserWrongQuestionBookDetailResponse?, isFromCache: Boolean) {
                super.onEmpty(date, isFromCache)
                mEmptyView.showEmpty()
            }
            
            override fun onError() {
                super.onError()
                mEmptyView.showError()
            }
        }, "")
    }
    
    private inner class GridBooksAdapter : RecyclerView.Adapter<GridBooksAdapter.Holder>() {
        internal var data: List<SccUserWrongQuestionBookBean>? = ArrayList()
        
        fun setData(list: List<SccUserWrongQuestionBookBean>) {
            this.data = list
            notifyDataSetChanged()
        }
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gv_wrongbook, null)
            return Holder(view)
        }
        
        override fun onBindViewHolder(holder: Holder, position: Int) {
            val bean = data!![position]
            holder.tv_nums.text = String.format(holder.tv_nums.text.toString().trim(), bean.correctNum, bean.wrongQuestionsNum)
            holder.tv_centerTxt.text = bean.subjectName
            holder.root.setOnClickListener {
                val intent = Intent(this@WrongExamBookActivity, WrongExamListActivity::class.java)
                intent.putExtra("subject", bean.subjectId)
                startActivity(intent)
                //                    startActivity(WrongExamListActivity.class);
            }
        }
        
        override fun getItemCount(): Int {
            return if (data != null) data!!.size else 0
        }
        
        internal inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tv_nums: TextView
            var tv_centerTxt: TextView
            var root: View
            
            init {
                tv_nums = itemView.findViewById(R.id.tv_nums)
                tv_centerTxt = itemView.findViewById(R.id.tv_centerTxt)
                root = itemView.findViewById(R.id.item_root)
            }
        }
    }
    
}
