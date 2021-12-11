package com.haoke91.a91edu.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.eduhdsdk.interfaces.JoinmeetingCallBack;
import com.eduhdsdk.interfaces.MeetingNotify;
import com.eduhdsdk.message.RoomClient;
import com.gaosiedu.Constant;
import com.gaosiedu.scc.sdk.android.api.user.live.initCk.LiveSccLiveInitCkRequest;
import com.gaosiedu.scc.sdk.android.api.user.live.initCk.LiveSccLiveInitCkResponse;
import com.gaosiedu.scc.sdk.android.domain.ImServerBean;
import com.gaosiedu.scc.sdk.android.domain.LiveKnowledgeBaseBean;
import com.gaosiedu.scc.sdk.android.domain.LiveUserBaseBean;
import com.gaosiedu.scc.sdk.android.domain.LmcLiveRoomBean;
import com.gaosiedu.scc.sdk.android.domain.LmcRoomConfigResponse;
import com.gaosiedu.scc.sdk.android.domain.SccUserGoldBean;
import com.gaosiedu.scc.sdk.android.domain.UserInfoBean;
import com.gaosiedu.scc.sdk.android.domain.UserMqttConfig;
import com.google.gson.Gson;
import com.haoke91.a91edu.CacheData;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.entities.CommandMessage;
import com.haoke91.a91edu.entities.GetImCommandResponse;
import com.haoke91.a91edu.entities.GetImHistoryResponse;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.presenter.player.PlayerPresenter;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.ui.liveroom.AgoraActivity;
import com.haoke91.a91edu.ui.liveroom.LivePlayerActivity;
import com.haoke91.a91edu.ui.liveroom.TkActivity;
import com.haoke91.a91edu.ui.liveroom.VideoActivity;
import com.haoke91.a91edu.ui.liveroom.VideoPlayerActivity;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.utils.share.UMengAnalytics;
import com.haoke91.baselibrary.event.MessageItem;
import com.haoke91.baselibrary.event.RxBus;
import com.haoke91.baselibrary.model.VideoUrl;
import com.haoke91.im.mqtt.IMManager;
import com.haoke91.im.mqtt.entities.ACL;
import com.haoke91.im.mqtt.entities.Prop;
import com.haoke91.im.mqtt.entities.User;
import com.talkcloud.room.TKRoomManager;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/8/30 16:56
 */
public class Utils {
    private static AlertDialog mLoadingDialog;
    
    public static boolean isSuccess(String code) {
        return code != null && code.equalsIgnoreCase("SUCCESS");
    }
    
