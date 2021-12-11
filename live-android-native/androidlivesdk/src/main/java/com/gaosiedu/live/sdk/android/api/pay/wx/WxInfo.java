package com.gaosiedu.live.sdk.android.api.pay.wx;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/28 10:19 AM
 * 修改人：weiyimeng
 * 修改时间：2018/11/28 10:19 AM
 * 修改备注：
 */
public class WxInfo {
    private String mweb_url;
    private String orderNo;
    private String status;
    
    public String getData() {
        return mweb_url;
    }
    
    public void setData(String data) {
        this.mweb_url = mweb_url;
    }
    
    public String getOrderNo() {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
//    public static class WxpayInfo {
//        private String mweb_url;
//
//        public String getMweb_url() {
//            return mweb_url;
//        }
//
//        public void setMweb_url(String mweb_url) {
//            this.mweb_url = mweb_url;
//        }
//    }
}
