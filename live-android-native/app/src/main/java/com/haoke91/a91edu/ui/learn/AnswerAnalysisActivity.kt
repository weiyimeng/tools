package com.haoke91.a91edu.ui.learn

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.collect.LiveSccUserWrongQuestionCollectRequest
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.collect.LiveSccUserWrongQuestionCollectResponse
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.delete.LiveSccUserWrongQuestionDeleteRequest
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.delete.LiveSccUserWrongQuestionDeleteResponse
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.detail.LiveSccUserWrongQuestionDetailRequest
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.detail.LiveSccUserWrongQuestionDetailResponse
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.reason.LiveSccUserWrongQuestionSaveReasonRequest
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.reason.LiveSccUserWrongQuestionSaveReasonResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.CorrectRecordAdapter
import com.haoke91.a91edu.entities.Exam
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.ui.homework.CorrectExamActivity
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.exam.ChoiceExamView
import com.haoke91.a91edu.widget.exam.FillBlankExamView
import com.haoke91.a91edu.widget.flowlayout.FlowLayout
import com.haoke91.a91edu.widget.flowlayout.TagAdapter
import com.haoke91.a91edu.widget.flowlayout.TagFlowLayout
import com.haoke91.baselibrary.views.CenterDialog
import com.haoke91.baselibrary.views.TipDialog
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.layout_answeranalysis.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/9/18 16:20
 * @version
 */
class AnswerAnalysisActivity : BaseActivity() {

    private lateinit var mExam: Exam
    private var mChoiceExamView: ChoiceExamView? = null
    private var mFillBlankExamView: FillBlankExamView? = null
    private lateinit var mRecordAdapter: CorrectRecordAdapter

    private var mWrongReasonDialog: WrongReasonDialog? = null

    override fun getLayout() = R.layout.layout_answeranalysis

