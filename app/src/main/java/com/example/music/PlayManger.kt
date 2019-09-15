package com.example.music

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.preference.PreferenceManager
import android.util.Log
import com.example.music.model.db.table.LocalMusic
import com.example.music.event.ProcessEvent
import com.example.music.event.RefreshEvent
import com.example.music.event.StateEvent
import com.example.music.network.ApiGenerator
import com.example.music.network.services.SongPlayService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    var hasSetDataSource = false //是否已经设置播放源
    //播放模式
    val ORDER = 0 //顺序播放
    val RANDOM = 1 //随机播放
    val REPEAT = 2 //循环播放

    var queneTag = ""

    //状态控制
    enum class State {
        PLAY //播放
        ,
        PAUSE //暂停
        ,
        NEXT //下一曲
        ,
        PREVIOUS//上一曲
    }

     var timer: Timer = Timer() //更新进度条

    init {

        //查找之前的播放模式
        playMode =
            PreferenceManager.getDefaultSharedPreferences(MusicApp.context).getInt("playmode", 0)
        //播放完成监听
        player.setOnCompletionListener {
            //播放结束更新
            timer.cancel()
            playNext()
        }
        //准备监听
        player.setOnPreparedListener {
            it.start()
            hasSetDataSource = true
            //发送歌曲总时长
            EventBus.getDefault().postSticky(ProcessEvent("duration", player.duration))
            //开始更新进度条,在设置播放之前不能调用duration，currentPosition，否则会出现常见错误-38,0
            timer = Timer()
            timer.schedule(
                object : TimerTask() {
                    override fun run() {
                        //每隔一秒发送当前播放进度
                        if (hasSetDataSource) {
                            EventBus.getDefault()
                                .postSticky(ProcessEvent("current", player.currentPosition))
                        }

                    }
                }, 0, 1000
            )
        }
        //播放失败监听
        player.setOnErrorListener { mp, what, extra ->
            //这里记录一下，常见错误-38,0，在设开始播放之前，不要对player做获取进度等操作
            Log.d(TAG, "OnError - Error code: " + what + " Extra code: " + extra);
            when(what){
                -1004-> Log.d(TAG, "MEDIA_ERROR_IO")
                -1007-> Log.d(TAG, "MEDIA_ERROR_MALFORMED")
                 200 -> Log.d(TAG, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK")
                 100 -> Log.d(TAG, "MEDIA_ERROR_SERVER_DIED")
                -110 -> Log.d(TAG, "MEDIA_ERROR_TIMED_OUT")
                   1 -> Log.d(TAG, "MEDIA_ERROR_UNKNOWN")
                -1010-> Log.d(TAG, "MEDIA_ERROR_UNSUPPORTED")

            }
            when(extra){
                800-> Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING")
                702-> Log.d(TAG, "MEDIA_INFO_BUFFERING_END")
                701-> Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE")
                802-> Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE")
                801-> Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE")
                  1-> Log.d(TAG, "MEDIA_INFO_UNKNOWN")
                  3-> Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START")
                700-> Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING")
            }

            false
        }


    }

    /**
     * 设置播放队列及播放位置
     */
    fun setPlayQuene(mQuene: ArrayList<LocalMusic>, mIndex: Int, mTag:String) {
        queneTag = mTag
        quene.clear()
        quene.addAll(mQuene)
        index = mIndex
        play(mQuene[mIndex])
    }

    /**
     * 如果已经设置播放队列，播放队列中的目标音乐
     */
    fun setPlayIndex(mIndex: Int) {
        index = mIndex
        play(quene[mIndex])
    }

    /**
     *播放音乐
     */
    @SuppressLint("CheckResult")
    fun play(song: LocalMusic) {
        hasSetDataSource = false
        //立即通知通知其他活动更改底部的音乐信息，不再等待准备之后
        EventBus.getDefault().postSticky(RefreshEvent(song, index, queneTag))
        //立即清空进度，以免在网络不佳情况下，总进度迟迟得不到更新
        EventBus.getDefault().postSticky(ProcessEvent("duration", 0))
        EventBus.getDefault().postSticky(ProcessEvent("current", 0))
        player.reset()
        when (song.tag) {
            //本地音乐
            "LOCAL" -> {
                player.apply {
                    setDataSource(song.path)
                    prepareAsync()
                }

            }

            //带有url的歌曲
            "NET_WITH_URL" -> {
                player.apply {
                    setDataSource(song.url)
                    prepareAsync()

                }

            }

            //来自网络的音乐,必须动态获取播放地址，一段时间后地址会失效
            "NET_NON_URL" -> {
                Log.d(TAG,song.musicId.toString()+"---"+song.songName)
                ApiGenerator.getApiService(SongPlayService::class.java)
                    .getUrl(song.musicId!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        player.apply {
                            Log.d(TAG,it.data[0].url)
                            setDataSource(it.data[0].url)
                            prepareAsync()
                        }

                    }, {
                        playNext()

                        Log.d(TAG, "获取歌曲播放url失败${it.message}")
                    })
            }

        }



    }

    /**
     * 依据播放模式播放下一曲
     */
    fun playNext() {
        when (playMode) {
            ORDER -> play(quene[getNextIndex()])
            RANDOM -> play(quene[getRandomIndex()])
            REPEAT -> play(quene[getRepeatIndex()])
        }
    }

    /**
     * 播放下一曲
     */
    fun playPreVious() {
        when (playMode) {
            ORDER -> play(quene[getPreviousIndex()])
            RANDOM -> play(quene[getRandomIndex()])
            REPEAT -> play(quene[getRepeatIndex()])
        }
    }

    /**
     *暂停
     */
    fun pause() {
        player.pause()
        //通知音乐已暂停
        EventBus.getDefault().post(StateEvent(PlayManger.State.PAUSE))



    }

    /**
     * 继续播放
     */
    fun resume() {
        player.start()
        EventBus.getDefault().post(StateEvent(PlayManger.State.PLAY))
    }

    /**
     * 调整更新进度
     */
    fun changeProgress(process: Int) {
        player.seekTo(process)
    }

    /**
     * 下一首的播放位置
     */
    fun getNextIndex(): Int {
        index = (index + 1) % quene.size
        return index
    }

    /**
     * 下一首的播放位置
     */
    fun getPreviousIndex(): Int {
        index = (index - 1) % quene.size
        return index
    }

    /**
     * 随机播放的位置
     */
    fun getRandomIndex(): Int {
        index = Random().nextInt(quene.size)
        return index
    }

    /**
     * 循环播放的位置
     */
    fun getRepeatIndex(): Int {
        return index
    }

    /**
     * 设置播放模式
     */
    fun setMode(mode: Int) {
        playMode = mode
        PreferenceManager.getDefaultSharedPreferences(MusicApp.context).edit()
            .putInt("playmode", mode).apply()
    }

    /**
     * 播放控制
     */
    fun setState(state: State) {
        when (state) {
            State.PLAY -> resume()
            State.PAUSE -> pause()
            State.NEXT -> playNext()
            State.PREVIOUS -> playPreVious()
        }
    }


    /**
     * 结束时释放资源
     */
    fun recycle() {
        Log.d(TAG,"结束")
        player.stop()
        player.release()

    }

}