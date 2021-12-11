package com.haoke91.a91edu.ui.homework

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.gaosiedu.scc.sdk.android.api.user.exercise.detail.LiveSccUserExerciseDetailRequest
import com.gaosiedu.scc.sdk.android.api.user.exercise.detail.LiveSccUserExerciseDetailResponse
import com.gaosiedu.scc.sdk.android.api.user.exercise.submit.LiveSccUserExerciseSubmitRequest
import com.gaosiedu.scc.sdk.android.api.user.exercise.submit.LiveSccUserExerciseSubmitResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.UploadHomeworkAdapter
import com.haoke91.a91edu.entities.StorageInfo
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.ui.homework.CorrectExamActivity.MAX_COUNT
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.imageloader.MediaLoader
import com.haoke91.a91edu.utils.manager.OssManager
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.utils.share.UMengAnalytics
import com.haoke91.a91edu.widget.tilibrary.loader.Glide4ImageLoader
import com.haoke91.a91edu.widget.tilibrary.style.index.NumberIndexIndicator
import com.haoke91.a91edu.widget.tilibrary.transfer.TransferConfig
import com.haoke91.a91edu.widget.tilibrary.transfer.Transferee
import com.haoke91.baselibrary.event.MessageItem
import com.haoke91.baselibrary.event.RxBus
import com.haoke91.baselibrary.recycleview.adapter.BaseQuickWithPositionAdapter
import com.orhanobut.logger.Logger
import com.yanzhenjie.album.Action
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.api.widget.Widget
import com.yanzhenjie.album.app.album.data.ThumbnailBuilder
import com.yanzhenjie.album.widget.divider.Api21ItemDivider
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_uploadhomework.*
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/9 上午11:22
 * 修改人：weiyimeng
 * 修改时间：2018/7/9 上午11:22
 * 修改备注：
 */
open class UpLoadHomeworkActivity : BaseActivity() {
    private lateinit var adapter: UploadHomeworkAdapter
    private lateinit var dates: ArrayList<AlbumFile>
    private var mExerciseId: Int = 0 //作业id
    private val mUrls = ArrayList<String>()
    private var imgCount = 0
    private var imgTotal = 0
    
    private var isUpload: Boolean = false
    
