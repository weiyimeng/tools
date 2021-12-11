package com.haoke91.a91edu.adapter

import android.content.Context
import android.text.Html

import com.gaosiedu.live.sdk.android.api.user.balance.record.LiveUserBalanceRecordResponse
import com.haoke91.a91edu.R
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/9 上午9:41
 * 修改人：weiyimeng
 * 修改时间：2018/8/9 上午9:41
 * 修改备注：
 */
class BalanceRecordAdapter(context: Context, dates: List<LiveUserBalanceRecordResponse.ListData>?) : QuickWithPositionAdapter<LiveUserBalanceRecordResponse.ListData>(context, R.layout.item_balance_record, dates) {
    
    override fun convert(helper: BaseAdapterHelper, item: LiveUserBalanceRecordResponse.ListData, position: Int) {
        helper.getTextView(R.id.tv_record_tittle).text = Html.fromHtml(item.recordName ?: "")
        helper.getTextView(R.id.tv_record_time).text = item.createTime
        helper.getTextView(R.id.tv_record_type).text = item.typeName
        helper.getTextView(R.id.tv_record_money).text = item.balance?.toString()
        
    }
}
