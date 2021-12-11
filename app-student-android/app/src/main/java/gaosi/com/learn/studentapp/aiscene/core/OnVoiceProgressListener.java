package gaosi.com.learn.studentapp.aiscene.core;

/**
 * 录音进度View
 * Created by test on 2018/7/9.
 */
public interface OnVoiceProgressListener {
    //倒计时执行中
    void onProgress(float progress);
    //停止执行
    void onStop();
}
