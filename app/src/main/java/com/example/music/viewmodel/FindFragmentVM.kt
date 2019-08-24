package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.music.bean.LastMusicBean
import com.example.music.bean.SongListBean
import com.example.music.db.repository.BannerRepository
import com.example.music.db.repository.MusicRepository
import com.example.music.db.repository.SongListRepository
import com.example.music.db.table.BannerTable
import com.example.music.event.AssortedSongListEvent
import com.example.music.event.BanberEvent
import com.example.music.event.HotSongListLimit
import com.example.music.event.LatestSongEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * Created by tk on 2019/8/20
 */
class FindFragmentVM : ViewModel() {
    val TAG = "FindFragment"
    //轮播图
    val listBanner = MutableLiveData<ArrayList<BannerTable>>()
    //热门歌单
    val songList = MutableLiveData<ArrayList<SongListBean.DataBean>>()

    //最新单曲
    val latestSong = MutableLiveData<ArrayList<LastMusicBean.DataBean>>()

    init {

        EventBus.getDefault().register(this)
        getBanners()
        getHotSongList()
        getLatestSong()
    }


    /**
     * 获取首页轮播图
     */
    fun getBanners() {
        BannerRepository.getBanners()
        Log.d(TAG, "getBanners:开始获取轮播图")
    }

    /**
     * 获取热门歌单
     */

    fun getHotSongList() {
        val mPage = Random().nextInt(200)
        SongListRepository.getInstance().getHotSongList(size = 6, page = mPage)
    }


    /**
     * 获取最新单曲
     */
    fun getLatestSong() {
        MusicRepository.getLatestSong()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveBanners(event: BanberEvent) {
        listBanner.value = event.list
        Log.d(TAG, "以获取banner")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveHotSongList(event: HotSongListLimit) {
        songList.value = event.list
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveLatestSong(event: LatestSongEvent) {
        latestSong.value = event.list
    }



}