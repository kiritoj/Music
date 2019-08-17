package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.music.MusicApp
import com.example.music.bean.LocalMusic
import com.example.music.repository.LocalMusicRepository
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by tk on 2019/8/17
 */
class LocalMusicVM : ViewModel(){
    var localMusic = MutableLiveData<List<LocalMusic>>()
    val repository = LocalMusicRepository.getInstance()
    var dismissDialog = MutableLiveData<Boolean>()

    init {
        EventBus.getDefault().register(this)
    }
    /**
     * 获取本地音乐
     */
    fun getLocalMusic(){
        repository.getLocalMusic(MusicApp.context)
    }

    /**
     * 通过eventbus拿到后台线程的歌曲
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshmusic(list: List<LocalMusic>){
        localMusic.value = list
        dismissDialog.value = true
    }

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
    }
}