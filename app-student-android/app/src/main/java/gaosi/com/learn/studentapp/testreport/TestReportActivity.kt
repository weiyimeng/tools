package gaosi.com.learn.studentapp.testreport

import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.net.callback.GSStringCallback
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.util.SchemeDispatcher
import com.lzy.okgo.model.Response
import gaosi.com.learn.R
import gaosi.com.learn.application.AppApi
import gaosi.com.learn.bean.TestReportClassBean
import gaosi.com.learn.bean.TestReportDetailBean
import gaosi.com.learn.bean.TestReportGradeBean
import gaosi.com.learn.util.OnItemDoubleClickCheckListener
import gaosi.com.learn.view.AxxEmptyView
import kotlinx.android.synthetic.main.activity_test_report.*
import org.json.JSONObject

/**
 * description:
 * created by huangshan on 2020-04-10 11:47
 */
@Router("testReport")
class TestReportActivity : BaseActivity() {

    private var mGradeIndex = 0 //当前选择年级
    private var mGradeList = arrayListOf(TestReportGradeBean(0, "全部年级", 0, 3, true))
    private var mGradeAdapter: GradeListAdapter? = null
    private var mTestReportClassList: ArrayList<TestReportClassBean>? = null
    private var mTestReportListAdapter: TestReportListAdapter? = null
    private var mEmptyView: AxxEmptyView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_report)
        initData()
    }

    override fun initView() {
        super.initView()
        title_bar.setLeftListener(this)
        tvSelector.setOnClickListener(this)
        initAdapter()
    }

    private fun initAdapter() {
        mGradeAdapter = GradeListAdapter()
        mGradeAdapter?.onItemClickListener = object : OnItemDoubleClickCheckListener() {
            override fun onDoubleClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if (mGradeList[position].itemType == 2 || mGradeList[position].itemType == 3) {
                    mGradeIndex = position
                    mGradeList.forEachIndexed { index, testReportGradeBean ->
                        testReportGradeBean.isClicked = index == position
                    }
                    mGradeAdapter?.notifyDataSetChanged()
                    mHandler.postDelayed({
                        if (mGradeList[position].type == 0) {
                            mTestReportListAdapter?.setNewData(mTestReportClassList)
                            recyclerView.smoothScrollToPosition(0)
                        } else {
                            val gradeId = mGradeList[position].gradeId
                            val filterList = mTestReportClassList?.filter {
                                it.gradeId == gradeId
                            }
                            mTestReportListAdapter?.setNewData(filterList)
                            recyclerView.smoothScrollToPosition(0)
                        }
                        DialogUtil.getInstance().dismiss()
                    }, 300)
                }
            }
        }
        mTestReportListAdapter = TestReportListAdapter()
        recyclerView.adapter = mTestReportListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        mTestReportListAdapter?.setOnItemChildClickListener { _, view, position ->
            val testReportClassBean = mTestReportClassList?.get(position)
            when (view.id) {
                R.id.rl_midterm -> {
                    val testReportDetailBean = testReportClassBean?.reports?.get(0)
                    var url = "axx://commonJsWeb?url=%s&title=检测报告"
                    url = String.format(url, Uri.encode(testReportDetailBean?.reportUrl))
                    SchemeDispatcher.jumpPage(this, url)
                    collectClickEvent("XSD_404")
                    if (testReportDetailBean?.viewed == 0) {
                        requestSaveRecord(testReportDetailBean, testReportClassBean.classId, position)
                    }
                }
                R.id.rl_finalterm -> {
                    val size = testReportClassBean?.reports?.size ?: 0
                    val testReportDetailBean = if (size > 1) {
                        testReportClassBean?.reports?.get(1)
                    } else {
                        testReportClassBean?.reports?.get(0)
                    }
                    var url = "axx://commonJsWeb?url=%s&title=检测报告"
                    url = String.format(url, Uri.encode(testReportDetailBean?.reportUrl))
                    SchemeDispatcher.jumpPage(this, url)
                    collectClickEvent("XSD_405")
                    if (testReportDetailBean?.viewed == 0) {
                        requestSaveRecord(testReportDetailBean, testReportClassBean?.classId, position)
                    }
                }
            }
        }
    }

    /**
     * 空页面
     */
    private val getEmptyView: View?
        get() {
            mEmptyView = AxxEmptyView(this)
            mEmptyView?.setClickCallBackListener(object : AxxEmptyView.OnClickCallBackListener {
                override fun onClick() {
                    requestReportList()
                }
            })
            return mEmptyView
        }

    private fun setEmptyStatus(emptyType: Int) {
        mTestReportListAdapter?.setNewData(null)
        mTestReportListAdapter?.emptyView = getEmptyView
        when (emptyType) {
            1 -> {
                mEmptyView?.setEmptyIcon(R.drawable.icon_net_error)
                mEmptyView?.setEmptyText(getString(R.string.error_text_net))
                mEmptyView?.setButtonText(getString(R.string.click_refresh))
                mEmptyView?.setEmptyVisibility(showImg = true, showText = true, showButton = true)
            }
            2 -> {
                mEmptyView?.setEmptyVisibility(showImg = true, showText = true, showButton = false)
                mEmptyView?.setEmptyIcon(R.drawable.icon_today_no_course)
                mEmptyView?.setEmptyText(getString(R.string.error_text_no_test_report))
            }
        }
    }

    /**
     * 爱上学习，收获成长
     */
    private val footerView: View
        get() {
            return View.inflate(this, R.layout.fragment_study_footer, null)
        }

    private fun initData() {
        requestReportGrade()
        requestReportList()
    }

    /**
     * 获取存在测评报告的年级
     */
    private fun requestReportGrade() {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        GSRequest.startRequest(AppApi.testReportGrade, paramMap, object : GSJsonCallback<ArrayList<TestReportGradeBean>>() {
            override fun onResponseSuccess(reponse: Response<*>?, code: Int, result: GSHttpResponse<ArrayList<TestReportGradeBean>>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                if (result.body == null) {
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                val dataList = result.body
                if (dataList.isNullOrEmpty()) {
                    return
                }
                dataList.forEach {
                    when (it.type) {
                        1, 2, 3 -> {
                            it.itemType = 2
                        }
                    }
                }
                mGradeList.addAll(dataList)

                val primaryIndex = mGradeList.indexOfFirst {
                    it.type == 1
                }
                mGradeList.add(primaryIndex, TestReportGradeBean(gradeName = "小学阶段", itemType = 1))
                val juniorIndex = mGradeList.indexOfFirst {
                    it.type == 2
                }
                mGradeList.add(juniorIndex, TestReportGradeBean(gradeName = "初中阶段", itemType = 1))
                val highIndex = mGradeList.indexOfFirst {
                    it.type == 3
                }
                mGradeList.add(highIndex, TestReportGradeBean(gradeName = "高中阶段", itemType = 1))
            }

            override fun onResponseError(reponse: Response<*>?, code: Int, message: String?) {
            }
        })
    }

    /**
     * 获取报告列表
     */
    private fun requestReportList() {
        showLoadingProgressDialog()
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        if (mGradeIndex != 0) {
            //没有选择全部年级
            paramMap["gradeId"] = mGradeList[mGradeIndex].gradeId.toString()
        }
        GSRequest.startRequest(AppApi.testReportList, paramMap, object : GSJsonCallback<ArrayList<TestReportClassBean>>() {
            override fun onResponseSuccess(reponse: Response<*>?, code: Int, result: GSHttpResponse<ArrayList<TestReportClassBean>>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                dismissLoadingProgressDialog()
                if (showResponseErrorMessage(result) == 0) {
                    setEmptyStatus(1)
                    return
                }
                if (result.body == null) {
                    if (mTestReportClassList.isNullOrEmpty()) {
                        setEmptyStatus(2)
                    }
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                mTestReportClassList = result.body
                if (mTestReportClassList.isNullOrEmpty()) {
                    setEmptyStatus(2)
                } else {
                    mTestReportListAdapter?.setNewData(mTestReportClassList)
                    mTestReportListAdapter?.addFooterView(footerView)
                }
            }

            override fun onResponseError(reponse: Response<*>?, code: Int, message: String?) {
                if (isFinishing || isDestroyed) {
                    return
                }
                dismissLoadingProgressDialog()
                setEmptyStatus(1)
                message ?: return
                ToastUtil.showToast(message)
            }
        })
    }

    /**
     * 保存查看测评报告记录
     */
    private fun requestSaveRecord(testReportDetailBean: TestReportDetailBean, classId: Int?, position: Int) {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["classId"] = classId.toString()
        paramMap["reportId"] = testReportDetailBean.reportId.toString()
        GSRequest.startRequest(AppApi.saveTestReportRecord, paramMap, object : GSStringCallback() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: String) {
                val json = JSONObject(result)
                val status = json.optInt("status")
                if (status == 1) {
                    testReportDetailBean.viewed = 1
                    mTestReportListAdapter?.notifyItemChanged(position)
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
            }
        })
    }

    /**
     * 显示年级选择弹框
     */
    private fun showGradeListDialog() {
        DialogUtil.getInstance().create(this, R.layout.ui_test_report_grade_dialog, 0.85F)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener?) {
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                        mGradeAdapter?.setSpanSizeLookup { _, position ->
                            if (mGradeList[position].itemType == 1 || mGradeList[position].itemType == 3) {
                                3
                            } else {
                                1
                            }
                        }
                        val gridLayoutManager = GridLayoutManager(this@TestReportActivity, 3)
                        recyclerView.layoutManager = gridLayoutManager
                        recyclerView.adapter = mGradeAdapter
                        mGradeAdapter?.setNewData(mGradeList)
                        bindListener(onClickListener, R.id.ivClose)
                    }

                    override fun onClick(v: View?, dialog: DialogUtil?) {
                        super.onClick(v, dialog)
                        when (v?.id) {
                            R.id.ivClose -> {
                                dialog?.dismiss()
                            }
                        }
                    }

                    override fun onDismiss() {
                        super.onDismiss()
                        tvSelector.text = mGradeList[mGradeIndex].gradeName
                    }
                })
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> finish()
            R.id.tvSelector -> showGradeListDialog()
        }
    }

    override fun getResources(): Resources {
        val resources = super.getResources()
        val configuration = resources.configuration
        configuration.fontScale = 1.0f
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return resources
    }
}
