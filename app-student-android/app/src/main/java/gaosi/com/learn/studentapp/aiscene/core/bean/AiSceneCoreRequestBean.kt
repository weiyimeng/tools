package gaosi.com.learn.studentapp.aiscene.core.bean

import gaosi.com.learn.bean.aiscene.SceneStrongPrompt
import gaosi.com.learn.bean.aiscene.SceneStudentAnswerItem
import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2019/8/5 14:38
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class AiSceneCoreRequestBean: Serializable {

    var studentAnswerItems: ArrayList<SceneStudentAnswerItem>? = null
    var strongPrompts: ArrayList<SceneStrongPrompt>? = null

}