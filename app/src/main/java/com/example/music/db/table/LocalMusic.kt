package com.example.music.db.table

import com.example.music.DEFAULT_COVER
import com.example.music.bean.SongsBean
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * Created by tk on 2019/8/17
 * @constructor 歌曲名，歌手名，长度，路径，专辑ID,mp3Url,所在歌单名,是否为本地歌曲标志符
 */

class LocalMusic : LitePalSupport(),Serializable {


    var id: Long? = null
    var songName: String? = null
    var singerName: String? = null
    var length: Int? = null
    var path: String? = null
    var albumID: Int? = null
    var url: String? = null
    var songListName: String?= null
    var flag: Int = 0
    var coverUrl: String = DEFAULT_COVER
    var objectID: String?=null
}