package com.example.music.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.music.IMAGE_BASE_URL
import com.example.music.R
import com.example.music.SONG_PLAY_BASE_URL
import com.example.music.activity.PlayingActivity
import com.example.music.bean.SongsBean
import com.example.music.databinding.RecycleItemSongsBinding
import com.example.music.db.table.LocalMusic
import com.example.music.event.QueneEvent
import com.example.music.event.SongEvent
import org.jetbrains.anko.startActivity
import org.greenrobot.eventbus.EventBus

/**
 * Created by tk on 2019/8/23
 */
class SongsAdapter(val list: ArrayList<SongsBean.DataBean.TracksBean>, val context: Context) :
    RecyclerView.Adapter<SongsAdapter.ViewHolder>() {
    //正在播放的位置
    var playingId = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: RecycleItemSongsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(p0.context)
            , R.layout.recycle_item_songs
            , p0
            , false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        if (playingId == p1) {
            p0.itembinding.ivPlaying.visibility = View.VISIBLE

        } else {
            p0.itembinding.ivPlaying.visibility = View.GONE

        }
        p0.bind(list[p1])

        //点击整体播放
        p0.itembinding.llSongRoot.setOnClickListener {
            //包装成localMusic类播放
            val quene =  ArrayList<LocalMusic>()
            list.forEach {
                val music = LocalMusic()
                music.apply {
                    id = it.id
                    songName = it.name
                    singerName = it.artists!![0].name
                    url = SONG_PLAY_BASE_URL+it.id
                    coverUrl = IMAGE_BASE_URL+it.id
                }
                quene.add(music)
            }

            if (playingId == p1) {
                //第二次点击跳转至详情页
                context.startActivity<PlayingActivity>()
            } else {
                val lastPlayingId = playingId
                playingId = p1
                notifyItemChanged(playingId)
                notifyItemChanged(lastPlayingId)

                EventBus.getDefault().post(QueneEvent(quene,p1))
            }
        }

        //点击右边的弹出更多操作，删除，或添加到歌单
        p0.itembinding.ivPopMore.setOnClickListener {
            EventBus.getDefault().post(SongEvent("onlineSong",onlineSong = list[p1]))
        }

    }

    fun refreshPlayId(newPlayId: Int){
        val lastId = playingId
        playingId = newPlayId
        notifyItemChanged(playingId)
        notifyItemChanged(lastId)
    }


    class ViewHolder(val itembinding: RecycleItemSongsBinding) : RecyclerView.ViewHolder(itembinding.root) {
        fun bind(mSong: SongsBean.DataBean.TracksBean) {
            itembinding.song = mSong
        }

    }
}