    public static void loading(Context context) {
        if (((Activity) context).isDestroyed()) {
            return;
        }
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = new AlertDialog.Builder(context, R.style.WrapDialog).create();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loading, null);
        mLoadingDialog.setView(view);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        if (!mLoadingDialog.isShowing() && !((Activity) context).isDestroyed()) {
            mLoadingDialog.show();
            ((LottieAnimationView) mLoadingDialog.findViewById(R.id.lottieView)).playAnimation();
            
        }
    }
    
    public static void dismissLoading() {
        try {
            
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                ((LottieAnimationView) mLoadingDialog.findViewById(R.id.lottieView)).cancelAnimation();
                mLoadingDialog.dismiss();
            }
        } catch (Exception e) {
        }
        mLoadingDialog = null;
    }
    
    /**
     * 得到今天的起始时间
     *
     * @return
     */
    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        // GregorianCalendar todayStart = new GregorianCalendar();
        //        todayStart.set(Calendar.YEAR,2018);
        //        todayStart.set(Calendar.MONTH,5);
        //        todayStart.set(Calendar.DAY_OF_MONTH,12);
        
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        // todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }
    
    /**
     * 得到今天的截止时间
     *
     * @return
     */
    public static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        // GregorianCalendar todayEnd = new GregorianCalendar();
        //        todayEnd.set(Calendar.YEAR,2018);
        //        todayEnd.set(Calendar.MONTH,5);
        //        todayEnd.set(Calendar.DAY_OF_MONTH,12);
        
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        // todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }
    
    /**
     * 获取改天的开始时间
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getStartTime(int year, int month, int day) {
        Calendar todayStart = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        // GregorianCalendar todayStart = new GregorianCalendar();
        todayStart.set(Calendar.YEAR, year);
        todayStart.set(Calendar.MONTH, month - 1);
        todayStart.set(Calendar.DAY_OF_MONTH, day);
        
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        // todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }
    
    /**
     * 获取改天的结束时间
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getEndTime(int year, int month, int day) {
        Calendar todayEnd = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        todayEnd.set(Calendar.YEAR, year);
        todayEnd.set(Calendar.MONTH, month - 1);
        todayEnd.set(Calendar.DAY_OF_MONTH, day);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        // todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }
    
    
    public static String getHolidayByNumber(int number, View view) {
        String holiday;
        switch (number) {
            case 1:
                holiday = "春";
                if (view != null) {
                    view.setBackgroundResource(R.drawable.bg_spring);
                }
                break;
            case 2:
                holiday = "暑";
                if (view != null) {
                    view.setBackgroundResource(R.drawable.bg_summer);
                }
                break;
            case 3:
                holiday = "秋";
                if (view != null) {
                    view.setBackgroundResource(R.drawable.bg_autumn);
                }
                break;
            case 4:
                holiday = "寒";
                if (view != null) {
                    view.setBackgroundResource(R.drawable.bg_winter);
                }
                break;
            default:
                holiday = "";
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            
        }
        return holiday;
    }
    
    /**
     * Date 格式转String
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }
    
    /**
     * 今年忽略年份
     *
     * @param date
     * @param noYearFormat
     * @param format
     * @return
     */
    public static String datetoStringJudge(Date date, SimpleDateFormat noYearFormat, SimpleDateFormat format) {
        if (date == null) {
            return "";
        }
        String result;
        int currentYear = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
        Calendar targetDate = Calendar.getInstance(Locale.CHINA);
        targetDate.setTime(date);
        if (currentYear == targetDate.get(Calendar.YEAR)) {
            result = noYearFormat.format(date);
        } else {
            result = format.format(date);
        }
        return result;
    }
    
    public static String dateToWeek(Date date) {
        if (date == null) {
            return "";
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        switch (instance.get(Calendar.DAY_OF_WEEK)) {
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
                return "六";
            case 1:
                return "日";
        }
        return "";
    }
    
    public static String convertLowerCaseToUpperCase(int num) {
        String[] unit = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
        String[] uName = {"十", "百", "千", "万", "亿"};
        if (num <= 10) {
            return unit[num - 1];
        }
        if (num < 100) {
            int tens = num / 10;
            String sTens = unit[tens - 1];
            if (tens == 1) {
                sTens = "";
            }
            int u = num % 10;
            String sU;
            if (u == 0) {
                sU = "";
            } else {
                sU = unit[u - 1];
            }
            return sTens + "十" + sU;
        }
        return String.valueOf(num);
    }
    
    public static String getOrderStatusByNumber(int number) {
        String OrderStatus;
        switch (number) {
            case 0:
                OrderStatus = "待支付";
                break;
            case 1:
                OrderStatus = "已支付";
                break;
            case 2:
                OrderStatus = "支付中";
                
                break;
            case 3:
            case 15:
                OrderStatus = "退款中";
                
                break;
            case 4:
                OrderStatus = "全部退款";
                
                break;
            case 5:
                OrderStatus = "部分退款";
                
                break;
            case 6:
                OrderStatus = "换货中";
                break;
            case 8:
                OrderStatus = "换货完成";
                break;
            case 9:
                OrderStatus = "换货驳回";
                break;
            case 10:
                OrderStatus = "退款驳回";
                break;
            case 11:
                OrderStatus = "过期待退款";
                break;
            case 12:
                OrderStatus = "待退款";
                break;
            case 13:
                OrderStatus = "用户取消退款";
                break;
            case 14:
                OrderStatus = "待审核";
                break;
            case 16:
                OrderStatus = "已退款";
                break;
            case -1:
                OrderStatus = "已取消";
                break;
            default:
                OrderStatus = "";
            
        }
        return OrderStatus;
    }
    
    public static String getOrderActionByNumber(int number) {
        String OrderStatus;
        switch (number) {
            case 0:
                OrderStatus = "立即支付";
                break;
            case 3:
                OrderStatus = "取消退款";
                break;
            case 4:
                OrderStatus = "全部退款";
                break;
            case 5:
                OrderStatus = "取消退款";
                break;
            case 6:
                OrderStatus = "换货中";
                break;
            case 10:
                OrderStatus = "退款驳回";
                break;
            case 11:
            case 12:
            case 14:
            case 15:
                OrderStatus = "取消退款";
                break;
            case -1:
                OrderStatus = "重新购买";
                break;
            default:
                OrderStatus = "";
            
        }
        return OrderStatus;
    }
    
    public static String getGardenByNumber(String number) {
        String OrderStatus;
        switch (number) {
            case "1":
                OrderStatus = "一年级";
                break;
            case "2":
                OrderStatus = "二年级";
                break;
            case "3":
                OrderStatus = "三年级";
                
                break;
            case "4":
                OrderStatus = "四年级";
                
                break;
            case "5":
                OrderStatus = "五年级";
                
                break;
            case "6":
                OrderStatus = "六年级";
                
                break;
            case "7":
                OrderStatus = "初一";
                
                break;
            case "8":
                OrderStatus = "初二";
                
                break;
            case "9":
                OrderStatus = "初三";
                break;
            case "10":
                OrderStatus = "高一";
                break;
            case "11":
                OrderStatus = "高二";
                break;
            case "12":
                OrderStatus = "高三";
                break;
            default:
                OrderStatus = "";
            
        }
        return OrderStatus;
    }
    
    public static String getNumByGarden(String garden) {
        String OrderStatus;
        switch (garden) {
            case "一年级":
                OrderStatus = "1";
                break;
            case "二年级":
                OrderStatus = "2";
                break;
            case "三年级":
                OrderStatus = "3";
                
                break;
            case "四年级":
                OrderStatus = "4";
                
                break;
            case "五年级":
                OrderStatus = "5";
                
                break;
            case "六年级":
                OrderStatus = "6";
                
                break;
            case "初一":
                OrderStatus = "7";
                
                break;
            case "初二":
                OrderStatus = "8";
                
                break;
            case "初三":
                OrderStatus = "9";
                break;
            case "高一":
                OrderStatus = "10";
                break;
            case "高二":
                OrderStatus = "11";
                break;
            case "高三":
                OrderStatus = "12";
                break;
            default:
                OrderStatus = "";
        }
        return OrderStatus;
    }
    
    /**
     * 进入直播间
     *
     * @param courseKnowledgeId 课次id
     */
    public static void startLiveRoom(final Context context, String courseKnowledgeId) {
        CacheData.CLICK_TIME = System.currentTimeMillis();
        CacheData.isFirstEnter = true;
        LiveSccLiveInitCkRequest request = new LiveSccLiveInitCkRequest();
        request.setUserId(UserManager.getInstance().getUserId());
        request.setCourseKnowledgeId(Integer.parseInt(courseKnowledgeId));
        loading(context);
        Api.getInstance().postScc(request, LiveSccLiveInitCkResponse.class, new ResponseCallback<LiveSccLiveInitCkResponse>() {
            @Override
            public void onResponse(LiveSccLiveInitCkResponse date, boolean isFromCache) {
                if (!Utils.isSuccess(date.code)) {
                    showError(date.getMessage());
                    return;
                }
                LiveSccLiveInitCkResponse.ResultData data = date.getData();
                if (data == null) {
                    showError("获取视频资源失败");
                    return;
                }
                ImServerBean imServerBean = data.getImServerBean() != null ? data.getImServerBean() : new ImServerBean();//imServer鉴权信息
                LiveKnowledgeBaseBean knowledgeBaseBean = data.getKnowlageBaseBean();//课次详情基本信息
                LmcLiveRoomBean lmcLiveRoomBean = data.getLmcLiveRoomBean();//imc鉴权信息
                SccUserGoldBean sccUserGoldBean = data.getSccUserGoldBean() != null ? data.getSccUserGoldBean() : new SccUserGoldBean();//本次课的用户金币情况
                UserInfoBean sccUserLevelBean = data.getSccUserLevelBean() != null ? data.getSccUserLevelBean() : new UserInfoBean();//用户信息
                LiveUserBaseBean userBaseBean = data.getUserBaseBean();//直播部分用户基本能信息
                UserMqttConfig userMqttConfig = imServerBean.getUserMqttConfig();//im连接基本信息
                if (knowledgeBaseBean == null) {
                    showError("获取课次信息失败");
                    dismissLoading();
                    return;
                }
                if (lmcLiveRoomBean == null) {
                    showError("获取lmc鉴权失败");
                    return;
                }
                // 如果是阿里旧平台不往下走了
                String platform = knowledgeBaseBean.getLivePlatform();
                if ("aliyun".equalsIgnoreCase(platform)) {
                    //阿里云2.0版本
                    //                    CacheData.getInstance().setVideoTitle(knowledgeBaseBean.getKnowlageName());
                    PlayerPresenter.Companion.setTopicName(knowledgeBaseBean.getKnowlageName());
                    String url = lmcLiveRoomBean.getHttpsLiveUrl();
                    VideoPlayerActivity.start(context, url);
                    dismissLoading();
                    return;
                }
                //是否初始化im，拓课旧版本不需要
                if ("talk".equalsIgnoreCase(platform) || "aliyun".equalsIgnoreCase(platform)) {
                    CacheData.isInitIm = false;
                } else {
                    CacheData.isInitIm = true;
                }
                //                if (sccUserGoldBean == null || sccUserLevelBean == null) {
                //                    ToastUtils.showShort("获取用户金币信息失败");//可以继续
                //                }
                if (userBaseBean == null) {
                    showError("获取用户基本信息失败");
                    return;
                }
                if (userMqttConfig == null) {
                    showError("im通信获取失败");
                    return;
                }
                //im Config
                IMManager.Config config = new IMManager.Config();
                config.setUserId(imServerBean.getUserId());
                config.setRoomId(imServerBean.getRoomId());
                config.setClientId(userMqttConfig.getClientId());
                config.setGroupId(userMqttConfig.getGroupId());
                config.setTopic(userMqttConfig.getTopic());
                config.setSecretKey(userMqttConfig.getSecretKey());
                config.setAccessKey(userMqttConfig.getAccessKey());
                config.setSubGroup(userBaseBean.getSubGroupId());
                config.setBroker("tcp://post-cn-4590xb58607.mqtt.aliyuncs.com");
                config.setKnowledgeId(knowledgeBaseBean.getKnowlageId() + "");
                CacheData.getInstance().setTeacherId(knowledgeBaseBean.getTeacherId());
                CacheData.getInstance().setConfig(config);
                //im 权限
                String str_acl = imServerBean.getAclConfig();
                ACL acl = new Gson().fromJson(str_acl, ACL.class);
                CacheData.getInstance().setACL(acl);
                //用户信息
                User user = new User();
                user.set_id(imServerBean.getId());
                user.setUserId(userBaseBean.getUserId() + "");
                user.setRole(userBaseBean.getUserRole());
                user.setSubgroupIds(Arrays.asList(userBaseBean.getSubGroupId()));
                Prop prop = new Prop();
                prop.setHeaderUrl(userBaseBean.getHeaderUrl());
                prop.setName(userBaseBean.getUserName());
                prop.setLevel(sccUserLevelBean.getLevel());
                prop.setLevelName(sccUserLevelBean.getLevelName());
                prop.setPartGold(sccUserGoldBean.getPartGold() == null ? 0 : sccUserGoldBean.getPartGold());
                prop.setPartProgress(sccUserLevelBean.getPartProgress() == null ? 0 : sccUserLevelBean.getPartProgress());
                prop.setProgressPoint(sccUserLevelBean.getPartProgress() == null ? 0 : sccUserLevelBean.getPartProgress());
                prop.setGoldPoint(sccUserGoldBean.getPartGold() == null ? 0 : sccUserGoldBean.getPartGold());
                user.setProp(prop);
                CacheData.getInstance().setUser(user);
                PlayerPresenter.Companion.setTopicName(knowledgeBaseBean.getKnowlageName());
                PlayerPresenter.Companion.InitInfo(imServerBean.getAppId(), lmcLiveRoomBean.getRoomId(), lmcLiveRoomBean.getRole(), lmcLiveRoomBean.getUserId(), lmcLiveRoomBean.getRandom(), System.currentTimeMillis(), imServerBean.getServerSignKey(), lmcLiveRoomBean.getSubGroupId());
                if (CacheData.isInitIm) {
                    pullCommandHistory(imServerBean.getAppId(), lmcLiveRoomBean.getRandom(), lmcLiveRoomBean.getRole(), lmcLiveRoomBean.getRoomId() + "", System.currentTimeMillis() + "", lmcLiveRoomBean.getUserId(), imServerBean.getServerSignKey(), 0L, lmcLiveRoomBean.getSubGroupId());
                }
                getLiveRoomConfig(context, lmcLiveRoomBean.getAppId(), lmcLiveRoomBean.getRoomId(), lmcLiveRoomBean.getRole(), lmcLiveRoomBean.getUserId(), lmcLiveRoomBean.getSubGroupId(), lmcLiveRoomBean.getRandom(), lmcLiveRoomBean.getExpire(), lmcLiveRoomBean.getAppSign(), lmcLiveRoomBean.getNickname());
                //            Api.getInstance().postScc(new LmcRoomConfigRequest(),);
            }
            
            @Override
            public void onError() {
                super.onError();
                showError("连接异常");
                
            }
        }, "");
    }
    
    
    public static void getLiveRoomConfig(final Context context, String appId, int roomId, String role, final String userId, String subGroupId, String random, String expire, String appSign, final String nickname) {
        //        LmcRoomConfigRequest request = new LmcRoomConfigRequest(appId, roomId, role, userId, subGroupId, random, expire, appSign, nickname);
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("appId", appId);
        hashMap.put("roomId", String.valueOf(roomId));
        hashMap.put("role", role);
        hashMap.put("userId", userId);
        hashMap.put("subGroupId", subGroupId);
        hashMap.put("random", random);
        hashMap.put("expire", expire);
        hashMap.put("appSign", appSign);
        //        hashMap.put("expire", expire);
        //        hashMap.put("expire", expire);
        Api.getInstance().getLiveRoomConfig(hashMap, LmcRoomConfigResponse.class, new ResponseCallback<LmcRoomConfigResponse>() {
            @Override
            public void onResponse(LmcRoomConfigResponse date, boolean isFromCache) {
                choiceRoom(context, date, userId, nickname);
            }
            
            @Override
            public void onFail(LmcRoomConfigResponse date, boolean isFromCache) {
                super.onFail(date, isFromCache);
                showError(date.getMessage());
            }
            
            @Override
            public void onError() {
                super.onError();
                showError("获取教室信息失败");
            }
        }, "");
    }
    
    public static void choiceRoom(Context context, LmcRoomConfigResponse date, String userId, String nickName) {
        if (ObjectUtils.isEmpty(date.getData())) {
            showError("获取教室信息失败");
            return;
        }
        LmcRoomConfigResponse.DataBean.RoomBean room = date.getData().getRoom();
        List<LmcRoomConfigResponse.DataBean.MediaConfigsBean> mediaConfigs = date.getData().getMediaConfigs();
        List<LmcRoomConfigResponse.DataBean.RoomBean.SignedRoomPlaybackMediasBean> signedRoomPlaybackMedias = room.getSignedRoomPlaybackMedias();
        List<LmcRoomConfigResponse.DataBean.RoomBean.SignedRoomMediasBean> signedRoomMedias = room.getSignedRoomMedias();
        long serverTime = date.getServerTime();
        CacheData.getInstance().setCurrentTime(serverTime);
        if (!ObjectUtils.isEmpty(signedRoomPlaybackMedias)) {
            //回放
            CacheData.isLivingPlay = false;
            LmcRoomConfigResponse.DataBean.RoomBean.SignedRoomPlaybackMediasBean signedRoomPlaybackMediasBean = signedRoomPlaybackMedias.get(0);
            
            String playbakcPlatformType = signedRoomPlaybackMediasBean.getPlaybakcPlatformType();
            List<LmcRoomConfigResponse.DataBean.RoomBean.SignedRoomPlaybackMediasBean.PlayListBean> playList = signedRoomPlaybackMediasBean.getPlayList();
            if ("aliyun".equalsIgnoreCase(playbakcPlatformType) || "agora_live".equalsIgnoreCase(playbakcPlatformType) || "aliyun_v3".equalsIgnoreCase(playbakcPlatformType)) {
                ((BaseActivity) context).dismissLoadingDialog();
                if (!ObjectUtils.isEmpty(playList)) {
                    LmcRoomConfigResponse.DataBean.RoomBean.SignedRoomPlaybackMediasBean.PlayListBean.TranscodeUrlBean transcodeUrl = playList.get(0).getTranscodeUrl();
                    String url = playList.get(0).getUrl();
                    if (!url.startsWith("http")) {
                        url = "http:" + url;
                    }
                    if (!ObjectUtils.isEmpty(transcodeUrl)) {
                        ArrayList<VideoUrl> videoUrls = new ArrayList<>();
                        if (!TextUtils.isEmpty(transcodeUrl.getSd())) {
                            VideoUrl videoUrl = new VideoUrl();
                            videoUrl.setFormatName("标清");
                            videoUrl.setFormatUrl(transcodeUrl.getSd());
                            videoUrls.add(videoUrl);
                        }
                        if (!TextUtils.isEmpty(transcodeUrl.getLd())) {
                            VideoUrl videoUrl = new VideoUrl();
                            videoUrl.setFormatName("高清");
                            videoUrl.setFormatUrl(transcodeUrl.getLd());
                            videoUrls.add(videoUrl);
                        }
                        if (!TextUtils.isEmpty(transcodeUrl.getHd())) {
                            VideoUrl videoUrl = new VideoUrl();
                            videoUrl.setFormatName("原画");
                            videoUrl.setFormatUrl(transcodeUrl.getHd());
                            videoUrls.add(videoUrl);
                        }
                        VideoActivity.start(context, videoUrls, date.getData().getLiveName());
                        dismissLoading();
                    } else {
                        if (!ObjectUtils.isEmpty(url)) {//仅一种画质
                            VideoUrl videoUrl = new VideoUrl();
                            videoUrl.setFormatName("原画");
                            videoUrl.setFormatUrl(url);
                            ArrayList<VideoUrl> urls = new ArrayList<>();
                            urls.add(videoUrl);
                            VideoActivity.start(context, urls, date.getData().getLiveName());
                            dismissLoading();
                            return;
                        }
                        ToastUtils.showShort("未知播放源");
                        dismissLoading();
                        UMengAnalytics.INSTANCE.onEvent(context, UMengAnalytics.INSTANCE.getLook_video_fail());
                    }
                } else {
                    ToastUtils.showShort("未知播放源");
                    dismissLoading();
                    UMengAnalytics.INSTANCE.onEvent(context, UMengAnalytics.INSTANCE.getLook_video_fail());
                }
            } else if ("talk".equalsIgnoreCase(playbakcPlatformType) || "talk_v3".equalsIgnoreCase(playbakcPlatformType)) {//拓课
                LmcRoomConfigResponse.DataBean.RoomBean.SignedRoomPlaybackMediasBean.PlayListBean playListBean = playList.get(0);
                String serial = playListBean.getSerial();
                String path = playListBean.getRecordpath();
                joinTalkPlayBack(context, serial, "", userId, nickName, path);
                dismissLoading();
            } else {
                UMengAnalytics.INSTANCE.onEvent(context, UMengAnalytics.INSTANCE.getLook_video_fail());
                showError("未知播放源");
            }
        } else {
            //直播
            CacheData.isLivingPlay = true;
            //            if (mediaConfigs != null && mediaConfigs.size() > 0) {
            if (ObjectUtils.isEmpty(signedRoomMedias) || ObjectUtils.isEmpty(mediaConfigs)) {
                UMengAnalytics.INSTANCE.onEvent(context, UMengAnalytics.INSTANCE.getLook_live_fail());
                showError("获取视频播放资源失败");
                return;
            }
            
            String platfrom = mediaConfigs.get(0).getLivePalatform();
            String liveStatus = mediaConfigs.get(0).getLiveStatus();// start 直播中  suspend：暂停休息中 end：直播结束  null 未开始
            if ("aliyun".equalsIgnoreCase(platfrom) || "aliyun_v3".equalsIgnoreCase(platfrom)) {
                ((BaseActivity) context).dismissLoadingDialog();
                //阿里云平台
                LmcRoomConfigResponse.DataBean.RoomBean.SignedRoomMediasBean signedRoomMediasBean = signedRoomMedias.get(0);
                if (!ObjectUtils.isEmpty(signedRoomMediasBean)) {
                    ArrayList<VideoUrl> videoUrls = new ArrayList<>();
                    //排序： 原画 高清 标清
                    String flvUrl = ObjectUtils.isEmpty(signedRoomMediasBean.getFLV_URL()) ? signedRoomMediasBean.getRTMP_URL() : signedRoomMediasBean.getFLV_URL();
                    if (!TextUtils.isEmpty(flvUrl)) {
                        VideoUrl videoUrl = new VideoUrl();
                        videoUrl.setFormatName("原画");
                        videoUrl.setFormatUrl(flvUrl);
                        videoUrls.add(videoUrl);
                    }
                    if (!TextUtils.isEmpty(signedRoomMediasBean.getHD_RTMP_URL())) {
                        VideoUrl videoUrl = new VideoUrl();
                        videoUrl.setFormatName("高清");
                        videoUrl.setFormatUrl(signedRoomMediasBean.getHD_RTMP_URL());
                        videoUrls.add(videoUrl);
                    }
                    if (!TextUtils.isEmpty(signedRoomMediasBean.getSD_RTMP_URL())) {
                        VideoUrl videoUrl = new VideoUrl();
                        videoUrl.setFormatName("标清");
                        videoUrl.setFormatUrl(signedRoomMediasBean.getSD_RTMP_URL());
                        videoUrls.add(videoUrl);
                    }
                    LivePlayerActivity.start(context, videoUrls, liveStatus, date.getData().getLiveName());
                    dismissLoading();
                }
            } else if ("agora_live".equalsIgnoreCase(platfrom)) {///声网
                ((BaseActivity) context).dismissLoadingDialog();
                
                String token = signedRoomMedias.get(0).getAgoraChannelKey();
                String appId = signedRoomMedias.get(0).getAgoraAppID();
                String channel = signedRoomMedias.get(0).getAgoraChannel();
                int uId = signedRoomMedias.get(0).getAgoraUid();
                if (ObjectUtils.isEmpty(token) || ObjectUtils.isEmpty(appId) || ObjectUtils.isEmpty(channel)) {
                    showError("您无权限播放视频源!");
                }
                AgoraActivity.start(context, liveStatus, token, appId, channel, uId);
                dismissLoading();
            } else if ("talk_v3".equalsIgnoreCase(platfrom) || "talk".equalsIgnoreCase(platfrom)) {
                //tuoke平台
                String pwd = signedRoomMedias.get(0).getStudent_pwd();
                String serial = signedRoomMedias.get(0).getSerial();
                joinRoom(context, serial, pwd, userId, nickName);
            } else {
                UMengAnalytics.INSTANCE.onEvent(context, UMengAnalytics.INSTANCE.getLook_live_fail());
                showError("未知播放源");
            }
        }
    }
    
    
    /**
     * 拉取普通历史消息
     *
     * @param context
     * @param appId
     * @param roomId
     * @param role
     * @param userId
     * @param subGroupId
     * @param random
     */
    public static void pullCommonHistory(final Context context, String appId, int roomId, String role, String userId, String random, long stamp, String serverSignKey, String subGroupId, long endTime, int page) {
        StringBuffer comStr = new StringBuffer();
        comStr.append("appId=" + appId)
            .append("&random=" + random)
            .append("&role=" + role)
            .append("&roomId=" + roomId + "")
            .append("&stamp=" + stamp + "")
            .append("&userId=" + userId)
            .append("&serverSignKey=" + serverSignKey);
        String appSign = com.haoke91.a91edu.utils.rsa.Md5Utils.MD5_LOWERCASE(comStr.toString());
        String url = Constant.BASEURL_IM + "/imApi/server/pull/msg/common?" + comStr + "&appSign=" + appSign;
        
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("subgroupId", subGroupId);
        hashMap.put("endTime", endTime + "");
        hashMap.put("pageSize", page + "");
        Api.getInstance().netPost(url, hashMap, GetImHistoryResponse.class, new ResponseCallback<GetImHistoryResponse>() {
            @Override
            public void onResponse(GetImHistoryResponse date, boolean isFromCache) {
            }
            
            @Override
            public void onError() {
                super.onError();
            }
        }, "load common history");
    }
    
    /**
     * 拉取命令历史消息
     *
     * @param appId
     * @param random
     * @param role
     * @param roomId
     * @param stamp
     * @param userId
     * @param endTime
     * @param subGroupId
     */
    public static void pullCommandHistory(String appId, String random, String role, String roomId, String stamp, final String userId, String serverSignKey, long endTime, final String subGroupId) {
        StringBuffer comStr = new StringBuffer();
        comStr.append("appId=" + appId)
            .append("&random=" + random)
            .append("&role=" + role)
            .append("&roomId=" + roomId)
            .append("&stamp=" + stamp)
            .append("&userId=" + userId)
            .append("&serverSignKey=" + serverSignKey);
        String appSign = com.haoke91.a91edu.utils.rsa.Md5Utils.MD5_LOWERCASE(comStr.toString());
        String url = Constant.BASEURL_IM + "/imApi/server/pull/command/list?" + comStr + "&appSign=" + appSign;
        
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("subgroupId", subGroupId);
        hashMap.put("endTime", endTime + "");
        Api.getInstance().netPost(url, hashMap, GetImCommandResponse.class, new ResponseCallback<GetImCommandResponse>() {
            @Override
            public void onResponse(GetImCommandResponse date, boolean isFromCache) {
                List<CommandMessage> list = date.getData();
                CacheData.getInstance().dealCommandMessage(list, userId, subGroupId);
            }
            
            @Override
            public void onError() {
                super.onError();
            }
        }, "load command history");
    }
    
    /**
     * playback
     */
    private static void joinTalkPlayBack(Context context, String serial, String pwd, String userId, String nickName, String path) {
        TKRoomManager.init(context, "x3Pa50JQi89mgFVG", null);
        RoomClient.getInstance().regiestInterface(new Notify(), new CallBack(context));
        String temp = "host=global.talk-cloud.net&domain=icoachu&serial=" + serial + "&type=2&path=" + RoomClient.webServer + ":8081" + path;
        RoomClient.getInstance().joinPlayBackRoom((Activity) context, temp);
    }
    
    /**
     * live
     */
    private static void joinRoom(Context context, String serial, String pwd, String userId, String nickName) {
        TKRoomManager.init(context, "x3Pa50JQi89mgFVG", null);
        HashMap map = new HashMap<String, Object>();
        map.put("host", RoomClient.webServer);
        map.put("port", 80);
        map.put("serial", serial); //房间号
        map.put("nickname", nickName); //昵称
        map.put("password", pwd);
        map.put("userid", userId);
        map.put("clientType", "2");//  1 PC   2 Android   3 IOS
        map.put("userrole", 2); //老师0，学生2，
        RoomClient.getInstance().regiestInterface(new Notify(), new CallBack(context));
        RoomClient.getInstance().joinRoom((Activity) context, map);
    }
    
    private static class Notify implements MeetingNotify {
        @Override
        public void onKickOut(int res) {
            ToastUtils.showShort("您已被踢出");
        }
        
        @Override
        public void onWarning(int code) {
            ToastUtils.showShort("code错误");
        }
        
        @Override
        public void onClassBegin() {
            ToastUtils.showShort("开始上课了");
        }
        
        @Override
        public void onClassDismiss() {
            ToastUtils.showShort("课程结束");
        }
    }
    
    /**
     * 直播错误提示
     *
     * @param err
     */
    private static void showError(String err) {
        dismissLoading();
    }
    
    private static class CallBack implements JoinmeetingCallBack {
        //        private Context context;
        private WeakReference<Context> context;
        
        CallBack(Context context) {
            this.context = new WeakReference<>(context);
            
        }
        
        @Override
        public void callBack(int code) {
            String result = "";
            //            ((BaseActivity) context.get()).dismissLoadingDialog();
            dismissLoading();
            if (code == 0) {
                TkActivity.Companion.stat(context.get());
                return;
            } else if (code == 100) {
                
                // 踢人重置  登陆按钮
                
            } else if (code == 101) {
                result = context.get().getString(R.string.checkmeeting_error_5005);
            } else if (code == 4008) {
                result = context.get().getString(R.string.checkmeeting_error_4008);
            } else if (code == 4110) {
                result = context.get().getString(R.string.checkmeeting_error_4110);
            } else if (code == 4007) {
                result = context.get().getString(R.string.checkmeeting_error_4007);
            } else if (code == 3001) {
                result = context.get().getString(R.string.checkmeeting_error_3001);
            } else if (code == 3002) {
                result = context.get().getString(R.string.checkmeeting_error_3002);
            } else if (code == 3003) {
                result = context.get().getString(R.string.checkmeeting_error_3003);
            } else if (code == 4109) {
                result = context.get().getString(R.string.checkmeeting_error_4109);
            } else if (code == 4103) {
                result = context.get().getString(R.string.checkmeeting_error_4103);
            } else if (code == 4012) {
                result = context.get().getString(R.string.checkmeeting_error_4008);
            } else if (code == 5005) {
                result = context.get().getString(R.string.checkmeeting_error_5005);
                
            } else {
                if (code == -1 || code == 3) {
                    result = context.get().getString(R.string.WaitingForNetwork);
                } else {
                    result = context.get().getString(R.string.WaitingForNetwork) + "(" + code + ")";
                }
            }
            ToastUtils.showShort(result);
        }
    }
}
