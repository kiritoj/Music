package com.example.music.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.music.R
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        //Glide.with(this).load("http://ww1.sinaimg.cn/large/006nwaiFly1g2lw2ys8r9j31z4140grd.jpg").into(iv_test)
    }
}
