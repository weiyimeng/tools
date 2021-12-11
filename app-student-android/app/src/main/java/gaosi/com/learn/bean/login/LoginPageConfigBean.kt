package gaosi.com.learn.bean.login

import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2020/2/5 16:45
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class LoginPageConfigBean: Serializable {

    var registerButtonSwitch: Int? = null // 注册按钮开关（0-关闭注册功能，1-开放注册）
    var oneClickLoginButtonSwitch: Int? = 0 // 一键登录按钮开关 （0-关闭， 1-开启)
}