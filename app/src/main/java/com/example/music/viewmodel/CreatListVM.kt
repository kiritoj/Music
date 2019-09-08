package com.example.music.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.util.Log
import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import com.example.music.model.db.table.LocalMusic
import com.example.music.model.db.table.SongList
import com.example.music.event.RefreshSongList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal
import java.util.*

/**
 * Created by tk on 2019/8/23
 */
class CreatListVM(val songList: SongList) : ViewModel() {

    val TAG = "CreatListVM"

    /**
     * 从歌单中删除
     */
    @SuppressLint("CheckResult")
    fun delete(song: LocalMusic) {
        //修改数据库
        val mSongList = LitePal.where("name = ?", songList.name)
            .findFirst(SongList::class.java, true)
        mSongList.num--
        mSongList.save()

        val mSong = LitePal.where("songName = ?", song.songName)
            .findFirst(LocalMusic::class.java, true)


        mSong.songLists.remove(mSongList)
        mSong.save()

        /**
         * 删除leancloud上的数据、这里不再通过objectid方式删除，而是通过and条件删除
         * 原因是歌单和歌曲已经在用多对多关系，objectid仍是一对一关系，会引起删除错乱
         */

        val avSongList = AVObject.createWithoutData("SongList", mSongList.objectId)
        val avQuery1 = AVQuery<AVObject>("Music").whereEqualTo("songList", avSongList)
        val avQuery2 = AVQuery<AVObject>("Music").whereEqualTo("name", song.songName)
        val avQuery = AVQuery.and(Arrays.asList(avQuery1, avQuery2))

        //不要直接调用delete，引起ANR，原因未知，先查询出来，再删除
        avQuery.firstInBackground.subscribeOn(Schedulers.io())
            .subscribe({
                it.deleteInBackground().subscribeOn(Schedulers.io())
                    .subscribe({
                    }, {
                        Log.d(TAG, "CreatListVM:删除目标歌曲失败${it.message}")
                    })
            }, {
                Log.d(TAG, "CreatListVM:查找要删除的歌曲失败${it.message}")
            })

        //更新云端歌单歌曲数量
        AVObject.createWithoutData("SongList", songList.objectId)
            .fetchInBackground().subscribe({
                it.apply {
                    put("num", mSongList.num)
                    saveInBackground()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                        }, {
                            Log.d(TAG, "CreatListVM:云端歌单数量更新失败${it.message}")
                        })

                }
            }, {
                Log.d(TAG, "fetchInBackground error:${it.message}")
            })

        //通知MainFragment更新数量
        EventBus.getDefault().post(RefreshSongList("creat", true))


    }


}