package com.gstudentlib.view.webview;

/**
 * 作者：created by 逢二进一 on 2019/9/16 15:13
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
public interface IHtmlClickListener {

    /**
     * 页面渲染完成
     */
    void onWebviewPageLoadFinish();

    /**
     * 选择题
     * @param jsCallback
     * @param agrs
     */
    void onChoiceQuestionClick(JSCallBack jsCallback , Object... agrs);

    /**
     * 填空题
     * @param jsCallback
     * @param agrs
     */
    void onCompletionClick(JSCallBack jsCallback , Object... agrs);

    /**
     * 判断题
     * @param jsCallback
     * @param agrs
     */
    void onJudgmentClick(JSCallBack jsCallback , Object... agrs);

    /**
     * 主观题
     * @param jsCallback
     * @param agrs
     */
    void onSubjectiveClick(JSCallBack jsCallback , Object... agrs);

    /**
     * 主观题
     * @param jsCallback
     * @param agrs
     */
    void onSelectPicClick(JSCallBack jsCallback , Object... agrs);

    /**
     * 显示大图
     * @param jsCallback
     * @param agr0
     */
    void onShowBigImage(JSCallBack jsCallback , String agr0);

    /**
     * 显示大图  含老师点评状态
     * @param jsCallback
     * @param answerResult
     * @param imgUrl
     */
    void onShowBigImageWithState(JSCallBack jsCallback , Integer answerResult, String imgUrl);

    /**
     * 语音点评修改回调
     * @param jsCallback
     * @param agrs
     */
    void onVoiceCommentsChange(JSCallBack jsCallback , Object... agrs);

}
