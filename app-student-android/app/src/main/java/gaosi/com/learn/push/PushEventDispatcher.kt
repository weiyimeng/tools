package gaosi.com.learn.push

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.github.mzule.activityrouter.router.Routers
import com.gsbaselib.base.GSBaseActivity
import com.gsbaselib.base.log.LogUtil
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.SystemUtil
import gaosi.com.learn.application.WeexApplication
import gaosi.com.learn.studentapp.CommonJsWebActivity
import gaosi.com.learn.studentapp.main.MainActivity
import org.json.JSONObject

/**
 * 推送消息处理工具
 *
 * @author pingfu
 */
object PushEventDispatcher {

    const val PUSH_EVENT = "pushEvent"
    private const val AXX = "axx"
    private const val HTTP = "http"
    private const val SCHEME_AXX = "$AXX://"
    private const val SCHEME_HTTP = "$HTTP://"
    private const val SCHEME_RN = "axx://rnPage"

    fun dispatcher(context: Context?, event: PushEvent?) {
        if (context == null || event == null) {
            return
        }
        val activity = WeexApplication.getApplication().currentActivity
        when {
            activity == null -> {
                //用户未打开app，首先去APP首页
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                val bundle = Bundle()
                bundle.putSerializable(PUSH_EVENT, event)
                intent.putExtras(bundle)
                context.startActivity(intent)
            }
            STBaseConstants.userInfo == null -> //用户没有登录，直接到登录界面
                SchemeDispatcher.jumpPage(activity, "axx://login")
            else -> {
                if (event.isPushMaidian == "1") {
                    GSLogUtil.collectClickLog(event.pushPageId, event.pushButtonId, "")
                }
                gotoPage(activity, event.link ?: "", event.params ?: "")
            }
        }
    }

    /**
     * 页面跳转
     *
     * @param activity 当前页面
     * @param link     跳转地址
     * @param params  传输参数
     */
    fun gotoPage(activity: GSBaseActivity, link: String, params: String) {
        try {
            var mParams = params
            when {
                //原生/web/js跳转
                link.startsWith(SCHEME_AXX) -> {
                    val url = StringBuilder(link)
                    if(TextUtils.isEmpty(mParams)) {
                        mParams = "{}"
                    }
                    val jsonObject = JSONObject(mParams)
                    var pageParam: Map<String, Any>? = null
                    if (jsonObject != null) {
                        pageParam = JSON.parseObject(jsonObject.toString())
                        if (pageParam != null && pageParam.isNotEmpty()) {
                            //rn跳转
                            if (link == SCHEME_RN) {
                                val json = JSONObject()
                                var page = ""
                                for (key in pageParam.keys) {
                                    if (key != "url") {
                                        json.put(key, pageParam[key])
                                    } else {
                                        page = pageParam[key] as String
                                    }
                                }
                                if (!pageParam.containsKey("userId")) {
                                    json.put("userId", STBaseConstants.userId)
                                }
                                val s = "$url?url=%s&param=%s"
                                val pageUrl = String.format(s, page, json.toString())
                                SchemeDispatcher.jumpPage(activity, pageUrl)
                                return
                            } else {
                                url.append("?")
                                var i = 0
                                for (key in pageParam.keys) {
                                    if (i > 0) {
                                        url.append("&")
                                    }
                                    val value = pageParam[key]
                                    if (!TextUtils.isEmpty(key) && value != null) {
                                        url.append(key).append("=").append(pageParam[key])
                                        i++
                                    }
                                }
                            }
                        }
                    }
                    LogUtil.w("url:$url")
                    //支持用户直接打开百度等地址
                    if (url.toString().contains("axx://httpWebView")) {
                        if (pageParam != null) {
                            var httpUrl = ""
                            val mUrl = pageParam["url"].toString()
                            if (pageParam.containsKey("title") && pageParam["title"] != null) {
                                val title = pageParam["title"].toString()
                                if (title == "同步课") {
                                    val intent = Intent(activity, CommonJsWebActivity::class.java)
                                    intent.putExtra("url", "${mUrl}?ptaxxxsapp=${STBaseConstants.Token}&userId=${STBaseConstants.businessUserId}&studentId=${STBaseConstants.userId}&source=app")
                                    activity.startActivity(intent)
                                } else {
                                    httpUrl = "axx://commonJsWeb?url=%s&title=%s"
                                    httpUrl = String.format(httpUrl, mUrl, title)
                                    Routers.openForResult(activity, httpUrl, 149)
                                }
                            } else {
                                httpUrl = "axx://commonJsWeb?url=%s"
                                httpUrl = String.format(httpUrl, mUrl)
                                Routers.openForResult(activity, httpUrl, 149)
                            }
                        }
                    } else {
                        SchemeDispatcher.jumpPage(activity, url.toString())
                    }
                }
                //h5跳转
                link.startsWith(SCHEME_HTTP) -> {
                    SystemUtil.gotoWebPage(activity, link, SystemUtil.generateDefautJsonStr(JSONObject(mParams), link))
                }
                else -> return
            }
        } catch (e: Exception) {
        }
    }
}
