package com.example.music.network.services

import com.example.music.model.bean.CommentBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tk on 2019/9/5
 * 获取歌曲评论
 */
interface MusicCommentService {
    @GET("comment/music")
            /**
             * @param id 歌曲id
             * @param limit 获取评论条数
             * @param offset 偏移量
             */
    fun getMusicComment(@Query("id")id:Long,@Query("limit")limit: Int,@Query("offset") offset:Int):Observable<CommentBean>
}