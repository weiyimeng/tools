package gaosi.com.learn.bean.aiscene

import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2019/8/6 16:10
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class SceneBean : Serializable {
    
    var scenesName: String? = "" //场景名字
    var textGuide: String? = "" //中文引导
    var englishGuide: String? = "" //英文引导
    var scenesBgImgUrl: String? = "" //情景背景图片
    var endCnText: String? = "" //中文结束语
    var endSpeechUrl: String? = "" //结束语音
    var endEnText: String? = "" //英文结束语
    var animationUrl: String? = ""
    var scenesId: Int? = 0 //场景id
    var version: Int? = 0 //版本
    var dialogues: ArrayList<SceneDialogues>? = null
    
}

class SceneDialogues : Serializable {
    
    var id: Int? = 0
    var deleted: Int? = 0
    var createTime: Long? = 0
    var creatorId: Int? = 0
    var updateTime: Long? = 0
    var updaterId: Int? = 0
    var scenesId: Int? = 0 //场景id
    var scenesVersion: Int? = 0
    var dialogueNum: Int? = 0
    var weakPromptText: String? = "" //弱提示1
    var teacherQuestionItem: SceneTeacherQuestionItem? = null
    var studentAnswerItems: ArrayList<SceneStudentAnswerItem>? = null
    var strongPrompts: ArrayList<SceneStrongPrompt>? = null
    var maxDepth: Int? = 0 //距离末尾剩余步数
    
}

class SceneTeacherQuestionItem : Serializable {
    
    var audioUrl: String? = ""
    var promptImg: String? = ""
    var questionEnglish: String? = ""
    var questionChinese: String? = ""
    var promptText: String? = ""
    
}

class SceneStudentAnswerItem : Serializable {
    
    var keyWords: ArrayList<String>? = null //关键字
    var jumpDialogueNumId: Int? = 0 //跳转对话组
    
}

class SceneStrongPrompt : Serializable {
    
    var audioUrl: String? = ""
    var questionEnglish: String? = ""
    var questionChinese: String? = ""
    var jumpDialogueNumId: Int? = 0
    
}
