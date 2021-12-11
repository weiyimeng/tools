package com.haoke91.a91edu.utils.share;

/**
 * @author fanxiaofeng
 *         登录成功的回调
 * @date 2015年10月15日 20:37:27
 */
public interface LoginCallBack {

    /**
     * 失败
     */
    void onFailed(String msg);

    /**
     * 成功
     *
     * @param unionid 授权返回的唯一id
     */
    void onSuccess(String unionid, String name, String avatar, String token);
}
