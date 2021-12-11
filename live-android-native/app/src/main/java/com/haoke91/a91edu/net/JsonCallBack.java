package com.haoke91.a91edu.net;

import android.content.Intent;
import android.os.Build;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.haoke91.a91edu.BuildConfig;
import com.haoke91.a91edu.MainActivity;
import com.haoke91.a91edu.ui.login.LoginActivity;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.qq.tencent.JsonUtil;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.sina.message.BaseResponse;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import okhttp3.ResponseBody;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/5/22 上午9:51
 * 修改人：weiyimeng
 * 修改时间：2018/5/22 上午9:51
 * 修改备注：
 */
public abstract class JsonCallBack<T extends ResponseResult> extends AbsCallback<T> {
    private static final String deviceTypeKey = "Device-Type";//标识key
    private static final String deviceInfoKey = "Device-Info";//标识key
    private static final String device_type = "android";
    private static final JsonObject device_info = new JsonObject();
    protected Class<T> clazz;
    
    public JsonCallBack(Class<T> clazz) {
        this.clazz = clazz;
        device_info.addProperty("deviceName", Build.BRAND);
        device_info.addProperty("deviceOsVersion", Build.VERSION.SDK_INT + File.separator + Build.VERSION.RELEASE);
        device_info.addProperty("appVersion", BuildConfig.VERSION_NAME);
        device_info.addProperty("others", DeviceUtils.getModel());
        device_info.addProperty("channel", AnalyticsConfig.getChannel(Utils.getApp()));
    }
    
    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        HttpHeaders headers = request.getHeaders();
        headers.put("Authorization", UserManager.getInstance().getToken());
        headers.put(deviceTypeKey, device_type);
        headers.put(deviceInfoKey, device_info.toString());
    }
    
    private static StringBuffer getSortedParam(Map<String, List<String>> params) {
        String[] keyArray = new String[params.size()];
        keyArray = params.keySet().toArray(keyArray);
        Arrays.sort(keyArray);
        
        StringBuffer sortedParam = new StringBuffer();
        for (String key : keyArray) {
            sortedParam.append(key);
            sortedParam.append("=");
            sortedParam.append(params.get(key).get(0));
            sortedParam.append("&");
        }
        return sortedParam;
    }
    
    /**
     * 工具-生成随机数
     *
     * @return
     */
    private String getRandom() {
        return UUID.randomUUID().toString().replaceAll("_", "");
    }
    
    @Override
    public void onFinish() {
        super.onFinish();
    }
    
    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        //        com.haoke91.a91edu.utils.Utils.dismissLoading();
        //        int code = response.code();
        ResponseBody body = response.body();
        if (ObjectUtils.isEmpty(body)) {
            return null;
        }
        T data;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        JsonReader jsonReader = new JsonReader(body.charStream());
        data = gson.fromJson(jsonReader, clazz);
        
        // data = sJsonUtil.readValue(body.charStream(), clazz);
        if (data == null || ResponseResult.ResultStatus.EXCEPTION.equalsIgnoreCase(data.code) || ResponseResult.ResultStatus.UNAUTHC.equalsIgnoreCase(data.code)) {
            throw new IllegalAccessException("数据异常");
        }
        if (ResponseResult.ResultStatus.EXPIRE.equalsIgnoreCase(data.code)) {
            if (!(ActivityUtils.getTopActivity() instanceof MainActivity)) {
                ActivityUtils.getTopActivity().finish();
            }
            goLogin();
            throw new IllegalAccessException("token过期");
            
        }
        //        if (!ResponseResult.ResultStatus.OK.equalsIgnoreCase(data.code) && !ResponseResult.ResultStatus.EMPTY.equalsIgnoreCase(data.code)) {//处理错误状态
        //            //    new RuntimeException()
        //            throw new IllegalAccessException(data.getMessage());
        //        }
        
        //}
        //        if (ResponseResult.ResultStatus.FAIL.equalsIgnoreCase(data.code)) {
        //            ToastUtils.showShort(data.getMessage());
        //        }
        return data;
    }
    
    @Override
    public void onError(Response<T> response) {
        //        com.haoke91.a91edu.utils.Utils.dismissLoading();
        //        super.onError(response);
        //        ToastUtils.showShort("连接异常");
        Logger.e(response.getException().toString());
        JsonCallBack.this.onError();
        MobclickAgent.reportError(Utils.getApp(), response.getException());
    }
    
    
    private void goLogin() {
        UserManager.getInstance().clearUserInfo();
        Intent intent = new Intent(Utils.getApp(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getApp().startActivity(intent);
    }
    
    
    @Override
    public void onCacheSuccess(Response<T> response) {
        onSuccess(response);
    }
    
    @Override
    public void onSuccess(Response<T> response) {
        onResponse(response.body(), response.isFromCache());
        
    }
    
    public abstract void onResponse(T date, boolean isFromCache);
    
    public void onError() {
    }
    
    
}
