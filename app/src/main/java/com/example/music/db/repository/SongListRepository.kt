package com.example.music.db.repository

import android.annotation.SuppressLint
import android.preference.PreferenceManager
import android.util.Log
import com.example.music.MusicApp
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal
import cn.leancloud.AVQuery
import cn.leancloud.AVObject
import cn.leancloud.AVUser
import com.example.music.bean.SongListBean
import com.example.music.db.table.SongList
import com.example.music.event.AssortedSongListEvent
import com.example.music.event.HotSongListLimit
import com.example.music.event.SongListEvent
import com.example.music.network.ApiGenerator
import com.example.music.network.services.OtherSongList


/**
 * Created by tk on 2019/8/18
 * 歌单仓库
 */
class SongListRepository {
    private val TAG = "SongListRepository"

    companion object{
        private val songListRepository by lazy { SongListRepository() }
        fun getInstance(): SongListRepository{
            return  songListRepository
        }
    }

    /**
     * 获取用户创建的歌单信息
     */
    fun getUserSongList(){
        Log.d(TAG,"getUserSongList：开始获取歌单")
        val preference = PreferenceManager.getDefaultSharedPreferences(MusicApp.context)
        val hasGetFromHttp = preference.getBoolean("hasGetFromHttp",false)
        if (hasGetFromHttp){
            getUserSongListFromDB()
        }else{
            getUserSongListFromHttp()
            preference.edit().apply{
                putBoolean("hasGetFromHttp",true)
                apply()
            }
        }
    }

    /**
     * 从数据库获取用户创建的歌单
     */
    fun getUserSongListFromDB() {
        LitePal.getDatabase()
        val disposable = Observable.create(ObservableOnSubscribe<ArrayList<SongList>> {
            val list = LitePal.where("isNetSongList = ?", "0").find(SongList::class.java) as ArrayList
            it.onNext(list)
            Log.d(TAG,"从数据库获取歌单")
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                EventBus.getDefault().post(SongListEvent("creat",it))
            }
    }

    /**
     * 从网络获取用户创建的歌单
     */
    @SuppressLint("CheckResult")
    fun getUserSongListFromHttp(){
        Log.d(TAG,"从网络获取歌单")
        val query = AVQuery<AVObject>("SongList")
        query.whereEqualTo("owner",AVUser.getCurrentUser())
        query.findInBackground().subscribe ({

            //保存到数据库
            val list = ArrayList<SongList>()
            it.forEach {
                val mSongList = SongList()
                mSongList.apply {
                    objectId = it.objectId
                    name = it.getString("name")
                    coverUrl = it.getString("cover")
                    num = it.getInt("num")
                    description = it.getString("description")
                    isNetSongList = 0
                }
                list.add(mSongList)
                mSongList.save()
                Log.d(TAG,"从网络获取歌单"+ list[0].name)
            }
            EventBus.getDefault().post(SongListEvent("creat",list))
        },{
            Log.d(TAG,"网络拉取歌单失败：${it.message}")
        })

    }


    /**
     * 获取用户收藏的歌单信息
     */
    fun getUserCollectSongList(){
        Log.d(TAG,"getUserSongList：开始获取收藏的歌单")
        val preference = PreferenceManager.getDefaultSharedPreferences(MusicApp.context)
        val hasGetFromHttp = preference.getBoolean("hasGetCollectFromHttp",false)
        if (hasGetFromHttp){
            getUserCollectSongListFromDB()
        }else{
            getUserCollectSongListFromHttp()
            preference.edit().apply{
                putBoolean("hasGetCollectFromHttp",true)
                apply()
            }
        }
    }


    /**
     * 从网络获取用户收藏的歌单
     */
    @SuppressLint("CheckResult")
    fun getUserCollectSongListFromHttp(){
        Log.d(TAG,"从网络获取歌单")
        val query = AVQuery<AVObject>("CollectSongList")
        query.whereEqualTo("owner",AVUser.getCurrentUser())
        query.findInBackground().subscribe ({

            //保存到数据库
            val list = ArrayList<SongList>()
            it.forEach {
                val mSongList = SongList()
                mSongList.apply {
                    objectId = it.objectId
                    name = it.getString("name")
                    coverUrl = it.getString("coverUrl")
                    num = it.getInt("num")
                    description = it.getString("description")
                    isNetSongList = 1
                    creatorAvatar = it.getString("creatorAvatar")
                    collectNum = it.getInt("collectNum")
                    netId = it.getLong("netID")
                    creatorName = it.getString("creatorName")
                    commentNum = it.getInt("commentNum")
                    creatorId = it.getInt("creatorId")
                    save()
                }
                list.add(mSongList)
                Log.d(TAG,"从网络获取歌单"+ list[0].name)
            }
            EventBus.getDefault().post(SongListEvent("collect",list))
        },{
            Log.d(TAG,"网络拉取歌单失败：${it.message}")
        })

    }

    /**
     * 从数据库获取用户的歌单
     */
    fun getUserCollectSongListFromDB() {
        LitePal.getDatabase()
        val disposable = Observable.create(ObservableOnSubscribe<ArrayList<SongList>> {
            val list = LitePal.where("isNetSongList = ?", "1").find(SongList::class.java) as ArrayList
            it.onNext(list)
            Log.d(TAG,"从数据库获取歌单")
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                EventBus.getDefault().post(SongListEvent("collect",it))
            }
    }


    /**
     * 获取热门等歌单
     *
     */
    @SuppressLint("CheckResult")
    fun getHotSongList(cat: String="全部", size: Int, page: Int = 0){
        ApiGenerator
            .getApiService(OtherSongList::class.java)
            .getOtherSongList(cat,size,page)
            .subscribeOn(Schedulers.io())
            .subscribe {
                Log.d(TAG,"${it.msg}")
                EventBus.getDefault().post(HotSongListLimit(it.data as ArrayList<SongListBean.DataBean>))
            }
    }

    /**
     * 获取分类歌单
     */
    @SuppressLint("CheckResult")
    fun getAssortedList(cat: String, size: Int, page: Int = 0){
        ApiGenerator
            .getApiService(OtherSongList::class.java)
            .getOtherSongList(cat,size,page)
            .subscribeOn(Schedulers.io())
            .subscribe {
                EventBus.getDefault().post(AssortedSongListEvent(cat,it.data as ArrayList<SongListBean.DataBean>))
            }
    }



}