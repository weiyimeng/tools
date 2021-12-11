package gaosi.com.learn.studentapp.dresscity.fragment

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
import com.gsbaselib.base.bean.BaseData
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.TypeValue
import com.gstudentlib.GSAPI
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.base.STBaseFragment
import com.lzy.okgo.model.Response
import gaosi.com.learn.R
import gaosi.com.learn.bean.dress.dressself.DressSelfFaceUMyItemCategory
import gaosi.com.learn.studentapp.dresscity.DressCityActivity
import gaosi.com.learn.studentapp.dresscity.adapter.DressSelfAdapter
import kotlinx.android.synthetic.main.activity_dress_self_fm.*

/**
 * Created by dingyz on 2018/11/30.
 */
class DressSelfFragment : STBaseFragment() {

    private var mDressSelfFaceUMyItemCategory: DressSelfFaceUMyItemCategory? = null
    private var mDressSelfAdapter: DressSelfAdapter? = null
    private var mCurrPosition: Int = 0

    companion object {
        fun instance(): DressSelfFragment {
            return DressSelfFragment()
        }
    }

    fun setCurrPosition(currPosition: Int) {
        this.mCurrPosition = currPosition
    }

    override fun initView(inflater: LayoutInflater?, container: ViewGroup?): View {
        return inflater ?. inflate (R.layout.activity_dress_self_fm, container, false) as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mDressSelfFaceUMyItemCategory = (activity as DressCityActivity).getDressCitySelfFragment().getData(mCurrPosition)
        if (this.mDressSelfFaceUMyItemCategory == null) {
            return
        }
        this.init()
    }

    private fun init() {
        mDressSelfFaceUMyItemCategory?.let {
            if(it.faceUMyItem == null || it.faceUMyItem?.size == 0) {
                tvExtract.visibility = View.VISIBLE
            }else {
                tvExtract.visibility = View.GONE
            }
            mDressSelfAdapter = DressSelfAdapter(it.faceUMyItem, activity)
            mDressSelfAdapter?.setIsCancel(it.defaultCategory == 0) //是否可以点击取消
            mDressSelfAdapter?.setItemClickListener { position ->
                if(it.defaultCategory == 0) {
                    if (position < (it.faceUMyItem?.size?:0) + 1) {
                        if(position == 0) {
                            unsed()
                        }else {
                            used(position - 1)
                        }
                    }
                }else {
                    if (position < (it.faceUMyItem?.size?:0)) {
                        used(position)
                    }
                }
            }
            val gridLayoutManager = GridLayoutManager(activity, 3)
            rvDress.layoutManager = gridLayoutManager
            rvDress.addItemDecoration(LineItemDecoration())
            rvDress.adapter = mDressSelfAdapter
        }
    }

    /**
     * 更换new
     */
    private fun used(position: Int) {
        mDressSelfFaceUMyItemCategory?.faceUMyItem?.let {
            if(it[position].toBeUsed == 0) {
                val paramMap = HashMap<String, String>()
                paramMap["studentId"] = STBaseConstants.userId
                paramMap["itemCode"] = it[position].code?:""
                GSRequest.startRequest(GSAPI.userdDressSelf, paramMap, object : GSJsonCallback<BaseData>() {
                    override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<BaseData>) {
                    }
                    override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                    }
                })
            }else if(it[position].toBeUsed == 2) {
                return
            }
            unsedNotify()
            it[position].toBeUsed = 2
            (activity as DressCityActivity).getDressCitySelfFragment().updateDress(it[position].assistCategory?:"" , it[position].code?:"")
            mDressSelfAdapter?.notifyDataSetChanged()
        }
    }

    /**
     * 取消使用不刷新
     */
    private fun unsedNotify() {
        mDressSelfFaceUMyItemCategory?.faceUMyItem?.let {
            for (i in 0 until it.size) {
                if (it[i].toBeUsed != 0) {
                    it[i].toBeUsed = 1
                }
            }
        }
    }

    /**
     * 取消使用刷新
     */
    private fun unsed() {
        mDressSelfFaceUMyItemCategory?.faceUMyItem?.let {
            for (i in 0 until it.size) {
                if(it[i].toBeUsed == 2) {
                    (activity as DressCityActivity).getDressCitySelfFragment().updateDress(it[i].assistCategory?:"" , "")
                }
                if (it[i].toBeUsed != 0) {
                    it[i].toBeUsed = 1
                }
            }
        }
        mDressSelfAdapter?.notifyDataSetChanged()
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
        private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
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
        private fun drawVertical(c: Canvas, parent: RecyclerView) {
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
