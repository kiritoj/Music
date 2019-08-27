package com.example.music

import android.media.MediaPlayer
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import com.example.music.db.table.LocalMusic
import com.example.music.event.ProcessEvent
import com.example.music.event.RefreshEvent
import com.example.music.event.StateEvent
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by tk on 2019/8/24
 */
object PlayManger {
    val TAG = "PlayManger"
    val player = MediaPlayer() //播放器
    var playMode: Int
    val quene = ArrayList<LocalMusic>() //播放队列
    var index = 0 //当前播放位置
    //播放模式
    val ORDER = 0 //顺序播放
    val RANDOM = 1 //随机播放
    val REPEAT = 2 //循环播放

    //状态控制
    enum class State{
        PLAY //播放
        ,PAUSE //暂停
        ,NEXT //下一曲
        ,PREVIOUS//上一曲
    }

    lateinit var timer: Timer //更新进度条

    init {

        //查找之前的播放模式
        playMode = PreferenceManager.getDefaultSharedPreferences(MusicApp.context).getInt("playmode",0)
        player.setOnCompletionListener {
            //播放结束更新
            timer?.cancel()
            playNext()
        }

    }

    /**
     * 设置播放队列及播放位置
     */
    fun setPlayQuene(mQuene: ArrayList<LocalMusic>,mIndex: Int){
        quene.clear()
        quene.addAll(mQuene)
        index = mIndex
        Log.d(TAG,"setPlayQuene:开始播放")
        play(mQuene[mIndex])
    }

    /**
     *播放音乐
     */
    fun play(song: LocalMusic){
        player.reset()
        if (song.isLocalMusic){
            //本地音乐
            player.apply {
                setDataSource(song.path)

            }
        }else{
            //来自网络的音乐
            player.apply {
                setDataSource(song.url)

            }
        }
        player.apply {
            prepareAsync()
            setOnPreparedListener{
                it.start()
                //准备完成发送歌曲总长度
                EventBus.getDefault().post(ProcessEvent("duration",player.duration))
                Log.d(TAG,"${player.duration}")
            }
        }
        //通知其他活动更改底部的音乐信息
        EventBus.getDefault().post(RefreshEvent(song, index))
        //开始是更新进度条
        timer = Timer()
        timer.schedule(object : TimerTask(){
            override fun run() {
                //每隔一秒发送当前播放进度
                 EventBus.getDefault().post(ProcessEvent("current",player.currentPosition))

            }
        },0,1000)

    }

    /**
     * 依据播放模式播放下一曲
     */
    fun playNext(){
        when(playMode){
            ORDER -> play(quene[getNextIndex()])
            RANDOM -> play(quene[getRandomIndex()])
            REPEAT -> play(quene[getRepeatIndex()])
        }
    }

    /**
     * 播放下一曲
     */
    fun playPreVious(){
        when(playMode){
            ORDER -> play(quene[getPreviousIndex()])
            RANDOM -> play(quene[getRandomIndex()])
            REPEAT -> play(quene[getRepeatIndex()])
        }
    }

    /**
     *暂停
     */
    fun pause(){
        player.pause()
        //通知音乐已暂停
       EventBus.getDefault().post(StateEvent(PlayManger.State.PAUSE))
    }

    /**
     * 继续播放
     */
    fun resume(){
        player.start()
       EventBus.getDefault().post(StateEvent(PlayManger.State.PLAY))
    }

    /**
     * 调整更新进度
     */
    fun changeProgress(process: Int){
        player.seekTo(process)
    }

    /**
     * 下一首的播放位置
     */
    fun getNextIndex(): Int  {
        index = (index+1) % quene.size
        return index
    }

    /**
     * 下一首的播放位置
     */
    fun getPreviousIndex(): Int{
        index = (index-1) % quene.size
        return index
    }

    /**
     * 随机播放的位置
     */
    fun getRandomIndex(): Int{
        index = Random().nextInt(quene.size)
        return index
    }

    /**
     * 循环播放的位置
     */
    fun getRepeatIndex(): Int{
        return index
    }

    /**
     * 设置播放模式
     */
    fun setMode(mode: Int){
        playMode = mode
        PreferenceManager.getDefaultSharedPreferences(MusicApp.context).edit().putInt("playmode",mode).apply()
    }

    /**
     * 播放控制
     */
    fun setState(state: State){
        when(state){
            State.PLAY -> resume()
            State.PAUSE -> pause()
            State.NEXT -> playNext()
            State.PREVIOUS -> playPreVious()
        }
    }



    /**
     * 结束时释放资源
     */
    fun recycle(){
        player.release()

    }

}