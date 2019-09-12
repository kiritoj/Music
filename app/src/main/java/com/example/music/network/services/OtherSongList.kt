package com.example.music.network.services

import com.example.music.model.bean.SongListBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

/**
 * Created by tk on 2019/8/20
 */
interface OtherSongList {
    @GET("top/playlist")

            /**
             * @param cat 歌单类型
             * @param size 获取条数
             * @param offset 偏移量
             */
    fun getOtherSongList(@Query("limit") size: Int,@Query("cat") cat: String,@Query("offset") offset: Int): Observable<SongListBean>
}