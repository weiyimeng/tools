package gaosi.com.learn.studentapp.dresscity

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.base.GSBaseApplication
import com.gsbaselib.base.event.EventBean
import com.gsbaselib.base.inject.GSAnnotation
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSStringCallback
import com.gsbaselib.utils.LOGGER
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbaselib.utils.glide.ImageLoader
import com.gsbaselib.utils.net.NetworkUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.event.EventType
import com.gstudentlib.util.hy.HyConfig
import com.lzy.okgo.model.Response

import java.io.File

import gaosi.com.learn.R
import gaosi.com.learn.bean.dress.FaceUMyItem
import kotlinx.android.synthetic.main.activity_dress_center_detail.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

/**
 * 装扮详情
 * Created by test on 2018/5/22.
 */
@Router("dressDetail")
@GSAnnotation(pageId = StatisticsDictionary.dressDetail)
class DressDetailActivity : BaseActivity() {

    private var mConfirmButtom: TextView? = null

    companion object {
        private const val EXTRA_FACEUITEM = "faceUItem"
        private const val EXTRA_REMARK = "remark"
    }

    private var mFaceUMyItem: FaceUMyItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dress_center_detail)
        init()
        loadData()
    }

    private fun init() {
        mFaceUMyItem = intent.getSerializableExtra(EXTRA_FACEUITEM) as? FaceUMyItem
        val remark = intent.getStringExtra(EXTRA_REMARK)
        title_bar.setLeftListener(this)
        title_bar.setTitle(remark)
        tvMergeFragment.setOnClickListener(this)
    }

    /**
     * 加载数据
     */
    private fun loadData() {
        mFaceUMyItem ?: return
        ImageLoader.setImageViewFileNoCache(ivUrl, HyConfig.hyResourceImagePath + File.separator + mFaceUMyItem?.smallCode)
        tvName.text = mFaceUMyItem?.name
        tvCountStr.text = mFaceUMyItem?.countStr
        tvCountStr.isSelected = 0 != mFaceUMyItem?.count
        when {
            "SA" == mFaceUMyItem?.level -> this.adapterLevelValue(R.drawable.icon_s, "#FFBD14", "秋季限定")
            "SS" == mFaceUMyItem?.level -> this.adapterLevelValue(R.drawable.icon_special_s, "#50B8B4", "暑期限定")
            "S" == mFaceUMyItem?.level -> this.adapterLevelValue(R.drawable.icon_s, "#FFBD14", "传说")
            "B" == mFaceUMyItem?.level -> this.adapterLevelValue(R.drawable.icon_b, "#A5A5A5", "精良")
            "A" == mFaceUMyItem?.level -> this.adapterLevelValue(R.drawable.icon_a, "#A9DC35", "稀有")

        }
        tvDescription?.text = mFaceUMyItem?.itemDescription
        if (mFaceUMyItem?.count == 0) {
            showSplitInfo(true)
        }
    }

    private fun showSplitInfo(isVisible: Boolean) {
        if (isVisible) {
            llFragment.visibility = View.VISIBLE
            tvFragment.text = "我的装扮碎片：${mFaceUMyItem?.myDressSplit ?: "0"}"
            tvFragmnetTips.visibility = View.VISIBLE
            tvFragmnetTips.text = "（${mFaceUMyItem?.levelForSplit ?: "0"}装扮碎片可合成）"
            tvFragmnetTips.setTextColor(Color.parseColor("#A9DC35"))
            tvMergeFragment.visibility = View.VISIBLE
        } else {
            llFragment.visibility = View.INVISIBLE
            tvFragmnetTips.visibility = View.GONE
            tvMergeFragment.visibility = View.GONE
            tvCountStr.text = "已召唤"
            tvCountStr.isSelected = true
        }
    }

    /**
     * 等级适配
     * @param levelRes
     * @param levelColor
     * @param levelName
     */
    private fun adapterLevelValue(levelRes: Int, levelColor: String, levelName: String) {
        ivLevel.setImageResource(levelRes)
        tvLevelName.setTextColor(Color.parseColor(levelColor))
        tvLevelName.text = levelName
    }

    private fun requestMergeDressConfirm() {
        if (NetworkUtil.isConnected(GSBaseApplication.getApplication())) {
            val paramMap = HashMap<String, String>()
            paramMap["studentId"] = STBaseConstants.userId
            paramMap["code"] = mFaceUMyItem?.code ?: ""
            GSRequest.startRequest(GSAPI.mergeDressConfirm, paramMap, object : GSStringCallback() {
                override fun onResponseSuccess(response: Response<*>?, code: Int, result: String) {
                    if (isFinishing || isDestroyed) {
                        return
                    }
                    val json = JSONObject(result)
                    val status = json.optInt("status")
                    if (status == 1) {
                        DialogUtil.getInstance().dismiss()
                        ToastUtil.showToast("合成成功")
                        showSplitInfo(false)
                        val event = EventBean(EventType.MERGE_DRESS)
                        EventBus.getDefault().post(event)
                    } else {
                        mConfirmButtom?.isEnabled = true
                        ToastUtil.showToast("网络错误")
                    }
                }

                override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                    if (isFinishing || isDestroyed) {
                        return
                    }
                    mConfirmButtom?.isEnabled = true
                    message ?: return
                    ToastUtil.showToast(message)
                }
            })
        } else {
            ToastUtil.showToast("网络错误")
        }
    }

    private fun showMergeDialog() {
        try {
            val mySplit = mFaceUMyItem?.myDressSplit?.toInt() ?: 0
            val needSplit = mFaceUMyItem?.levelForSplit?.toInt() ?: 0
            if (needSplit > mySplit) {
                DialogUtil.getInstance().create(this, R.layout.ui_recycle_dress_unable)
                        .show(object : AbsAdapter() {
                            override fun bindListener(onClickListener: View.OnClickListener?) {
                                bindText(R.id.tvTitle, "合成装扮")
                                bindText(R.id.tvTips, "装扮碎片不足，不能合成")
                                bindText(R.id.tvMyFragment, "我的装扮碎片：$mySplit")
                                bindText(R.id.tvNeedFragment, "所需装扮碎片：$needSplit")
                                val ivType = findViewById<ImageView>(R.id.ivType)
                                ivType.setImageResource(R.drawable.icon_merge_fragment)
                                bindListener(onClickListener, R.id.tvKnow)
                            }

                            override fun onClick(v: View?, dialog: DialogUtil?) {
                                super.onClick(v, dialog)
                                when (v?.id) {
                                    R.id.tvKnow -> dialog?.dismiss()
                                }
                            }
                        })
            } else {
                DialogUtil.getInstance().create(this, R.layout.ui_recycle_dress)
                        .show(object : AbsAdapter() {
                            override fun bindListener(onClickListener: View.OnClickListener?) {
                                bindText(R.id.tvTitle, "合成装扮")
                                bindText(R.id.tvMyFragment, "我的装扮碎片：$mySplit")
                                bindText(R.id.tvNeedFragment, "所需装扮碎片：$needSplit")
                                var tips = "消耗%s装扮碎片合成%s?"
                                tips = String.format(tips, needSplit, mFaceUMyItem?.name)
                                bindText(R.id.tvTips, tips)
                                findViewById<TextView>(R.id.tvSmallTips).visibility = View.GONE
                                mConfirmButtom = findViewById(R.id.tvConfirm)
                                bindListener(onClickListener, R.id.tvCancel)
                                bindListener(onClickListener, R.id.tvConfirm)
                            }

                            override fun onClick(v: View?, dialog: DialogUtil?) {
                                super.onClick(v, dialog)
                                when (v?.id) {
                                    R.id.tvConfirm -> {
                                        mConfirmButtom?.isEnabled = false
                                        requestMergeDressConfirm()
                                        collectClickEvent("as1005_compound_clk_sure")
                                    }
                                    R.id.tvCancel -> dialog?.dismiss()
                                }
                            }
                        })
            }
        } catch (e: Exception) {
            LOGGER.log(e)
        }

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.llTitleLeft -> finish()
            R.id.tvMergeFragment -> {
                showMergeDialog()
                collectClickEvent("as1005_clk_compound")
            }
            else -> {
            }
        }
    }

    override fun getResources(): Resources {
        val resources = super.getResources()
        val configuration = resources.configuration
        configuration.fontScale = 1.0f
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return resources
    }
}
