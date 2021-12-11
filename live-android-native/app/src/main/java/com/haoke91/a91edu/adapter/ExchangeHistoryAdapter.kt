package com.haoke91.a91edu.adapter

import android.content.Context
import android.view.View
import com.gaosiedu.live.sdk.android.api.user.gold.history.LiveUserGoldHistoryResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/13 下午2:16
 * 修改人：weiyimeng
 * 修改时间：2018/8/13 下午2:16
 * 修改备注：
 */
class ExchangeHistoryAdapter(context: Context) : QuickWithPositionAdapter<LiveUserGoldHistoryResponse.ListData>(context, R.layout.item_exchange_history) {
    private var mType: Int = 0 //  1实物  2：虚拟
    
    constructor(context: Context, type: Int) : this(context) {
        this.mType = type
    }
    
    override fun convert(helper: BaseAdapterHelper, item: LiveUserGoldHistoryResponse.ListData, position: Int) {
        val product = item.goldProductDomain
        GlideUtils.load(context, product.coverImg, helper.getImageView(R.id.iv_gift))
        val tvNum = helper.getTextView(R.id.tv_num)
        val tvStatus = helper.getTextView(R.id.tv_status)
        helper.getTextView(R.id.tv_giftName).text = product.name
        val tvTime = helper.getTextView(R.id.tv_exchange_time)
        if (mType == 1) {
            tvTime.text = String.format("兑换时间：%s", item.createTime)
        } else {
            tvTime.text = String.format("有效期限：%s", item.createTime)
            tvTime.visibility = View.GONE
        }
        tvNum.text = String.format("数量 %s  金币 %s", item.count, item.gold)
        if (mType == 1) {
            tvStatus.text = if (item.shopingStatus == 1) "已发货" else "未发货"
        } else {
            tvStatus.visibility = View.GONE
        }
    }
}
