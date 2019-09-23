package com.example.music.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.util.Log
import com.example.music.model.bean.Comment
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
    //是否显示‘暂无评论’
    val isShowText = ObservableField<Boolean>(false)
    //是否显示加载bar
    val isShowBar = ObservableField<Boolean>(true)
    //是否显示列表
    val isShowList = ObservableField<Boolean>(false)
    //是否显示暂无热评
    val isShowNoHot = ObservableField<Boolean>(false)
    //热门评论列表
    val hotConments = MutableLiveData<List<Comment>>()
    //普通评论列表
    var comments = MutableLiveData<List<Comment>>()
    //是否已经成功获取过评论
    var flagN = false
    var flagH = false
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
            isShowBar.set(false)
            isShowList.set(false)
            isShowText.set(true)
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
                isShowBar.set(false)
                if (it.hotComments.isNullOrEmpty() and it.comments.isNullOrEmpty() and !flagN) {
                    isShowList.set(false)
                    isShowText.set(true)
                } else {
                    if (it.hotComments.isNullOrEmpty() and !flagH) {
                        Log.d(TAG, "test")
                        isShowNoHot.set(true)
                    }else{
                        flagH = true
                    }
                    Log.d(TAG, "test1")
                    isShowList.set(true)
                    hotConments.value = it.hotComments
                    comments.value = it.comments
                    flagN = true

                }
            }, {
                Log.d(TAG, "获取音乐评论失败${it.message}")
            })
    }

    //获取歌单评论
    @SuppressLint("CheckResult")
    fun getSongListComment(id: Long, offset: Int = 0) {
        ApiGenerator.getApiService(SongListCommentService::class.java)
            .getComment(id, 20, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isShowBar.set(false)
                if (it.hotComments.isNullOrEmpty() and it.comments.isNullOrEmpty() and !flagN) {
                    isShowList.set(false)
                    isShowText.set(true)
                } else {
                    if (it.hotComments.isNullOrEmpty() and !flagH) {
                        Log.d(TAG, "test")
                        isShowNoHot.set(true)
                    }else{
                        flagH = true
                    }
                    Log.d(TAG, "test1")
                    isShowList.set(true)
                    hotConments.value = it.hotComments
                    comments.value = it.comments
                    flagN = true

                }
            }, {
                Log.d(TAG, "获取歌单评论失败${it.message}")
            })
    }
}