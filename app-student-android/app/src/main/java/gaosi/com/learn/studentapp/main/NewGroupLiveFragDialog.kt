package gaosi.com.learn.studentapp.main

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import android.widget.Button
import android.widget.EditText
import com.gaosi.newgrouplive.jump.JumpDoNewGroupLiveBuilder
import com.gaosi.newgrouplive.jump.JumpNewGroupLiveListener
import com.gaosi.newgrouplive.jump.NewGroupLiveOptions
import com.gsbaselib.utils.ToastUtil
import com.gstudentlib.util.LoadingDialog
import gaosi.com.learn.R

/**
 * <新版直播进入房间中间页>
 *
 * @author  tianyejun
 * @version  [版本号]
 * @see  [参考资料]
 * @since  [历史 创建日期:2020/4/25]
 */
class NewGroupLiveFragDialog : DialogFragment(), View.OnClickListener {
    private var mUserId: EditText? = null
    private var mCourseId: EditText? = null
    private var mLessonId: EditText? = null
    private var mEnterRoomBtn: Button? = null

    private var mClearUserId: Button? = null
    private var mClearCourseId: Button? = null
    private var mClearLessonId: Button? = null
    private var preferences: SharedPreferences? = null

    private val userIdKey: String = "userIdKey"
    private val courseIdKey: String = "courseId"
    private val lessonIdKey: String = "lessonId"
    private var mPageId: String? = ""
    private var mLoadingDialog: LoadingDialog? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        preferences = context!!.getSharedPreferences("newLiveConfig", Context.MODE_PRIVATE)

        val mRootView = inflater.inflate(R.layout.newlive_dialog_jump_layout, container, false) as ViewGroup
        mUserId = mRootView.findViewById<View>(R.id.mUserId) as EditText
        mCourseId = mRootView.findViewById<View>(R.id.mCourseId) as EditText
        mLessonId = mRootView.findViewById<View>(R.id.mLessonId) as EditText

        mClearUserId = mRootView.findViewById(R.id.mClearUserId)
        mClearCourseId = mRootView.findViewById(R.id.mClearCourseId)
        mClearLessonId = mRootView.findViewById(R.id.mClearLessonId)
        mEnterRoomBtn = mRootView.findViewById(R.id.enterRoomBtn)

        initData()
        initListener()
        return mRootView
    }

    private fun initData() {
        mPageId = arguments?.getString("pageId")
        val userId: String? = preferences?.getString(userIdKey, "")
        val courseId: Int? = preferences?.getInt(courseIdKey, 0)
        val lessonId: Int? = preferences?.getInt(lessonIdKey, 0)
        mUserId?.setText(userId)
        if (courseId == 0) {
            mCourseId?.setText("")
        } else {
            mCourseId?.setText(courseId.toString())
        }

        if (lessonId == 0) {
            mLessonId?.setText("")
        } else {
            mLessonId?.setText(lessonId.toString())
        }
    }

    private fun initListener() {
        mClearUserId?.setOnClickListener(this)
        mClearCourseId?.setOnClickListener(this)
        mClearLessonId?.setOnClickListener(this)
        mEnterRoomBtn?.setOnClickListener(this)
    }


    override fun onResume() {
        super.onResume()
        val window = dialog.window
        if (window != null) {
            val lp = window.attributes
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            lp.gravity = Gravity.CENTER
            lp.dimAmount = 0.3f
            window.attributes = lp
            window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mClearUserId -> {
                mUserId?.setText("")
            }
            R.id.mClearCourseId -> {
                mCourseId?.setText("")

            }
            R.id.mClearLessonId -> {
                mLessonId?.setText("")
            }
            R.id.enterRoomBtn -> {
                val userId = mUserId?.text.toString().trim()
                val courseId = mCourseId?.text.toString().trim().toInt()
                val lessonId = mLessonId?.text.toString().trim().toInt()

                val editor = preferences!!.edit()
                editor.putString(userIdKey, userId)
                editor.putInt(courseIdKey, courseId)
                editor.putInt(lessonIdKey, lessonId)
                editor.apply()
                //进入直播间
                jumpLiveRoom(userId, courseId, lessonId)
            }
        }
    }

    private fun jumpLiveRoom(userId: String, courseId: Int, lessonId: Int) {
        NewGroupLiveOptions.instance
                .with(activity)
                .pad(mPageId)
                .start(JumpDoNewGroupLiveBuilder()
                        .setGaoSiId(userId)
                        .setCourseId(courseId)
                        .setLessonId(lessonId)
                        .isPlayback(false)
                        .setJumpNewGroupLiveListener(object : JumpNewGroupLiveListener {
                            override fun onStart() {
                                showLoadingProcessDialog()
                            }

                            override fun onRoomLogin() {
                            }

                            override fun onRoomLoginSuccess() {
                                dismissLoadingProcessDialog()
                            }

                            override fun onRoomLoginError(message: String?) {
                                dismissLoadingProcessDialog()
                                ToastUtil.showToast(message + "")
                            }
                        }))
    }

    private fun dismissLoadingProcessDialog() {
        if (mLoadingDialog != null && mLoadingDialog!!.isShowing && isAdded) {
            mLoadingDialog!!.dismiss()
        }
    }

    private fun showLoadingProcessDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog(context!!)
        }

        if (!mLoadingDialog!!.isShowing && isAdded) {
            mLoadingDialog!!.show()
        }
    }
}