package com.haoke91.a91edu.ui.found

import android.content.Context
import android.content.Intent
import android.text.Html
import com.gaosiedu.live.sdk.android.api.user.gold.history.LiveUserGoldHistoryResponse
import com.gaosiedu.live.sdk.android.api.user.gold.history.detail.LiveUserGoldHistoryDetailRequest
import com.gaosiedu.live.sdk.android.api.user.gold.history.detail.LiveUserGoldHistoryDetailResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.a91edu.utils.manager.UserManager
import kotlinx.android.synthetic.main.activity_gooodsdetails.*

/**
 * 项目名称：91haoke_Android
 * 类描述： 兑换商品详情
 * 创建人：weiyimeng
 * 创建时间：2018/8/22 下午3:47
 * 修改人：weiyimeng
 * 修改时间：2018/8/22 下午3:47
 * 修改备注：
 */
class GoodsDetailsActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_gooodsdetails
    }
    
    override fun initialize() {
        var id = intent.getIntExtra("id", 0)
        networkForExchangeDetail(id)
    }
    
    /**
     * 获取详情
     */
    private fun networkForExchangeDetail(id: Int) {
        mEmptyView.showLoading()
        var request = LiveUserGoldHistoryDetailRequest()
        request.recordId = id
        request.userId = UserManager.getInstance().userId
        Api.getInstance().post(request, LiveUserGoldHistoryDetailResponse::class.java, object : ResponseCallback<LiveUserGoldHistoryDetailResponse>() {
            override fun onResponse(date: LiveUserGoldHistoryDetailResponse?, isFromCache: Boolean) {
                if (Utils.isSuccess(date!!.code)) {
                    mEmptyView.showContent()
                    val data = date.data
                    GlideUtils.load(this@GoodsDetailsActivity, data.productIco, iv_gift)
                    tv_giftName.text = data.productName
                    tv_exchange_time.text = "兑换时间：${data.exchangeTime}"
                    tv_nums.text = "数量  ${data.count}    合计:"
                    tv_gold.text = "${data.costGold ?: 0}金币"
                    tv_name.text = data.contactName
                    tv_phone.text = data.contactPhone
                    tv_address_detail.text = data.address
                    tv_translate_num.text = data.trackingCompany + "     " + data.trackNo
                    
                    tv_status.text = if (data.shopingStatus == 1) "已收货" else "未收货"
                } else {
                    mEmptyView.showError()
                }
            }
            
            override fun onEmpty(date: LiveUserGoldHistoryDetailResponse?, isFromCache: Boolean) {
                super.onEmpty(date, isFromCache)
                mEmptyView.showError()
            }
            
            override fun onError() {
                super.onError()
                mEmptyView.showError()
            }
        }, "load detail of goods")
        
    }
    
    companion object {
        fun start(context: Context, id: Int) {
            val intent = Intent(context, GoodsDetailsActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }
}
