package com.haoke91.a91edu.ui.found

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.gaosiedu.live.sdk.android.api.user.gold.history.LiveUserGoldHistoryRequest
import com.gaosiedu.live.sdk.android.api.user.gold.history.LiveUserGoldHistoryResponse

import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.ExchangeHistoryAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.order.BaseLoadMoreFragment
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.baselibrary.recycleview.HorizontalDividerItemDecoration

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/13 下午2:10
 * 修改人：weiyimeng
 * 修改时间：2018/8/13 下午2:10
 * 修改备注：
 */
class ExchangeVirtualHistoryFragment : BaseLoadMoreFragment() {
    
    private val STARTPAGE = 1
    private var mCurrentPage = STARTPAGE
    private lateinit var mGridAdapter: ExchangeHistoryAdapter
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    
    override fun fetchData() {
        empty_view.showContent()
        empty_view.builder().setEmptyDrawable(R.mipmap.empty_image)
                .setEmptyText("这里什么都没有")
        rv_list.layoutManager = LinearLayoutManager(mContext)
        mGridAdapter = ExchangeHistoryAdapter(mContext, 2)
        rv_list.adapter = mGridAdapter
        rv_list.addItemDecoration(HorizontalDividerItemDecoration.Builder(mContext).color(
                mContext.resources.getColor(R.color.FBFBFB))
                .size(mContext.resources.getDimension(R.dimen.dp_2).toInt())
                .build())
        //        mGridAdapter!!.setData(Arrays.asList("1", "2", "3", "4", "3", "4", "1", "2", "3", "4"))
//        mGridAdapter.setOnItemClickListener { view, position -> GoodsDetailsActivity.start(mContext) }
        refreshLayout.setEnableRefresh(true)
        refreshLayout.setEnableLoadMore(true)
        refreshLayout.autoRefresh(200)
        empty_view.builder().setEmptyText("这里什么也没有哦")
    }
    
    override fun onRefresh() {
        super.onRefresh()
        mCurrentPage = STARTPAGE
        refreshLayout.setEnableLoadMore(true)
        networkForRecord(mCurrentPage, false)
    }
    
    override fun onLoadMore() {
        super.onLoadMore()
        mCurrentPage++
        networkForRecord(mCurrentPage, true)
    }
    
    fun networkForRecord(page: Int, isLoadMore: Boolean) {
        var request = LiveUserGoldHistoryRequest()
        request.userId = UserManager.getInstance().userId
        request.type = 2
        request.pageNum = page
        Api.getInstance().post(
                request,
                LiveUserGoldHistoryResponse::class.java,
                object : ResponseCallback<LiveUserGoldHistoryResponse>() {
                    override fun onResponse(date: LiveUserGoldHistoryResponse?, isFromCache: Boolean) {
                        empty_view.showContent()
                        val data = date!!.data
                        val list = data.list
                        refreshLayout.finishRefresh()
                        if (isLoadMore) {
                            mGridAdapter.addAll(list)
                        } else {
                            mGridAdapter.setData(list)
                        }
                        if (data.isLastPage) {
                            refreshLayout.finishLoadMoreWithNoMoreData()
                        } else {
                            refreshLayout.finishLoadMore()
                        }
                    }
                    
                    override fun onEmpty(date: LiveUserGoldHistoryResponse?, isFromCache: Boolean) {
                        super.onEmpty(date, isFromCache)
                        if (!isLoadMore) {
                            empty_view.showEmpty()
                        }
                        refreshLayout.finishRefresh()
                        refreshLayout.finishLoadMore()
                    }
    
                    override fun onError() {
                        super.onError()
                        if(!isLoadMore)
                            empty_view.showError()
                        refreshLayout.finishRefresh()
                        refreshLayout.finishLoadMore()
                    }
                    
                },
                ""
        )
    }
}
