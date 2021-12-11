package com.gstudentlib

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.gaosi.passport.Passport
import com.gaosi.passport.PassportAPI
import com.gsbaselib.InitBaseLib
import com.gsbaselib.base.BaseTaskSwitch
import com.gsbaselib.base.GSBaseApplication
import com.gsbaselib.base.GSBaseConstants
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.ICallbackErrorListener
import com.gsbaselib.net.interceptor.Interceptor
import com.gsbaselib.utils.LOGGER
import com.gsbaselib.utils.SharedPreferenceUtil
import com.gsbaselib.utils.WalleChannelReader
import com.gsbaselib.utils.update.IResUpdateListener
import com.gsbaselib.utils.update.UpdateUtil
import com.gsbaselib.utils.upload.GSUploader
import com.gsbaselib.utils.upload.GSUploader2
import com.gsbiloglib.builder.*
import com.gstudentlib.base.STBaseConfigManager
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.bean.HostBean
import com.gstudentlib.bean.StudentInfo
import com.gstudentlib.bean.gateway.OpenApiTokenBean
import com.gstudentlib.bean.gateway.OpenApiTokenDataBean
import com.gstudentlib.util.passport.PassportNetAdapter
import com.lzy.okgo.OkGo
import com.qiyukf.unicorn.api.*
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.Util
import org.json.JSONObject
import java.nio.charset.Charset
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * 作者：created by 逢二进一 on 2019/9/26 11:23
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
object SDKConfig {

    private val mSDK = HashMap<String , SDK>()
    private lateinit var application: Application //应用
    private var updateServer: String? = "https://api.aixuexi.com/" //检查更新server
    private var studentServer: String? = "https://c.aixuexi.com/" //业务server
    private var buildType: String? = "release" // debug、release、monkey、monkeyRelease、beta
    private var applicationId: String? = null //应用ID
    private var isDebug: Boolean? = false //是否是debug模式
    private var projectName: String? = null //项目名称，在埋点中的落库
    private var onTaskSwitchListener: BaseTaskSwitch.OnTaskSwitchListener? = null //前后台切换监听
    private var requestInterceptor: Interceptor.RequestInterceptor? = null //请求拦截监听
    private var responseInterceptor: Interceptor.ResponseInterceptor? = null //响应拦截监听
    private var customInterceptor: Interceptor.CustomInterceptor? = null //自定义拦截监听
    private var callbackErrorListener: ICallbackErrorListener? = null //网络请求异常监听
    private var mStatusBarNotificationConfig: StatusBarNotificationConfig? = null
    private var mUICustomization: UICustomization? = null
    private var qiyuTestKey: String? = null
    private var qiyuReleaseKey: String? = null
    private var updateAppId: String? = null
    private var updateSerialNumber: String? = null
    private var clientId: String? = null
    private var secret: String? = "" //网关密钥
    private var signSecret: String? = "" //签名密钥
    private const val SDK_TOKEN = "axx_sdk_openapi"
    private var mSdkToken: OpenApiTokenBean? = null
    private var passportFlag: String? = ""
    private var passportRefreshCallback: Passport.RefreshTokenCallback? = null
    private var refreshTokenListener: RefreshTokenListener? = null
    private var mIpList: List<HostBean>? = null
    private var mApiVersion: String? = "v16" //api请求的版本

