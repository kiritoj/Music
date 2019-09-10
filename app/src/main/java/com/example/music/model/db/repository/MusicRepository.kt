package com.example.music.model.db.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.music.event.LatestSongEvent
import com.example.music.event.LoadEvent
import com.example.music.network.ApiGenerator
import com.example.music.network.services.LatestSongService
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

/**
 * Created by tk on 2019/8/20
 */
object MusicRepository {

    val TAG = "MusicRepository"

    /**
     * 获取最新单曲
     */
    @SuppressLint("CheckResult")
    fun getLatestSong() {
        ApiGenerator
            .getApiService(LatestSongService::class.java)
            .getLatestSong()
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d(TAG,it.data.size.toString())
                EventBus.getDefault().post(LatestSongEvent(it.data as ArrayList))
            }, {
                //加载失败
                EventBus.getDefault().postSticky(LoadEvent(false,"latestSong"))
                Log.d(TAG, "获取最新单曲失败${it.message}")
            })
    }
}