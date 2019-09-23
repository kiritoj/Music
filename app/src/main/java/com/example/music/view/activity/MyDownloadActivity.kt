package com.example.music.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.example.music.R

class MyDownloadActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_download)
        toolbar.init("已下载")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}
