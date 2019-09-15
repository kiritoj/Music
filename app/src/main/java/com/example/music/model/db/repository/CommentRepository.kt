package com.example.music.model.db.repository

import android.annotation.SuppressLint
import android.util.EventLog
import android.util.Log
import com.example.music.network.ApiGenerator
import com.example.music.network.services.MvCommentService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

/**
 * Created by tk on 2019/9/14
 * 评论数据仓库
 */
object CommentRepository {
    val TAG = "CommentRepository"
    //获取mv评论
    @SuppressLint("CheckResult")
    fun getMvComment(id: Long, limit: Int = 20, offset: Int = 0){
        ApiGenerator.getApiService(MvCommentService::class.java)
            .getMvComment(id, limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                EventBus.getDefault().post(it)
            },{
                Log.d(TAG,"获取mv评论失败---${it.message}")
            })
    }
}