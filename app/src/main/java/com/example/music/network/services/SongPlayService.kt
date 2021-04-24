package com.example.music.network.services

import com.example.music.model.bean.Lrc
import com.example.music.model.bean.SongPlayBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by tk on 2019/9/3
 * 根据歌曲id动态获取播放地址
 */
interface SongPlayService {
    @GET
    fun getUrl(@Url url: String, @Query("id")id:Long):Observable<SongPlayBean>
}