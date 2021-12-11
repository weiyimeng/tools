package gaosi.com.learn.view

import com.chad.library.adapter.base.loadmore.LoadMoreView
import gaosi.com.learn.R

/**
 * description:
 * created by huangshan on 2020/6/5 下午7:12
 */
class AxxLoadMoreView: LoadMoreView() {

    private var mLoadEndId = R.id.ivLoadEnd

    override fun getLayoutId(): Int {
        return R.layout.ui_load_more
    }

    override fun getLoadingViewId(): Int {
        return R.id.llLoadMore
    }

    override fun getLoadEndViewId(): Int {
        return mLoadEndId
    }

    override fun getLoadFailViewId(): Int {
        return R.id.tvLoadMoreError
    }

    /**
     * 如果返回true，数据全部加载完毕后会隐藏加载更多
     * 如果返回false，数据全部加载完毕后会显示getLoadEndViewId()布局
     */
    override fun isLoadEndGone(): Boolean {
        return true
    }

    /**
     * 设置全部加载完的 resId  0为时不显示
     */
    public fun setLoadEndId(resId: Int) {
        mLoadEndId = resId
    }
}