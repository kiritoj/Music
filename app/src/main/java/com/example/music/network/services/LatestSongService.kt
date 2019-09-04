package com.example.music.network.services

import com.example.music.bean.LastMusicBean
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by tk on 2019/8/20
 */
interface LatestSongService {
    @GET("/top/song")

    fun getLatestSong() : Observable<LastMusicBean>
}