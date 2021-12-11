package com.gstudentlib.util.rxcheck

/**
 * 作者：created by 逢二进一 on 2019/5/13 14:46
 * 邮箱：dingyuanzheng@gaosiedu.com
 * 用法
 * RxCheckStatusMaster
        .addCheckStatus(RxCheckNetStatus())
        .check(object : IRxCheckStatusListener {
            override fun onCheckPass() {
            }

            override fun onCheckUnPass(iRxCheckStatus: IRxCheckStatus?) {
                iRxCheckStatus?.let {
                    if(it is RxCheckNetStatus) {
                    ToastUtil.showToast("当前无网络，请检查网络后重试！")
                    }
                }
            }
        })
 */
object RxCheckStatusMaster {

    private val mCheckStatus: ArrayList<IRxCheckStatus> = ArrayList()

    /**
     * 添加检查者
     */
    fun addCheckStatus(checkStatus: IRxCheckStatus?): RxCheckStatusMaster {
        checkStatus?.let {
            mCheckStatus.add(it)
        }
        return this
    }

    /**
     * 进行检查
     */
    fun check(iRxCheckStatusListener : IRxCheckStatusListener?) {
        if(mCheckStatus.isEmpty()) {
            iRxCheckStatusListener?.onCheckUnPass(null)
        }else {
            for(i in 0 until mCheckStatus.size) {
                if(!mCheckStatus[i].check()) {
                    iRxCheckStatusListener?.onCheckUnPass(mCheckStatus[i])
                    return
                }
            }
            iRxCheckStatusListener?.onCheckPass()
            mCheckStatus.clear()
        }
    }

}