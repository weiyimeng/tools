package com.haoke91.videolibrary;

import android.app.Application;

/**
 * 项目名称：91edu
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/5/8 上午10:31
 * 修改人：weiyimeng
 * 修改时间：2018/5/8 上午10:31
 * 修改备注：
 */
public class VideoApp {
    private static Application app;
    
    public static Application getApp() {
        return app;
    }
    
    public static void setApp(Application app) {
        VideoApp.app = app;
    }
}
