package com.example.music.network.services

import com.example.music.model.bean.Lrc
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by tk on 2019/8/28
 * 获取歌词的service
 */
interface LrcService {
    @GET
            /**
             * @param url 新接口的baseurl
             * @param id 歌曲id
             */
    fun getLrc(@Url url: String, @Query("id") id: Long): Observable<Lrc>
}