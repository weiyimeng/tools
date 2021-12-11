package gaosi.com.learn.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.haibin.calendarview.WeekBar
import gaosi.com.learn.R

/**
 * description:
 * created by huangshan on 2020/6/2 下午1:09
 */
class AxxWeekBar(context: Context) : WeekBar(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.ui_week_bar, this, true)
        setBackgroundResource(R.drawable.bg_week_bar_10_shape)
    }

    override fun onWeekStartChange(weekStart: Int) {
        for (i in 0 until childCount) {
            (getChildAt(i) as TextView).text = getWeekString(i, weekStart)
        }
    }

    /**
     * 周文本
     * @param index index
     * @param weekStart weekStart
     * @return 周文本
     */
    private fun getWeekString(index: Int, weekStart: Int): String? {
        val weeks = context.resources.getStringArray(R.array.axx_week_string_array)
        if (weekStart == 1) {
            return weeks[index]
        }
        return if (weekStart == 2) {
            weeks[if (index == 6) 0 else index + 1]
        } else weeks[if (index == 0) 6 else index - 1]
    }
}