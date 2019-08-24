package com.example.music.db.table

import org.litepal.crud.LitePalSupport

/**
 * Created by tk on 2019/8/21
 * 存储歌单种类
 */
class CatTable : LitePalSupport(){
    var catList : ArrayList<String>? = null //默认歌单种类
    var lunguageList: ArrayList<String>? = null //语种
    var styleList: ArrayList<String>? = null //风格
    var placeList: ArrayList<String>? = null //场景
    var emotionList: ArrayList<String>? = null //情感
    var themeList: ArrayList<String>? = null //主题
}