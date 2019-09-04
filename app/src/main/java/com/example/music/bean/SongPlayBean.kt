package com.example.music.bean

/**
 * Created by tk on 2019/9/3
 */
data class SongPlayBean(
    val `data`: List<SongPlayData>,
    val code: Int
)

data class SongPlayData(
    val br: Int,
    val canExtend: Boolean,
    val code: Int,
    val encodeType: String,
    val expi: Int,
    val fee: Int,
    val flag: Int,
    val freeTrialInfo: Any,
    val gain: Int,
    val id: Int,
    val level: String,
    val md5: String,
    val payed: Int,
    val size: Int,
    val type: String,
    val uf: Any,
    val url: String
)