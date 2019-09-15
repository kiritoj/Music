package com.example.music.network.services

import com.example.music.model.bean.TopMvBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tk on 2019/9/13
 */
interface TopMvService {
    @GET("top/mv")
    fun getTopMv(@Query("limit") limit: Int,@Query("offset") offset: Int): Observable<TopMvBean>
}