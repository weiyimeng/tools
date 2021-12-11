package com.haoke91.a91edu.utils.share;

import android.app.Activity;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haoke91.a91edu.entities.Constants;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;
import java.util.Set;

//微信auth    expires_in = 7200 openid = o5RTWwCUVkVBfSCesHi5N0SsWnms refresh_token = OezXcEiiBSKSxW0eoylIeLNNuB3n6-zyx6bXWmFAF7d3pM3xP_sroBHxBRWSGYdsnPhrMgamYq9p0AqjAJMm8eEnLhwczXSWVZ8sVlYoFuIqbKY-PX9a8uPA7b04RNBstU_4M866Ucav7EtJ4Hy7sQ scope = snsapi_userinfo access_token = OezXcEiiBSKSxW0eoylIeLNNuB3n6-zyx6bXWmFAF7d3pM3xP_sroBHxBRWSGYdsy5WULJAm01040f_u6VLpAojIDW3GgUMCHjnu2HXQ3GEEGp8P-Jaa69OIZpGke-A80n-6NYX4ozv5tLdoFe5hkw unionid = oR7LIt1L8FTGCL4WadFtmdOgIzAA
//微信用户信息 sex = 0 nickname = 风雨无阻Y unionid = oR7LIt1L8FTGCL4WadFtmdOgIzAA province =  openid = o5RTWwCUVkVBfSCesHi5N0SsWnms language = zh_CN headimgurl = http://wx.qlogo.cn/mmopen/64NyKmlzVPHhCiaseiaibice8pVJnZqUHMjmgLcwzH7CDyRxwRcDhn0ia7icILsjG4sff4k6HoNYF26JaXWytIEZab6tLqhP0LDGice/0 country = 中国 city =

//微博auth  uid = 2490228837 expires_in = 625929 refresh_token = 2.00PRkWiCvf7ejC6dce683093krioOE remind_in = 625929 access_token = 2.00PRkWiCvf7ejCc04bcb174fbXNALE
//微博用户信息 result = {"id":2490228837,"idstr":"2490228837","class":1,"screen_name":"世事--無常","name":"世事--無常","province":"11","city":"14","location":"北京 昌平区","description":"","url":"","profile_image_url":"http://tp2.sinaimg.cn/2490228837/50/5681625640/1","profile_url":"u/2490228837","domain":"","weihao":"","gender":"m","followers_count":27,"friends_count":35,"pagefriends_count":0,"statuses_count":44,"favourites_count":1,"created_at":"Thu Nov 24 15:22:19 +0800 2011","following":false,"allow_all_act_msg":false,"geo_enabled":true,"verified":false,"verified_type":-1,"remark":"","status":{"created_at":"Wed Feb 24 14:05:27 +0800 2016","id":3946045094789520,"mid":"3946045094789520","idstr":"3946045094789520","text":"我正在看#小米5#发布会，参与抽奖就有机会得到新品小米5！看直播，送新品小米手机。抢先预约小米5，这次没跑啦！ http://t.cn/RGKGntP","textLength":125,"source_allowclick":0,"source_type":1,"source":"<a href=\"http://app.weibo.com/t/feed/31iSj1\" rel=\"nofollow\">小米社区网站</a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/946de065jw1f1ae4opst1j20f00qogn2.jpg"}],"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/946de065jw1f1ae4opst1j20f00qogn2.jpg","bmiddle_pic":"http://ww2.sinaimg.cn/bmiddle/946de065jw1f1ae4opst1j20f00qogn2.jpg","original_pic":"http://ww2.sinaimg.cn/large/946de065jw1f1ae4opst1j20f00qogn2.jpg","geo":null,"reposts_count":0,"comments_count":0,"attitudes_count":0,"isLongText":false,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"page_type":32,"darwin_tags":[],"hot_weibo_tags":[],"text_tag_tips":[],"userType":0},"ptype":0,"allow_all_comment":true,"avatar_large":"http://tp2.sinaimg.cn/2490228837/180/5681625640/1","avatar_hd":"http://tva4.sinaimg.cn/crop.0.0.480.480.1024/946de065jw8ebem15q007j20dc0dct8n.jpg","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"online_status":0,"bi_followers_count":2,"lang":"zh-cn","star":0,"mbtype":0,"mbrank":0,"block_word":0,"block_app":0,"credit_score":80,"user_ability":0,"urank":4}

//QQ auth
//QQ 用户信息

public class UMLoginUtil {
    
    private static final String TAG = "UMLoginUtil";
    private Activity mContext;
    private UMShareAPI mShareAPI = null;
    private LoginCallBack loginCallBack;
    private String access_token = "";
    private String openId = "";
    //LoadingDialog dialog;
    
    public UMLoginUtil(Activity context) {
        this.mContext = context;
        mShareAPI = UMShareAPI.get(context);
        //  dialog = new LoadingDialog(context);
    }
    
