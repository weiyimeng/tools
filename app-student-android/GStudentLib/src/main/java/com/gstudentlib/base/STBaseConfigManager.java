package com.gstudentlib.base;

import android.text.TextUtils;

import com.gsbaselib.base.GSBaseConfigManager;
import com.gsbaselib.base.log.LogUtil;
import com.gsbaselib.utils.SharedPreferenceUtil;

/**
 * GSBaseConfig App配置信息类
 * Created by test on 2018/9/6.
 */
public abstract class STBaseConfigManager extends GSBaseConfigManager {

    //开发环境
    protected static final String DEV_SERVER = "https://c.dev.aixuexi.com/";
    //正式环境
    protected static final String RELEASE_SERVER = "https://c.aixuexi.com/";

    //Tag
    protected static final String Tag = "GSConfig";
    protected static final String UPDATE = "UPDATE";
    protected static final String SERVER = "SERVER";
    protected static final String H5_SERVER_IP = "H5_SERVER_IP";
    protected static final String HOMEWORK_SERVER_IP = "HOMEWORK_SERVER_IP";
    protected static final String MOCK_SERVER_IP = "MOCK_SERVER_IP";

    //业务服务器地址 目前会有很多服务端吗？为什么和更新不一样？
    public static String BASE_DEFAULT_SERVER;
    public static String DEFAULT_SERVER;

    //在线H5的IP地址，如果使用在线H5资源，需要设置H5开发者的ip地址，才能使用最新的H5资源
    protected static final String h5ServerIp = "http://omjm32dtk.bkt.clouddn.com/";

    //使用在线H5
    private boolean useOnlineH5;
    //使用在线交作业
    private boolean userOnLineHomework;

    //是否使用自定义服务器
    private boolean userServer;

    /**
     * 设置自定义复选框状态
     * @param userServer
     */
    public void setUserServer(boolean userServer) {
        this.userServer = userServer;
    }

    /**
     * 设置使用在线h5复选框状态
     * @param useOnlineH5
     */
    public void setUseOnlineH5(boolean useOnlineH5) {
        this.useOnlineH5 = useOnlineH5;
    }

    /**
     * 是否使用在线h5
     * @return
     */
    @Override
    public boolean getUseOnlineH5() {
        return useOnlineH5;
    }

    /**
     * 设置使用在线作业复选框状态
     * @param userOnLineHomework
     */
    public void setUserOnLineHomework(boolean userOnLineHomework) {
        this.userOnLineHomework = userOnLineHomework;
    }

    /**
     * 是否使用在线作业调试
     * @return
     */
    public boolean getUseOnlineHomework() {
        return userOnLineHomework;
    }

    /**
     * 初始化公共参数
     */
    public abstract void init();

    /**
     * 更新服务地址
     * @param server
     */
    public void updateServer(String server) {
        BASE_DEFAULT_SERVER = server;
    }

    /**
     * 更新服务器地址
     * 通过选择dev or release设置服务器地址
     * @param type
     */
    public void updateServerType(String type) {
        if ("release".equalsIgnoreCase(type)) {
            BASE_DEFAULT_SERVER = RELEASE_SERVER;
        } else {
            BASE_DEFAULT_SERVER = DEV_SERVER;
        }
    }

    /**
     * 点击保存时候将h5调试Ip更新到本地持久化
     * @param serverIp
     */
    public void updateH5ServerIp(String serverIp) {
        if (TextUtils.isEmpty(serverIp)) {
            return;
        }
        SharedPreferenceUtil.setStringDataIntoSP(Tag, H5_SERVER_IP, serverIp);
    }

    /**
     * 点击保存时候将h5作业调试Ip更新到本地持久化
     * @param serverIp
     */
    @Override
    public void updateWebviewIp(String serverIp) {
        if (TextUtils.isEmpty(serverIp)) {
            return;
        }
        SharedPreferenceUtil.setStringDataIntoSP(Tag, HOMEWORK_SERVER_IP, serverIp);
    }

    /**
     * 点击保存时候将mock Ip更新到本地持久化
     * @param ip
     */
    public void updateMockServerIp(String ip) {
        SharedPreferenceUtil.setStringDataIntoSP(Tag, MOCK_SERVER_IP, ip);
    }

    /**
     * 获取h5通信地址ip
     * @return
     */
    public String getH5ServerUrl() {
        String url = h5ServerIp;
        if (useOnlineH5) {
            String ip = SharedPreferenceUtil.getStringValueFromSP(Tag, H5_SERVER_IP, "http://10.39.2.10:8080/");
            url = ip;
        }
        return url;
    }

    /**
     * 获取作业h5通信地址ip
     * @return
     */
    @Override
    public String getHomeworkUrl() {
        String url = h5ServerIp;
        if (userOnLineHomework) {
            String ip = SharedPreferenceUtil.getStringValueFromSP(Tag, HOMEWORK_SERVER_IP, h5ServerIp);
            url = ip;
        }

        return url;
    }

    /**
     * 判断当前连接的Server环境是不是线上环境
     * @return 是不是线上环境
     */
    public boolean isReleaseServer() {
        return TextUtils.equals(BASE_DEFAULT_SERVER, RELEASE_SERVER);
    }

    /**
     * 得到H5模块的运行环境的类型，现在由原生控制H5模块的运行环境
     * 原生如果是release、releaseTest、monkeyRelease包，H5模块使用api.aixuexi.com环境，类型是release
     * 否则，H5模块使用api.dev.aixuexi.com环境，类型是debug
     *
     * @return H5模块的运行环境的类型
     */
    public String getH5EnvironmentType() {
        String type = "debug";
        if (TextUtils.equals(BASE_DEFAULT_SERVER, RELEASE_SERVER)) {
            type = "release";
        }

        return type;
    }

    /**
     * 得到当前业务服务器地址
     *
     * @return 业务服务器地址
     */
    public String getBaseUrl() {
        if(!userServer) {
            BASE_DEFAULT_SERVER = DEFAULT_SERVER;
        }
        String url = BASE_DEFAULT_SERVER;
        LogUtil.d("getBaseUrl" + url);
        return url;
    }

    /**
     * 是否使用自定义服务器
     * @return
     */
    @Override
    public boolean getUserServer() {
        return userServer;
    }

    /**
     * mock服务器地址
     * @return
     */
    public String getMockServerIp() {
        return SharedPreferenceUtil.getStringValueFromSP(Tag, MOCK_SERVER_IP, "c.aixuexi.com");
    }

    /**
     * 在线h5调试 IP
     * @return
     */
    public String getH5ServerIp() {
        return SharedPreferenceUtil.getStringValueFromSP(Tag, H5_SERVER_IP, h5ServerIp);
    }

    /**
     * 在线作业调试 IP
     * @return
     */
    public String getHomeworkIp() {
        return SharedPreferenceUtil.getStringValueFromSP(Tag, HOMEWORK_SERVER_IP, h5ServerIp);
    }

    /**
     * 获取服务类型
     * release、debug
     * @return
     */
    public String getServerType() {
        String serverType = "debug";
        if (TextUtils.equals(BASE_DEFAULT_SERVER, RELEASE_SERVER)) {
            serverType = "release";
        }
        return serverType;
    }
}
