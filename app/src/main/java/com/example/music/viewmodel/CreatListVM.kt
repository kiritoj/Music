package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.ContentValues
import android.util.Log
import cn.leancloud.AVObject
import com.example.music.db.repository.SongsRepository
import com.example.music.db.table.LocalMusic
import com.example.music.db.table.SongList
import com.example.music.event.RefreshSongList
import com.example.music.event.SongsEvent
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal

/**
 * Created by tk on 2019/8/23
 */
class CreatListVM(val songList: SongList) : ViewModel() {

    val TAG = "CreatListVM"
    val songs = MutableLiveData<ArrayList<LocalMusic>>() //用户创建的歌单里面的歌曲

    init {

        EventBus.getDefault().register(this)
        SongsRepository.getCreatListSongs(songList)
    }

    /**
     * 从歌单中删除
     */
    fun delete(song: LocalMusic){
        //修改数据库
        val value = ContentValues()
        value.put("songListName","")
        LitePal.updateAll(LocalMusic::class.java,value,"songName = ?",song.songName)
        //更新数量
        val mSongList = LitePal.where("objectId = ?",songList.objectId).find(SongList::class.java)[0]
        mSongList.num = mSongList.num-1
        mSongList.save()

        //修改云端数据
        AVObject.createWithoutData("Music", song.objectID).deleteInBackground()
            .subscribeOn(Schedulers.io())
            .subscribe ({
            Log.d(TAG,"删除成功")
        },{
                Log.d(TAG,"删除歌曲失败：${it.message}")
            })

        //更新云端歌单歌曲数量
        AVObject.createWithoutData("SongList", songList.objectId)
            .fetchInBackground().subscribe ({
                it.apply {
                    put("num",mSongList.num)
                    saveInBackground().subscribe ({
                        Log.d(TAG,"云端歌单数量更新成功")
                    },{
                        Log.d(TAG,"云端歌单数量更新失败${it.message}")
                    })

                }
            },{
                Log.d(TAG,"fetchInBackground error:${it.message}")
            })

        //通知MainFragment更新数量
        EventBus.getDefault().post(RefreshSongList("creat",true))


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveLocalSong(event: SongsEvent){
        if (event.tag.equals("creat")){
            songs.value = event.mList
        }
    }



}