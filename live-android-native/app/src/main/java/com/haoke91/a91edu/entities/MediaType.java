package com.haoke91.a91edu.entities;

import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/5/16 下午4:37
 * 修改人：weiyimeng
 * 修改时间：2018/5/16 下午4:37
 * 修改备注：
 */
public class MediaType {
    
    /**
     * isLive : 1
     * mobileVedioType : ->application/octet-stream
     * paltformType : aliyun
     * liveStatus : end
     * pcUrls : [{"url":"http://v-test.teacherv.top/91haoke/transcoding/transcode_hd/mp4/bbbd5b32b5cd43a0a75d8b81791b3651/777_1_20170524171545.mp4?auth_key=1526475726-0-0-e3ac0b89e9fbfe1771dfa75ac8a95e38","definitionText":"原画"},{"url":"http://v-test.teacherv.top/91haoke/transcoding/transcode_sd/mp4/bbbd5b32b5cd43a0a75d8b81791b3651/777_1_20170524171545.mp4?auth_key=1526475726-0-0-b7dec4595ed7d0aae72a4c1dc28ad375","definitionText":"高清"},{"url":"http://v-test.teacherv.top/91haoke/transcoding/transcode_ld/mp4/bbbd5b32b5cd43a0a75d8b81791b3651/777_1_20170524171545.mp4?auth_key=1526475726-0-0-d0f7bea12ba4feead599c08d9d514f5b","definitionText":"标清"}]
     * liveName : 数学几何综合之中点问题（免费公开课）
     * liveDesc : 数学几何综合之中点问题（免费公开课）
     * liveTime : 18:00~19:30
     * bgImg : http://file1.teacherv.top/static/lmc/resources/images/placeholder/player_bg_end.jpg
     */
    
    private String isLive;
    private String mobileVedioType;
    private String paltformType;
    private String liveStatus;
    private String liveName;
    private String liveDesc;
    private String liveTime;
    private String bgImg;
    private List<PcUrlsBean> pcUrls;
    
    public String getIsLive() {
        return isLive;
    }
    
    public void setIsLive(String isLive) {
        this.isLive = isLive;
    }
    
    public String getMobileVedioType() {
        return mobileVedioType;
    }
    
    public void setMobileVedioType(String mobileVedioType) {
        this.mobileVedioType = mobileVedioType;
    }
    
    public String getPaltformType() {
        return paltformType;
    }
    
    public void setPaltformType(String paltformType) {
        this.paltformType = paltformType;
    }
    
    public String getLiveStatus() {
        return liveStatus;
    }
    
    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }
    
    public String getLiveName() {
        return liveName;
    }
    
    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }
    
    public String getLiveDesc() {
        return liveDesc;
    }
    
    public void setLiveDesc(String liveDesc) {
        this.liveDesc = liveDesc;
    }
    
    public String getLiveTime() {
        return liveTime;
    }
    
    public void setLiveTime(String liveTime) {
        this.liveTime = liveTime;
    }
    
    public String getBgImg() {
        return bgImg;
    }
    
    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }
    
    public List<PcUrlsBean> getPcUrls() {
        return pcUrls;
    }
    
    public void setPcUrls(List<PcUrlsBean> pcUrls) {
        this.pcUrls = pcUrls;
    }
    
    public static class PcUrlsBean {
        /**
         * url : http://v-test.teacherv.top/91haoke/transcoding/transcode_hd/mp4/bbbd5b32b5cd43a0a75d8b81791b3651/777_1_20170524171545.mp4?auth_key=1526475726-0-0-e3ac0b89e9fbfe1771dfa75ac8a95e38
         * definitionText : 原画
         */
        
        private String url;
        private String definitionText;
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
        
        public String getDefinitionText() {
            return definitionText;
        }
        
        public void setDefinitionText(String definitionText) {
            this.definitionText = definitionText;
        }
    }
}
