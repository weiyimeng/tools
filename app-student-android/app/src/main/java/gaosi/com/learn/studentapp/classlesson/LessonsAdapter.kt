package gaosi.com.learn.studentapp.classlesson

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gsbaselib.base.event.EventBean
import com.gstudentlib.event.EventType
import gaosi.com.learn.R
import gaosi.com.learn.bean.classlesson.LessonBean
import org.greenrobot.eventbus.EventBus

/**
 * 讲次列表Adapper
 */
class LessonsAdapter(var context: LessonsDialogFragment) : BaseQuickAdapter<LessonBean, BaseViewHolder>(R.layout.item_fragment_lession) {

    init {
        setOnItemClickListener { _, _, i ->
            val event = EventBean(EventType.CLICK_CHANGE_LESSON)
            event.obj = data[i].lessonId
            EventBus.getDefault().post(event)
            context.dismiss()
        }
    }

    override fun convert(helper: BaseViewHolder?, data: LessonBean?) {
        helper?.let {
            it.setText(R.id.tvLessionName , "第 " + data?.num + " 讲  ${data?.lessonName + ""}")
        }
    }

}