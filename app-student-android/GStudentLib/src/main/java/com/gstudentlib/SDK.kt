package com.gstudentlib

import android.app.Application
import com.lzy.okgo.request.base.Request

/**
 * 作者：created by 逢二进一 on 2019/10/12 15:53
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
interface SDK {

    /**
     * 初始化SDK
     */
    fun init(application: Application)

    /**
     * 配置SDK名称
     */
    fun configSdkName(): String

    /**
     * 配置请求header
     */
    fun configRequestHeader(request: Request<*, out Request<*, *>>)

    /**
     * 获取模块api
     */
    fun getApis(): ArrayList<String>

    /**
     * 获取SDK版本
     */
    fun getVersion(): String

    /**
     * 获取域名
     */
    fun getBaseUrl(): String

    /**
     * 获取请求头
     */
    fun getHeaders(): Map<String , String>

}