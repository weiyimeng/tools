package com.haoke91.a91edu.widget.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;

import com.blankj.utilcode.util.ObjectUtils;
import com.orhanobut.logger.Logger;

import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import java.util.HashMap;

public class TalkWebView extends XWalkView {
    private boolean interceptBack = true;
    private Context mContext;
    private View mProgressBar;//加载进度
    private View mView_errorPage; //设置错误页面，如果没有就不会处理了
    
    
    //    @SuppressLint("JavascriptInterface")
    public TalkWebView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        if (isInEditMode()) {
            return;
        }
        addJavascriptInterface(APIJSTalkInterface.class, APIJSTalkInterface.JS_FUNCTION_NAME);
        setUserAgentString(getUserAgentString() + " 91haoke_android");
        setDrawingCacheEnabled(true);
        XWalkSettings settings = getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportSpatialNavigation(true);
        settings.setAllowFileAccess(true);
        setUIClient(new XWalkUIClient(this) {
            @Override
            public void openFileChooser(XWalkView view, ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                Logger.e("请求打开本地文件！");
            }
            
            @Override
            public void onPageLoadStarted(XWalkView view, String url) {
                super.onPageLoadStarted(view, url);
                if (mOnOpenUrlListener != null) {
                    mOnOpenUrlListener.onOpenUrl(url);
                    mOnOpenUrlListener.showLoadingLayout();
                }
                if (mView_errorPage != null) {
                    mView_errorPage.setVisibility(GONE);
                }
            }
            
            @Override
            public void onReceivedTitle(XWalkView view, String title) {
                super.onReceivedTitle(view, title);
                Logger.e("onReceivedTitle==\n" + title);
                if (mOnReceiveListener != null) {
                    mOnReceiveListener.OnReceiveTitle(title);
                }
                //                if (!TextUtils.isEmpty(title) && title.contains("404")) {
                //                    mOnLoadErrorListener.onError();
                //                }
            }
        });
        setResourceClient(new XWalkResourceClient(this) {
            @Override
            public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
                Logger.e("loading:" + url);
                if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    ((Activity) mContext).startActivityForResult(intent, 11);
                    return true;
                } else {
                    if (wxPay) {
                        HashMap<String, String> ss = new HashMap<>();
                        ss.put("Referer", "http://test.91haoke.com");
                        view.loadUrl(url, ss);
                    } else {
                        view.loadUrl(url);
                    }
                    return true;
                    
                }
            }
            
            @Override
            public void onProgressChanged(XWalkView view, int progressInPercent) {
                super.onProgressChanged(view, progressInPercent);
                if (mProgressBar == null) {
                    return;
                }
                if (progressInPercent < 100) {
                    mProgressBar.setVisibility(VISIBLE);
                } else {
                    mProgressBar.setVisibility(GONE);
                }
            }
            
            @Override
            public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
                //                super.onReceivedLoadError(view, errorCode, description, failingUrl);
                //加载失败提示
                
                Logger.e("onerror==" + errorCode + "\n " + description + "\n" + failingUrl);
                if (errorCode < 0 && mView_errorPage != null) {
                    mView_errorPage.setVisibility(VISIBLE);
                }
                
                if (mOnOpenUrlListener != null) {
                    mOnOpenUrlListener.hideLoadingLayout();
                }
                
            }
            
            //            @Override
            //            public void onReceivedSslError(XWalkView view, ValueCallback<Boolean> callback, SslError error) {
            //                //                super.onReceivedSslError(view, callback, error);
            //                callback.onReceiveValue(true);
            //            }
            @Override
            public void onLoadFinished(XWalkView view, String url) {
                super.onLoadFinished(view, url);
                if (mOnOpenUrlListener != null) {
                    mOnOpenUrlListener.hideLoadingLayout();
                }
            }
        });
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (!interceptBack) {
                        return false;
                    }
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && getNavigationHistory().canGoBack()) {
                        getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
                        return true;
                    }
                }
                return false;
            }
        });
        
        APIJSTalkInterface jsInterface = new APIJSTalkInterface(context);
        this.addJavascriptInterface(jsInterface, APIJSTalkInterface.JS_FUNCTION_NAME);
    }
    
    public void setProgressBar(View _progressBar) {
        this.mProgressBar = _progressBar;
    }
    
    public void setView_errorPage(View _errorPage) {
        this.mView_errorPage = _errorPage;
    }
    
    private void showChild(ViewGroup vg) {
        int count = vg.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = vg.getChildAt(i);
            if (v instanceof ViewGroup) {
                showChild(vg);
            } else {
                Logger.d("WEBVIDEO", v.toString());
            }
        }
    }
    
    /**
     * 调用JS方法
     *
     * @param funcName 方法名
     */
    public void callJSFunction(String funcName) {
        loadUrl("javascript: " + funcName + "()");
    }
    
    @Override
    public void loadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        super.loadUrl(url);
        if (mOnOpenUrlListener != null) {
            mOnOpenUrlListener.onOpenUrl(url);
        }
    }
    
    public void loadUrl(String url, HashMap<String, String> map) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        super.loadUrl(url, map);
        if (mOnOpenUrlListener != null) {
            mOnOpenUrlListener.onOpenUrl(url);
        }
    }
    
    public void openUrl(String url) {
        if (ObjectUtils.isEmpty(url)) {
            loadData("", "text/html", "UTF-8");
        } else {
            loadUrl(url);
        }
        
    }
    
    private OnOpenUrlListener mOnOpenUrlListener;
    
    public void setOnOpenUrlListener(OnOpenUrlListener mOnOpenUrlListener) {
        this.mOnOpenUrlListener = mOnOpenUrlListener;
    }
    
    private boolean wxPay;
    
    public void setWxPay(boolean wxPay) {
        this.wxPay = wxPay;
    }
    
    public interface OnOpenUrlListener {
        void onOpenUrl(String url);
        
        void showLoadingLayout();
        
        void hideLoadingLayout();
        
        void onReceivedError();
    }
    
    /**
     * 设置是否拦截back事件
     *
     * @param mInterceptBack
     */
    public void setInterceptBack(boolean mInterceptBack) {
        this.interceptBack = mInterceptBack;
    }
    
    private onReceiveInfoListener mOnReceiveListener;
    
    public void setOnReceiveInfoListener(onReceiveInfoListener mOnReceiveListener) {
        this.mOnReceiveListener = mOnReceiveListener;
    }
    
    public interface onReceiveInfoListener {
        void OnReceiveTitle(String title);
        
        void onReceiveIcon(Bitmap icon);
        
    }
    
    public interface OnShareListener {
        void share(String title, String desc, String image, String url);
    }
    
    public void clear_view() {
        loadUrl("about:blank");
    }
}
