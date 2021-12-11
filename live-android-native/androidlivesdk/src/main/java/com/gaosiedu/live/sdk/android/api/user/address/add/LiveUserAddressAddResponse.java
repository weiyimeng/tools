/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.address.add;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/30 19:10
 * @since 2.1.0
 */
public class LiveUserAddressAddResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * address
        */
        private  String address;
        /**
        * area
        */
        private  String area;
        /**
        * areaCode
        */
        private  int areaCode;
        /**
        * city
        */
        private  String city;
        /**
        * cityCode
        */
        private  int cityCode;
        /**
        * contactMobile
        */
        private  String contactMobile;
        /**
        * contactPeople
        */
        private  String contactPeople;
        /**
        * createTime
        */
        private  String createTime;
        /**
        * general
        */
        private  String general;
        /**
        * id
        */
        private  int id;
        /**
        * province
        */
        private  String province;
        /**
        * provinceCode
        */
        private  int provinceCode;
        /**
        * status
        */
        private  int status;
        /**
        * type
        */
        private  int type;
        /**
        * updateTime
        */
        private  String updateTime;
        /**
        * userId
        */
        private  int userId;
        /**
        * zipCode
        */
        private  String zipCode;

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



        public int getAreaCode() {
        return this.areaCode;
        }

        public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
        }



        public String getCity() {
        return this.city;
        }

        public void setCity(String city) {
        this.city = city;
        }



        public int getCityCode() {
        return this.cityCode;
        }

        public void setCityCode(int cityCode) {
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



        public String getCreateTime() {
        return this.createTime;
        }

        public void setCreateTime(String createTime) {
        this.createTime = createTime;
        }



        public String getGeneral() {
        return this.general;
        }

        public void setGeneral(String general) {
        this.general = general;
        }



        public int getId() {
        return this.id;
        }

        public void setId(int id) {
        this.id = id;
        }



        public String getProvince() {
        return this.province;
        }

        public void setProvince(String province) {
        this.province = province;
        }



        public int getProvinceCode() {
        return this.provinceCode;
        }

        public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
        }



        public int getStatus() {
        return this.status;
        }

        public void setStatus(int status) {
        this.status = status;
        }



        public int getType() {
        return this.type;
        }

        public void setType(int type) {
        this.type = type;
        }



        public String getUpdateTime() {
        return this.updateTime;
        }

        public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        }



        public int getUserId() {
        return this.userId;
        }

        public void setUserId(int userId) {
        this.userId = userId;
        }



        public String getZipCode() {
        return this.zipCode;
        }

        public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
        }

    }
}
