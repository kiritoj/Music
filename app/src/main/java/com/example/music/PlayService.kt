package com.example.music

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.music.event.ModeEvent
import com.example.music.event.QueneEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class PlayService : Service() {

    val TAG = "PlayService"
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        EventBus.getDefault().register(this)

    }


    @Subscribe
    fun setQuene(event: QueneEvent){
        Log.d(TAG,"接收到播放列表")
        PlayManger.setPlayQuene(event.list,event.index)
    }
    @Subscribe
    fun setMode(event: ModeEvent){
        PlayManger.setMode(event.mode)
    }
    @Subscribe
    fun setState(event: PlayManger.State){
        PlayManger.setState(event)
    }

    override fun onDestroy() {
        PlayManger.recycle()
        EventBus.getDefault().unregister(this)
    }
}
