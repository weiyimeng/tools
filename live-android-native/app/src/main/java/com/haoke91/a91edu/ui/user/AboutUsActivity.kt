package com.haoke91.a91edu.ui.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import com.blankj.utilcode.util.PermissionUtils
import com.haoke91.a91edu.BuildConfig
import com.haoke91.a91edu.R
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.haoke91.baselibrary.views.TipDialog
import com.yanzhenjie.permission.*
import kotlinx.android.synthetic.main.activity_about.*

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2019/1/11 2:08 PM
 * 修改人：weiyimeng
 * 修改时间：2019/1/11 2:08 PM
 * 修改备注：
 * @version
 */
class AboutUsActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_about
    }
    
    override fun initialize() {
        tv_version_name.text = "v${BuildConfig.VERSION_NAME}"
        rl_service_phone.setOnClickListener(object : NoDoubleClickListener() {
            override fun onDoubleClick(v: View) {
                isLoginOut()
            }
        })
    }
    
    
    @SuppressLint("MissingPermission")
    private fun isLoginOut() {
        val dialog = TipDialog(this)
        dialog.setTextDes("呼叫 400-898-9991")
        dialog.setButton1(getString(R.string.action_ok)) { _, dialog ->
            
            var permission = PermissionUtils.isGranted(Permission.CALL_PHONE)
            if (permission) {
                var intent = Intent(Intent.ACTION_CALL)
                var data = Uri.parse("tel:" + "400-898-9991")
                intent.data = data
                startActivity(intent)
            } else {
                requestPhonePermission()
            }
            dialog.dismiss()
        }
        dialog.setButton2(getString(R.string.cancel)) { _, dialog -> dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }
    
    @SuppressLint("MissingPermission")
    private fun requestPhonePermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.CALL_PHONE)
                .rationale { _, _, executor ->
                    executor.execute()
                }
                .onGranted {
                    var intent = Intent(Intent.ACTION_CALL)
                    var data = Uri.parse("tel:" + "400-898-9991")
                    intent.data = data
                    startActivity(intent)
                }
                .start()
    }
    
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AboutUsActivity::class.java)
            context.startActivity(intent)
        }
    }
}
