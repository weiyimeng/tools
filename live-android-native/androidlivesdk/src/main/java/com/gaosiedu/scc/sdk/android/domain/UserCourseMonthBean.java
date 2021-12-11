package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;

/**
 * Created by wangjia on 2018/8/30.
 */
public class UserCourseMonthBean implements Serializable {

    /** 参与日期 */
    private int hasDay;
    /** 兼容老接口，这个不用 */
    private int totalCount;
    /** 参与次数。 */
    private int joinNum;
    /** 兼容老接口，这个不用 */
    private String joinCourseIds;

    public int getHasDay() {
        return hasDay;
    }

    public void setHasDay(int hasDay) {
        this.hasDay = hasDay;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(int joinNum) {
        this.joinNum = joinNum;
    }

    public String getJoinCourseIds() {
        return joinCourseIds;
    }

    public void setJoinCourseIds(String joinCourseIds) {
        this.joinCourseIds = joinCourseIds;
    }
}
