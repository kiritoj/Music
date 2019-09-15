package com.example.music.network.services

import com.example.music.model.bean.MvDetailBean
import com.example.music.model.bean.MvDetailData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tk on 2019/9/14
 */
interface MvDetailService {
    @GET("mv/detail")
    fun getMvDetail(@Query("mvid")id: Long): Observable<MvDetailBean>
}