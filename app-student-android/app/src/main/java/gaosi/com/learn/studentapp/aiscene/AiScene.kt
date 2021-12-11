package gaosi.com.learn.studentapp.aiscene

/**
 * 作者：created by 逢二进一 on 2019/8/5 11:26
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class AiScene {

    /**
     * 录音状态
     */
    class RECORD_STATUS{
        companion object{
            const val NONE = 0X00 //不可操作
            const val RECORDING = 0X01 //录音中
            const val STOPED = 0X02 //已经停止录音
            const val TESTING = 0X03 //评测中
        }
    }

}