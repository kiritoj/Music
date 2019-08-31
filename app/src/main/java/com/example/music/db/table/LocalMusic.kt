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


    var id: Long? = null //网络音乐的id号
    var songName: String? = null
    var singerName: String? = null
    var length: Int? = null
    var path: String? = null
    var albumID: Int? = null
    var url: String? = null //网络音乐的播放地址
    var songListName: String?= null
    var flag: Int = 0  //是本地音乐还是网络音乐
    var coverUrl: String = DEFAULT_COVER //专辑封面图
    var objectID: String?=null
    var isLocalMusic: Boolean = false

    var songLists = ArrayList<SongList>()  //属于哪个创建的歌单，与歌单是多对多关系



}