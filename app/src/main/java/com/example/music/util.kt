package com.example.music

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import java.text.SimpleDateFormat
import java.util.*


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

/**
 * 时间戳转时间
 * 接口获取到的时间戳是以毫秒为单位
 */
object TimeUtil{
    val TAG = "TimeUtil"
    fun timestampToTime(mTime: Long) : String{
        val mFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        //参数时间对应的年月日
        val time = mFormat.format(Date(mTime))
        Log.d(TAG,time)
        //当前系统时间戳
        val currentStamp = System.currentTimeMillis()
        val currentTime = mFormat.format(Date(currentStamp))
        //当前时间戳与参数时间戳的差值
        val diff = currentStamp/1000 - mTime/1000

        //月份差值
        val monthDiff = diff/(60*60*24*30)
        //日期差值
        val dayDiff = diff/(60*60*24)
        //小时差值
        val hourDiff = diff/(60*60)
        //分钟差值
        val minuteDiff = diff/(60)
        //首先判断年限，如果不是今年的评论，完整地输出年月日：xxxx-xx-xx
        if (time.substring(0,4).toInt() < currentTime.substring(0,4).toInt() ){
            Log.d(TAG,"0")
            return time.substring(0,10)
        }else if(time.substring(5,7).toInt() < currentTime.substring(5,7).toInt()){
            //是今年的评论,但不是当前月份的评论，只输出月份和日 xx-xx
            Log.d(TAG,"1")
            return time.substring(5,10)
        }else if(dayDiff > 5){
            //是当月的评论，但评论日期超过5天，仍然输出月份和日
            Log.d(TAG,"2")
            return  time.substring(5,10)
        }else if (dayDiff > 0){
            //是当月的评论，且在5天之内，输出x天前
            Log.d(TAG,"3")
            return "${dayDiff}天前"
        }else if(time.substring(11,13).toInt() > currentTime.substring(11,13).toInt()){
            //时间间隔不超过一天，仍然可能是昨天
            return "昨天${time.substring(11)}"
        }
        else if (hourDiff > 0){
            //当天的评论，但不是当前小时，输出当天发送时间
            Log.d(TAG,"4")
            return time.substring(11)
        }else if (minuteDiff > 0){
            //当前小时的评论，但不是当前分钟的评论，输出x分钟前
            Log.d(TAG,"5")
            return "${minuteDiff}分钟前"
        }else{
            //实时评论
            Log.d(TAG,"6")
            return "刚刚"
        }

    }
}


