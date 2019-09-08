package com.example.music.util

import android.text.TextUtils
import android.util.Log
import com.example.music.model.db.bean.LrcBean

/**
 * Created by tk on 2019/8/28
 * 解析歌词的工具类
 */
object LrcUtil {

    /**
     * @param text 歌词文本
     *
     */
    val TAG = "LrcUtil"
    fun analyzeLrc(text: String): ArrayList<LrcBean> {
        val list = ArrayList<LrcBean>()
        //将歌词文本每行分开
        val array = text.split("\n")
        for (i in 0 until array.size) {

            //过滤掉不是以[0x:xx:xxx]时间开头的
            if (array[i].startsWith("[0")) {
                //该段歌词文本，例[02:12.42]身体战栗着，害怕这战争即将结束
                val string = array[i]
                Log.d(TAG,string)
                //该段歌词播出的分钟时间
                val minute = string.substring(string.indexOf("[") + 1, string.indexOf("[") + 3).toInt()
                //秒钟
                val second = string.substring(string.indexOf(":") + 1, string.indexOf(":") + 3).toInt()
                //毫秒
             //   val millisecond = string.substring(string.indexOf(".") + 1, string.indexOf("]")).toInt()

                val mStarTime = minute * 60 * 1000 + second * 1000 //+ millisecond
                var mText = string.substring(string.indexOf("]") + 1)
                //该段没有，可能是伴奏，用"..."代替
                if (TextUtils.isEmpty(mText)) {
                    mText = "..."
                }

                //寻找是否存在和它同一时间的翻译歌词
                var flag = false
                if (list.size>0) {
                    for (j in 0 until list.size) {
                        if (list[j].startTime == mStarTime) {
                            //存在翻译歌词直接添加翻译
                            list[j].text[1] = mText
                            list[j].hasTranslation = true
                            flag = true
                            break
                        }
                    }
                }
                //没有找到就添加本段歌词
                if (!flag) {
                    val lrcBean = LrcBean()
                    lrcBean.startTime = mStarTime
                    lrcBean.text[0] = mText
                    list.add(lrcBean)

                    //结束时间为下一段歌词的开始时间
                    if (list.size > 1){
                        list[list.size-2].endTime = mStarTime
                    }


                }


            }
        }
        if (list.size > 0 ){
            list[list.size-1].endTime = list[list.size-1].startTime+10000
        }
            return list
    }


}