package gaosi.com.learn.studentapp.dresscity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.alibaba.fastjson.JSON
import com.gsbaselib.base.GSBaseFragment
import com.gsbaselib.base.bean.BaseData
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.TypeValue
import com.gsbaselib.utils.customevent.OnNoDoubleClickListener
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbaselib.utils.glide.ImageLoader
import com.gsbaselib.utils.upload.Upload
import com.gstudentlib.GSAPI
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.base.STBaseFragment
import com.lzy.okgo.model.Response
import gaosi.com.learn.R
import gaosi.com.learn.bean.dress.dressself.DressSelfData
import gaosi.com.learn.bean.dress.dressself.DressSelfFaceUMyItemCategory
import gaosi.com.learn.bean.dress.dressself.DressSelfMyItem
import gaosi.com.learn.studentapp.dresscity.adapter.FragmentAdapter
import gaosi.com.learn.studentapp.dresscity.fragment.DressSelfFragment
import kotlinx.android.synthetic.main.activity_dress_city_self.*
import java.util.ArrayList

class DressCitySelfFragment: STBaseFragment(), TabLayout.OnTabSelectedListener {

    private var mCommentVpAdapter: FragmentAdapter? = null
    private var mDressSelfData: DressSelfData? = null
    private var mFragments: ArrayList<GSBaseFragment>? = ArrayList()
    private var tempMyItem: DressSelfMyItem? = null
    private var isFirstLoad: Boolean = true
    private var mCurrPosition: Int = 0
    private var coinCount: String? = "0"
    private var mDressName: String? = ""
    private var mFromPage: String? = ""

    companion object {
        private const val COIN_COUNT = "coinCount"
        private const val DRESS_NAME = "dressName"
        private const val FROM_PAGE = "fromPage"
        fun instance(coinCount: String , mDressName: String , mFromPage: String): DressCitySelfFragment {
            return DressCitySelfFragment().apply {
                arguments = Bundle().apply {
                    putString(COIN_COUNT, coinCount)
                    putString(DRESS_NAME, mDressName)
                    putString(FROM_PAGE, mFromPage)
                }
            }
        }
    }

    override fun initView(inflater: LayoutInflater?, container: ViewGroup?): View {
        return inflater ?. inflate (R.layout.activity_dress_city_self, container, false) as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinCount = arguments?.getString(COIN_COUNT)
        mDressName = arguments?.getString(DRESS_NAME)
        mFromPage = arguments?.getString(FROM_PAGE)
        this.adjustBgSize()
        btnSave.setOnClickListener(this)
        btnExtracting.setOnClickListener(object : OnNoDoubleClickListener(){
            override fun onNoDoubleClick(p0: View?) {
                (activity as DressCityActivity).goExtracting()
            }
        })
        showLoadingProcessDialog()
        this.initData()
    }

    /**
     * 根据手机长宽比配置
     */
    private fun adjustBgSize() {
        if (!isPad) {
            val rate: Float = STBaseConstants.deviceInfoBean.screenHeight.toFloat() / STBaseConstants.deviceInfoBean.screenWidth.toFloat()
            if(rate <= 1.71f) {
                val params = rlBg.layoutParams
                params.height = TypeValue.dp2px(290f)
                rlBg.layoutParams = params
                rlBg.setBackgroundResource(R.drawable.bg_dress_city_small)
            }else {
                val params = rlBg.layoutParams
                params.height = TypeValue.dp2px(310f)
                rlBg.layoutParams = params
                rlBg.setBackgroundResource(R.drawable.bg_dress_city)
            }
        }
    }

