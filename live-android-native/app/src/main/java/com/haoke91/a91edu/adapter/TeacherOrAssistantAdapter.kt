package com.haoke91.a91edu.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.gaosiedu.live.sdk.android.domain.TeacherDomain
import com.haoke91.a91edu.GlobalConfig
import com.haoke91.a91edu.R
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter

import java.util.ArrayList

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/9/13 10:38
 */
class TeacherOrAssistantAdapter(context: Context) : QuickWithPositionAdapter<TeacherDomain>(context, R.layout.item_teacher, ArrayList()) {
    
    private var remainCount = 0
    override fun convert(helper: BaseAdapterHelper, item: TeacherDomain, position: Int) {
        val ivHead = helper.getImageView(R.id.iv_assistant)
        var tvName = helper.getTextView(R.id.tv_head_two)
        val tvRole = helper.getTextView(R.id.tv_role)
        val tvRemainCount = helper.getTextView(R.id.tvRemainCount)
        GlideUtils.loadHead(getContext(), item.headUrl, ivHead)
        tvName.text = item.realname?.replace("辅导老师", "")
        if (item.type == 4) {
            tvRole.text = "助教"
            if (remainCount > 0) {
                tvRemainCount.visibility = View.VISIBLE
                tvRemainCount.text = "剩余名额${remainCount}个"
            } else {
                tvRemainCount.visibility = View.GONE
            }
        } else {
            tvRole.text = "主讲"
            tvRemainCount.visibility = View.GONE
        }
    }
    
    fun setData(list: List<TeacherDomain>?, count: Int) {
        remainCount = count
        super.setData(list)
    }
}
