package com.gstudentlib.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import com.gstudentlib.R

/**
 * description: 通用web loading
 * created by huangshan on 2020-01-02 18:27
 */
class CommonLoadingDialog(context: Context) : Dialog(context, R.style.loadingDialog) {

    init {
        setContentView(R.layout.student_common_web_loading_dialog)
        val window = window
        val params = window?.attributes
        params?.gravity = Gravity.CENTER
        window?.attributes = params
    }

}