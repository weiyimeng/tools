package com.haoke91.a91edu.ui.user

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ObjectUtils
import com.gaosiedu.live.sdk.android.api.user.coupon.list.LiveUserCouponListResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.TabAdapter
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.widget.BottomInputLayout
import com.haoke91.baselibrary.smarttab.SmartTabLayout
import kotlinx.android.synthetic.main.activity_my_conpans.*


/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/10 下午3:29
 * 修改人：weiyimeng
 * 修改时间：2018/7/10 下午3:29
 * 修改备注：
 */
class MyCouponsActivity : BaseActivity(), SmartTabLayout.TabProvider {
    private lateinit var adapter: TabAdapter<AllCouponsFragment>
    
    override fun getLayout(): Int {
        return R.layout.activity_my_conpans
    }
    
    override fun initialize() {
        vp_order.offscreenPageLimit = 2
        adapter = TabAdapter(supportFragmentManager)
        adapter.addFragment(AllCouponsFragment.newInstance(AllCouponsFragment.not_use), "")
        adapter.addFragment(AllCouponsFragment.newInstance(AllCouponsFragment.used), "")
        adapter.addFragment(AllCouponsFragment.newInstance(AllCouponsFragment.cant_use), "")
        vp_order.adapter = adapter
        tab_order.setCustomTabView(this)
        tab_order.setViewPager(vp_order)
        rl_input.listener = object : BottomInputLayout.onExchangeSuccessListener {
            override fun exchangeSuccess(date: LiveUserCouponListResponse.ListData) {
                if (!ObjectUtils.isEmpty(adapter.getmFragments())) {
                    adapter!!.getmFragments()[0].addCoupon(date)
                }
            }
        }
        
    }
    
    
    override fun createTabView(container: ViewGroup, position: Int, adapter: PagerAdapter): View {
        
        val inflater = LayoutInflater.from(container.context)
        val res = container.context.resources
        val tab = inflater.inflate(R.layout.item_tab, container, false)
        val value = tab.findViewById<TextView>(R.id.custom_text)
        value.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
        when (position) {
            0 -> value.text = res.getString(R.string.not_use)
            1 -> value.text = res.getString(R.string.used)
            2 -> value.text = res.getString(R.string.cant_used)
        }
        return tab
    }
    
    companion object {
        
        fun start(context: Context) {
            val intent = Intent(context, MyCouponsActivity::class.java)
            context.startActivity(intent)
        }
    }
}
