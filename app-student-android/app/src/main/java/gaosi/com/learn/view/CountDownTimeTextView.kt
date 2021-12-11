package gaosi.com.learn.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.os.CountDownTimer
import java.util.*

/**
 * 作者：created by 逢二进一 on 2019/10/22 15:45
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class CountDownTimeTextView: AppCompatTextView {

    private var mFrontText: String = ""
    private var mBehindText: String = ""
    private var mTimer: CountDownTimer? = null
    private var mTime: Long = 0

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }

    fun setFrontText(frontText: String) {
        this.mFrontText = frontText
    }

    fun setBehindText(behindText: String) {
        this.mBehindText = behindText
    }

    /**
     * 毫秒时间
     */
    fun start(time: Long , onFinishListener: OnFinishListener) {
        mTimer?.cancel()
        this.mTime = time
        this.text = mFrontText + "" + mBehindText
        mTimer = object: CountDownTimer(this.mTime , 1000){
            override fun onTick(millisUntilFinished: Long) {
                mTime = millisUntilFinished
                text = mFrontText + dateLong2String(mTime)
            }

            override fun onFinish() {
                mTime = 0
                text = mFrontText + dateLong2String(mTime)
                onFinishListener.onFinish()
            }
        }
    }

    fun stop() {
       mTimer?.cancel()
    }

    fun start() {
        mTimer?.start()
    }

    /**
     * long类型时间转剩余日期
     */
    private fun dateLong2String(time: Long): String {
        val elapseSecond = (time / 1000).toInt()
        val hour = elapseSecond / 3600
        val min = elapseSecond % 3600 / 60
        val second = elapseSecond % 60
        val stringTime = StringBuilder()
        if(hour > 0) {
            stringTime.append(String.format(Locale.CHINA , "%02d时" , hour))
        }
        stringTime.append(String.format(Locale.CHINA , "%02d分%02d秒" , min , second))
        return stringTime.toString()
    }

    interface OnFinishListener {
        fun onFinish()
    }

}