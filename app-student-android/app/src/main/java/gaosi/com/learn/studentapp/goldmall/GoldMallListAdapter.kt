package gaosi.com.learn.studentapp.goldmall

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gsbaselib.utils.glide.ImageLoader
import com.gstudentlib.view.RoundCornerImageView
import gaosi.com.learn.R
import gaosi.com.learn.bean.CoinImage

/**
 * type = 1 商城列表  2可使用列表
 * Created by huangshan on 2019/12/18.
 */
class GoldMallListAdapter(var type: Int) : BaseQuickAdapter<CoinImage, BaseViewHolder>(R.layout.item_avatar_list) {

    override fun convert(helper: BaseViewHolder?, data: CoinImage?) {
        helper?.let {
            val tvAvatarName = it.getView<TextView>(R.id.tvAvatarName)
            val tvUsageTime = it.getView<TextView>(R.id.tvUsageTime)
            val tvPrice = it.getView<TextView>(R.id.tvPrice)
            val ivAlreadyBuy = it.getView<ImageView>(R.id.ivAlreadyBuy)
            val ivCurrentUse = it.getView<ImageView>(R.id.ivCurrentUse)
            val ivAvatar = it.getView<RoundCornerImageView>(R.id.ivAvatar)
            ImageLoader.setImageViewResource(ivAvatar, data?.doucmentUrl
                    ?: "", R.drawable.icon_default_placeholder)
            tvAvatarName.text = data?.documentName
            if (type == 1) {
                if (data?.isCurrentDoc == 1) {
                    ivCurrentUse.visibility = View.VISIBLE
                } else {
                    ivCurrentUse.visibility = View.GONE
                }
            } else {
                val bgCard = it.getView<RelativeLayout>(R.id.bgCard)
                if (data?.selectStatus == true) {
                    ivCurrentUse.visibility = View.GONE
                    bgCard.setBackgroundResource(R.drawable.bg_avatar_list_click)
                } else {
                    ivCurrentUse.visibility = View.GONE
                    bgCard.setBackgroundResource(R.drawable.bg_avatar_list_unclick)
                }
            }

            when (data?.buy) {
                0 -> {
                    tvPrice.text = data?.costCoin.toString() + "/" + data?.usageCycle + "天"
                    tvPrice.visibility = View.VISIBLE
                    tvUsageTime.visibility = View.GONE
                    ivAlreadyBuy.visibility = View.GONE
                }
                1 -> {
                    tvPrice.visibility = View.GONE
                    tvUsageTime.text = "剩余使用天数：" + data?.resumeDay.toString()
                    tvUsageTime.visibility = View.VISIBLE
                    ivAlreadyBuy.setImageResource(R.drawable.icon_already_bought_avatar)
                    ivAlreadyBuy.visibility = View.VISIBLE
                }
                2 -> {
                    tvPrice.visibility = View.GONE
                    tvUsageTime.text = "永久有效"
                    tvUsageTime.visibility = View.VISIBLE
                    ivAlreadyBuy.setImageResource(R.drawable.icon_already_have_avatar)
                    ivAlreadyBuy.visibility = View.VISIBLE
                }
            }
        }
    }
}