    /**
     * 添加application
     * 初始化拦截器
     * 初始化七鱼客服
     */
    fun with(application: Application): SDKConfig {
        this.application = application
        mStatusBarNotificationConfig = StatusBarNotificationConfig()
        mUICustomization = UICustomization()
        customInterceptor = object : Interceptor.CustomInterceptor {
            override fun interceptor(chain: okhttp3.Interceptor.Chain): Response {
                if (STBaseConstants.isUseIp) {
                    if (mIpList == null) {
                        getIpList()
                    }
                } else {
                    mIpList = null
                }
                var resuest = chain.request()
                var response: Response
                //SDK网关的验证与请求(验证的是当前的网关token是否存在)
                if(null == mSdkToken && resuest.url().toString() != GSAPI.gatewayLogin) {
                    requestGatewaySynchronization()
                    if(null != mSdkToken) {
                        resuest = oldRequestToNewRequest(resuest)
                    }
                }
                var isContains = false
                //用于确认当前的请求地址在业务请求内
                when {
                    resuest.url().toString().contains(GSAPI.BASE_URL) -> isContains = true
                    resuest.url().toString().contains(GSAPI.SDK_BASE_URL) -> isContains = true
                    resuest.url().toString().contains(GSAPI.SIGAO_BASE_URL) -> isContains = true
                    resuest.url().toString().contains(GSAPI.DT_BASE_URL) -> isContains = true
                    resuest.url().toString().contains(GSAPI.SDK_AXX_BASE_URL) -> isContains = true
                }
                //对当前业务进行数据请求
                resuest = oldRequestToNewRequest(resuest)
                response = chain.proceed(resuest)
                //对当前业务的请求进行验证，确认token是否过期
                if(resuest.url().toString() != GSAPI.gatewayLogin
                        && !resuest.url().toString().contains("surrogates/passport/user/refresh")
                        && isContains) {
                    val bodyString = getResponseBodyString(response)
                    LogUtil.d("Interceptor：$bodyString  状态码：${response.code()} 请求地址：${resuest.url().toString()}")
                    bodyString?.let {
                        try{
                            val bodyJson = JSONObject(bodyString)
                            val error = bodyJson.optString("error" , "")
                            if(!TextUtils.isEmpty(error)) { //说明是网关接口过期了
                                requestGatewaySynchronization()
                                resuest = oldRequestToNewRequest(resuest)
                                //请求重试
                                response = chain.proceed(resuest)
                            } else {//说明是业务接口过期了
                                val errorCode = bodyJson.optInt("errorCode" , -1)
                                if(PassportAPI.CODE_1006 == errorCode.toString() || PassportAPI.CODE_1007 == errorCode.toString()) {
                                    PassportAPI.instance.refreshSynchronization(object : Passport.CheckTokenValidity{
                                        override fun onPass() {
                                            STBaseConstants.Token = PassportAPI.instance.getToken_S()
                                            updateInfo(STBaseConstants.Token)
                                            refreshTokenListener?.onPassportRefresh(PassportAPI.instance.getToken_S() , PassportAPI.instance.getToken_L())
                                            resuest = oldRequestToNewRequest(resuest)
                                            //请求重试
                                            response = chain.proceed(resuest)
                                        }
                                        override fun onFail() {
                                        }
                                    })
                                }
                            }
                        }catch (e: Exception){}
                    }
                }
                return response
            }
        }
        return this
    }

    /**
     * 配置更新服务器
     */
    fun configUpdateServer(updateServer: String?): SDKConfig {
        this.updateServer = updateServer
        return this
    }

    /**
     * 配置业务服务器
     */
    fun configStudentServer(studentServer: String?): SDKConfig {
        this.studentServer = studentServer
        return this
    }

    /**
     * 配置构建类型
     */
    fun configBuildType(buildType: String?): SDKConfig {
        this.buildType = buildType
        return this
    }

    fun configApplicationId(applicationId: String?): SDKConfig {
        this.applicationId = applicationId
        return this
    }

    fun configIsDebug(isDebug: Boolean?): SDKConfig {
        this.isDebug = isDebug
        return this
    }

    fun configProjectName(projectName: String?): SDKConfig {
        this.projectName = projectName
        return this
    }

    fun configOnTaskSwitchListener(onTaskSwitchListener: BaseTaskSwitch.OnTaskSwitchListener?): SDKConfig {
        this.onTaskSwitchListener = onTaskSwitchListener
        return this
    }

    /**
     * 配置自定义拦截器
     */
    fun configInterceptor(requestInterceptor: Interceptor.RequestInterceptor? , responseInterceptor: Interceptor.ResponseInterceptor?): SDKConfig {
        this.requestInterceptor = requestInterceptor
        this.responseInterceptor = responseInterceptor
        return this
    }

    /**
     * 配置OKIO拦截器
     */
    fun configInterceptor(customInterceptor: Interceptor.CustomInterceptor?): SDKConfig {
        this.customInterceptor = customInterceptor
        return this
    }

    fun configCallbackErrorListener(callbackErrorListener: ICallbackErrorListener?): SDKConfig {
        this.callbackErrorListener = callbackErrorListener
        return this
    }

