package gaosi.com.learn.bean.classlesson.lessondetail

import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2020/4/7 19:33
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class CourseActivationBean: Serializable {

    var isActivation: Int? = null //0 未激活 1 已激活
    var comment: String? = "" //提醒消息

}