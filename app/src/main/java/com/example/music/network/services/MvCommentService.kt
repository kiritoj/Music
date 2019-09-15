package com.example.music.network.services

import com.example.music.model.bean.CommentBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tk on 2019/9/14
 */
interface MvCommentService {
    @GET("comment/mv")
    fun getMvComment(@Query("id")id: Long,@Query("limit")limit: Int,@Query("offset")offset:Int) : Observable<CommentBean>
}