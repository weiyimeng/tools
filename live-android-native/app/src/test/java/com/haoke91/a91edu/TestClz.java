package com.haoke91.a91edu;

import com.haoke91.a91edu.utils.rsa.Md5Utils;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2019/1/9 16:39
 */
public class TestClz {
    public static void main(String[] args) throws Exception{
        String uri = "/sccApi/user/studyReport/detail";
        String userAppId = "91hakeapi";
        String stamp = "1547017934000";
        String random = "b43453ff-6c6e-4365-8a00-e7ec88236324";
        String appKey = "4161C4F2BC3A5AE54E9E814C4A7F3F3D";
    
        String signStr = "appId=" + userAppId + "&random=" + random + "&stamp=" + stamp + "&uri=" + uri + "&appKey=" + appKey;
        String newSign = Md5Utils.MD5_LOWERCASE(signStr);
        System.out.println(signStr);
        System.out.println(newSign);
    
    }
}
