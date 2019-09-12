package com.example.music.network.services

import com.example.music.model.bean.CommentBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tk on 2019/9/5
 */
interface SongListCommentService {
    @GET("comment/playlist")
    fun getComment(@Query("id")id:Long
                   ,@Query("limit")limit: Int
                   ,@Query("offset")offset: Int) : Observable<CommentBean>

}