package com.gstudentlib.base.homework

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.PixelCopy
import android.view.View
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.utils.CameraUtil
import com.gsbaselib.utils.FileUtil
import com.gsbaselib.utils.LOGGER
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gstudentlib.R
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.player.IPlayerView
import com.gstudentlib.player.NavPlayer
import com.gstudentlib.player.OnPlayerStatusListener
import com.gstudentlib.view.SelectPicPopupWindow
import java.io.File
import java.io.FileOutputStream

/**
 * 作者：created by 逢二进一 on 2019/9/16 16:29
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
@SuppressLint("Registered")
open class BaseHomeWorkActivity : BaseActivity(), OnPlayerStatusListener {

    protected val doWrong = "do_wrong"
    protected val doRight = "do_right"
    private val REQUEST_SELECT_CARMER = 34
    private val PERMISSION_REQUEST_CAMERA = 60
    private val PERMISSION_REQUEST_ALBUM = 63

    private var mPicPath = ""
    //图片上传index
    protected var mIndex = 0
    //上传图片列表
    protected var mPathList = ArrayList<String>()

    private val mNavPlayer: IPlayerView by lazy { NavPlayer(0) }

    val selectPic: SelectPicPopupWindow by lazy { SelectPicPopupWindow(this, this) }

    /**
     * 定义回去token回调接口
     */
    protected interface OnTokenListener {
        fun onTokenSuccess()

        fun onTokenFail()
    }

    protected fun gsCapture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getBitmapFromView {
                try {
                    val file = File(FileUtil.getCapturePath())
                    val os = FileOutputStream(file)
                    it.compress(Bitmap.CompressFormat.PNG, 90, os)
                    os.flush()
                    os.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            val dView = window.decorView
            dView.isDrawingCacheEnabled = true
            dView.buildDrawingCache()
            val bitmap = Bitmap.createBitmap(dView.drawingCache)
            dView.destroyDrawingCache()
            if (bitmap != null) {
                try {
                    val file = File(FileUtil.getCapturePath())
                    val os = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, os)
                    os.flush()
                    os.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun getBitmapFromView(callback: (Bitmap) -> Unit) {
        window?.let { window ->
            val view = window.decorView
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val locationOfViewInWindow = IntArray(2)
            view.getLocationInWindow(locationOfViewInWindow)
            try {
                PixelCopy.request(window, Rect(locationOfViewInWindow[0], locationOfViewInWindow[1], locationOfViewInWindow[0] + view.width, locationOfViewInWindow[1] + view.height), bitmap, { copyResult ->
                    if (copyResult == PixelCopy.SUCCESS) {
                        callback(bitmap)
                    }
                }, Handler(Looper.getMainLooper()))
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }


    /**
     * 播放音效
     */
    protected fun play(name: String) {
        mNavPlayer.setOnPlayerStatusListener(this)
        mNavPlayer.reCreate(name)
    }

    /**
     * 上传图片进度条
     */
    protected fun showUploadLoadingProgress(message: String?) {
        DialogUtil.getInstance().create(this, R.layout.ui_process_bar)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener) {
                        bindText(R.id.tvContent, message)
                    }
                })
    }

    /**
     * 打开相机
     */
    protected fun openCamera() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //android 7.0以上需要配置权限file
                    openCamera(REQUEST_SELECT_CARMER)
                } else {
                    val perms = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_CAMERA)
                }
            } else {
                openCamera(REQUEST_SELECT_CARMER)
            }
        } catch (e: Exception) {
            LOGGER.log(e)
        }
    }

    /**
     * 打开图库
     */
    protected fun openAlbum() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    showAlbum()
                } else {
                    val perms = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_ALBUM)
                }
            } else {
                showAlbum()
            }
        } catch (e: Exception) {
            LOGGER.log(e)
        }
    }

    private fun openCamera(requestCode: Int) {
        // if device support camera?
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            //yes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    val tempFile = CameraUtil.createTempFile()
                    mPicPath = tempFile.absolutePath
                    CameraUtil.openCamera(this, requestCode, tempFile)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                mPicPath = FileUtil.getSaveFileDir(mContext) + "/" + "temp.png"
                val uri = Uri.fromFile(File(mPicPath))
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                startActivityForResult(intent, requestCode)
            }
        } else {
            ToastUtil.showToast("您的设备不支持拍照")
        }
    }

    /**
     * 弹出权限dialog
     */
    private fun permissionDialog(content: String) {
        val builder = AlertDialog.Builder(mContext)
        val alertDialog = builder.create()
        alertDialog.setTitle("温馨提示")
        alertDialog.setMessage("$content\n以保证您能正常使用应用。请到“设置→应用权限”中打开。")
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定") { dialog, which -> alertDialog.dismiss() }
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent))
    }

    protected open fun showAlbum() {

    }

    protected open fun upLoad() {

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> if (grantResults.size >= 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCamera(REQUEST_SELECT_CARMER)
            } else {
                permissionDialog("爱学习需要获取相机权限")
            }
            PERMISSION_REQUEST_ALBUM -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum()
            } else {
                permissionDialog("爱学习需要开启读写文件存储权限")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SELECT_CARMER) {
                LogUtil.w("$mPicPath======")
                mPathList.clear()
                mIndex = 0
                mPathList.add(mPicPath)
                upLoad()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mNavPlayer.destroy()
    }

    override fun onPrepared(tag: Int) {
        if (!mNavPlayer.isPlaying()) {
            mNavPlayer.play()
        }
    }

    override fun onCompletion(tag: Int) {
    }
}