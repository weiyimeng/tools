package com.haoke91.a91edu.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.ObjectUtils
import com.haoke91.a91edu.R
import com.haoke91.a91edu.entities.player.ListStudent
import com.haoke91.a91edu.entities.player.Student
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.baselibrary.recycleview.WrapRecyclerView
import com.haoke91.baselibrary.utils.ICallBack
import java.util.*

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/19 下午2:36
 * 修改人：weiyimeng
 * 修改时间：2018/7/19 下午2:36
 * 修改备注：
 */
class LiveRoomAdapter(context: Context?, dates: List<ListStudent>?, val callBack: ICallBack<String, String>) : BasePagerAdapter<ListStudent>(context, R.layout.item_liveroom, dates) {
    override fun convert(view: View, item: ListStudent, position: Int) {
        //        GlideUtils.load(context, item, imageView as ImageView)
        //        view.setOnClickListener {
        //            //            when (item.type) {
        //            //                1 -> GeneralWebViewActivity.start(context, item.url)
        //            //            }
        //        }
        var rlClose = view.findViewById<View>(R.id.rl_close)
        var ivImgRank1 = view.findViewById<ImageView>(R.id.iv_img_rank1)
        var ivImgRank2 = view.findViewById<ImageView>(R.id.iv_img_rank1)
        var ivImgRank3 = view.findViewById<ImageView>(R.id.iv_img_rank1)
        var ivRankTop = view.findViewById<ImageView>(R.id.iv_rankTop)
        var ivFirstHead = view.findViewById<ImageView>(R.id.iv_first_head)
        var tvFirstTag = view.findViewById<TextView>(R.id.tv_first_tag)
        var tvFirstName = view.findViewById<TextView>(R.id.tv_first_name)
        var tvFirstPercent = view.findViewById<TextView>(R.id.tv_first_percent)
        var ivSecondHead = view.findViewById<ImageView>(R.id.iv_second_head)
        var tvSecondTag = view.findViewById<TextView>(R.id.tv_second_tag)
        var tvSecondName = view.findViewById<TextView>(R.id.tv_second_name)
        var tvSecondPercent = view.findViewById<TextView>(R.id.tv_second_percent)
        var ivThirdHead = view.findViewById<ImageView>(R.id.iv_third_head)
        var tvThirdTag = view.findViewById<TextView>(R.id.tv_third_tag)
        var tvThirdName = view.findViewById<TextView>(R.id.tv_third_name)
        var tvThirdPercent = view.findViewById<TextView>(R.id.tv_third_percent)
        
        if (!ObjectUtils.isEmpty(dates) && position == dates.size - 1) {
            rlClose.visibility = View.VISIBLE
        } else {
            rlClose.visibility = View.GONE
        }
        
        rlClose.setOnClickListener {
            callBack?.call("", position)
        }
        if (item.studentList == null) {
            return
        }
        //顶部图片
        if ("averageConsumeList" == item.tag) {
            ivRankTop.setImageResource(R.mipmap.img_rank_speed)
        } else if ("advanceRatioList" == item.tag) {
            ivRankTop.setImageResource(R.mipmap.img_rank_grow)
        } else {
            ivRankTop.setImageResource(R.mipmap.img_rank_right)
        }
        var listCount = item.studentList.size
        if (listCount > 0) {
            val student1 = item.studentList[0]
            GlideUtils.load(context, student1.headUrl, ivFirstHead)
            tvFirstTag.text = student1.levelName
            tvFirstName.text = student1.nickName
            if ("averageConsumeList" == item.tag) {
                tvFirstPercent.text = "${student1.averageConsume}s"
            } else if ("advanceRatioList" == item.tag) {
                tvFirstPercent.text = "${student1.advanceRatio}"
            } else {
                tvFirstPercent.text = "${student1.rightRatio}%"
            }
        } else {
            ivImgRank1.visibility = View.GONE
            ivFirstHead.visibility = View.GONE
            tvFirstTag.visibility = View.GONE
            tvFirstName.visibility = View.GONE
            tvFirstPercent.visibility = View.GONE
        }
        if (listCount > 1) {
            val student1 = item.studentList[1]
            GlideUtils.load(context, student1.headUrl, ivSecondHead)
            tvSecondTag.text = student1.levelName
            tvSecondName.text = student1.nickName
            when {
                "averageConsumeList" == item.tag -> tvSecondPercent.text = "${student1.averageConsume}s"
                "advanceRatioList" == item.tag -> tvSecondPercent.text = "${student1.advanceRatio}"
                else -> tvSecondPercent.text = "${student1.rightRatio}%"
            }
        } else {
            ivImgRank2.visibility = View.GONE
            ivSecondHead.visibility = View.GONE
            tvSecondTag.visibility = View.GONE
            tvSecondName.visibility = View.GONE
            tvSecondPercent.visibility = View.GONE
        }
        if (listCount > 2) {
            val student1 = item.studentList[2]
            GlideUtils.load(context, student1.headUrl, ivThirdHead)
            tvThirdTag.text = student1.levelName
            tvThirdName.text = student1.nickName
            when {
                "averageConsumeList" == item.tag -> tvThirdPercent.text = "${student1.averageConsume}s"
                "advanceRatioList" == item.tag -> tvThirdPercent.text = "${student1.advanceRatio}"
                else -> tvThirdPercent.text = "${student1.rightRatio}%"
            }
        } else {
            ivImgRank3.visibility = View.GONE
            ivThirdHead.visibility = View.GONE
            tvThirdTag.visibility = View.GONE
            tvThirdName.visibility = View.GONE
            tvThirdPercent.visibility = View.GONE
        }
        
        var room_list = view.findViewById<WrapRecyclerView>(R.id.wr_liveRoom_list)
        room_list.layoutManager = LinearLayoutManager(context)
        if (item.studentList != null && item.studentList.size > 3)
            room_list.adapter = LiveRoomListAdapter(context, item.studentList.subList(3, item.studentList.size), item.tag)
    }
    
    
}
