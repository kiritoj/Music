package com.example.music.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.preference.PreferenceManager
import android.util.Log
import cn.leancloud.AVFile
import cn.leancloud.AVObject
import com.example.music.DEFAULT_AVATAR
import com.example.music.MusicApp
import com.example.music.util.checkStringIsNull
import com.example.music.event.MusicNumEvene
import com.example.music.event.RefreshSongList
import com.example.music.event.SongListEvent
import com.example.music.model.db.repository.MvRepository
import com.example.music.model.db.repository.SongListRepository
import com.example.music.model.db.table.SongList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.litepal.LitePal
import java.io.ByteArrayOutputStream

/**
 * Created by tk on 2019/8/16
 */
class MineFragmentVM : ViewModel() {
    val TAG = "MineFragmentVM"
    val mUserAvatar = ObservableField<String>(DEFAULT_AVATAR)//头像
    val mUserName = ObservableField<String>()//用户名
    val isUpLoadSuccess = MutableLiveData<Boolean>()
    val isShowCreatedList = MutableLiveData<Boolean>()//控制创建歌单的可见性
    val isShowCollectList = MutableLiveData<Boolean>()//控制收藏歌单的可见性
    var creatSongList = MutableLiveData<ArrayList<SongList>>()//创建的歌单
    var collectSongList = MutableLiveData<ArrayList<SongList>>()//收藏的歌单
    val repository = SongListRepository.getInstance()//歌单仓库
    val mLocalMusicNum = ObservableField<Int>()//本地音乐的数量
    val isShowProcessPar = MutableLiveData<Boolean>()
    val toast = MutableLiveData<String>()
    init {

        //加载用户头像和昵称，首先从本地加载
        val preference = PreferenceManager.getDefaultSharedPreferences(MusicApp.context)
        val name = preference.getString("username", "")
        val avatar = preference.getString("useravatar", "")
        mLocalMusicNum.set(preference.getInt("localmusicnum", 0))

        if (checkStringIsNull(name)) {
            //从网络拉取用户名
            mUserName.set(MusicApp.currentUser.username)
            //保存用户名
            preference.edit().apply {
                putString("username", mUserName.get())
                apply()
            }
        } else {
            mUserName.set(name)
        }
        if (checkStringIsNull(avatar)) {
            //从网络获取头像链接
            MusicApp.currentUser.getAVFile("avatar")?.url?.let { mUserAvatar.set(it) }
            preference.edit().apply {
                putString("useravatar", mUserAvatar.get())
                apply()
            }
        } else {
            mUserAvatar.set(avatar)
        }

        //默认显示歌单
        isShowCreatedList.value = true
        isShowCollectList.value = true
        EventBus.getDefault().register(this)

        getSonList()
        getCollectSongList()
        getCollectMv()

    }

    //获取创建的歌单列表
    fun getSonList() {
        repository.getUserSongList()
    }

    //获取收藏的歌单的列表
    fun getCollectSongList(){
        repository.getUserCollectSongList()
    }

    //获取收藏的mv
    fun getCollectMv(){
        MvRepository.getCollectMv()
    }


    /**
     * 上传头像
     *
     */
    @SuppressLint("CheckResult")
    fun uploadAvatar(bitmap: Bitmap) {
        val outputStream = ByteArrayOutputStream(bitmap.byteCount)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        //上传图片，返回url
        val file = AVFile("avatar.png", outputStream.toByteArray())
        file.saveInBackground().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mUserAvatar.set(it.url)
                PreferenceManager.getDefaultSharedPreferences(MusicApp.context).edit().apply {
                    putString("useravatar", it.url)
                    apply()
                }
                isUpLoadSuccess.value = true
            }, {
                isUpLoadSuccess.value = false
            })


        //与当前用户关联
        MusicApp.currentUser.apply {
            put("avatar", file)
            saveInBackground()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    bitmap.recycle()
                })
        }
    }

    /**
     * 上传昵称
     */
    @SuppressLint("CheckResult")
    fun changeName(newName: String) {
        mUserName.set(newName)
        if (!checkStringIsNull(newName)) {
            MusicApp.currentUser.put("username", newName)
            MusicApp.currentUser.saveInBackground().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    PreferenceManager.getDefaultSharedPreferences(MusicApp.context).edit().apply {
                        putString("username", newName)
                        apply()
                    }
                }
        }
    }

    /**
     * 添加创建的歌单到云端
     * @param 歌单名字
     */
    @SuppressLint("CheckResult")
    fun addSongList(mName: String) {

        isShowProcessPar.value = true
        AVObject("SongList").apply {
            put("name", mName)
            put("creatorAvatar",MusicApp.currentUser.getAVFile("avatar")?.url)
            put("owner", MusicApp.currentUser)
            put("creatorName",MusicApp.currentUser.username)
            saveInBackground().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //保存到本地数据库
                    val songList = SongList()
                    songList.apply {
                        objectId = it.objectId
                        name = mName
                        isNetSongList = 0
                        save()
                    }
                    //刷新列表
                    getSonList()
                    isShowProcessPar.value = false
                    toast.value = "添加成功"

                }, {
                    isShowProcessPar.value = false
                    toast.value = "添加失败"
                    Log.d(TAG,it.printStackTrace().toString())

                })
        }
    }

    /**
     * 删除创建的歌单
     */
    @SuppressLint("CheckResult")
    fun deleteSongList(songList: SongList, position: Int){
        //本地删除
        LitePal.deleteAll(SongList::class.java,"objectId = ?",songList.objectId)
        if (songList.isNetSongList==0) {
            //删除创建的歌单
            creatSongList.value?.removeAt(position)
            //云端删除
            AVObject.createWithoutData("SongList", songList.objectId).deleteInBackground().subscribe {
                toast.value = "删除成功"
            }
        }else{
            //删除收藏的歌单
            collectSongList.value?.removeAt(position)
            AVObject.createWithoutData("CollectSongList", songList.objectId).deleteInBackground().subscribe {
                toast.value = "删除成功"
            }
        }
    }



    /**
     * 接收音乐数量
     */
    @Subscribe
    fun reshreshNum(event: MusicNumEvene) {
        mLocalMusicNum.set(event.localMusicNum)
        //存储音乐数量
        PreferenceManager.getDefaultSharedPreferences(MusicApp.context).edit().apply {
            putInt("localmusicnum", event.localMusicNum)
            apply()
        }
    }

    /**
     * 接收歌单列表
     */
    @Subscribe
    fun loadSongList(event: SongListEvent) {
        when(event.tag) {
            "creat" -> {
                Log.d(TAG, "接收到了创建歌单")
                creatSongList.value = event.list
            }
            "collect" -> {
                Log.d(TAG, "接收到了收藏歌单")
                collectSongList.value = event.list
            }
        }

    }

    /**
     * 添加到某歌单后刷新列表
     */
    @Subscribe
    fun refreshSongList(event: RefreshSongList){
        when(event.tag){
            "creat"-> getSonList()
            "collect"-> getCollectSongList()
        }

    }



    /**
     * 展开/关闭歌单
     */
    fun controlCreatedList() {
        isShowCreatedList.value = !isShowCreatedList.value!!
    }

    fun controlCollectList() {
        isShowCollectList.value = !isShowCollectList.value!!
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

}