package com.gaosiedu.live.sdk.android.base;

import java.io.Serializable;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/24 上午9:12
 * 修改人：weiyimeng
 * 修改时间：2018/7/24 上午9:12
 * 修改备注：
 */
public class ResponseResult implements Serializable {
    
    private static final long serialVersionUID = 7386101989354922441L;
    public String code;
    //
    public String msg;
    
    //
    //    // public int total;
    //
    public String status;//lmc以前接口对应code
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getMessage() {
        return msg;
    }
    
    public void setMessage(String message) {
        this.msg = message;
    }
    
    public String getStatus(){
        return status;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    //
    //
    
    /**
     * 服务器的错误码
     */
    public interface ResultStatus {
        
        // TODO 需要跟服务器接口一致
        String OK = "SUCCESS";// 成功
        String EMPTY = "EMPTY";
        String FAIL = "FAIL";//
        String UNAUTHC = "UNAUTHC";//鉴权失败
        String EXPIRE = "EXPIRE";//token过期
        String EXCEPTION="EXCEPTION";
        int HAS_BIND = 123500;//已绑定
        int REPEAT_ACTION = 129000;//重复操作
        int BE_ADD_BLACK = 140100;//被拉黑
        int SUSPENED = 144444;//被封停
        int DELETED = 155555;//删除
    }
    
}
