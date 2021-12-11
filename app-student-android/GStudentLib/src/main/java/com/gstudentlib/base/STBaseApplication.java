package com.gstudentlib.base;

import android.text.TextUtils;

import com.gaosi.passport.PassportAPI;
import com.gsbaselib.base.GSBaseApplication;
import com.gsbaselib.base.log.LogUtil;
import com.gsbaselib.net.GSRequest;
import com.gsbaselib.net.ICallbackErrorListener;
import com.gsbaselib.net.interceptor.Interceptor;
import com.gsbiloglib.log.GSLogUtil;
import com.gstudentlib.SDKConfig;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;

/**
 * 作者：created by 逢二进一 on 2019/9/12 14:01
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
public abstract class STBaseApplication extends GSBaseApplication implements Interceptor.ResponseInterceptor
        , Interceptor.RequestInterceptor
        , ICallbackErrorListener
        , SDKConfig.RefreshTokenListener {

    /**
     * 响应拦截器
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject response(JSONObject jsonObject) {
        JSONObject newObject = new JSONObject();
        try {
            if(jsonObject != null) {
                int status = jsonObject.optInt("status", 0);
                String errorCode = jsonObject.optString("errorCode" , "");
                String message = jsonObject.optString("message" , "");
                if(TextUtils.isEmpty(message)) {
                    message = jsonObject.optString("errorMessage" , "");
                }
                String body = jsonObject.optString("body" , "");
                if(TextUtils.isEmpty(body)) {
                    body = jsonObject.optString("data" , "");
                }
                try {
                    newObject.put("status" , status);
                    newObject.put("errorCode" , errorCode);
                    newObject.put("message" , message);
                    newObject.put("body" , body);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(status == 0) {
                    PassportAPI.Companion.getInstance().refreshTokenCallback(errorCode , message);
                    if(PassportAPI.CODE_1003.equals(errorCode)
                            || PassportAPI.CODE_1006.equals(errorCode)
                            || PassportAPI.CODE_1007.equals(errorCode)
                            || PassportAPI.CODE_1009.equals(errorCode)
                            || PassportAPI.CODE_1013.equals(errorCode)
                            || PassportAPI.CODE_1014.equals(errorCode)) {
                        return null;
                    }
                }
        }else {
            LogUtil.d("interceptor====null");
        }
            LogUtil.d("interceptor" + GSRequest.getConverterFactory().ObjectToStringConverter().convert(newObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newObject;
    }

    /**
     * 请求拦截器
     * @param request
     * @return
     */
    @Override
    public Map<String, String> request(Map<String, String> request) {
        return request;
    }

    /**
     * 请求拦截器
     * @param request
     * @return
     */
    @Override
    public JSONObject request(@Nullable JSONObject request) {
        return request;
    }

    /**
     * SocketTimeoutException
     * @param throwable
     * @param url
     * @param errorCode
     * @param message
     */
    @Override
    public void onError(@org.jetbrains.annotations.Nullable Throwable throwable,
                        @org.jetbrains.annotations.Nullable String url,
                        @org.jetbrains.annotations.Nullable String errorCode,
                        @org.jetbrains.annotations.Nullable String message) {
        LogUtil.i( " url: " + url + " errorCode: " + errorCode + " message: " + message);
        if(throwable != null) {
            if(throwable instanceof SocketTimeoutException) {
                GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_TIME_OUT, url, errorCode, message);
            }else {
                GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_FAILED, url, errorCode, message);
            }
        }else {
            GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, url, errorCode, message);
        }
    }

    @Override
    public void onPassportRefresh(@Nullable String ptoken, @Nullable String refreshToken) {
    }

    @Override
    public void onGatewayRefresh() {
    }

    public void deleteAlias() { }
}
