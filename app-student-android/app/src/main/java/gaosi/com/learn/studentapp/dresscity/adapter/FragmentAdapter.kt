package gaosi.com.learn.studentapp.dresscity.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import com.gsbaselib.base.GSBaseFragment

import java.util.ArrayList

/**
 * Created by huangshan on 2019/4/26.
 */

class FragmentAdapter(fm: FragmentManager, private val fragmentList: ArrayList<GSBaseFragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): GSBaseFragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}
