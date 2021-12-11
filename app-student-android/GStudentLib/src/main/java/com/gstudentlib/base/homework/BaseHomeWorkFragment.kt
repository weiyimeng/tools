package com.gstudentlib.base.homework

import android.content.Intent
import com.gstudentlib.activity.ZoomImageActivity
import com.gstudentlib.base.STBaseFragment
import com.gstudentlib.view.webview.IHtmlClickListener
import com.gstudentlib.view.webview.JSCallBack

/**
 * 作者：created by 逢二进一 on 2019/9/16 16:26
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
abstract class BaseHomeWorkFragment : STBaseFragment(), IHtmlClickListener {

    var mJSCallBack: JSCallBack? = null

    override fun onChoiceQuestionClick(jsCallback: JSCallBack?, vararg agrs: Any?) {
        this.mJSCallBack = jsCallback
        changeAnswerData(agrs[0] , agrs[1] as Int)
        checkAutoNext(agrs[0])
    }

    override fun onCompletionClick(jsCallback: JSCallBack?, vararg agrs: Any?) {
        this.mJSCallBack = jsCallback
        changeAnswerData(agrs[0] , agrs[1] as Int)
    }

    override fun onJudgmentClick(jsCallback: JSCallBack?, vararg agrs: Any?) {
        this.mJSCallBack = jsCallback
        changeAnswerData(agrs[0] , agrs[1] as Int)
        checkAutoNext(agrs[0])
    }

    override fun onSubjectiveClick(jsCallback: JSCallBack?, vararg agrs: Any?) {
        this.mJSCallBack = jsCallback
        changeAnswerData(agrs[0] , agrs[1] as Int)
    }

    override fun onSelectPicClick(jsCallback: JSCallBack?, vararg agrs: Any?) {
        this.mJSCallBack = jsCallback
        showSelectPic(mJSCallBack)
    }

    override fun onShowBigImage(jsCallback: JSCallBack?, agr0: String?) {
        context ?: return
        val intent = Intent(context, ZoomImageActivity::class.java)
        intent.putExtra("data", agr0)
        startActivity(intent)
    }

    override fun onShowBigImageWithState(jsCallback: JSCallBack?, answerResult: Int?, imgUrl: String?) {
        context ?: return
        val intent = Intent(context, ZoomImageActivity::class.java)
        intent.putExtra("data", imgUrl)
        intent.putExtra("answerResult", answerResult)
        startActivity(intent)
    }

    override fun onVoiceCommentsChange(jsCallback: JSCallBack?, vararg agrs: Any?) {
        this.mJSCallBack = jsCallback
        changeVoiceCommentData(agrs[0] , agrs[1] as Int)
    }

    protected open fun showSelectPic(mJSCallBack: JSCallBack?) {
    }

    protected open fun changeAnswerData(data: Any? , showIndex: Int) {
    }

    protected open fun changeVoiceCommentData(data: Any?, showIndex: Int) {
    }

    protected open fun checkAutoNext(data: Any?) {
    }
}