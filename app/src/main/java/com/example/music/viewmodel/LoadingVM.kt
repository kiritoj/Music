package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import android.os.Message

/**
 * Created by tk on 2019/8/15
 */
class LoadingVM {
    var time = MutableLiveData<Boolean>()

    fun startTiming(){
        Handler().sendEmptyMessageDelayed(1,2000)
    }

    inner class Handler : android.os.Handler(){
        override fun handleMessage(msg: Message?) {
            time.value = true
        }
    }
}