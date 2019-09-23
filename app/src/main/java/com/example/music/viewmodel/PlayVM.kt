package com.example.music.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.ContentValues
import android.content.Intent
import android.databinding.ObservableField
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import android.widget.SeekBar
import cn.leancloud.AVFile
import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import com.example.music.LRC_BASE_URL
import com.example.music.MusicApp
import com.example.music.PlayManger
import com.example.music.R
import com.example.music.model.db.table.LocalMusic
import com.example.music.model.db.table.SongList
import com.example.music.event.RefreshEvent
import com.example.music.event.RefreshSongList
import com.example.music.event.StateEvent
import com.example.music.model.bean.Lrc
import com.example.music.network.ApiGenerator
import com.example.music.network.services.LrcService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal
import java.io.File
import java.util.*


/**
 * Created by tk on 2019/8/24
 */
class PlayVM : ViewModel() {

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
    //当前播放音乐的进度
    val mCurrentPosition = MutableLiveData<Int>()
    //歌词
    val lrc = ObservableField<Lrc>()
    //是否添加到我喜欢的音乐图标
    val collectIc = ObservableField<Int>()

    val toast = MutableLiveData<String>()
    //是否启用seekbar
    var seekBarEnable = false
    //是够显示缓冲
    val showBuffer = MutableLiveData<Boolean>()
    //显示添加到我喜欢的
    val showProcessBar = ObservableField<Boolean>(false)
    init {
        EventBus.getDefault().register(this)
        //获取上次结束时的模式(默认顺序播放)
        modeIndex.value =
            PreferenceManager.getDefaultSharedPreferences(MusicApp.context).getInt("playmode", 0)

    }

    /**
     * 检查是否在播放
     */
    fun checkPlaying() {

        if (PlayManger.hasSetDataSource) {
            Log.d(TAG,"已设置")
            mDuration.value = PlayManger.player.duration
            mCurrentPosition.value = PlayManger.player.currentPosition
            if (PlayManger.player.isPlaying) {
                isPlaying.value = true
                playIc.set(R.drawable.ic_play_running)
            } else {

                isPlaying.value = false
                playIc.set(R.drawable.ic_play_pause)

            }
            song.get()?.let { getLrc(it) }
        }else{
            Log.d(TAG,"未设置")
        }
        checkIsMyLove()
    }

    /**
     *检查该单曲是否添加到我喜欢的音乐
     * @param name 歌曲名字
     */
    fun checkIsMyLove() {
        val songList = LitePal.where("name = ?", "我喜欢的音乐").findFirst(SongList::class.java, true)

        if (songList.songs.contains(song.get())) {
            collectIc.set(R.drawable.vector_drawable_collect_red)
        } else {
            collectIc.set(R.drawable.vector_drawable_collect_white)
        }
    }

    /**
     * 暂停/播放控制
     */
    fun pause() {
        if (PlayManger.player.isPlaying) {
            isPlaying.value = false
            playIc.set(R.drawable.ic_play_pause)
            PlayManger.pause()
        } else {
            isPlaying.value = true
            playIc.set(R.drawable.ic_play_running)
            PlayManger.resume()
        }

    }

    /**
     * 播放上一曲
     */

    fun playPrevious() {
        PlayManger.playPreVious()
    }

    /**
     * 播放下一曲
     */
    fun playNext() {
        PlayManger.playNext()
    }

    /**
     * 当播放完成后刷新UI
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun update(event: RefreshEvent) {

        song.set(event.mSong)
        Log.d(TAG, "id为" + song.get()?.musicId.toString())

        isPlaying.value = true
        playIc.set(R.drawable.ic_play_running)
        songIndex.value = event.position

        getLrc(event.mSong)
        checkIsMyLove()
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun receiveState(event: StateEvent){
        //更新播放按钮icon
        Log.d(TAG,"音乐状态变化")
        when(event.state){
            PlayManger.State.PLAY -> playIc.set(R.drawable.ic_play_running)
            PlayManger.State.PAUSE -> playIc.set(R.drawable.ic_play_pause)

        }
    }


    /**
     * 拖拽进度条
     */
    fun changeProcess(process: Int) {
        PlayManger.changeProgress(process)
    }

    /**
     * 更换播放模式
     */
    fun changeMode() {
        modeIndex.value = (1 + modeIndex.value!!) % 3
        PlayManger.setMode(modeIndex.value!!)
    }

