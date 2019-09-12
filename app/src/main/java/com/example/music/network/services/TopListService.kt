package com.example.music.network.services

import com.example.music.model.bean.TopListBean
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by tk on 2019/9/7
 * 获取所有排行榜
 */
interface TopListService {
    @GET("toplist")
    fun getAllTopList(): Observable<TopListBean>
}