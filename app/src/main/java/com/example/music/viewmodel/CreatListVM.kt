package com.example.music.viewmodel

import android.arch.lifecycle.ViewModel
import android.util.Log
import cn.leancloud.AVObject
import com.example.music.db.table.LocalMusic
import com.example.music.db.table.SongList
import com.example.music.event.RefreshSongList
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal

/**
 * Created by tk on 2019/8/23
 */
class CreatListVM(val songList: SongList) : ViewModel() {

    val TAG = "CreatListVM"

    /**
     * 从歌单中删除
     */
    fun delete(song: LocalMusic) {
        //修改数据库
        val mSongList = LitePal.where("name = ?", songList.name)
            .findFirst(SongList::class.java, true)
        mSongList.num--
        mSongList.save()

        val mSong = LitePal.where("songName = ?", song.songName)
            .findFirst(LocalMusic::class.java, true)
        Log.d(TAG,"删除前歌单列表")
        if (mSong.songLists.isNullOrEmpty()){
            Log.d(TAG,"列表为空")
        }else{
            for (i in 0 until mSong.songLists.size){
                Log.d(TAG,mSong.songLists[i].name)
            }
        }

        mSong.songLists.remove(mSongList)
        mSong.save()

        //修改云端数据
        AVObject.createWithoutData("Music", song.objectID).deleteInBackground()
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d(TAG, "删除成功")
            }, {
                Log.d(TAG, "删除歌曲失败：${it.message}")
            })

        //更新云端歌单歌曲数量
        AVObject.createWithoutData("SongList", songList.objectId)
            .fetchInBackground().subscribe({
                it.apply {
                    put("num", mSongList.num)
                    saveInBackground().subscribe({
                        Log.d(TAG, "云端歌单数量更新成功")
                    }, {
                        Log.d(TAG, "云端歌单数量更新失败${it.message}")
                    })

                }
            }, {
                Log.d(TAG, "fetchInBackground error:${it.message}")
            })

        //通知MainFragment更新数量
        EventBus.getDefault().post(RefreshSongList("creat", true))


    }


}