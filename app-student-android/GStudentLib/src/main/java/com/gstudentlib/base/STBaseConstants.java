package com.gstudentlib.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.gaosi.passport.PassportAPI;
import com.gsbaselib.InitBaseLib;
import com.gsbaselib.base.GSBaseApplication;
import com.gsbaselib.base.GSBaseConstants;
import com.gsbaselib.base.bean.DeviceInfoBean;
import com.gsbaselib.base.bean.WebResourceInfoBean;
import com.gsbaselib.base.log.LogUtil;
import com.gsbaselib.utils.AppInfo;
import com.gsbaselib.utils.DeviceIdUtil;
import com.gsbaselib.utils.LOGGER;
import com.gsbaselib.utils.LangUtil;
import com.gsbaselib.utils.SharedPreferenceUtil;
import com.gstudentlib.bean.StudentInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 学生端
 */
public abstract class STBaseConstants extends GSBaseConstants {

    static {
        /** 当前APP类型 1教师端,2.学生端, */
        appId = 2;
        apiVersion = "v16";
    }

    //是否是北校用户
    public static int isBeixiao = 1;
    //是否修改过密码，默认修改过密码，不提供修改密码接口
    public static int changedPasswordCode = 1;

    //umeng 推动类型
    public static String Umeng_Push_Type = "student_id";
    public static String Umeng_Push_Type_Test = "student_id_test";

    public static StudentInfo userInfo;

    //1 需要将log埋点发送一份到后台，0 不需要
    public static int logSymbol = 0;

    public static String mPageParams = "{}";

    public static boolean isKickOut = false;

    //装扮城资源文件是否更新
    public static boolean isResourceUpdate = false;
    //装扮城资源文件是否更新完成
    public volatile static boolean isResourceUpdateSuccess = true;

    //weex页面pop存放数据
    public static String ActivityPopParams = "";

    public static String suffixPopup = "publicPopup.web.js";

    //业务userId
    public static String businessUserId = "";

    //口语sdk信息
    public static String appKey = "1528699601000005";
    public static String chineseAppKey = "1566269236000024";
    public static String secertKey = "aa2f472bb9f0f697209d268228ba48be";
    public static String chineseSecertKey = "232aff808002a64c17a033cc5e6a0639";
    public static String provisionFilename = "aiengine.provision";
    public static String cloudServer = "wss://cloud.chivox.com"; //服务器地址

    public static boolean isUseIp = false;//是否用IP请求

    /**
     * 判断用户是否登录
     * 用户登录的条件是：有用户基本信息，并且Token不为空
     *
     * @return 用户是否登录
     */
    public static boolean hasLogin() {
        if (TextUtils.isEmpty(Token)) {
            Token = SharedPreferenceUtil.getStringValueFromSP("axxuserInfo", "token", "");
        }
        if (userInfo == null) {
            UserInfo = SharedPreferenceUtil.getStringValueFromSP("axxuserInfo", "data", "");
            if (!TextUtils.isEmpty(UserInfo)) {
                try {
                    userInfo = JSON.parseObject(UserInfo, StudentInfo.class);
                    userId = userInfo.getId();
                    businessUserId = userInfo.getUserId();
                } catch (Exception e) {
                    LOGGER.log("context" , e);
                }
            }
        }
        return !TextUtils.isEmpty(Token) && userInfo != null;
    }

    /**
     * 更新用户信息
     * @param userInfo
     */
    public static void updateUserInfo(StudentInfo userInfo, String token) {
        if (userInfo == null) {
            return;
        }
        ((STBaseApplication)STBaseApplication.getApplication()).deleteAlias();
        GSBaseConstants.Token = token;
        STBaseConstants.userInfo = userInfo;
        STBaseConstants.userId = userInfo.getId();
        STBaseConstants.businessUserId = userInfo.getUserId();
        STBaseConstants.UserInfo = JSON.toJSONString(userInfo);
        SharedPreferenceUtil.setStringDataIntoSP("axxuserInfo", "token", Token);
        SharedPreferenceUtil.setStringDataIntoSP("axxuserInfo", "data", UserInfo);
    }

    /**
     * 获取用户姓名
     * @return
     */
    public static String getUserName() {
        String name = "爱学习";
        if(userInfo == null) {
            name = "爱学习";
        }
        if(!TextUtils.isEmpty(userInfo.getTruthName())) {
            name = userInfo.getTruthName();
        }else {
            if(!TextUtils.isEmpty(userInfo.getParentTel1())) {
                name = userInfo.getParentTel1();
            }
        }
        return name;
    }

