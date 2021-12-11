package com.haoke91.a91edu.widget.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.haoke91.a91edu.R
import com.haoke91.baselibrary.utils.ACallBack
import kotlinx.android.synthetic.main.dialog_confirm_back.*
import kotlinx.android.synthetic.main.dialog_confirm_back.view.*
import kotlinx.android.synthetic.main.item_text.view.*

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/20 10:48 AM
 * 修改人：weiyimeng
 * 修改时间：2018/11/20 10:48 AM
 * 修改备注：
 * @version
 */
class ConfirmBackDialog : DialogFragment() {
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!, R.style.DialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val attributes = dialog.window!!.attributes
        attributes.gravity = Gravity.CENTER
        //  dialog.setCanceledOnTouchOutside(false);
        val view = View.inflate(activity, R.layout.dialog_confirm_back, null)
        val layoutParams = ViewGroup.LayoutParams((resources.displayMetrics.widthPixels * 0.8).toInt(), -2)
        dialog.setContentView(view, layoutParams)
        
        initView(view)
        return dialog
    }
    
    private fun initView(view: View) {
        view.findViewById<View>(R.id.tv_cancel).setOnClickListener {
            callBack.call("1")
            dismiss()
        }
        view.findViewById<View>(R.id.tv_commit).setOnClickListener {
            callBack.call("2")
            dismiss()
        }
        
        val returnWay = arguments?.getString("returnWay")
        val money = arguments?.getString("money")
        
        view.findViewById<TextView>(R.id.tv_back_method).text = returnWay
        view.findViewById<TextView>(R.id.tv_count).text = money
    }
    
    
    companion object {
        var TAG = "ConfirmBackDialog"
        lateinit var callBack: ACallBack<String>
        fun showDialog(appCompatActivity: AppCompatActivity, callBack: ACallBack<String>, returnWay: String, money: String?): ConfirmBackDialog {
            val dialog = ConfirmBackDialog()
            val bundle = Bundle()
            ConfirmBackDialog.callBack = callBack
            bundle.putString("returnWay", returnWay)
            bundle.putString("money", money)
            dialog.arguments = bundle
            appCompatActivity.supportFragmentManager.beginTransaction().add(dialog, TAG).commitAllowingStateLoss()
            return dialog
        }
    }
}