    /**
     * 配置回调，比如被踢等
     */
    fun configPassportRefreshCallback(passportRefreshCallback: Passport.RefreshTokenCallback?): SDKConfig {
        this.passportRefreshCallback = passportRefreshCallback
        return this
    }

    /**
     * 配置token刷新回调
     */
    fun configRefreshTokenListener(refreshTokenListener: RefreshTokenListener): SDKConfig {
        this.refreshTokenListener = refreshTokenListener
        return this
    }

    fun configQiYuTestKey(qiyuTestKey: String?): SDKConfig {
        this.qiyuTestKey = qiyuTestKey
        return this
    }

    fun configQiYuReleaseKey(qiyuReleaseKey: String?): SDKConfig {
        this.qiyuReleaseKey = qiyuReleaseKey
        return this
    }

    /**
     * 配置网关 clientId  secret
     */
    fun configGatewayId(clientId: String? , secret: String?): SDKConfig {
        this.clientId = clientId
        this.secret = secret
        return this
    }

    /**
     * 配置签名密钥
     */
    fun configSignSecret(signSecret: String?) : SDKConfig {
        this.signSecret = signSecret
        return this
    }

    fun configPassportFlag(passportFlag: String?): SDKConfig {
        this.passportFlag = passportFlag
        return this
    }

    /**
     * 设置点击Notification消息后进入的页面
     * @param activity
     */
    fun setServiceEntranceActivity(activity: Class<out Activity>) {
        this.mStatusBarNotificationConfig?.notificationEntrance = activity
    }

    /**
     * 设置聊天头像
     */
    fun setCustomization(rightAvatar: String) {
        this.mUICustomization?.rightAvatar = rightAvatar
    }

    /**
     * 配置更新模块对应的AppId
     */
    fun configUpdateAppId(updateAppId: String?): SDKConfig {
        this.updateAppId = updateAppId
        return this
    }

    /**
     * 配置更新模块对应的业务线
     */
    fun configUpdateSerialNumber(updateSerialNumber: String?): SDKConfig {
        this.updateSerialNumber = updateSerialNumber
        return this
    }

    /**
     * 配置接口版本
     */
    fun configApiVersion(apiVersion: String?): SDKConfig {
        this.mApiVersion = apiVersion
        return this
    }

    /**
     * 注冊SDK
     */
    fun registerSDK(sdk : SDK): SDKConfig {
        this.mSDK[sdk.configSdkName()] = sdk
        return this
    }

    /**
     * 如果返回值为null，则全部使用默认参数。
     */
    private fun options(): YSFOptions {
        val options = YSFOptions()
        options.statusBarNotificationConfig = mStatusBarNotificationConfig
        options.statusBarNotificationConfig.vibrate = false
        options.uiCustomization = mUICustomization
        options.savePowerConfig = SavePowerConfig()
        return options
    }

