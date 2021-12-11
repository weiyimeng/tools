/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.gold.product;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.live.sdk.android.domain.*;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/05 10:12
 * @since 2.1.0
 */
public class LiveUserGoldListResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData extends LiveSdkBasePageResponse {

        private List<ListData> list;

        public List<ListData> getList() {
            return list;
        }

        public void setList(List<ListData> list) {
            this.list = list;
        }

    }
    public static class ListData implements Serializable{
        /**
        * count
        */
        private  int count;
        /**
        * coverImg
        */
        private  String coverImg;
        /**
        * createTime
        */
        private  String createTime;
        /**
        * exchangeCount
        */
        private  int exchangeCount;
        /**
        * flag
        */
        private  int flag;
        /**
        * gold
        */
        private  int gold;
        /**
        * icoImg
        */
        private  String icoImg;
        /**
        * id
        */
        private  int id;
        /**
        * info
        */
        private  String info;
        /**
        * infoMobile
        */
        private  String infoMobile;
        /**
        * name
        */
        private  String name;
        /**
        * oldPrice
        */
        private  BigDecimal oldPrice;
        /**
        * price
        */
        private  BigDecimal price;
        /**
        * productDesc
        */
        private  String productDesc;
        /**
        * remark
        */
        private  String remark;
        /**
        * status
        */
        private  int status;
        /**
        * type
        */
        private  int type;
        /**
        * upTime
        */
        private  String upTime;

        //属性get||set方法


        public int getCount() {
        return this.count;
        }

        public void setCount(int count) {
        this.count = count;
        }



        public String getCoverImg() {
        return this.coverImg;
        }

        public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
        }



        public String getCreateTime() {
        return this.createTime;
        }

        public void setCreateTime(String createTime) {
        this.createTime = createTime;
        }



        public int getExchangeCount() {
        return this.exchangeCount;
        }

        public void setExchangeCount(int exchangeCount) {
        this.exchangeCount = exchangeCount;
        }



        public int getFlag() {
        return this.flag;
        }

        public void setFlag(int flag) {
        this.flag = flag;
        }



        public int getGold() {
        return this.gold;
        }

        public void setGold(int gold) {
        this.gold = gold;
        }



        public String getIcoImg() {
        return this.icoImg;
        }

        public void setIcoImg(String icoImg) {
        this.icoImg = icoImg;
        }



        public int getId() {
        return this.id;
        }

        public void setId(int id) {
        this.id = id;
        }



        public String getInfo() {
        return this.info;
        }

        public void setInfo(String info) {
        this.info = info;
        }



        public String getInfoMobile() {
        return this.infoMobile;
        }

        public void setInfoMobile(String infoMobile) {
        this.infoMobile = infoMobile;
        }



        public String getName() {
        return this.name;
        }

        public void setName(String name) {
        this.name = name;
        }



        public BigDecimal getOldPrice() {
        return this.oldPrice;
        }

        public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
        }



        public BigDecimal getPrice() {
        return this.price;
        }

        public void setPrice(BigDecimal price) {
        this.price = price;
        }



        public String getProductDesc() {
        return this.productDesc;
        }

        public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
        }



        public String getRemark() {
        return this.remark;
        }

        public void setRemark(String remark) {
        this.remark = remark;
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



        public String getUpTime() {
        return this.upTime;
        }

        public void setUpTime(String upTime) {
        this.upTime = upTime;
        }

    }
}
