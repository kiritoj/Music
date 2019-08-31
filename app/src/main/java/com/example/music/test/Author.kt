package com.example.music.test

import org.litepal.crud.LitePalSupport

/**
 * Created by tk on 2019/8/31
 */
class Author : LitePalSupport() {
    var name: String?= null
    var age: Int? = null
    var list = ArrayList<Book>()

    override fun equals(other: Any?): Boolean {
        val otherAuthor = other as Author
        return name.equals(otherAuthor.name)
    }
}