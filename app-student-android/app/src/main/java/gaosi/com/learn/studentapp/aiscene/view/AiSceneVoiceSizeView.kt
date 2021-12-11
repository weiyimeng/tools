package gaosi.com.learn.studentapp.aiscene.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.base.view.BaseDrawView
import com.gsbaselib.utils.TypeValue
import kotlin.math.sin

/**
 * 作者：created by 逢二进一 on 2019/11/14 16:55
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class AiSceneVoiceSizeView: BaseDrawView {

    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var sigleWidth: Float = 0f
    private val num: Int = 11
    private var mLastRefreshTime: Long = 0
    private val maxRefreshTime: Long = 80
    private var mVoicesRatios: ArrayList<Double>? = null
    private var mAverageHeight  = 1f
    private var mPaint: Paint? = null
    private var mOffset: Float = 0f

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    override fun init(p0: Context?) {
        mPaint = Paint()
        mPaint?.strokeCap = Paint.Cap.ROUND
        mPaint?.color = Color.WHITE
        mPaint?.strokeWidth = sigleWidth
        mVoicesRatios = ArrayList()
        this.mVoicesRatios?.clear()
        this.mVoicesRatios?.add(0.1f * Math.PI * 0.5f)
        this.mVoicesRatios?.add(0.3f * Math.PI * 0.5f)
        this.mVoicesRatios?.add(0.7f * Math.PI * 0.5f)
        this.mVoicesRatios?.add(0.8f * Math.PI * 0.5f)
        this.mVoicesRatios?.add(0.9f * Math.PI * 0.5f)

        this.mVoicesRatios?.add(1.0f * Math.PI * 0.5f)

        this.mVoicesRatios?.add(0.9f * Math.PI * 0.5f)
        this.mVoicesRatios?.add(0.8f * Math.PI * 0.5f)
        this.mVoicesRatios?.add(0.7f * Math.PI * 0.5f)
        this.mVoicesRatios?.add(0.3f * Math.PI * 0.5f)
        this.mVoicesRatios?.add(0.1f * Math.PI * 0.5f)
        this.updateVoiceSize(20)
    }

    fun updateVoiceSize(voiceSize: Int) {
        LogUtil.d("updateVoiceSize $voiceSize")
        if(System.currentTimeMillis() - mLastRefreshTime >= maxRefreshTime) {
            mAverageHeight = if(voiceSize < 35) {
                voiceSize.toFloat() / 33 //计算平均音量
            }else {
                voiceSize.toFloat() / 11 //计算平均音量
            }
            postInvalidate()
            mLastRefreshTime = System.currentTimeMillis()
        }
    }

    override fun initSize(p0: Int, p1: Int, p2: Int, p3: Int) {
        screenWidth = p0
        screenHeight = p1
        sigleWidth = p0.toFloat() / (num * 2 - 1) //计算单线的宽度
        mOffset = sigleWidth / 2 //向右偏移量
        mPaint?.strokeWidth = sigleWidth
    }

    override fun drawView(canvas: Canvas?) {
        for(i in this.mVoicesRatios?.indices!!) {
            var height = (mAverageHeight * sin(this.mVoicesRatios!![i]) * Math.random() * 8f).toFloat()
            if(height < 1) {
                height = 1f
            }else if(height > 35) {
                height = 35f
            }
            val heightPx =  TypeValue.dp2px(height).toFloat()
            canvas?.drawLine((sigleWidth * i * 2) + mOffset , (screenHeight - heightPx) / 2 , (sigleWidth * i * 2) + mOffset , ((screenHeight - heightPx) / 2) + heightPx, mPaint)
        }
    }
}