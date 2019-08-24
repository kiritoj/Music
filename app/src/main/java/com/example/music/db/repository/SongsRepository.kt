package com.example.music.db.repository

import android.annotation.SuppressLint
import android.preference.PreferenceManager
import android.util.Log
import com.example.music.MusicApp
import com.example.music.bean.SongsBean
import com.example.music.db.table.BannerTable
import com.example.music.db.table.LocalMusic
import com.example.music.db.table.SongList
import com.example.music.event.BanberEvent
import com.example.music.event.SongEvent
import com.example.music.event.SongsEvent
import com.example.music.network.ApiGenerator
import com.example.music.network.services.SongsService
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal
import cn.leancloud.AVObject
import io.reactivex.disposables.Disposable
import cn.leancloud.AVQuery



/**
 * Created by tk on 2019/8/23
 * 获取歌单里的歌曲
 */
object SongsRepository {
    val TAG = "SongsRepository"

    /**
     * 从网络歌单获取歌曲
     */
    @SuppressLint("CheckResult")
    fun getSongsFromHttp(id: Long){
        ApiGenerator
            .getApiService(SongsService::class.java)
            .getSongs(id)
            .subscribeOn(Schedulers.io())
            .subscribe ({
                EventBus.getDefault().post(SongsEvent("",it.data?.tracks as ArrayList<SongsBean.DataBean.TracksBean>))
                Log.d(TAG,"从网络歌单获取歌曲成功")
            },{
                Log.d(TAG,"从网络歌单获取歌曲失败${it.message}")
            })
    }

    /**
     * 获取创建的歌单里面的歌曲
     */

    fun getCreatListSongs(songlist: SongList){
        val flag = PreferenceManager
            .getDefaultSharedPreferences(MusicApp.context)
            .getBoolean("hasloadcollectlist",false)
        if (flag){
            getCreatListFromDB(songlist.name!!)
        }else{
            songlist.objectId?.let { getCreatListFromHttp(it) }
        }
    }

    /**
     * 从数据库获取歌曲
     */
    fun getCreatListFromDB(name: String){
        Log.d(TAG,"从数据库获取")
        Observable.create(ObservableOnSubscribe<ArrayList<LocalMusic>> {
            val list = LitePal.where("songListName = ?",name).find(LocalMusic::class.java) as ArrayList
            it.onNext(list)
        }).subscribe {
            EventBus.getDefault().post(SongsEvent("creat",mList = it))
        }
    }

    /**
     * 从网络获取歌曲
     */
    @SuppressLint("CheckResult")
    fun getCreatListFromHttp(objectId: String){
        Log.d(TAG,"从旺火获取")
        val mSongList = AVObject.createWithoutData("SongList", objectId)
        val query = AVQuery<AVObject>("Music")
        query.whereEqualTo("songList", mSongList)
        query.findInBackground().subscribeOn(Schedulers.io())
            .subscribe ({
                val list = ArrayList<LocalMusic>()
                //存进数据库
                it.forEach {
                    val music = LocalMusic()
                    music.apply {
                        objectID = it.objectId
                        songName = it.getString("name")
                        singerName = it.getString("singer")
                        coverUrl = it.getString("albumUrl")
                        id = it.getLong("id")
                        url = it.getString("mp3Url")

                    }
                    list.add(music)
                }
                Log.d(TAG,list[0].songName)
                EventBus.getDefault().post(SongsEvent("creat",mList = list))



            },{
                Log.d(TAG,"从网络获取创建歌单歌曲失败：${it.message}")
            })
    }
}