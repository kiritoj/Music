package com.example.music.model.bean

/**
 * Created by tk on 2019/9/14
 * mv详情
 */
data class MvDetailBean(
    val `data`: MvDetailData,
    val bufferPic: String,
    val bufferPicFS: String,
    val code: Int,
    val loadingPic: String,
    val loadingPicFS: String,
    val subed: Boolean
)

data class MvDetailData(
    val artistId: Int,
    val artistName: String,
    //val artists: List<Artist>,
    val briefDesc: String,
    val brs: Brs,
    val commentCount: Int,
    val commentThreadId: String,
    val cover: String,
    val coverId: Long,
    val desc: String,
    val duration: Int,
    val id: Long,
    val isReward: Boolean,
    val likeCount: Int,
    val nType: Int,
    val name: String,
    val playCount: Int,
    val publishTime: String,
    val shareCount: Int,
    val subCount: Int
)

data class Brs(
    val `1080`: String,
    val `240`: String,
    val `480`: String,
    val `720`: String
)

//data class Artist(
//    val id: Int,
//    val name: String
//)