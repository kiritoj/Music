package com.example.music.network.services

import com.example.music.bean.SongListBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

/**
 * Created by tk on 2019/8/20
 */
interface OtherSongList {
    @GET("songList/hot")

            /**
             * @param 歌单类型
             * @param 获取条数
             * @param 分页
             */
    fun getOtherSongList(@Query("cat") cat: String,@Query("pageSize") size: Int,@Query("page") page: Int): Observable<SongListBean>
}