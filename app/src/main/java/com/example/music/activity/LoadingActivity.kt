package com.example.music.activity


import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        })
    }


}
