package com.eduhdsdk.tools;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/12/27 17:03
 */
public interface TKCallBack {
    int DESTORY_PLAYER=0x11;
    int ONROOMJOIN=0x12;
    void call(int flag);
}
