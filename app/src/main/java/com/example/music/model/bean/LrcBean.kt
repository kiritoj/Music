package com.example.music.model.bean

/**
 * Created by tk on 2019/8/28
 * @constructor 开始时间，结束时间，该段歌词
 */
class LrcBean {
    var startTime: Int = 0
    var endTime: Int = 0

    //text数组第1位是翻译
    val text: Array<String> = Array(2,{""})
    var hasTranslation: Boolean = false


}