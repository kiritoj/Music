package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.music.event.*
import com.example.music.model.db.bean.Data
import com.example.music.model.db.bean.LastMusicBean
import com.example.music.model.db.bean.Playlists
import com.example.music.model.db.bean.SongListBean
import com.example.music.model.db.repository.BannerRepository
import com.example.music.model.db.repository.MusicRepository
import com.example.music.model.db.repository.SongListRepository
import com.example.music.model.db.table.BannerTable
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
    val songList = MutableLiveData<ArrayList<Playlists>>()

    //最新单曲
    val latestSong = MutableLiveData<ArrayList<Data>>()

    //显示歌单加载失败
    val showError1 = MutableLiveData<Boolean>()
    //显示新歌加载失败
    val showError2 = MutableLiveData<Boolean>()

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
        //首页只展示6张歌单，一共1200张
        val mOffset = Random().nextInt(200)*6
        SongListRepository.getInstance().getHotSongList(size = 6, offset = mOffset)
    }

    /**
     * 获取最新单曲
     */
    fun getLatestSong() {
        MusicRepository.getLatestSong()
    }

    /**
     * 刷新
     */
    fun refresh(){
        showError1.value = false
        showError2.value = false
        getHotSongList()
        getLatestSong()
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveLoadState(event: LoadEvent){
        if (event.errorTag.equals("latestSong")){
            showError2.value = true
        }else{
            showError1.value = true
        }
    }


}