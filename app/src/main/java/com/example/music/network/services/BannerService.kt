package com.example.music.network.services

import com.example.music.model.bean.Banner
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tk on 2019/8/20
 */
interface BannerService {

    @GET("banner")
            /**
             * @param type 轮播图类型
             * 0: pc
             * 1: android
             * 2: iphone
             * 3: ipad
             */
    fun getBanners(@Query("type") type: Int): Observable<Banner>
}