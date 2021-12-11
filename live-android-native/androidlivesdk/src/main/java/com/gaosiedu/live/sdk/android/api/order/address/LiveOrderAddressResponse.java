/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.order.address;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/29 19:15
 * @since 2.1.0
 */
public class LiveOrderAddressResponse extends ResponseResult  {

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
        private  boolean areaCode;
        /**
        * city
        */
        private  String city;
        /**
        * cityCode
        */
        private  boolean cityCode;
        /**
        * contactNumber
        */
        private  String contactNumber;
        /**
        * contacts
        */
        private  String contacts;
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
        private  boolean id;
        /**
        * ordersId
        */
        private  boolean ordersId;
        /**
        * province
        */
        private  String province;
        /**
        * provinceCode
        */
        private  boolean provinceCode;
        /**
        * type
        */
        private  boolean type;
        /**
        * userId
        */
        private  boolean userId;
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



        public boolean getAreaCode() {
        return this.areaCode;
        }

        public void setAreaCode(boolean areaCode) {
        this.areaCode = areaCode;
        }



        public String getCity() {
        return this.city;
        }

        public void setCity(String city) {
        this.city = city;
        }



        public boolean getCityCode() {
        return this.cityCode;
        }

        public void setCityCode(boolean cityCode) {
        this.cityCode = cityCode;
        }



        public String getContactNumber() {
        return this.contactNumber;
        }

        public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        }



        public String getContacts() {
        return this.contacts;
        }

        public void setContacts(String contacts) {
        this.contacts = contacts;
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



        public boolean getId() {
        return this.id;
        }

        public void setId(boolean id) {
        this.id = id;
        }



        public boolean getOrdersId() {
        return this.ordersId;
        }

        public void setOrdersId(boolean ordersId) {
        this.ordersId = ordersId;
        }



        public String getProvince() {
        return this.province;
        }

        public void setProvince(String province) {
        this.province = province;
        }



        public boolean getProvinceCode() {
        return this.provinceCode;
        }

        public void setProvinceCode(boolean provinceCode) {
        this.provinceCode = provinceCode;
        }



        public boolean getType() {
        return this.type;
        }

        public void setType(boolean type) {
        this.type = type;
        }



        public boolean getUserId() {
        return this.userId;
        }

        public void setUserId(boolean userId) {
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
