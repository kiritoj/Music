package com.example.music.event

import com.example.music.model.db.table.SongList

/**
 * Created by tk on 2019/8/18
 */
class SongListEvent(val tag:String,val list: ArrayList<SongList>) {
}
