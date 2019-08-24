package com.example.music.network.services

import com.example.music.bean.CategoryBean
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by tk on 2019/8/22
 */
interface CategrayService {
    @GET("songList/category")
    fun getAllCateGray(): Observable<CategoryBean>
}