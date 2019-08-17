package com.example.music.databindingadapter

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.music.MusicApp

/**
 * Created by tk on 2019/8/16
 */

object ImageBindAdapter{
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView,url: String?){
        Glide.with(MusicApp.context).load(url).into(view)
    }
}