package com.example.music.event

import com.example.music.bean.LastMusicBean
import com.example.music.bean.SongListBean
import com.example.music.bean.SongsBean
import com.example.music.db.table.BannerTable
import com.example.music.db.table.LocalMusic
import com.example.music.db.table.SongList

/**
 * Created by tk on 2019/8/20
 */

class BanberEvent(val list: ArrayList<BannerTable>)

//只显示6条的歌单
class HotSongListLimit(val list: ArrayList<SongListBean.DataBean>?)

//分类歌单
class AssortedSongListEvent(val tag: String,val list: ArrayList<SongListBean.DataBean>)

//最新单曲

class LatestSongEvent(val list: ArrayList<LastMusicBean.DataBean>)

//歌单里面的全部歌曲

class SongsEvent(val tag:String,val list: ArrayList<SongsBean.DataBean.TracksBean>?=null,
                 val mList: ArrayList<LocalMusic>? = null)

//通知更新歌单

class StringEvent(val tag: String)

//
class SongEvent(val tag: String,val creatSong: LocalMusic? = null,val onlineSong: SongsBean.DataBean.TracksBean?=null,val position: Int=0)

//播放队列事件

class QueneEvent(val list: ArrayList<LocalMusic>,val index: Int)

//播放模式事件
class ModeEvent(val mode: Int)

class RefreshEvent(val song: LocalMusic,val position: Int)

//更新进度条事件
class ProcessEvent(val current: Int,val end: Int)