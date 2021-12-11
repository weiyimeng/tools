package gaosi.com.learn.studentapp.login

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import com.chuanglan.shanyan_sdk.tool.ShanYanUIConfig
import com.gsbaselib.utils.FileUtil
import com.gsbaselib.utils.TypeValue
import gaosi.com.learn.R

/**
 * description:
 * created by huangshan on 2020/7/29 下午3:34
 */
object OneKeyLoginUIConfig {

    fun getConfig(context: Context): ShanYanUIConfig {
        val logoImgPath = ContextCompat.getDrawable(context, R.drawable.app_icon_logo)
        val navReturnImgPath = ContextCompat.getDrawable(context, R.drawable.app_icon_black_back)
        val logBtnImgPath = ContextCompat.getDrawable(context, R.drawable.app_bg_auth_btn)
        val checkImgPath = ContextCompat.getDrawable(context, R.drawable.icon_agree_checked)
        val unCheckImgPath = ContextCompat.getDrawable(context, R.drawable.icon_agree_unchecked)

        //loading
        val inflater = LayoutInflater.from(context)
        val loadingView = inflater.inflate(R.layout.loading_layout, null)
        val loadingLayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        loadingLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        loadingView.layoutParams = loadingLayoutParams
        loadingView.visibility = View.GONE

        val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.setImageResource(R.drawable.app_bg_login_bottom)
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, TypeValue.dp2px(52F))
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        imageView.layoutParams = layoutParams

        val filePath1 = FileUtil.getJsBundleSaveFilePath("agreement_userService.html")
        val url1 = "file://$filePath1"
        val filePath2 = FileUtil.getJsBundleSaveFilePath("agreement_privacy.html")
        val url2 = "file://$filePath2"

        val uiConfig = ShanYanUIConfig.Builder()
                //设置导航栏
                .setNavColor(Color.parseColor("#ffffff"))
                .setNavText("")
                .setNavReturnImgPath(navReturnImgPath)
                .setNavReturnBtnOffsetX(10)
                .setNavReturnImgHidden(false)
                //设置logo
                .setLogoImgPath(logoImgPath)
                .setLogoWidth(72)
                .setLogoHeight(72)
                .setLogoOffsetY(128)
                .setLogoHidden(false)
                //设置号码栏
                .setNumberColor(0xFF1F243D.toInt())
                .setNumberSize(14)
                .setNumberBold(true)
                .setNumFieldOffsetY(212)
                //设置登录按钮
                .setLogBtnText("本机号码一键登录")
                .setLogBtnTextColor(0xffffffff.toInt())
                .setLogBtnTextSize(14)
                .setLogBtnTextBold(true)
                .setLogBtnHeight(48)
                .setLogBtnWidth(getScreenWidth(context, true) - 104)
                .setLogBtnImgPath(logBtnImgPath)
                .setLogBtnOffsetY(258)
                //隐私协议
                .setPrivacyTextSize(12)
                .setAppPrivacyColor(0xFFA3B3C2.toInt(), 0xFF1F243D.toInt())
                .setAppPrivacyOne("用户服务协议", url1)
                .setAppPrivacyTwo("隐私政策", url2)
                .setPrivacyText("请阅读并同意", "和", "", "", "并授权爱学习获取本机号码")
                .setPrivacySmhHidden(false)
                .setPrivacyCustomToastText("请先同意《用户服务协议》和《隐私政策》")
                .setPrivacyState(true)
                .setUncheckedImgPath(unCheckImgPath)
                .setCheckedImgPath(checkImgPath)
                .setCheckBoxMargin(0, 0, 4, 0)
                .setPrivacyOffsetX(20)
                .setPrivacyOffsetBottomY(72)
                //设置slogan位置
                .setSloganOffsetBottomY(52)
                //设置自定义loading
                .setLoadingView(loadingView)
                //设置自定义布局
                .addCustomView(imageView, false, false, null)
        return uiConfig.build()
    }

    private fun getScreenWidth(context: Context, isDp: Boolean): Int {
        var screenWidth = 0
        val winWidth: Int
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val defaultDisplay = wm.defaultDisplay
        val point = Point()
        defaultDisplay.getSize(point)
        winWidth = if (point.x > point.y) {
            point.y
        } else {
            point.x
        }
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        if (!isDp) {
            return winWidth
        }
        val density = dm.density //屏幕密度（0.75 / 1.0 / 1.5）
        screenWidth = (winWidth / density).toInt() //屏幕高度(dp)
        return screenWidth
    }
}