package com.haoke91.a91edu.ui.found

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gaosiedu.live.sdk.android.api.user.gold.info.LiveUserGoldInfoRequest
import com.gaosiedu.live.sdk.android.api.user.gold.info.LiveUserGoldInfoResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.TabAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.ui.order.BaseLoadMoreFragment
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.ui.GeneralWebViewActivity
import com.haoke91.baselibrary.smarttab.SmartTabLayout
import kotlinx.android.synthetic.main.activity_exchange_history.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/22 上午11:10
 * 修改人：weiyimeng
 * 修改时间：2018/8/22 上午11:10
 * 修改备注：
 */
class ExchangeHistoryActivity : BaseActivity(), SmartTabLayout.TabProvider {
    private var mTabLayout: SmartTabLayout? = null
    
    override fun getLayout(): Int {
        return R.layout.activity_exchange_history
    }
    
    override fun initialize() {
        toolbar_more.setImageResource(R.mipmap.shop_nav_icon_issue)
        toolbar_more.visibility = View.VISIBLE
        mTabLayout = findViewById(R.id.tab_order)
        //        mrv_giftGrid = view.findViewById(R.id.rv_giftGrid);
        vp_order.offscreenPageLimit = 2
        val adapter = TabAdapter<BaseLoadMoreFragment>(supportFragmentManager)
        adapter.addFragment(ExchangePhyHistoryFragment(), "")
//        adapter.addFragment(ExchangeVirtualHistoryFragment(), "")
        vp_order.adapter = adapter
        mTabLayout!!.setCustomTabView(this)
        mTabLayout!!.setViewPager(vp_order)
        toolbar_more.setOnClickListener({ GeneralWebViewActivity.start(this,"http://www.baidu.com") })
        networkForGoldInfo()
    }
    
    override fun createTabView(container: ViewGroup, position: Int, adapter: PagerAdapter): View {
        
        val inflater = LayoutInflater.from(container.context)
        val res = container.context.resources
        val tab = inflater.inflate(R.layout.item_tab, container, false)
        val value = tab.findViewById<TextView>(R.id.custom_text)
        //        value.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        //        ColorStateList csl = getResources().getColorStateList(R.color.text_white_black_selector);
        //        value.setTextColor(csl);
        when (position) {
            0 -> value.text = res.getString(R.string.exchange_physical_history)
            1 -> value.text = res.getString(R.string.exchange_virtual_history)
        }
        return tab
    }
    
    companion object {
        
        fun start(context: Context) {
            val intent = Intent(context, ExchangeHistoryActivity::class.java)
            context.startActivity(intent)
        }
    }
    
    private fun networkForGoldInfo() {
        var request = LiveUserGoldInfoRequest()
        request.userId = UserManager.getInstance().userId
        Api.getInstance().post(request, LiveUserGoldInfoResponse::class.java, object : ResponseCallback<LiveUserGoldInfoResponse>() {
            override fun onResponse(date: LiveUserGoldInfoResponse?, isFromCache: Boolean) {
                val data = date!!.data
                //                data.gold
                //                data.entityGold
                //                data.inventedGold
                tv_usableGold.text = "${data.gold}"
//                tv_usedGold.text = "${(data.entityGold )+(data.inventedGold)} (实物${data.entityGold }/道具${data.inventedGold })"
                tv_usedGold.text = "${(data.entityGold )+(data.inventedGold)} (实物${data.entityGold })"
            }
        }, "")
    }
}
