package gaosi.com.learn.studentapp.classlesson.myclass

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.base.event.EventBean
import com.gsbaselib.base.inject.GSAnnotation
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.StatusBarUtil
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.TypeValue
import com.gsbaselib.utils.net.NetworkUtil
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.util.SchemeDispatcher
import com.lzy.okgo.model.Response
import gaosi.com.learn.R
import gaosi.com.learn.application.AppApi
import gaosi.com.learn.application.event.AppEventType
import gaosi.com.learn.bean.classlesson.myclass.CourseRemindBean
import gaosi.com.learn.bean.classlesson.myclass.MyClassData
import gaosi.com.learn.bean.classlesson.myclass.MyClassInfoBean
import gaosi.com.learn.bean.classlesson.myclass.SubjectBean
import gaosi.com.learn.studentapp.active.ActiveClassActivity
import gaosi.com.learn.util.DrawableUtil
import gaosi.com.learn.view.AxxEmptyView
import gaosi.com.learn.view.AxxLoadMoreView
import kotlinx.android.synthetic.main.activity_my_class.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.Serializable
import java.util.HashMap

/**
 * description:
 * created by huangshan on 2020/6/12 上午10:51
 */
@Router("myClass")
@GSAnnotation(pageId = StatisticsDictionary.allHomework)
class MyClassActivity : BaseActivity() {

    val INTO_CLASS_SUCCESS: Int = 0x102
    private var mGsId = 0L
    private var mSubjectId = 0
    private var mCourseState = 0
    private var mPageNum = 1

