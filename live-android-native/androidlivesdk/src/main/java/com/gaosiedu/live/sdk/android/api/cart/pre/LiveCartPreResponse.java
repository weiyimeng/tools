/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.cart.pre;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**   返回值
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/12/06 13:56
 * @since 2.1.0
 */
public class LiveCartPreResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * 活动优惠金额
        */
        private  BigDecimal activityAmount;
        /**
        * 购物车参加的活动列表
        */
//        private  List<CourseActivityDomain> activityDomains;
        /**
        * 订单参加活动拼串显示
        */
        private  String activityStr;
        /**
        * 购物车总金额
        */
        private  BigDecimal cartAmount;
        /**
        * 应付金额
        */
        private  BigDecimal dueAmount;
        /**
        * 能否下单  false  可以
        */
        private  Boolean notCanCreateOrder;
        /**
        * 购物车列表
        */
//        private  List<ShoppingCarDomain> shoppingCarDomains;

        //属性get||set方法


        public BigDecimal getActivityAmount() {
        return this.activityAmount;
        }

        public void setActivityAmount(BigDecimal activityAmount) {
        this.activityAmount = activityAmount;
        }




//        public List<CourseActivityDomain> getActivityDomains() {
//            return this.activityDomains;
//        }
//
//        public void setActivityDomains(List<CourseActivityDomain> activityDomains) {
//            this.activityDomains = activityDomains;
//        }



        public String getActivityStr() {
        return this.activityStr;
        }

        public void setActivityStr(String activityStr) {
        this.activityStr = activityStr;
        }



        public BigDecimal getCartAmount() {
        return this.cartAmount;
        }

        public void setCartAmount(BigDecimal cartAmount) {
        this.cartAmount = cartAmount;
        }



        public BigDecimal getDueAmount() {
        return this.dueAmount;
        }

        public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
        }



        public Boolean getNotCanCreateOrder() {
        return this.notCanCreateOrder;
        }

        public void setNotCanCreateOrder(Boolean notCanCreateOrder) {
        this.notCanCreateOrder = notCanCreateOrder;
        }




//        public List<ShoppingCarDomain> getShoppingCarDomains() {
//            return this.shoppingCarDomains;
//        }
//
//        public void setShoppingCarDomains(List<ShoppingCarDomain> shoppingCarDomains) {
//            this.shoppingCarDomains = shoppingCarDomains;
//        }

    }
}
