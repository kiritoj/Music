package com.example.music.db.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.music.bean.SongsBean
import com.example.music.db.table.LocalMusic
import com.example.music.db.table.SongList
import com.example.music.event.SongsEvent
import com.example.music.network.ApiGenerator
import com.example.music.network.services.SongsService
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal
import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import cn.leancloud.AVUser


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
     * 获取用户创建的歌单里面的歌曲
     */

    fun getCreatListSongs(songlist: SongList){
        //直接检查数据库里有无歌曲
        val mSongList = LitePal.where("isNetSongList = ? and name = ?","0",songlist.name)
            .findFirst(SongList::class.java,true)
        if (mSongList.songs.isNullOrEmpty()){
            getCreatListFromHttp(songlist)
        }else{
            Log.d(TAG,"从数据库获取用户创建的歌单里的歌曲")
            for (i in 0 until mSongList.songs.size){
                Log.d(TAG,mSongList.songs[i].songName)
            }
            EventBus.getDefault().post(SongsEvent("creat",mList = mSongList.songs))
        }
    }


    /**
     * 从网络获取用户创建歌单里的歌曲
     * @param objectId leancloudc储存的歌单id
     */
    @SuppressLint("CheckResult")
    fun getCreatListFromHttp(songlist: SongList){
        Log.d(TAG,"从网络获取用户创建的歌单里的歌曲")
        //云端的歌单对象
        val AVSongList = AVObject.createWithoutData("SongList", songlist.objectId)
        //本地的歌单对象
        val mSongList = LitePal.where("isNetSongList = ? and name = ?","0",songlist.name)
            .findFirst(SongList::class.java,true)

        val query = AVQuery<AVObject>("Music")
        query.whereEqualTo("songList", AVSongList)
        query.findInBackground().subscribeOn(Schedulers.io())
            .subscribe ({
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
                        songLists.add(mSongList)
                        save()

                    }

                    Log.d(TAG,it.getString("name"))
                }

                //EventBus.getDefault().post(SongsEvent("creat",mList = mSongList.songs))

            },{
                Log.d(TAG,"从网络获取创建歌单歌曲失败：${it.message}")
            })
    }
}