    //是否有下一页
    private var mNextPage = 0
    private var mEmptyView: AxxEmptyView? = null
    private var mMyClassAdapter: MyClassAdapter? = null
    private var mSubjectList = arrayListOf(SubjectBean(0, "全部", true))
    private var mClassStatusList = arrayListOf(ClassStatus(0, true), ClassStatus(2, false), ClassStatus(3, false))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_class)
        EventBus.getDefault().register(this)
        initAdapter()
        initData()
    }

    override fun onResume() {
        super.onResume()
        requestCourseRemind()
    }

    override fun initView() {
        super.initView()
        title_bar.setLeftListener(this)
        tv_join_class.background = DrawableUtil.createShape(Color.parseColor("#1ED278"), TypeValue.dp2px(32f))
        tv_join_class.setOnClickListener(this)
        tvSubject.setOnClickListener(this)
        tvAll.setOnClickListener(this)
        rlCheckCourse.setOnClickListener(this)
        mEmptyView = AxxEmptyView(this)
        mEmptyView?.setClickCallBackListener(object : AxxEmptyView.OnClickCallBackListener {
            override fun onClick() {
                mPageNum = 1
                initData()
            }
        })
    }

    private fun initData() {
        requestMyClass()
        requestSubjectList()
    }

    private fun initAdapter() {
        mMyClassAdapter = MyClassAdapter()
        mMyClassAdapter?.run {
            val axxLoadMoreView = AxxLoadMoreView()
            axxLoadMoreView.setLoadEndId(0)
            setLoadMoreView(axxLoadMoreView)
            setOnLoadMoreListener({
                if (mNextPage == 1) {
                    requestMyClass()
                } else {
                    //数据加载完毕
                    loadMoreEnd()
                }
            }, recyclerView)
            setOnItemClickListener { adapter, _, position ->
                val data = adapter.getItem(position) as MyClassInfoBean
                if (data.courseType == 1) {
                    var url = "axx://courseDetailedPage?courseId=%s&gsId=%s&courseTypeFlag=%s&lectureId=%s&lecturePackageId=%s&lectureType=%s"
                    url = String.format(url, data.id, data.gsId, data.courseTypeFlag, data.lectureId, data.lecturePackageId, data.lectureType)
                    SchemeDispatcher.jumpPage(this@MyClassActivity, url)
                } else {
                    var url = "axx://lessonDetail?classId=%s"
                    url = String.format(url, data.id)
                    SchemeDispatcher.jumpPage(this@MyClassActivity, url)
                }
                collectClickEvent("XSD_551")
            }
        }
        recyclerView.adapter = mMyClassAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun requestMyClass() {
        if (!NetworkUtil.isConnected(this)) {
            ToastUtil.showToast("网络未连接")
            if (mPageNum != 1) {
                mMyClassAdapter?.loadMoreFail()
            } else {
                //网络错误
                setEmptyStatus(1)
            }
            return
        }
        showLoadingProgressDialog()
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["subjectId"] = mSubjectId.toString() //0 查询全部学科  其他学科
        paramMap["courseState"] = mCourseState.toString() //0 全部 2 学习中 3已结课
        paramMap["pageNum"] = mPageNum.toString()
        GSRequest.startRequest(AppApi.getAllClass, paramMap, object : GSJsonCallback<MyClassData>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<MyClassData>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                dismissLoadingProgressDialog()
                if (showResponseErrorMessage(result) == 0) {
                    if (mPageNum != 1) {
                        mMyClassAdapter?.loadMoreFail()
                    } else {
                        //网络错误
                        setEmptyStatus(1)
                    }
                    return
                }
                if (result.body == null) {
                    if (mPageNum != 1) {
                        mMyClassAdapter?.loadMoreFail()
                    } else {
                        //网络错误
                        setEmptyStatus(1)
                    }
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                val myClassData = result.body
                mNextPage = myClassData.nextPage ?: 0
                val classList = myClassData.classList
                if (classList.isNullOrEmpty()) {
                    if (mPageNum == 1) {
                        when (mCourseState) {
                            0 -> {
                                setEmptyStatus(2)
                            }
                            2 -> {
                                setEmptyStatus(3)
                            }
                            3 -> {
                                setEmptyStatus(4)
                            }
                        }
                    } else {
                        mMyClassAdapter?.loadMoreFail()
                    }
                } else {
                    if (mPageNum == 1) {
                        mMyClassAdapter?.setNewData(classList)
                    } else {
                        mMyClassAdapter?.addData(classList)
                    }
                    if (mNextPage == 1) {
                        mPageNum++
                        mMyClassAdapter?.loadMoreComplete()
                    } else {
                        mMyClassAdapter?.loadMoreEnd()
                    }
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (isFinishing || isDestroyed) {
                    return
                }
                dismissLoadingProgressDialog()
                if (mPageNum != 1) {
                    mMyClassAdapter?.loadMoreFail()
                } else {
                    //网络错误
                    setEmptyStatus(1)
                }
                message ?: return
                ToastUtil.showToast(message)
            }
        })
    }

    private fun requestSubjectList() {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        GSRequest.startRequest(AppApi.getSubjectList, paramMap, object : GSJsonCallback<ArrayList<SubjectBean>>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<ArrayList<SubjectBean>>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                if (result.body == null) {
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                mSubjectList.removeAll {
                    it.subjectId != 0
                }
                mSubjectList.addAll(result.body)
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
            }
        })
    }

    private fun requestCourseRemind() {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        GSRequest.startRequest(AppApi.courseRemind, paramMap, object : GSJsonCallback<CourseRemindBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<CourseRemindBean>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                if (result.body == null) {
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                val data = result.body
                mGsId = data.gsId ?: 0L
                if (data.remindNum ?: 0 >= 1) {
                    rlCheckCourse.visibility = View.VISIBLE
                } else {
                    rlCheckCourse.visibility = View.GONE
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
            }
        })
    }

    /**
     * 空页面点击类型  1网络错误 2暂无报名班级 3暂无正在学习班级 4暂无已结课班级
     */
    private fun setEmptyStatus(emptyType: Int) {
        mMyClassAdapter?.setNewData(null)
        mMyClassAdapter?.emptyView = mEmptyView
        when (emptyType) {
            1 -> {
                mEmptyView?.setEmptyIcon(R.drawable.icon_net_error)
                mEmptyView?.setEmptyText(getString(R.string.error_text_net))
                mEmptyView?.setButtonText(getString(R.string.click_refresh))
                mEmptyView?.setEmptyVisibility(showImg = true, showText = true, showButton = true)
            }
            2 -> {
                mEmptyView?.setEmptyIcon(R.drawable.icon_myclass_no_registered_class)
                mEmptyView?.setEmptyText("暂无报名班级 \n快去爱学习合作机构报名吧~")
                mEmptyView?.setEmptyVisibility(showImg = true, showText = true, showButton = false)
            }
            3 -> {
                mEmptyView?.setEmptyIcon(R.drawable.icon_today_no_course)
                mEmptyView?.setEmptyText("暂无正在学习的班级~")
                mEmptyView?.setEmptyVisibility(showImg = true, showText = true, showButton = false)
            }
            4 -> {
                mEmptyView?.setEmptyIcon(R.drawable.icon_today_no_course)
                mEmptyView?.setEmptyText("暂无已结课的班级~")
                mEmptyView?.setEmptyVisibility(showImg = true, showText = true, showButton = false)
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> finish()
            R.id.tv_join_class -> {
                val intent = Intent(this, ActiveClassActivity::class.java)
                startActivityForResult(intent, INTO_CLASS_SUCCESS)
                collectClickEvent("as401_clk_myclass_joinclass")
            }
            R.id.tvSubject -> {
                collectClickEvent("XSD_553")
                SelectSubjectDialogFragment.newInstance(mSubjectList).show(supportFragmentManager, "select_subject_dialog")
            }
            R.id.tvAll -> {
                collectClickEvent("XSD_554")
                ClassStatusDialogFragment.newInstance(mClassStatusList).show(supportFragmentManager, "class_status_dialog")
            }
            R.id.rlCheckCourse -> {
                var url = "axx://courseCodeVerifyPage?url=http://www.aixuexi.com/checkCode.html&gsId=%s"
                url = String.format(url, mGsId.toString())
                SchemeDispatcher.jumpPage(this@MyClassActivity, url)
                collectClickEvent("XSD_552")
            }
            else -> {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == ActiveClassActivity.INTO_CLASS_SUCCESS) {
            //加入班级成功，重新打开页面
            val url = "axx://myClass"
            SchemeDispatcher.jumpPage(this, url)
            finish()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectClassType(event: EventBean) {
        if (event.what == AppEventType.SELECT_SUBJECT) {
            val subjectBean = event.obj as SubjectBean
            if (mSubjectId == subjectBean.subjectId) {
                return
            }
            mSubjectId = subjectBean.subjectId ?: 0
            if (mSubjectId == 0) {
                tvSubject.text = "学科"
            } else {
                tvSubject.text = subjectBean.subjectName
            }
            mPageNum = 1
            requestMyClass()
            recyclerView?.scrollToPosition(0)
        }
        if (event.what == AppEventType.SELECT_CLASS_STATUS) {
            if (mCourseState == event.arg1) {
                return
            }
            mCourseState = event.arg1
            when (mCourseState) {
                0 -> tvAll.text = "全部"
                2 -> tvAll.text = "正在学"
                3 -> tvAll.text = "已结课"
            }
            mPageNum = 1
            requestMyClass()
            recyclerView?.scrollToPosition(0)
        }
    }

    /**
     * type 0全部 2正在学 3已结课
     */
    data class ClassStatus(var type: Int = 1, var isClicked: Boolean = false) : Serializable

    override fun getResources(): Resources {
        val resources = super.getResources()
        val configuration = resources.configuration
        configuration.fontScale = 1.0f
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return resources
    }

    override fun setStatusBar() {
        setStatusBar(Color.parseColor("#F8FAFD"), 0)
        StatusBarUtil.setLightMode(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