    /**
     * 初始化
     */
    fun initLib() {
        InitBaseLib.getInstance()
                .configMockManager(object : STBaseConfigManager(){
                    override fun init() {
                        /**更新服务地址*/
                        BASE_UPDATE_SERVER = updateServer
                        BASE_DEFAULT_SERVER = studentServer
                        DEFAULT_SERVER = BASE_DEFAULT_SERVER
                        /**默认类型*/
                        BASE_DEFAULT_TYPE = buildType//定义类型的形式出现
                        if (!isRelease) {
                            val updateUrl = SharedPreferenceUtil.getStringValueFromSP(Tag, UPDATE, "")
                            if (!TextUtils.isEmpty(updateUrl)) {
                                BASE_UPDATE_SERVER = updateUrl
                            }
                            val defaultUrl = SharedPreferenceUtil.getStringValueFromSP(Tag, SERVER, "")
                            if (!TextUtils.isEmpty(defaultUrl)) {
                                BASE_DEFAULT_SERVER = defaultUrl
                            }
                        }
                        LogUtil.i("GSConfigManager：" + JSON.toJSONString(this))
                    }
                })
                .configConstants(object : STBaseConstants() {
                    override fun init(context: GSBaseApplication?) {
                        super.init(context)
                        GSBaseConstants.apiVersion = mApiVersion
                        GSBaseConstants.isDebug = SDKConfig.isDebug ?: true
                        GSBaseConstants.applicationId = SDKConfig.applicationId
                        hasLogin()//可能存在用户在操作时出现崩溃，需要重新初始化一下
                        GSBaseConstants.projectName = SDKConfig.projectName
                        val channelInfo = WalleChannelReader.getChannelInfo(application as Context)
                        if (channelInfo != null) {
                            GSBaseConstants.channel = channelInfo.channel
                        }
                    }
                })
                .configRequest()
                .configTaskSwitch(this.onTaskSwitchListener)
                .configAlbum()
                .configUpload(GSUploader2("APP_STUDENT"), GSUploader())
                .configNetInterceptor(this.requestInterceptor, this.responseInterceptor)
                .configCustomInterceptor(this.customInterceptor)
                .configCallbackErrorListener(this.callbackErrorListener)
                .configRequestHeader { request ->
                    this.mSDK.forEach {
                        for(sdkApi in it.value.getApis()) {
                            val apiLength = sdkApi.length
                            var url = when {
                                sdkApi.contains(GSAPI.BASE_URL) -> sdkApi.substring(GSAPI.BASE_URL.length, apiLength)
                                sdkApi.contains(GSAPI.SDK_BASE_URL) -> sdkApi.substring(GSAPI.SDK_BASE_URL.length, apiLength)
                                sdkApi.contains(GSAPI.SIGAO_BASE_URL) -> sdkApi.substring(GSAPI.SIGAO_BASE_URL.length, apiLength)
                                sdkApi.contains(GSAPI.DT_BASE_URL) -> sdkApi.substring(GSAPI.DT_BASE_URL.length, apiLength)
                                sdkApi.contains(GSAPI.SDK_AXX_BASE_URL) -> sdkApi.substring(GSAPI.SDK_AXX_BASE_URL.length, apiLength)
                                else -> ""
                            }
                            if(request.url.contains(url)) {
                                it.value.configRequestHeader(request)
                                return@forEach
                            }
                        }
                    }
                    this.mSDK["app"]?.configRequestHeader(request)
                }
        this.configBi()
        this.configQiyu()
        this.configPassport()
        this.mSDK.forEach {
            it.value.init(application)
            LogUtil.d("SDK名称：" + it.value.configSdkName() + " SDK版本：" + it.value.getVersion())
        }
        val sdkTokenString = SharedPreferenceUtil.getStringValueFromSP("axxuserInfo", SDK_TOKEN, "")
        if(!TextUtils.isEmpty(sdkTokenString)) {
            mSdkToken = JSON.parseObject(sdkTokenString , OpenApiTokenBean::class.java)
        }
        getIpList()
    }

    /**
     * 配置BIsdk
     */
    private fun configBi() {
        Builder()
                .setContext(application)
                .setAppVersion(GSBaseConstants.deviceInfoBean.appVersion)
                .setDebug(GSBaseConstants.isDebug)
                .setOpenHookBi(true)
                .setDeviceId(GSBaseConstants.deviceInfoBean.deviceId)
                .setKeyId("LTAI4G5neh5cHcoBScwkMjmi" , "wktXiNvo3xLCQwrCsGtd6IVuvi3pMw")
                .setProjectName(object : IProjectName {
                    override fun projectName(): String? {
                        return GSBaseConstants.projectName
                    }
                })
                .setAnalysisDataTrack(object : IAnalysisDataTrack {
                    override fun jsonToObject(s: String?, aClass: Class<DataTrackBean>): DataTrackBean? {
                        return JSON.parseObject(s, aClass)
                    }
                })
                .setUserId(object : IUserId {
                    override fun getUserId(): String? {
                        return GSBaseConstants.userId
                    }
                })
                .apply()
    }

