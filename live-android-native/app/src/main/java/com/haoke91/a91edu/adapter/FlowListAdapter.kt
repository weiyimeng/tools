package com.haoke91.a91edu.adapter

import android.content.Context
import com.gaosiedu.live.sdk.android.domain.OrderDeliverDomain
import com.haoke91.a91edu.R
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/10/11 下午4:21
 * 修改人：weiyimeng
 * 修改时间：2018/10/11 下午4:21
 * 修改备注：
 * @version
 */
class FlowListAdapter(context: Context, date: List<OrderDeliverDomain>) : QuickWithPositionAdapter<OrderDeliverDomain>(context, R.layout.item_flow, date) {
    
    override fun convert(helper: BaseAdapterHelper, item: OrderDeliverDomain, position: Int) {
        helper.getTextView(R.id.tv_flow_name).text = item.expressName + ":" + item.expressNo
        
        
    }
}
