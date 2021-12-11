package com.gaosiedu.scc.sdk.android.domain;

import com.gaosiedu.live.sdk.android.base.ResponseResult;

import java.util.List;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/12 3:02 PM
 * 修改人：weiyimeng
 * 修改时间：2018/11/12 3:02 PM
 * 修改备注：
 */
public class LmcRoomConfigResponse extends ResponseResult {
    
    
    private DataBean data;
    private long serverTime;
    
    public long getServerTime(){
        return serverTime;
    }
    
    public void setServerTime(long serverTime){
        this.serverTime = serverTime;
    }
    
    public DataBean getData() {
        return data;
    }
    
    public void setData(DataBean data) {
        this.data = data;
    }
    //
    //    public long getServerTime() {
    //        return serverTime;
    //    }
    //
    //    public void setServerTime(long serverTime) {
    //        this.serverTime = serverTime;
    //    }
    //
    //    public String getStatus() {
    //        return status;
    //    }
    //
    //    public void setStatus(String status) {
    //        this.status = status;
    //    }
    
    public static class DataBean {
        /**
         * appId : 91haoke
         * endTime : 1512207000000
         * flag : 4874
         * id : 4023
         * liveDesc : 三年级迎春杯初赛试题讲解
         * liveName : 三年级迎春杯初赛试题讲解
         * mediaConfigs : [{"id":4076,"isMain":1,"lastOptionTime":1512210510000,"lastUpdateTime":1511243443000,"livePalatform":"aliyun","livePlatformParam":{"liveDomain":"live.91haoke.com","courseId":"4023_1","liveAppName":"91haoke"},"liveRoomId":4023,"liveStatus":"end","mediaIndex":1,"name":"三年级迎春杯初赛试题讲解","realEndTime":1512210510000,"realStartTime":1512204150000,"sort":1,"status":1,"subGroupIds":["ALL"],"users":["a_11921","t_11921"]}]
         * room : {"signedRoomPlaybackMedias":[{"mediaIndex":1,"playList":[{"useM3u8":true,"transcodeUrl":{"ld":"http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/ld/21I8CBVjJgzadsi6/1540644036808.m3u8","sd":"http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/sd/Rk42uUrfflhU3t6H/1540644036808.m3u8","hd":"http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/hd/HMovhOi9sLjNnJxZ/1540644036808.m3u8"},"format":"m3u8","transcode":false,"url":"http://playback.teacherv.top/record/91haoke/2018-10-27/6220_1/6220_1_20181027204017.m3u8"}],"playbakcPlatformType":"aliyun"}],"signedRoomMedias":[{"index":1,"HD_RTMP_AUTH_KEY":"1542091338-0-0-90eb84d4008017f69fca1a368956a45b","paltformType":"aliyun","FLV_URL":"http://live.91haoke.com/91haoke/4023_1.flv?auth_key=1542091338-0-0-a63ef7e2244e3b690a99868d8fc856ad","HD_RTMP_URL":"rtmp://live.91haoke.com/91haoke/4023_1_hd?auth_key=1542091338-0-0-90eb84d4008017f69fca1a368956a45b","SD_RTMP_AUTH_KEY":"1542091338-0-0-eeec03ae46270ca4e1f8af072d293726","FLV_AUTH_KEY":"1542091338-0-0-a63ef7e2244e3b690a99868d8fc856ad","isMain":1,"SD_RTMP_URL":"rtmp://live.91haoke.com/91haoke/4023_1_sd?auth_key=1542091338-0-0-eeec03ae46270ca4e1f8af072d293726"}],"conf":{"imAccountType":"8734","imSign":"eJxNj9tugkAQht*FWxu7R1N6RxWQKKRYidYbgu4iE3ShsAXbpu9eIJh07uY-fJP5Mbbrt2kt8jgpSxDGs4EZQogjRpnxMJjyVkIl4yTVsup8wk3SJUZzaMWJjmnVl**yhqvsWZx1WWbSp1EHIZWGFAaSibOkyCXFhPE77nQqPpWO9Vcp-*FqOHebb7-PvXDhs81k4ti8katrQ87H3BTHV9jfRBBtL8EhXKYfs2-ncbV0Wy*z1o4FeL9LL*IQLsRGzHzttirhKoOXCAeusndW60WFtprxWCOrGgrVv4owx4SifozfP1ogWRw_","identifier":"91haoke31245","imsdkAppID":"1400050434"}}
         * serverConfig : {"resourceCdn":"//file1.teacherv.top/static/lmc/resources","heartbeatInterval":300000}
         * startTime : 1512203400000
         * status : 1
         */
        