    /**
     * 请求数据
     */
    fun initData() {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        GSRequest.startRequest(GSAPI.getDressSelf, paramMap, object : GSJsonCallback<DressSelfData>() {

            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<DressSelfData>) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                dismissLoadingProcessDialog()
                isFirstLoad = false
                mDressSelfData = result.body ?: return
                mDressSelfData?.let {
                    updateTempDress()
                    dsmDress?.showDress(it.faceUMyFace?.myItem)
                    loadDressSelf()
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                dismissLoadingProcessDialog()
                isFirstLoad = false
                ToastUtil.showToast(message + "")
            }
        })
    }

    /**
     * 加载个人形象
     */
    private fun loadDressSelf() {
        this.reset()
        mDressSelfData?.faceUMyItemCategory?.let {
            (activity as DressCityActivity).updateFreeNumber(mDressSelfData?.firestFreeNumber?:0)
            for (i in 0 until it.size) {
                val dressCenterFragment = DressSelfFragment()
                dressCenterFragment.setCurrPosition(i)
                mFragments?.add(dressCenterFragment)
                tlDress.addTab(tlDress.newTab())
            }
            mCommentVpAdapter = mFragments?.let { it1 -> FragmentAdapter(childFragmentManager, it1) }
            vpDress.adapter = mCommentVpAdapter
            vpDress.offscreenPageLimit = mFragments?.size ?: 0
            tlDress.setupWithViewPager(vpDress)
            //设置自定义视图
            for (i in 0 until tlDress.tabCount) {
                val tab = tlDress.getTabAt(i)
                tab?.customView = this.createView(i)
            }
            tlDress.addOnTabSelectedListener(this)
            adapterPosition()
            if (mCurrPosition < tlDress.tabCount) {
                vpDress.currentItem = mCurrPosition
                tlDress.getTabAt(mCurrPosition)?.select()
            } else {
                this.mCurrPosition = 0
            }
        }
    }

    /**
     * 分析抽出的
     */
    private fun adapterPosition(){
        mDressSelfData?.faceUMyItemCategory?.let {
            for (i in 0 until it.size) {
                if(TextUtils.isEmpty(mDressName)) {
                    break
                }
                if(it[i].assistCode.equals(mDressName)) {
                    mCurrPosition = i
                    break
                }
            }
            mDressName = ""
        }
    }

    /**
     * 创建tab view
     * @param position
     * @return
     */
    private fun createView(position: Int): View {
        val mDressSelfFaceUMyItemCategory = mDressSelfData?.faceUMyItemCategory?.get(position)
        val container = LayoutInflater.from(activity).inflate(R.layout.activity_dress_self_tab_item, null)
        val iviIcon = container.findViewById<ImageView>(R.id.iviIcon)
        container.tag = iviIcon
        mDressSelfFaceUMyItemCategory?.let {
            ImageLoader.setNetImageResource(iviIcon, if (position == 0) it.iconLightUrl
                    ?: "" else it.iconUrl ?: "")
        }
        return container
    }

    /**
     * 更新装扮形象
     */
    fun updateDress(assistCode: String, code: String?) {
        LogUtil.d("assistCode:" + assistCode + "code:" + code)
        mDressSelfData?.faceUMyFace?.myItem?.run {
            when (assistCode) {
                "eye" -> {
                    eye = code
                }
                "nose" -> {
                    nose = code
                }
                "mouth" -> {
                    mouth = code
                }
                "hairStyle" -> {
                    hairStyle = code
                }
                "glass" -> {
                    glass = code
                }
                "handShow" -> {
                    handShow = code
                }
                "background" -> {
                    background = code
                }
                "cloth" -> {
                    cloth = code
                }
            }
            dsmDress?.showDress(this)
        }
    }

    /**
     * 更新临时记录池
     */
    private fun updateTempDress() {
        tempMyItem = DressSelfMyItem()
        mDressSelfData?.faceUMyFace?.myItem?.run {
            tempMyItem?.eye = eye
            tempMyItem?.nose = nose
            tempMyItem?.mouth = mouth
            tempMyItem?.hairStyle = hairStyle
            tempMyItem?.glass = glass
            tempMyItem?.handShow = handShow
            tempMyItem?.background = background
            tempMyItem?.cloth = cloth
        }
    }

    /**
     * 检测用户有没有修改过数据
     */
    private fun checkDressIsChanged(): Boolean {
        var isChange = false
        dsmDress?.let {
            if(TextUtils.isEmpty(it.bitmapBase64)) {
                return isChange
            }
        }
        mDressSelfData?.faceUMyFace?.myItem?.run {
            //检测用户有没有修改过数据
            if (tempMyItem != null) {
                if (tempMyItem?.eye != eye) {
                    isChange = true
                }
                if (tempMyItem?.background != background) {
                    isChange = true
                }
                if (tempMyItem?.glass != glass) {
                    isChange = true
                }
                if (tempMyItem?.hairStyle != hairStyle) {
                    isChange = true
                }
                if (tempMyItem?.handShow != handShow) {
                    isChange = true
                }
                if (tempMyItem?.mouth != mouth) {
                    isChange = true
                }
                if (tempMyItem?.nose != nose) {
                    isChange = true
                }
                if (tempMyItem?.cloth != cloth) {
                    isChange = true
                }
            }
        }
        return isChange
    }

    /**
     * 保存装扮
     */
    private fun save(isExit: Boolean) {
        if (checkDressIsChanged()) {
            showSaveLoadingProgress("保存装扮中")
            Upload.upload(1, mDressSelfData?.uploadToken, dsmDress.bitmapBase64, "png", false, object : Upload.UploadListener {
                override fun uploadSuccess(vararg url: String) {
                    LogUtil.i("base64上传成功：" + url[0])
                    val params = com.alibaba.fastjson.JSONObject()
                    params["studentId"] = STBaseConstants.userId
                    params["imageUrl"] = url[0]
                    params["imageUC"] = ""
                    val faceUMyFace = com.alibaba.fastjson.JSONObject()
                    val myItem = com.alibaba.fastjson.JSONObject()
                    mDressSelfData?.faceUMyFace?.myItem?.run {
                        if (!TextUtils.isEmpty(eye)) {
                            myItem["eye"] = eye
                        }
                        if (!TextUtils.isEmpty(background)) {
                            myItem["background"] = background
                        }
                        if (!TextUtils.isEmpty(glass)) {
                            myItem["glass"] = glass
                        }
                        if (!TextUtils.isEmpty(hairStyle)) {
                            myItem["hairStyle"] = hairStyle
                        }
                        if (!TextUtils.isEmpty(handShow)) {
                            myItem["handShow"] = handShow
                        }
                        if (!TextUtils.isEmpty(mouth)) {
                            myItem["mouth"] = mouth
                        }
                        if (!TextUtils.isEmpty(nose)) {
                            myItem["nose"] = nose
                        }
                        if (!TextUtils.isEmpty(cloth)) {
                            myItem["cloth"] = cloth
                        }
                    }
                    faceUMyFace["myItem"] = myItem
                    params["faceUMyFace"] = faceUMyFace
                    val paramMap = HashMap<String, String>()
                    paramMap["adornOneselfVO"] = JSON.toJSONString(params)
                    LogUtil.d(JSON.toJSONString(paramMap))
                    GSRequest.startRequest(GSAPI.saveDressSelf, paramMap, object : GSJsonCallback<BaseData>() {
                        override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<BaseData>) {
                            if (activity == null || activity?.isFinishing == true) {
                                return
                            }
                            DialogUtil.getInstance().dismiss()
                            if (result == null) {
                                if (isExit) {
                                    activity?.finish()
                                }
                                return
                            }
                            ToastUtil.showToast("保存装扮成功")
                            updateTempDress()
                            if (isExit) {
                                activity?.finish()
                            }
                        }

                        override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                            if (activity == null || activity?.isFinishing == true) {
                                return
                            }
                            ToastUtil.showToast("保存失败")
                            DialogUtil.getInstance().dismiss()
                            if (isExit) {
                                activity?.finish()
                            }
                        }
                    })
                }

                override fun uploadError() {
                    if (activity == null || activity?.isFinishing == true) {
                        return
                    }
                    ToastUtil.showToast("图片上传失败")
                    DialogUtil.getInstance().dismiss()
                    if (isExit) {
                        activity?.finish()
                    }
                }
            })
        } else {
            ToastUtil.showToast("装扮未修改")
            if (isExit) {
                activity?.finish()
            }
        }
    }

    /**
     * 返回
     */
    private fun goBack() {
        DialogUtil.getInstance().create(activity, R.layout.ui_dressself_unsave)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener) {
                        this.bindListener(onClickListener, R.id.tvUnSaveAndExit, R.id.tvSaveAndExit)
                    }

                    override fun onClick(v: View, dialog: DialogUtil) {
                        super.onClick(v, dialog)
                        when (v.id) {
                            R.id.tvUnSaveAndExit -> {
                                dialog.dismiss()
                                activity?.finish()
                            }
                            R.id.tvSaveAndExit -> {
                                save(true)
                            }
                        }
                    }
                })
    }

    /**
     * 保存装扮进度条
     */
    private fun showSaveLoadingProgress(message: String?) {
        DialogUtil.getInstance().create(activity, R.layout.ui_process_bar)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener) {
                        bindText(R.id.tvContent, message)
                    }
                })
    }

    /**
     * 刷新数据
     */
    fun refreshData(coinCount: String , mDressName: String) {
        if (!isFirstLoad) {
            this.mDressName = mDressName
            this.coinCount = coinCount
            this.initData()
        }
    }

    /**
     * 获取当前对象数据
     * @param position
     * @return
     */
    fun getData(position: Int): DressSelfFaceUMyItemCategory? {
        mDressSelfData?.let {
            return if (position < it.faceUMyItemCategory?.size ?: 0) {
                it.faceUMyItemCategory?.get(position)
            } else null
        }
        return null
    }

    /**
     * 重置
     */
    private fun reset() {
        mFragments?.clear()
        tlDress.removeAllTabs()
        tlDress.clearOnTabSelectedListeners()
        this.vLine.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.btnSave -> save(false)
        }
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {
    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
        if (p0?.customView?.tag == null) {
            return
        }
        val faceUMyItem = mDressSelfData?.faceUMyItemCategory?.get(p0.position)
        val iviIcon = p0.customView?.tag as ImageView
        ImageLoader.setImageViewResource(iviIcon, faceUMyItem?.iconUrl ?: "")
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        if (p0?.customView?.tag == null) {
            return
        }
        mCurrPosition = p0.position
        val faceUMyItem = mDressSelfData?.faceUMyItemCategory?.get(p0.position)
        val iviIcon = p0.customView?.tag as ImageView
        ImageLoader.setImageViewResource(iviIcon, faceUMyItem?.iconLightUrl ?: "")
    }

    fun updateFreeNumber(num: Int) {
        if(num > 0) {
            tvFreeNum.visibility = View.VISIBLE
            tvFreeNum.text = "免费$num" + "次"
        }else {
            tvFreeNum.visibility = View.GONE
        }
    }

    fun onBackPressed(position: Int) {
        if (checkDressIsChanged()) {
            if(0 != position) {
                (activity as? DressCityActivity)?.changePage(0)
                mHandler.postDelayed({
                    this.goBack()
                }, 800)
            }else {
                this.goBack()
            }
        }else {
            activity?.finish()
        }
    }

    override fun onDestroy() {
        if (mFragments != null) {
            mFragments?.clear()
        }
        this.mFragments = null
        this.mDressSelfData = null
        LogUtil.w("页面销毁")
        super.onDestroy()
    }
}