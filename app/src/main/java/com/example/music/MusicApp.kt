package com.example.music

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import cn.leancloud.AVLogger
import cn.leancloud.AVOSCloud
import cn.leancloud.AVUser
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.litepal.LitePal
import org.litepal.LitePalApplication


/**
 * Created by tk on 2019/8/15
 */
class MusicApp : Application() {
    val APP_ID = "l9OMvWgQ58e8kpo3SV4G1AFv-MdYXbMMI"
    val APP_KEY = "QzQOxu0hQ9dc0093UJgySOrC"
    //国内版
    //val APP_ID = "nmLtmSAsfQWMFwSByifhcukR-gzGzoHsz"
    //val APP_KEY = "V1SIQsUIBLMdMREIWWNvJssh"

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        //开启leancloud调试日志
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG)
        //初始化leancloud
        AVOSCloud.initialize(this, APP_ID, APP_KEY)
        //初始化litepal
        LitePal.initialize(context)
        EventBus.getDefault().register(this)
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        //全局当前对象
        lateinit var currentUser: AVUser
    }

    @Subscribe
    fun getUSer(user: AVUser){
        currentUser = user
    }

    override fun onTerminate() {
        super.onTerminate()
        EventBus.getDefault().unregister(this)
    }

}