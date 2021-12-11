package gaosi.com.learn.studentapp.dresscity.fragment

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gsbaselib.utils.TypeValue
import com.gstudentlib.base.STBaseFragment
import gaosi.com.learn.R
import gaosi.com.learn.bean.dress.FaceUMallItem
import gaosi.com.learn.studentapp.dresscity.DressCityActivity
import gaosi.com.learn.studentapp.dresscity.DressDetailActivity
import gaosi.com.learn.studentapp.dresscity.adapter.DressAdapter
import kotlinx.android.synthetic.main.activity_dress_center_fm.*

class DressCenterFragment: STBaseFragment() {

    private var containerView: ViewGroup? = null
    private var mDressAdapter: DressAdapter? = null
    private var faceUMallItem: FaceUMallItem? = null
    private val EXTRA_FACEUITEM = "faceUItem"
    private val EXTRA_REMARK = "remark"
    private var mCurrPosition: Int = 0

    companion object {
        fun instance(): DressCenterFragment {
            return DressCenterFragment()
        }
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup): View? {
        containerView = inflater.inflate(R.layout.activity_dress_center_fm, container, false) as ViewGroup
        return containerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.faceUMallItem = (activity as DressCityActivity).getDressCityCenterFragment().getData(mCurrPosition)
        init()
        loadFaceUMall()
    }

    private fun init() {
        mDressAdapter = DressAdapter(null, activity)
        mDressAdapter?.setItemClickListener { position ->
            if (position < faceUMallItem?.faceUMyItem?.size ?: 0) {
                val mIntent = Intent(activity, DressDetailActivity::class.java)
                mIntent.putExtra(EXTRA_FACEUITEM, faceUMallItem?.faceUMyItem?.get(position))
                mIntent.putExtra(EXTRA_REMARK, faceUMallItem?.remark)
                startActivity(mIntent)
            }
        }
        val gridLayoutManager = GridLayoutManager(activity, 3)
        rvDress.layoutManager = gridLayoutManager
        rvDress.addItemDecoration(LineItemDecoration())
        rvDress.adapter = mDressAdapter
    }

    /**
     * 加载道具
     */
    private fun loadFaceUMall() {
        if (faceUMallItem != null
                && faceUMallItem?.faceUMyItem != null
                && faceUMallItem?.faceUMyItem?.size ?: 0 > 0) {
            mDressAdapter?.setNewData(faceUMallItem?.faceUMyItem)
        }
    }

    fun setCurrPosition(currPosition: Int) {
        this.mCurrPosition = currPosition
    }

    /**
     * 自定义分割线
     */
    internal inner class LineItemDecoration : RecyclerView.ItemDecoration() {

        private val mPaint: Paint?
        private val mDividerHeight = TypeValue.dp2px(1f)

        init {
            mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mPaint.color = Color.parseColor("#E6E6E6")
            mPaint.style = Paint.Style.FILL
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val spanCount = getSpanCount(parent)
            val childCount = parent.adapter?.itemCount ?: 0
            val itemPosition = parent.getChildAdapterPosition(view)
            when {
                isLastRaw(parent, itemPosition, spanCount, childCount) -> // 如果是最后一行，则不需要绘制底部
                    outRect.set(0, 0, mDividerHeight, 0)
                isLastColum(parent, itemPosition, spanCount, childCount) -> // 如果是最后一列，则不需要绘制右边
                    outRect.set(0, 0, 0, mDividerHeight)
                else -> outRect.set(0, 0, mDividerHeight, mDividerHeight)
            }
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)
            drawHorizontal(c, parent)
            drawVertical(c, parent)
        }

        private fun getSpanCount(parent: RecyclerView): Int {
            // 列数
            var spanCount = -1
            val layoutManager = parent.layoutManager
            if (layoutManager is GridLayoutManager) {
                spanCount = layoutManager.spanCount
            } else if (layoutManager is StaggeredGridLayoutManager) {
                spanCount = layoutManager.spanCount
            }
            return spanCount
        }

        // 绘制水平线
        fun drawHorizontal(c: Canvas, parent: RecyclerView) {
            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val left = child.left - params.leftMargin
                val right = child.right + params.rightMargin + mDividerHeight
                val top = child.bottom + params.bottomMargin
                val bottom = top + mDividerHeight
                if (mPaint != null) {
                    c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
                }
            }
        }

        // 绘制垂直线
        fun drawVertical(c: Canvas, parent: RecyclerView) {
            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.top - params.topMargin
                val bottom = child.bottom + params.bottomMargin
                val left = child.right + params.rightMargin
                val right = left + mDividerHeight
                if (mPaint != null) {
                    c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
                }
            }
        }

        // 判断是否是最后一行
        private fun isLastRaw(parent: RecyclerView, pos: Int, spanCount: Int, childCount: Int): Boolean {
            var childCount = childCount
            val layoutManager = parent.layoutManager
            if (layoutManager is GridLayoutManager) {
                childCount -= childCount % spanCount
                if (pos >= childCount) {// 如果是最后一行，则不需要绘制底部
                    return true
                }
            } else if (layoutManager is StaggeredGridLayoutManager) {
                val orientation = layoutManager.orientation
                // StaggeredGridLayoutManager 且纵向滚动
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    childCount -= childCount % spanCount
                    if (pos >= childCount)
                        return true
                } else {// StaggeredGridLayoutManager 且横向滚动
                    if ((pos + 1) % spanCount == 0) {
                        return true
                    }
                }
            }
            return false
        }

        // 判断是否是最后一列
        private fun isLastColum(parent: RecyclerView, pos: Int, spanCount: Int, childCount: Int): Boolean {
            var childCount = childCount
            val layoutManager = parent.layoutManager
            if (layoutManager is GridLayoutManager) {
                if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                    return true
                }
            } else if (layoutManager is StaggeredGridLayoutManager) {
                val orientation = layoutManager.orientation
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    if ((pos + 1) % spanCount == 0) {
                        return true
                    }
                } else {
                    childCount -= childCount % spanCount
                    if (pos >= childCount)
                        return true
                }
            }
            return false
        }
    }
}
