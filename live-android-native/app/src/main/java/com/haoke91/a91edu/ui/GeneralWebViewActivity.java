package com.haoke91.a91edu.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gaosiedu.live.sdk.android.api.order.pre.LiveOrderCheckRequest;
import com.gaosiedu.live.sdk.android.api.order.pre.LiveOrderCheckResponse;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.order.AllOrderFragment;
import com.haoke91.a91edu.ui.order.PaySuccessActivity;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.webview.TalkWebView;
import com.haoke91.baselibrary.event.MessageItem;
import com.haoke91.baselibrary.event.RxBus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/9/12 下午7:16
 * 修改人：weiyimeng
 * 修改时间：2018/9/12 下午7:16
 * 修改备注：
 */
public class GeneralWebViewActivity extends BaseActivity {
    private TalkWebView web;
    private TextView toolbar_title;
    
    public static void start(Context context, String url) {
        Intent intent = new Intent(context, GeneralWebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
    
    public static void start(Context context, String url, String orderNo, BigDecimal dueAmount) {
        Intent intent = new Intent(context, GeneralWebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("dueAmount", dueAmount);
        context.startActivity(intent);
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_general_webview;
    }
    
    @Override
    public void initialize() {
        String url = getIntent().getStringExtra("url");
        String orderNo = getIntent().getStringExtra("orderNo");
        web = findViewById(R.id.webView);
        if (!TextUtils.isEmpty(orderNo)) {
            HashMap ss = new HashMap<String, String>();
            ss.put("Referer", "http://test.91haoke.com");
            web.loadUrl(url, ss);
            web.setWxPay(true);
        } else {
            web.loadUrl(url);
        }
        toolbar_title = findViewById(R.id.toolbar_title);
        findViewById(R.id.toolbar_back).setOnClickListener(OnClickListener);
        web.setOnReceiveInfoListener(onReceiveInfoListener);
        web.setOnOpenUrlListener(OnOpenUrlListener);
        
    }
    
    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.toolbar_back) {
                onBackPressed();
                //                finish();
            }
        }
    };
    private TalkWebView.OnOpenUrlListener OnOpenUrlListener = new TalkWebView.OnOpenUrlListener() {
        @Override
        public void onOpenUrl(String url) {
            dismissLoadingDialog();
        }
        
        @Override
        public void showLoadingLayout() {
            showLoadingDialog();
        }
        
        @Override
        public void hideLoadingLayout() {
            dismissLoadingDialog();
        }
        
        @Override
        public void onReceivedError() {
            dismissLoadingDialog();
            
        }
    };
    
    private TalkWebView.onReceiveInfoListener onReceiveInfoListener = new TalkWebView.onReceiveInfoListener() {
        @Override
        public void OnReceiveTitle(String title) {
            toolbar_title.setText(title);
        }
        
        @Override
        public void onReceiveIcon(Bitmap icon) {
        
        }
    };
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            showLoadingDialog();
            if (!TextUtils.isEmpty(getIntent().getStringExtra("orderNo"))) {
                showLoadingDialog();
                LiveOrderCheckRequest request = new LiveOrderCheckRequest();
                request.setUserId(UserManager.getInstance().getUserId());
                request.setOrderNo(getIntent().getStringExtra("orderNo"));
                Api.getInstance().post(request, LiveOrderCheckResponse.class, new ResponseCallback<LiveOrderCheckResponse>() {
                    @Override
                    public void onResponse(LiveOrderCheckResponse date, boolean isFromCache) {
                        dismissLoadingDialog();
                        if (date.getData().getFlag()) {
                            if (ObjectUtils.isEmpty(getIntent().getSerializableExtra("dueAmount"))) {
                                PaySuccessActivity.Companion.start(GeneralWebViewActivity.this, (getIntent().getSerializableExtra("dueAmount").toString()));
                            } else {
                                PaySuccessActivity.Companion.start(GeneralWebViewActivity.this, "");
                            }
                            //                            MessageItem messageItem = new MessageItem(MessageItem.order_change, AllOrderFragment.wait_pay);
                            //                            RxBus.getIntanceBus().post(messageItem);
                        } else {
                            ToastUtils.showShort("支付失败");
                        }
                        finish();
                    }
                }, "");
            }
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (web != null) {
            web.onDestroy();
        }
    }
    
}
