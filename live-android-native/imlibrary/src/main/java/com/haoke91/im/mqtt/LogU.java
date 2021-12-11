package com.haoke91.im.mqtt;

import android.util.Log;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/6/25 18:53
 */
public class LogU {
    public static void log(String msg) {
        if (!BuildConfig.DEBUG) {
            Log.e("im===", msg);
        }
    }
}
