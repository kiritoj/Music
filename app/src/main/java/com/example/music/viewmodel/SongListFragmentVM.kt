package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
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
    val assortedList = MutableLiveData<ArrayList<SongListBean.DataBean>>()

//    init {
//       EventBus.getDefault().register(this)
//    }
    //获取分类歌单
    fun getAssortedSongList(cat: String, size: Int = 21) {
        val mPage = Random().nextInt(40)
        SongListRepository.getInstance().getAssortedList(cat, size, mPage)
    }


    /**
     * 加载更多
     */
    fun loadMore(cat :String){
        getAssortedSongList(cat)
    }

}