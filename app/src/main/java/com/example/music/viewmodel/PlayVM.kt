package com.example.music.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.ContentValues
import android.databinding.ObservableField
import android.preference.PreferenceManager
import android.util.Log
import cn.leancloud.service.APIService
import com.example.music.MusicApp
import com.example.music.PlayManger
import com.example.music.db.table.LocalMusic
import com.example.music.event.RefreshEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.example.music.R
import com.example.music.db.table.SongList
import com.example.music.network.ApiGenerator
import com.example.music.network.services.LrcService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.litepal.LitePal
import cn.leancloud.AVObject



/**
 * Created by tk on 2019/8/24
 */
class PlayVM: ViewModel() {

    val TAG = "PlayVM"
    //当前正在播放的曲目
    val song = ObservableField<LocalMusic>()
    //后台音乐播放器
    val player = PlayManger.player
    //播放/暂停图标
    val playIc = ObservableField<Int>()
    //播放模式
    val modeIndex = MutableLiveData<Int>()
    //当前音乐在播放队列的位置
    val songIndex = MutableLiveData<Int>()
    //是否播放动画
    val isPlaying = MutableLiveData<Boolean>()
    //当前播放音乐的总长度
    val mDuration = MutableLiveData<Int>()
    //歌词
    val lrc = ObservableField<String>()
    //是否添加到我喜欢的音乐图标
    val collectIc = ObservableField<Int>()


    init {
        EventBus.getDefault().register(this)
        //获取上次结束时的模式(默认顺序播放)
        modeIndex.value = PreferenceManager.getDefaultSharedPreferences(MusicApp.context).getInt("playmode",0)

    }

    /**
     * 检查是否在播放
     */
    fun checkPlaying(){
        song.set(PlayManger.quene[PlayManger.index])
        if (PlayManger.player.isPlaying){
            isPlaying.value = true
            playIc.set(R.drawable.ic_play_running)
        }else{
            isPlaying.value = false
            playIc.set(R.drawable.ic_play_pause)
        }
        songIndex.value = PlayManger.index
        mDuration.value = PlayManger.player.duration
        song.get()?.let { getLrc(it) }
        //checkIsMyLove()
    }

    /**
     *检查该单曲是否添加到我喜欢的音乐
     * @param name 歌曲名字
     */
    fun checkIsMyLove(){
        val list = LitePal
            .where("songListName = ? and songName = ?","我喜欢的音乐",song.get()?.songName)
            .find(LocalMusic::class.java)
        if (list.isNullOrEmpty()){
            collectIc.set(R.drawable.vector_drawable_collect_white)
        }else{
            collectIc.set(R.drawable.vector_drawable_collect_red)
        }
    }

    /**
     * 暂停/播放控制
     */
    fun pause(){
        if (PlayManger.player.isPlaying){
            isPlaying.value = false
            playIc.set(R.drawable.ic_play_pause)
            PlayManger.pause()
        }else{
            isPlaying.value = true
            playIc.set(R.drawable.ic_play_running)
            PlayManger.resume()
        }
    }

    /**
     * 播放上一曲
     */

    fun playPrevious(){
        PlayManger.playPreVious()
    }
    /**
     * 播放下一曲
     */
    fun playNext(){
        PlayManger.playNext()
    }

    /**
     * 当播放完成后刷新UI
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(event: RefreshEvent){

        song.set(event.song)
        isPlaying.value = true
        playIc.set(R.drawable.ic_play_running)
        songIndex.value = PlayManger.index
        song.get()?.let { getLrc(it) }
        //checkIsMyLove()
    }




    /**
     * 拖拽进度条
     */
    fun changeProcess(process: Int){
        PlayManger.changeProgress(process)
    }

    /**
     * 更换播放模式
     */
    fun changeMode(){
        modeIndex.value = (1+ modeIndex.value!!) % 3
        Log.d(TAG,modeIndex.value.toString())
        PlayManger.setMode(modeIndex.value!!)
    }

    /**
     * 获取歌词
     */
    @SuppressLint("CheckResult")
    fun getLrc(song: LocalMusic){
        if (song.isLocalMusic){
            Log.d(TAG,"本地音乐暂无歌词")
        }else{
            ApiGenerator.getApiService(LrcService::class.java)
                .getLrc(song.id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    lrc.set(it.string())
                    Log.d(TAG,it.string())
                },{
                    lrc.set("[00:00.000]歌词获取失败")
                    Log.d(TAG,"获取歌词失败${it.message}")
                })
        }
    }

    /**
     * 添加到我喜欢的音乐
     */
    fun addToMyLove(){
//        //添加到当前用户‘我喜欢的音乐’歌单
//        val list = LitePal.where("name = ?","我喜欢的音乐").find(SongList::class.java)
//        when(collectIc.get()){
//            //未添加状态下
//            R.drawable.vector_drawable_collect_white -> {
//                //首先添加到本地数据库
//                if (song.get()?.isLocalMusic!!){
//                    //数据库中已存在，只需修改歌单名属性即可
//                    val value = ContentValues()
//                    value.put("songListName","我喜欢的音乐")
//                    LitePal.updateAll()
//                }
//
//            }
//        }
//
//
//        val songList = AVObject.createWithoutData("SongList", list[0].objectId)
//
//        songList.saveInBackground()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe ({
//                Log.d(TAG,"数量更新成功")
//            },{
//                Log.d(TAG,"数量更新失败${it.message}")
//            })
    }



    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }
}