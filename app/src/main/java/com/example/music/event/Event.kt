package com.example.music.event

import com.example.music.PlayManger
import com.example.music.bean.*
import com.example.music.db.table.BannerTable
import com.example.music.db.table.LocalMusic
import com.example.music.db.table.SongList

/**
 * Created by tk on 2019/8/20
 */

class BanberEvent(val list: ArrayList<BannerTable>)

//只显示6条的歌单
class HotSongListLimit(val list: ArrayList<Playlists>?)

//分类歌单
class AssortedSongListEvent(val tag: String,val list: ArrayList<Playlists>)

//最新单曲

class LatestSongEvent(val list: ArrayList<Data>)

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

class RefreshEvent(val mSong: LocalMusic,val position: Int)

//更新进度条事件
class ProcessEvent(val tag: String,val num: Int)

//播放状态事件
class StateEvent(val state: PlayManger.State)

//播放位置事件
class IndexEvent(val index:Int)