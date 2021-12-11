/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.address.add;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/30 19:10
* @since 2.1.0
*/
public class LiveUserAddressAddRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/address/add";

    public LiveUserAddressAddRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 地址详情
     */
    private String address;

    /**
     * 区名称
     */
    private String area;

    /**
     * 区编码
     */
    private Integer areaCode;

    /**
     * 市名称
     */
    private String city;

    /**
     * 市编码
     */
    private Integer cityCode;

    /**
     * 联系电话
     */
    private String contactMobile;

    /**
     * 联系人
     */
    private String contactPeople;

    /**
     * 省名称
     */
    private String province;

    /**
     * 省编码
     */
    private Integer provinceCode;

    /**
     * 是否为默认地址 1为默认 0 为普通收货地址
     */
    private String type;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCityCode() {
        return this.cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    public String getContactMobile() {
        return this.contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactPeople() {
        return this.contactPeople;
    }

    public void setContactPeople(String contactPeople) {
        this.contactPeople = contactPeople;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getProvinceCode() {
        return this.provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}