    /**
     * 配置七鱼客服
     */
    private fun configQiyu() {
        Unicorn.init(application, (if(isDebug == true) qiyuTestKey else qiyuReleaseKey) , options(), object : UnicornImageLoader {
            override fun loadImageSync(s: String, i: Int, i1: Int): Bitmap? {
                return null
            }
            override fun loadImage(s: String, width: Int, height: Int, imageLoaderListener: ImageLoaderListener?) {
                var mWidth = width
                var mHeight = height
                if (mWidth <= 0 || mHeight <= 0) {
                    mHeight = Integer.MIN_VALUE
                    mWidth = mHeight
                }
                val options = RequestOptions()
                        .override(mWidth, mHeight)
                Glide.with(GSBaseApplication.getApplication())
                        .asBitmap()
                        .load(s)
                        .apply(options)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                imageLoaderListener?.onLoadComplete(resource)
                            }

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                super.onLoadFailed(errorDrawable)
                                imageLoaderListener?.onLoadFailed(null)
                            }
                        })
            }
        })
    }

    /**
     * 配置passport
     */
    private fun configPassport(){
        if(null == passportRefreshCallback) {
            passportRefreshCallback = object : Passport.RefreshTokenCallback {
                override fun onRefresh(code: String?, message: String?) {
                    when(code) {
                        PassportAPI.CODE_1 -> {
                            val token_s = PassportAPI.instance.getToken_S()
                            val token_l = PassportAPI.instance.getToken_L()
                            updateInfo(token_s)
                            LogUtil.d("token_s====$token_s ==== token_l$token_l")
                        }
                        PassportAPI.CODE_1003 -> {
                            if (GSBaseApplication.getApplication().currentActivity != null) {
                                GSBaseApplication.getApplication().currentActivity.kickOut("您的账号已经在其它设备登录，请重新登录！")
                            }
                        }
                        PassportAPI.CODE_1006, PassportAPI.CODE_1007, PassportAPI.CODE_1009, PassportAPI.CODE_1012, PassportAPI.CODE_1013, PassportAPI.CODE_1014 -> {
                            if (GSBaseApplication.getApplication().currentActivity != null) {
                                GSBaseApplication.getApplication().currentActivity.kickOut("验证失效，请重新登录！")
                            }
                        }
                    }
                }
            }
        }
        PassportAPI.instance.init(STBaseConstants.deviceInfoBean.deviceId , getPassportFlag()?:"" , application)
                .setNetAdapter(PassportNetAdapter())
                .setRefreshTokenCallback(this.passportRefreshCallback!!)
                .setDebug(GSBaseConstants.isDebug)
    }

    /**
     * 配置更新
     * 请在启动页进行配置
     */
    fun configUpdate(context: Context , resUpdateListener: IResUpdateListener?) {
        UpdateUtil.getInstance()
                .inject(context)
                .register(updateAppId
                        , updateSerialNumber
                        , applicationId)
                .setListener(resUpdateListener)
                .startCheckWebResource()
    }

    /**
     * 初始化StudentInfo、学生Id、用户Id、账号、学生名字、头像、性别、是否北校、机构名称
     */
    fun initInfo(studentId: String?
                 , userId: String?
                 , telePhone: String?
                 , name: String?
                 , headerPng: String?
                 , sex: String?
                 , isBeiXiao: Int
                 , institutionName: String?
                 , token: String?) {
        val userInfo = StudentInfo()
        userInfo.path = headerPng
        userInfo.id = studentId
        userInfo.userId = userId
        userInfo.institutionName = institutionName
        userInfo.parentTel1 = telePhone
        userInfo.isBeiXiao = isBeiXiao
        userInfo.truthName = name
        userInfo.sex = sex
        this.updateInfo(userInfo, token)
    }

    /**
     * 更新用户信息
     */
    fun updateInfo(userInfo: StudentInfo? , token: String?) {
        STBaseConstants.updateUserInfo(userInfo , token)
    }

    /**
     * 更新用户信息
     */
    fun updateInfo(token: String?) {
        STBaseConstants.updateUserInfo(STBaseConstants.userInfo , token)
    }

    /**
     * 更新passportToken
     * token passportToken
     * refreshToken passportRefreshToken
     */
    fun updatePassportToken(token: String, refreshToken: String) {
        PassportAPI.instance.updateToken(token , refreshToken)
    }

    /**
     * 获取响应结果
     */
    fun getResponseBodyString(response: Response): String? {
        val responseBody = response.body()
        val source = responseBody?.source()
        source?.request(Integer.MAX_VALUE.toLong())
        val buffer = source?.buffer()
        var charset: Charset ?= null
        val contentType = responseBody?.contentType()
        if(contentType != null) {
            charset = contentType?.charset()
            charset = charset ?: Util.UTF_8
        }
        return buffer?.clone()?.readString(charset?: Util.UTF_8)
    }

    /**
     * 同步请求网关服务
     */
    @Synchronized
    private fun requestGatewaySynchronization() {
        val jsonObject = JSONObject()
        val timestamp = System.currentTimeMillis().toString()
        jsonObject.put("clientId" , clientId)
        jsonObject.put("timestamp" , timestamp)
        jsonObject.put("sign" , sign(clientId + timestamp , signSecret?:""))
        val gateResponse = OkGo.post<String>(GSAPI.gatewayLogin)
                .upJson(jsonObject).execute()
        val reponseBody = gateResponse.body()?.string()
        if(!TextUtils.isEmpty(reponseBody)) {
            try{
                val responseJsonObject = JSONObject(reponseBody)
                val status = responseJsonObject.optInt("status" , 0)
                if(1 == status) {
                    val bodyString = responseJsonObject.optString("data")
                    if(!TextUtils.isEmpty(bodyString)) {
                        val openApiTokenDataBean = GSRequest.getConverterFactory().StringToObjectConverter(OpenApiTokenDataBean::class.java).convert(bodyString)
                        mSdkToken = (openApiTokenDataBean as OpenApiTokenDataBean).openApiToken
                        SharedPreferenceUtil.setStringDataIntoSP("axxuserInfo", SDK_TOKEN, JSON.toJSONString(mSdkToken))
                        refreshTokenListener?.onGatewayRefresh()
                    }
                }
            }catch (e: Exception){}
        }
    }

    /**
     * 因为token的过期导致该请求需要重新创建请求头，因此需要重新进行处理
     */
    private fun oldRequestToNewRequest(request: Request): Request {
        val requestBuilder = request.newBuilder()
        var newUrl : HttpUrl? = request.url()
        this.mSDK.forEach {
            for(sdkApi in it.value.getApis()) {
                val apiLength = sdkApi.length
                var url = when {
                    sdkApi.contains(GSAPI.BASE_URL) -> sdkApi.substring(GSAPI.BASE_URL.length, apiLength)
                    sdkApi.contains(GSAPI.SDK_BASE_URL) -> sdkApi.substring(GSAPI.SDK_BASE_URL.length, apiLength)
                    sdkApi.contains(GSAPI.SIGAO_BASE_URL) -> sdkApi.substring(GSAPI.SIGAO_BASE_URL.length, apiLength)
                    sdkApi.contains(GSAPI.DT_BASE_URL) -> sdkApi.substring(GSAPI.DT_BASE_URL.length, apiLength)
                    sdkApi.contains(GSAPI.SDK_AXX_BASE_URL) -> sdkApi.substring(GSAPI.SDK_AXX_BASE_URL.length, apiLength)
                    else -> ""
                }
                if(!TextUtils.isEmpty(url)) {
                    if(request.url().toString().contains(url)) {
                        for(header in it.value.getHeaders().entries) {
                            requestBuilder.header(header.key, header.value)
                        }
                        if (mIpList != null) {
                            mIpList?.forEach { hostBean ->
                                if (request.url().toString().contains(hostBean.url ?: "")) {
                                    val oldUrl = request.url()
                                    when {
                                        oldUrl.toString().contains("xsd") -> {
                                            var oldUrlStr = oldUrl.toString()
                                            oldUrlStr = oldUrlStr.replace("xsd/", "")
                                            newUrl = oldUrl.newBuilder(oldUrlStr)
                                                    ?.scheme("http")
                                                    ?.host(hostBean.host ?: "")
                                                    ?.port(hostBean.port?.toInt() ?: 8009)
                                                    ?.build()
                                        }
                                        oldUrl.toString().contains("wjd") -> {
                                            var oldUrlStr = oldUrl.toString()
                                            oldUrlStr = oldUrlStr.replace("wjd/", "")
                                            newUrl = oldUrl.newBuilder(oldUrlStr)
                                                    ?.scheme("http")
                                                    ?.host(hostBean.host ?: "")
                                                    ?.port(hostBean.port?.toInt() ?: 8009)
                                                    ?.build()
                                        }
                                        else -> newUrl = oldUrl.newBuilder()
                                                .scheme("http")
                                                .host(hostBean.host ?: "")
                                                .port(hostBean.port?.toInt() ?: 8009)
                                                .build()
                                    }
                                }
                            }
                            return request.newBuilder().url(newUrl).build()
                        } else {
                            return requestBuilder.build()
                        }
                    }
                }
            }
        }
        this.mSDK["app"]?.let {
            for(header in it.getHeaders()?.entries) {
                requestBuilder.header(header.key, header.value)
            }
        }
        if (mIpList != null) {
            mIpList?.forEach { hostBean ->
                if (request.url().toString().contains(hostBean.url ?: "")) {
                    val oldUrl = request.url()
                    when {
                        oldUrl.toString().contains("surrogates") -> {
                            var oldUrlStr = oldUrl.toString()
                            oldUrlStr = oldUrlStr.replace("surrogates/", "")
                            newUrl = oldUrl.newBuilder(oldUrlStr)
                                    ?.scheme("http")
                                    ?.host(hostBean.host ?: "")
                                    ?.port(hostBean.port?.toInt() ?: 8009)
                                    ?.build()
                        }
                        oldUrl.toString().contains("xsd") -> {
                            var oldUrlStr = oldUrl.toString()
                            oldUrlStr = oldUrlStr.replace("xsd/", "")
                            newUrl = oldUrl.newBuilder(oldUrlStr)
                                    ?.scheme("http")
                                    ?.host(hostBean.host ?: "")
                                    ?.port(hostBean.port?.toInt() ?: 8009)
                                    ?.build()
                        }
                        else -> newUrl = oldUrl.newBuilder()
                                .scheme("http")
                                .host(hostBean.host ?: "")
                                .port(hostBean.port?.toInt() ?: 8009)
                                .build()
                    }
                }
            }
            return request.newBuilder().url(newUrl).build()
        } else {
            return requestBuilder.build()
        }
    }

    /**
     * 进行数据签名
     */
    fun sign(stringToHash: String , signSecret: String): String {
        val sign = StringBuilder()
        val mac = Mac.getInstance("HmacSHA1")
        val secretKey = SecretKeySpec(signSecret.toByteArray() , mac.algorithm)
        mac.init(secretKey)
        try{
            val hash = mac.doFinal(stringToHash.toByteArray())
            for(b in hash) {
                var str = Integer.toHexString(b.toInt() and 0xff)
                if (str.length == 1) {
                    str = "0$str"
                }
                sign.append(str)
            }
        }catch (e: Exception){}
        return sign.toString()
    }

    /**
     * 获取SDKtoken
     */
    fun getSDKToken(): OpenApiTokenBean? {
        return mSdkToken
    }

    /**
     * 获取SDK
     */
    fun getSDK(): HashMap<String , SDK> {
        return this.mSDK
    }

    fun getApplication(): Application {
        return application
    }

    fun getIsDebug(): Boolean? {
        return isDebug
    }

    /**
     * 获取passport标识，用于header的token验证
     */
    fun getPassportFlag() : String? {
        return passportFlag
    }

    /**
     * 获取sdk 客户端id
     */
    fun getSDKClientId() : String? {
        return clientId
    }

    /**
     * 获取sdk 网关secret
     */
    fun getSDKSecret() : String? {
        return secret
    }

    /**
     * 获取签名密钥
     */
    fun getSignSecret() : String? {
        return signSecret
    }

    /**
     * 退出登录
     */
    fun exitLogin() {
        STBaseConstants.clearUserInfo()
        this.mSdkToken = null
        SharedPreferenceUtil.setStringDataIntoSP("axxuserInfo", SDK_TOKEN, "")
    }

    /**
     * 获取ip列表
     */
    private fun getIpList() {
        try {
            val stream = GSBaseApplication.getApplication().assets.open("ip.json")
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            val text = String(buffer, Charset.forName("utf-8"))
            mIpList = JSON.parseArray(text, HostBean::class.java)
        } catch (e: Exception) {
            LOGGER.log(e)
        }
    }

    /**
     * passport刷新接口
     */
    interface RefreshTokenListener{

        /**
         * passport出现刷新
         */
        fun onPassportRefresh(ptoken: String? , refreshToken: String?)

        /**
         * 网关token出现刷新
         */
        fun onGatewayRefresh()

    }

}