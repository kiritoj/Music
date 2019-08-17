package com.example.music.bean

import org.litepal.crud.LitePalSupport

/**
 * Created by tk on 2019/8/17
 * @constructor 歌曲名，歌手名，长度，路径，专辑ID
 */

 class LocalMusic : LitePalSupport(){
    var songName: String? = null
    var singerName: String? = null
    var length: Int? = null
    var path: String? =null
    var albumID:Int? = null
    override fun toString(): String {
        return "$songName,$singerName"
    }
}