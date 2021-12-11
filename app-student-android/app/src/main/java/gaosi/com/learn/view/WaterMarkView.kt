package gaosi.com.learn.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.gstudentlib.base.STBaseConstants
import gaosi.com.learn.R

/**
 * 作者：created by 逢二进一 on 2020/6/28 10:44
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class WaterMarkView: LinearLayout {

    private var tvInstitutionName1: TextView? = null
    private var tvInstitutionName2: TextView? = null
    private var tvName1: TextView? = null
    private var tvName2: TextView? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.ui_water_mark, this)
        tvInstitutionName1 = findViewById(R.id.tvInstitutionName1)
        tvInstitutionName2 = findViewById(R.id.tvInstitutionName2)
        tvName1 = findViewById(R.id.tvName1)
        tvName2 = findViewById(R.id.tvName2)
        tvInstitutionName1?.text = STBaseConstants.userInfo?.institutionName
        tvInstitutionName2?.text = STBaseConstants.userInfo?.institutionName
        if(!TextUtils.isEmpty(STBaseConstants.userInfo?.phone)) {
            if(STBaseConstants.userInfo?.phone?.length == 11) {
                tvName1?.text = STBaseConstants.userInfo?.truthName + "${STBaseConstants.userInfo?.phone?.subSequence(7 , 11)}"
                tvName2?.text = STBaseConstants.userInfo?.truthName + "${STBaseConstants.userInfo?.phone?.subSequence(7 , 11)}"
            }else {
                tvName1?.text = STBaseConstants.userInfo?.truthName
                tvName2?.text = STBaseConstants.userInfo?.truthName
            }
        }else {
            tvName1?.text = STBaseConstants.userInfo?.truthName
            tvName2?.text = STBaseConstants.userInfo?.truthName
        }
    }

}