package gaosi.com.learn.studentapp.aiscene

import android.Manifest
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewPager
import android.text.Html
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.*
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.app.hubert.guide.NewbieGuide
import com.app.hubert.guide.core.Controller
import com.app.hubert.guide.listener.OnGuideChangedListener
import com.app.hubert.guide.model.GuidePage
import com.app.hubert.guide.model.HighLight
import com.app.hubert.guide.model.HighlightOptions
import com.app.hubert.guide.model.RelativeGuide
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.base.GSBaseConstants
import com.gsbaselib.base.adapter.CommentVpAdapter
import com.gsbaselib.base.event.EventBean
import com.gsbaselib.base.inject.GSAnnotation
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.net.callback.GSStringCallback
import com.gsbaselib.utils.*
import com.gsbaselib.utils.glide.ImageLoader
import com.gsbaselib.utils.net.NetworkUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.event.EventType
import com.lzy.okgo.model.Response
import gaosi.com.learn.R
import gaosi.com.learn.bean.aiscene.*
import gaosi.com.learn.studentapp.aiscene.base.BaseAiSceneActivity
import gaosi.com.learn.studentapp.aiscene.core.AiSceneCoreLaunchParam
import gaosi.com.learn.studentapp.aiscene.core.bean.AiSceneCoreRequestBean
import gaosi.com.learn.studentapp.aiscene.core.AiSceneCoreService
import gaosi.com.learn.studentapp.aiscene.core.OnLaunchListener
import gaosi.com.learn.studentapp.aiscene.core.OnVoiceProgressListener
import gaosi.com.learn.studentapp.aiscene.core.bean.AiSceneResult
import gaosi.com.learn.studentapp.aiscene.core.bean.AiSceneResultBean
import kotlinx.android.synthetic.main.activity_ai_scene.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.util.*
import kotlin.collections.HashMap

/**
 * 作者：created by 逢二进一 on 2019/8/5 09:58
 * 邮箱：dingyuanzheng@gaosiedu.com
 * 1、狐狸在说话的过程中是不可以进行录音操作的，可以通过点击狐狸停止说话进行录音
 */
@RequiresApi(Build.VERSION_CODES.N)
@Router("aiscene")
@GSAnnotation(pageId = StatisticsDictionary.aiScene)
class AiSceneActivity: BaseAiSceneActivity(), ViewPager.OnPageChangeListener {

