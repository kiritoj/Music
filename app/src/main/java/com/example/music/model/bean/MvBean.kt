package com.example.music.model.bean

/**
 * Created by tk on 2019/9/13
 */
data class MvBean(
    val `data`: List<MvData>,
    val code: Int

)

data class MvData(
    val artistId: Int,
    val artistName: String,
    //val artists: List<Artist>,
    val briefDesc: String,
    val cover: String,
    //val desc: Any,
    //val duration: Int,
    val id: Long,
    //val mark: Int,
    val name: String,
    val playCount: Int
    //val subed: Boolean,
    //val transNames: List<String>
)

//data class Artist(
//    val id: Int,
//    val name: String
//)