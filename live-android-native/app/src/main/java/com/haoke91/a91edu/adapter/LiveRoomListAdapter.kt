package com.haoke91.a91edu.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import com.haoke91.a91edu.R
import com.haoke91.a91edu.entities.player.Student
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter
import java.lang.Exception

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/1 下午4:53
 * 修改人：weiyimeng
 * 修改时间：2018/11/1 下午4:53
 * 修改备注：
 * @version
 */
class LiveRoomListAdapter(context: Context, date: List<Student>, val tag: String?) : QuickWithPositionAdapter<Student>(context, R.layout.item_liveroom_list, date) {
    override fun convert(helper: BaseAdapterHelper, item: Student?, position: Int) {
        var mV_item = helper.getView<View>(R.id.ll_item)
        var mTvRankNum = helper.getTextView(R.id.tv_rankNum)
        var mTvName = helper.getTextView(R.id.tv_stu_name)
        var mTvLevelName = helper.getTextView(R.id.tv_stu_levelName)
        var mTvPercent = helper.getTextView(R.id.tv_stu_percent)
        if (item != null) {
            mTvRankNum.text = "${position + 4}"
            mTvName.text = item.nickName
            mTvLevelName.text = item.levelName
            if ("averageConsumeList" == tag) {
                mTvPercent.text = "${item.averageConsume}s"
            } else if ("advanceRatioList" == tag) {
                mTvPercent.text = "${item.advanceRatio}"
            } else {
                mTvPercent.text = "${item.rightRatio}%"
            }
        }
        if (position % 2 == 0) {
            mV_item.setBackgroundColor(Color.parseColor("#FFFEDFC2"))
        } else {
            mV_item.setBackgroundColor(Color.parseColor("#00FFFFFF"))
        }
        if (item?.userId?.equals(UserManager.getInstance().userId) == true) {
            mV_item.setBackgroundColor(Color.parseColor("FFFAC797"))
        }
        
    }
}
