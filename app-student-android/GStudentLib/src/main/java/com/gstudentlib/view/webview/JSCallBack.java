package com.gstudentlib.view.webview;

/**
 * 作者：created by 逢二进一 on 2019/9/16 15:12
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
public interface JSCallBack {

    void invoke(String methodName , Object var1);

    void invoke(String methodName , Object... var1);

}
