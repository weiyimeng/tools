package com.gstudentlib.util.passport

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.gaosi.passport.Passport
import com.gaosi.passport.adapter.INetAdapter
import com.gsbaselib.base.GSBaseConstants
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.net.GSRequest
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request
import org.json.JSONObject

/**
 * 作者：created by 逢二进一 on 2019/9/16 13:53
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class PassportNetAdapter: INetAdapter {

    override fun requestFormSynchronization(url: String, param: Map<String, String>): String? {
        val response = OkGo.post<String>(url).tag("passport")
                .headers("ptoken", GSBaseConstants.Token?:"")
                .params(param).execute()
        return response.body()?.string()
    }

    override fun requestJsonSynchronization(url: String, param: Map<String, String>): String? {
        val jsonObject = JSONObject(JSON.toJSONString(param))
        val response = OkGo.post<String>(url).tag("passport")
                .headers("ptoken", GSBaseConstants.Token?:"")
                .upJson(jsonObject).execute()
        return response.body()?.string()
    }

    override fun requestForm(url: String, param: Map<String, String>, adapterCallback: Passport.AdapterCallback) {
        OkGo.post<String>(url).tag("passport")
                .params(param)
                .execute(object: StringCallback() {

                    override fun onStart(request: Request<String, out Request<Any, Request<*, *>>>?) {
                        super.onStart(request)
                        request?.headers("Content-Type", "application/json;charset=UTF-8")
                        if (!TextUtils.isEmpty(GSBaseConstants.Token)) {
                            request?.headers("ptoken", GSBaseConstants.Token)
                        }
                    }

                    override fun onSuccess(response: Response<String>?) {
                        if (response?.body() != null) {
                            LogUtil.d("onSuccess:" + JSON.toJSON(response?.body()))
                            adapterCallback.onCallback(response?.body() , response?.code() , response?.message())
                        }else {
                            adapterCallback.onCallback(null, -1 , "net work error")
                        }
                    }

                    override fun onError(response: Response<String>?) {
                        super.onError(response)
                        if (response?.message() != null) {
                            adapterCallback.onCallback(null , response?.code() , response?.message())
                        }else {
                            adapterCallback.onCallback(null, -1 , "net work error")
                        }
                    }
                })
    }

    override fun requestJson(url: String, param: Map<String , String>, adapterCallback: Passport.AdapterCallback) {
        OkGo.post<String>(url).tag("passport")
                .upJson(GSRequest.getConverterFactory().ObjectToStringConverter().convert(param))
                .execute(object: StringCallback() {

                    override fun onStart(request: Request<String, out Request<Any, Request<*, *>>>?) {
                        super.onStart(request)
                        request?.headers("Content-Type", "application/json;charset=UTF-8")
                        if (!TextUtils.isEmpty(GSBaseConstants.Token)) {
                            request?.headers("ptoken", GSBaseConstants.Token)
                        }
                    }

                    override fun onSuccess(response: Response<String>?) {
                        if (response?.body() != null) {
                            LogUtil.d("onSuccess:" + JSON.toJSON(response?.body()))
                            adapterCallback.onCallback(response?.body() , response?.code() , response?.message())
                        }else {
                            adapterCallback.onCallback(null, -1 , "net work error")
                        }
                    }

                    override fun onError(response: Response<String>?) {
                        super.onError(response)
                        if (response?.message() != null) {
                            adapterCallback.onCallback(null , response?.code() , response?.message())
                        }else {
                            adapterCallback.onCallback(null, -1 , "net work error")
                        }
                    }
                })
    }

}