    override fun initialize() {
        val id = intent.getIntExtra("id", 0)
        toolbar_title.text = "答案解析"
        toolbar_back.setOnClickListener({ onBackPressed() })
        if (intent.hasExtra("exam"))
            mExam = intent.getSerializableExtra("exam") as Exam
        networkForExerciseDetail(id)
        //订正记录
        cbRecord.isChecked = true
        cbRecord.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                var animation = ObjectAnimator.ofFloat(rvRecord!!, "translationY", -rvRecord.height.toFloat(), 0f)
                animation.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        rvRecord.visibility = View.VISIBLE
                    }
                })
                animation.duration = 300
                animation.start()
            } else {
                var exitAnim = ObjectAnimator.ofFloat(rvRecord, "translationY", 0f, -rvRecord.height.toFloat())
                exitAnim.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        rvRecord.visibility = View.GONE
                    }
                })
                exitAnim.duration = 300
                exitAnim.start()
            }
        }
        //解析
        cb_showAnalyzeBtn.isChecked = true
        cb_showAnalyzeBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                rlAnswerArea.visibility = View.VISIBLE
                ll_analysis.visibility = View.VISIBLE
                rlAnswerArea.animate().scaleYBy(0f).scaleY(1f).setDuration(300).start()
                ll_analysis.animate().scaleYBy(0f).scaleY(1f).setDuration(300).start()
            } else {
                var exitAnim1 = ObjectAnimator.ofFloat(rlAnswerArea, "scaleY", 1f, 0f)
                var exitAnim2 = ObjectAnimator.ofFloat(ll_analysis, "scaleY", 1f, 0f)
                exitAnim1.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        rlAnswerArea.visibility = View.GONE
                        ll_analysis.visibility = View.GONE
                    }
                })
                exitAnim1.duration = 300
                exitAnim2.duration = 300
                exitAnim1.start()
                exitAnim2.start()
            }
        }
        //错因总结
        tvLookOrEditReason.setOnClickListener(ClickLisen())
        //订正习题
        tvCorrectBtn.setOnClickListener(ClickLisen())
        ivIsCollected.setOnClickListener(ClickLisen())
        ivDelete.setOnClickListener(ClickLisen())
    }

    private fun initExam(bean: LiveSccUserWrongQuestionDetailResponse.ResultData) {
        if (examContainer.childCount > 0) {
            examContainer.removeAllViews()
        }
        val isChoice = bean.questionType != 3
        val isMulti = bean.questionType == 2
        if (isChoice) {
            mChoiceExamView = ChoiceExamView(this)
            examContainer.addView(mChoiceExamView)
            mChoiceExamView!!.setExamHead(bean.questionStem)
            mChoiceExamView!!.tag = bean.questionWrongReason
            val questionContent = bean.questionContent
            var array: JSONArray? = null
            try {
                array = JSONArray(questionContent)
                val s = arrayOfNulls<String>(array!!.length())
                for (i in 0 until array!!.length()) {
                    val opt = array!!.get(i) as JSONObject
                    s[i] = opt.getString("questionOptionValue")
                }
                mChoiceExamView!!.setOptions(isMulti, false, *s)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        } else {
            mFillBlankExamView = FillBlankExamView(this)
            mFillBlankExamView!!.setHead(bean.questionStem)
            mFillBlankExamView!!.setEditAreaVisible(View.VISIBLE, true)
            mFillBlankExamView!!.tag = bean.questionWrongReason
            examContainer.addView(mFillBlankExamView)
        }
        rvRecord.layoutManager = LinearLayoutManager(this)
        mRecordAdapter = CorrectRecordAdapter(this)
        rvRecord.adapter = mRecordAdapter
        //        tvYourAnswer.text="你的答案：${mExam.use}"
    }


    private inner class ClickLisen : View.OnClickListener {

        private val instance: ClickLisen? = null
        //
        //        public ClickLisen(int id){
        //            questionId = id;
        //        }

        override fun onClick(v: View) {
            if (v.id == R.id.ivDelete) { //删除
                val dialog = TipDialog(v.context)
                dialog.setTextDes("确认删除这道题？\n删除后无法恢复")
                dialog.setTextDesSize(14)
                dialog.setButton1Color(Color.parseColor("#75C82B"))
                dialog.setButton1("确认") { _, dialog ->
                    Utils.loading(v.context)
                    networkForDelete(v.tag as Int)
                    dialog.dismiss()
                }
                dialog.setButton2Color(Color.parseColor("#363636"))
                dialog.setButton2("取消") { _, dialog -> dialog.dismiss() }
                dialog.show()
            } else if (v.id == R.id.tvLookOrEditReason) {
                mWrongReasonDialog = WrongReasonDialog(v.context, R.layout.dialog_wrongreasonsummary)
                mWrongReasonDialog!!.setExtra(v.tag!! as Int)
                mWrongReasonDialog!!.show()
                val attr = mWrongReasonDialog!!.window!!.attributes
                attr.width = (v.resources.displayMetrics.widthPixels - 40f * 2f * v.resources.displayMetrics.density).toInt()
                //                attr.horizontalMargin = 40 * v.getResources().getDisplayMetrics().density;
                mWrongReasonDialog!!.window!!.attributes = attr
            } else if (v.id == R.id.cancelBtn) {
                if (mWrongReasonDialog != null && mWrongReasonDialog!!.isShowing) {
                    mWrongReasonDialog!!.dismiss()
                }
            } else if (v.id == R.id.okBtn) {
                if (mWrongReasonDialog != null && mWrongReasonDialog!!.isShowing) {
                    mWrongReasonDialog!!.dismiss()
                }
                Utils.loading(v.context)
                networkForReasonSummary(mWrongReasonDialog!!.id, mWrongReasonDialog!!.selectedText)
            } else if (v.id == R.id.tvCorrectBtn) {
                //                v.getContext().startActivity(new Intent(v.getContext(), CorrectExamActivity.class));
                CorrectExamActivity.start(v.context, v.tag as Exam)
            }
        }
    }

    /**
     * update date of ui
     */
    private fun updateUI(bean: LiveSccUserWrongQuestionDetailResponse.ResultData) {
        tvSourceFrom.text = "来自：${bean.questionSource}"
        initExam(bean)
        ivIsCollected.isSelected = (bean.collect!!.toInt() == 1)
        ivIsCollected.setOnClickListener {
            it.isSelected = !it.isSelected
            networkForCollect(if (it.isSelected) 1 else 0, bean.id!!, it)
            Utils.loading(it.context)
        }
        //答案
        tvYourAnswer.text = "你的答案 ：${bean.userAnswer}"
        tvCorrectAnswer.text = "正确答案：${bean.questionAnswer}"
        //题目
        if (bean.questionAnalysis != null) {
            ll_analysis.visibility = View.VISIBLE
            tvAnalysis.text = bean.questionAnalysis
        } else {
            ll_analysis.visibility = View.GONE
        }
        mRecordAdapter.setData(bean.correctList)
        val exam = Exam()
        exam.setId(bean.id)
        exam.setQuestionType(bean.questionType!!)
        exam.setQuestionStem(bean.questionStem)
        exam.setQuestionContent(bean.questionContent)
        tvCorrectBtn.tag = exam
        tvLookOrEditReason.tag = bean.id
        if (bean.correct <= 0) {
            rlRecordArea.visibility = View.GONE
        } else {
            rlRecordArea.visibility = View.VISIBLE
        }
    }

    /**
     * 错题原因总结
     */
    private inner class WrongReasonDialog(context: Context, layout: Int) : CenterDialog(context, layout) {
        var mStringList: MutableList<String>? = null
        lateinit var mTags: List<String>
        private var mInputView: EditText? = null
        var id: Int = 0
            private set

        val selectedText: String
            get() {
                val sb = StringBuffer()
                val inputText = if (mInputView == null) "" else mInputView!!.text.toString().trim()
                if (mStringList == null) {
                    mStringList = ArrayList()
                }
                if (inputText != null && inputText.trim().isNotEmpty()) {
                    mStringList!!.add(inputText)
                }
                if (mStringList!!.size > 0) {
                    for (i in mStringList!!.indices) {
                        if (i == 0) {
                            sb.append(mStringList!![0])
                        } else {
                            sb.append("," + mStringList!![i])
                        }
                    }
                }
                return sb.toString()
            }

        init {
            window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }

        fun setExtra(questionId: Int) {
            this.id = questionId
        }

        override fun initView(view: View) {
            mStringList = ArrayList()
            mStringList!!.clear()
            //                        mTags = Arrays.asList("题目抄错", "题目不明白", "就是猜的啊")
            mTags = listOf("题目抄错", "题目不明白", "就是猜的啊")
            val flowLayout = view.findViewById<TagFlowLayout>(R.id.tagFlowLawout_reason)
            mInputView = view.findViewById(R.id.et_inputReason)
            val cancelBtn = view.findViewById<View>(R.id.cancelBtn)
            val okBtn = view.findViewById<View>(R.id.okBtn)
            cancelBtn.setOnClickListener(ClickLisen())
            okBtn.setOnClickListener(ClickLisen())
            flowLayout.adapter = object : TagAdapter<String>(mTags) {
                override fun getView(parent: FlowLayout, position: Int, s: String): View {
                    val v = LayoutInflater.from(parent.context).inflate(R.layout.item_tag_select, parent, false) as TextView
                    v.text = s
                    return v
                }
            }
            flowLayout.setOnTagClickListener { view, position, parent ->
                view.isSelected = !view.isSelected
                if (view.isSelected) {
                    mStringList!!.add(mTags[position])
                } else {
                    mStringList!!.remove(mTags[position])
                }
                true
            }
        }
    }

    /**
     * 收藏
     *
     * @param collect
     * @param questionId
     */
    private fun networkForCollect(collect: Int, questionId: Int, view: View) {
        val request = LiveSccUserWrongQuestionCollectRequest()
        request.userId = UserManager.getInstance().userId.toString()
        request.collect = collect
        request.userWrongQuestionId = questionId
        Utils.loading(this)
        Api.getInstance().postScc(request, LiveSccUserWrongQuestionCollectResponse::class.java, object : ResponseCallback<LiveSccUserWrongQuestionCollectResponse>() {
            override fun onResponse(date: LiveSccUserWrongQuestionCollectResponse, isFromCache: Boolean) {
                Utils.dismissLoading()
                if (collect == 1) {
                    ToastUtils.showShort("已收藏")
                } else {
                    ToastUtils.showShort("取消收藏")
                }
            }

            override fun onError() {
                super.onError()
                view.isSelected = !view.isSelected
                Utils.dismissLoading()
            }
        }, "")
    }

    /**
     * 删除题目
     *
     * @param questionId
     */
    private fun networkForDelete(questionId: Int) {
        val request = LiveSccUserWrongQuestionDeleteRequest()
        request.userId = UserManager.getInstance().userId.toString()
        request.userWrongQuestionId = questionId
        Utils.loading(this)
        Api.getInstance().postScc(request, LiveSccUserWrongQuestionDeleteResponse::class.java, object : ResponseCallback<LiveSccUserWrongQuestionDeleteResponse>() {
            override fun onResponse(date: LiveSccUserWrongQuestionDeleteResponse, isFromCache: Boolean) {
                ToastUtils.showShort("删除成功")
                Utils.dismissLoading()
            }
        }, "")
    }

    /**
     * 错因总结
     *
     * @param questionId
     * @param reason
     */
    private fun networkForReasonSummary(questionId: Int, reason: String) {
        val request = LiveSccUserWrongQuestionSaveReasonRequest()
        request.userId = UserManager.getInstance().userId.toString()
        request.userWrongQuestionId = questionId
        request.reason = reason
        Utils.loading(this)
        Api.getInstance().postScc(request, LiveSccUserWrongQuestionSaveReasonResponse::class.java, object : ResponseCallback<LiveSccUserWrongQuestionSaveReasonResponse>() {
            override fun onResponse(date: LiveSccUserWrongQuestionSaveReasonResponse, isFromCache: Boolean) {
                ToastUtils.showShort("提交成功")
                Utils.dismissLoading()
            }
        }, "")
    }

    /**
     * id获取错题详情
     */
    private fun networkForExerciseDetail(questionId: Int) {
        val request = LiveSccUserWrongQuestionDetailRequest()
        request.userId = UserManager.getInstance().userId.toString()
        request.userWrongQuestionId = questionId
        mEmptyView.showLoading()
        Api.getInstance().postScc(request, LiveSccUserWrongQuestionDetailResponse::class.java, object : ResponseCallback<LiveSccUserWrongQuestionDetailResponse>() {
            override fun onResponse(date: LiveSccUserWrongQuestionDetailResponse?, isFromCache: Boolean) {
                mEmptyView.showContent()
                updateUI(date!!.data)
            }

            override fun onEmpty(date: LiveSccUserWrongQuestionDetailResponse?, isFromCache: Boolean) {
                super.onEmpty(date, isFromCache)
                mEmptyView.showEmpty()
            }

            override fun onError() {
                super.onError()
                mEmptyView.showError()
            }

        }, "")
    }

    companion object {
        fun start(context: Context, id: Int) {
            var intent = Intent(context, AnswerAnalysisActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }
}
