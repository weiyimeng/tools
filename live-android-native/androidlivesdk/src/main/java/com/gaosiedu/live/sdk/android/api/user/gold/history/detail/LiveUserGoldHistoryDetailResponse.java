/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.gold.history.detail;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/10/12 11:50
 * @since 2.1.0
 */
public class LiveUserGoldHistoryDetailResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * 收件人地址
        */
        private  String address;
        /**
        * 收件人名称
        */
        private  String contactName;
        /**
        * 收件人手机号
        */
        private  String contactPhone;
        /**
        * 花费的金币
        */
        private  int costGold;
        /**
        * 商品数
        */
        private  int count;
        /**
        * 兑换时间
        */
        private  String exchangeTime;
        /**
        * 商品图片
        */
        private  String productIco;
        /**
        * 商品名
        */
        private  String productName;
        /**
        * 发货状态0 未发货，1已发货
        */
        private  int shopingStatus;
        /**
        * 发货时间
        */
        private  String shopingTime;
        /**
        * 快递编号
        */
        private  String trackNo;
        /**
        * 快递公司
        */
        private  String trackingCompany;
        /**
        * 类型，1表示实体，2表示虚拟
        */
        private  int type;

        //属性get||set方法


        public String getAddress() {
        return this.address;
        }

        public void setAddress(String address) {
        this.address = address;
        }



        public String getContactName() {
        return this.contactName;
        }

        public void setContactName(String contactName) {
        this.contactName = contactName;
        }



        public String getContactPhone() {
        return this.contactPhone;
        }

        public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
        }



        public int getCostGold() {
        return this.costGold;
        }

        public void setCostGold(int costGold) {
        this.costGold = costGold;
        }



        public int getCount() {
        return this.count;
        }

        public void setCount(int count) {
        this.count = count;
        }



        public String getExchangeTime() {
        return this.exchangeTime;
        }

        public void setExchangeTime(String exchangeTime) {
        this.exchangeTime = exchangeTime;
        }



        public String getProductIco() {
        return this.productIco;
        }

        public void setProductIco(String productIco) {
        this.productIco = productIco;
        }



        public String getProductName() {
        return this.productName;
        }

        public void setProductName(String productName) {
        this.productName = productName;
        }



        public int getShopingStatus() {
        return this.shopingStatus;
        }

        public void setShopingStatus(int shopingStatus) {
        this.shopingStatus = shopingStatus;
        }



        public String getShopingTime() {
        return this.shopingTime;
        }

        public void setShopingTime(String shopingTime) {
        this.shopingTime = shopingTime;
        }



        public String getTrackNo() {
        return this.trackNo;
        }

        public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
        }



        public String getTrackingCompany() {
        return this.trackingCompany;
        }

        public void setTrackingCompany(String trackingCompany) {
        this.trackingCompany = trackingCompany;
        }



        public int getType() {
        return this.type;
        }

        public void setType(int type) {
        this.type = type;
        }

    }
}
