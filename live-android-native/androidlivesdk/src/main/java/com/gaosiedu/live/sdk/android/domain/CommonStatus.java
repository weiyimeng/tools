package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/9/10 上午10:05
 * 修改人：weiyimeng
 * 修改时间：2018/9/10 上午10:05
 * 修改备注：
 */
public class CommonStatus implements Serializable {
    // * status   0 未付款 1 已付款 -1  已取消 2支付中 3退款中 4全部退款 5部分退款 6换货中8换货完成9换货驳回
    public int status;
    public String orderNo;
    public int returnNo;
    
}
