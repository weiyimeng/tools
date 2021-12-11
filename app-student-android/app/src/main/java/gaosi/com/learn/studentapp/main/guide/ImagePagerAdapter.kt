package gaosi.com.learn.studentapp.main.guide

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout

/**
 * 引导adapter
 * Created by huangshan on 2019/3/29.
 */
class ImagePagerAdapter(private val mImages: List<Int>) : PagerAdapter() {

    private var itemClickListener: OnItemClickListener? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(container.context)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.setImageResource(mImages[position])
        container.addView(imageView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        imageView.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }
        return imageView
    }

    override fun getCount(): Int {
        return mImages.size
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 == arg1
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface OnItemClickListener {
        /**
         * 点击回调
         * @param position 当前点击ID
         */
        fun onItemClick(position: Int)
    }

}