package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.music.MusicApp
import com.example.music.model.db.repository.CategrayRepository
import com.example.music.model.db.table.CatTable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal
import java.util.*
import kotlin.collections.ArrayList as ArrayList1

/**
 * Created by tk on 2019/8/21
 */
class SongListActivityVM : ViewModel() {
    val TAG = "SongListActivityVM"
    val mList = MutableLiveData<ArrayList<String>>() //默认歌单类别
    val category = MutableLiveData<CatTable>()
    /**
     * 获取展示的歌单类别
     */
    init {
        EventBus.getDefault().register(this)
        getCategory()
        getAllCategray()
    }

    /**
     * 获取每个用户的5个歌单种类
     */
    fun getCategory(){
        val list = LitePal.findAll(CatTable::class.java)
        if (list.isNullOrEmpty()) {
            //如果是空的，从网络获取歌单分类
            mList.value = MusicApp.currentUser.getList("catSongList") as ArrayList<String>?
            //存储进数据库
            val cat = CatTable()
            cat.catList = mList.value
            cat.save()
            Log.d(TAG,"从网络获取的歌单类别${mList.value.toString()}")

        }else{
            //从数据库获取
            mList.value = list[0].catList
            Log.d(TAG,"从数据库获取到的歌单类别${list[0].catList.toString()}")

        }
    }

    /**
     * 更改歌单分类后，上传
     * @param 分类的名字：华语，动漫等
     */
    fun update(cat: String){
        mList.value?.apply {
            //删除最后一个分类
            removeAt(4)
            //添加新类别到第二个,第一个类别”全部“不得更改
            add(1,cat)
        }


        //上传到数据库
        LitePal.findAll(CatTable::class.java)[0].apply {
            this.catList = mList.value
            save()
        }

        //上传到云端
        MusicApp.currentUser.apply {
            put("catSongList",mList.value)
            saveInBackground()
                .subscribeOn(Schedulers.io())
                .subscribe {
                    Log.d(TAG,"最新歌单类别保存成功")
                }
        }
    }

    /**
     * 获取全部歌单分类
     */
    fun getAllCategray(){
        CategrayRepository.getCategray()
    }

    /**
     * 获取歌单种类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiceAllCate(event: CatTable){
        category.value = event
        Log.d(TAG,"viewmodel成功获取数据")
    }

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
    }
}