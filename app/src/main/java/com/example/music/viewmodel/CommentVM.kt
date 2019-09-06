package com.example.music.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.music.bean.Comment
import com.example.music.network.ApiGenerator
import com.example.music.network.services.MusicCommentService
import com.example.music.network.services.SongListCommentService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by tk on 2019/9/5
 */
class CommentVM {
    val TAG = "CommentVM"
    //是否显示‘暂无歌单’
    val isShowText = MutableLiveData<Boolean>()
    //是否显示加载bar
    val isShowBar = MutableLiveData<Boolean>()
    //是否显示列表
    val isShowList = MutableLiveData<Boolean>()
    //热门评论列表
    val hotConments = MutableLiveData<List<Comment>>()
    //普通评论列表
    var comments = MutableLiveData<List<Comment>>()

    /**
     * 获取评论
     */
    fun getComment(tag: String, id: Long, offset: Int = 0) {

        if (id != (0).toLong()) {
            when (tag) {
                //获取歌曲评论
                "music" -> {
                    //只有网络歌曲才有评论
                    getMusicComment(id, offset)
                }
                //获取歌单评论列表
                "songList" -> {
                    getSongListComment(id, offset)
                }
            }
        } else {
            isShowBar.value = false
            isShowList.value = false
            isShowText.value = true
        }
    }

    //获取歌曲评论
    @SuppressLint("CheckResult")
    fun getMusicComment(id: Long, offset: Int = 0) {
        ApiGenerator.getApiService(MusicCommentService::class.java)
            .getMusicComment(id, 20, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isShowBar.value = false
                isShowList.value = true
                hotConments.value = it.hotComments
                comments.value = it.comments
                if (!it.comments.isNullOrEmpty()){
                    for (i in 0 until it.comments.size){
                        Log.d(TAG,it.comments[i].content)
                    }
                }
            }, {
                Log.d(TAG, "获取音乐评论失败${it.message}")
            })
    }

    //获取歌单评论
    @SuppressLint("CheckResult")
    fun getSongListComment(id: Long, offset: Int = 0){
        ApiGenerator.getApiService(SongListCommentService::class.java)
            .getComment(id,20,offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                isShowBar.value = false
                isShowList.value = true
                hotConments.value = it.hotComments
                comments.value = it.comments
            },{
                Log.d(TAG,"获取歌单评论失败${it.message}")
            })
    }
}