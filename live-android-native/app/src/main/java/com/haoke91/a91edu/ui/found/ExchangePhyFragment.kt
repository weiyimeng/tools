package com.haoke91.a91edu.ui.found

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.gaosiedu.live.sdk.android.api.user.gold.product.LiveUserGoldListRequest
import com.gaosiedu.live.sdk.android.api.user.gold.product.LiveUserGoldListResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.ExchangeAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.order.BaseLoadMoreFragment
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.baselibrary.utils.DensityUtil
import com.lzy.okgo.OkGo

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/13 下午2:10
 * 修改人：weiyimeng
 * 修改时间：2018/8/13 下午2:10
 * 修改备注：
 */
class ExchangePhyFragment : BaseLoadMoreFragment() {
    private var mGridAdapter: ExchangeAdapter? = null
    private val LOADLIST = "loadList"
    
    companion object {
        private val STARTPAGE = 1
        private var mCurrentPage = STARTPAGE
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        empty_view.showContent()
        empty_view.builder().setEmptyText("这里什么都没有")
                .setErrorText("网络连接异常")
                .setEmptyDrawable(R.mipmap.empty_image)
        
        rv_list.layoutManager = GridLayoutManager(mContext, 2)
        mGridAdapter = ExchangeAdapter(mContext, 1)
        rv_list.adapter = mGridAdapter
        rv_list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    //                    outRect.right = DensityUtil.dip2px(mContext, 8);
                    outRect.right = DensityUtil.dip2px(mContext,16f)
                    //                    outRect.left = DensityUtil.dip2px(mContext, 16);
                    outRect.left = DensityUtil.dip2px(mContext,8f)
                }
                if (parent.getChildAdapterPosition(view) % 2 == 1) {
                    //                    outRect.left = DensityUtil.dip2px(mContext, 8);
                    outRect.left = DensityUtil.dip2px(mContext,8f)
                    //                    outRect.right = DensityUtil.dip2px(mContext, 16);
                    outRect.right = DensityUtil.dip2px(mContext,16f)
                    
                }
                //                outRect.bottom = DensityUtil.dip2px(mContext, 16);
                outRect.bottom = DensityUtil.dip2px(mContext,16f)
            }
        })
        refreshLayout.setEnableRefresh(true)
        refreshLayout.setEnableLoadMore(true)
        //        refreshLayout.autoRefresh(200)
        onRefresh()
    }
    
    override fun fetchData() {
        //        mGridAdapter.setData();
        
    }
    
    override fun onRefresh() {
        super.onRefresh()
        Utils.loading(context)
        mCurrentPage = STARTPAGE
        networkForGoodsList(mCurrentPage, false)
    }
    
    override fun onLoadMore() {
        super.onLoadMore()
        mCurrentPage++
        networkForGoodsList(mCurrentPage, true)
    }
    
    fun clickRefresh() {
        //        onRefresh()
        OkGo.getInstance().cancelTag(LOADLIST)
        onRefresh()
    }
    
    /**
     * list for goods
     */
    private fun networkForGoodsList(page: Int, isLoadMore: Boolean) {
        //
        val request = LiveUserGoldListRequest()
        request.userId = UserManager.getInstance().userId
        request.pageNum = page
        if (GoldGoodsActivity.isTimeSort) {
            request.time = GoldGoodsActivity.currentSort
        } else {
            request.gold = GoldGoodsActivity.currentSort
        }
        request.type = GoldGoodsActivity.TYPE_PHYSICS
        if (GoldGoodsActivity.isUsable) {
            request.usable = 1
        }
        Api.getInstance().post(request, LiveUserGoldListResponse::class.java, object : ResponseCallback<LiveUserGoldListResponse>() {
            override fun onResponse(date: LiveUserGoldListResponse?, isFromCache: Boolean) {
                Utils.dismissLoading()
                refreshLayout.finishRefresh()
                val data = date!!.data
                val list = data.list
                empty_view.showContent()
                if (list != null && list.size > 0) {
                    if (isLoadMore) {
                        mGridAdapter!!.addAll(list)
                    } else {
                        mGridAdapter!!.setData(data.list)
                    }
                } else {
                    empty_view.showEmpty()
                }
                if (data.isLastPage) {
                    refreshLayout.finishLoadMoreWithNoMoreData()
                } else {
                    refreshLayout.finishLoadMore()
                }
            }
            
            override fun onEmpty(date: LiveUserGoldListResponse?, isFromCache: Boolean) {
                Utils.dismissLoading()
                super.onEmpty(date, isFromCache)
                if (!isLoadMore) {
                    empty_view.showEmpty()
                }
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
            }
    
            override fun onFail(date: LiveUserGoldListResponse?, isFromCache: Boolean) {
                super.onFail(date, isFromCache)
                Utils.dismissLoading()
            }
            
            override fun onError() {
                Utils.dismissLoading()
                super.onError()
                if (!isLoadMore)
                    empty_view.showError()
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
            }
            
        }, LOADLIST)
        
    }
}
