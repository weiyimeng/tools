package com.haoke91.a91edu.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.widget.ImageView
import android.widget.LinearLayout
import com.blankj.utilcode.util.ObjectUtils

import com.bumptech.glide.Glide
import com.gaosiedu.live.sdk.android.api.content.banner.LiveContentBannerResponse
import com.gaosiedu.live.sdk.android.domain.ContentDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.ui.GeneralWebViewActivity
import com.haoke91.a91edu.ui.course.CourseDetailActivity
import com.haoke91.a91edu.ui.course.SearchActivity
import com.haoke91.a91edu.ui.course.SpecialClassActivity
import com.haoke91.a91edu.ui.order.ChoiceChangeClassActivity.Companion.courseId
import com.haoke91.a91edu.utils.CRequest
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.orhanobut.logger.Logger
import com.umeng.socialize.utils.DeviceConfig.context
import java.net.URLDecoder
import java.util.Arrays

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/19 下午2:36
 * 修改人：weiyimeng
 * 修改时间：2018/7/19 下午2:36
 * 修改备注：
 */
class HomeBannerAdapter(context: Context?, dates: List<ContentDomain>?) : BasePagerAdapter<ContentDomain>(context, R.layout.item_home_banner, dates) {
    override fun convert(imageView: View, item: ContentDomain, position: Int) {
        GlideUtils.load(context, item.img, imageView as ImageView)
        val urlRequest = CRequest.URLRequest(item.url)
        imageView.setOnClickListener(object : NoDoubleClickListener() {
            override fun onDoubleClick(v: View) {
                when (item.targetType) {
                    -1, 3 -> GeneralWebViewActivity.start(context, item.url)
                    1 -> {
                        try {
                            val courseId = item.url.substring(item.url.lastIndexOf("/") + 1).toInt()
                            CourseDetailActivity.start(context, courseId)
                        } catch (e: Exception) {
                            Logger.e(e.message)
                        }
                        //https://mp.weixin.qq.com/s/skPU6N8eJ9wuEbmdBlMUjg
                    }
                    2 -> {
                        if (!ObjectUtils.isEmpty(urlRequest) && urlRequest.containsKey("subject")) {
                            SpecialClassActivity.start(context, urlRequest["subject"]!!.toInt())
                        } else if (!ObjectUtils.isEmpty(urlRequest) && urlRequest.containsKey("keywords")) {
                            SearchActivity.start(context, URLDecoder.decode(urlRequest["keywords"], "utf-8"))
                        }
                    }
                    else -> {
                        GeneralWebViewActivity.start(context, item.url)
                    }
                }
            }
        })
    }
    
    // -1指外部链接，1标识课程详情，2标识课程列表，3标识活动页面
}
