package gaosi.com.learn.studentapp.aiscene

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.STBaseFragment
import com.gstudentlib.player.IPlayerView
import com.gstudentlib.player.OnPlayerStatusListener
import com.gstudentlib.player.Player
import gaosi.com.learn.R
import gaosi.com.learn.bean.aiscene.SceneStrongPrompt
import gaosi.com.learn.studentapp.aiscene.base.BaseAiSceneActivity
import gaosi.com.learn.studentapp.aiscene.core.AiSceneCoreService
import kotlinx.android.synthetic.main.item_strongprompt_ai_scene.*

/**
 * 作者：created by 逢二进一 on 2019/8/7 11:46
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
@RequiresApi(Build.VERSION_CODES.N)
class AiSceneStrongPromptFragment: STBaseFragment(), OnPlayerStatusListener {

    private var isVoicePrepared = false //语音播放是否准备完成
    private val mPlayer: IPlayerView by lazy { Player(0) }
    private var mSceneStrongPrompt: SceneStrongPrompt? = null
    private var mPosition = 0
    private var needAutoPlay = false

    companion object {
        fun instance(): AiSceneStrongPromptFragment {
            return AiSceneStrongPromptFragment()
        }
    }

    fun setPosition(position: Int) {
        this.mPosition = position
    }

    override fun initView(inflater: LayoutInflater?, container: ViewGroup?): View {
        return inflater?.inflate(R.layout.item_strongprompt_ai_scene, container, false) as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSceneStrongPrompt = (activity as AiSceneActivity).getStrongPromptByPosition(mPosition)
        this.initData()
    }

    private fun initData() {
        mSceneStrongPrompt?.let {
            tvQuestionEnglish.text = it.questionEnglish
            if(TextUtils.isEmpty(it.questionChinese)) {
                tvQuestionChinese.visibility = View.GONE
                tvQuestionChinese.text = ""
            }else {
                tvQuestionChinese.visibility = View.VISIBLE
                tvQuestionChinese.text = it.questionChinese
            }
            if(!TextUtils.isEmpty(it.audioUrl)) {
                mPlayer.setOnPlayerStatusListener(this)
                mPlayer.reCreate(it.audioUrl?:"")
            }
        }
        lavSceneVoice.setOnClickListener(this)
        this.playVoice()
    }

    private fun playVoice() {
        lavSceneVoice.setAnimation("scene_voice.json")
        lavSceneVoice.loop(true)
        lavSceneVoice.progress = 1.0f
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.lavSceneVoice -> {
                if(isVoicePrepared) {
                    if(mPlayer.isPlaying()) {
                        mPlayer.stop()
                        lavSceneVoice.progress = 1.0f
                        lavSceneVoice.pauseAnimation()
                        GSLogUtil.collectClickLog(StatisticsDictionary.aiScene, "as602_clk_tips_play", "")
                    }else {
                        if(AiSceneCoreService.instance.getRecordStatus() == AiScene.RECORD_STATUS.NONE
                                || AiSceneCoreService.instance.getRecordStatus() == AiScene.RECORD_STATUS.STOPED) {
                            (activity as AiSceneActivity).reset(BaseAiSceneActivity.PLAY_STRONGPROMPTS)
                            mPlayer.play()
                            lavSceneVoice.playAnimation()
                            GSLogUtil.collectClickLog(StatisticsDictionary.aiScene, "as602_clk_tips_stop", "")
                        }
                    }
                }
            }
        }
    }

    override fun onPrepared(tag: Int) {
        isVoicePrepared = true
        if(needAutoPlay) {
            this.play()
        }
    }

    override fun onCompletion(tag: Int) {
        needAutoPlay = false
        lavSceneVoice.progress = 1.0f
        lavSceneVoice.pauseAnimation()
    }

    /**
     * 点击提示播放
     */
    fun play() {
        needAutoPlay = true
        if(isVoicePrepared) {
            if(!mPlayer.isPlaying()) {
                if(AiSceneCoreService.instance.getRecordStatus() == AiScene.RECORD_STATUS.NONE
                        || AiSceneCoreService.instance.getRecordStatus() == AiScene.RECORD_STATUS.STOPED) {
                    (activity as AiSceneActivity).reset(BaseAiSceneActivity.PLAY_STRONGPROMPTS)
                    mPlayer.play()
                    lavSceneVoice.playAnimation()
                }
            }
        }
    }

    fun stopPlay() {
        try{
            needAutoPlay = false
            if(mPlayer.isPlaying()) {
                mPlayer.stop()
                lavSceneVoice.progress = 1.0f
                lavSceneVoice.pauseAnimation()
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        this.stopPlay()
    }

    override fun onDetach() {
        super.onDetach()
        try{
            mPlayer.destroy()
        }catch (e : Exception){}
    }

}