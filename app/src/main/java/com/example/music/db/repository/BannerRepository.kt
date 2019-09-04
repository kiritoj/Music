package com.example.music.db.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.music.LOADING_ERROR
import com.example.music.NetworkUtils
import com.example.music.event.BanberEvent
import com.example.music.network.ApiGenerator
import com.example.music.network.services.BannerService
import com.example.music.db.table.BannerTable
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal


/**
 * Created by tk on 2019/8/20
 */
object BannerRepository {
    val TAG = "BannerRepository"
    /**
     * 获取轮播图
     */
    fun getBanners() {
        //有网络拉取最新的轮播图
        if (NetworkUtils.isConnectde()){
            getBannersFromHttp()
        }else{
            getBannersFromDB()
        }
    }


    /**
     * 从数据库获取轮播图
     */
    @SuppressLint("CheckResult")
    fun getBannersFromDB() {

        Observable.create(ObservableOnSubscribe<ArrayList<BannerTable>> {
            val list = LitePal.findAll(BannerTable::class.java) as ArrayList<BannerTable>
            it.onNext(list)
        }).subscribe {
            EventBus.getDefault().post(BanberEvent(it))

        }


    }


    /**
     * 从网络获取轮播图
     */
    @SuppressLint("CheckResult")
    fun getBannersFromHttp()  {

        //清空之前的轮播图
        LitePal.deleteAll(BannerTable::class.java)

        Log.d(TAG,"从网络获取Banners")
        ApiGenerator
            .getApiService(BannerService::class.java)
            .getBanners(1)
            .subscribeOn(Schedulers.io())
            .subscribe ({
                EventBus.getDefault().post(BanberEvent(it.banners as ArrayList<BannerTable>))
                //保存到数据库
                it.banners.forEach {
                    it.save()
                }


            },{
                val bannerList = ArrayList<BannerTable>()
                for (i in 1..3){
                    val banner = BannerTable(imageUrl = LOADING_ERROR)
                    bannerList.add(banner)
                }
                //发送加载失败的图片
                EventBus.getDefault().post(BanberEvent(bannerList))
                Log.d(TAG,it.message)
            })
    }

}