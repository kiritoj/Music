package com.example.music.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import cn.leancloud.AVObject
import com.example.music.IMAGE_BASE_URL
import com.example.music.MusicApp
import com.example.music.SONG_PLAY_BASE_URL
import com.example.music.model.bean.Track
import com.example.music.model.db.repository.SongListRepository
import com.example.music.model.db.repository.SongsRepository
import com.example.music.model.db.table.LocalMusic
import com.example.music.model.db.table.SongList
import com.example.music.event.RefreshSongList
import com.example.music.event.SongListEvent
import com.example.music.event.SongsEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal

/**
 * Created by tk on 2019/8/23
 *
 */
class SongsVM(val songlist: SongList) : ViewModel() {
    val TAG = "SongsVM"
    val songs = MutableLiveData<ArrayList<Track>>()
    val showAddDialog = MutableLiveData<Boolean>()
    val toast = MutableLiveData<String>()
    val iscollected = MutableLiveData<Boolean>() //该歌单是否已经被收藏过
    var mSongList: SongList? = null //保存组啊数据库的歌单对象
    var creatSongList = MutableLiveData<ArrayList<SongList>>()//创建的歌单
    var observableSongList = MutableLiveData<SongList>()//从排行榜过来的榜单没有创建者等信息，需要重新获取

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
        mSongList = LitePal.where("name = ?", name).findFirst(SongList::class.java, true)
        if (mSongList == null) {
            iscollected.value = false
        } else {
            iscollected.value = true
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
            Log.d(TAG, "已收藏")
            //已经收藏点击取消收藏
            //数据库删除
            mSongList = LitePal.where("name = ?", songlist.name).find(SongList::class.java)[0]
            val mObjectId = mSongList?.objectId
            mSongList?.delete()
            //云端删除
            AVObject.createWithoutData("CollectSongList", mObjectId)
                .deleteInBackground()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    iscollected.value = false
                    toast.value = "已取消收藏"
                }, {
                    Log.d(TAG, it.message)
                })
            //刷新列表
            EventBus.getDefault().post(RefreshSongList("collect", true))

        } else {
            Log.d(TAG, "未收藏")
            //未收藏则收藏
            showAddDialog.value = true
            AVObject("CollectSongList").apply {
                put("description", songlist.description)
                put("creatorAvatar", songlist.creatorAvatar)
                put("collectNum", songlist.collectNum)
                put("netID", songlist.netId)
                put("creatorName", songlist.creatorName)
                put("commentNum", songlist.commentNum)
                put("creatorId", songlist.creatorId)
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
                        EventBus.getDefault().post(RefreshSongList("collect", true))
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

    /**
     * 获取用户创建的歌单
     */
    fun getUserCreatSongList() {
        SongListRepository.getInstance().getUserSongList()
    }

    /**
     * 保存歌曲到创建的歌单
     */
    @SuppressLint("CheckResult")
    fun saveToSongList(music: Track, songList: SongList) {

        //本地收藏
        val mMusic = LocalMusic()
        val mSongList = LitePal.where("name = ?", songList.name)
            .findFirst(SongList::class.java, true)
        mMusic.apply {
            musicId = music.id
            songName = music.name
            singerName = music.ar[0].name
            url = SONG_PLAY_BASE_URL + music.id
            isLocalMusic = false
            coverUrl = IMAGE_BASE_URL + music.id
            songLists.add(mSongList)
            tag = "NET_NON_URL"
        }
        if (mSongList.songs.contains(mMusic)) {
            toast.value = "该歌单已经添加过了，请选择其他歌单"
        } else {
            mMusic.save()
            toast.value = "正在导入"
            //更新本地该歌单歌曲数量
            mSongList.apply {
                num++
                save()
            }

            //云端收藏
            val avMusic = AVObject("Music")
            val avSongList = AVObject.createWithoutData("SongList", songList.objectId)
            avMusic.apply {
                put("id", music.id)
                put("name", music.name)
                put("singer", music.ar[0].name)
                put("albumUrl", music.al.picUrl)
                put("tag", "NET_NON_URL")
                put("songList", avSongList)
                saveInBackground().subscribe({
                    toast.value = "收藏成功"
                    //mMusic.objectID = it.objectId
                    //mMusic.save()
                }, {
                    toast.value = "收藏失败"
                    Log.d(TAG, "SongsVM: 网络歌单音乐收藏失败${it.message}")

                })
            }
            //更新云端歌单歌曲数量
            avSongList.apply {
                put("num", mSongList.num)
                saveInBackground().subscribe({
                    Log.d(TAG, "云端歌单数量更新成功")
                }, {
                    Log.d(TAG, "SongsVM:云端歌单数量更新失败${it.message}")
                })
            }


            //通知MainFragment更新数量
            EventBus.getDefault().post(RefreshSongList("creat", true))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveSongs(event: SongsEvent) {
        songs.value = event.mPlaylist?.tracks as ArrayList
        //没有创作者的是榜单，重新获取创建者，数目等信息
        if (songlist.creatorName.isNullOrEmpty()){
            songlist.apply {
                creatorAvatar = event.mPlaylist.creator.avatarUrl
                creatorId = event.mPlaylist.creator.userId
                creatorName = event.mPlaylist.creator.nickname
                collectNum = event.mPlaylist.subscribedCount
                commentNum = event.mPlaylist.commentCount
            }
            observableSongList.value = songlist
        }
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