    private var mStrongFragmentPosition = 0 //当前强提示的滑动位置
    private var mIsShowed = false //是否显示过对话
    private var mController: Controller? = null
    private var mThreeNotSayRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.getData()
    }

    private fun getData() {
        llLoading.visibility = View.VISIBLE
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["scenesId"] = mSceneId ?: ""
        paramMap["version"] = mVersion ?: ""
        GSRequest.startRequest(GSAPI.getDialogeInfo, paramMap, object : GSJsonCallback<SceneBean>() {
            override fun onResponseError(response: Response<*>?, p1: Int, message: String?) {
                if (isFinishing || isDestroyed) {
                    return
                }
                llLoading.visibility = View.INVISIBLE
                llLoading.removeAllViews()
                message ?: return
                ToastUtil.showToast(message)
            }

            override fun onResponseSuccess(response: Response<*>?, p1: Int, result: GSHttpResponse<SceneBean>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                if(showResponseErrorMessage(result) == 0) {
                    finish()
                    return
                }
                //收集异常日志
                if (result.body == null) {
                    llLoading.visibility = View.INVISIBLE
                    llLoading.removeAllViews()
                    return
                }
                mSceneBean = result.body
                mDialogues = mSceneBean?.dialogues ?: return
                mCurrDialoguePosition = 0
                Glide.with(this@AiSceneActivity).load(mSceneBean?.scenesBgImgUrl?:"").listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        loadData()
                        return false
                    }
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        loadData()
                        return false
                    }
                }).into(mivSceneBg)
            }
        })
    }

    /**
     * 加载数据
     */
    private fun loadData() {
        llLoading.visibility = View.INVISIBLE
        llLoading.removeAllViews()
        val aiSceneGuide = SharedPreferenceUtil.getBooleanValueFromSP("userInfo", STBaseConstants.userId + "_aiSceneGuide", false)
        if(!aiSceneGuide) {
            SharedPreferenceUtil.setBooleanDataIntoSP("userInfo", STBaseConstants.userId + "_aiSceneGuide", true)
            this.showGuide()
        }else {
            this.show()
        }
    }

    /**
     * 行为统计: 1翻译  2强提示
     */
    private fun requestDialogueBehavior(type : Int) {
        val json = JSONObject()
        json["studentId"] = STBaseConstants.userId
        json["scenesId"] = mSceneId
        json["dialogueNumId"] = mDialogues?.get(mCurrDialoguePosition)?.dialogueNum
        if (type == 1) {
            json["translationCount"] = 1
        } else {
            json["promptCount"] = 1
        }
        val params = mapOf<String, String>(
                "params" to JSON.toJSONString(json)
        )
        GSRequest.startRequest(GSAPI.dialogueBehavior, params, object : GSStringCallback() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: String) {
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
            }
        })
    }

    /**
     * 展示对话组
     * 首先判断是否存在图片展示，展示响应的图片
     * 其次将弱提示与强提示提前显示上去
     * 最后进行sayHello操作，在sayHello之后进行狐狸语音的播放
     */
    private fun show() {
        this.mIsShowed = true
        this.mIsShowThreeTips = false
        mDialogues?.let {
            val dialogue = it[mCurrDialoguePosition]
            if (dialogue.teacherQuestionItem == null) {
                return
            }
            this.updateDialogueProgress(false)
            tvQuestionEnglish.movementMethod = ScrollingMovementMethod.getInstance()
            val strBuilder = StringBuilder()
            strBuilder.append("<html><font style= 'font-color: #051535;'><b><big>").append(dialogue.teacherQuestionItem?.questionEnglish).append("</big></b></font><br>")
            strBuilder.append("<font style= 'font-color: #D8BFD8;'>").append(dialogue.teacherQuestionItem?.questionChinese).append("</font></html>")
            if(GSBaseConstants.deviceInfoBean.systemVersion >= Build.VERSION_CODES.N) {
                tvQuestionEnglish.text = Html.fromHtml(strBuilder.toString() , Html.FROM_HTML_MODE_LEGACY)
            }else {
                tvQuestionEnglish.text = Html.fromHtml(strBuilder.toString())
            }
            if(!TextUtils.isEmpty(dialogue.teacherQuestionItem?.promptImg) && !TextUtils.isEmpty(dialogue.teacherQuestionItem?.promptText)) {
                showPromptImg()
                hidePromptText()
                ImageLoader.setImageViewResource(ivPromptImg, dialogue.teacherQuestionItem?.promptImg
                        ?: "", R.drawable.icon_placeholder_scene_dialog)
                tvPromptText.text = dialogue.teacherQuestionItem?.promptText
            }else if(TextUtils.isEmpty(dialogue.teacherQuestionItem?.promptImg) && TextUtils.isEmpty(dialogue.teacherQuestionItem?.promptText)) {
                hidePromptImg()
                hidePromptText()
            }else {
                if(!TextUtils.isEmpty(dialogue.teacherQuestionItem?.promptImg)) {
                    showPromptImg()
                    hidePromptText()
                    rlPromptImageAndText.visibility = View.INVISIBLE
                    tvPromptText.visibility = View.INVISIBLE
                    ImageLoader.setImageViewResource(ivPromptImg, dialogue.teacherQuestionItem?.promptImg
                            ?: "", R.drawable.icon_placeholder_scene_dialog)
                }else {
                    showPromptText()
                    hidePromptImg()
                    tvPromptOnlyText.text = dialogue.teacherQuestionItem?.promptText
                }
            }
            mStrongFragmentList.clear()
            this.mStrongFragmentPosition = 0
            if(dialogue.strongPrompts != null && dialogue.strongPrompts?.size != 0) {
                for(index in dialogue?.strongPrompts?.indices!!) {
                    val aiSceneStrongPromptFragment = AiSceneStrongPromptFragment.instance()
                    aiSceneStrongPromptFragment.setPosition(index)
                    mStrongFragmentList.add(aiSceneStrongPromptFragment)
                }
                mCommentVpAdapter = CommentVpAdapter(supportFragmentManager, mStrongFragmentList)
                vpStrongPrompt.adapter = mCommentVpAdapter!!
                vpStrongPrompt.offscreenPageLimit = mStrongFragmentList.size
                vpStrongPrompt.pageMargin = TypeValue.dp2px(16f)
                vpStrongPrompt.addOnPageChangeListener(this)
            }
            tvWeakPrompt.text = dialogue.weakPromptText
            this.reset(SAY_HELLO)
            this.standby()
            this.isPreparedSuccess = false
            this.mPlayer.setOnPlayerStatusListener(this)
            this.mPlayer.reCreate(dialogue.teacherQuestionItem?.audioUrl?:"")
        }
    }

    /**
     * 展示下组对话
     */
    override fun showNextDialogue() {
        super.showNextDialogue()
        mHandler.postDelayed( {
            mDialogues?.let {
                var dialogue: SceneDialogues? = null
                for(i in it.indices) {
                    if(mCurrDialogueNum == it[i].dialogueNum) {
                        dialogue = it[i]
                        mCurrDialoguePosition = i
                        break
                    }
                }
                updateDialogueProgress(false)
                if (dialogue?.teacherQuestionItem == null) {
                    ToastUtil.showToast("对话组跳转失败！")
                    return@let
                }
                tvQuestionEnglish.movementMethod = ScrollingMovementMethod.getInstance()
                val strBuilder = StringBuilder()
                strBuilder.append("<html><font style= 'font-color: #051535;'><b><big>").append(dialogue?.teacherQuestionItem?.questionEnglish).append("</big></b></font><br>")
                strBuilder.append("<font style= 'font-color: #D8BFD8;'>").append(dialogue?.teacherQuestionItem?.questionChinese).append("</font></html>")
                if(GSBaseConstants.deviceInfoBean.systemVersion >= Build.VERSION_CODES.N) {
                    tvQuestionEnglish.text = Html.fromHtml(strBuilder.toString() , Html.FROM_HTML_MODE_LEGACY)
                }else {
                    tvQuestionEnglish.text = Html.fromHtml(strBuilder.toString())
                }
                if(!TextUtils.isEmpty(dialogue.teacherQuestionItem?.promptImg) && !TextUtils.isEmpty(dialogue.teacherQuestionItem?.promptText)) {
                    showPromptImg()
                    hidePromptText()
                    ImageLoader.setImageViewResource(ivPromptImg, dialogue.teacherQuestionItem?.promptImg
                            ?: "", R.drawable.icon_placeholder_scene_dialog)
                    tvPromptText.text = dialogue.teacherQuestionItem?.promptText
                }else if(TextUtils.isEmpty(dialogue.teacherQuestionItem?.promptImg) && TextUtils.isEmpty(dialogue.teacherQuestionItem?.promptText)) {
                    hidePromptImg()
                    hidePromptText()
                }else {
                    if(!TextUtils.isEmpty(dialogue.teacherQuestionItem?.promptImg)) {
                        showPromptImg()
                        hidePromptText()
                        rlPromptImageAndText.visibility = View.INVISIBLE
                        tvPromptText.visibility = View.INVISIBLE
                        ImageLoader.setImageViewResource(ivPromptImg, dialogue.teacherQuestionItem?.promptImg
                                ?: "", R.drawable.icon_placeholder_scene_dialog)
                    }else {
                        showPromptText()
                        hidePromptImg()
                        tvPromptOnlyText.text = dialogue.teacherQuestionItem?.promptText
                    }
                }
                mCommentVpAdapter?.clearFragments()
                mStrongFragmentList.clear()
                this.mStrongFragmentPosition = 0
                if(dialogue?.strongPrompts != null && dialogue?.strongPrompts?.size != 0) {
                    for(index in dialogue?.strongPrompts?.indices!!) {
                        val aiSceneStrongPromptFragment = AiSceneStrongPromptFragment.instance()
                        aiSceneStrongPromptFragment.setPosition(index)
                        mStrongFragmentList.add(aiSceneStrongPromptFragment)
                    }
                    mCommentVpAdapter = CommentVpAdapter(supportFragmentManager, mStrongFragmentList)
                    vpStrongPrompt.adapter = mCommentVpAdapter!!
                    vpStrongPrompt.offscreenPageLimit = mStrongFragmentList.size
                    vpStrongPrompt.pageMargin = TypeValue.dp2px(16f)
                    vpStrongPrompt.addOnPageChangeListener(this)
                }
                tvWeakPrompt.text = dialogue?.weakPromptText
                this.standby()
                this.isPreparedSuccess = false
                this.mPlayer.setOnPlayerStatusListener(this)
                this.mPlayer.reCreate(dialogue?.teacherQuestionItem?.audioUrl?:"")
            }
        } , 500)
    }

    /**
     * 监听事件
     */
    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.ivFanyi -> {
                if(mSceneBean == null) {
                    return
                }
                this.reset(FANYI)
                val biJsonObject = org.json.JSONObject()
                biJsonObject?.put("scenesId" , mSceneId)
                collectClickEvent("as602_clk_translate" , biJsonObject.toString())
            }
            R.id.ivRecorder -> {
                if(mSceneBean == null) {
                    return
                }
                if (!hasPermissions(Manifest.permission.RECORD_AUDIO)) {
                    requestPermissions("录音权限", REQUEST_READ_PHONE , Manifest.permission.RECORD_AUDIO)
                }else {
                    this.startRecorder()
                }
            }
            R.id.ivTips -> {
                if(mSceneBean == null) {
                    return
                }
                this.reset(STRONG_PROMPTS)
                val biJsonObject = org.json.JSONObject()
                biJsonObject?.put("scenesId" , mSceneId)
                collectClickEvent("as602_clk_tips" , biJsonObject.toString())
            }
            R.id.vFoxClick -> {
                if(mSceneBean == null) {
                    return
                }
                this.reset(TO_SAY)
            }
            R.id.ivTitleRight -> {
                if(mSceneBean == null) {
                    return
                }
                this.showGuide()
                val biJsonObject = org.json.JSONObject()
                biJsonObject?.put("scenesId" , mSceneId)
                collectClickEvent("as602_clk_guide" , biJsonObject.toString())
            }
            R.id.ivBack -> {
                exitScene()
                val biJsonObject = org.json.JSONObject()
                biJsonObject?.put("scenesId" , mSceneId)
                collectClickEvent("as602_clk_return" , biJsonObject.toString())
            }
        }
    }

    /**
     * 依据当前的操作
     * 重置属于当前操作应该显示的按钮
     */
    override fun reset(status: Int) {
        super.reset(status)
        when(status) {
            //首次进入当前情景
            NONE -> {
                hidePromptImg() //图片提示
                hidePromptText() //文字提示
                hideQuestion() //翻译
                hideWeakPrompt() //弱提示
                hideStrongPrompt() //强提示
                hideThreeNotSay() //3s未说话提示
                hideWeakPromptGuide() //第一次未通过
                tvVoiceTips.visibility = View.INVISIBLE //声音提示
                tvRecorderTips.visibility = View.INVISIBLE //录音提示
                AiSceneCoreService.instance.updateStatus(AiScene.RECORD_STATUS.NONE)
            }
            //狐狸在打招呼
            SAY_HELLO -> {
                AiSceneCoreService.instance.updateStatus(AiScene.RECORD_STATUS.NONE)
//                this.sayHello()
                isSayHelloSuccess = true
            }
            //语音准备中
            VOICE_PREPARE -> {
                AiSceneCoreService.instance.updateStatus(AiScene.RECORD_STATUS.NONE)
            }
            //狐狸开始说话，首先需要判断当前的是否在评测中
            //评测中不进行处理，同时在播放时需要判断是否在播放强提示
            //在结束评测的时候存在进行处理
            TO_SAY -> {
                if(AiSceneCoreService.instance.getRecordStatus() == AiScene.RECORD_STATUS.RECORDING
                        || AiSceneCoreService.instance.getRecordStatus() == AiScene.RECORD_STATUS.TESTING) {
                    return
                }
                if(mIsTestEnd) {
                    return
                }
                AiSceneCoreService.instance.updateStatus(AiScene.RECORD_STATUS.NONE)
                if(isSayHelloSuccess) {
                    this.play()
                }
                //隐藏第一次不通过提醒
                if(llWeakPromptGuide.visibility == View.VISIBLE) {
                    val biJsonObject = org.json.JSONObject()
                    biJsonObject?.put("scenesId" , mSceneId)
                    collectClickEvent("XSD_246" , biJsonObject.toString())
                    hideWeakPromptGuide()
                }
            }
            //说话中
            SAYINT -> {
                //说话过程中关闭3s未说话手势提醒
                if(mThreeNotSayRunnable != null) {
                    mHandler.removeCallbacks(mThreeNotSayRunnable)
                    mThreeNotSayRunnable = null
                }
                hideThreeNotSay()
            }
            //狐狸带着不动，此时可以进行一些其他的操作，比如说话等
            STANDBY -> {
                AiSceneCoreService.instance.updateStatus(AiScene.RECORD_STATUS.STOPED)
                this.standby()
                //未显示过3s未说提醒
                if(!this.mIsShowThreeTips) {
                    if(llThreeNotSay.visibility == View.INVISIBLE) {
                        this.mIsShowThreeTips = true
                        if(mThreeNotSayRunnable != null) {
                            mHandler.removeCallbacks(mThreeNotSayRunnable)
                            mThreeNotSayRunnable = null
                        }
                        mThreeNotSayRunnable = createNotSayRunnable()
                        mHandler.postDelayed(mThreeNotSayRunnable , 3000)
                    }
                }
            }
            //录音中，此时需要判断当前的强提示是否在显示状态，同时关闭当前的强提示播放
            RECORDING -> {
                if(llWeakPrompt.visibility == View.VISIBLE) {
                    llWeakPrompt.visibility = View.INVISIBLE
                }
                if(vpStrongPrompt.visibility == View.VISIBLE) {
                    if(mStrongFragmentList.size > 0) {
                        (mStrongFragmentList[this.mStrongFragmentPosition] as AiSceneStrongPromptFragment).stopPlay()
                    }
                }
                //录音过程中关闭3s未说话手势提醒
                if(llThreeNotSay.visibility == View.INVISIBLE) {
                    if(mThreeNotSayRunnable != null) {
                        mHandler.removeCallbacks(mThreeNotSayRunnable)
                        mThreeNotSayRunnable = null
                    }
                }else {
                    hideThreeNotSay()
                }
                //录音过程中关闭第一次未通过提醒
                if(llWeakPromptGuide.visibility == View.VISIBLE) {
                    hideWeakPromptGuide()
                }
            }
            //录音中，此时需要判断当前的强提示是否在显示状态，同时关闭当前的强提示播放
            TESTING -> {
                if(llWeakPrompt.visibility == View.VISIBLE) {
                    llWeakPrompt.visibility = View.INVISIBLE
                }
                if(vpStrongPrompt.visibility == View.VISIBLE) {
                    if(mStrongFragmentList.size > 0) {
                        (mStrongFragmentList[this.mStrongFragmentPosition] as AiSceneStrongPromptFragment).stopPlay()
                    }
                }
            }
            FANYI -> {
                if (llQuestion.visibility == View.INVISIBLE) {
                    this.showQuestion()
                    requestDialogueBehavior(1)
                } else {
                    hideQuestion()
                }
                //隐藏第一次不通过提醒
                if(llWeakPromptGuide.visibility == View.VISIBLE) {
                    hideWeakPromptGuide()
                }
            }
            STRONG_PROMPTS -> {
                if(!isPreparedSuccess) {
                    return
                }
                if(vpStrongPrompt.visibility == View.INVISIBLE) {
                    if(llWeakPrompt.visibility == View.VISIBLE) {
                        llWeakPrompt.visibility = View.INVISIBLE
                    }
                    showStrongPrompt()
                    if(mStrongFragmentList.size > 0) {
                        this.mStrongFragmentPosition = 0
                        vpStrongPrompt.currentItem = this.mStrongFragmentPosition
                    }
                    (mStrongFragmentList[this.mStrongFragmentPosition] as AiSceneStrongPromptFragment).play()
                    requestDialogueBehavior(2)
                }else {
                    hideStrongPrompt()
                    (mStrongFragmentList[this.mStrongFragmentPosition] as AiSceneStrongPromptFragment).stopPlay()
                }
            }
            //强提示播放中，判断当前的狐狸是否在说话
            PLAY_STRONGPROMPTS -> {
                if(mPlayer.isPlaying()) {
                    mPlayer.stop()
                }
                this.reset(STANDBY)
            }
            SHOW_WEAK_PROMPT -> {
                if(llWeakPrompt.visibility == View.VISIBLE) {
                    hideWeakPrompt()
                }else {
                    if(vpStrongPrompt.visibility == View.VISIBLE) {
                        hideStrongPrompt()
                        (mStrongFragmentList[this.mStrongFragmentPosition] as AiSceneStrongPromptFragment).stopPlay()
                    }
                    showWeakPrompt()
                }
                //第一次展示弱提示
                if(llWeakPromptGuide.visibility == View.INVISIBLE) {
                    showWeakPromptGuide()
                }
            }
            PLAY_END_SPEECH -> {
                hidePromptImg() //图片提示
                hidePromptText() //文字提示
                hideQuestion() //翻译
                hideWeakPrompt() //弱提示
                hideStrongPrompt() //强提示
                hideThreeNotSay() //3s未说话提示
                hideWeakPromptGuide() //第一次未通过
                hideProgress() //进度条
                tvVoiceTips.visibility = View.INVISIBLE //声音提示
                tvRecorderTips.visibility = View.INVISIBLE //录音提示
                ivRecorder.visibility = View.INVISIBLE //录音按钮
                ivFanyi.visibility = View.INVISIBLE //翻译按钮
                ivTips.visibility = View.INVISIBLE //强提示按钮
                ivTitleRight.visibility = View.INVISIBLE
                ivBack.visibility = View.INVISIBLE
                isPreparedSuccess = false
                mIsPlayEndSuccess = false
                mPlayer.setOnPlayerStatusListener(this)
                mPlayer.reCreate(mSceneBean?.endSpeechUrl?:"")
                requestChallengeTheStatus()
                updateDialogueProgress(true)
                return
            }
            END -> {
                hidePromptImg()//图片提示
                hidePromptText() //文字提示
                hideQuestion() //翻译
                hideWeakPrompt() //弱提示
                hideStrongPrompt() //强提示
                hideThreeNotSay() //3s未说话提示
                hideWeakPromptGuide() //第一次未通过
                hideProgress() //进度条
                tvVoiceTips.visibility = View.INVISIBLE //声音提示
                tvRecorderTips.visibility = View.INVISIBLE //录音提示
                ivRecorder.visibility = View.INVISIBLE //录音按钮
                ivFanyi.visibility = View.INVISIBLE //翻译按钮
                ivTips.visibility = View.INVISIBLE //强提示按钮
                ivTitleRight.visibility = View.INVISIBLE
                ivBack.visibility = View.INVISIBLE
                return
            }
        }
        this.changeRecordBtnStatus()
        this.updateBtnStatus()
    }

    /**
     * 开启录音
     */
    private fun startRecorder() {
        val currentTime = Calendar.getInstance().timeInMillis
        if (currentTime - this.lastClickTime <= this.MIN_CLICK_DELAY_TIME) {
            return
        }
        this.lastClickTime = currentTime
        if (AiSceneCoreService.instance.getRecordStatus() == AiScene.RECORD_STATUS.NONE) {
            return
        }
        mDialogues?.let {
            val dialogue = it[mCurrDialoguePosition]
            if (AiSceneCoreService.instance.getRecordStatus() == AiScene.RECORD_STATUS.STOPED) {
                if(llThreeNotSay.visibility == View.VISIBLE) {
                    val biJsonObject = org.json.JSONObject()
                    biJsonObject?.put("scenesId" , mSceneId)
                    collectClickEvent("XSD_245" , biJsonObject.toString())
                }else {
                    val biJsonObject = org.json.JSONObject()
                    biJsonObject?.put("scenesId" , mSceneId)
                    collectClickEvent("as602_clk_start_recording" , biJsonObject.toString())
                }
                val aiSceneCoreLaunchParam = AiSceneCoreLaunchParam()
                aiSceneCoreLaunchParam.setSoundIntensityEnable(true)
                aiSceneCoreLaunchParam.setScenesId(mSceneId)
                val aiSceneCoreRequestBean = AiSceneCoreRequestBean()
                aiSceneCoreRequestBean.strongPrompts = dialogue.strongPrompts
                aiSceneCoreRequestBean.studentAnswerItems = dialogue.studentAnswerItems
                aiSceneCoreLaunchParam.setBranches(aiSceneCoreRequestBean)
                AiSceneCoreService.instance.recordStart(aiSceneCoreLaunchParam, object : OnLaunchListener {
                    override fun onBeforeLaunch(var1: Long) {
                        if(isFinishing || isDestroyed) {
                            return
                        }
                        val biJsonObject = org.json.JSONObject()
                        biJsonObject?.put("scenesId" , mSceneId)
                        biJsonObject?.put("time" , var1.toString())
                        collectClickEvent("as602_clk_end_recording" , biJsonObject.toString())
                        aspVoice.stop()
                        reset(TESTING)
                    }
                    override fun onRealTimeVolume(var1: Int) {
                        if(isFinishing || isDestroyed) {
                            return
                        }
                        avvVoiceSize.updateVoiceSize(var1)
                    }
                    override fun onAfterLaunch(var1: Int, var2: String?, var3: File?) {
                        if(isFinishing || isDestroyed) {
                            return
                        }
                        if(var1 == 1) {
                            val aiSceneResult = GSRequest.getConverterFactory().StringToObjectConverter(AiSceneResultBean::class.java).convert(var2)
                            requestDialogueAnswer(aiSceneResult as AiSceneResultBean?)
                        }else {
                            if(NetworkUtil.isConnected(this@AiSceneActivity)) {
                                ToastUtil.showToast("录音上传出错，请重新录音")
                            }else {
                                ToastUtil.showToast("网络连接异常")
                            }
                            reset(STANDBY)
                        }
                    }
                    override fun onError(var1: Int, var2: String) {
                        if(isFinishing || isDestroyed) {
                            return
                        }
                        if(NetworkUtil.isConnected(this@AiSceneActivity)) {
                            ToastUtil.showToast("录音上传出错，请重新录音")
                        }else {
                            ToastUtil.showToast("网络连接异常")
                        }
                        aspVoice.stop()
                        reset(STANDBY)
                    }
                })
                this.reset(RECORDING)
            } else if (AiSceneCoreService.instance.getRecordStatus() == AiScene.RECORD_STATUS.RECORDING) {
                aspVoice.stop()
            }
        }
    }

    /**
     * 上传评测结果
     */
    private fun requestDialogueAnswer(aiSceneResultBean: AiSceneResultBean?) {
        if(aiSceneResultBean != null) {
            when(aiSceneResultBean.code) {
                "10000","10001","10003","10004","10005","10006" -> {
                    ToastUtil.showToast("录音上传出错，请重新录音")
                    reset(STANDBY)
                }
                "10007" -> {
                    ToastUtil.showToast("好像没有声音，是不是话筒或操作不对劲？")
                    reset(STANDBY)
                }
                "200" -> {
                    if(!NetworkUtil.isConnected(this@AiSceneActivity)) {
                        ToastUtil.showToast("网络连接异常")
                        return
                    }
                    val aiSceneResult = aiSceneResultBean.data as AiSceneResult
                    aiSceneResult?.let {
                        val json = JSONObject()
                        val isLast = it.recommendBranchId == -1
                        json["studentId"] = STBaseConstants.userId
                        json["scenesId"] = mSceneId
                        json["dialogueNumId"] = mDialogues?.get(mCurrDialoguePosition)?.dialogueNum
                        json["answer"] = it.audioUrl
                        json["pronunciationScore"] = it.speechAssessment
                        json["semanticsScore"] = it.semanticAssessment
                        json["expressScore"] = it.grammaticalAssessment
                        json["jumpNext"] = it.recommendBranchId
                        json["assessmentTasksId"] = it.taskId
                        json["lastOne"] = if(isLast) "1" else "0"
                        val params = mapOf<String, String>(
                                "params" to JSON.toJSONString(json)
                        )
                        GSRequest.startRequest(GSAPI.dialogueAnswer , params , object: GSJsonCallback<DialogueAnswerBean>(){
                            override fun onResponseError(p0: Response<*>?, p1: Int, message: String?) {
                                if (isFinishing || isDestroyed) {
                                    return
                                }
                                reset(STANDBY)
                                message ?: return
                                ToastUtil.showToast("录音上传出错，请重新录音")
                            }

                            override fun onResponseSuccess(response: Response<*>?, p1: Int, result: GSHttpResponse<DialogueAnswerBean>) {
                                if (isFinishing || isDestroyed) {
                                    return
                                }
                                reset(STANDBY)
                                if (result.body == null) {
                                    ToastUtil.showToast("录音上传出错，请重新录音")
                                    return
                                }
                                val body = result.body?: return
                                if(body.status == 3) {
                                    mTestCount ++ //计算当前评测次数
                                    when (mTestCount) {
                                        1 -> { //显示弱提示
                                            reset(SHOW_WEAK_PROMPT)
                                        }
                                        2 -> {//显示强提示
                                            if(vpStrongPrompt.visibility == View.INVISIBLE) {
                                                reset(STRONG_PROMPTS)
                                            }
                                        }
                                        3 -> {//跳转下一组对话
                                            if(isLast) {
                                                mIsTestEnd = isLast
                                                reset(PLAY_END_SPEECH)
                                            }else {
                                                mCurrDialogueNum = it.recommendBranchId?:0
                                                showNextDialogue()
                                            }
                                        }
                                    }
                                }else if(body.status == 1) {
                                    if(isLast) {
                                        mIsTestEnd = isLast
                                    }else {
                                        mCurrDialogueNum = it.recommendBranchId?:0
                                    }
                                    excellent()
                                }else if(body.status == 2) {
                                    if(isLast) {
                                        mIsTestEnd = isLast
                                    }else {
                                        mCurrDialogueNum = it.recommendBranchId?:0
                                    }
                                    wellDone()
                                }
                            }
                        })
                    }
                }
            }
        }else {
            if(!NetworkUtil.isConnected(this@AiSceneActivity)) {
                ToastUtil.showToast("网络连接异常")
            }else {
                ToastUtil.showToast("录音上传出错，请重新录音")
            }
        }
    }

    /**
     * 获取挑战状态
     */
    private fun requestChallengeTheStatus() {
        val json = JSONObject()
        json["studentId"] = STBaseConstants.userId
        json["scenesId"] = mSceneId
        val params = mapOf<String, String>(
                "params" to JSON.toJSONString(json)
        )
        GSRequest.startRequest(GSAPI.challengeTheStatus , params , object: GSJsonCallback<ChallengeTheBean>(){
            override fun onResponseError(p0: Response<*>?, p1: Int, message: String?) {
                if (isFinishing || isDestroyed) {
                    return
                }
                message ?: return
                ToastUtil.showToast(message)
            }

            override fun onResponseSuccess(response: Response<*>?, p1: Int, result: GSHttpResponse<ChallengeTheBean>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                if(showResponseErrorMessage(result) == 1) {
                    val eventBean = EventBean()
                    eventBean.what = EventType.AI_SCENE_SUCCESS
                    EventBus.getDefault().post(eventBean)
                    result.body?: return
                    mChallengeTheBean = result.body
                    if(mIsPlayEndSuccess) {
                        if(mChallengeTheBean?.status == 4) {
                            challengeSuccess()
                        }else if(mChallengeTheBean?.status == 5) {
                            challengeFail()
                        }
                    }
                }else {
                    finish()
                }
            }
        })
    }

    /**
     * 播放狐狸的发音
     * 首先需要判断是否加载成功
     * 其次判断当前是否在播放中
     * 播放状态下进行停止操作，并且狐狸保护待机状态
     * 非播放状态下首先关闭其它播放语音，开始执行狐狸播放
     */
    private fun play() {
        if(isPreparedSuccess) {
            if(mPlayer.isPlaying()) {
                mPlayer.stop()
                this.reset(STANDBY)
                val biJsonObject = org.json.JSONObject()
                biJsonObject?.put("scenesId" , mSceneId)
                collectClickEvent("as602_clk_fox_stop" , biJsonObject.toString())
            }else {
                //首先停止当前的强提示播放
                if(vpStrongPrompt.visibility == View.VISIBLE) {
                    if(mStrongFragmentList.size > 0) {
                        (mStrongFragmentList[this.mStrongFragmentPosition] as AiSceneStrongPromptFragment).stopPlay()
                    }
                }
                this.say()
                mPlayer.play()
                this.reset(SAYINT)
                val biJsonObject = org.json.JSONObject()
                biJsonObject?.put("scenesId" , mSceneId)
                collectClickEvent("as602_clk_fox_play" , biJsonObject.toString())
            }
        }
    }

    /**
     * 显示引导图
     */
    private fun showGuide() {
        if (AiSceneCoreService.instance.getRecordStatus() == AiScene.RECORD_STATUS.RECORDING
                || AiSceneCoreService.instance.getRecordStatus() == AiScene.RECORD_STATUS.TESTING) {
            return
        }
        val options1 = HighlightOptions.Builder()
                .setRelativeGuide(object : RelativeGuide(R.layout.ui_guide_fox, Gravity.TOP, 36) {
                    override fun offsetMargin(marginInfo: MarginInfo?, viewGroup: ViewGroup?, view: View?) {
                        marginInfo?.leftMargin = 80
                        marginInfo?.rightMargin = 100
                    }
                })
                .setOnClickListener {
                    mController?.showPage(1)
                }.build()
        val page1 = GuidePage.newInstance().addHighLightWithOptions(vFox, HighLight.Shape.CIRCLE, options1)

        val options2 = HighlightOptions.Builder()
                .setRelativeGuide(object : RelativeGuide(R.layout.ui_guide_fanyi, Gravity.TOP, 36) {
                    override fun offsetMargin(marginInfo: MarginInfo?, viewGroup: ViewGroup?, view: View?) {
                        marginInfo?.leftMargin = 80
                        marginInfo?.rightMargin = 100
                    }
                })
                .setOnClickListener {
                    mController?.showPage(2)
                }.build()
        val page2 = GuidePage.newInstance().addHighLightWithOptions(ivFanyi, HighLight.Shape.CIRCLE, options2)

        val options3 = HighlightOptions.Builder()
                .setRelativeGuide(object : RelativeGuide(R.layout.ui_guide_recorder, Gravity.TOP, 20) {
                    override fun offsetMargin(marginInfo: MarginInfo?, viewGroup: ViewGroup?, view: View?) {
                        marginInfo?.leftMargin = 70
                        marginInfo?.rightMargin = 100
                    }
                })
                .setOnClickListener {
                    mController?.showPage(3)
                }.build()
        val page3 = GuidePage.newInstance().addHighLightWithOptions(ivRecorder, HighLight.Shape.CIRCLE, options3)

        val options4 = HighlightOptions.Builder()
                .setRelativeGuide(object : RelativeGuide(R.layout.ui_guide_tips, Gravity.TOP, 20) {
                    override fun offsetMargin(marginInfo: MarginInfo?, viewGroup: ViewGroup?, view: View?) {
                        marginInfo?.leftMargin = 100
                        marginInfo?.rightMargin = 80
                    }
                })
                .setOnClickListener {
                    mController?.remove()
                }.build()
        val page4 = GuidePage.newInstance().addHighLightWithOptions(ivTips, HighLight.Shape.CIRCLE, options4)
        NewbieGuide.with(this)
                .setLabel("scene_guide")
                .alwaysShow(true)
                .addGuidePage(page1)
                .addGuidePage(page2)
                .addGuidePage(page3)
                .addGuidePage(page4)
                .setOnGuideChangedListener(object : OnGuideChangedListener {
                    override fun onShowed(controller: Controller?) {
                        mController = controller
                    }
                    override fun onRemoved(controller: Controller?) {
                        mController = null
                        if(!mIsShowed) {
                            show()
                        }
                    }
                })
                .show()
    }

    /**
     * 改变btn显示状态
     */
    private fun changeRecordBtnStatus() {
        when (AiSceneCoreService.instance.getRecordStatus()) {
            AiScene.RECORD_STATUS.NONE -> {
                ivRecorder.setImageResource(R.drawable.icon_ai_scene_unrecord)
                avvVoiceSize.visibility = View.INVISIBLE
                ivFanyi.visibility = View.VISIBLE
                ivTips.visibility = View.VISIBLE
                tvRecorderTips.visibility = View.INVISIBLE
                ivProgress.visibility = View.INVISIBLE
                aspVoice.visibility = View.INVISIBLE
            }//不可操作
            AiScene.RECORD_STATUS.RECORDING -> {
                ivRecorder.setImageResource(R.drawable.icon_ai_scene_recording)
                avvVoiceSize.visibility = View.VISIBLE
                tvRecorderTips.visibility = View.VISIBLE
                ivFanyi.visibility = View.INVISIBLE
                ivTips.visibility = View.INVISIBLE
                tvRecorderTips.visibility = View.VISIBLE
                ivProgress.visibility = View.INVISIBLE
                aspVoice.visibility = View.VISIBLE
                tvRecorderTips.text = "点击结束"
                aspVoice.setOnVoiceProgressListener(object : OnVoiceProgressListener {
                    override fun onProgress(progress: Float) {
                        LogUtil.d("progress: $progress")
                    }
                    override fun onStop() {
                        AiSceneCoreService.instance.recordStop()
                    }
                })
                aspVoice.start()
            }//录音中
            AiScene.RECORD_STATUS.TESTING -> {
                ivRecorder.setImageResource(R.drawable.icon_ai_scene_recording)
                avvVoiceSize.visibility = View.INVISIBLE
                tvRecorderTips.visibility = View.VISIBLE
                ivFanyi.visibility = View.INVISIBLE
                ivTips.visibility = View.INVISIBLE
                tvRecorderTips.visibility = View.VISIBLE
                ivProgress.visibility = View.VISIBLE
                aspVoice.visibility = View.INVISIBLE
                tvRecorderTips.text = "点击录音"
                mTestingAnima?.start()
            }//评测中
            AiScene.RECORD_STATUS.STOPED -> {
                ivRecorder.setImageResource(R.drawable.icon_ai_scene_record)
                avvVoiceSize.visibility = View.INVISIBLE
                tvRecorderTips.visibility = View.VISIBLE
                ivFanyi.visibility = View.VISIBLE
                ivTips.visibility = View.VISIBLE
                tvRecorderTips.visibility = View.VISIBLE
                ivProgress.visibility = View.INVISIBLE
                aspVoice.visibility = View.INVISIBLE
                tvRecorderTips.text = "点击录音"
            }//已经停止录音
        }
    }

    /**
     * 更新按钮状态
     */
    private fun updateBtnStatus() {
        when(this.mCurrOptStatus) {
            STANDBY -> {
                ivFanyi.setImageResource(R.drawable.icon_ai_scene_fanyi_clickable)
                ivFanyi.isClickable = true
                ivTips.setImageResource(R.drawable.icon_ai_scene_answer_clickable)
                ivTips.isClickable = true
            }
            SAY_HELLO -> {
                ivFanyi.setImageResource(R.drawable.icon_ai_scene_fanyi_not_clickable)
                ivFanyi.isClickable = true
                ivTips.setImageResource(R.drawable.icon_ai_scene_answer_not_clickable)
                ivTips.isClickable = true
            }
            SAYINT -> {
                ivFanyi.setImageResource(R.drawable.icon_ai_scene_fanyi_not_clickable)
                ivFanyi.isClickable = false
                ivTips.setImageResource(R.drawable.icon_ai_scene_answer_not_clickable)
                ivTips.isClickable = false
            }
        }
    }

    /**
     * 创建小狐狸3s说话弹窗提醒
     */
    private fun createNotSayRunnable(): Runnable {
        return Runnable {
            showThreeNotSay()
        }
    }

    /**
     * 更新对话进度
     */
    private fun updateDialogueProgress(end : Boolean) {
        this.mDialogueIndex ++
        mDialogues?.let {
            val layoutParams = flProgress.layoutParams
            var maxHeight = 0
            if(end) {
                tvProgress.text = "100%"
                maxHeight = TypeValue.dp2px(159f)
                ivAiSceneProgress.setImageResource(R.drawable.icon_ai_scene_progress_success)
            }else {
                val dialogue = it[mCurrDialoguePosition]
                val maxDepth = dialogue.maxDepth?:0
                val progress = mDialogueIndex - 1
                val progressRat = progress.toFloat() / (mDialogueIndex + maxDepth)
                val progressInt = (progressRat * 100).toInt()
                tvProgress.text = "$progressInt%"
                maxHeight = TypeValue.dp2px(progressRat * 159f)
                ivAiSceneProgress.setImageResource(R.drawable.icon_ai_scene_progress_loading)
            }
            mValueAnimator?.setIntValues(layoutParams.height , maxHeight)
            mValueAnimator?.duration = 300
            if(mValueAnimator?.isRunning == true) {
                mValueAnimator?.cancel()
            }
            mValueAnimator?.start()
        }
    }

    /**-----------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(p0: Int) {
        if(mStrongFragmentList?.size > 0) {
            (mStrongFragmentList[this.mStrongFragmentPosition] as AiSceneStrongPromptFragment).stopPlay()
        }
        this.mStrongFragmentPosition = p0
        vpStrongPrompt.resetHeight(this.mStrongFragmentPosition)
    }

}