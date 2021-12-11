package com.haoke91.a91edu.ui.homework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gaosiedu.scc.sdk.android.api.user.exercise.result.detail.LiveSccUserExerciseResultDetailRequest
import com.gaosiedu.scc.sdk.android.api.user.exercise.result.detail.LiveSccUserExerciseResultDetailResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.LookHomeworkAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.tilibrary.loader.Glide4ImageLoader
import com.haoke91.a91edu.widget.tilibrary.transfer.TransferConfig
import com.haoke91.a91edu.widget.tilibrary.transfer.Transferee
import com.haoke91.a91edu.widget.tilibrary.view.indicator.NumberIndicator
import com.tmall.ultraviewpager.UltraViewPager
import kotlinx.android.synthetic.main.activity_look_homework.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/9 上午11:22
 * 修改人：weiyimeng
 * 修改时间：2018/7/9 上午11:22
 * 修改备注：
 */
class LookHomeworkActivity : BaseActivity() {
    private var vp_homework: UltraViewPager? = null
    private var indicator: NumberIndicator? = null
    private var mExerciseId: Int = 0 //作业id
    private var mtv_courseName: TextView? = null
    private var mtv_secondName: TextView? = null
    private var mtv_time: TextView? = null
    private var mtv_homeworkName: TextView? = null
    private var mtv_homeworkContent: TextView? = null
    private lateinit var mAdapter: LookHomeworkAdapter
    private var mId: Int = 0 //用于提交作业的id
    private val mUrls = StringBuffer()
    
    private var transferee: Transferee? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        //        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
    
    override fun getLayout(): Int {
        return R.layout.activity_look_homework
    }
    
    override fun initialize() {
        mExerciseId = intent.getIntExtra("id", 0)
        transferee = Transferee.getDefault(this)
        vp_homework = findViewById(R.id.vp_homework)
        indicator = findViewById(R.id.indicator)
        mtv_courseName = id(R.id.tv_courseName)
        mtv_secondName = id(R.id.tv_secondName)
        mtv_time = id(R.id.tv_time)
        mtv_homeworkName = id(R.id.tv_homeworkName)
        mtv_homeworkContent = id(R.id.tv_homeworkContent)
        //        List<String> img_urls = Arrays.asList("http://img4.imgtn.bdimg.com/it/u=4230904282,2566685140&fm=27&gp=0.jpg", "http://img2.imgtn.bdimg.com/it/u=1101292676,797053170&fm=27&gp=0.jpg");
        
        mAdapter = LookHomeworkAdapter(this, ArrayList())
        vp_homework!!.adapter = mAdapter
        indicator!!.setViewPager(vp_homework!!.viewPager)
        mAdapter.setOnPagerItemClickListener { position -> openPic(position) }
    }
    
    override fun onResume() {
        super.onResume()
        netwrokForDetialOfHomeWork(mExerciseId)
    }
    
    fun clickEvent(view: View) {
        when (view.id) {
            R.id.tv_reUpload ->
                //                UpLoadHomeworkActivity.Companion.start(LookHomeworkActivity.this, mId, mUrls.toString());
                UpLoadHomeworkActivity.start(this@LookHomeworkActivity, mId, true)
        }
    }
    
