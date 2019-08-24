package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.preference.PreferenceManager
import android.util.Log
import com.example.music.MusicApp
import com.example.music.PlayManger
import com.example.music.db.table.LocalMusic
import com.example.music.event.RefreshEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by tk on 2019/8/24
 */
class PlayVM: ViewModel() {

    val TAG = "PlayVM"
    val isPause = MutableLiveData<Boolean>()
    val song = MutableLiveData<LocalMusic>() //当前播放的曲目
    val modeIndex = MutableLiveData<Int>()
    /**
     * 暂停播放
     */
    init {
        EventBus.getDefault().register(this)
        //获取上次结束时的模式(默认顺序播放)
        modeIndex.value = PreferenceManager.getDefaultSharedPreferences(MusicApp.context).getInt("playmode",0)
    }
    fun pause(){
        if (PlayManger.player.isPlaying){
            isPause.value = true
            PlayManger.pause()
        }else{
            isPause.value = false
            PlayManger.resume()
        }
    }

    /**
     * 播放上一曲
     */

    fun playPrevious(){
        PlayManger.playPreVious()
    }
    /**
     * 播放下一曲
     */
    fun playNext(){
        PlayManger.playNext()
    }

    /**
     * 当播放完成后刷新UI
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(event: RefreshEvent){
        song.value = event.song
        isPause.value = false
    }

    /**
     * 拖拽进度条
     */
    fun changeProcess(process: Int){
        PlayManger.changeProgress(process)
    }

    /**
     * 更换播放模式
     */
    fun changeMode(){
        modeIndex.value = (1+ modeIndex.value!!) % 3
        Log.d(TAG,modeIndex.value.toString())
        PlayManger.setMode(modeIndex.value!!)
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }
}