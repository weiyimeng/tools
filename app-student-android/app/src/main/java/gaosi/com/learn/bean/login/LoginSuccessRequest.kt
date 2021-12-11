package gaosi.com.learn.bean.login

import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2019/7/31 14:19
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class LoginSuccessRequest : Serializable {

    var phone: String? = "" //手机号
    var studentId: String? = "" //用户id

}