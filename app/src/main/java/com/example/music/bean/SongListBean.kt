package com.example.music.bean

import java.io.Serializable

/**
 * Created by tk on 2019/8/20
 * 歌单bean
 */

data class SongListBean (
    val cat: String,
    val code: Int,
    val more: Boolean,
    val playlists: List<Playlists>,
    val total: Int
): Serializable

/**
 * @param name 歌单名字
 * @param coverImgUrl 歌单图片
 * @param trackCount 歌曲数量
 * @param id 网络歌单id
 * @param creator 创建者
 * @param description 歌单简介
 * @param commentCount 评论数量
 * @param playCount 播放量
 * @param subscribedCount 订阅量
 * @param tags 歌单tag
 */
data class Playlists(
    val adType: Int,
    val alg: String,
    val anonimous: Boolean,
    val cloudTrackCount: Int,
    val commentCount: Int,
    val commentThreadId: String,
    val coverImgId: Long,
    val coverImgId_str: String,
    val coverImgUrl: String,
    val coverStatus: Int,
    val createTime: Long,
    val creator: Creator,
    val description: String,
    val highQuality: Boolean,
    val id: Long,
    val name: String,
    val newImported: Boolean,
    val ordered: Boolean,
    val playCount: Int,
    val privacy: Int,
    val shareCount: Int,
    val specialType: Int,
    val status: Int,
    val subscribed: Any,
    val subscribedCount: Int,
    val subscribers: List<Subscriber>,
    val tags: List<String>,
    val totalDuration: Int,
    val trackCount: Int,
    val trackNumberUpdateTime: Long,
    val trackUpdateTime: Long,
    val tracks: Any,
    val updateTime: Long,
    val userId: Int
)

/**
 * 创建者信息
 */
data class Creator(
    val accountStatus: Int,
    val authStatus: Int,
    val authority: Int,
    val avatarImgId: Long,
    val avatarImgIdStr: String,
    val avatarImgId_str: String,
    val avatarUrl: String,
    val backgroundImgId: Long,
    val backgroundImgIdStr: String,
    val backgroundUrl: String,
    val birthday: Long,
    val city: Int,
    val defaultAvatar: Boolean,
    val description: String,
    val detailDescription: String,
    val djStatus: Int,
    val expertTags: Any,
    val experts: Any,
    val followed: Boolean,
    val gender: Int,
    val mutual: Boolean,
    val nickname: String,
    val province: Int,
    val remarkName: Any,
    val signature: String,
    val userId: Int,
    val userType: Int,
    val vipType: Int
)

/**
 * 订阅者信息
 */
data class Subscriber(
    val accountStatus: Int,
    val authStatus: Int,
    val authority: Int,
    val avatarImgId: Long,
    val avatarImgIdStr: String,
    val avatarImgId_str: String,
    val avatarUrl: String,
    val backgroundImgId: Long,
    val backgroundImgIdStr: String,
    val backgroundUrl: String,
    val birthday: Long,
    val city: Int,
    val defaultAvatar: Boolean,
    val description: String,
    val detailDescription: String,
    val djStatus: Int,
    val expertTags: Any,
    val experts: Any,
    val followed: Boolean,
    val gender: Int,
    val mutual: Boolean,
    val nickname: String,
    val province: Int,
    val remarkName: Any,
    val signature: String,
    val userId: Int,
    val userType: Int,
    val vipType: Int
)

