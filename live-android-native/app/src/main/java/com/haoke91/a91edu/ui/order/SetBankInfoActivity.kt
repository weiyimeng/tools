package com.haoke91.a91edu.ui.order

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.gaosiedu.live.sdk.android.api.user.order.returned.create.LiveUserOrderReturnedCreateRequest
import com.haoke91.a91edu.GlobalConfig
import com.haoke91.a91edu.R
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.a91edu.utils.imageloader.MediaLoader
import com.haoke91.a91edu.utils.manager.OssManager
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.haoke91.a91edu.widget.tilibrary.style.progress.ProgressPieIndicator
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import com.yanzhenjie.album.api.widget.Widget
import kotlinx.android.synthetic.main.activity_setbankinfo.*
import kotlinx.android.synthetic.main.activity_setbankinfo.view.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.item_text.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/14 11:06 AM
 * 修改人：weiyimeng
 * 修改时间：2018/11/14 11:06 AM
 * 修改备注：
 * @version
 */
class SetBankInfoActivity : BaseActivity() {
    lateinit var request: LiveUserOrderReturnedCreateRequest
    private var orderId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        request = intent.getSerializableExtra(REQUEST) as LiveUserOrderReturnedCreateRequest
        orderId = intent.getIntExtra("orderId", -1)
        super.onCreate(savedInstanceState)
    }
    
    override fun initialize() {
        iv_front.setOnClickListener(onClickListener)
        iv_contrary.setOnClickListener(onClickListener)
        tv_commit.setOnClickListener(onClickListener)
        tv_cancel.setOnClickListener(onClickListener)
        tv_except_back.text = intent.getStringExtra("money")
    }
    
    val onClickListener = object : NoDoubleClickListener() {
        override fun onDoubleClick(v: View) {
            when (v.id) {
                R.id.iv_front -> choicePic(iv_front)
                R.id.iv_contrary -> choicePic(iv_contrary)
                R.id.tv_commit -> commit()
                R.id.tv_cancel -> finish()
            }
        }
    }
    private var frontPath: String = ""
    private var contraryPath: String = ""
    
    private fun commit() {
        val num = et_bank_card.text.toString().trim()
        if (TextUtils.isEmpty(num)) {
            ToastUtils.showShort("请输入银行卡号")
            return
        }
        val name = et_bank_name.text.toString().trim()
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort("请输入开户行")
            return
            
        }
        if (ObjectUtils.isEmpty(frontPath)) {
            ToastUtils.showShort("请上传身份证正面")
            return
            
        }
        if (ObjectUtils.isEmpty(contraryPath)) {
            ToastUtils.showShort("请上传身份证反面")
            return
        }
        request.bankAddress = name
        request.cardNum = num
        val indicator = ProgressPieIndicator()
        indicator.attach(0, fl_front)
        indicator.attach(1, fl_contrary)
        indicator.onStart(0)
        val nowString = TimeUtils.getNowString(SimpleDateFormat("yyyy/MM/dd"))
        var objectKey = GlobalConfig.CARD_PATH + nowString + File.separator + UserManager.getInstance().userId + File.separator + orderId + File.separator + System.currentTimeMillis() + "." + ImageUtils.getImageType(frontPath)
        OssManager.getInstance().uploadFile(frontPath, objectKey, object : OssManager.ResponseCallback<String>() {
            override fun onSuccess(date: String?) {
                request.resourceFront = date
                runOnUiThread {
                    indicator.hideView(0)
                }
                uploadSuccess()
            }
            
            override fun downloadProgress(fraction: Float) {
                runOnUiThread {
                    indicator.onProgress(0, (fraction * 100).toInt())
                }
            }
            
            override fun onError() {
                runOnUiThread {
                    indicator.hideView(0)
                    
                }
            }
        })
        indicator.onStart(1)
        var contraryKey = GlobalConfig.CARD_PATH + nowString + File.separator + UserManager.getInstance().userId + File.separator + orderId + File.separator + System.currentTimeMillis() + "." + ImageUtils.getImageType(contraryPath)
        
        OssManager.getInstance().uploadFile(contraryPath, contraryKey, object : OssManager.ResponseCallback<String>() {
            override fun onSuccess(date: String?) {
                request.resourceReverse = date
                runOnUiThread {
                    indicator.hideView(1)
                }
                uploadSuccess()
            }
            
            override fun downloadProgress(fraction: Float) {
                runOnUiThread {
                    indicator.onProgress(1, (fraction * 100).toInt())
                }
            }
            
            override fun onError() {
                runOnUiThread {
                    indicator.hideView(0)
                }
            }
        })
    }
    
    private var count: Int = 0
    private fun uploadSuccess() {
        count = count.plus(1)
        if (count == 2) {
            setResult(Activity.RESULT_OK, intent.putExtra(REQUEST, request))
            finish()
            return
        }
    }
    
    private fun choicePic(iv: ImageView) {
        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader())
                .setLocale(Locale.getDefault())
                .build())
        Album.image(this)
                .multipleChoice()
                .columnCount(3)
                .selectCount(1)
                .camera(true)
                //  .camera(false)
                //                .checkedList(datas)
                //                .filterMimeType { attributes ->
                //                    //                            Logger.e("===attributes======"+attributes);
                //                    attributes.contains("octet-stream") //过滤gif
                //                }
                .widget(
                        Widget.newDarkBuilder(this)
                                .title("相册")
                                .statusBarColor(Color.parseColor("#666666"))
                                .toolBarColor(Color.parseColor("#666666"))
                                .mediaItemCheckSelector(ContextCompat.getColor(this, R.color.white), ContextCompat.getColor(this, R.color.albumColorPrimaryDark))
                                .bucketItemCheckSelector(ContextCompat.getColor(this, R.color.white), ContextCompat.getColor(this, R.color.albumColorPrimaryDark))
                                .build()
                )
                .onResult { result ->
                    if (!ObjectUtils.isEmpty(result)) {
                        if (iv == iv_front) {
                            frontPath = result[0].path
                        } else {
                            contraryPath = result[0].path
                        }
                        GlideUtils.load(this, result[0].path, iv)
                    }
                }
                .onCancel { }
                .start()
        
    }
    
    override fun getLayout(): Int {
        return R.layout.activity_setbankinfo
    }
    
    companion object {
        val REQUEST = "request"
        
        fun start(context: Activity, request: LiveUserOrderReturnedCreateRequest, orderId: Int, money: String?) {
            val intent = Intent(context, SetBankInfoActivity::class.java)
            intent.putExtra(REQUEST, request)
            intent.putExtra("orderId", orderId)
            intent.putExtra("money", money)
            context.startActivityForResult(intent, 1)
        }
    }
}
