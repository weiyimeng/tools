package gaosi.com.learn.bean.aiscene

import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2019/8/12 11:46
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class DialogueAnswerBean: Serializable {

    var studentId: String? = ""
    var status: Int? = null //1 优秀 2 良好 3 未通过
    var promptAction: Int? = null

}