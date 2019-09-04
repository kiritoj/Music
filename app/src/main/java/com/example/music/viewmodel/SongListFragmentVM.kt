package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.music.bean.Playlists
import com.example.music.bean.SongListBean
import com.example.music.db.repository.SongListRepository
import com.example.music.event.AssortedSongListEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * Created by tk on 2019/8/21
 */
class SongListFragmentVM() : ViewModel(){
    //分类歌单
    val assortedList = MutableLiveData<ArrayList<Playlists>>()

//    init {
//       EventBus.getDefault().register(this)
//    }
    //获取分类歌单
    fun getAssortedSongList(size: Int = 18,cat: String) {
    //大约1200条数据，每次只获取18条
    val mOffset = Random().nextInt(65)*18
        SongListRepository.getInstance().getAssortedList( size,cat,mOffset)
    }


    /**
     * 加载更多
     */
    fun loadMore(mCat :String){
        getAssortedSongList(cat = mCat)
    }

}