package com.example.music.db.repository

import android.annotation.SuppressLint
import com.example.music.bean.LastMusicBean
import com.example.music.event.LatestSongEvent
import com.example.music.network.ApiGenerator
import com.example.music.network.services.LatestSongService
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

/**
 * Created by tk on 2019/8/20
 */
object MusicRepository {



    /**
     * 获取最新单曲
     */
    @SuppressLint("CheckResult")
    fun getLatestSong(){
        ApiGenerator
            .getApiService(LatestSongService::class.java)
            .getLatestSong()
            .subscribeOn(Schedulers.io())
            .subscribe {
                EventBus.getDefault().post(LatestSongEvent(it.data as ArrayList))
            }
    }
}