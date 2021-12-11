package com.haoke91.baselibrary.event;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/5/15 下午5:46
 * 修改人：weiyimeng
 * 修改时间：2018/5/15 下午5:46
 * 修改备注：
 */
public class MessageItem {
    public static final int change_head = 0x11111;//更换头像
    public static final int action_login = 0x11211;//登录
    public static final int deafautlAddressChange = 0x111111;//默认收货地址改变
    public static final int order_change = 0x11411;//订单状态改变
    public static final int REFRESH_HOMEWORK = 0x10001;// 提交作业
    public static final int change_tab = 0x12001;// 改变首页tab
    public static final int PULLCOMMANDHISTORY=0x1002;//拉取历史命令
    
    
    private int type;
    private String message;
    private Object targetClass;
    
    public MessageItem(int type, String message) {
        this.type = type;
        this.message = message;
    }
    
    public MessageItem(int type, String message, Object targetClass) {
        this.type = type;
        this.message = message;
        this.targetClass = targetClass;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    
    public Object getTargetClass() {
        return targetClass;
    }
    
    public void setTargetClass(Object targetClass) {
        this.targetClass = targetClass;
    }
}
