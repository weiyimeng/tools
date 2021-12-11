package gaosi.com.learn.bean.login

import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2020/6/5 16:57
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class StudentInfoBody: Serializable {

    var changedPasswordCode: Int? = 0
    var beixiao: Boolean? = false
    var studentPage: StudentInfoPage? = null

}