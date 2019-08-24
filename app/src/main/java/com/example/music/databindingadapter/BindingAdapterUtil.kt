package com.example.music.databindingadapter

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.example.music.MusicApp
import com.example.music.R
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * Created by tk on 2019/8/16
 * BindingAdapter实现自定义属性
 */

/**
 * 自定义imageUrl属性使用Glide加载图片
 */
object ImageBindAdapter{
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView,url: String?){
        Glide.with(MusicApp.context)
            .load(url)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_loading_error)
            .into(view)
    }
}

/**
 * background_utl高斯模糊加载viewgroup背景
 */

object BackgroundBindAdapter{
    @BindingAdapter("background_utl")
    @JvmStatic
    fun loadBackground(view: ImageView,url: String){
        Glide.with(MusicApp.context)
            .load(url)
            .bitmapTransform(BlurTransformation(MusicApp.context, 30, 5))
            .into(view)
    }
}





/**
 *自定义属性专辑id加载专辑图片
 */
object BitmapBindAdapter{
    @BindingAdapter("albumId")
    @JvmStatic
    fun setBitmap(view: ImageView,id: Int){
        view.setImageBitmap(getAlbumArt(id,MusicApp.context))
    }
}



fun getAlbumArt(album_id: Int,context: Context) : Bitmap {
    val mUriAlbums = "content://media/external/audio/albums"
    val projection = arrayOf("album_art")
    val cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)), projection, null, null, null)
    var album_art: String? = null;
    if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
        cur.moveToNext()
        album_art = cur.getString(0)
    }
    cur.close()
    var bm: Bitmap?
    if (album_art != null) {
        bm = BitmapFactory.decodeFile(album_art)
    } else {
        bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_loading_error)
    }
    return bm
}

//自定义属性播放数量，计算成万
object PlayCount{
    @BindingAdapter("playcount")
    @JvmStatic
    fun setplaycount(view: TextView,count: Int){
        if (count<10000){
            view.setText(count.toString())
        }else{
            view.setText((count.toFloat()/10000f).toString().substring(0,3)+"万")
        }
    }
}