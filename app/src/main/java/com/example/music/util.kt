package com.example.music

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.WindowManager

/**
 * Created by tk on 2019/8/16
 * 一些常用小工具
 */

//检查输入null
fun checkStringIsNull(vararg input: String?): Boolean {
    for (i in 0 until  input.size) {
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
 * 降低显示透明度
 */
fun Activity.reduceTransparency(){

    val lp = window.getAttributes()

    lp.alpha = 0.8f //设置透明度
    window.setAttributes(lp)
    window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
}

/**
 * 还原透明度
 */
fun Activity.resetTransparency(){
    val lp1 = window.attributes
    lp1.alpha = 1f
    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    window.attributes = lp1
}

/**
 * 初始化toobar
 */
fun Toolbar.init(title: String,activity: Activity){
    this.title = title

    setNavigationIcon(R.drawable.vector_drawable_back)
    setNavigationOnClickListener {  }
}
