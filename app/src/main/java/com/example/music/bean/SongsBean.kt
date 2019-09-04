package com.example.music.bean

/**
 * Created by tk on 2019/8/23
 */
data class SongsBean(
    val code: Int,
    val playlist: Playlist
    //val privileges: List<Privilege>,
    //val relatedVideos: Any,
    //val urls: Any
)

data class Playlist(
    //val adType: Int,
    //val backgroundCoverId: Long,
    //val backgroundCoverUrl: String,
    //val cloudTrackCount: Int,
    val commentCount: Int,
    //val commentThreadId: String,
    //val coverImgId: Long,
    //val coverImgId_str: String,
    val coverImgUrl: String,
    //val createTime: Long,
    val creator: Creator,
    val description: String,
    //val highQuality: Boolean,
    val id: Long,
    val name: String,
    //val newImported: Boolean,
    //val ordered: Boolean,
    val playCount: Int,
    //val privacy: Int,
    //val shareCount: Int,
    //val specialType: Int,
    //val status: Int,
    //val subscribed: Boolean,
    val subscribedCount: Int,
    //val subscribers: List<Subscriber>,
    val tags: List<String>,
    val trackCount: Int,
    //val trackIds: List<TrackId>,
    //val trackNumberUpdateTime: Long,
    //val trackUpdateTime: Long,
    val tracks: List<Track>,
    //val updateFrequency: String,
    //val updateTime: Long,
    val userId: Int
)

data class TrackId(
    val alg: String,
    val id: Int,
    val v: Int
)



data class Track(
    //val a: Any,
    val al: Al,
    //val alg: String,
    //val alia: List<Any>,
    val ar: List<Ar>,
    //val cd: String,
    //val cf: String,
    //val copyright: Int,
    //val cp: Int,
    //val crbt: String,
    //val djId: Int,
    //val dt: Int,
    //val fee: Int,
    //val ftype: Int,
    //val h: H,
    val id: Long,
    //val l: L,
    //val m: M,
    //val mark: Int,
    //val mst: Int,
    //val mv: Int,
    val name: String
    //val no: Int,
    //val pop: Int,
    //val pst: Int,
    //val publishTime: Long,
    //val rt: String,
    //val rtUrl: Any,
    //val rtUrls: List<Any>,
    //val rtype: Int,
    //val rurl: Any,
    //val s_id: Int,
    //val st: Int,
    //val t: Int,
    //val v: Int
)

data class M(
    val br: Int,
    val fid: Int,
    val size: Int,
    val vd: Int
)

data class Ar(
    //val alias: List<Any>,
    val id: Int,
    val name: String
    //val tns: List<Any>
)

data class L(
    val br: Int,
    val fid: Int,
    val size: Int,
    val vd: Int
)

data class Al(
    val id: Int,
    val name: String,
    val pic: Long,
    val picUrl: String,
    val pic_str: String
    //val tns: List<Any>
)

data class H(
    val br: Int,
    val fid: Int,
    val size: Int,
    val vd: Int
)