    public void login(SHARE_MEDIA platform, LoginCallBack loginCallBack) {
        this.loginCallBack = loginCallBack;
        if (platform == SHARE_MEDIA.WEIXIN && !mShareAPI.isInstall(mContext, platform)) {
            //    ToastUtil.showToast(RT.getString(R.string.uninstallWx));
            return;
        } else if (platform == SHARE_MEDIA.SINA && !mShareAPI.isInstall(mContext, platform)) {
            ToastUtils.showShort("您未安装微博");
            return;
        }
        mShareAPI.doOauthVerify(mContext, platform, authListener);
    }
    
    
    public void deleteAuth() {
        String platform = SPUtils.getInstance().getString(Constants.THIRD_PLANT);
        if (TextUtils.isEmpty(platform)) {
            return;
        }
        mShareAPI.deleteOauth(mContext, SHARE_MEDIA.convertToEmun(platform), null);
    }
    
    UMAuthListener authListener = new UMAuthListener() {
        
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            //dialog.show();
            Logger.e("onStart===");
        }
        
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            //  dialog.dismiss();
            //            StringBuilder sb = new StringBuilder();
            try {
                Set<String> keySet = map.keySet();
                for (String key : keySet) {
                    //                sb.append(key + " = ").append(map.get(key)).append(" ");
                    if (share_media == SHARE_MEDIA.WEIXIN) {
                        if (key.equals("access_token")) {
                            access_token = map.get(key);
                        }
                        if (key.equals("openid")) {
                            openId = map.get(key);
                        }
                    } else if (share_media == SHARE_MEDIA.SINA) {
                        if (key.equals("accessToken")) {
                            access_token = map.get(key);
                        }
                        if (key.equals("uid")) {
                            openId = map.get(key);
                        }
                    } else if (share_media == SHARE_MEDIA.QQ) {
                        if (key.equals("access_token")) {
                            access_token = map.get(key);
                        }
                        if (key.equals("openid")) {
                            openId = map.get(key);
                        }
                    }
                }
                mShareAPI.getPlatformInfo(mContext, share_media, platformInfoListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            //      dialog.dismiss();
            ToastUtils.showShort("授权错误");
        }
        
        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            //       dialog.dismiss();
            ToastUtils.showShort("授权取消");
        }
    };
    
    UMAuthListener platformInfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        
        }
        
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            SPUtils.getInstance().put(Constants.THIRD_PLANT, share_media.toString());
            switch (share_media) {
                case QQ:
                    handlerQQInfo(map);
                    break;
                case SINA:
                    handlerSinaInfo(map);
                    break;
                case WEIXIN:
                    handlerWeixinInfo(map);
                    break;
            }
            //   dialog.dismiss();
        }
        
        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            throwable.printStackTrace();
        }
        
        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
        }
    };
    
    private void handlerQQInfo(Map<String, String> data) {
        if (data != null) {
            String name = "";
            String avatar = "";
            String openId = "";
            
            Set<String> keys = data.keySet();
            for (String key : keys) {
                if (key.equals("screen_name")) {
                    name = data.get(key)
                        .toString();
                }
                if (key.equals("profile_image_url")) {
                    avatar = data.get(key).toString();
                }
                if (key.equals("openid")) {
                    openId = data.get(key).toString();
                }
            }
            
            if (null != loginCallBack) {
                loginCallBack.onSuccess(openId, name, avatar, access_token);
            }
        }
    }
    
    private void handlerSinaInfo(Map<String, String> data) {
        if (data != null) {
            String name = "";
            String avatar = "";
            String result = "";
            //            Set<String> keys = data.keySet();
            //            for (String key : keys) {
            //                if (key.equals("result")) {
            //                    result = data.get(key).toString();
            //                    break;
            //                }
            //            }
            //            if (StringUtil.isEmpty(result)) {
            //                DLOG.e("未取到用户信息");
            //                return;
            //            }
            //            try {
            //                JSONObject infojson = new JSONObject(result);
            //                name = infojson.optString("screen_name");
            //                avatar = infojson.optString("avatar_hd");
            //
            //            } catch (JSONException e) {
            //                e.printStackTrace();
            //            }
            Set<String> keys = data.keySet();
            for (String key : keys) {
                if (key.equals("name")) {
                    name = data.get(key).toString();
                }
                if (key.equals("avatar_hd")) {
                    avatar = data.get(key).toString();
                }
                if (key.equals("id")) {
                    openId = data.get(key).toString();
                }
            }
            
            if (null != loginCallBack) {
                loginCallBack.onSuccess(openId, name, avatar, access_token);
            }
        }
    }
    
    private void handlerWeixinInfo(Map<String, String> data) {
        if (data != null) {
            String name = "";
            String avatar = "";
            Set<String> keys = data.keySet();
            for (String key : keys) {
                if (key.equals("screen_name")) {
                    name = data.get(key).toString();
                }
                if (key.equals("profile_image_url")) {
                    avatar = data.get(key).toString();
                }
            }
            
            if (null != loginCallBack) {
                loginCallBack.onSuccess(openId, name, avatar, access_token);
            }
        }
    }
    
    
}
