package com.example.music.model.bean

import com.example.music.model.db.table.BannerTable


 data class Banner(
     val banners: List<BannerTable>,
     val code: Int
)

