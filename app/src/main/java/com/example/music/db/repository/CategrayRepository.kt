package com.example.music.db.repository

import android.preference.PreferenceManager
import android.util.Log
import com.example.music.MusicApp
import com.example.music.db.table.CatTable
import com.example.music.network.ApiGenerator
import com.example.music.network.services.CategrayService
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal

/**
 * Created by tk on 2019/8/22
 */
object CategrayRepository {
    val LUNGUAGE = 0
    val STYLE = 1
    val PLACE = 2
    val EMOTION = 3
    val THEME = 4
    val TAG = "CategrayRepository"
    /**
     * 获取歌单分类
     */
    fun getCategray(){
        if (PreferenceManager.getDefaultSharedPreferences(MusicApp.context).getBoolean("hasLoadCategray",false)){
            //从数据库获取
            getCategrayFromDB()
        }else{
            //从网络获取
            getCategrayFromHttp()
        }
    }

    fun getCategrayFromDB(){
        val list = LitePal.findAll(CatTable::class.java)
        EventBus.getDefault().post(list[0])
    }

    fun getCategrayFromHttp(){
        ApiGenerator
            .getApiService(CategrayService::class.java)
            .getAllCateGray()
            .subscribeOn(Schedulers.io())
            .subscribe ({
                //取出全部种类
                val cat = CatTable()
                var mLunguageList =  ArrayList<String>() //语种
                var mStyleList = ArrayList<String>() //风格
                var mPlaceList = ArrayList<String>() //场景
                var mEmotionList = ArrayList<String>() //情感
                var mThemeList = ArrayList<String>() //主题
                it.sub.forEach {
                    when(it.category){
                        LUNGUAGE ->  mLunguageList.add(it.name)
                        STYLE -> mStyleList.add(it.name)
                        PLACE -> mPlaceList.add(it.name)
                        EMOTION -> mEmotionList.add(it.name)
                        THEME ->  mThemeList.add(it.name)
                    }
                }
                cat.apply {
                    lunguageList = mLunguageList
                    styleList = mStyleList
                    placeList = mPlaceList
                    emotionList = mEmotionList
                    themeList = mThemeList
                    //保存到数据库
                    save()
                }
                EventBus.getDefault().post(cat)
                Log.d(TAG,"获取歌单信息成功")
            },{
                Log.d(TAG,"获取歌单信息失败：${it.message}")
            })
    }


}