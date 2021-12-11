package com.haoke91.a91edu.ui.homework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.TimeUtils

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
import kotlinx.android.synthetic.main.activity_homework_correctresult.*
import java.text.SimpleDateFormat

import java.util.ArrayList
import java.util.Arrays

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/9 上午11:22
 * 修改人：weiyimeng
 * 修改时间：2018/7/9 上午11:22
 * 修改备注：
 */
class HomeworkResultActivity : BaseActivity() {
    private var mExerciseId: Int = 0 //作业id
    lateinit var mAdapter: LookHomeworkAdapter
    
    private var transferee: Transferee? = null
    
    
    override fun getLayout(): Int {
        return R.layout.activity_homework_correctresult
    }
    
    override fun initialize() {
        mExerciseId = intent.getIntExtra("id", 0)
        transferee = Transferee.getDefault(this)
        mAdapter = LookHomeworkAdapter(this, ArrayList())
        vp_homework!!.adapter = mAdapter
        indicator!!.setViewPager(vp_homework!!.viewPager)
        mAdapter.setOnPagerItemClickListener { position -> openPic(position) }
        netwrokForDetialOfHomeWork(mExerciseId)
    }
    
    fun clickEvent(view: View) {
        when (view.id) {
            R.id.tv_reUpload -> UpLoadHomeworkActivity.start(this@HomeworkResultActivity, mExerciseId)
        }
    }
    
    private fun openPic(position: Int) {
        
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
                Glide.with(this@HomeworkResultActivity).pauseRequests()
            }
            
            override fun onDismiss() {
                Glide.with(this@HomeworkResultActivity).resumeRequests()
            }
        })
        
        
    }
    
    private fun wrapOriginImageViewList(position: String): List<ImageView> {
        val originImgList = ArrayList<ImageView>()
        val imageView = findViewById<ImageView>(R.id.iv_background)
        GlideUtils.load(this, position, imageView)
        for (item in mAdapter.data) {
            originImgList.add(imageView)
        }
        // imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_arrow));
        return originImgList
    }
    
    private fun netwrokForDetialOfHomeWork(exerciseId: Int) {
        val request = LiveSccUserExerciseResultDetailRequest()
        request.userId = UserManager.getInstance().userId.toString() + ""
        request.exerciseResultId = exerciseId
        Api.getInstance().postScc(request, LiveSccUserExerciseResultDetailResponse::class.java, object : ResponseCallback<LiveSccUserExerciseResultDetailResponse>() {
            override fun onResponse(date: LiveSccUserExerciseResultDetailResponse, isFromCache: Boolean) {
                val data = date.data ?: return
                val bean = data.exercise
                if (bean != null) {
                    tv_courseName.text = bean.courseName
                    tv_secondName.text = bean.knowledgeName
                    tv_time.text = "批改时间：${TimeUtils.date2String(bean.latest, SimpleDateFormat("yyyy-MM-dd"))}"
//                    tv_homeworkName.text = bean.name
                    tv_homeworkContent!!.text = bean.content
                    tv_score.text = bean.score!!.toString() + "分"
                    if (data.evaluationGold ?: 0 > 0 || data.evaluationProgress ?: 0 > 0) {
                        tv_homeworkAward.text = "金币  +${data.evaluationGold}   成长值  +${data.evaluationProgress}    继续努力哦"
                        ll_awardArea.visibility = View.VISIBLE
                    } else {
                        ll_awardArea.visibility = View.GONE
                    }
                    if (data.correctUserName.isNullOrEmpty()) {
                        ll_teacherArea.visibility = View.GONE
                    } else {
                        ll_teacherArea.visibility = View.VISIBLE
                        tv_teacherName.text = data.correctUserName + "\n" + data.comment
                        
                    }
                }
                val imgs = data.exerciseResourceDomain
                if (imgs != null && imgs.size > 0) {
                    val urls = imgs.map {
                        //                        String resource = b.getResource();
                        it.resource
                    }
                    mAdapter = LookHomeworkAdapter(this@HomeworkResultActivity, urls)
                    vp_homework!!.adapter = mAdapter
                    indicator!!.setViewPager(vp_homework!!.viewPager)
                    mAdapter.setOnPagerItemClickListener { position -> openPic(position) }
                }
                
            }
        }, "")
    }
    
    companion object {
        
        fun start(context: Context) {
            val intent = Intent(context, HomeworkResultActivity::class.java)
            context.startActivity(intent)
        }
        
        fun start(context: Context, id: Int) {
            val intent = Intent(context, HomeworkResultActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }
}
