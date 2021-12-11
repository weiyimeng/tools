package com.haoke91.a91edu.ui.found

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.TabAdapter
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.ui.order.BaseLoadMoreFragment
import com.haoke91.a91edu.ui.GeneralWebViewActivity
import com.haoke91.baselibrary.smarttab.SmartTabLayout
import kotlinx.android.synthetic.main.activity_goldgoods.*

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/16 上午11:24
 * 修改人：weiyimeng
 * 修改时间：2018/8/16 上午11:24
 * 修改备注：
 */
class GoldGoodsActivity : BaseActivity(), SmartTabLayout.TabProvider {
    //    private var vp_order: ViewPager? = null
    private lateinit var mExchangePhyFragment: ExchangePhyFragment
//    private lateinit var mExchangeVirtualFragment: ExchangeVirtualFragment
    
    override fun getLayout(): Int {
        return R.layout.activity_goldgoods
    }
    
    override fun initialize() {
        //        mrv_giftGrid = view.findViewById(R.id.rv_giftGrid);
        vp_order.offscreenPageLimit = 2
        val adapter = TabAdapter<BaseLoadMoreFragment>(supportFragmentManager)
        mExchangePhyFragment = ExchangePhyFragment()
//        mExchangeVirtualFragment = ExchangeVirtualFragment()
        adapter.addFragment(mExchangePhyFragment, "")
//        adapter.addFragment(mExchangeVirtualFragment, "")
        vp_order.adapter = adapter
        mTabLayout.setCustomTabView(this)
        mTabLayout.setViewPager(vp_order)
        iv_toHelp.setOnClickListener({ GoldHelpActivity.start(this) })
        iv_historyList.setOnClickListener { ExchangeHistoryActivity.start(this@GoldGoodsActivity) }
        iv_back.setOnClickListener { finish() }
        iv_timeSort.isSelected = true
        currentSort = 0
        //        mTabLayout.setOnTabClickListener { position ->
        //            isTimeSort = (position == 0)
        //        }
        currentSort = 0
        isTimeSort = true
        rl_timeSort.setOnClickListener({
            if (!isTimeSort) {
                iv_timeSort.tag = 0
                currentSort = 0
                iv_timeSort.rotation = 0f
            } else {
                if (currentSort == 0) {
                    currentSort = 1
                    iv_timeSort.rotation = 180f
                } else {
                    currentSort = 0
                    iv_timeSort.rotation = 0f
                }
            }
            iv_timeSort.isSelected = true
            iv_priceSort.isSelected = false
            isTimeSort = true
//            if (mTabLayout.getTabAt(0).isSelected) {
                mExchangePhyFragment.clickRefresh()
//            } else {
//                mExchangeVirtualFragment.clickRefresh()
//            }
        })
        rl_priceSort.setOnClickListener({
            if (!iv_priceSort.isSelected) {
                iv_priceSort.tag = 0
                iv_priceSort.rotation = 0f
            } else {
                if (currentSort == 0) {
                    currentSort = 1
                    iv_priceSort.rotation = 180f
                } else {
                    currentSort = 0
                    iv_priceSort.rotation = 0f
                }
            }
            iv_priceSort.isSelected = true
            iv_timeSort.isSelected = false
            isTimeSort = false
//            if (mTabLayout.getTabAt(1).isSelected) {
//                mExchangeVirtualFragment.clickRefresh()
//            } else {
                mExchangePhyFragment.clickRefresh()
//            }
        })
        cb_usable.setOnCheckedChangeListener { buttonView, isChecked ->
            isUsable = isChecked
//            if (mTabLayout.getTabAt(0).isSelected) {
                mExchangePhyFragment.clickRefresh()
//            } else {
//                mExchangeVirtualFragment.clickRefresh()
//            }
        }
        isUsable = false
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
            0 -> value.text = res.getString(R.string.exchange_physical)
            1 -> value.text = res.getString(R.string.exchange_virtual)
        }
        return tab
    }
    
    companion object {
        var isTimeSort = true
        var currentSort = 0
        var isUsable = false
        val TYPE_PHYSICS = 1 //实物
        val TYPE_VIRTUAL = 2 //虚拟
        
        fun start(context: Context) {
            val intent = Intent(context, GoldGoodsActivity::class.java)
            context.startActivity(intent)
        }
    }
}
