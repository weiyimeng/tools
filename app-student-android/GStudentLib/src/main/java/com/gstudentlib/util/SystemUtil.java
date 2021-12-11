package com.gstudentlib.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.github.mzule.activityrouter.router.Routers;
import com.gsbaselib.InitBaseLib;
import com.gsbaselib.base.GSBaseApplication;
import com.gsbaselib.base.log.LogUtil;
import com.gsbaselib.utils.LOGGER;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * 作者：created by 逢二进一 on 2019/9/11 18:41
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
public class SystemUtil {

    /**
     * 打电话方法
     *
     * @param activity 当前的activity
     * @param phoneNum 电话号码
     */
    public static void callPhone(Activity activity, String phoneNum) {
        if (!isTelephonyEnabled()) {
            LogUtil.e("can not call phone");
            return;
        }

        if (activity == null || TextUtils.isEmpty(phoneNum)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            throw new ActivityNotFoundException();
        }
    }

    private static boolean isTelephonyEnabled() {
        boolean flag = false;
        TelephonyManager tm = (TelephonyManager) GSBaseApplication.getApplication().getSystemService(TELEPHONY_SERVICE);
        if (tm != null) {
            flag = tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT
                    || tm.getSimState() == TelephonyManager.SIM_STATE_READY
                    || tm.getSimState() == TelephonyManager.SIM_STATE_UNKNOWN;
        }
        return flag;
    }

    /**
     * 跳转到Web页面
     *
     * @param activity  当前Activity
     * @param url       跳转的URL
     * @param paramsStr 页面参数
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
            weexUrl = "axx://weexWebView?url=%s";
            weexUrl = String.format(weexUrl , url);
        }
        Routers.openForResult(activity, weexUrl, 149);
    }

    /**
     * 具有回调的gotoWebPage
     *
     * @param activity
     * @param url
     * @param paramsStr
     * @param requestCode
     */
    public static void gotoWebPage(Activity activity, @NonNull String url, String paramsStr, int requestCode) {
        if (activity == null || TextUtils.isEmpty(url)) {
            return;
        }
        String weexUrl = "";
        if (paramsStr != null) {
            weexUrl = "axx://weexWebView?url=%s&data=%s";
            weexUrl = String.format(weexUrl , url , Uri.encode(paramsStr));
        }else {
            weexUrl = "axx://weexWebView?url=%s";
            weexUrl = String.format(weexUrl , url);
        }
        Routers.openForResult(activity, weexUrl, 149);
    }

    /**
     * 跳转到Web页面
     *
     * @param activity    当前Activity
     * @param url         跳转的URL
     * @param params      页面参数
     * @param requestCode 页面跳转参数
     */
    public static void gotoWebPage(Activity activity, String url, @NonNull Map<String, Object> params, int requestCode) {
        if (activity == null || TextUtils.isEmpty(url)) {
            return;
        }
        String weexUrl = "";
        if (params != null && params.size() > 0) {
            weexUrl = "axx://weexWebView?url=%s&data=%s";
            weexUrl = String.format(weexUrl , url , Uri.encode(JSON.toJSONString(params)));
        }else {
            weexUrl = "axx://weexWebView?url=%s";
            weexUrl = String.format(weexUrl , url);
        }
        Routers.openForResult(activity, weexUrl, 149);
    }

    /**
     * 生成跳转默认参数
     * @param params
     * @param suffix
     * @return
     */
    public static String generateDefautJsonStr(JSONObject params, String suffix) {
        return generateJsonStr(params , InitBaseLib.getInstance().getConfigManager().getH5ServerUrl() + suffix , 1 + "");
    }

    /**
     * 生成跳转默认参数
     * @param params
     * @param url
     * @param type
     * @return
     */
    public static String generateJsonStr(JSONObject params, String url , String type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("url", url);
            jsonObject.put("animated", "true");
            jsonObject.put("type", type);
            if(null != params) {
                jsonObject.put("params", params);
            }
        } catch (JSONException e) {
            LOGGER.log("context" , e);
        }
        return jsonObject.toString();
    }

    /**
     * 保存文件
     *
     * @param path 路径
     * @param data 数据
     */
    public static void writeToFile(String path, String data) {
        GSBaseApplication.getApplication().deleteFile(path);
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = GSBaseApplication.getApplication().openFileOutput(path, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data);
        } catch (IOException e) {
            LOGGER.log("context" , e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                LOGGER.log("context" , e);
            }
        }

    }

    /**
     * 从本地读取文件
     *
     * @param path 路径
     * @return
     */
    public static String readFromFile(String path) {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = GSBaseApplication.getApplication().openFileInput(path);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            LogUtil.e(content.toString());
        } catch (IOException e) {
            LOGGER.log("context" , e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.log("context" , e);
                }
            }
        }
        return content.toString();
    }

    /**
     * 判断系统中是否存在相应activity
     * @param context Context
     * @param clazz Activity类名
     * @return boolean
     */
    public static boolean isLaunchedActivity(Context context, Class<?> clazz) {
        try {
            Intent intent = new Intent(context, clazz);
            ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
            boolean flag = false;
            if (cmpName != null) { // 说明系统中存在这个activity
                LogUtil.e(cmpName.toString());
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
                for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                    if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                        flag = true;
                        break;
                    }
                }
            }
            return flag;
        } catch (Exception e) {
            LOGGER.log(e);
            return false;
        }
    }

}
