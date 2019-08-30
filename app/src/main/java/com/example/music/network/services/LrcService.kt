package com.example.music.network.services

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tk on 2019/8/28
 * 获取歌词的service
 */
interface LrcService {
    @GET("lrc")
            /**
             * @param id 歌曲id
             */
    fun getLrc(@Query("id") id: Long): Observable<ResponseBody>
}