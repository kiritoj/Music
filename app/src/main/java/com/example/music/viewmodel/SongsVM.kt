package com.example.music.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.preference.PreferenceManager
import android.util.Log
import cn.leancloud.AVFile
import cn.leancloud.AVObject
import com.example.music.IMAGE_BASE_URL
import com.example.music.MusicApp
import com.example.music.bean.SongListBean
import com.example.music.bean.SongsBean
import com.example.music.db.repository.SongListRepository
import com.example.music.db.repository.SongsRepository
import com.example.music.db.table.LocalMusic
import com.example.music.db.table.SongList
import com.example.music.event.RefreshSongList
import com.example.music.event.SongListEvent
import com.example.music.event.SongsEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal
import java.io.File
import com.example.music.R
import com.example.music.SONG_PLAY_BASE_URL

/**
 * Created by tk on 2019/8/23
 *
 */
class SongsVM(val songlist: SongList) : ViewModel() {
    val TAG = "SongsVM"
    val songs = MutableLiveData<ArrayList<SongsBean.DataBean.TracksBean>>()
    val showAddDialog = MutableLiveData<Boolean>()
    val toast = MutableLiveData<String>()
    val iscollected = MutableLiveData<Boolean>() //该歌单是否已经被收藏过
    var mSomgList : SongList? = null //保存组啊数据库的歌单对象
    var creatSongList = MutableLiveData<ArrayList<SongList>>()//创建的歌单

    init {
        EventBus.getDefault().register(this)
        songlist.name?.let { checkIsCollect(it) }
        songlist.netId?.let { getSongs(it) }

    }


    /**
     * 获取歌单的全部歌曲
     */
    fun getSongs(id: Long) {
        SongsRepository.getSongsFromHttp(id)
    }

    /**
     * 检查是否已经收藏过
     */
    fun checkIsCollect(name: String) {
        val list = LitePal.where("name = ?", name).find(SongList::class.java)
        if (list.isNullOrEmpty()) {
            iscollected.value = false
        } else {
            iscollected.value = true
            mSomgList = list[0]
        }
    }


    /**
     * 添加歌单
     * @param 歌单名字
     * @param 歌单封面
     * @param 歌单曲目数量
     */

    @SuppressLint("CheckResult")
    fun addSongList() {

        if (iscollected.value!!) {
            Log.d(TAG,"已收藏")
            //已经收藏点击取消收藏
            //数据库删除
            mSomgList = LitePal.where("name = ?", songlist.name).find(SongList::class.java)[0]
            val mObjectId = mSomgList?.objectId
            mSomgList?.delete()
            //云端删除
            AVObject.createWithoutData("CollectSongList",mObjectId)
                .deleteInBackground()
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    iscollected.value = false
                    toast.value = "已取消收藏"
                },{
                    Log.d(TAG,it.message)
                })
            //刷新列表
            EventBus.getDefault().post(RefreshSongList("collect",true))

        } else {
            Log.d(TAG,"未收藏")
            //未收藏则收藏
            showAddDialog.value = true
            AVObject("CollectSongList").apply {
                put("description",songlist.description)
                put("creatorAvatar",songlist.creatorAvatar)
                put("collectNum",songlist.collectNum)
                put("netID",songlist.netId)
                put("creatorName",songlist.creatorName)
                put("commentNum",songlist.commentNum)
                put("creatorId",songlist.creatorId)
                put("name", songlist.name)
                put("owner", MusicApp.currentUser)
                put("coverUrl", songlist.coverUrl)
                put("num", songlist.num)

                saveInBackground().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        //保存到本地数据库
                        songlist.apply {
                            objectId = it.objectId
                            save()
                        }
                        //刷新歌单列表
                        EventBus.getDefault().post(RefreshSongList("collect",true))
                        iscollected.value = true
                        showAddDialog.value = false
                        toast.value = "添加成功"

                    }, {
                        showAddDialog.value = false
                        toast.value = "添加失败"
                        Log.d(TAG, it.message)

                    })
            }
        }
    }

    fun getUserCreatSongList(){
//        val preference = PreferenceManager.getDefaultSharedPreferences(MusicApp.context)
//        val hasGetFromHttp = preference.getBoolean("hasGetFromHttp",false)
//        if (hasGetFromHttp){
//            SongListRepository.getInstance().getUserSongListFromDB()
//        }else{
//            SongListRepository.getInstance().getUserSongListFromHttp()
//            preference.edit().apply{
//                putBoolean("hasGetFromHttp",true)
//                apply()
//            }
//        }
        SongListRepository.getInstance().getUserCollectSongList()
    }

    /**
     * 保存歌曲到创建的歌单
     */
    @SuppressLint("CheckResult")
    fun saveToSongList(music: SongsBean.DataBean.TracksBean, songList: SongList){
        //本地收藏，通过外键将歌曲和歌单联系起来,
        val mMusic = LocalMusic()
        mMusic.songListName = songList.name
        mMusic.updateAll("songName = ?", music.name)
        toast.value = "正在导入"
        //更新本地该歌单歌曲数量
        val mSongList = LitePal.where("objectId = ?",songList.objectId).find(SongList::class.java)[0]
        mSongList.num = mSongList.num+1
        mSongList.save()


        //云端收藏
        val avMusic = AVObject("Music")
        avMusic.apply {
            put("id",music.id)
            put("name", music.name)
            put("singer", music.artists?.get(0)?.name)
            put("albumUrl", IMAGE_BASE_URL+music.id)
            put("mp3Url", SONG_PLAY_BASE_URL+music.id)
        }

        val avSongList = AVObject.createWithoutData("SongList", songList.objectId)
                avMusic.apply {
                    put("songList", avSongList)
                    saveInBackground().subscribe({
                        toast.value = "收藏成功"
                    }, {
                        toast.value = "收藏失败"
                        Log.d(TAG, it.message)
                    })
                }

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
    fun receiveSongs(event: SongsEvent) {
        songs.value = event.list
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loadSongList(event: SongListEvent) {
        creatSongList.value = event.list
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

}