package com.gstudentlib.bean

import java.io.Serializable

/**
 * description:
 * created by huangshan on 2020-03-25 13:36
 */
class HostBean : Serializable {
    var url: String? = ""//域名
    var host: String? = ""//host ip
    var port: String? = "8009"//端口
}