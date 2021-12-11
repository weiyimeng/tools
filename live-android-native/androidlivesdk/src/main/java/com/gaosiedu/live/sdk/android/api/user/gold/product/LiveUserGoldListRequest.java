/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.gold.product;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/05 10:12
* @since 2.1.0
*/
public class LiveUserGoldListRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "user/gold/product";

    public LiveUserGoldListRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 表示按兑换的金额的大小排序，1表示为降序，0表示为升序
     */
    private Integer gold;

    /**
     * 表示按时间排序，1表示降序，0表示升序
     */
    private Integer time;

    /**
     * 1表示实物，2表示虚拟物品
     */
    private Integer type;

    /**
     * 1表示可以兑换的，当用1时注意必须要传userId
     */
    private Integer usable;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public Integer getGold() {
        return this.gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public Integer getTime() {
        return this.time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUsable() {
        return this.usable;
    }

    public void setUsable(Integer usable) {
        this.usable = usable;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}