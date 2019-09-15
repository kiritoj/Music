package com.example.music.model.bean

/**
 * Created by tk on 2019/9/13
 */
data class TopMvBean(
    val `data`: List<MvData>,
    val code: Int,
    val hasMore: Boolean,
    val updateTime: Long
)

