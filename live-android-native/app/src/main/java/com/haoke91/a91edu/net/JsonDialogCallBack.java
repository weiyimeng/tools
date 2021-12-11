package com.haoke91.a91edu.net;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Window;

import com.blankj.utilcode.util.ObjectUtils;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;

import okhttp3.ResponseBody;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/5/22 上午9:51
 * 修改人：weiyimeng
 * 修改时间：2018/5/22 上午9:51
 * 修改备注：
 */
public abstract class JsonDialogCallBack<T extends ResponseResult> extends JsonCallBack<T> {
    // private Class<T> clazz;
    private ProgressDialog dialog;
    // Activity activity;
    
    public JsonDialogCallBack(Class<T> clazz, Activity activity) {
        super(clazz);
        //  this.clazz = clazz;
        //  this.activity = activity;
        dialog = new ProgressDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("111111");
        dialog.show();
    }
    
    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        HttpHeaders headers = request.getHeaders();
    }
    
    
    @Override
    public void onFinish() {
        super.onFinish();
        dialog.dismiss();
    }
    
    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        
        // return super.convertResponse(response);
        int code = response.code();
        Logger.e("code-====" + code);
        ResponseBody body = response.body();
        if (ObjectUtils.isEmpty(body)) {
            return null;
        }
        T data = null;
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(body.charStream());
        if (!ObjectUtils.isEmpty(clazz)) {
            data = gson.fromJson(jsonReader, clazz);
        }
        return data;
    }
}
