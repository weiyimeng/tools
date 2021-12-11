package gaosi.com.learn.studentapp.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import gaosi.com.learn.R
import gaosi.com.learn.bean.main.TodoTaskListBean
import gaosi.com.learn.studentapp.classlesson.status.SubjectId

/**
 * 首页讲次任务
 */
class TodoTaskAdapter : BaseQuickAdapter<TodoTaskListBean, BaseViewHolder>(R.layout.item_todo_task) {

    override fun convert(helper: BaseViewHolder?, data: TodoTaskListBean?) {
        helper?.apply {

            val ivSubject = getView<ImageView>(R.id.ivSubject)
            val tvLessonName = getView<TextView>(R.id.tvLessonName)
            val tvClassName = getView<TextView>(R.id.tvClassName)

            val llContainer = getView<LinearLayout>(R.id.llContainer)

            val rlPreStudy by lazy {
                LayoutInflater.from(mContext).inflate(R.layout.item_todo_task_pre_study, llContainer, false)
            }
            val tvPreStudyGold by lazy {
                rlPreStudy.findViewById<TextView>(R.id.tvPreStudyGold)
            }
            val rlPreCourseware by lazy {
                LayoutInflater.from(mContext).inflate(R.layout.item_todo_task_pre_course_ware, llContainer, false)
            }
            val tvPreCoursewareGold by lazy {
                rlPreCourseware.findViewById<TextView>(R.id.tvPreCoursewareGold)
            }
            val rlSpecialClass by lazy {
                LayoutInflater.from(mContext).inflate(R.layout.item_todo_task_special_class, llContainer, false)
            }
            val tvSpecialClassGold by lazy {
                rlSpecialClass.findViewById<TextView>(R.id.tvSpecialClassGold)
            }
            val rlHomework by lazy {
                LayoutInflater.from(mContext).inflate(R.layout.item_todo_task_homework, llContainer, false)
            }
            val tvHomeworkGold by lazy {
                rlHomework.findViewById<TextView>(R.id.tvHomeworkGold)
            }
            val rlEnglishOral by lazy {
                LayoutInflater.from(mContext).inflate(R.layout.item_todo_task_english_oral, llContainer, false)
            }
            val tvEnglishOralGold by lazy {
                rlEnglishOral.findViewById<TextView>(R.id.tvEnglishOralGold)
            }
            val rlEnglishRecite by lazy {
                LayoutInflater.from(mContext).inflate(R.layout.item_todo_task_english_recite, llContainer, false)
            }
            val tvEnglishReciteGold by lazy {
                rlEnglishRecite.findViewById<TextView>(R.id.tvEnglishReciteGold)
            }

            if (llContainer.childCount > 1) {
                llContainer.removeViews(1, llContainer.childCount - 1)
            }

            addOnClickListener(R.id.rlLessonInfo)

            ivSubject.setImageResource(SubjectId.getSubjectLabel(data?.subjectId))
            tvLessonName.text = "第" + data?.lessonNum + "讲 " + data?.lessonName
            tvClassName.text = data?.className

            data?.tasks?.forEach {
                when (it.type) {
                    6 -> {
                        //预习
                        llContainer.addView(rlPreStudy)
                        rlPreStudy.setOnClickListener {
                            onItemChildClickListener?.onItemChildClick(this@TodoTaskAdapter, it, getClickPosition(this))
                        }
                        rlPreStudy.visibility = View.VISIBLE
                        tvPreStudyGold.text = "+" + it.gold
                    }
                    10 -> {
                        //预习课件 在线外教
                        llContainer.addView(rlPreCourseware)
                        rlPreCourseware.setOnClickListener {
                            onItemChildClickListener?.onItemChildClick(this@TodoTaskAdapter, it, getClickPosition(this))
                        }
                        rlPreCourseware.visibility = View.VISIBLE
                        tvPreCoursewareGold.text = "+" + it.gold
                    }
                    5 -> {
                        //专题课
                        llContainer.addView(rlSpecialClass)
                        rlSpecialClass.setOnClickListener {
                            onItemChildClickListener?.onItemChildClick(this@TodoTaskAdapter, it, getClickPosition(this))
                        }
                        rlSpecialClass.visibility = View.VISIBLE
                        tvSpecialClassGold.text = "+" + it.gold
                    }
                    1 -> {
                        //自我巩固
                        llContainer.addView(rlHomework)
                        rlHomework.setOnClickListener {
                            onItemChildClickListener?.onItemChildClick(this@TodoTaskAdapter, it, getClickPosition(this))
                        }
                        rlHomework.visibility = View.VISIBLE
                        tvHomeworkGold.text = "+" + it.gold
                    }
                    2 -> {
                        //口语练习
                        llContainer.addView(rlEnglishOral)
                        rlEnglishOral.setOnClickListener {
                            onItemChildClickListener?.onItemChildClick(this@TodoTaskAdapter, it, getClickPosition(this))
                        }
                        rlEnglishOral.visibility = View.VISIBLE
                        tvEnglishOralGold.text = "+" + it.gold
                    }
                    8 -> {
                        //课文背诵
                        llContainer.addView(rlEnglishRecite)
                        rlEnglishRecite.setOnClickListener {
                            onItemChildClickListener?.onItemChildClick(this@TodoTaskAdapter, it, getClickPosition(this))
                        }
                        rlEnglishRecite.visibility = View.VISIBLE
                        tvEnglishReciteGold.text = "+" + it.gold
                    }
                }
            }
        }
    }

    private fun getClickPosition(holder: BaseViewHolder): Int {
        return if (holder.layoutPosition >= headerLayoutCount) {
            holder.layoutPosition - headerLayoutCount
        } else 0
    }
}
