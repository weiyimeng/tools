package com.gstudentlib.bean.gateway

import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2019/11/1 15:45
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
data class OpenApiTokenBean(
        var access_token: String? = "",
        var token_type: String? = "",
        var expires_in: Long? = 0
) : Serializable