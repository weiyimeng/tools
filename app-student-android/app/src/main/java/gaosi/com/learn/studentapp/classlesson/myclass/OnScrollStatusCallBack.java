package gaosi.com.learn.studentapp.classlesson.myclass;

/**
 * Created by test on 2018/5/9.
 * 当列表出现滑动的时候回调此方法
 * 以此判断列表的状态以及触发宝箱球的显示与隐藏
 */
public interface OnScrollStatusCallBack {
    void onCallBack(boolean isFling);
}
