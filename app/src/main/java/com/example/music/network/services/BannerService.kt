package com.example.music.network.services

import com.example.music.bean.Banner
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by tk on 2019/8/20
 */
interface BannerService {

    @GET("banner")
    fun getBanners(): Observable<Banner>
}