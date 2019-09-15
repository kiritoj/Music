package com.example.music.network.services

import com.example.music.model.bean.RelatedMvBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

/**
 * Created by tk on 2019/9/14
 */
interface RelatedMvService {
    @GET("simi/mv")
    fun getRelatedMv(@Query("mvid")id: Long):Observable<RelatedMvBean>
}