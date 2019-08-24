package com.example.music.db.table

/**
 * Created by tk on 2019/8/24
 * 存储上一次退出时的播放队列
 */
class LastPlayTable {
    var id: Long? = null
    var songName: String? = null
    var singerName: String? = null
    var path: String? = null
    var albumID: Int? = null
    var url: String? = null
    var flag: Int = 0
    var coverUrl: String? = null
}