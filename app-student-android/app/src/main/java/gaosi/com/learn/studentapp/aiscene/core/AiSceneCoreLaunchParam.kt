package gaosi.com.learn.studentapp.aiscene.core

import gaosi.com.learn.studentapp.aiscene.core.bean.AiSceneCoreRequestBean

/**
 * 作者：created by 逢二进一 on 2019/8/5 14:37
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class AiSceneCoreLaunchParam {

    private var mScenesId: String? = null
    private var mAiSceneCoreRequestBean: AiSceneCoreRequestBean? = null
    private var _soundIntensityEnable: Boolean = false

    fun setBranches(aiSceneCoreRequestBean: AiSceneCoreRequestBean?) {
        this.mAiSceneCoreRequestBean = aiSceneCoreRequestBean
    }

    fun setScenesId(scenesId: String?) {
        this.mScenesId = scenesId
    }

    fun getScenesId(): String? {
        return mScenesId
    }

    fun getBranchs(): AiSceneCoreRequestBean? {
        return this.mAiSceneCoreRequestBean
    }

    fun setSoundIntensityEnable(soundIntensityEnable: Boolean) {
        this._soundIntensityEnable= soundIntensityEnable
    }

    fun isSoundIntensityEnable(): Boolean {
        return _soundIntensityEnable
    }

}