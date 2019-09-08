package com.example.music.databindingadapter

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView
import com.example.music.MusicApp
import com.example.music.PlayManger
import com.example.music.R
import com.example.music.util.TimeUtil
import com.example.music.view.activity.customveiw.LrcView
import com.example.music.model.db.table.LocalMusic
import com.example.music.util.GlideUtil


/**
 * Created by tk on 2019/8/16
 * BindingAdapter实现自定义属性
 */

/**
 * 自定义imageUrl属性使用Glide加载图片
 */
object ImageBindAdapter {
    @BindingAdapter("url", "placeholder", "error", requireAll = true)
    @JvmStatic
            /**
             * 加载普通图片
             */
    fun loadImage(view: ImageView, url: String?, placeholder: Drawable, error: Drawable) {
        url?.let { GlideUtil.loadPic(view, it, placeholder, error) }
    }

    @BindingAdapter("url", "placeholder", "error", "radius", requireAll = true)
    @JvmStatic
            /**
             * 加载圆角图片
             */
    fun loadCornerImage(
        view: ImageView,
        url: String?,
        placeholder: Drawable,
        error: Drawable,
        radius: Int
    ) {

        url?.let { GlideUtil.loadCornerPic(view, it, placeholder, error, radius) }
    }

    @BindingAdapter("url", "placeholder", "error", "tag", "radius", "sampling", requireAll = true)
    @JvmStatic
            /**
             * 加载模糊图片
             */
    fun loadBlurredImage(
        view: ImageView,
        url: String?,
        placeholder: Drawable,
        error: Drawable,
        tag: Int,
        radius: Int,
        sampling: Int
    ) {
        url?.let { GlideUtil.loadBlurredPic(view, it, placeholder, error, radius, sampling) }
    }
}


/**
 * 本地歌曲和网络歌曲加载图片
 */
object SongAlbumAdapter {
    @BindingAdapter("song")
    @JvmStatic
    fun setImag(view: ImageView, song: LocalMusic?) {
        if (song != null) {
            when (song.tag) {
                "LOCAL" ->
                    //如果是本地歌曲。直接设置专辑bitmap
                    view.setImageBitmap(song.albumID?.let { getAlbumArt(it, MusicApp.context) })
                "NET_WITH_URL" -> {
                    view.setImageResource(R.drawable.disk)
                }
                "NET_NON_URL" ->
                    //否则从网络加载
                    GlideUtil.loadPic(view,song.coverUrl
                        ,ContextCompat.getDrawable(MusicApp.context,R.drawable.disk)!!
                        ,ContextCompat.getDrawable(MusicApp.context,R.drawable.disk)!!)
            }
        }
    }
}

/**
 * imageview通过资源id设置图片
 */
object ImageAdapter {
    @BindingAdapter("id")
    @JvmStatic
    fun setImageById(view: ImageView, id: Int) {
        if (id == 0) {
            // view.setImageResource(R.drawable.vector_drawable_play_black)
        } else {
            view.setImageResource(id)
        }

    }
}


/**
 *自定义属性专辑id加载专辑图片
 */
object BitmapBindAdapter {
    @BindingAdapter("albumId")
    @JvmStatic
    fun setBitmap(view: ImageView, id: Int) {
        view.setImageBitmap(getAlbumArt(id, MusicApp.context))
    }
}


fun getAlbumArt(album_id: Int, context: Context): Bitmap {
    val mUriAlbums = "content://media/external/audio/albums"
    val projection = arrayOf("album_art")
    val cur = context.getContentResolver().query(
        Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
        projection,
        null,
        null,
        null
    )
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
object PlayCount {
    @BindingAdapter("playcount")
    @JvmStatic
    fun setplaycount(view: TextView, count: Int) {
        if (count < 10000) {
            view.setText(count.toString())
        } else {
            view.setText(String.format("%.1f",(count.toFloat() / 10000f))+"万")
        }
    }
}


//歌词控件设置
object LrcText {
    @BindingAdapter("text")
    @JvmStatic
    fun init(lrcView: LrcView, text: String?) {
        text?.let { lrcView.setLrc(it) }
        lrcView.bindPlayer(PlayManger.player)
    }
}

//时间戳得到时间
object Time {
    @BindingAdapter("timeStamp")
    @JvmStatic
    fun getTime(view: TextView, timeStamp: Long) {
        view.setText(TimeUtil.timestampToTime(timeStamp))
    }
}