    private var transferee: Transferee? = null
    private val onItemClickListener = BaseQuickWithPositionAdapter.OnItemClickListener { view, position ->
        
        var position = position
        val finalPosition: Int
        finalPosition = when (dates.size) {
            MAX_COUNT -> position
            else -> position - 1
        }
        if (view.id == R.id.tvResubmitBtn) {
            if (dates[finalPosition].isDisable) {
                return@OnItemClickListener
            }
            adapter.indicator.onStart(position)
            //            val finalPosition1 = position
            val body = adapter.indicator.getViewByPosition(position).tag as? StorageInfo.BodyBean
            if (ObjectUtils.isEmpty(body)) {
                var path = ArrayList<String>()
                path.add(dates[finalPosition].addDate.toString() + "." + ImageUtils.getImageType(dates[finalPosition].path))
                OssManager.getInstance().getUpLoadConfig(object : OssManager.ResponseCallback<StorageInfo>() {
                    override fun onSuccess(date: StorageInfo) {
                        if (!ObjectUtils.isEmpty(date.body)) {
                            val body = date.body[0]
                            if (!ObjectUtils.isEmpty(body)) {
                                reUpLoad(body!!, finalPosition, position)
                            }
                        }
                    }
                    
                    override fun onError() {
                        dates[finalPosition].isDisable = false
                        adapter.notifyItemChanged(position)
                        adapter.indicator.hideView(position)
                    }
                }, path)
            } else {
                reUpLoad(body!!, finalPosition, position)
            }
            return@OnItemClickListener
        }
        if (position == 0 && dates.size != MAX_COUNT) {
            var permission = PermissionUtils.isGranted(Permission.CAMERA)
            if (!permission) {
                ToastUtils.showShort("请打开相机权限")
                requestCameraPermission()
                return@OnItemClickListener
            }
            
            ReflectUtils.reflect(ThumbnailBuilder::class.java).field("THUMBNAIL_QUALITY", 10)
            Album.initialize(AlbumConfig.newBuilder(this@UpLoadHomeworkActivity)
                    .setAlbumLoader(MediaLoader())
                    .setLocale(Locale.getDefault())
                    .build())
            Album.image(this@UpLoadHomeworkActivity)
                    .multipleChoice()
                    .columnCount(3)
                    .selectCount(MAX_COUNT)
                    .camera(true)
                    //  .camera(false)
                    .checkedList(dates)
                    //                                        .filterMimeType { attributes ->
                    //                                            attributes.contains("octet-stream") //过滤gif
                    //                                        }
                    .widget(
                            Widget.newDarkBuilder(this@UpLoadHomeworkActivity)
                                    .title("相册")
                                    .statusBarColor(Color.parseColor("#666666"))
                                    .toolBarColor(Color.parseColor("#666666"))
                                    .mediaItemCheckSelector(ContextCompat.getColor(this@UpLoadHomeworkActivity, R.color.white), ContextCompat.getColor(this@UpLoadHomeworkActivity, R.color.albumColorPrimaryDark))
                                    .bucketItemCheckSelector(ContextCompat.getColor(this@UpLoadHomeworkActivity, R.color.white), ContextCompat.getColor(this@UpLoadHomeworkActivity, R.color.albumColorPrimaryDark))
                                    .build()
                    )
                    .onResult { result ->
                        dates.clear()
                        for (file in result) {
                            file.isDisable = true
                        }
                        dates.addAll(result)
                        adapter.setData(dates)
                        isUpload = false
                        imgCount = 0
                    }
                    
                    .onCancel { }
                    .start()
            //            Logger.e("ssdsd==" + ReflectUtils.reflect(ThumbnailBuilder::class.java).field("THUMBNAIL_QUALITY").get())
        } else {
            val image = ArrayList<String>()
            for (file in dates) {
                image.add(file.path)
            }
            
            if (dates.size != MAX_COUNT) {
                position--
            }
            //         if (transConfig.getNowThumbnailIndex() >= transConfig.getOriginImageList().size())
            val config = TransferConfig.build()
                    .setSourceImageList(image)
                    .setMissPlaceHolder(R.mipmap.empty_small)
                    .setErrorPlaceHolder(R.mipmap.empty_small)
                    // .setProgressIndicator(new ProgressBarIndicator())
                    .setIndexIndicator(NumberIndexIndicator())
                    .setJustLoadHitImage(true)
                    .setImageLoader(Glide4ImageLoader.with(applicationContext))
                    .create()
            
            config.nowThumbnailIndex = position
            config.originImageList = wrapOriginImageViewList(dates.size)
            
            transferee!!.apply(config).show(object : Transferee.OnTransfereeStateChangeListener {
                override fun onShow() {
                    Glide.with(this@UpLoadHomeworkActivity).pauseRequests()
                }
                
                override fun onDismiss() {
                    Glide.with(this@UpLoadHomeworkActivity).resumeRequests()
                }
            })
        }
    }
    
    
    @SuppressLint("MissingPermission")
    private fun requestCameraPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.CAMERA)
                .rationale { _, _, executor ->
                    executor.execute()
                }
                .onGranted {
                }
                .start()
    }
    
    private fun reUpLoad(body: StorageInfo.BodyBean, finalPosition: Int, position: Int) {
        OssManager.getInstance().upLoad(body, dates[finalPosition].thumbPath, object : OssManager.ResponseCallback<String>() {
            override fun onSuccess(date: String) {
                imgCount++
                dates[finalPosition].isDisable = true
                mUrls.add(date)
                adapter.notifyItemChanged(position)
                adapter.indicator.hideView(position)
                UMengAnalytics.onEvent(this@UpLoadHomeworkActivity, UMengAnalytics.uploadHomework_success)
                
            }
            
            override fun onError() {
                ToastUtils.showShort("重新上传失败")
                dates[finalPosition].isDisable = false
                //                if (dates.size == MAX_COUNT) {
                adapter.notifyItemChanged(position)
                //                } else {
                //                    adapter.notifyItemChanged(finalPosition + 1)
                //                }
                adapter.indicator.hideView(position)
                UMengAnalytics.onEvent(this@UpLoadHomeworkActivity, UMengAnalytics.uploadHomework_fail)
                
                //                                adapter.getIndicator().hideView(finalPosition);
            }
            
            override fun downloadProgress(fraction: Float) {
                adapter.indicator.onProgress(position, fraction.toInt() * 100)
            }
            
        })
    }
    
    
    override fun getLayout(): Int {
        return R.layout.activity_uploadhomework
    }
    
    override fun initialize() {
        mExerciseId = intent.getIntExtra("id", 0)
        dates = ArrayList()
        adapter = UploadHomeworkAdapter(this@UpLoadHomeworkActivity)
        adapter.setData(dates)
        val layoutManager = GridLayoutManager(this@UpLoadHomeworkActivity, 3)
        rv_homework_list.layoutManager = layoutManager
        rv_homework_list.setHasFixedSize(true)
        rv_homework_list.isNestedScrollingEnabled = false
        rv_homework_list.adapter = adapter
        
        //        var callback = SimpleItemTouchHelperCallback(adapter)
        //        var touchHelper = ItemTouchHelper(callback)
        //        touchHelper.attachToRecyclerView(rv_homework_list)
        
        val dividerSize = resources.getDimensionPixelSize(R.dimen.dp_8)
        rv_homework_list.addItemDecoration(Api21ItemDivider(Color.TRANSPARENT, dividerSize, dividerSize))
        adapter.setOnItemClickListener(onItemClickListener)
        transferee = Transferee.getDefault(this)
        getDetailInfoHomeWork(mExerciseId)
        checkChoiceImages(intent)
        
        //        ReflectUtils.reflect(ThumbnailBuilder::class.java).field("THUMBNAIL_SIZE", 180)
        //        Logger.e("sssss===" + ReflectUtils.reflect(ThumbnailBuilder::class.java).field("THUMBNAIL_SIZE").get())
        
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkChoiceImages(intent)
    }
    
    private fun checkChoiceImages(intent: Intent) {
        if (intent.hasExtra("urls")) {
            mUrls.addAll(Arrays.asList(*intent.getStringExtra("urls").split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
        }
        if (dates != null) {
            dates.clear()
        } else {
            dates = ArrayList()
        }
        if (mUrls != null && mUrls.size > 0) {
            for (s in mUrls) {
                val file = AlbumFile()
                file.mediaType = 1
                file.path = s
                file.isDisable = true
                dates.add(file)
            }
            adapter.setData(dates)
        }
    }
    
    fun clickEvent(view: View) {
        when (view.id) {
            R.id.tvCommitBtn -> if (ObjectUtils.isEmpty(dates)) {
                ToastUtils.showShort("请选择要提交的图片")
                return
            } else {
                if (isUpload) {
                    if (imgCount < dates.size) {
                        ToastUtils.showShort("有未提交成功的图片")
                        return
                    }
                    networkForSendUrlToServer(mUrls)
                } else {
                    ossUploadImages()
                }
                
            }
        }
        
    }
    
    private fun ossUploadImages() {
        isUpload = true
        tvCommitBtn.isEnabled = false
        mUrls.clear()
        imgTotal = dates.size
        imgCount = 0
        var path = ArrayList<String>()
        
        for (item in dates) {
            path.add(item.addDate.toString() + "." + ImageUtils.getImageType(item.path))
        }
        OssManager.getInstance().getUpLoadConfig(object : OssManager.ResponseCallback<StorageInfo>() {
            override fun onSuccess(date: StorageInfo) {
                val body = date.body
                for (index in body.indices) {
                    
                    val finalI = if (dates.size == MAX_COUNT) {
                        index
                    } else {
                        index + 1
                    }
                    adapter.indicator.onStart(finalI)
                    //                    if (index >= dates.size) {
                    //                        break
                    //                    }
                    OssManager.getInstance().upLoad(body[index], dates[index].thumbPath, object : OssManager.ResponseCallback<String>() {
                        override fun onSuccess(date: String) {
                            if (isDestroyed) {
                                return
                            }
                            Logger.e("onSuccess" + imgCount)
                            imgCount++
                            mUrls.add(date)
                            
                            adapter.indicator.hideView(finalI)
                            UMengAnalytics.onEvent(this@UpLoadHomeworkActivity, UMengAnalytics.uploadHomework_success)
                            if (imgCount == imgTotal) {
                                //                                dates.get(finalI).setDisable(true);
                                networkForSendUrlToServer(mUrls)
                                //                        adapter.setData(dates);
                            }
                        }
                        
                        override fun onError() {
                            if (isDestroyed) {
                                return
                            }
                            dates[index].isDisable = false
                            adapter.indicator.hideView(finalI)
                            adapter.notifyItemChanged(finalI)
                            tvCommitBtn.isEnabled = true
                            adapter.indicator.getViewByPosition(finalI).tag = body[index]
                            UMengAnalytics.onEvent(this@UpLoadHomeworkActivity, UMengAnalytics.uploadHomework_fail)
                            
                        }
                        
                        override fun downloadProgress(fraction: Float) {
                            adapter.indicator.onProgress(finalI, (fraction * 100).toInt())
                            
                        }
                    })
                }
                
            }
            
            override fun onError() {
                for (item in adapter.all) {
                    item.isDisable = false
                }
                adapter.notifyDataSetChanged()
                tvCommitBtn.isEnabled = true
            }
            
        }, path)
        
    }
    
    
    private fun wrapOriginImageViewList(size: Int): List<ImageView> {
        val originImgList = ArrayList<ImageView>()
        if (dates.size == MAX_COUNT) {
            for (i in 0 until size) {
                val thumImg = (rv_homework_list.getChildAt(i) as? ViewGroup)?.getChildAt(0) as? ImageView
                if (thumImg != null) {
                    originImgList.add(thumImg)
                }
            }
        } else {
            for (i in 1 until size + 1) {
                val thumImg = (rv_homework_list.getChildAt(i) as? ViewGroup)?.getChildAt(0) as? ImageView
                if (thumImg != null) {
                    originImgList.add(thumImg)
                }
            }
        }
        return originImgList
    }
    
    
    private fun getDetailInfoHomeWork(exerciseId: Int) {
        emptyView?.showLoading()
        val request = LiveSccUserExerciseDetailRequest()
        request.userId = UserManager.getInstance().userId.toString()
        request.exerciseId = exerciseId
        Api.getInstance().postScc(request, LiveSccUserExerciseDetailResponse::class.java, object : ResponseCallback<LiveSccUserExerciseDetailResponse>() {
            override fun onResponse(date: LiveSccUserExerciseDetailResponse, isFromCache: Boolean) {
                val data = date.data
                emptyView?.showContent()
                if (data == null) {
                    return
                }
                tv_courseName.text = data.courseName
                tv_secondName.text = data.knowledgeName
                tv_time.text = "截止时间: ${TimeUtils.date2String(TimeUtils.string2Date(data.latest), SimpleDateFormat("yyyy-MM-dd"))}"
                //                tv_homeworkName.text = data.name
                tv_homeworkContent.text = data.content
            }
            
            override fun onEmpty(date: LiveSccUserExerciseDetailResponse, isFromCache: Boolean) {
                super.onEmpty(date, isFromCache)
                emptyView?.showContent()
            }
            
            override fun onError() {
                super.onError()
                emptyView?.showError()
            }
        }, "")
    }
    
    /**
     * 提交到scc
     *
     * @param urls
     */
    private fun networkForSendUrlToServer(urls: List<String>) {
        showLoadingDialog()
        val request = LiveSccUserExerciseSubmitRequest()
        request.userId = UserManager.getInstance().userId
        request.exerciseId = mExerciseId
        request.resourceList = urls
        Api.getInstance().postScc(request, LiveSccUserExerciseSubmitResponse::class.java, object : ResponseCallback<LiveSccUserExerciseSubmitResponse>() {
            override fun onResponse(date: LiveSccUserExerciseSubmitResponse, isFromCache: Boolean) {
                if (isDestroyed) {
                    return
                }
                dismissLoadingDialog()
                ToastUtils.showShort("提交成功")
                tvCommitBtn.isEnabled = true
                if (!this@UpLoadHomeworkActivity.isFinishing) {
                    if (isClosePreActivity) {
                        ActivityUtils.finishToActivity(MyHomeworkActivity::class.java, false, true)
                    } else {
                        finish()
                    }
                }
                RxBus.getIntanceBus().post(MessageItem(MessageItem.REFRESH_HOMEWORK, ""))
            }
            
            override fun onError() {
                if (isDestroyed) {
                    return
                }
                dismissLoadingDialog()
                ToastUtils.showShort("提交失败")
                tvCommitBtn.isEnabled = true
                
            }
        }, "")
    }
    
    companion object {
        val MAX_COUNT = 21
        var isClosePreActivity = false
        
        //    public static void start(Context context){
        //        Intent intent = new Intent(context, UpLoadHomeworkActivity.class);
        //        context.startActivity(intent);
        //
        //    }
        
        fun start(context: Context, id: Int) {
            val intent = Intent(context, UpLoadHomeworkActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
        
        /**
         * contain the uploaded images
         *
         * @param context
         * @param id      homework id
         * @param urls    images
         */
        fun start(context: Context, id: Int, urls: String) {
            val intent = Intent(context, UpLoadHomeworkActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("urls", urls)
            context.startActivity(intent)
        }
        
        fun start(context: Context, id: Int, closePre: Boolean) {
            val intent = Intent(context, UpLoadHomeworkActivity::class.java)
            intent.putExtra("id", id)
            isClosePreActivity = closePre
            context.startActivity(intent)
        }
    }
    
}
