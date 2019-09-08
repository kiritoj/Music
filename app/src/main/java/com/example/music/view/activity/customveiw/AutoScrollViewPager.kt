package com.example.music.view.activity.customveiw

import android.content.Context
import android.os.Handler
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import java.util.*


/**
 * Created by tk on 2019/8/19
 *
 * 自定义viewpager实现无限自动轮播图
 */
class AutoScrollViewPager : ViewPager {
    val TAG = "AutoScrollViewPager"
    var mHandler = Handler()

    lateinit var timer: Timer


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    /**
     * 开始翻页
     */
    fun start() {

        Log.d(TAG,"开始翻页")
        timer = Timer(true)

        timer.schedule(object : TimerTask() {
            override fun run() {
                mHandler.post { setCurrentItem(currentItem + 1) }
            }
        }, 3000, 3000)
    }

    /**
     * 停止翻页
     */
    fun stop() {
        Log.d(TAG,"停止翻页")
        timer.cancel()
    }

//    override fun onTouchEvent(ev: MotionEvent?): Boolean {
//
//        //当手指按下，停止轮播
//        when (ev?.action) {
//            MotionEvent.ACTION_DOWN -> {
//                stop()
//
//            }
//            MotionEvent.ACTION_UP -> {start()}
//
//        }
//
//        return super.onTouchEvent(ev)
//    }


}