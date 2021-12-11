package com.haoke91.a91edu.net;

import android.os.Build;
import android.util.Log;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gaosiedu.Constant;
import com.gaosiedu.live.sdk.android.base.LiveSdkHelper;
import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;
import com.gaosiedu.live.sdk.android.bean.LiveSdkDeviceInfo;
import com.gaosiedu.scc.sdk.android.base.LiveSccSdkHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.haoke91.a91edu.BuildConfig;
import com.haoke91.a91edu.utils.Md5Utils;
import com.haoke91.im.mqtt.LogU;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Progress;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.Response;

public class Api {
    private final String appId = "91hakeapi";
    private final String appKey = "4161C4F2BC3A5AE54E9E814C4A7F3F3D";
    public static final String baseImgUrl = "https://img.91haoke.com/upload/";
    //    public final String deviceTypeKey = "Device-Type";//标识key
    //    public final String deviceInfokey = "Device-Info";//标识key
    //    public final String device_type = "android";
    //    public final JsonObject device_info = new JsonObject();
    
    public static Api getInstance() {
        return SingletonHolder.INSTANCE;
    }
    
    private static class SingletonHolder {
        private static final Api INSTANCE = new Api();
    }
    
    //    LinkedHashMap<String, String> params;
    //    private HttpHeaders httpHeaders;
    //    private String passKey;
    
    private Api() {
        //        httpHeaders = new HttpHeaders();
        //        LinkedHashMap<String, String> signparams = new LinkedHashMap<>();
        //        //        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        //        long currentTime = System.currentTimeMillis();
        //        //  String random = getRandom();
        //        signparams.put("appId", appId);
        //
        //        signparams.put("random", getRandom());
        //
        //
        //        signparams.put("stamp", String.valueOf(currentTime));
        //
        //
        //        //排序加密的参数
        //        //        params.putAll(signparams);
        //        StringBuffer sortedSignParam = getSortedParam(signparams);
        //        //只对加密的参数添加key做加密
        //        StringBuffer keyString = new StringBuffer(sortedSignParam);
        //        keyString.append("appKey=");
        //        keyString.append(appKey);
        //        String sign = Md5Utils.MD5(keyString.toString());
        //        passKey = new StringBuilder().append("?appId=").append(appId).append("&random=").append(getRandom()).append("&stamp=").append(currentTime)
        //            .append("&sign=").append(sign)
        //            // .append("&device=").append(getDeviceInfo())
        //            .toString();
        //        device_info.addProperty("deviceName", Build.BRAND);
        //        device_info.addProperty("deviceOsVersion", Build.VERSION.SDK_INT + " " + Build.VERSION.RELEASE);
        //        device_info.addProperty("appVersion", BuildConfig.VERSION_NAME);
        //        device_info.addProperty("others", "");
        
    }
    
    public String getSign(String uri) {
        LinkedHashMap<String, String> signparams = new LinkedHashMap<>();
        //        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        long currentTime = System.currentTimeMillis();
        //  String random = getRandom();
        signparams.put("appId", appId);
        String random = getRandom();
        signparams.put("random", random);
        
        
        signparams.put("stamp", String.valueOf(currentTime));
        
        //appId=91hakeapi&random=900a8548-d20d-44cb-a5cc-728945a70ef7&stamp=1539166656600&uri=/api/auth/login&appKey=4161C4F2BC3A5AE54E9E814C4A7F3F3D
        //排序加密的参数
        //        params.putAll(signparams);
        StringBuffer sortedSignParam = getSortedParam(signparams);
        //只对加密的参数添加key做加密
        StringBuffer keyString = new StringBuffer(sortedSignParam);
        keyString.append("uri=");
        keyString.append(uri);
        keyString.append("&appKey=");
        keyString.append(appKey);
        //        Logger.e(keyString.toString());
        String sign = Md5Utils.MD5(keyString.toString());
        
        
        return new StringBuilder().append("?appId=").append(appId).append("&random=").append(random).append("&stamp=").append(currentTime)
            .append("&sign=").append(sign)
            // .append("&device=").append(getDeviceInfo())
            .toString();
    }
    
