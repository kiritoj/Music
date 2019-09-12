package com.example.music.model.bean

/**
 * Created by tk on 2019/9/12
 * @param lrc 歌词
 * @param tlyric 翻译歌词
 */
data class Lrc(
    val code: Int,
    val klyric: Klyric,
    val lrc: LrcX,
    val lyricUser: LyricUser,
    val qfy: Boolean,
    val sfy: Boolean,
    val sgc: Boolean,
    val tlyric: Tlyric
    //val transUser: TransUser
)

data class LrcX(
    val lyric: String,
    val version: Int
)

data class LyricUser(
    val demand: Int,
    val id: Int,
    val nickname: String,
    val status: Int,
    val uptime: Long,
    val userid: Int
)

data class TransUser(
    val demand: Int,
    val id: Int,
    val nickname: String,
    val status: Int,
    val uptime: Long,
    val userid: Int
)

data class Klyric(
    val lyric: String,
    val version: Int
)

data class Tlyric(
    val lyric: String,
    val version: Int
)