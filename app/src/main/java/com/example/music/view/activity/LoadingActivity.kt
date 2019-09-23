package com.example.music.view.activity


import android.arch.lifecycle.Observer
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.animation.AnimationUtils
import com.example.music.PlayControlReceiver
import com.example.music.PlayManger
import com.example.music.R
import com.example.music.util.NetworkUtils
import com.example.music.viewmodel.LoadingVM
import kotlinx.android.synthetic.main.activity_loading.*

/**
 * app启动页
 */
class LoadingActivity : AppCompatActivity() {

    val mViewModel = LoadingVM()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        val animation = AnimationUtils.loadAnimation(this,R.anim.icon_music)
        iv_music_note.startAnimation(animation)
        observe()
        mViewModel.startTiming()


    }

    fun observe() {
        mViewModel.time.observe(this, Observer {
            //检测网络可用，不可用直接跳转至主活动
            if (NetworkUtils.isConnectde()) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        })
    }

}


