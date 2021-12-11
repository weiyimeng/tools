package gaosi.com.learn.studentapp.aiscene

import android.text.Spannable
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.gsbaselib.utils.ActivityCollector
import com.gsbaselib.utils.glide.ImageLoader
import gaosi.com.learn.R
import gaosi.com.learn.bean.aiscene.SceneInfo
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.view.View
import com.gsbaselib.InitBaseLib
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.SystemUtil
import com.gstudentlib.view.RoundCornerImageView
import org.json.JSONObject

/**
 * Created by huangshan on 2019/8/02.
 */
class AiSceneListAdapter : BaseQuickAdapter<SceneInfo, BaseViewHolder>(null) {

    private val TITLE = 1//标题
    private val LEFT_CONTENT = 2//内容
    private val RIGHT_CONTENT = 3
    private var mEasyList: List<SceneInfo>? = null
    private var mMiddleList: List<SceneInfo>? = null
    private var mHardList: List<SceneInfo>? = null
    private var mEasySize = 0
    private var mMiddleSize = 0
    private var mHardSize = 0
    private var mContentView: View? = null
    private var mTitleView: View? = null

    init {
        multiTypeDelegate = object : MultiTypeDelegate<SceneInfo>() {
            override fun getItemType(data: SceneInfo): Int {
                val level = data.scenesLevel
                var index = 0
                when (level) {
                    0 -> index = mEasyList?.indexOf(data) ?: 0
                    1 -> index = mMiddleList?.indexOf(data) ?: 0
                    2 -> index = mHardList?.indexOf(data) ?: 0
                }
                when (index % 2) {
                    0 -> {
                        when (getData().indexOf(data)) {
                            0, mEasySize, mEasySize + mMiddleSize -> return TITLE
                        }
                        return RIGHT_CONTENT
                    }
                    1 -> {
                        when (getData().indexOf(data)) {
                            0,
                            mEasySize,
                            mEasySize + mMiddleSize -> return TITLE

                        }
                        return LEFT_CONTENT
                    }
                }
                return TITLE
            }
        }
        multiTypeDelegate
                .registerItemType(TITLE, R.layout.item_scene_title)
                .registerItemType(LEFT_CONTENT, R.layout.item_scene_content_left)
                .registerItemType(RIGHT_CONTENT, R.layout.item_scene_content_right)
    }

    fun setEasyList(list: List<SceneInfo>) {
        mEasyList = list
        this.mEasySize = mEasyList?.size ?: 0
    }

    fun setMiddleList(list: List<SceneInfo>) {
        mMiddleList = list
        this.mMiddleSize = mMiddleList?.size ?: 0
    }

    fun setHardList(list: List<SceneInfo>) {
        mHardList = list
        this.mHardSize = mHardList?.size ?: 0
    }

    fun getView(type: Int): View? {
        when (type) {
            0 -> return mTitleView
            1 -> return mContentView
        }
        return mTitleView
    }

    override fun convert(helper: BaseViewHolder?, data: SceneInfo?) {
        when (getData().indexOf(data)) {
            0 -> {
                mTitleView = helper?.itemView
            }
            1 -> {
                mContentView = helper?.itemView
            }
        }
        when (helper?.itemViewType) {
            TITLE -> {
                when (data?.scenesLevel) {
                    0 -> {
                        helper.setText(R.id.tvType, "简单")
                        helper.setText(R.id.tvTips, "词汇量大于100")
                    }
                    1 -> {
                        helper.setText(R.id.tvType, "中等")
                        helper.setText(R.id.tvTips, "词汇量大于500")
                    }
                    2 -> {
                        helper.setText(R.id.tvType, "困难")
                        helper.setText(R.id.tvTips, "词汇量大于1000")
                    }
                }
            }
            LEFT_CONTENT,
            RIGHT_CONTENT -> {
                showContent(helper, data)
            }
        }
    }

    private fun showContent(helper: BaseViewHolder?, data: SceneInfo?) {
        helper ?: return
        val tvStatus = helper.getView<TextView>(R.id.tvStatus)
        val ivSence = helper.getView<RoundCornerImageView>(R.id.ivSence)
        ImageLoader.setImageViewResource(ivSence, data?.scenesThumbnail
                ?: "", R.drawable.icon_placeholder_scene_list)
        ivSence.setOnClickListener {
            if (ActivityCollector.getInstance().currentActivity is AiSceneListActivity) {
                (ActivityCollector.getInstance().currentActivity as AiSceneListActivity).getScrollY()
            }
            AiSceneListActivity.mSceneInfo = data
            if (data?.studentScenesStaus == 1) {
                val suffix = "AiStudyReport.web.js"
                val params = JSONObject()
                params.put("studentId", STBaseConstants.userId)
                params.put("scenesId", data.scenesId)
                params.put("scenesStatus", data.scenesStatus)
                params.put("version", data.version)
                params.put("source", "list")
                params.put("callback", "0")
                SchemeDispatcher.gotoWebPage(ActivityCollector.getInstance().currentActivity, InitBaseLib.getInstance().configManager.h5ServerUrl + suffix, SystemUtil.generateDefautJsonStr(params, suffix))
            } else {
                var url = "axx://aiSceneEntrance?chineseTips=%s&englishTips=%s&sceneIcon=%s&sceneId=%s&version=%s"
                url = String.format(url, data?.textGuide, data?.englishGuide, data?.entryScenesThumbnail, data?.scenesId.toString(), data?.version.toString())
                SchemeDispatcher.jumpPage(ActivityCollector.getInstance().currentActivity, url)
            }
            GSLogUtil.collectClickLog(StatisticsDictionary.sceneList, "as600_clk_topic_card", data?.scenesId.toString())
        }
        helper.setText(R.id.tvTitle, data?.scenesName)
        if (data?.studentScenesStaus == 1) {
            tvStatus.setBackgroundResource(R.drawable.icon_scene_round_green)
            tvStatus.textSize = 16F
            val textStr = (data.studentScenesScore ?: 0).toString() + "分"
            val spannableString = SpannableStringBuilder(textStr)
            val span = RelativeSizeSpan(0.5F)
            spannableString.setSpan(span, textStr.length - 1, textStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            tvStatus.text = spannableString
        } else {
            tvStatus.setBackgroundResource(R.drawable.icon_scene_round_gray)
            tvStatus.textSize = 12F
            tvStatus.text = "未练习"
        }
        when (getData().indexOf(data)) {
            1,
            mEasySize + 1,
            mEasySize + mMiddleSize + 1 -> {
                helper.setVisible(R.id.ivStep, false)
            }
            else -> {
                helper.setVisible(R.id.ivStep, true)
            }
        }
    }
}