    public String getSign(String uri, String random, String stamp) {
        LinkedHashMap<String, String> signparams = new LinkedHashMap<>();
        //        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        //        long currentTime = System.currentTimeMillis();
        //  String random = getRandom();
        signparams.put("appId", appId);
        signparams.put("random", random);
        signparams.put("stamp", stamp);
        
        StringBuffer sortedSignParam = getSortedParam(signparams);
        //只对加密的参数添加key做加密
        StringBuffer keyString = new StringBuffer(sortedSignParam);
        keyString.append("uri=");
        keyString.append(uri);
        keyString.append("&appKey=");
        keyString.append(appKey);
        Log.e("===", "url===" + keyString);
        String sign = Md5Utils.MD5(keyString.toString());
        
        
        return new StringBuilder().append("?appId=").append(appId).append("&random=").append(random).append("&stamp=").append(stamp)
            .append("&sign=").append(sign)
            // .append("&device=").append(getDeviceInfo())
            .toString();
    }
    
    private static String getDeviceInfo() {
        try {
            LiveSdkDeviceInfo deviceInfo = new LiveSdkDeviceInfo();
            deviceInfo.setDeviceName(DeviceUtils.getModel());
            deviceInfo.setSdkVersion(DeviceUtils.getSDKVersionName());
            deviceInfo.setAppVersion(BuildConfig.VERSION_NAME);
            deviceInfo.setOtherInfo("net===" + NetworkUtils.getNetworkOperatorName() + "==" + NetworkUtils.getNetworkType());
            deviceInfo.setDeviceSystemName("android");
            return new Gson().toJson(deviceInfo);
        } catch (Exception e) {
            MobclickAgent.reportError(com.blankj.utilcode.util.Utils.getApp(), e);
        }
        return "";
    }
    
    /**
     * 工具-生成随机数
     *
     * @return
     */
    private String getRandom() {
        return UUID.randomUUID().toString().replaceAll("_", "");
    }
    
    private static StringBuffer getSortedParam(Map<String, String> params) {
        String[] keyArray = new String[params.size()];
        keyArray = params.keySet().toArray(keyArray);
        Arrays.sort(keyArray);
        
        StringBuffer sortedParam = new StringBuffer();
        for (String key : keyArray) {
            sortedParam.append(key);
            sortedParam.append("=");
            sortedParam.append(params.get(key));
            sortedParam.append("&");
        }
        return sortedParam;
    }
    
    public <T extends ResponseResult> void get(String url, HashMap<String, String> map, Class<T> clazz, final ResponseCallback<T> callback, Object tag) {
        get(url, map, clazz, callback, CacheMode.NO_CACHE, tag);
    }
    
    /**
     * get请求
     *
     * @param map
     * @param clazz
     * @param callback
     * @param cacheMode
     * @param tag
     * @param <T>
     */
    public <T extends ResponseResult> void get(String url, HashMap<String, String> map, Class<T> clazz, final ResponseCallback<T> callback, CacheMode cacheMode, Object tag) {
        OkGo.<T>get(url)
            .params(map)
            .tag(tag)
            .cacheMode(cacheMode)
            .execute(new JsonCallBack<T>(clazz) {
                @Override
                public void onResponse(T date, boolean isFromCache) {
                    callback.onResponse(date, isFromCache);
                }
            });
    }
    
    /**
     * post 请求 Content-Type: application/json
     *
     * @param clazz
     * @param callback
     * @param tag
     * @param <T>
     */
    
    //    public <T extends ResponseResult> void post(String url,HashMap map, Class<T> clazz, final ResponseCallback<T> callback, Object tag) {
    //
    //    }
    public <T extends ResponseResult> void post(LiveSdkBaseRequest request, Class<T> clazz, final ResponseCallback<T> callback, Object tag) {
        post(request, clazz, callback, CacheMode.NO_CACHE, 0, tag);
    }
    
