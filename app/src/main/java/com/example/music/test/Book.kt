package com.example.music.test

import org.litepal.crud.LitePalSupport

/**
 * Created by tk on 2019/8/31
 */
class Book : LitePalSupport(){
    var name: String? = null
    var price: Int? = null
    var authorList = ArrayList<Author>()
}