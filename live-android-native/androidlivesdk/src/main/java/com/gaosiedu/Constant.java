package com.gaosiedu;

import android.os.Build;

import gaosiedu.com.androidlivesdk.BuildConfig;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/12/5 18:56
 */
public class Constant {
    //https://scc-api.online-test.teacherv.top
    public static final String BASEURL_SCC = BuildConfig.DEBUG ? "http://192.168.5.131:10087" : "https://scc3.91haoke.com";//学习中心http://47.93.59.153:10030  /sccApi http://192.168.5.131:10087
    //https://api.online-test.teacherv.top
    public static final String BASEURL_LIVE = BuildConfig.DEBUG ? "http://192.168.5.237:10011" : "https://api3.91haoke.com";//业务接口 http://47.93.59.153:10010
    public static final String BASEURL_IM = BuildConfig.DEBUG ? "http://192.168.5.131:10010" : "https://im.91haoke.com";//IM http://47.93.59.153:10060
    public static final String BASEURL_ROOMCONFIG = BuildConfig.DEBUG ? "http://192.168.5.237:9000" : "https://lmc.91haoke.com";//lmc http://47.93.59.153:10051
    //        public static final String BASEURL_LIVE = "http://test.91haoke.com";//学科中心
    
}
