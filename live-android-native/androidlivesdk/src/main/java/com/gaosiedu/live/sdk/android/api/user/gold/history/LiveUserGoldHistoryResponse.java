/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.gold.history;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.live.sdk.android.domain.*;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/05 10:12
 * @since 2.1.0
 */
public class LiveUserGoldHistoryResponse extends ResponseResult  {

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
    public static class ListData{
        /**
        * addressId
        */
        private  int addressId;
        /**
        * count
        */
        private  int count;
        /**
        * createTime
        */
        private  String createTime;
        /**
        * gold
        */
        private  int gold;
        /**
        * goldProductDomain
        */
        private  GoldProductDomain goldProductDomain;
        /**
        * goldProductId
        */
        private  int goldProductId;
        /**
        * id
        */
        private  int id;
        /**
        * shopingStatus
        */
        private  int shopingStatus;
        /**
        * source
        */
        private  String source;
        /**
        * sourceDesc
        */
        private  String sourceDesc;
        /**
        * type
        */
        private  int type;

        //属性get||set方法


        public int getAddressId() {
        return this.addressId;
        }

        public void setAddressId(int addressId) {
        this.addressId = addressId;
        }



        public int getCount() {
        return this.count;
        }

        public void setCount(int count) {
        this.count = count;
        }



        public String getCreateTime() {
        return this.createTime;
        }

        public void setCreateTime(String createTime) {
        this.createTime = createTime;
        }



        public int getGold() {
        return this.gold;
        }

        public void setGold(int gold) {
        this.gold = gold;
        }



        public GoldProductDomain getGoldProductDomain() {
        return this.goldProductDomain;
        }

        public void setGoldProductDomain(GoldProductDomain goldProductDomain) {
        this.goldProductDomain = goldProductDomain;
        }



        public int getGoldProductId() {
        return this.goldProductId;
        }

        public void setGoldProductId(int goldProductId) {
        this.goldProductId = goldProductId;
        }



        public int getId() {
        return this.id;
        }

        public void setId(int id) {
        this.id = id;
        }



        public int getShopingStatus() {
        return this.shopingStatus;
        }

        public void setShopingStatus(int shopingStatus) {
        this.shopingStatus = shopingStatus;
        }



        public String getSource() {
        return this.source;
        }

        public void setSource(String source) {
        this.source = source;
        }



        public String getSourceDesc() {
        return this.sourceDesc;
        }

        public void setSourceDesc(String sourceDesc) {
        this.sourceDesc = sourceDesc;
        }



        public int getType() {
        return this.type;
        }

        public void setType(int type) {
        this.type = type;
        }

    }
}
