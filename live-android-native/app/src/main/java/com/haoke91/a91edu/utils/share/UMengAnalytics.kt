package com.haoke91.a91edu.utils.share

import android.content.Context

import com.haoke91.a91edu.ui.order.ConfirmOrderActivity
import com.umeng.analytics.MobclickAgent

/**
 * 项目名称：91haoke_Android
 * 类描述： 友盟埋点
 * 创建人：weiyimeng
 * 创建时间：2018/12/10 5:02 PM
 * 修改人：weiyimeng
 * 修改时间：2018/12/10 5:02 PM
 * 修改备注：
 */
object UMengAnalytics {
    val look_video_fail = "look_video_fail" //观看回放失败
    val look_live_fail = "look_live_fail" //观看直播失败
    val look_live_success = "look_live_success" //观看直播成功
    val uploadHomework_success = "uploadhomework_success" //上传作业成功
    val uploadHomework_fail = "uploadHomework_fail" //上传作业失败
    
    val DURATION_BEFORE_GETSTREAM="duration_before_getStream"//获取视频流第一帧的时间
    val DURATION_BEFORE_GETSTREAM_ALIYUNLIVE="duration_before_getstream_aliyunlive"//阿里直播第一帧时间
    val DURATION_BEFORE_GETSTREAM_ALIYUNVIDEO="duration_before_getstream_aliyunvideo"//阿里回放第一帧
    val DURATION_BEFORE_GETSTREAM_AGORA="duration_before_getstream_agora"//声网直播第一帧
    val DURATION_BEFORE_GETSTREAM_TALKLIVE="duration_before_getstream_talk"//拓课直播第一帧
//    val DURATION_BEFORE_GETSTREAM_TALKVIDEO="duration_before_getstream_talkvideo"//拓课回放第一帧
    fun onEvent(context: Context, eventId: String) {
        MobclickAgent.onEvent(context, eventId) //自定义事件
    }
    
    fun onEvent(context: Context, eventId: String, map: HashMap<String, String>) {
        MobclickAgent.onEvent(context, eventId, map) //自定义事件
        
    }
}
