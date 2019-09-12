package com.example.music.model.bean

/**
 * Created by tk on 2019/9/7
 * 排行榜单bean类
 * @param artistToplist 歌手榜
 * @param list 所有榜单
 */
data class TopListBean(
    val artistToplist: ArtistToplist,
    val code: Int,
    val list: List<X>
)

/**
 *
 * @param coverUrl 榜单图片地址
 * @param name 榜单名字
 * @param upateFrequency 榜单更新时间
 */
data class ArtistToplist(
    val coverUrl: String,
    val name: String,
    //val position: Int,
    val upateFrequency: String
    //val updateFrequency: String
)

/**
 * @param ToplistType 不为空是官方榜，否则是普通榜单
 */
data class X(
    val ToplistType: String,
    //val adType: Int,
    //val anonimous: Boolean,
    //val artists: Any,
    //val backgroundCoverId: Int,
    //val backgroundCoverUrl: Any,
    //val cloudTrackCount: Int,
    //val commentThreadId: String,
    //val coverImgId: Long,
    //val coverImgId_str: String,
    val coverImgUrl: String,
    //val createTime: Long,
    val creator: Creator?,
    val description: String,
    //val highQuality: Boolean,
    val id: Long,
    val name: String,
    //val newImported: Boolean,
    //val ordered: Boolean,
    //val playCount: Int,
    //val privacy: Int,
//    val specialType: Int,
//    val status: Int,
//    val subscribed: Any,
//    val subscribedCount: Int,
//    val subscribers: List<Any>,
//    val tags: List<String>,
//    val totalDuration: Int,
//    val trackCount: Int,
//    val trackNumberUpdateTime: Long,
//    val trackUpdateTime: Long,
//    val tracks: Any,
    val updateFrequency: String
//    val updateTime: Long,
//    val userId: Int
)