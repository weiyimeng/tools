package gaosi.com.learn.studentapp.pdf

import android.Manifest
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.FileDownloadCallback
import com.gsbaselib.utils.StatusBarUtil
import com.gsbaselib.utils.ToastUtil
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.lzy.okgo.model.Response
import gaosi.com.learn.R
import kotlinx.android.synthetic.main.activity_pdf.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

/**
 * Created by mrz on 2020/1/30.
 */
@Router("PDFViewer")
class PDFActivity : BaseActivity() {

    companion object {
        private const val REQUEST_PERMISSION = 5
    }

    private var path: String? = null
    private var waterMarkType: String? = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN) // 设置全屏
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_pdf)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (EasyPermissions.hasPermissions(this, *perms)) {
                initData()
            } else {
                EasyPermissions.requestPermissions(this, "存储权限申请",
                        REQUEST_PERMISSION, *perms)
            }
        } else {
            initData()
        }
    }

    override fun initView() {
        super.initView()
        pdfView.maxZoom = 5f
        progressBar1.progress = 0
        progressBar1.secondaryProgress = 1
        findViewById<View>(R.id.tv_back).setOnClickListener { finish() }
        STBaseConstants.userInfo?.institutionName
    }

    private fun initData() {
        path = intent.getStringExtra("pdfUrl") ?: ""
        waterMarkType = intent.getStringExtra("waterMarkType") ?: "0"
        path?.let {
            val fileName = it.substring(it.lastIndexOf("/") + 1)
//            val file = File(Environment.getExternalStorageDirectory().absolutePath + FileConvert.DM_TARGET_FOLDER , fileName)
//            if(file.exists() && file.isFile) {
//                loadLocalFile(file.absolutePath)
//            }else {
            loadNetWorkFile(it, fileName)
//            }
        }
    }

    /**
     * 加载网络文件
     */
    private fun loadNetWorkFile(urlString: String, saveName: String?) {
        GSRequest.download(urlString, saveName, object : FileDownloadCallback() {
            override fun onResponseError(response: Response<File>?, p1: Int, p2: String?) {
                ToastUtil.showToast("网络错误")
            }

            override fun onResponseSuccess(response: Response<File>?, p1: Int, file: File?) {
                handler?.post {
                    loadLocalFile(file?.absolutePath)
                }
            }

            override fun onDownloadProcess(progress: Float) {
                handler?.post {
                    progressBar1.max = 100
                    progressBar1.progress = (progress * 100).toInt()
                }
            }
        })
    }

    private fun loadLocalFile(filePath: String?) {
        val file = File(filePath)
        if (!file.exists() || file.length() < 1) { //文件不存在
            findViewById<View>(R.id.ll_error).visibility = View.VISIBLE
            rl_progress.visibility = View.GONE
            return
        }
        if(waterMarkType == "1") {
            wmvView.visibility = View.VISIBLE
        }else {
            wmvView.visibility = View.GONE
        }
        //pdfview初始化并加载文件，
        pdfView.fromFile(file)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                .onLoad {
                }
                .load()
        rl_progress.visibility = View.GONE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (requestCode == REQUEST_PERMISSION && perms.size == 1) {
            initData()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (requestCode == REQUEST_PERMISSION && perms.size == 1) {
            permissonDialog("阅读讲义需要存储权限")
        }
    }

    private fun permissonDialog(msg: String) {
        val builder = AlertDialog.Builder(this)
        val alertDialog = builder.create()
        alertDialog.setTitle("温馨提示")
        alertDialog.setMessage("$msg\n请在手机设置-->权限管理中开启权限!")
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定") { _, _ ->
            alertDialog.dismiss()
            finish()
        }
        alertDialog.show()
        val posButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        posButton.setTextColor(Color.parseColor("#b4b4b4"))
    }

    override fun setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, null)
        StatusBarUtil.setLightMode(this)
    }

}