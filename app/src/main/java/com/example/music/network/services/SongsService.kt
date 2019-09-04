package com.example.music.network.services

import com.example.music.bean.SongsBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tk on 2019/8/23
 * 根据歌单id获取里面的歌曲
 */
interface SongsService {
    @GET("playlist/detail")
            /**
             * @param 歌单id
             */
    fun getSongs(@Query("id") id: Long): Observable<SongsBean>
}