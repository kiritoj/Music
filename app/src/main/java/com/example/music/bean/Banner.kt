package com.example.music.bean

import com.example.music.db.table.BannerTable


 data class Banner(
    val banners: List<BannerTable>,
    val code: Int
)

