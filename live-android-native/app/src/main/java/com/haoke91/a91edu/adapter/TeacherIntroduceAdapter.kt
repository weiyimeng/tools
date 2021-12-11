package com.haoke91.a91edu.adapter

import android.content.Context
import android.text.Html
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
class TeacherIntroduceAdapter(context: Context) : QuickWithPositionAdapter<TeacherDomain>(context, R.layout.item_teacherinfo, ArrayList()) {
    
    override fun convert(helper: BaseAdapterHelper, item: TeacherDomain, position: Int) {
        val ivHead = helper.getImageView(R.id.ivHead)
        val tvRole = helper.getTextView(R.id.tvRole)
        val tvFeature = helper.getTextView(R.id.tvFeature)
        val tvOffice = helper.getTextView(R.id.tvOffice)
        val tvDetail = helper.getTextView(R.id.tvDetail)
        GlideUtils.loadHead(getContext(), item.headUrl, ivHead)
        tvRole.text = "主讲： ${item.realname}"
        tvFeature.text = Html.fromHtml("特点： ${item.honour}")
        tvOffice.text = "教龄：${item.gradeNames ?: ""}"
        tvDetail.text = Html.fromHtml("  ${item.teacherDesc}")
        
    }
}
