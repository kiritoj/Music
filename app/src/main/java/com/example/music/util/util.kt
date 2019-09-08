package com.example.music.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import android.view.WindowManager
import com.example.music.MusicApp


/**
 * Created by tk on 2019/8/16
 * 一些常用小工具
 */

//检查输入null
fun checkStringIsNull(vararg input: String?): Boolean {
    for (i in 0 until input.size) {
        if (TextUtils.isEmpty(input[i])) {
            return true
        }
    }
    return false
}

/**
 * 获取屏幕宽度
 * @return 屏幕宽度
 */
fun Activity.getScreenWidth() = resources.displayMetrics.widthPixels

/**
 * 获取屏幕高度
 */
fun Activity.getScreeHeight() = resources.displayMetrics.heightPixels


/**
 * 降低显示透明度
 */
fun Activity.reduceTransparency(mAlpha: Float = 0.5f) {

    val lp = window.getAttributes()

    lp.alpha = mAlpha //设置透明度
    window.setAttributes(lp)
    window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
}

/**
 * 还原透明度
 */
fun Activity.resetTransparency() {
    val lp1 = window.attributes
    lp1.alpha = 1f
    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    window.attributes = lp1
}


/**
 * 监测网络可用
 */
object NetworkUtils {
    fun isConnectde(): Boolean {

        val mConnectivityManager = MusicApp.context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNetworkInfo != null) {
            return mNetworkInfo.isConnected
        }
        return false
    }
}




