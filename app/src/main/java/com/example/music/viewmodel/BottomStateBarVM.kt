package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import com.example.music.PlayManger
import com.example.music.R
import com.example.music.model.db.table.LocalMusic
import com.example.music.event.RefreshEvent
import com.example.music.event.StateEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by tk on 2019/8/25
 * 每个activity底部播放栏的viewmodel
 */
class BottomStateBarVM private constructor(): ViewModel() {
    val TAG = "BottomStateBarVM"

    companion object {
        private var instance: BottomStateBarVM? = null
            get() {
                if (field == null){
                    field = BottomStateBarVM()
                }
                return field
            }
        fun get(): BottomStateBarVM? {
            return instance
        }
    }

    //当前正在播放的曲目
    val song = ObservableField<LocalMusic>()
    //后台音乐播放器
    val player = PlayManger.player
    //播放/暂停图标
    val playIc = ObservableField<Int>()
    //是否显示底部播放栏
    val isDisplay = MutableLiveData<Boolean>()
    //当前播放位置
    val index = MutableLiveData<Int>()

    init {
        EventBus.getDefault().register(this)
        index.value = -1
    }

    /**
     * 检查是否有播放的音乐队列，如果有显示底部播放栏
     * 没有就不显示
     */
    fun  checkMusicPlaying(){
        if (PlayManger.quene.isNullOrEmpty()){
            isDisplay.value = false
        }else{
            song.set(PlayManger.quene[PlayManger.index])
            isDisplay.value = true
            index.value = PlayManger.index

            //更新播放状态
            if (player.isPlaying){
                playIc.set(R.drawable.vector_drawable_play_black)
            }else{
                playIc.set(R.drawable.vector_drawable_pause_black)
            }
        }
    }


    /**
     * 底部播放栏的暂停/播放事件
     */
    fun pause() {
        //正在播放时，点击暂停
        if (player.isPlaying) {

            player.pause()
            playIc.set(R.drawable.vector_drawable_pause_black)
        } else {

            //反之继续播放
            player.start()
            playIc.set(R.drawable.vector_drawable_play_black)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
            /**
             * 播放其他歌曲时计时刷新UI
             */
    fun refresh(event: RefreshEvent){
        //底部播放栏可见
        isDisplay.value = true

        //更新数据
        song.set(event.mSong)

        index.value = event.position

        playIc.set(R.drawable.vector_drawable_play_black)

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    /**
     * 接收音乐播放状态
     */
    fun receiveState(event: StateEvent){
        Log.d(TAG,"音乐状态变化")
        when(event.state){

            PlayManger.State.PLAY -> playIc.set(R.drawable.vector_drawable_play_black)
            PlayManger.State.PAUSE -> playIc.set(R.drawable.vector_drawable_pause_black)
        }
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

}