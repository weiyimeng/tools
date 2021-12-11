package com.haoke91.a91edu.widget.webview;

import android.content.Context;

import com.haoke91.baselibrary.event.MessageItem;
import com.haoke91.baselibrary.event.RxBus;
import com.orhanobut.logger.Logger;


/**
 * 项目名称：91edu
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/5/10 上午9:52
 * 修改人：weiyimeng
 * 修改时间：2018/5/10 上午9:52
 * 修改备注：
 */
public class APIJSTalkInterface {
    public static final int JSMESSAGE_TALK = 101;//拓课云视频
    public static final int KICK_OUT = 102;//用户重复登录的通知
    public static final int VIDEO_PLAY = 111;//播放器
    public static final int JSMESSAGE_GOTOPLAYER = 103;//进入播放器
    public static final int SCREEN_CHANGE = 2222;//屏幕方向改变
    
    public static final String JS_FUNCTION_NAME = "android";
    private Context context;
    
    public APIJSTalkInterface(Context context) {
        this.context = context;
    }
    
    @org.xwalk.core.JavascriptInterface
    public void jsAppMediaPlayerBack(String info) {
        Logger.e("info===" + info);
        MessageItem messageItem = new MessageItem(VIDEO_PLAY, info);
        RxBus.getIntanceBus().post(messageItem);
        //        {
        //            "liveStatus": "suspend",
        //            "isLive": "1",
        //            "liveName": "5.03",
        //            "liveDesc": "直播测试",
        //            "liveTime": "14:10~18:10",
        //            "bgImg": "http://file1.teacherv.top/static/lmc/resources/images/placeholder/player_bg_suspend.jpg"
        //        }context.getResources().getConfiguration().orientation;
    }
    
    @org.xwalk.core.JavascriptInterface
    public int getOrientation(String info) {
        return context.getResources().getConfiguration().orientation;
    }
    
    //    @org.xwalk.core.JavascriptInterface
    //    public void jsAppModalChangeBack(String info) {
    //        Logger.e("info===" + info);
    //        MessageItem messageItem = new MessageItem(2, info);
    //        RxBus.getIntanceBus().post(messageItem);
    //        // return context.getResources().getConfiguration().orientation;
    //    }
    
    @org.xwalk.core.JavascriptInterface
    public void jsAppTalkMediaPlayerBack(String json) {
//        Logger.e("talk==" + json);
//        MessageItem messageItem = new MessageItem(JSMESSAGE_TALK, json, VideoPlayerActivity.class.getSimpleName());
//        RxBus.getIntanceBus().post(messageItem);
    }
    
    /**
     * @param type    repeat  重复登录  timeout  超时
     * @param message 错误消息
     */
    @org.xwalk.core.JavascriptInterface
    public void jsAppShowErrorBack(String type, String message) {
        Logger.e("jsAppShowErrorBack==" + message);
        MessageItem messageItem = new MessageItem(KICK_OUT, message);
        RxBus.getIntanceBus().post(messageItem);
    }
    
    @org.xwalk.core.JavascriptInterface
    public void jsAppLMCMediaPlayerBack(String url) {
//        Logger.e("91haoke==" + url);
//        VideoPlayerActivity.start(context, url);
    }
    
    /**
     * @param info
     */
    @org.xwalk.core.JavascriptInterface
    public void jsAppModalChangeBack(String info) {
        Logger.e("info===" + info);
        MessageItem messageItem = new MessageItem(SCREEN_CHANGE, info);
        RxBus.getIntanceBus().post(messageItem);
        // return context.getResources().getConfiguration().orientation;
    }
}
