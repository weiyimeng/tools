package com.gaosiedu.live.sdk.android.base;

import com.gaosiedu.Constant;
import com.google.gson.Gson;
import com.gaosiedu.live.sdk.android.bean.LiveSdkDeviceInfo;
import com.lzy.okgo.model.HttpHeaders;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class LiveSdkHelper {
    public static final int DEFAULT = -2;
    
    public static String makeFullUrl(String path) {
        return Constant.BASEURL_LIVE + LiveSdkConsts.MODEL_PATH + "/" + path;
    }
    
    /**
     * 构造请求的header
     *
     * @return
     */
    public static HttpHeaders makeHeaders(String path) {
        Map<String, String> signparams = new HashMap<>();
        long currentTime = System.currentTimeMillis();
        String random = getRandom();
        //生成sign的参数
        signparams.put("appId", LiveSdkConsts.APP_ID);
        signparams.put("random", random);
        signparams.put("stamp", String.valueOf(currentTime));
        signparams.put("uri", path);
        StringBuffer sortedSignParam = getSortedParam(signparams);
        //只对加密的参数添加key做加密
        StringBuffer keyString = new StringBuffer(sortedSignParam);
        keyString.append("appKey=");
        keyString.append(LiveSdkConsts.APP_KEY);
        String sign = MD5_LOWERCASE(keyString.toString());
        //添加header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put("appid", LiveSdkConsts.APP_ID);
        httpHeaders.put("random", random);
        httpHeaders.put("stamp", String.valueOf(currentTime));
        httpHeaders.put("sign", sign);
        httpHeaders.put("Authorization", getAuthorization());
        httpHeaders.put("device", getDeviceInfo());
        return httpHeaders;
    }
    
    /**
     * 从设别上获取token
     *
     * @return
     */
    private static String getAuthorization() {
        
        return "Authorization";
    }
    
    /**
     * {"deviceName":"设备名称"，"deviceVersion":}
     *
     * @return
     */
    private static String getDeviceInfo() {
        LiveSdkDeviceInfo deviceInfo = new LiveSdkDeviceInfo();
        deviceInfo.setDeviceName("xiaomi");
        deviceInfo.setDeviceSystemName("android");
        return new Gson().toJson(deviceInfo);
    }
    
    private static String getRandom() {
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
    
    /**
     * 通过字符串获得md5加密数据
     *
     * @param s
     * @return 其中的字符串是小写
     */
    private final static String MD5_LOWERCASE(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    
    
}
