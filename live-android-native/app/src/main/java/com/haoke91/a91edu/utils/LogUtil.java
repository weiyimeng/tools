package com.haoke91.a91edu.utils;

import android.util.Log;

import com.haoke91.a91edu.BuildConfig;

/**
 * 项目名称：91edu
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/5/15 14:10
 */
public class LogUtil {
    public static final String tag = "===";
    
    public static void log(String msg) {
        if (BuildConfig.LOG_DEBUG) {
            Log.e(tag, msg);
        }
    }
}
