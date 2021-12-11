package gaosi.com.learn.bean.aiscene

import java.io.Serializable

class SceneListBean : Serializable {
    var pageNum : Int? = 0
    var count: Int? = 0
    var aiScenesInfo : List<SceneInfo>? = null
}

class SceneInfo : Serializable {
    var scenesId: Int? = 0//场景id
    var scenesName: String? = ""//场景名字
    var scenesIcon: String? = ""//场景Icon图
    var textGuide: String? = ""//中文引导文
    var englishGuide: String? = ""//英文引导文
    var scenesBgImgUrl: String? = ""//背景图
    var scenesThumbnail: String? = ""//场景缩略图
    var entryScenesThumbnail: String? = ""//场景进场缩略图
    var endCnText: String? = ""
    var endSpeechUrl: String? = ""
    var endEnText: String?  =""
    var animationUrl: String? = ""
    var studentScenesStaus: Int? = 0//0未完成 1已完成
    var studentScenesScore: Int? = 0//分数
    var scenesLevel: Int? = 0//0-简单 1-中等 2-复杂
    var scenesIndex: Int? = 0
    var businessUnit: Int? = 0//事业部ID
    var subjectProductId: Int? = 0//学科ID
    var scenesStatus: Int? = 0//场景状态 0下架 1上架
    var latest: Int? = 0//是否为最新版本 1是 0不是
    var version: Int? = 0//版本号 新增场景版本为1
}