    public <T extends ResponseResult> void post(LiveSdkBaseRequest request, Class<T> clazz, final ResponseCallback<T> callback, CacheMode cacheMode, long cacheTime, Object tag) {
        //        String fullUrl = LiveSdkHelper.makeFullUrl(request.getPath());
        OkGo.<T>post(LiveSdkHelper.makeFullUrl(request.getPath() + getSign("/api/" + request.getPath())))
            .tag(tag)
            //            .params()
            //            .headers(httpHeaders)
            .upJson(new Gson().toJson(request))
            //            .cacheKey(request.getPath())
            .cacheKey(request.getPath() + tag)
            //            .isSpliceUrl(true)
            .cacheMode(cacheMode)
            .cacheTime(cacheTime)
            .execute(new JsonCallBack<T>(clazz) {
                @Override
                public void onResponse(T date, boolean isFromCache) {
                    if (ResponseResult.ResultStatus.OK.equalsIgnoreCase(date.code)) {
                        callback.onResponse(date, isFromCache);
                    } else if (ResponseResult.ResultStatus.EMPTY.equalsIgnoreCase(date.code)) {
                        callback.onEmpty(date, isFromCache);
                    } else if (ResponseResult.ResultStatus.FAIL.equalsIgnoreCase(date.code)) {
                        ToastUtils.showShort(date.msg);
                        callback.onFail(date, isFromCache);
                    }
                }
                
                @Override
                public void onError() {
                    callback.onError();
                    
                }
            });
    }
    
    public <T extends ResponseResult> void postCourse(LiveSdkBaseRequest request, Class<T> clazz, final ResponseCallback<T> callback, Object tag) {
        //        String fullUrl = LiveSdkHelper.makeFullUrl(request.getPath());
        //        OkGo.<T>post(LiveSdkHelper.makeFullUrl(request.getPath() + passKey))
        OkGo.<T>post(LiveSdkHelper.makeFullUrl(request.getPath() + getSign("/api/" + request.getPath())))
            
            //        OkGo.<T>post(LiveSdkHelper.make"FullUrl(request.getPath() + passKey) + getSign("/api/" + request.getPath()))
            .tag(tag)
            //            .params()
            .upJson(new Gson().toJson(request))
            //            .cacheKey(request.getPath())
            .cacheKey(request.getPath() + tag)
            //            .isSpliceUrl(true)
            .cacheMode(CacheMode.NO_CACHE)
            //            .cacheTime(cachetime)
            .execute(new JsonCallBack<T>(clazz) {
                @Override
                public void onResponse(T date, boolean isFromCache) {
                    if (ResponseResult.ResultStatus.OK.equalsIgnoreCase(date.code) || ResponseResult.ResultStatus.FAIL.equalsIgnoreCase(date.code)) {
                        callback.onResponse(date, isFromCache);
                    } else if (ResponseResult.ResultStatus.EMPTY.equalsIgnoreCase(date.code)) {
                        callback.onEmpty(date, isFromCache);
                    }
                }
                
                @Override
                public void onError() {
                    callback.onError();
                    
                }
            });
    }
    
