package com.gstudentlib.base.homework

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter

/**
 * 作者：created by 逢二进一 on 2019/9/16 16:28
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class BaseHomeWorkAdapter(fm: FragmentManager, private val list: List<BaseHomeWorkFragment>) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}