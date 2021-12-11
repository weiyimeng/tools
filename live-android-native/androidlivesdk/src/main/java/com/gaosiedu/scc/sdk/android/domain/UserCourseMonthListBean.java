package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangjia on 2018/8/30.
 */
public class UserCourseMonthListBean implements Serializable{
    List<UserCourseMonthBean> list;

    public List<UserCourseMonthBean> getList() {
        return list;
    }

    public void setList(List<UserCourseMonthBean> list) {
        this.list = list;
    }
}