    //同步请求
    public <T extends ResponseResult> String postSyn(LiveSdkBaseRequest request, Object tag) {
        try {
            Response execute = OkGo.<T>post(LiveSdkHelper.makeFullUrl(request.getPath()) + getSign("/api/" + request.getPath()))
                .tag(tag)
                .upJson(new Gson().toJson(request))
                .cacheKey(request.getPath())
                .cacheMode(CacheMode.NO_CACHE)
                .cacheKey(request.getPath() + tag)
                .execute();
            String string = execute.body().string();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    
    /**
     * post 请求 Content-Type: application/json
     *
     * @param clazz
     * @param callback
     * @param tag
     * @param <T>
     */
    
    //    public <T extends ResponseResult> void post(String url,HashMap map, Class<T> clazz, final ResponseCallback<T> callback, Object tag) {
    //
    //    }
    public <T extends ResponseResult> void postScc(LiveSdkBaseRequest request, Class<T> clazz, final ResponseCallback<T> callback, Object tag) {
        //        String fullUrl = LiveSdkHelper.makeFullUrl(request.getPath());
        postScc(request, clazz, callback, CacheMode.NO_CACHE, 0, tag);
    }
    
    public <T extends ResponseResult> void postScc(LiveSdkBaseRequest request, Class<T> clazz, final ResponseCallback<T> callback, CacheMode cacheMode, long cacheTime, Object tag) {
        //        String fullUrl = LiveSdkHelper.makeFullUrl(request.getPath());
        OkGo.<T>post(LiveSccSdkHelper.makeFullUrl(request.getPath()) + getSign("/sccApi/" + request.getPath()))
            .tag(tag)
            .upJson(new Gson().toJson(request))
            .cacheMode(cacheMode)
            .execute(new JsonCallBack<T>(clazz) {
                @Override
                public void onResponse(T date, boolean isFromCache) {
                    if (ResponseResult.ResultStatus.OK.equalsIgnoreCase(date.code)) {
                        callback.onResponse(date, isFromCache);
                    } else if (ResponseResult.ResultStatus.EMPTY.equalsIgnoreCase(date.code)) {
                        callback.onEmpty(date, isFromCache);
                    } else if (ResponseResult.ResultStatus.FAIL.equalsIgnoreCase(date.code)) {
                        ToastUtils.showShort(date.getMessage());
                    }
                }
    
    
                @Override
                public void onError() {
                    callback.onError();
                }
            });
    }
    
    public <T extends ResponseResult> void getLiveRoomConfig(HashMap request, Class<T> clazz, final ResponseCallback<T> callback, Object tag) {
        //        String fullUrl = LiveSdkHelper.makeFullUrl(request.getPath());
        OkGo.<T>post(Constant.BASEURL_ROOMCONFIG + "/core/initLmcRoomConfig")
            .tag(tag)
            .params(request)
            .cacheMode(CacheMode.NO_CACHE)
            .execute(new JsonCallBack<T>(clazz) {
                @Override
                public void onResponse(T date, boolean isFromCache) {
                    //                    if (ResponseResult.ResultStatus.OK.equalsIgnoreCase(date.code)) {
                    callback.onResponse(date, isFromCache);
                    //                    } else if (ResponseResult.ResultStatus.EMPTY.equalsIgnoreCase(date.code)) {
                    //                        callback.onEmpty(date, isFromCache);
                    //                    } else if (ResponseResult.ResultStatus.FAIL.equalsIgnoreCase(date.code)) {
                    //                        ToastUtils.showShort(date.getMessage());
                    //                    }
                }
                
                @Override
                public void onError() {
                    callback.onError();
                }
            });
    }
    
    /**
     * post 请求
     *
     * @param url      路径
     * @param request  参数
     * @param clz
     * @param callback
     * @param tag
     * @param <T>
     */
    public <T extends ResponseResult> void netPost(String url, HashMap request, Class<T> clz, final ResponseCallback<T> callback, Object tag) {
        OkGo.<T>post(url)
            .tag(tag)
            .params(request)
            .cacheMode(CacheMode.NO_CACHE)
            .execute(new JsonCallBack<T>(clz) {
                @Override
                public void onResponse(T date, boolean isFromCache) {
                    callback.onResponse(date, isFromCache);
                }
                
                @Override
                public void onError() {
                    callback.onError();
                }
            });
    }
    
    /**
     * post 请求 Content-Type: application/form
     *
     * @param map
     * @param clazz
     * @param callback
     * @param tag
     * @param <T>
     */
    public <T extends ResponseResult> void postForm(String url, HashMap map, Class<T> clazz, final ResponseCallback<T> callback, Object tag) {
        OkGo.<T>post(url)
            .tag(tag)
            .isSpliceUrl(true)
            .cacheMode(CacheMode.NO_CACHE)
            .params(map)
            .execute(new JsonCallBack<T>(clazz) {
                @Override
                public void onResponse(T date, boolean isFromCache) {
                    callback.onResponse(date, isFromCache);
                }
                
                @Override
                public void onError() {
                    super.onError();
                    callback.onError();
                }
            });
    }
    
    /**
     * 下载
     *
     * @param map
     * @param clazz
     * @param callback
     * @param tag
     * @param <T>
     */
    public <T extends ResponseResult> void downLoad(HashMap map, Class<T> clazz, final ResponseCallback<T> callback, Object tag) {
        OkGo.<T>get("https://api.douban.com/v2/movie/top250")
            .params(map)
            .tag(tag)
            .cacheMode(CacheMode.NO_CACHE)
            .execute(new JsonCallBack<T>(clazz) {
                @Override
                public void onResponse(T date, boolean isFromCache) {
                    callback.onResponse(date, isFromCache);
                }
                
                @Override
                public void downloadProgress(Progress progress) {
                    callback.downloadProgress(progress.fraction, progress.totalSize);
                    
                }
            });
    }
}
