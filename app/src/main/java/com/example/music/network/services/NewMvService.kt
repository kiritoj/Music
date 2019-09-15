package com.example.music.network.services

import com.example.music.model.bean.MvBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tk on 2019/9/13
 * 获取最新mv
 */
interface NewMvService {
    @GET("mv/first")
    fun getNewMv(@Query("limit") limit: Int): Observable<MvBean>
}