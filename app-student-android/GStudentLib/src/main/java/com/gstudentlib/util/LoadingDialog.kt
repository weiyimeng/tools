package com.gstudentlib.util

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import com.gstudentlib.R
import kotlinx.android.synthetic.main.loading_layout.*

/**
 * loading dialog
 * Created by huangshan on 2019/4/4.
 */

class LoadingDialog(context: Context) : Dialog(context, R.style.loadingDialog) {

    init {
        setContentView(R.layout.loading_layout)
        val window = window
        val params = window.attributes
        params.gravity = Gravity.CENTER
        window.attributes = params
    }

    override fun dismiss() {
        loadingView.cancelAnimation()
        super.dismiss()
    }

}
