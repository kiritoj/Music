package com.example.music.util

import android.graphics.drawable.Drawable
import android.support.constraint.Placeholder
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.music.MusicApp
import com.example.music.R

/**
 * Created by tk on 2019/9/8
 * 图片加载工具类
 */
object GlideUtil {
    /**
     * 加载普通图片
     */
    fun loadPic(view: ImageView,url: String,mPlaceholder: Drawable,mError: Drawable){
        val requestOptions = RequestOptions()
            .placeholder(mPlaceholder)
            .error(mError)
            .dontAnimate()
        Glide.with(MusicApp.context)
            .load(url)
            .apply(requestOptions)
            .into(view)
    }

    /**
     * 加载圆角图片
     */
    fun loadCornerPic(view: ImageView,url: String,mPlaceholder: Drawable,mError: Drawable,radius: Int){
        val requestOptions = RequestOptions
            .bitmapTransform(RoundedCorners(radius))
            .placeholder(mPlaceholder)
            .error(mError)
            .dontAnimate()
        Glide.with(MusicApp.context)
            .load(url)
            .apply(requestOptions)
            .into(view)
    }

    /**
     * 加载模糊图片
     */
    fun loadBlurredPic(view: ImageView,url: String,mPlaceholder: Drawable,mError: Drawable,radius: Int,sampling: Int){
        val requestOptions = RequestOptions.bitmapTransform(BlurTransformation(radius,sampling))
            .placeholder(mPlaceholder)
            .error(mError)
            .dontAnimate()
        Glide.with(MusicApp.context)
            .load(url)
            .apply(requestOptions)
            .into(view)
    }
}