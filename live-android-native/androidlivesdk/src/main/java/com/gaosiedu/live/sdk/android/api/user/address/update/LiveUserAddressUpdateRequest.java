/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.address.update;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/31 12:05
* @since 2.1.0
*/
public class LiveUserAddressUpdateRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/address/update";

    public LiveUserAddressUpdateRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 地址
     */
    private String address;

    /**
     * 区
     */
    private String area;

    /**
     * 地区编码
     */
    private Integer areaCode;

    /**
     * 市
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
     * 地址概况
     */
    private String general;

    /**
     * 收货地址id
     */
    private Integer id;

    /**
     * 省
     */
    private String province;

    /**
     * 省编码
     */
    private Integer provinceCode;

    /**
     * 是否删除，0表示删除  else 1
     */
    private Integer status;

    /**
     * 是否为默认地址，1表示为默认收货地址，0表示为非默认地址
     */
    private Integer type;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 邮政编码
     */
    private String zipCode;


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

    public String getGeneral() {
        return this.general;
    }

    public void setGeneral(String general) {
        this.general = general;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

}
