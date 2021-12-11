package gaosi.com.learn.studentapp.aiscene

import android.os.Bundle
import android.view.View
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.utils.StatusBarUtil
import com.gsbaselib.utils.glide.ImageLoader
import gaosi.com.learn.R
import com.gsbaselib.base.inject.GSAnnotation
import com.gsbaselib.utils.customevent.OnNoDoubleClickListener
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.util.SchemeDispatcher
import kotlinx.android.synthetic.main.activity_scene_entrance.*

@Router("aiSceneEntrance")
@GSAnnotation(pageId = StatisticsDictionary.sceneEntrance)
class SceneEntranceActivity : BaseActivity() {

    private var mChineseTips: String? = ""
    private var mEnglishTips: String? = ""
    private var mSceneIcon: String? = ""
    private var mSceneId: String? = ""
    private var mVersion: String? = ""
    private var mSource: String? = ""//来源

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scene_entrance)
        adjustStatusBarMargin(vStatusBar2)
        getIntentData()
        showInfo()
    }

    override fun initView() {
        super.initView()
        ivBack.setOnClickListener(this)
        tvStart.setOnClickListener(object : OnNoDoubleClickListener(){
            override fun onNoDoubleClick(p0: View?) {
                var url = "axx://aiscene?sceneId=%s&version=%s&source=%s"
                url = if (mSource == "list") {
                    String.format(url, mSceneId, mVersion, "list")
                } else {
                    String.format(url, mSceneId, mVersion, "report")
                }
                SchemeDispatcher.jumpPage(this@SceneEntranceActivity, url)
                collectClickEvent("as601_clk_start")
            }
        })
    }

    private fun getIntentData() {
        intent?.apply {
            mSource = getStringExtra("source") ?: "list"
        }
        mChineseTips = AiSceneListActivity.mSceneInfo?.textGuide
        mEnglishTips = AiSceneListActivity.mSceneInfo?.englishGuide
        mSceneIcon = AiSceneListActivity.mSceneInfo?.entryScenesThumbnail
        mSceneId = AiSceneListActivity.mSceneInfo?.scenesId.toString()
        mVersion = AiSceneListActivity.mSceneInfo?.version.toString()
    }

    private fun showInfo() {
        tvChinese.text = mChineseTips
        tvEnglish.text = mEnglishTips
        ImageLoader.setImageViewResource(ivScene, mSceneIcon ?: "", R.drawable.icon_placeholder_scene_entrance)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.ivBack -> {
                finish()
                collectClickEvent("as601_clk_return")
            }
        }
    }

    override fun setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null)
    }

}