    /**
     * 获取歌词
     */
    @SuppressLint("CheckResult")
    fun getLrc(song: LocalMusic) {
        when (song.tag) {
            "LOCAL","NET_WITH_URL" -> {
                Log.d(TAG, "暂无歌词")
                lrc.set(null)
            }
            "NET_NON_URL" -> {
                ApiGenerator.getApiService(LrcService::class.java)
                    .getLrc(LRC_BASE_URL,song.musicId!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        lrc.set(it)
                        Log.d(TAG,it.lrc.lyric)
                    }, {
                        Log.d(TAG, "获取歌词失败${it.message}")
                    })
            }
        }


    }

    /**
     * 添加到我喜欢的音乐
     */
    @SuppressLint("CheckResult")
    fun addToMyLove() {
        showProcessBar.set(true)
        //添加到当前用户‘我喜欢的音乐’歌单
        val mSongList = LitePal.where("name = ?", "我喜欢的音乐").findFirst(SongList::class.java, true)
        val avSongList = AVObject.createWithoutData("SongList", mSongList.objectId)
        val mSong = LitePal.where(
            "songName = ? and singerName = ?",
            song.get()?.songName,
            song.get()?.singerName
        )
            .findFirst(LocalMusic::class.java, true)

        val name = song.get()?.songName //获取当前歌曲名，防止在异步线程更新数据库发生错乱
        when (collectIc.get()) {
            //未添加状态下
            R.drawable.vector_drawable_collect_white -> {
                //首先添加到本地数据库

                //数据库中已存在，只需修改修改对应歌曲的歌单列表即可,三种类型都可能存在
                if (mSong != null) {
                    mSong.songLists.add(mSongList)
                    mSong.save()
                    when (mSong.tag) {
                        "Local" -> {
                            //之前该本地歌曲可能上传过其他歌单，无需重新上传文件来获取播放url,第一次添加至歌单要上传文件
                            if (TextUtils.isEmpty(mSong.url)) {
                                val file = File(mSong.path)
                                AVFile(mSong.songName, file).saveInBackground()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        mSong.url = it.url
                                        mSong.save()

                                        val avMusic = AVObject("Music")
                                        avMusic.apply {
                                            put("name", mSong.songName)
                                            put("singer", mSong.singerName)
                                            put("mp3Url", it.url)
                                            put("songList", avSongList)
                                            put("tag", "NET_WITH_URL")
                                            saveInBackground()
                                                .subscribe({
                                                    showProcessBar.set(false)
                                                    toast.value = "添加成功"
                                                    collectIc.set(R.drawable.vector_drawable_collect_red)
                                                    mSong.objectID = it.objectId
                                                    mSong.save()
                                                }, {
                                                    showProcessBar.set(false)
                                                    toast.value = "添加失败"
                                                    Log.d(TAG, "PlayVM:保存avMusic失败${it.message}")
                                                })
                                        }
                                    }, {
                                        Log.d(TAG, "PlayVM:本地文件上传失败${it.message}")
                                    })
                            } else {
                                //已上传过直接使用url保存到云端
                                val avMusic = AVObject("Music")
                                avMusic.apply {
                                    put("name", mSong.songName)
                                    put("singer", mSong.singerName)
                                    put("mp3Url", mSong.url)
                                    put("songList", avSongList)
                                    put("tag", "NET_WITH_URL")
                                    saveInBackground()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({
                                            showProcessBar.set(false)
                                            toast.value = "添加成功"
                                            collectIc.set(R.drawable.vector_drawable_collect_red)
                                        }, {
                                            showProcessBar.set(false)
                                            toast.value = "添加失败"
                                            Log.d(TAG, "PlayVM:保存代带有url的avMusic失败${it.message}")
                                        })
                                }
                            }
                        }
                        //带有url的音乐，一般是用户创建的歌单里面的音乐
                        "NET_WITH_URL" -> {
                            //已上传过直接使用url保存到云端
                            val avMusic = AVObject("Music")
                            avMusic.apply {
                                put("name", mSong.songName)
                                put("singer", mSong.singerName)
                                put("mp3Url", mSong.url)
                                put("songList", avSongList)
                                put("tag", "NET_WITH_URL")
                                saveInBackground()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        showProcessBar.set(false)
                                        toast.value = "添加成功"
                                        collectIc.set(R.drawable.vector_drawable_collect_red)
                                    }, {
                                        showProcessBar.set(false)
                                        toast.value = "添加失败"
                                        Log.d(TAG, "PlayVM:保存代带有url的avMusic失败${it.message}")
                                    })
                            }
                        }

                        //没有播放url的，一般是从网络获取的歌单里面的音乐
                        "NET_NON_URL" -> {
                            val avMusic = AVObject("Music")
                            avMusic.apply {
                                put("id", mSong.musicId)
                                put("name", mSong.songName)
                                put("singer", mSong.singerName)
                                put("songList", avSongList)
                                put("tag", "NET_NON_URL")
                                saveInBackground()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        showProcessBar.set(false)
                                        toast.value = "添加成功"
                                        collectIc.set(R.drawable.vector_drawable_collect_red)
                                    }, {
                                        showProcessBar.set(false)
                                        toast.value = "添加失败"
                                        Log.d(TAG, "PlayVM:保存代带有url的avMusic失败${it.message}")
                                    })
                            }
                        }
                    }

                } else {
                    //数据库中没有一定是网络歌单里面的音乐
                    song.get()?.apply {
                        songLists.add(mSongList)
                        save()
                    }
                    val avMusic = AVObject("Music")
                    //网络歌曲自带播放url不用上传文件
                    avMusic.apply {
                        put("name", song.get()?.songName)
                        put("singer", song.get()?.singerName)
                        put("songList", avSongList)
                        put("albumUrl", song.get()?.coverUrl)
                        put("id", song.get()?.musicId)
                        put("tag", "NET_NON_URL")
                        saveInBackground()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                showProcessBar.set(false)
                                toast.value = "添加成功"
                                collectIc.set(R.drawable.vector_drawable_collect_red)
                                //上传成功保存objectID
                                val value = ContentValues()
                                value.put("objectID", it.objectId)
                                LitePal.updateAll(
                                    LocalMusic::class.java, value, "songName = ?", name
                                )
                            }, {
                                showProcessBar.set(false)
                                toast.value = "添加失败"
                                Log.d(TAG, "PlayVM:保存不带url的avmusic失败，${it.message}")
                            })
                    }

                }

                //更新本地数量
                mSongList.num++
                mSongList.save()

            }

            //已添加状态下,取消添加
            R.drawable.vector_drawable_collect_red -> {

                //本地删除
                mSongList.apply {
                    num--
                    save()
                }
                mSong.apply {
                    songLists.remove(mSongList)
                    save()
                }


                //云端删除

                val avQuery1 = AVQuery<AVObject>("Music").whereEqualTo("songList", avSongList)
                val avQuery2 = AVQuery<AVObject>("Music").whereEqualTo("name", name)
                val avQuery = AVQuery.and(Arrays.asList(avQuery1, avQuery2))
                avQuery.firstInBackground.subscribeOn(Schedulers.io())
                    .subscribe({
                        it.deleteInBackground().subscribeOn(Schedulers.io())
                            .subscribe({
                                showProcessBar.set(false)
                                toast.value = "已移除"
                                collectIc.set(R.drawable.vector_drawable_collect_white)
                            }, {
                                showProcessBar.set(false)
                                toast.value = "移除失败"
                                Log.d(TAG, "PlayVM:删除目标歌曲失败${it.message}")
                            })
                    }, {
                        Log.d(TAG, "PlayVM:查找要删除的歌曲失败${it.message}")
                    })


            }
        }

        //最后更新云端歌单里的歌曲数量
        avSongList.apply {
            put("num", mSongList.num)
            saveInBackground()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "PlayVM:增删操作云端歌单数量更新成功")
                }, {
                    Log.d(TAG, "PlayVM:增删操作后云端歌单数量更新失败${it.message}")
                })

        }
        //通知MainFragment更新数量
        EventBus.getDefault().post(RefreshSongList("creat", true))

    }

    /**
     * 禁用seekbar拖动
     */
    fun banSeekBar(mSeekBar: SeekBar){
        mSeekBar.setClickable(false)
        mSeekBar.setEnabled(false)
        mSeekBar.setSelected(false)
        mSeekBar.setFocusable(false)
        seekBarEnable = false
        showBuffer.value = true
    }

    /**
     * 启用seekBar
     */
    fun enableSeekBar(mSeekBar: SeekBar){
        if (!seekBarEnable) {
            mSeekBar.setClickable(true)
            mSeekBar.setEnabled(true)
            mSeekBar.setSelected(true)
            mSeekBar.setFocusable(true)
            seekBarEnable = true
            showBuffer.value = false
        }

    }


    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }
}