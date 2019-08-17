package com.example.music.repository

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import com.example.music.bean.LocalMusic
import com.example.music.viewmodel.LoginVM
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal
import org.litepal.tablemanager.Connector

/**
 * Created by tk on 2019/8/17
 * 获取本地音乐
 */
class LocalMusicRepository {

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
    var list = ArrayList<LocalMusic>()
    fun getLocalMusic(context: Context){
        //查询是耗时操作，放在子线程执行


         object : AsyncTask<Unit, Unit, Boolean>() {
            override fun doInBackground(vararg params: Unit?): Boolean {
                hasGetFromLocal =
                    PreferenceManager.getDefaultSharedPreferences(context).getBoolean("hasGetFromLocal", false)
                /**
                 * 从本地获取音乐
                 */
                if (!hasGetFromLocal) {

                    //第一次查询建表
                    LitePal.getDatabase()

                    val cursor = context.getContentResolver()
                        .query(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                            null, MediaStore.Audio.AudioColumns.IS_MUSIC
                        )
                    Log.d("testzhixing", "本地查询")

                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            val music = LocalMusic()
                            music.songName =
                                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                            music.singerName =
                                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                            music.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                            music.length = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                            music.albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
//                    if (song.size > 1000 * 800) {
//                        // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
//                        if (song.song.contains("-")) {
//                            String[] str = song.song.split("-");
//                            song.singer = str[0];
//                            song.song = str[1];
//                        }
//                        list.add(song);
//                    }

                            list.add(music)
                            //添加进数据库
                            music.save()
                        }
                        Log.d("testzhixing", list[0].toString())
                        // 释放资源
                        cursor.close()
                        PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
                            putBoolean("hasGetFromLocal", true)
                            apply()
                        }
                    }
                } else {
                    /**
                     * 从数据库获取
                     *
                     */
                    Log.d("testzhixing", "数据库查询")
                    list = LitePal.findAll(LocalMusic::class.java) as ArrayList<LocalMusic>

                }
                return true
            }

            override fun onPostExecute(result: Boolean?) {
                EventBus.getDefault().post(list)
            }
        }.execute()

    }
}