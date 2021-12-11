package gaosi.com.learn.push

import java.io.Serializable

/**
 * 推送消息数据结构
 *
 * @author pingfu
 */
class PushEvent : Serializable{
    //业务线类型，详情见WIKI：http://iwork.gaosiedu.com/pages/viewpage.action?pageId=28541940
    var appId: Int = 0

    //消息类型
    var type: Int = 0

    //跳转地址，最好和路由跳转一致
    var link: String? = ""

    //参数
    var params: String? = ""

    //推送消息标题
    var title: String? = ""

    //推送消息内容
    var content: String? = ""

    //是否埋点 1埋  0不埋
    var isPushMaidian: String? = ""

    //埋点页面ID
    var pushPageId: String? = ""

    //点击事件ID
    var pushButtonId: String? = ""
}