    private fun openPic(position: Int) {
        
        //        List<String> img_urls = Arrays.asList("http://img4.imgtn.bdimg.com/it/u=4230904282,2566685140&fm=27&gp=0.jpg", "http://img2.imgtn.bdimg.com/it/u=1101292676,797053170&fm=27&gp=0.jpg");
        val config = TransferConfig.build()
                .setSourceImageList(mAdapter.data)
                .setMissPlaceHolder(R.mipmap.empty_small)
                .setErrorPlaceHolder(R.mipmap.empty_small)
                // .setProgressIndicator(new ProgressBarIndicator())
                //   .setIndexIndicator(new NumberIndexIndicator())
                .setJustLoadHitImage(true)
                .setImageLoader(Glide4ImageLoader.with(applicationContext))
                .create()
        
        config.nowThumbnailIndex = position
        config.originImageList = wrapOriginImageViewList(mAdapter.data[position])
        
        transferee!!.apply(config).show(object : Transferee.OnTransfereeStateChangeListener {
            override fun onShow() {
                Glide.with(this@LookHomeworkActivity).pauseRequests()
            }
            
            override fun onDismiss() {
                Glide.with(this@LookHomeworkActivity).resumeRequests()
            }
        })
        
        
    }
    //    private BaseQuickWithPositionAdapter.OnItemClickListener onItemClickListener = new BaseQuickWithPositionAdapter.OnItemClickListener() {
    //        @Override
    //        public void onItemClick(View view, int position) {
    //            List<String> img_urls = Arrays.asList("http://img4.imgtn.bdimg.com/it/u=4230904282,2566685140&fm=27&gp=0.jpg", "http://img2.imgtn.bdimg.com/it/u=1101292676,797053170&fm=27&gp=0.jpg");
    //            TransferConfig config = TransferConfig.build()
    //                .setSourceImageList(img_urls)
    //                .setMissPlaceHolder(R.mipmap.ic_arrow)
    //                .setErrorPlaceHolder(R.mipmap.ic_arrow)
    //                // .setProgressIndicator(new ProgressBarIndicator())
    //                .setIndexIndicator(new NumberIndexIndicator())
    //                .setJustLoadHitImage(true)
    //                .setImageLoader(Glide4ImageLoader.with(getApplicationContext()))
    //                .create();
    //
    //            config.setNowThumbnailIndex(position);
    //            config.setOriginImageList(wrapOriginImageViewList());
    //
    //            transferee.apply(config).show(new Transferee.OnTransfereeStateChangeListener() {
    //                @Override
    //                public void onShow() {
    //                    Glide.with(LookHomeworkActivity.this).pauseRequests();
    //                }
    //
    //                @Override
    //                public void onDismiss() {
    //                    Glide.with(LookHomeworkActivity.this).resumeRequests();
    //                }
    //            });
    //
    //
    //        }
    //    };
    
    private fun wrapOriginImageViewList(position: String): List<ImageView> {
        val originImgList = arrayListOf<ImageView>()
        val imageView = findViewById<ImageView>(R.id.iv_background)
        GlideUtils.load(this, position, imageView)
        // imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_arrow));
        for (item in mAdapter.data) {
            originImgList.add(imageView)
        }
        return originImgList
    }
    
    private fun netwrokForDetialOfHomeWork(exerciseId: Int) {
        val request = LiveSccUserExerciseResultDetailRequest()
        request.userId = UserManager.getInstance().userId.toString() + ""
        request.exerciseResultId = exerciseId
        Api.getInstance().postScc(request, LiveSccUserExerciseResultDetailResponse::class.java, object : ResponseCallback<LiveSccUserExerciseResultDetailResponse>() {
            override fun onResponse(date: LiveSccUserExerciseResultDetailResponse, isFromCache: Boolean) {
                val data = date.data ?: return
                if (data.allowReSubmit != null && data.allowReSubmit == 1) {
                    tv_reUpload.visibility = View.VISIBLE
                } else {
                    tv_reUpload.visibility = View.GONE
                }
                mId = data.exercise.id
                val bean = data.exercise
                mtv_courseName!!.text = bean.courseName
                mtv_secondName!!.text = bean.knowledgeName
                mtv_time!!.text = "截止时间：" + com.blankj.utilcode.util.TimeUtils.date2String(bean.latest, SimpleDateFormat("yyyy-MM-dd"))
                mtv_homeworkName!!.text = bean.name
                mtv_homeworkContent!!.text = bean.content
                val list = data.exerciseResourceDomain
                if (list != null && list.size > 0) {
                    mUrls.setLength(0)
                    val urls = ArrayList<String>()
                    for (i in list.indices) {
                        urls.add(list[i].resource)
                        if (i == 0) {
                            mUrls.append(list[i].resource)
                        } else {
                            mUrls.append("," + list[i].resource)
                        }
                    }
                    mAdapter = LookHomeworkAdapter(this@LookHomeworkActivity, urls)
                    vp_homework!!.adapter = mAdapter
                    indicator!!.setViewPager(vp_homework!!.viewPager)
                    mAdapter.setOnPagerItemClickListener { position -> openPic(position) }
                }
                
            }
        }, "detail of homework")
    }
    
    companion object {
        
        fun start(context: Context) {
            val intent = Intent(context, LookHomeworkActivity::class.java)
            context.startActivity(intent)
        }
        
        fun start(context: Context, id: Int) {
            val intent = Intent(context, LookHomeworkActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }
}