    /**
     * 获取安全的用户姓名
     * @return
     */
    public static String getSafeName() {
        String name = getUserName();
        //对大于等于11位姓名进行校验
        if(name.length() >= 11) {
            int start = 0;
            int step = 11;
            int end = 11;
            String temp = "";
            for(int i = 0 ; i < (name.length() - step) ; i ++) {
                start = i;
                end = start + step;
                temp = name.substring(start , end);//截取连续11位字符串
                LogUtil.d("temp" + temp);
                //开始进行纯数字验证
                String regEx = "[0-9]{1,}";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(temp);
                if (m.matches()) {
                    temp = temp.substring(0, 3) + "****" + temp.substring(7, 11);
                    return name.substring(0 , start) + temp + name.substring(end);
                }
            }
        }
//        if(name.length() == 11) {
//            String regEx = "[0-9]{1,}";
//            Pattern p = Pattern.compile(regEx);
//            Matcher m = p.matcher(name);
//            if (m.matches()) {
//                name = name.substring(0, 3) + "****" + name.substring(7, 11);
//            }
//        }
        return name;
    }

    /**
     * 退出登录的时候调用，清空登录数据
     */
    public static void clearUserInfo() {
        PassportAPI.Companion.getInstance().kickOut();
        ((STBaseApplication)STBaseApplication.getApplication()).deleteAlias();
        userInfo = null;
        userId = "";
        businessUserId = "";
        Token = "";
        UserInfo = "";
        SharedPreferenceUtil.setStringDataIntoSP("axxuserInfo", "data", "");
        SharedPreferenceUtil.setStringDataIntoSP("axxuserInfo", "token", "");
    }

    @Override
    public void init(GSBaseApplication context) {
        try {
            deviceInfoBean = new DeviceInfoBean();
            AppInfo.getLocation(context);
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            deviceInfoBean.setAppVersion(packInfo.versionName);
            deviceInfoBean.setAppVersionCode(packInfo.versionCode);
            deviceInfoBean.setDeviceId(DeviceIdUtil.getDeviceId(context));
            deviceInfoBean.setDeviceType(android.os.Build.MODEL);
            deviceInfoBean.setSystemType("android");
            deviceInfoBean.setSystemVersion(Build.VERSION.SDK_INT);
            deviceInfoBean.setFontScale(context.getResources().getConfiguration().fontScale);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            deviceInfoBean.setScreenWidth(size.x);
            deviceInfoBean.setScreenHeight(size.y);
            DisplayMetrics metric = new DisplayMetrics();
            display.getMetrics(metric);
            deviceInfoBean.setDensity(metric.density);
            deviceInfoBean.setScaledDensity(GSBaseApplication.getApplication().getResources()
                    .getDisplayMetrics().scaledDensity);
            /**
             * 读取更新的h5 rn资源
             * 后面拼上STBaseConstants.deviceInfoBean.getAppVersion()是为了防止用户升级过h5或者rn然后再次升级原生时无法将原生携带的h5资源进行解压
             * 加上版本的情况下，升级原生以后此时本地化的h5 rn信息将不再存在
             * 测试环境下希望每次都会解压，不然的话每次打包都需要卸载重装
             */
            if(InitBaseLib.getInstance().getConfigManager().isRelease()
                    || InitBaseLib.getInstance().getConfigManager().isReleaseTest()) {
                h5InfoBean = JSON.parseObject(SharedPreferenceUtil.getStringValueFromSP("userInfo", "h5Info_" + STBaseConstants.deviceInfoBean.getAppVersion() , ""), WebResourceInfoBean.class);
                rnInfoBean = JSON.parseObject(SharedPreferenceUtil.getStringValueFromSP("userInfo", "rnInfo_" + STBaseConstants.deviceInfoBean.getAppVersion() , ""), WebResourceInfoBean.class);
                LogUtil.i("开始检查-->sp存储的h5:" + SharedPreferenceUtil.getStringValueFromSP("userInfo", "h5Info_" + STBaseConstants.deviceInfoBean.getAppVersion() , ""));
                LogUtil.i("开始检查-->sp存储的rn:" + SharedPreferenceUtil.getStringValueFromSP("userInfo", "rnInfo_" + STBaseConstants.deviceInfoBean.getAppVersion() , ""));
            }
            LogUtil.i("STBaseConstants：init" + JSON.toJSONString(deviceInfoBean));
        }catch (Exception e){}
    }
}
