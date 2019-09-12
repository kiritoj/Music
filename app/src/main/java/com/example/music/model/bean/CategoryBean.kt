package com.example.music.model.bean

import com.google.gson.annotations.SerializedName

/**
 * Created by tk on 2019/8/22
 * 歌单分类
 */
data class CategoryBean(
    val all: All,
    val categories: Categories,
    val code: Int,
    val sub: List<Sub>
)

data class All(
    val activity: Boolean,
    val category: Int,
    val hot: Boolean,
    val imgId: Int,
    val imgUrl: Any,
    val name: String, //种类名字，古风、华语、流行等
    val resourceCount: Int,
    val resourceType: Int,
    val type: Int
)

data class Sub(
    val activity: Boolean,
    val category: Int,
    val hot: Boolean,
    val imgId: Int,
    val imgUrl: Any,
    val name: String,
    val resourceCount: Int,
    val resourceType: Int,
    val type: Int
)

/**
 * 大分类
 * 0 语种
 * 1 风格
 * 2 场景
 * 3 情感
 * 4 主题
 */
data class Categories(
    val `0`: String,
    val `1`: String,
    val `2`: String,
    val `3`: String,
    val `4`: String
)