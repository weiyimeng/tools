package gaosi.com.learn.studentapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Point
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.view.*
import android.webkit.*
import android.widget.FrameLayout
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.utils.LOGGER
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.view.CommonLoadingDialog
import gaosi.com.learn.R
import kotlinx.android.synthetic.main.activity_common_js_web.*
import com.gstudentlib.base.STBaseConstants
import android.view.WindowManager

@Router("commonJsWeb")
class CommonJsWebActivity : BaseActivity() {

    private var mUrl: String? = ""
    private var mTitle: String? = ""
    private val loadingDialog: CommonLoadingDialog by lazy { CommonLoadingDialog(this) }

    private var customView: View? = null
    private var fullscreenContainer: FrameLayout? = null
    private var customViewCallback: WebChromeClient.CustomViewCallback? = null
    private val COVER_SCREEN_PARAMS = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_js_web)
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
        webView.removeJavascriptInterface("searchBoxJavaBridge_")
        webView.removeJavascriptInterface("accessibility")
        webView.removeJavascriptInterface("accessibilityTraversal")
        val settings = webView.settings
        settings.savePassword = false
        settings.setAppCacheEnabled(true)
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowFileAccess = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.domStorageEnabled = true
        settings.setSupportZoom(false)
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.builtInZoomControls = false
        settings.textZoom = 100
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
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
                return false
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

            /** 视频播放相关的方法  **/
            override fun getVideoLoadingProgressView(): View {
                val frameLayout = FrameLayout(this@CommonJsWebActivity)
                frameLayout.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                return frameLayout
            }

            /** 全屏调用 **/
            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                showCustomView(view, callback)
            }

            /** 取消全屏调用 **/
            override fun onHideCustomView() {
                hideCustomView()
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

    /**
     * 获取虚拟按键高度
     */
    private fun getNavigationHeight(): Int {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                val display = windowManager.defaultDisplay
                val size = Point()
                val realSize =  Point()
                display.getSize(size)
                display.getRealSize(realSize)
                val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
                val height = resources.getDimensionPixelSize(resourceId)
                //超出系统默认的导航栏高度以上，则认为存在虚拟导航
                if ((realSize.y - size.y) > (height - 10)) {
                    return height
                }
                return 0
            } else {
                val menu = ViewConfiguration.get(this).hasPermanentMenuKey()
                val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
                return if (menu || back) {
                    0
                } else {
                    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
                    val height = resources.getDimensionPixelSize(resourceId)
                    height
                }
            }
        } catch (e : Exception) {
            LOGGER.log(e)
        }
        return 0
    }

    /**
     * 视频播放全屏
     */
    private fun showCustomView(view: View, callback: WebChromeClient.CustomViewCallback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden()
            return
        }
        if (!isDestroyed && !isFinishing) {
            fullScreen()
            val decor = window?.decorView as FrameLayout
            fullscreenContainer = FullscreenHolder(mContext)
            fullscreenContainer?.addView(view, COVER_SCREEN_PARAMS)
            decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS)
            val layoutParams = fullscreenContainer?.layoutParams
            layoutParams?.width = STBaseConstants.deviceInfoBean.screenHeight - getNavigationHeight()
            customView = view
            setStatusBarVisibility(false)
            customViewCallback = callback
        }
    }

    /**
     * 隐藏视频全屏
     */
    private fun hideCustomView() {
        if (customView == null) {
            return
        }
        setStatusBarVisibility(true)
        if (!isDestroyed && !isFinishing) {
            fullScreen()
            val decor = window?.decorView as FrameLayout
            decor.removeView(fullscreenContainer)
            fullscreenContainer = null
            customView = null
            customViewCallback?.onCustomViewHidden()
            webView.visibility = View.VISIBLE
        }
    }

    /**
     * 全屏/取消全屏
     */
    private fun fullScreen() {
        requestedOrientation = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    /**
     * 全屏容器界面
     */
    internal class FullscreenHolder(ctx: Context) : FrameLayout(ctx) {

        init {
            setBackgroundColor(ContextCompat.getColor(ctx, R.color.homework_black))
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouchEvent(evt: MotionEvent): Boolean {
            return true
        }
    }

    private fun setStatusBarVisibility(visible: Boolean) {
        val flag = if (visible) 0 else WindowManager.LayoutParams.FLAG_FULLSCREEN
        window?.setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft ->
                if(webView.canGoBack()) {
                    webView.goBack()
                }else {
                    finish()
                }
        }
    }

    override fun kickOut(message: String?) {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            when {
                customView != null -> hideCustomView()
                webView.canGoBack() -> webView.goBack()
                else -> finish()
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
