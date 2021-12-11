package com.gstudentlib.util;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.mzule.activityrouter.router.Routers;
import com.gsbaselib.base.log.LogUtil;
import com.gsbaselib.utils.ActivityCollector;
import com.gstudentlib.base.STBaseConstants;
import com.gstudentlib.bean.StudentInfo;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.RequestCallback;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFUserInfo;
import java.util.Calendar;

/**
 * 作者：created by 逢二进一 on 2019/9/11 11:25
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
public class SchemeDispatcher {

    public final static String AXX = "axx";
    public final static String SCHEME = AXX + "://";
    public final static String H5_SCHEME = SCHEME + "h5";
    public final static String HTTP = "http";
    private static final int MIN_CLICK_DELAY_TIME = 1000;//用户两次点击时间间隔
    private static long lastClickTime = 0;//上次点击时间
    private static volatile long currentTime;

    /**
     * 页面跳转方法
     * 为了兼容之前的方法，
     * @param currentActivity 当前页面
     * @param url             页面的URL
     */
    public static void jumpPage(Activity currentActivity, String url) {
        jumpPage(currentActivity, url, null, -1);
    }

    public static void jumpPage(Activity currentActivity, String url, String pageParam, int requestCode) {
        if (currentActivity == null || TextUtils.isEmpty(url)) {
            return;
        }

        if (url.startsWith(H5_SCHEME) || url.startsWith(HTTP)) {
            gotoWebPage(currentActivity, url, pageParam);
        } else {
            if(url.contains("axx://login")) {
                //登录页面比较特殊，需要清空栈
                Routers.open(currentActivity, url);
                ActivityCollector.getInstance().finishAllActivity();
            }else {
                //避免用户进行快速点击
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime <= MIN_CLICK_DELAY_TIME) {
                    return;
                }
                lastClickTime = currentTime;
                Routers.openForResult(currentActivity, url, requestCode);
            }
        }
    }

    /**
     * 跳转到Web页面
     * @param activity    当前Activity
     * @param url         跳转的URL
     * @param paramsStr   页面参数，JSON格式，可以使用Map类型转化
     */
    public static void gotoWebPage(Activity activity, @NonNull String url, String paramsStr) {
        if (activity == null || TextUtils.isEmpty(url)) {
            return;
        }
        String weexUrl = "";
        if (paramsStr != null) {
            weexUrl = "axx://weexWebView?url=%s&data=%s";
            weexUrl = String.format(weexUrl , url , Uri.encode(paramsStr));
        }else {
            weexUrl = "axx://weexWebView?url=%s&title=%s";
            weexUrl = String.format(weexUrl , url);
        }
        Routers.openForResult(activity, weexUrl, 149);
    }

    /**
     * 跳转到横屏Web页面
     *
     * @param activity  当前Activity
     * @param url       跳转的URL
     * @param paramsStr 页面参数，JSON格式，可以使用Map类型转化
     */
    public static void gotoLandscapeWebPage(Activity activity, @NonNull String url, String paramsStr) {
        if (activity == null || TextUtils.isEmpty(url)) {
            return;
        }
        String weexUrl = "";
        if (paramsStr != null) {
            weexUrl = "axx://landscapeWeexWebView?url=%s&data=%s";
            weexUrl = String.format(weexUrl , url , Uri.encode(paramsStr));
        }else {
            weexUrl = "axx://landscapeWeexWebView?url=%s";
            weexUrl = String.format(weexUrl , url);
        }
        Routers.openForResult(activity, weexUrl, 150);
    }

    /**
     * 跳转到网络Web页面
     * @param activity    当前Activity
     * @param url         跳转的URL
     * @param paramsStr   页面参数，JSON格式，可以使用Map类型转化
     */
    public static void gotoHttpWebPage(Activity activity, @NonNull String url , @NonNull String title , String paramsStr) {
        if (activity == null || TextUtils.isEmpty(url)) {
            return;
        }
        String weexUrl = "";
        if (paramsStr != null) {
            weexUrl = "axx://weexWebView?url=%s&title=%s&data=%s";
            weexUrl = String.format(weexUrl , url , title , Uri.encode(paramsStr));
        }else {
            weexUrl = "axx://weexWebView?url=%s&title=%s";
            weexUrl = String.format(weexUrl , url , title);
        }
        Routers.openForResult(activity, weexUrl, 149);
    }

    /**
     * 跳转到Web页面
     * @param activity    当前Activity
     * @param url         跳转的URL
     * @param paramsStr   页面参数
     */
    public synchronized static void openH5Dialog(Activity activity, @NonNull String url, String paramsStr) {
        if (activity == null || activity.isFinishing() || TextUtils.isEmpty(url)) {
            return;
        }
        if (System.currentTimeMillis() - currentTime < 400) {
            //防止重复点击
            return;
        }
        currentTime = System.currentTimeMillis();
        String weexUrl = "";
        if (paramsStr != null) {
            weexUrl = "axx://weexDialogWebView?url=%s&data=%s";
            weexUrl = String.format(weexUrl , url , Uri.encode(paramsStr));
        }else {
            weexUrl = "axx://weexDialogWebView?url=%s";
            weexUrl = String.format(weexUrl , url);
        }
        Routers.open(activity, weexUrl);
    }

    /**
     * 跳转到七牛云客服页面
     *
     * @param activity   当前页面
     * @param jsonObject 跳转参数
     */
    public static void gotoQiniu(final Activity activity, JSONObject jsonObject) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        final String sourceUrl = "main";
        final String title = "爱学习";
        final String sourceInfo = "custom information string";
        final ConsultSource source = new ConsultSource(sourceUrl, title, sourceInfo);
        if (STBaseConstants.userInfo != null){
            YSFUserInfo userInfoA = new YSFUserInfo();
            userInfoA.userId = String.valueOf(STBaseConstants.userInfo.getId());
            userInfoA.data = userInfoData(STBaseConstants.userInfo, jsonObject).toJSONString();
            Unicorn.setUserInfo(userInfoA, new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
                @Override
                public void onFailed(int i) {
                    LogUtil.w("跳转客服失败onFailed==" + i);
                }
                @Override
                public void onException(Throwable throwable) {
                    LogUtil.w("跳转客服失败onException==" + throwable.toString());
                }
            });
            Unicorn.openServiceActivity(activity, title, source);
        }else {
            Unicorn.openServiceActivity(activity, title, source);
        }
    }

    private static JSONArray userInfoData(StudentInfo userInfo, JSONObject jsonObject) {
        JSONArray array = new JSONArray();
        int i = 1;
        //手机品牌
        array.add(userInfoDataItem("brand", Build.BRAND, false, i, "手机品牌", null));
        i ++;

        //手机型号
        array.add(userInfoDataItem("model", Build.MODEL, false, i, "手机型号", null));
        i ++;

        //手机操作系统版本号
        array.add(userInfoDataItem("system_version", Build.VERSION.RELEASE, false, i, "手机操作系统版本号", null));
        i ++;

        //APP版本号
        array.add(userInfoDataItem("app_version", STBaseConstants.deviceInfoBean.getAppVersion(), false, i, "App版本号", null));
        i ++;

        if (userInfo != null) {
            //姓名
            array.add(userInfoDataItem("real_name", userInfo.getTruthName(), false, i, "学生姓名", null)); // name
            i ++;

            //email是必填的，但是不需要显示，index < 0代表不显示
            array.add(userInfoDataItem("email", "", false, -1, null, null));

            //avatar是必填的，但是不需要显示，index < 0代表不显示，显示用户的头像
            array.add(userInfoDataItem("avatar", userInfo.getPath(), false, -1, null, null));

            //手机号
            array.add(userInfoDataItem("mobile_phone", userInfo.getPhone(), false, i, "登录手机号", null));
            i ++;

            //所属机构
            array.add(userInfoDataItem("institution_name", userInfo.getInstitutionName(), false, i, "所属机构", null));
            i ++;
        }

        if (jsonObject != null) {
            //班型名称
            array.add(userInfoDataItem("class_type_name", jsonObject.getString("classTypeName"), false, i, "班型名称", null));
            i ++;

            //讲次编号
            array.add(userInfoDataItem("lesson_num", jsonObject.getString("lessonNum"), false, i, "讲次编号", null));
            i ++;

            //讲次名称
            array.add(userInfoDataItem("lesson_name", jsonObject.getString("lessonName"), false, i, "讲次名称", null));
        }

        return array;
    }

    private static JSONObject userInfoDataItem(String key, Object value, boolean hidden, int index, String label, String href) {
        JSONObject item = new JSONObject();
        item.put("key", key);
        item.put("value", value);
        if (hidden) {
            item.put("hidden", true);
        }
        if (index >= 0) {
            item.put("index", index);
        }
        if (!TextUtils.isEmpty(label)) {
            item.put("label", label);
        }
        if (!TextUtils.isEmpty(href)) {
            item.put("href", href);
        }
        return item;
    }

}
