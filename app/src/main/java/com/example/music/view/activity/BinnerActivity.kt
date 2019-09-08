package com.example.music.view.activity

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.music.R
import kotlinx.android.synthetic.main.activity_binner.*

/**
 * 轮播图跳转详情页
 */
class BinnerActivity : BaseActivity() {

    val TAG = "BinnerActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binner)

        val url = intent.getStringExtra("url")
        toolbar.init("")
        //接口信息有误，部分链接错误
        wv_detail.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                blockNetworkImage = false
                //pluginState = WebSettings.PluginState.ON
                //安卓5.0以上允许混合模式
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
            }
            webViewClient = object : WebViewClient(){

                override fun onPageFinished(view: WebView?, url: String?) {
                    toolbar.title = view?.title
                }
            }

            webChromeClient = object : WebChromeClient(){
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (newProgress == 100){
                        process_bar.visibility = View.GONE
                    }else{
                        process_bar.visibility = View.VISIBLE
                        process_bar.progress = newProgress
                    }
                }
            }

            //这里视频只有声音没有图像

            loadUrl(url)


        }
    }

    /**
     * 取消toolbar右侧的图标
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    /**
     * 按下返回键不退出activity，回到上一个网页
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (wv_detail.canGoBack()){
                wv_detail.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }


}
