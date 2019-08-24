package com.example.music.db.table

import com.example.music.DEFAULT_COVER
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * Created by tk on 2019/8/18
 * @constructor 歌单id,歌单名，歌单封面url,歌曲数量
 * @param isNetSongList 是否为网络歌单,0表示不是，1表示是
 * @param netId 网络歌单的id
 * @param creatorId 创建者id
 * @param creatorAvatar 创建者头像
 * @param creatorName 创建者昵称
 * @param description 歌单形容
 * @param commentNum 评论数
 * @param collectNum 收藏数
 */
class SongList : LitePalSupport(),Serializable {
     var objectId: String? = null
     var name: String? = null
     var coverUrl: String = DEFAULT_COVER
     var num: Int = 0
     var isNetSongList: Int = 1
     var netId: Long? = null
     var creatorId: Int? = null
     var creatorAvatar: String? = null
     var creatorName: String? = null
     var description: String? = null
     var commentNum: Int? = null
     var collectNum: Int? = null
}