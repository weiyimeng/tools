package com.gstudentlib.activity

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.utils.LOGGER
import com.gstudentlib.R
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.view.CommonLoadingDialog
import kotlinx.android.synthetic.main.activity_common_web.*

@Router("commonWeb")
class CommonWebActivity : BaseActivity() {

    private var mUrl: String? = ""
    private var mTitle: String? = ""
    private val loadingDialog: CommonLoadingDialog by lazy { CommonLoadingDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_web)
    }

    override fun initView() {
        super.initView()
        getIntentData()
        if (!mTitle.isNullOrEmpty()) {
            title_bar.setTitle(mTitle)
        }
        title_bar.setLeftListener(this)
        showLoadingDialog()
        initWebSetting()
    }

    private fun getIntentData() {
        intent.apply {
            mUrl = getStringExtra("url") ?: ""
            mTitle = getStringExtra("title") ?: ""
        }
    }

    private fun initWebSetting() {
        val settings = webView.settings
        settings.savePassword = false
        settings.setAppCacheEnabled(true)
        settings.allowFileAccess = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.domStorageEnabled = true
        settings.setSupportZoom(false)
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.builtInZoomControls = false
        settings.textZoom = 100
        settings.setAppCachePath(mContext.applicationContext.cacheDir.absolutePath)
        setWebViewClient()
        setWebChromeClient()
        webView.loadUrl(mUrl)
    }

    private fun setWebViewClient() {
        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                dismissLoadingDialog()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                //接受所有网站的证书
                handler?.proceed()
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
            }
        }
    }

    private fun setWebChromeClient() {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                if (!mTitle.isNullOrEmpty()) {
                    return
                }
                if (!title.isNullOrEmpty()) {
                    title_bar.setTitle(title)
                } else {
                    title_bar.setTitle("爱学习")
                }
            }
        }
    }

    /**
     * 显示loading
     */
    private fun showLoadingDialog() {
        if (!loadingDialog.isShowing && !(isFinishing || isDestroyed)) {
            loadingDialog.show()
        }
    }

    /**
     * 销毁loading
     */
    private fun dismissLoadingDialog() {
        if (loadingDialog.isShowing && !(isFinishing || isDestroyed)) {
            loadingDialog.dismiss()
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> finish()
        }
    }

    override fun kickOut(message: String?) {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        if (webView != null) {
            val viewGroup = webView.parent as ViewGroup
            viewGroup.removeView(webView)
            webView.removeAllViews()
            try {
                webView.destroy()
            } catch (e: Exception) {
                LOGGER.log(e)
            }
        }
        super.onDestroy()
    }
}
