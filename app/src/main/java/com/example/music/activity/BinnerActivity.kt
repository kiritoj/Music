package com.example.music.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import com.example.music.R
import kotlinx.android.synthetic.main.activity_binner.*

/**
 * 轮播图跳转详情页
 */
class BinnerActivity : AppCompatActivity() {

    val TAG = "BinnerActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binner)
        val url = intent.getStringExtra("url")
        //接口信息有误，部分链接错误
        wv_detail.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
            loadUrl(url)


        }
    }
}
