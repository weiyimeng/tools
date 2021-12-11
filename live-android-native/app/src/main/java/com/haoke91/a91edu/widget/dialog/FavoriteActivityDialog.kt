package com.haoke91.a91edu.widget.dialog

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.TextView

import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ToastUtils
import com.gaosiedu.live.sdk.android.domain.CourseActivityDomain
import com.haoke91.a91edu.GlobalConfig
import com.haoke91.a91edu.R

import java.io.File
import java.io.FileNotFoundException


/**
 * Created by wdy on 2017/9/9.
 */

class FavoriteActivityDialog : DialogFragment() {
    
    private val onClickListener = View.OnClickListener { dismiss() }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!, R.style.Assistant_BottomDialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        val view = View.inflate(activity, R.layout.dialog_favoriteactivity, null)
        dialog.setContentView(view)
        initView(view)
        val window = dialog.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.BOTTOM
        //设置没有效果
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = wlp
        return dialog
    }
    
    
    private fun initView(view: View) {
        view.findViewById<View>(R.id.iv_closeDialog).setOnClickListener { dismiss() }
        val tvTypeName = view.findViewById<TextView>(R.id.tvTypeName)
        val tvFlag = view.findViewById<TextView>(R.id.tvDiscount)
        val tvTime = view.findViewById<TextView>(R.id.tvTime)
        tvTypeName.text = mActivityDomain?.name ?: ""
        tvFlag.text = mActivityDomain?.flag ?: ""
        tvTime.text = Html.fromHtml(mActivityDomain?.plan ?: "")
        
    }
    
    companion object {
        private val TAG = "ChoiceSexDialog"
        private var mActivityDomain: CourseActivityDomain? = null
        
        
        fun showDialog(appCompatActivity: AppCompatActivity, activity: CourseActivityDomain): FavoriteActivityDialog {
            mActivityDomain = activity
            val fragmentManager = appCompatActivity.supportFragmentManager
            var bottomDialogFragment: FavoriteActivityDialog = (fragmentManager.findFragmentByTag(TAG)
                    ?: FavoriteActivityDialog()) as FavoriteActivityDialog
            //            if (null == bottomDialogFragment) {
            //                bottomDialogFragment = FavoriteActivityDialog()
            //            }
            //
            if (!appCompatActivity.isFinishing
                    && !bottomDialogFragment.isAdded) {
                fragmentManager.beginTransaction()
                        .add(bottomDialogFragment, TAG)
                        .commitAllowingStateLoss()
            }
            
            return bottomDialogFragment
        }
    }
    
    
}
