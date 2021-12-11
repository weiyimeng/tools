package com.haoke91.a91edu;

import android.os.Environment;

import com.blankj.utilcode.util.SDCardUtils;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/24 下午6:02
 * 修改人：weiyimeng
 * 修改时间：2018/7/24 下午6:02
 * 修改备注：
 */
public class GlobalConfig {
    public static String ROOT = "91live";
    
    //sd 根目录
    public static String mLocalExternalPath = SDCardUtils.getSDCardPathByEnvironment();
    //项目根目录
    public static String defaultRootPath = mLocalExternalPath.concat("/").concat(ROOT);
    public static String defaultCache = defaultRootPath.concat("/cache/");
    public static String defaultImage = defaultRootPath.concat("/images/");
    public static String defaultLog = defaultRootPath.concat("/logs/");
    public static String album = mLocalExternalPath.concat("/AlbumCache/");
    public static final String IMGURL_PRE = "https://img.91haoke.com/upload/";
    public static final String COURSE_LIST = "COURSE_LIST";//科目列表
    public static final String TERM_LIST = "TERM_LIST";//学期列表
    
    
    public static final String OSSIMGPATH = "upload/v30/user/";//oss头像上传路径
    public static final String OSSHOMEWORKPATH = "upload/v30/exercise/resource/";//oss作業上傳路徑
    public static final String CARD_PATH = "upload/order/return/idCard/";
    public static final String APP_ATTACHE_TIME = "APP_ATTACHE_TIME";
    
    
    /**
     * 学期
     */
    public static final int TERM_SPRING = 1;
    public static final int TERM_SUMMER = 2;
    public static final int TERM_AUTUMN = 3;
    public static final int TERM_WINTER = 4;
    
    /**
     * 直播状态
     */
    public static final String LIVE_COMPLETED = "complete";//直播已结束
    public static final String LIVE_LIVING = "living";//直播中
    public static final String LIVE_WAITING = "wating";//直播待开始
    
    /**
     * 课程状态  -1：退班，1：未完结，2：已完结  4 开课中
     */
    public static final int COURSE_STATUS_CANCEL = -1;
    public static final int COURSE_STATUS_UNCOMPLETED = 1;
    public static final int COURSE_STATUSS_COMPLETED = 2;
    public static final int COURSE_STATUS_RUNNING = 4;
    
    
}
