package com.example.music.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import cn.leancloud.AVFile
import cn.leancloud.AVObject
import com.example.music.db.table.LocalMusic
import com.example.music.db.table.SongList
import com.example.music.event.MusicNumEvene
import com.example.music.event.RefreshSongList
import com.example.music.event.SongListEvent
import com.example.music.db.repository.LocalMusicRepository
import com.example.music.db.repository.SongListRepository
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal
import java.io.File


/**
 * Created by tk on 2019/8/17
 */
class LocalMusicVM : ViewModel() {
    val TAG = "LocalMusicVM"
    var localMusic = MutableLiveData<ArrayList<LocalMusic>>()
    val repository = LocalMusicRepository.getInstance()
    var dismissDialog = MutableLiveData<Boolean>()
    var creatSongList = MutableLiveData<ArrayList<SongList>>()//创建的歌单
    val mRepository = SongListRepository.getInstance()//歌单仓库
    val toast = MutableLiveData<String>()
    init {
        EventBus.getDefault().register(this)
    }

    /**
     * 获取本地音乐
     */
    fun getLocalMusic() {
        repository.getLocalMusic()
    }

    /**
     * 重新扫描
     */
    fun reScanning() {
        //清空数据库
        LitePal.deleteAll(LocalMusic::class.java)

        //从本地扫描
        repository.getLocalMusicFromCursor()
    }

    /**
     * 通过eventbus拿到后台线程的歌曲
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshmusic(list: ArrayList<LocalMusic>) {
        localMusic.value = list
        dismissDialog.value = true
    }

    /***
     * 数据库删除某首歌曲
     * @param 要删除的歌曲
     * @param 位置
     * @param 是否删除源文件
     */
    fun deleteMusic(music: LocalMusic, id: Int,flag: Boolean) {

        LitePal.deleteAll(LocalMusic::class.java, "songName = ?", music.songName)
        //更新歌曲数量
        val event = MusicNumEvene()
        event.localMusicNum = localMusic.value!!.size - 1
        localMusic.value?.removeAt(id)
        if(flag){
            val file = File(music.path)
            if (file.exists()){
                file.delete()
            }
            Log.d(TAG,"删除本地文件"+music.path)
        }
        EventBus.getDefault().post(event)
    }

    /**
     * 获取创建的歌单列表篇
     */
    fun getSonList() {
        mRepository.getUserSongList()
    }

    /**
     * 接收歌单列表
     */
    @Subscribe
    fun loadSongList(event: SongListEvent) {
        Log.d(TAG, "接收到了其他list")
        creatSongList.value = event.list
    }

    /**
     * 保存某首歌到歌单中
     * @param 要保存的歌曲及歌单
     */
    @SuppressLint("CheckResult")
    fun saveToSongList(music: LocalMusic, songList: SongList) {
        //本地收藏，通过外键将歌曲和歌单联系起来,
        val mMusic = LocalMusic()
        mMusic.songListName = songList.name
        mMusic.updateAll("songName = ?", music.songName)
        toast.value = "正在导入"
        //更新本地该歌单歌曲数量
        val mSongList = LitePal.where("objectId = ?",songList.objectId).find(SongList::class.java)[0]
        mSongList.num = mSongList.num+1
        mSongList.save()


        //云端收藏
        val avMusic = AVObject("Music")
        avMusic.apply {
            put("name", music.songName)
            put("singer", music.singerName)
        }

        //先将歌曲文件保存到云端，拿到MP3url
        val file = File(music.path)
        AVFile(music.songName, file).saveInBackground()
            .subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe ({
                //通过objectid拿到歌单对象,将avmusic指向歌单
                val avSongList = AVObject.createWithoutData("SongList", songList.objectId)
                avMusic.apply {
                    put("mp3Url", it.url)
                    put("songList", avSongList)
                    saveInBackground().subscribe({
                        toast.value = "收藏成功"
                        Log.d(TAG, "收藏成功")
                    }, {
                        toast.value = "收藏失败"
                        Log.d(TAG, "收藏失败${it.message}")
                    })
                }
            },{
                Log.d(TAG, "收藏失败${it.message}")
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

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
    }
}
