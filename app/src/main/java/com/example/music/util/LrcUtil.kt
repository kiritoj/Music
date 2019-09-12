package com.example.music.util

import android.text.TextUtils
import android.util.Log
import com.example.music.model.bean.LrcBean

/**
 * Created by tk on 2019/8/28
 * 解析歌词的工具类
 */
object LrcUtil {


    val TAG = "LrcUtil"
    /**
     * @param text 歌词文本
     * @param trans 翻译歌词
     *
     */
    fun analyzeLrc(text: String?, trans: String?): ArrayList<LrcBean> {
        val list = ArrayList<LrcBean>()
        //将歌词文本每行分开
        val array = text?.split("\n")
        //翻译歌词
        val transArray = trans?.split("\n")
        if (array != null) {
            for (i in 0 until array.size) {

                //过滤掉不是以[0x:xx:xxx]时间开头的
                if (array[i].startsWith("[0")) {
                    //该段歌词文本，例[02:12.42]身体战栗着，害怕这战争即将结束
                    val string = array[i]
                    Log.d(TAG, string)
                    //该段歌词播出的分钟时间
                    val minute = string.substring(1, 3).toInt()
                    //秒钟
                    val second = string.substring(4, 6).toInt()
                    //毫秒
                    val millisecond = string.substring(7, 9).toInt()

                    val mStarTime = minute * 60 * 1000 + second * 1000 + millisecond * 10
                    var mText = string.substring(string.indexOf("]") + 1)
                    //该段没有，可能是伴奏，用"..."代替
                    if (TextUtils.isEmpty(mText)) {
                        mText = "..."
                    }

                    val lrcBean = LrcBean()
                    lrcBean.startTime = mStarTime
                    lrcBean.text[0] = mText
                    list.add(lrcBean)

                    //结束时间为下一段歌词的开始时间
                    if (list.size > 1) {
                        list[list.size - 2].endTime = mStarTime
                    }

                }
            }
            //添加翻译歌词
            if (!TextUtils.isEmpty(trans)){
                transArray?.forEach {
                    if (it.startsWith("[0")){
                        val mstartTime = it.substring(1,3).toInt() * 60 * 1000 + it.substring(4, 6).toInt() * 1000 + it.substring(7, 9).toInt() * 10
                        val mText = it.substring(it.indexOf("]")+1)
                        list.forEach {
                            if (it.startTime == mstartTime){
                                it.hasTranslation = true
                                it.text[1] = mText
                            }
                        }
                    }
                }
            }


        }


        if (list.size > 0) {
            list[list.size - 1].endTime = list[list.size - 1].startTime + 10000
        }
        return list
    }


}

