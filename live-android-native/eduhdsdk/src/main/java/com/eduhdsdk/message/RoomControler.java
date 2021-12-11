package com.eduhdsdk.message;

import android.text.TextUtils;

public class RoomControler {

    public static String chairmanControl = "1111111001101111101000010000000000000000";

    /***
     * 是否自动下课
     */
    // && TextUtils.isEmpty(TKRoomManager.recordfilepath)
    public static boolean isAutoClassDissMiss() {
        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 7) {
            return chairmanControl.charAt(7) == '1';
        } else {
            return false;
        }
    }

    /***
     * 上课后是否自动上台
     */
    public static boolean isAutomaticUp() {
        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 23) {
            return chairmanControl.charAt(23) == '1';
        } else {
            return false;
        }
    }

    /***
     * 是否自动上课
     */
    public static boolean isAutoClassBegin() {

        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 32) {
            return chairmanControl.charAt(32) == '1';
        } else {
            return false;
        }
    }

    /***
     * 允许学生自己控制音视频
     */
    public static boolean isAllowStudentControlAV() {

        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 33) {
            return chairmanControl.charAt(33) == '1';
        } else {
            return false;
        }
    }

    /***
     * 是否显示上下课按钮
     */
    public static boolean isShowClassBeginButton() {

        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 34) {
            return chairmanControl.charAt(34) == '1';
        } else {
            return false;
        }
    }

    /***
     * 是否有画笔权限
     * @return
     */
    public static boolean isAutoHasDraw() {
        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 37) {
            return chairmanControl.charAt(37) == '1';
        } else {
            return false;
        }
    }

    /***
     * 是否允许助教开启音视频
     */
    public static boolean isShowAssistantAV() {
        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 36) {
            return chairmanControl.charAt(36) == '1';
        } else {
            return false;
        }

    }

    /***
     * 学生是否可以翻页
     * @return
     */
    public static boolean isStudentCanTurnPage() {
        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 38) {
            return chairmanControl.charAt(38) == '1';
        } else {
            return false;
        }
    }

    /***
     * 上课前是否发布音视频
     */
    public static boolean isReleasedBeforeClass() {

        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 41) {
            return chairmanControl.charAt(41) == '1';
        } else {
            return false;
        }
    }

    /***
     * 下课后是否不离开课堂
     */
    public static boolean isNotLeaveAfterClass() {

        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 47) {
            return chairmanControl.charAt(47) == '1';
        } else {
            return false;
        }
    }

    /***
     * 是否有视频标注
     */
    public static boolean isShowVideoWhiteBoard() {

        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 48) {

            return chairmanControl.charAt(48) == '1';
        } else {
            return false;
        }
    }

    /***
     * 是否不自动关闭视频播放器
     */
    //&& TextUtils.isEmpty(TKRoomManager.recordfilepath)
    public static boolean isNotCloseVideoPlayer() {

        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 52) {
            return chairmanControl.charAt(52) == '1';
        } else {
            return false;
        }
    }

    /***
     * 是否有文档分类
     */
    public static boolean isDocumentClassification() {

        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 56) {
            return chairmanControl.charAt(56) == '1';
        } else {
            return false;
        }
    }

    /***
     * 下课后是否有时间节点退出教室
     */
    public static boolean haveTimeQuitClassroomAfterClass() {

        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 71) {
            return chairmanControl.charAt(71) == '1';
        } else {
            return false;
        }
    }

    /***
     * 是否自定义奖杯
     */
    public static boolean isCustomTrophy() {

        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 44) {
            return chairmanControl.charAt(44) == '1';
        } else {
            return false;
        }
    }

    /***
     * 是否自定义白板底色
     */
    public static boolean isCustomizeWhiteboard() {

        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 81) {
            return chairmanControl.charAt(81) == '1';
        } else {
            return false;
        }
    }

    /***
     *
     * @return 巡课取消点击下课按钮
     */
    public static boolean patrollerCanClassDismiss() {
        if (chairmanControl != null && chairmanControl.length() > 78) {
            return chairmanControl.charAt(78) == '1';
        } else {
            return false;
        }
    }

    /***
     *
     * @return 课件或MP4全屏后video放置右下角(画中画效果)
     */
    public static boolean isFullScreenVideo() {
        if (chairmanControl != null && chairmanControl.length() > 50) {
            return chairmanControl.charAt(50) == '1';
        } else {
            return false;
        }
    }

    /***
     *  学生视频顺序统一 （ture 有顺序）
     */
    public static boolean isStudentVideoSequence() {

        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 116) {
            return chairmanControl.charAt(116) == '1';
        } else {
            return false;
        }
    }

    /***
     *   只显示老师和自己视频 （true 是）
     */
    public static boolean isOnlyShowTeachersAndVideos() {
        if (!TextUtils.isEmpty(chairmanControl) && chairmanControl.length() > 119) {
            return chairmanControl.charAt(119) == '1';
        } else {
            return false;
        }
    }
}
