package com.example.music.model.bean

import org.litepal.crud.LitePalSupport
import java.io.Serializable

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
):LitePalSupport(),Serializable{
    var objectId :String? = null
    var mvId: Long = 0 //id属性与liptpal主键id冲突
}

//data class Artist(
//    val id: Int,
//    val name: String
//)