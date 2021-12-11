package gaosi.com.learn.studentapp.aiscene.base

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import com.gsbaselib.InitBaseLib
import com.gsbaselib.base.GSBaseFragment
import com.gsbaselib.base.adapter.CommentVpAdapter
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.utils.ActivityCollector
import com.gsbaselib.utils.StatusBarUtil
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.player.IPlayerView
import com.gstudentlib.player.NavPlayer
import com.gstudentlib.player.OnPlayerStatusListener
import com.gstudentlib.player.Player
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.SystemUtil
import gaosi.com.learn.R
import gaosi.com.learn.bean.aiscene.ChallengeTheBean
import gaosi.com.learn.bean.aiscene.SceneBean
import gaosi.com.learn.bean.aiscene.SceneDialogues
import gaosi.com.learn.bean.aiscene.SceneStrongPrompt
import gaosi.com.learn.studentapp.aiscene.AiScene
import gaosi.com.learn.studentapp.aiscene.SceneEntranceActivity
import gaosi.com.learn.studentapp.aiscene.core.AiSceneCoreService
import kotlinx.android.synthetic.main.activity_ai_scene.*
import org.json.JSONObject
import java.util.*

/**
 * 作者：created by 逢二进一 on 2019/11/14 13:56
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
open class BaseAiSceneActivity : BaseActivity(), OnPlayerStatusListener {
    
    protected val wellDone = "scene_welldone"
    protected val excellent = "scene_excellent"
    protected val success = "scene_success"
    protected val fail = "scene_fail"
    protected var mSceneId: String? = ""
    protected var mVersion: String? = ""
    protected var mSource: String? = ""
    protected val mPlayer: IPlayerView by lazy { Player(1) }
    protected val mNavPlayer: IPlayerView by lazy { NavPlayer(2) }
    protected var mIntoTime: Long = 0
    protected var mTestingAnima: ObjectAnimator? = null
    protected var mValueAnimator: ValueAnimator? = null
    protected var gestureDetector: GestureDetector? = null
    protected val MIN_CLICK_DELAY_TIME = 1000 //用户两次点击时间间隔
    protected var lastClickTime: Long = 0 //上次点击时间
    protected var mCurrOptStatus = NONE //当前用户的操作状态
    protected var isSayHelloSuccess = false //打招呼是否完成
    protected var isPreparedSuccess = false //语音资源是否准备完成
    protected var mCommentVpAdapter: CommentVpAdapter? = null
    protected var mStrongFragmentList = ArrayList<GSBaseFragment>()
    protected var mSceneBean: SceneBean? = null //情景内容
    protected var mDialogues: ArrayList<SceneDialogues>? = null //情景列表
    protected var mChallengeTheBean: ChallengeTheBean? = null //挑战状态
    protected var mCurrDialoguePosition: Int = 0 //当前对话组
    protected var mCurrDialogueNum: Int = 0 //当前对话组对应的num
    protected var mDialogueIndex: Int = 0 //当前对话次数
    protected var mIsTestEnd = false // 是否评测完成
    protected var mIsPlayEndSuccess = false //结束语是否播放完成
    protected var mTestCount = 0 //用户评测次数，一次未通过出现弱提示，二次未通过强提示，三次未通过直接跳转下一组
    protected var mIsShowThreeTips = false //是否显示过3s未说话提醒
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_scene)
        this.adjustStatusBarMargin(vStatusBar2)
        this.mIntoTime = System.currentTimeMillis()
        this.getIntentData()
        AiSceneCoreService.instance.init(baseContext)
        if (mSource == "list") {
            ActivityCollector.getInstance().finishActivity(SceneEntranceActivity::class.java)
        } else if (mSource == "report") {
            ActivityCollector.getInstance().getLastPageByNum(2)?.finish()
            ActivityCollector.getInstance().finishActivity(SceneEntranceActivity::class.java)
        }
    }
    
    private fun getIntentData() {
        intent?.apply {
            mSceneId = getStringExtra("sceneId")
            mVersion = getStringExtra("version")
            mSource = getStringExtra("source") ?: ""
        }
    }
    
    override fun initView() {
        super.initView()
        ivRecorder.setOnClickListener(this)
        ivFanyi.setOnClickListener(this)
        ivTips.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        //vFox.setOnClickListener(this)
        vFoxClick.setOnClickListener(this)
        ivTitleRight.setOnClickListener(this)
        this.initGestureDetector()
        this.initAnimation()
    }
    
    /**
     * 初始化json动画
     */
    private fun initAnimation() {
        lavFox.imageAssetsFolder = "images"
        lavFox.setAnimation("scene_fox.json")
        //评审动画
        this.mTestingAnima = ObjectAnimator.ofFloat(ivProgress, "rotation", 0f, 360f)
        this.mTestingAnima?.duration = 2000
        this.mTestingAnima?.interpolator = LinearInterpolator()
        this.mTestingAnima?.repeatCount = -1
        //3s提示动画
        lavThreeNotSay.setAnimation("scene_hand_xie.json")
        lavThreeNotSay.loop(true)
        lavThreeNotSay.progress = 1.0f
        //第一次出错动画
        lavWeakPromptGuide.setAnimation("scene_hand_chui.json")
        lavWeakPromptGuide.loop(true)
        lavWeakPromptGuide.progress = 1.0f
        //初始化valueAnaim
        mValueAnimator = ValueAnimator.ofInt(0)
        mValueAnimator?.interpolator = DecelerateInterpolator()
        mValueAnimator?.addUpdateListener {
            val height = Integer.parseInt(it.animatedValue.toString())
            val layoutParams = flProgress.layoutParams
            layoutParams.height = height
            flProgress.requestLayout()
        }
    }
    
    /**
     * 初始化背景监听事件
     */
    private fun initGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                mivSceneBg.updatePosition(distanceX.toInt())
                return super.onScroll(e1, e2, distanceX, distanceY)
            }
            
            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                //                mivSceneBg.updatePosition(velocityX.toInt())
                //                if (mVelocityTracker == null) {
                //                    mVelocityTracker = VelocityTracker.obtain()
                //                }
                //                mVelocityTracker?.computeCurrentVelocity(1000 , velocityX)
                LogUtil.d("velocityX: " + velocityX + "   e1: " + e1?.rawX + "    e2: " + e2?.rawX)
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })
    }
    
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return if (vpStrongPrompt.visibility == View.INVISIBLE
                && llQuestion.visibility == View.INVISIBLE) {
            gestureDetector?.onTouchEvent(ev) // 让GestureDetector响应触碰事件
            super.dispatchTouchEvent(ev)
            false
        } else {
            super.dispatchTouchEvent(ev) // 让Activity响应触碰事件
        }
    }
    
    open fun reset(status: Int) {
        this.lastClickTime = Calendar.getInstance().timeInMillis - 500
        this.mCurrOptStatus = status
        this.resetAnima()
    }
    
    /**
     * 关闭评审动画
     */
    protected fun resetAnima() {
        if (this.mTestingAnima != null && this.mTestingAnima?.isRunning == true) { //评测动画初始化
            this.mTestingAnima?.cancel()
        }
    }
    
    /**
     * 打招呼
     * 只有首次进入场景时会进行sayHello
     * 在sayHello完成后才会进行狐狸的说话
     */
    protected fun sayHello() {
        isSayHelloSuccess = false
        lavFox.pauseAnimation()
        lavFox.loop(false)
        lavFox.setMinAndMaxProgress(0f, 4 / 23f)
        lavFox.playAnimation()
        lavFox.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                isSayHelloSuccess = true
                if (isPreparedSuccess && !mPlayer.isPlaying()) {
                    reset(TO_SAY)
                }
                lavFox.removeAllAnimatorListeners()
            }
            
            override fun onAnimationCancel(animation: Animator?) {
            }
            
            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }
    
    /**
     * 说话
     */
    protected fun say() {
        lavFox.pauseAnimation()
        lavFox.loop(true)
        lavFox.setMinAndMaxProgress(4 / 23f, 14 / 23f)
        lavFox.playAnimation()
    }
    
    /**
     * 待机
     */
    protected fun standby() {
        lavFox.pauseAnimation()
        lavFox.loop(true)
        lavFox.setMinAndMaxProgress(15 / 23f, 19 / 23f)
        lavFox.playAnimation()
    }
    
    /**
     * 开心
     */
    protected fun happy() {
        lavFox.pauseAnimation()
        lavFox.loop(true)
        lavFox.setMinAndMaxProgress(19 / 23f, 21 / 23f)
        lavFox.playAnimation()
    }
    
    /**
     * 失落
     */
    protected fun sad() {
        lavFox.pauseAnimation()
        lavFox.loop(true)
        lavFox.setMinAndMaxProgress(21 / 23f, 23 / 23f)
        lavFox.playAnimation()
    }
    
    /**
     * 良好
     */
    protected fun wellDone() {
        this.play(wellDone)
        lavTestStatus.visibility = View.VISIBLE
        lavTestStatus.setAnimation("scene_welldone.json")
        lavTestStatus.loop(false)
        lavTestStatus.playAnimation()
        lavTestStatus.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                lavTestStatus.visibility = View.INVISIBLE
                if (mIsTestEnd) {
                    reset(PLAY_END_SPEECH)
                } else {
                    showNextDialogue()
                }
                lavTestStatus.removeAllAnimatorListeners()
            }
            
            override fun onAnimationCancel(animation: Animator?) {
            }
            
            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }
    
    /**
     * 优秀
     */
    protected fun excellent() {
        this.play(excellent)
        lavTestStatus.visibility = View.VISIBLE
        lavTestStatus.setAnimation("scene_excellent.json")
        lavTestStatus.loop(false)
        lavTestStatus.playAnimation()
        lavTestStatus.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                lavTestStatus.visibility = View.INVISIBLE
                if (mIsTestEnd) {
                    reset(PLAY_END_SPEECH)
                } else {
                    showNextDialogue()
                }
                lavTestStatus.removeAllAnimatorListeners()
            }
            
            override fun onAnimationCancel(animation: Animator?) {
            }
            
            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }
    
    /**
     * 挑战成功
     */
    protected fun challengeSuccess() {
        this.play(success)
        this.happy()
        this.reset(END)
        lavSceneSuccess.visibility = View.VISIBLE
        lavSceneSuccess.setAnimation("scene_success.json")
        lavSceneSuccess.loop(false)
        lavSceneSuccess.playAnimation()
        lavSceneSuccess.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                toStudyReport()
                lavSceneSuccess.removeAllAnimatorListeners()
            }
            
            override fun onAnimationCancel(animation: Animator?) {
            }
            
            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }
    
    /**
     * 挑战失败
     */
    protected fun challengeFail() {
        this.play(fail)
        this.sad()
        this.reset(END)
        lavSceneSuccess.visibility = View.VISIBLE
        lavSceneSuccess.setAnimation("scene_fail.json")
        lavSceneSuccess.loop(false)
        lavSceneSuccess.playAnimation()
        lavSceneSuccess.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                toStudyReport()
                lavSceneSuccess.removeAllAnimatorListeners()
            }
            
            override fun onAnimationCancel(animation: Animator?) {
            }
            
            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }
    
    /**
     * 展示图片显示动画
     */
    protected fun showPromptImg() {
        llPromptImg.visibility = View.VISIBLE
        rlPromptImageAndText.visibility = View.VISIBLE
        tvPromptText.visibility = View.VISIBLE
        val animator = ObjectAnimator.ofFloat(llPromptImg, "alpha", 0f, 1.0f)
        animator.duration = 300
        animator.start()
    }
    
    /**
     * 展示图片隐藏动画
     */
    protected fun hidePromptImg() {
        val animator = ObjectAnimator.ofFloat(llPromptImg, "alpha", 1.0f, 0f)
        animator.duration = 300
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                llPromptImg.visibility = View.INVISIBLE
            }
            
            override fun onAnimationCancel(animation: Animator?) {
            }
            
            override fun onAnimationStart(animation: Animator?) {
            }
        })
        animator.start()
    }
    
    /**
     * 展示文字显示动画
     */
    protected fun showPromptText() {
        rlPromptText.visibility = View.VISIBLE
        val animator = ObjectAnimator.ofFloat(rlPromptText, "alpha", 0f, 1.0f)
        animator.duration = 300
        animator.start()
    }
    
    /**
     * 隐藏文字显示动画
     */
    protected fun hidePromptText() {
        val animator = ObjectAnimator.ofFloat(rlPromptText, "alpha", 1.0f, 0f)
        animator.duration = 300
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                rlPromptText.visibility = View.INVISIBLE
            }
            
            override fun onAnimationCancel(animation: Animator?) {
            }
            
            override fun onAnimationStart(animation: Animator?) {
            }
        })
        animator.start()
    }
    
    /**
     * 显示狐狸说话
     */
    protected fun showQuestion() {
        llQuestion.visibility = View.VISIBLE
        val scaleX = ObjectAnimator.ofFloat(llQuestion, "scaleX", 1.0f, 1.1f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(llQuestion, "scaleY", 1.0f, 1.1f, 1.0f)
        val animator = AnimatorSet()
        animator.duration = 300
        animator.playTogether(scaleX, scaleY)
        animator.start()
    }
    
    /**
     * 隐藏狐狸说话
     */
    protected fun hideQuestion() {
        val scaleX = ObjectAnimator.ofFloat(llQuestion, "scaleX", 1.0f, 1.1f, 0f)
        val scaleY = ObjectAnimator.ofFloat(llQuestion, "scaleY", 1.0f, 1.1f, 0f)
        val animator = AnimatorSet()
        animator.duration = 300
        animator.playTogether(scaleX, scaleY)
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                llQuestion.visibility = View.INVISIBLE
            }
            
            override fun onAnimationCancel(animation: Animator?) {
            }
            
            override fun onAnimationStart(animation: Animator?) {
            }
        })
        animator.start()
    }
    
    /**
     * 显示弱提示
     */
    protected fun showWeakPrompt() {
        llWeakPrompt.visibility = View.VISIBLE
        val animator = ObjectAnimator.ofFloat(llWeakPrompt, "alpha", 0f, 1.0f)
        animator.duration = 300
        animator.start()
    }
    
    /**
     * 隐藏弱提示
     */
    protected fun hideWeakPrompt() {
        val animator = ObjectAnimator.ofFloat(llWeakPrompt, "alpha", 1.0f, 0f)
        animator.duration = 300
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                llWeakPrompt.visibility = View.INVISIBLE
            }
            
            override fun onAnimationCancel(animation: Animator?) {
            }
            
            override fun onAnimationStart(animation: Animator?) {
            }
        })
        animator.start()
    }
    
    /**
     * 显示强提示
     */
    protected fun showStrongPrompt() {
        vpStrongPrompt.visibility = View.VISIBLE
        val animator = ObjectAnimator.ofFloat(vpStrongPrompt, "alpha", 0f, 1.0f)
        animator.duration = 300
        animator.start()
    }
    
    /**
     * 隐藏强提示
     */
    protected fun hideStrongPrompt() {
        val animator = ObjectAnimator.ofFloat(vpStrongPrompt, "alpha", 1.0f, 0f)
        animator.duration = 300
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                vpStrongPrompt.visibility = View.INVISIBLE
            }
            
            override fun onAnimationCancel(animation: Animator?) {
            }
            
            override fun onAnimationStart(animation: Animator?) {
            }
        })
        animator.start()
    }
    
    /**
     * 显示3s未说话提示
     */
    protected fun showThreeNotSay() {
        val biJsonObject = JSONObject()
        biJsonObject.put("scenesId", mSceneId)
        collectClickEvent("XSD_243", biJsonObject.toString())
        lavThreeNotSay.progress = 1.0f
        lavThreeNotSay.playAnimation()
        llThreeNotSay.visibility = View.VISIBLE
        val animator = ObjectAnimator.ofFloat(llThreeNotSay, "alpha", 0f, 1.0f)
        animator.duration = 300
        animator.start()
    }
    
    /**
     * 隐藏3s未说话提示
     */
    protected fun hideThreeNotSay() {
        lavThreeNotSay.pauseAnimation()
        val animator = ObjectAnimator.ofFloat(llThreeNotSay, "alpha", 1.0f, 0f)
        animator.duration = 300
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                llThreeNotSay.visibility = View.INVISIBLE
            }
            
            override fun onAnimationCancel(animation: Animator?) {
            }
            
            override fun onAnimationStart(animation: Animator?) {
            }
        })
        animator.start()
    }
    
    /**
     * 显示第一次不通过提醒
     */
    protected fun showWeakPromptGuide() {
        val biJsonObject = JSONObject()
        biJsonObject.put("scenesId", mSceneId)
        collectClickEvent("XSD_244", biJsonObject.toString())
        lavWeakPromptGuide.progress = 1.0f
        lavWeakPromptGuide.playAnimation()
        llWeakPromptGuide.visibility = View.VISIBLE
        val animator = ObjectAnimator.ofFloat(llWeakPromptGuide, "alpha", 0f, 1.0f)
        animator.duration = 300
        animator.start()
    }
    
    /**
     * 隐藏第一次不通过提醒
     */
    protected fun hideWeakPromptGuide() {
        lavWeakPromptGuide.pauseAnimation()
        val animator = ObjectAnimator.ofFloat(llWeakPromptGuide, "alpha", 1.0f, 0f)
        animator.duration = 300
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                llWeakPromptGuide.visibility = View.INVISIBLE
            }
            
            override fun onAnimationCancel(animation: Animator?) {
            }
            
            override fun onAnimationStart(animation: Animator?) {
            }
        })
        animator.start()
    }
    
    /**
     * 隐藏进度条
     */
    protected fun hideProgress() {
        mHandler.postDelayed({
            val animator = ObjectAnimator.ofFloat(llProgress, "alpha", 1.0f, 0f)
            animator.duration = 300
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }
                
                override fun onAnimationEnd(animation: Animator?) {
                    llProgress.visibility = View.INVISIBLE
                }
                
                override fun onAnimationCancel(animation: Animator?) {
                }
                
                override fun onAnimationStart(animation: Animator?) {
                }
            })
            animator.start()
        }, 1000)
    }
    
    /**
     * 跳转报告页面
     * 延迟500毫秒，为了可以更好的查看这个页面的状态
     */
    protected fun toStudyReport() {
        mHandler.postDelayed({
            val suffix = "AiStudyReport.web.js"
            val params = JSONObject()
            params.put("studentId", STBaseConstants.userId)
            params.put("scenesId", mSceneId)
            params.put("scenesStatus", "1")
            params.put("source", "dialog")
            SchemeDispatcher.gotoWebPage(ActivityCollector.getInstance().currentActivity,
                    InitBaseLib.getInstance().configManager.h5ServerUrl + suffix,
                    SystemUtil.generateDefautJsonStr(params, suffix))
            finish()
        }, 500)
    }
    
    /**
     * 展示下一组对话
     */
    protected open fun showNextDialogue() {
        this.mTestCount = 0
        this.mIsShowThreeTips = false
        this.reset(NONE)
    }
    
    /**
     * 通过position获取对话组数据
     */
    fun getStrongPromptByPosition(position: Int): SceneStrongPrompt? {
        mDialogues?.let {
            val dialogue = it[mCurrDialoguePosition]
            return dialogue?.strongPrompts?.get(position)
        }
        return null
    }
    
    /**
     * 播放音效
     */
    protected fun play(name: String) {
        mNavPlayer.setOnPlayerStatusListener(this)
        mNavPlayer.reCreate(name)
    }
    
    /**
     * 当语音准备完成时需要判断sayHello是否完成
     * 若sayHello完成则进行语音播放
     * 若sayHello未完成则等待sayHello完成后进行播放，用于首次进入场景
     */
    override fun onPrepared(tag: Int) {
        if (isFinishing || isDestroyed) {
            return
        }
        if (tag == 2) { //本地播放
            if (!mNavPlayer.isPlaying()) {
                mNavPlayer.play()
            }
        } else if (tag == 1) { //在线播放
            isPreparedSuccess = true
            if (mIsTestEnd) {
                this.say()
                if (!mPlayer.isPlaying()) {
                    mPlayer.play()
                }
            } else {
                if (!mPlayer.isPlaying() && isSayHelloSuccess) {
                    this.reset(TO_SAY)
                }
            }
        }
    }
    
    /**
     * 当狐狸说完时此时进入待机状态
     * 最后一次评测结束时要播放结束语
     * 同时需要请求此情景的跳转状态
     * 在播放完成以及请求跳转状态同时完成的情况下播放挑战成功与失败的动画
     */
    override fun onCompletion(tag: Int) {
        if (isFinishing || isDestroyed) {
            return
        }
        if (tag == 1) { //在线播放
            //最后一次评测结束
            if (mIsTestEnd) {
                mIsPlayEndSuccess = true
                this.standby()
                if (mIsPlayEndSuccess && mChallengeTheBean != null) {
                    if (mChallengeTheBean?.status == 4) {
                        challengeSuccess()
                    } else if (mChallengeTheBean?.status == 5) {
                        challengeFail()
                    }
                }
            } else {
                this.reset(STANDBY)
            }
        }
    }
    
    /**
     * 退出情景
     */
    protected fun exitScene() {
        if (AiSceneCoreService.instance.getRecordStatus() == AiScene.RECORD_STATUS.RECORDING
                || AiSceneCoreService.instance.getRecordStatus() == AiScene.RECORD_STATUS.TESTING) {
            return
        }
        DialogUtil.getInstance().create(this, R.layout.back_dialog)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener) {
                        this.bindListener(onClickListener, R.id.tvCancel)
                        this.bindListener(onClickListener, R.id.tvConfirm)
                        this.findViewById<TextView>(R.id.tvSubTips).visibility = View.GONE
                        this.bindText(R.id.tvTips, "还没有完成，真的要离开吗?")
                        this.bindText(R.id.tvCancel, "坚持退出")
                        this.bindText(R.id.tvConfirm, "继续对话")
                    }
                    
                    override fun onClick(v: View, dialog: DialogUtil) {
                        super.onClick(v, dialog)
                        when (v.id) {
                            R.id.tvCancel -> {
                                dialog.dismiss()
                                finish()
                            }
                            R.id.tvConfirm -> {
                                dialog.dismiss()
                            }
                        }
                    }
                })
    }
    
    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageViewInFragment(this, null)
    }
    
    override fun onBackPressed() {
        this.exitScene()
    }
    
    override fun onResume() {
        super.onResume()
        StatusBarUtil.setLightMode(this)
    }
    
    override fun onPause() {
        super.onPause()
        try {
            if (mPlayer.isPlaying()) {
                mPlayer.stop()
                this.reset(STANDBY)
            }
        } catch (e: Exception) {
        }
    }
    
    override fun onDestroy() {
        val extParam = HashMap<String, Any>()
        extParam["scenesId"] = mSceneId ?: ""
        GSLogUtil.collectPageRemainTime(pageId, System.currentTimeMillis() - mIntoTime, extParam)
        super.onDestroy()
        try {
            mPlayer.destroy()
            mNavPlayer.destroy()
            AiSceneCoreService.instance.release()
            this.mStrongFragmentList.clear()
        } catch (e: Exception) {
        }
    }
    
    /**
     * 用户操作状态码
     */
    companion object {
        const val NONE = -1 //未进行任何操作
        const val SAY_HELLO = 0 //正在打招呼
        const val VOICE_PREPARE = 1 //语音资源加载中
        const val SAYINT = 2 //说话中
        const val STANDBY = 3 //狐狸已经结束说话，并且此时处于待机状态
        const val RECORDING = 4 //录音中
        const val TESTING = 5 //评测中
        const val FANYI = 6 //翻译
        const val STRONG_PROMPTS = 7 //提示
        const val PLAY_STRONGPROMPTS = 8 //播放强提示
        const val SHOW_WEAK_PROMPT = 9 //展示弱提示
        const val PLAY_END_SPEECH = 10 //播放结束语
        const val END = 11 //结束
        const val REQUEST_READ_PHONE = 12
        const val TO_SAY = 13 //去说话，正在说话中停止说话，没有说话就去说话
    }
    
}
