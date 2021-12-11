package com.haoke91.a91edu.ui.found

import android.content.Context
import android.content.Intent
import android.text.Html
import com.haoke91.a91edu.R
import com.haoke91.a91edu.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_gold_help.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/9/14 15:04
 */
class GoldHelpActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_gold_help
    }

    override fun initialize() {
        toolbar_title.text = ("金币兑换说明")
        toolbar_back.setOnClickListener({ onBackPressed() })
        var content="<html lang='en'>" +
                "<body>" +
                "<div class='mask'>" +
                "    <div class='whatsGold'>" +
                "      <p style='color: #181818;'><span style='text-align:left'>什么是金币和成长值？<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;学生每次参与直播、与老师互动、提交作业等，都将获得相应的“成长值”和“金币”。<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;通过完成相应任务可获得成长值，累计成长值可晋升到相应等级，并可以获得相应等级的荣誉称号，如 “沉睡种子”、“史诗树神”等，最高为7级。<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;金币可以通过完成相应任务及老师发放获得，可用于在金币商城中兑换物品。成长值等级越高，获得的金币数越多哦。</span></p>" +
                "    </div>" +
                "</div>" +
                "</body>" +
                "</html>"
        tvContent.text=Html.fromHtml(content)
    }

    companion object {
        fun start(context: Context) {
            var intent = Intent(context, GoldHelpActivity::class.java)
            context.startActivity(intent)
        }
    }
}
