package com.gstudentlib.event;

import java.io.Serializable;

/**
 * EventBus
 */
public final class EventType implements Serializable {

    //宝箱领金币
    public static final int GET_GLOD_COINS = 0x01;

    //大乱斗加金币
    public static final int GOLD_UPDATE = 0x02;

    //点击进入单个题号
    public static final int BACK_LOOK_UP_HOMEWORK = 0x03;

    //作业提交成功
    public static final int SUBMIT_VOICE_HOMEWORK_SUCCESS = 0x04;

    //英语语音砸蛋成功
    public static final int SMASHING_EGG_SUCCESS = 0x05;

    //通知全部作业我点击过出门考了（出门考全对刷新页面）
    public static final int HOMEWORK_ALL_RIGHT = 0x06;

    //点击进入相应题号的做题页面 HomeWorkActivity
    public static final int GO_TO_HOMEWORK = 0x07;

    //答题卡页提交作业 native
    public static final int SUBMIT_HOMEWROK = 0x08;

    //解析页去订正页 订正正确
    public static final int PARSING_TO_CORRECT_SUTMIT_SUCCESS = 0x09;

    //解析页去订正页 订正错误
    public static final int PARSING_TO_CORRECT_SUTMIT_FAIL = 0x03;

    //解析页去订正页 单题目自评正确
    public static final int PARSING_TO_CORRECT_TO_ZIPING_SUCCESS = 0x10;

    //解析页去订正页 单题目自评页面通知砸蛋
    public static final int PARSING_TO_CORRECT_TO_ZIPING_TO_ZADAN = 0x12;

    //讲次详情 点击切换讲次
    public static final int CLICK_CHANGE_LESSON = 0x13;
    
    //完成预习奖励
    public static final int GET_REWARD = 0x167;

    //抽取装扮成功
    public static final int CHOOSE_DRESS = 0x14;

    //合成装扮成功
    public static final int MERGE_DRESS = 0x18;

    //激活码验证成功
    public static final int CLASS_ACTIVATE_SUCCESS = 0x15;

    //显示键盘
    public static final int SHOW_SUBJECT_KEYBOARD = 0x16;

    //隐藏键盘
    public static final int HIDE_SUBJECT_KEYBOARD = 0x17;

    //情景对话完成
    public static final int AI_SCENE_SUCCESS = 0X18;

    //进入1v1直播间
    public static final int GO_TO_1V1_ROOM = 0X19;
}
