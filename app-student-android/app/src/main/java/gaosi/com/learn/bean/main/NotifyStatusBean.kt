package gaosi.com.learn.bean.main

import java.io.Serializable

/**
 * description:
 * created by huangshan on 2020-04-10 10:59
 */
data class NotifyStatusBean(
     var type: String? = "", //通知类型
     var notify: Int? = 0 //1-有未读，0-无未读
) : Serializable