        private String appId;
        private long endTime;
        private String flag;
        private int id;
        private String liveDesc;
        private String liveName;
        private RoomBean room;
        private ServerConfigBean serverConfig;
        private long startTime;
        private int status;
        private List<MediaConfigsBean> mediaConfigs;
        
        public String getAppId() {
            return appId;
        }
        
        public void setAppId(String appId) {
            this.appId = appId;
        }
        
        public long getEndTime() {
            return endTime;
        }
        
        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }
        
        public String getFlag() {
            return flag;
        }
        
        public void setFlag(String flag) {
            this.flag = flag;
        }
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public String getLiveDesc() {
            return liveDesc;
        }
        
        public void setLiveDesc(String liveDesc) {
            this.liveDesc = liveDesc;
        }
        
        public String getLiveName() {
            return liveName;
        }
        
        public void setLiveName(String liveName) {
            this.liveName = liveName;
        }
        
        public RoomBean getRoom() {
            return room;
        }
        
        public void setRoom(RoomBean room) {
            this.room = room;
        }
        
        public ServerConfigBean getServerConfig() {
            return serverConfig;
        }
        
        public void setServerConfig(ServerConfigBean serverConfig) {
            this.serverConfig = serverConfig;
        }
        
        public long getStartTime() {
            return startTime;
        }
        
        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }
        
        public int getStatus() {
            return status;
        }
        
        public void setStatus(int status) {
            this.status = status;
        }
        
        public List<MediaConfigsBean> getMediaConfigs() {
            return mediaConfigs;
        }
        
        public void setMediaConfigs(List<MediaConfigsBean> mediaConfigs) {
            this.mediaConfigs = mediaConfigs;
        }
        
        public static class RoomBean {
            /**
             * signedRoomPlaybackMedias : [{"mediaIndex":1,"playList":[{"useM3u8":true,"transcodeUrl":{"ld":"http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/ld/21I8CBVjJgzadsi6/1540644036808.m3u8","sd":"http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/sd/Rk42uUrfflhU3t6H/1540644036808.m3u8","hd":"http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/hd/HMovhOi9sLjNnJxZ/1540644036808.m3u8"},"format":"m3u8","transcode":false,"url":"http://playback.teacherv.top/record/91haoke/2018-10-27/6220_1/6220_1_20181027204017.m3u8"}],"playbakcPlatformType":"aliyun"}]
             * signedRoomMedias : [{"index":1,"HD_RTMP_AUTH_KEY":"1542091338-0-0-90eb84d4008017f69fca1a368956a45b","paltformType":"aliyun","FLV_URL":"http://live.91haoke.com/91haoke/4023_1.flv?auth_key=1542091338-0-0-a63ef7e2244e3b690a99868d8fc856ad","HD_RTMP_URL":"rtmp://live.91haoke.com/91haoke/4023_1_hd?auth_key=1542091338-0-0-90eb84d4008017f69fca1a368956a45b","SD_RTMP_AUTH_KEY":"1542091338-0-0-eeec03ae46270ca4e1f8af072d293726","FLV_AUTH_KEY":"1542091338-0-0-a63ef7e2244e3b690a99868d8fc856ad","isMain":1,"SD_RTMP_URL":"rtmp://live.91haoke.com/91haoke/4023_1_sd?auth_key=1542091338-0-0-eeec03ae46270ca4e1f8af072d293726"}]
             * conf : {"imAccountType":"8734","imSign":"eJxNj9tugkAQht*FWxu7R1N6RxWQKKRYidYbgu4iE3ShsAXbpu9eIJh07uY-fJP5Mbbrt2kt8jgpSxDGs4EZQogjRpnxMJjyVkIl4yTVsup8wk3SJUZzaMWJjmnVl**yhqvsWZx1WWbSp1EHIZWGFAaSibOkyCXFhPE77nQqPpWO9Vcp-*FqOHebb7-PvXDhs81k4ti8katrQ87H3BTHV9jfRBBtL8EhXKYfs2-ncbV0Wy*z1o4FeL9LL*IQLsRGzHzttirhKoOXCAeusndW60WFtprxWCOrGgrVv4owx4SifozfP1ogWRw_","identifier":"91haoke31245","imsdkAppID":"1400050434"}
             */
            
            private ConfBean conf;
            private List<SignedRoomPlaybackMediasBean> signedRoomPlaybackMedias;
            private List<SignedRoomMediasBean> signedRoomMedias;
            
            public ConfBean getConf() {
                return conf;
            }
            
            public void setConf(ConfBean conf) {
                this.conf = conf;
            }
            
            public List<SignedRoomPlaybackMediasBean> getSignedRoomPlaybackMedias() {
                return signedRoomPlaybackMedias;
            }
            
            public void setSignedRoomPlaybackMedias(List<SignedRoomPlaybackMediasBean> signedRoomPlaybackMedias) {
                this.signedRoomPlaybackMedias = signedRoomPlaybackMedias;
            }
            
            public List<SignedRoomMediasBean> getSignedRoomMedias() {
                return signedRoomMedias;
            }
            
            public void setSignedRoomMedias(List<SignedRoomMediasBean> signedRoomMedias) {
                this.signedRoomMedias = signedRoomMedias;
            }
            
            public static class ConfBean {
                /**
                 * imAccountType : 8734
                 * imSign : eJxNj9tugkAQht*FWxu7R1N6RxWQKKRYidYbgu4iE3ShsAXbpu9eIJh07uY-fJP5Mbbrt2kt8jgpSxDGs4EZQogjRpnxMJjyVkIl4yTVsup8wk3SJUZzaMWJjmnVl**yhqvsWZx1WWbSp1EHIZWGFAaSibOkyCXFhPE77nQqPpWO9Vcp-*FqOHebb7-PvXDhs81k4ti8katrQ87H3BTHV9jfRBBtL8EhXKYfs2-ncbV0Wy*z1o4FeL9LL*IQLsRGzHzttirhKoOXCAeusndW60WFtprxWCOrGgrVv4owx4SifozfP1ogWRw_
                 * identifier : 91haoke31245
                 * imsdkAppID : 1400050434
                 */
                
                private String imAccountType;
                private String imSign;
                private String identifier;
                private String imsdkAppID;
                
                public String getImAccountType() {
                    return imAccountType;
                }
                
                public void setImAccountType(String imAccountType) {
                    this.imAccountType = imAccountType;
                }
                
                public String getImSign() {
                    return imSign;
                }
                
                public void setImSign(String imSign) {
                    this.imSign = imSign;
                }
                
                public String getIdentifier() {
                    return identifier;
                }
                
                public void setIdentifier(String identifier) {
                    this.identifier = identifier;
                }
                
                public String getImsdkAppID() {
                    return imsdkAppID;
                }
                
                public void setImsdkAppID(String imsdkAppID) {
                    this.imsdkAppID = imsdkAppID;
                }
            }
            
            public static class SignedRoomPlaybackMediasBean {
                /**
                 * mediaIndex : 1
                 * playList : [{"useM3u8":true,"transcodeUrl":{"ld":"http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/ld/21I8CBVjJgzadsi6/1540644036808.m3u8","sd":"http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/sd/Rk42uUrfflhU3t6H/1540644036808.m3u8","hd":"http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/hd/HMovhOi9sLjNnJxZ/1540644036808.m3u8"},"format":"m3u8","transcode":false,"url":"http://playback.teacherv.top/record/91haoke/2018-10-27/6220_1/6220_1_20181027204017.m3u8"}]
                 * playbakcPlatformType : aliyun
                 */
                
                private int mediaIndex;
                private String playbakcPlatformType;
                private List<PlayListBean> playList;
                
                public int getMediaIndex() {
                    return mediaIndex;
                }
                
                public void setMediaIndex(int mediaIndex) {
                    this.mediaIndex = mediaIndex;
                }
                
                public String getPlaybakcPlatformType() {
                    return playbakcPlatformType;
                }
                
                public void setPlaybakcPlatformType(String playbakcPlatformType) {
                    this.playbakcPlatformType = playbakcPlatformType;
                }
                
                public List<PlayListBean> getPlayList() {
                    return playList;
                }
                
                public void setPlayList(List<PlayListBean> playList) {
                    this.playList = playList;
                }
                
                public static class PlayListBean {
                    /**
                     * useM3u8 : true
                     * transcodeUrl : {"ld":"http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/ld/21I8CBVjJgzadsi6/1540644036808.m3u8","sd":"http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/sd/Rk42uUrfflhU3t6H/1540644036808.m3u8","hd":"http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/hd/HMovhOi9sLjNnJxZ/1540644036808.m3u8"}
                     * format : m3u8
                     * transcode : false
                     * url : http://playback.teacherv.top/record/91haoke/2018-10-27/6220_1/6220_1_20181027204017.m3u8
                     * recordpath:拓课回放路径
                     * serial:拓课房间号
                     */
                    
                    private boolean useM3u8;
                    private TranscodeUrlBean transcodeUrl;
                    private String format;
                    private boolean transcode;
                    private String url;
                    private String serial;
                    private String recordpath;
    
                    public String getSerial() {
                        return serial;
                    }
    
                    public void setSerial(String serial) {
                        this.serial = serial;
                    }
    
                    public String getRecordpath() {
                        return recordpath;
                    }
    
                    public void setRecordpath(String recordpath) {
                        this.recordpath = recordpath;
                    }
    
                    public boolean isUseM3u8() {
                        return useM3u8;
                    }
                    
                    public void setUseM3u8(boolean useM3u8) {
                        this.useM3u8 = useM3u8;
                    }
                    
                    public TranscodeUrlBean getTranscodeUrl() {
                        return transcodeUrl;
                    }
                    
                    public void setTranscodeUrl(TranscodeUrlBean transcodeUrl) {
                        this.transcodeUrl = transcodeUrl;
                    }
                    
                    public String getFormat() {
                        return format;
                    }
                    
                    public void setFormat(String format) {
                        this.format = format;
                    }
                    
                    public boolean isTranscode() {
                        return transcode;
                    }
                    
                    public void setTranscode(boolean transcode) {
                        this.transcode = transcode;
                    }
                    
                    public String getUrl() {
                        return url;
                    }
                    
                    public void setUrl(String url) {
                        this.url = url;
                    }
                    
                    public static class TranscodeUrlBean {
                        /**
                         * ld : http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/ld/21I8CBVjJgzadsi6/1540644036808.m3u8
                         * sd : http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/sd/Rk42uUrfflhU3t6H/1540644036808.m3u8
                         * hd : http://playback.teacherv.top/record_transcode/91haoke/2018/10/27/6220_1/6220_1_20181027204036/m3u8/hd/HMovhOi9sLjNnJxZ/1540644036808.m3u8
                         */
                        
                        private String ld;
                        private String sd;
                        private String hd;
                        
                        public String getLd() {
                            return ld;
                        }
                        
                        public void setLd(String ld) {
                            this.ld = ld;
                        }
                        
                        public String getSd() {
                            return sd;
                        }
                        
                        public void setSd(String sd) {
                            this.sd = sd;
                        }
                        
                        public String getHd() {
                            return hd;
                        }
                        
                        public void setHd(String hd) {
                            this.hd = hd;
                        }
                    }
                }
            }
            
            public static class SignedRoomMediasBean {
                /**
                 * index : 1
                 * HD_RTMP_AUTH_KEY : 1542091338-0-0-90eb84d4008017f69fca1a368956a45b
                 * paltformType : aliyun
                 * FLV_URL : http://live.91haoke.com/91haoke/4023_1.flv?auth_key=1542091338-0-0-a63ef7e2244e3b690a99868d8fc856ad
                 * HD_RTMP_URL : rtmp://live.91haoke.com/91haoke/4023_1_hd?auth_key=1542091338-0-0-90eb84d4008017f69fca1a368956a45b
                 * SD_RTMP_AUTH_KEY : 1542091338-0-0-eeec03ae46270ca4e1f8af072d293726
                 * FLV_AUTH_KEY : 1542091338-0-0-a63ef7e2244e3b690a99868d8fc856ad
                 * isMain : 1
                 * SD_RTMP_URL : rtmp://live.91haoke.com/91haoke/4023_1_sd?auth_key=1542091338-0-0-eeec03ae46270ca4e1f8af072d293726
                 * //声网
                 * agoraChannelKey："token"
                 * agoraAppID:""
                 * agoraChannel:"channel"
                 * agoraUid:1
                 * //拓课
                 * serial：教室号
                 * student_pwd:密码
                 *
                 *
                 */
                
                private int index;
                private String HD_RTMP_AUTH_KEY;
                private String paltformType;
                private String FLV_URL;
                private String HD_RTMP_URL;
                private String SD_RTMP_AUTH_KEY;
                private String FLV_AUTH_KEY;
                private String RTMP_URL;//和flv 都是原画
                private int isMain;
                private String SD_RTMP_URL;
                
                //拓课
                private String student_pwd;
                private String teacher_pwd;
                private String live_url;
                private String serial;//教室号
                //声网
                private String agoraChannelKey;
                private String agoraAppID;
                private String agoraChannel;
                private int agoraUid;
    
    
                public String getSerial(){
                    return serial;
                }
    
                public void setSerial(String serial){
                    this.serial = serial;
                }
    
                public String getStudent_pwd(){
                    return student_pwd;
                }
    
                public void setStudent_pwd(String student_pwd){
                    this.student_pwd = student_pwd;
                }
    
                public String getTeacher_pwd(){
                    return teacher_pwd;
                }
    
                public void setTeacher_pwd(String teacher_pwd){
                    this.teacher_pwd = teacher_pwd;
                }
    
                public String getLive_url(){
                    return live_url;
                }
    
                public void setLive_url(String live_url){
                    this.live_url = live_url;
                }
    
                public int getIndex() {
                    return index;
                }
                
                public void setIndex(int index) {
                    this.index = index;
                }
                
                public String getHD_RTMP_AUTH_KEY() {
                    return HD_RTMP_AUTH_KEY;
                }
                
                public void setHD_RTMP_AUTH_KEY(String HD_RTMP_AUTH_KEY) {
                    this.HD_RTMP_AUTH_KEY = HD_RTMP_AUTH_KEY;
                }
                
                public String getPaltformType() {
                    return paltformType;
                }
                
                public void setPaltformType(String paltformType) {
                    this.paltformType = paltformType;
                }
    
                public String getRTMP_URL() {
                    return RTMP_URL;
                }
    
                public void setRTMP_URL(String RTMP_URL) {
                    this.RTMP_URL = RTMP_URL;
                }
    
                public String getFLV_URL() {
                    return FLV_URL;
                }
                
                public void setFLV_URL(String FLV_URL) {
                    this.FLV_URL = FLV_URL;
                }
                
                public String getHD_RTMP_URL() {
                    return HD_RTMP_URL;
                }
                
                public void setHD_RTMP_URL(String HD_RTMP_URL) {
                    this.HD_RTMP_URL = HD_RTMP_URL;
                }
                
                public String getSD_RTMP_AUTH_KEY() {
                    return SD_RTMP_AUTH_KEY;
                }
                
                public void setSD_RTMP_AUTH_KEY(String SD_RTMP_AUTH_KEY) {
                    this.SD_RTMP_AUTH_KEY = SD_RTMP_AUTH_KEY;
                }
                
                public String getFLV_AUTH_KEY() {
                    return FLV_AUTH_KEY;
                }
                
                public void setFLV_AUTH_KEY(String FLV_AUTH_KEY) {
                    this.FLV_AUTH_KEY = FLV_AUTH_KEY;
                }
                
                public int getIsMain() {
                    return isMain;
                }
                
                public void setIsMain(int isMain) {
                    this.isMain = isMain;
                }
                
                public String getSD_RTMP_URL() {
                    return SD_RTMP_URL;
                }
                
                public void setSD_RTMP_URL(String SD_RTMP_URL) {
                    this.SD_RTMP_URL = SD_RTMP_URL;
                }
    
                public String getAgoraChannelKey(){
                    return agoraChannelKey;
                }
    
                public void setAgoraChannelKey(String agoraChannelKey){
                    this.agoraChannelKey = agoraChannelKey;
                }
    
                public String getAgoraAppID(){
                    return agoraAppID;
                }
    
                public void setAgoraAppID(String agoraAppID){
                    this.agoraAppID = agoraAppID;
                }
    
                public String getAgoraChannel(){
                    return agoraChannel;
                }
    
                public void setAgoraChannel(String agoraChannel){
                    this.agoraChannel = agoraChannel;
                }
    
                public int getAgoraUid(){
                    return agoraUid;
                }
    
                public void setAgoraUid(int agoraUid){
                    this.agoraUid = agoraUid;
                }
            }
        }
        
        public static class ServerConfigBean {
            /**
             * resourceCdn : //file1.teacherv.top/static/lmc/resources
             * heartbeatInterval : 300000
             */
            
            private String resourceCdn;
            private int heartbeatInterval;
            
            public String getResourceCdn() {
                return resourceCdn;
            }
            
            public void setResourceCdn(String resourceCdn) {
                this.resourceCdn = resourceCdn;
            }
            
            public int getHeartbeatInterval() {
                return heartbeatInterval;
            }
            
            public void setHeartbeatInterval(int heartbeatInterval) {
                this.heartbeatInterval = heartbeatInterval;
            }
        }
        
        public static class MediaConfigsBean {
            /**
             * id : 4076
             * isMain : 1
             * lastOptionTime : 1512210510000
             * lastUpdateTime : 1511243443000
             * livePalatform : aliyun
             * livePlatformParam : {"liveDomain":"live.91haoke.com","courseId":"4023_1","liveAppName":"91haoke"}
             * liveRoomId : 4023
             * liveStatus : end
             * mediaIndex : 1
             * name : 三年级迎春杯初赛试题讲解
             * realEndTime : 1512210510000
             * realStartTime : 1512204150000
             * sort : 1
             * status : 1
             * subGroupIds : ["ALL"]
             * users : ["a_11921","t_11921"]
             */
            
            private int id;
            private int isMain;
            private long lastOptionTime;
            private long lastUpdateTime;
            private String livePalatform;
            private LivePlatformParamBean livePlatformParam;
            private int liveRoomId;
            private String liveStatus;
            private int mediaIndex;
            private String name;
            private long realEndTime;
            private long realStartTime;
            private int sort;
            private int status;
            private List<String> subGroupIds;
            private List<String> users;
            
            public int getId() {
                return id;
            }
            
            public void setId(int id) {
                this.id = id;
            }
            
            public int getIsMain() {
                return isMain;
            }
            
            public void setIsMain(int isMain) {
                this.isMain = isMain;
            }
            
            public long getLastOptionTime() {
                return lastOptionTime;
            }
            
            public void setLastOptionTime(long lastOptionTime) {
                this.lastOptionTime = lastOptionTime;
            }
            
            public long getLastUpdateTime() {
                return lastUpdateTime;
            }
            
            public void setLastUpdateTime(long lastUpdateTime) {
                this.lastUpdateTime = lastUpdateTime;
            }
            
            public String getLivePalatform() {
                return livePalatform;
            }
            
            public void setLivePalatform(String livePalatform) {
                this.livePalatform = livePalatform;
            }
            
            public LivePlatformParamBean getLivePlatformParam() {
                return livePlatformParam;
            }
            
            public void setLivePlatformParam(LivePlatformParamBean livePlatformParam) {
                this.livePlatformParam = livePlatformParam;
            }
            
            public int getLiveRoomId() {
                return liveRoomId;
            }
            
            public void setLiveRoomId(int liveRoomId) {
                this.liveRoomId = liveRoomId;
            }
            
            public String getLiveStatus() {
                return liveStatus;
            }
            
            public void setLiveStatus(String liveStatus) {
                this.liveStatus = liveStatus;
            }
            
            public int getMediaIndex() {
                return mediaIndex;
            }
            
            public void setMediaIndex(int mediaIndex) {
                this.mediaIndex = mediaIndex;
            }
            
            public String getName() {
                return name;
            }
            
            public void setName(String name) {
                this.name = name;
            }
            
            public long getRealEndTime() {
                return realEndTime;
            }
            
            public void setRealEndTime(long realEndTime) {
                this.realEndTime = realEndTime;
            }
            
            public long getRealStartTime() {
                return realStartTime;
            }
            
            public void setRealStartTime(long realStartTime) {
                this.realStartTime = realStartTime;
            }
            
            public int getSort() {
                return sort;
            }
            
            public void setSort(int sort) {
                this.sort = sort;
            }
            
            public int getStatus() {
                return status;
            }
            
            public void setStatus(int status) {
                this.status = status;
            }
            
            public List<String> getSubGroupIds() {
                return subGroupIds;
            }
            
            public void setSubGroupIds(List<String> subGroupIds) {
                this.subGroupIds = subGroupIds;
            }
            
            public List<String> getUsers() {
                return users;
            }
            
            public void setUsers(List<String> users) {
                this.users = users;
            }
            
            public static class LivePlatformParamBean {
                /**
                 * liveDomain : live.91haoke.com
                 * courseId : 4023_1
                 * liveAppName : 91haoke
                 */
                
                private String liveDomain;
                private String courseId;
                private String liveAppName;
                
                public String getLiveDomain() {
                    return liveDomain;
                }
                
                public void setLiveDomain(String liveDomain) {
                    this.liveDomain = liveDomain;
                }
                
                public String getCourseId() {
                    return courseId;
                }
                
                public void setCourseId(String courseId) {
                    this.courseId = courseId;
                }
                
                public String getLiveAppName() {
                    return liveAppName;
                }
                
                public void setLiveAppName(String liveAppName) {
                    this.liveAppName = liveAppName;
                }
            }
        }
    }
}
