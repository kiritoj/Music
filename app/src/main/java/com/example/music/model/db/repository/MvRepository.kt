package com.example.music.model.db.repository

import android.annotation.SuppressLint
import android.util.Log
import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import cn.leancloud.AVUser
import com.example.music.event.LoadEvent
import com.example.music.event.MvEvent
import com.example.music.event.MvListEvent
import com.example.music.model.bean.MvData
import com.example.music.network.ApiGenerator
import com.example.music.network.services.MvDetailService
import com.example.music.network.services.NewMvService
import com.example.music.network.services.RelatedMvService
import com.example.music.network.services.TopMvService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal

/**
 * Created by tk on 2019/9/13
 */
object MvRepository {
    val TAG = "MvRepository"
    /**
     * 获取网络上的最新mv
     */
    @SuppressLint("CheckResult")
    fun getNewMv(mLimit: Int = 57){
        ApiGenerator.getApiService(NewMvService::class.java)
            .getNewMv(mLimit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                it.data.forEach {
                    it.mvId = it.id
                }
                EventBus.getDefault().post(MvEvent("net","newMv",it.data as ArrayList))
            },{
                EventBus.getDefault().post(LoadEvent(false, "newMv","NewMvFragment"))
                Log.d(TAG,"获取最新mv失败--${it.message}")
            })
    }

    /**
     * 获取排行榜上的mv
     */

    @SuppressLint("CheckResult")
    fun getTopMv(mLimit: Int, offset: Int){
        ApiGenerator.getApiService(TopMvService::class.java)
            .getTopMv(mLimit,offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                it.data.forEach {
                    it.mvId = it.id
                }
                EventBus.getDefault().post(MvEvent("net","topMv",it.data as ArrayList))
            },{
                EventBus.getDefault().post(LoadEvent(false,"topMv","TopMvFragmentVM"))
                Log.d(TAG,"获取mv排行榜失败--${it.message}")
            })
    }

    //获取mv详情
    @SuppressLint("CheckResult")
    fun getMvDetail(id: Long){
        ApiGenerator.getApiService(MvDetailService::class.java)
            .getMvDetail(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                EventBus.getDefault().post(it.data)
            },{
                EventBus.getDefault().post(LoadEvent(false,"","MvDetailVM"))
                Log.d(TAG,"获取mv详情失败--${it.message}")
            })
    }

    //获取相关mv
    @SuppressLint("CheckResult")
    fun getRelatedMv(id: Long){
        ApiGenerator.getApiService(RelatedMvService::class.java)
            .getRelatedMv(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                it.mvs.forEach {
                    it.mvId = it.id
                }
                EventBus.getDefault().post(it)
            },{
                Log.d(TAG,"获取相关mv失败---${it.message}")
            })
    }

    //获取用户收藏的mv
    fun getCollectMv(){
        val list = LitePal.findAll(MvData::class.java) as ArrayList
        if (list.isNullOrEmpty()){
            getUserCollectMvFromHttp()
        }else{
            Log.d(TAG,"从数据库获取用户收藏的歌单")
            EventBus.getDefault().post(MvListEvent(list))
        }
    }

    @SuppressLint("CheckResult")
    fun getUserCollectMvFromHttp(){
        Log.d(TAG,"从网络获取用户收藏的mv")
        val query = AVQuery<AVObject>("Mv")
        query.whereEqualTo("owner", AVUser.getCurrentUser())
        query.findInBackground().subscribe ({

            it.forEach {
                    val objectId = it.objectId
                    val artistId = it.getInt("artistId")
                    val artistName= it.getString("artistName")
                    val briefDesc = it.getString("briefDesc") ?: "暂无简介"
                    val cover = it.getString("cover")
                    val mvId = it.getLong("mvId")
                    val name = it.getString("name")
                    val playCount = it.getInt("playCount")
                val mvData = MvData(artistId,artistName,briefDesc,cover,0,name,playCount)
                mvData.objectId = objectId
                mvData.mvId = mvId
                mvData.save()
                Log.d(TAG,name)
            }
        },{
            Log.d(TAG,"网络拉取用户收藏mv失败：${it.message}")
        })
    }
}