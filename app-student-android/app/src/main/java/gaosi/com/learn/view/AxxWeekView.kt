package gaosi.com.learn.view

import android.content.Context
import android.graphics.*
import com.gsbaselib.utils.TypeValue
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.WeekView
import gaosi.com.learn.R
import kotlin.math.min


/**
 * description:
 * created by huangshan on 2020/6/2 下午6:46
 */
class AxxWeekView : WeekView {

    companion object {
        /**
         * 当前选中日期的 月份
         */
        var mSelectMonth = 0
    }

    private var mRadius = 0

    /**
     * 顶部偏移量
     */
    private var mTop = 0

    /**
     * 左侧偏移量
     */
    private var mLeft = 0

    /**
     * 背景圆点
     */
    private val mPointPaint = Paint()

    /**
     * 今天的背景色
     */
    private val mCurrentDayPaint = Paint()

    /**
     * 不是当前月 文字画笔
     */
    private val mNotTheMonthPaint = Paint()

    /**
     * 圆点半径
     */
    private var mPointRadius = 0f

    private var mPadding = 0

    private var mCircleRadius = 0f

    var select: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.app_icon_calendar_select)

    var selectToday: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.app_icon_calendar_select_today)

    val pfd = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    constructor(context: Context): super(context) {
        //针对绘制bitmap添加抗锯齿
        mSelectedPaint.isFilterBitmap = true

        mCurDayTextPaint.isFakeBoldText = false

        mNotTheMonthPaint.isAntiAlias = true
        mNotTheMonthPaint.textAlign = Paint.Align.CENTER
        mNotTheMonthPaint.color = Color.parseColor("#D8DCE2")
        mNotTheMonthPaint.isFakeBoldText = false
        mNotTheMonthPaint.textSize = TypeValue.dp2px(14f).toFloat()

        mPointPaint.isAntiAlias = true
        mPointPaint.style = Paint.Style.FILL
        mPointPaint.textAlign = Paint.Align.CENTER
        mPointPaint.color = Color.parseColor("#52CC52")

        mCurrentDayPaint.isAntiAlias = true
        mCurrentDayPaint.style = Paint.Style.FILL
        mCurrentDayPaint.color = Color.parseColor("#DBF8DB")

        mCircleRadius = TypeValue.dp2px(7f).toFloat()

        mPadding = TypeValue.dp2px(3f)

        mPointRadius = TypeValue.dp2px(3f).toFloat()
    }

    override fun onPreviewHook() {
        mRadius = min(mItemWidth, mItemHeight) / 11 * 5
        mTop = (mItemHeight - (min(mItemWidth, mItemHeight) / 11 * 10)) / 2
        mLeft = (mItemWidth - (min(mItemWidth, mItemHeight) / 11 * 10)) / 2
    }

    override fun onDrawSelected(canvas: Canvas?, calendar: Calendar?, x: Int, hasScheme: Boolean): Boolean {
        if (mSelectMonth == 0) {
            //页面初始化时
            mSelectMonth = calendar?.month ?: 1
        }
        val rect = RectF((x + mLeft).toFloat(), mTop.toFloat(),  (x + mLeft + mRadius * 2).toFloat(), (mTop + mRadius * 2 + 2).toFloat())
        //对canvas设置抗锯齿的滤镜，防止变化canvas引起画质降低
        canvas?.drawFilter = pfd
        canvas?.drawBitmap(select, null, rect, mSelectedPaint)
        return true
    }

    override fun onDrawScheme(canvas: Canvas?, calendar: Calendar?, x: Int) {
        calendar ?: return
        val isSelected = isSelected(calendar)
        if (isSelected) {
            mPointPaint.color = Color.WHITE
        } else {
            mPointPaint.color = calendar.schemeColor
        }
        canvas?.drawCircle(x + mItemWidth / 2.toFloat(), mItemHeight - 5 * mPadding.toFloat(), mPointRadius, mPointPaint)
    }

    override fun onDrawText(canvas: Canvas?, calendar: Calendar?, x: Int, hasScheme: Boolean, isSelected: Boolean) {
        calendar ?: return
        val cx = x + mItemWidth / 2
        val cy = mItemHeight / 2
        val baselineY: Float = mTextBaseLine
        if (calendar.isCurrentDay && !isSelected) {
            val rect = RectF((x + mLeft).toFloat(), mTop.toFloat(),  (x + mLeft + mRadius * 2).toFloat(), (mTop + mRadius * 2 + 2).toFloat())
            canvas?.drawFilter = pfd
            canvas?.drawBitmap(selectToday, null, rect, mSelectedPaint)
            mPointPaint.color = calendar.schemeColor
            canvas?.drawCircle(x + mItemWidth / 2.toFloat(), mItemHeight - 5 * mPadding.toFloat(), mPointRadius, mPointPaint)
        }

        if (isSelected) {
            canvas?.drawText(calendar.day.toString(), cx.toFloat(), baselineY, mSelectTextPaint)
        } else {
            if (calendar.month < mSelectMonth || calendar.month > mSelectMonth) {
                canvas?.drawText(calendar.day.toString(), cx.toFloat(), baselineY, mNotTheMonthPaint)
            } else {
                canvas?.drawText(calendar.day.toString(), cx.toFloat(), baselineY, mCurDayTextPaint)
            }
        }
    }
}