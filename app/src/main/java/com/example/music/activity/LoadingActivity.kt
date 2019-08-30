package com.example.music.activity


import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.music.NetworkUtils
import com.example.music.R
import com.example.music.viewmodel.LoadingVM

/**
 * app启动页
 */
class LoadingActivity : AppCompatActivity() {

    val mViewModel = LoadingVM()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        observe()

        mViewModel.startTiming()

    }

    fun observe(){
        mViewModel.time.observe(this, Observer {
            //检测网络可用，不可用直接跳转至主活动
            if (NetworkUtils.isConnectde()) {
                startActivity(Intent(this, LoginActivity::class.java))
            }else{
                startActivity(Intent(this,MainActivity::class.java))
            }
            finish()
        })
    }


}
