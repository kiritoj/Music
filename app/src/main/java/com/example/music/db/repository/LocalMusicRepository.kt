package com.example.music.db.repository

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import cn.leancloud.AVFile
import com.example.music.MusicApp
import com.example.music.databindingadapter.getAlbumArt
import com.example.music.db.table.LocalMusic
import com.example.music.event.MusicNumEvene
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal
import java.io.ByteArrayOutputStream


/**
 * Created by tk on 2019/8/17
 * 获取本地音乐
 */
class LocalMusicRepository {

    val TAG = "LocalMusicRepository"
    companion object {
        private val localMusicRepository = LocalMusicRepository()
        fun getInstance(): LocalMusicRepository {
            return localMusicRepository
        }
    }

    /**
     * 是否已经从本地获取，否则从数据库获取
     */
    private var hasGetFromLocal = false

    /**
     *音乐列表
     */


    lateinit var disposable: Disposable

    /**
     * 获取本地音乐
     */
    fun getLocalMusic() {
        hasGetFromLocal =
            PreferenceManager.getDefaultSharedPreferences(MusicApp.context).getBoolean("hasGetFromLocal", false)
        if (!hasGetFromLocal) {
            getLocalMusicFromCursor()
        } else {
            getLocalMusicFromDb()
        }
    }



    /**
     * 从内容提供器中获取本地歌曲
     */
    @SuppressLint("CheckResult")
    fun getLocalMusicFromCursor() {
        val list = ArrayList<LocalMusic>()
        disposable = Observable.create(ObservableOnSubscribe<ArrayList<LocalMusic>> {

            //第一次查询要建表
            LitePal.getDatabase()
            val cursor = MusicApp.context.getContentResolver()
                .query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, MediaStore.Audio.AudioColumns.IS_MUSIC
                )
            Log.d(TAG, "本地查询")

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val music = LocalMusic()
                    music.songName =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    music.singerName =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    music.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    music.length = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    music.albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                    music.isLocalMusic = true

                    list.add(music)
                    //添加进数据库
                    music.save()

                }
                it.onNext(list)
                // 释放资源
                cursor.close()
                PreferenceManager.getDefaultSharedPreferences(MusicApp.context).edit().apply {
                    putBoolean("hasGetFromLocal", true)
                    apply()
                }
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                EventBus.getDefault().post(it)
                val event = MusicNumEvene()
                event.localMusicNum = list.size
                EventBus.getDefault().post(event)
            }
    }

    /**
     * 从数据库中获取本地歌曲
     */
    fun getLocalMusicFromDb() {

        disposable = Observable.create(ObservableOnSubscribe<ArrayList<LocalMusic>> { emitter ->
            val list = LitePal.findAll(LocalMusic::class.java) as ArrayList<LocalMusic>
            emitter.onNext(list)
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                EventBus.getDefault().post(it)
                Log.d(TAG, it[0].path)
            }

    }

}



