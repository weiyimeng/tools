package com.gstudentlib

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.gsbaselib.base.GSBaseApplication
import java.io.Serializable
import java.nio.charset.Charset
import java.util.HashMap

/**
 * 作者：created by 逢二进一 on 2019/9/11 19:20
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
object StatisticsDictionary {

    //切换到后台页面
    const val systemHomePage = "as_SystemHomePage"

    //启动页
    private const val splash = "as1"

    //首页
    const val homePage = "as101"

    //首页 学情报告
    const val learnReport = "as1009"

    //首页 我的
    const val mine = "as102"

    //登录
    private const val loginPage = "as2"

    //全部作业
    const val allHomework = "as401"

    //h5弹框页面
    const val h5Dialog = "as1001"

    //切换用户
    const val switchUser = "as1003"

    //忘记密码
    const val updatePsw = "as1002"

    //装扮城
    const val dressCenter = "as1004"

    //装扮自己
    const val dressSelf = "as413"

    //装扮简介
    const val dressDetail = "as1005"

    //双师回放
    const val doubleTeacher = "as1006"

    //选择图片
    const val zoomImage = "as1007"

    //英语语音订正
    const val englishVoice = "as1011"

    //交英语作业
    const val englishVoiceHomework = "as1012"

    //英语答题卡
    const val  englishAnswerCard = "as4052"

    //近日直播
    const val liveCenter = "as1008"

    //web直播
    const val webLive = "as1010"

    //native交作业
    const val doHomework = "as402"

    //答题卡
    const val answercard = "as403"

    //主观题自评
    const val selfRating = "as410"

    //题目解析
    const val parsingHomework = "as406"

    //相似题解析
    const val parsingSingleHomework = "XSD_629"

    //题目订正
    const val correctHomework = "as407"

    //设置
    const val setting = "as302"

    //全部班级列表
    const val allClassList = "as417"

    //本讲内容（讲次详情页面）
    private const val lessonDetail = "as103"

    //每周任务
    const val weeklyTask = "as104"

    //扫一扫
    const val qrCode = "as206"

    //专题课视频内容页
    const val threeSpecialContent = "as4055"

    //专题课确认弹框
    const val threeSpecialConfirm = "as4057"

    //专题课报告弹框
    const val threeSpecialReport = "as4058"

    //专题课题板
    const val threeSpecialOption = "as4056"

    //视频加载失败弹框
    const val threeSpecialVideoLoadFail = "as4059"

    //预习内容页面
    const val threePreEnglishExercise = "as4054"

    //验证激活码页面
    const val activationCode = "as105"

    //激活码验证成功页面
    const val activeSuccess = "as106"

    //验证班级码
    private const val activeClassRome = "as1013"

    //注册账号
    const val registerAccountRome = "as1014"

    //添加学员信息
    const val attendClassRome = "as1015"

    //班级码输入 选择学员
    const val selectStudentRome = "as1016"

    //解析视频页面
    const val analysisVideo = "as207"

    //情景对话列表
    const val sceneList = "as600"

    //情景对话转场
    const val sceneEntrance = "as601"

    //情景对话页
    const val aiScene = "as602"

    private var webPageDictionary: HashMap<String?, String?>? = null

    fun init() {
        if (webPageDictionary == null) {
            webPageDictionary = HashMap()
            try {
                val stream = GSBaseApplication.getApplication().assets.open("pad.json")
                val size = stream.available()
                val buffer = ByteArray(size)
                stream.read(buffer)
                stream.close()
                val text = String(buffer, Charset.forName("utf-8"))
                val pads = JSON.parseArray(text, Pad::class.java)
                if (pads != null) {
                    for (pad in pads) {
                        if (pad != null) {
                            (webPageDictionary as HashMap<String?, String?>)[pad.name] = pad.pad
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getWebPageId(url: String): String? {
        if (TextUtils.isEmpty(url)) {
            return ""
        } else if (webPageDictionary != null && webPageDictionary?.size != 0) {
            return webPageDictionary?.get(url)
        } else {
            object : Thread() {
                override fun run() {
                    super.run()
                    init()
                }
            }.start()
        }
        return ""
    }

    private class Pad : Serializable {
        var name: String? = ""
        var pad: String? = ""
        var title: String? = ""
    }

}