package com.example.music.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.music.IMAGE_BASE_URL
import com.example.music.bean.LastMusicBean
import com.example.music.databinding.RecycleItemLatesMusicBinding
import com.example.music.R
import com.example.music.SONG_PLAY_BASE_URL
import com.example.music.activity.PlayingActivity
import com.example.music.db.table.LocalMusic
import com.example.music.event.QueneEvent
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity

/**
 * Created by tk on 2019/8/20
 */
class LatestSongAdapter(val list: ArrayList<LastMusicBean.DataBean>,val context: Context): RecyclerView.Adapter<LatestSongAdapter.ViewHolder>() {
    var playingId = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: RecycleItemLatesMusicBinding = DataBindingUtil.inflate(LayoutInflater.from(p0.context),R.layout.recycle_item_lates_music,p0,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        if (playingId == p1) {
            p0.binding.ivPlaying.visibility = View.VISIBLE

        } else {
            p0.binding.ivPlaying.visibility = View.GONE

        }
        p0.binding.song = list[p1].song
        //点击整体播放
        p0.binding.llLatestSongRoot.setOnClickListener {
            val quene = ArrayList<LocalMusic>()
            list.forEach {
                val music = LocalMusic()
                music.apply {
                    id = it.id
                    songName = it.name
                    singerName = it.song?.name
                    url = SONG_PLAY_BASE_URL + it.id
                    coverUrl = IMAGE_BASE_URL + it.id
                }
                quene.add(music)
            }
            if (playingId == p1) {
                //第二次点击跳转至详情页
                context.startActivity<PlayingActivity>("song" to quene[p1])
            } else {
                val lastPlayingId = playingId
                playingId = p1
                notifyItemChanged(playingId)
                notifyItemChanged(lastPlayingId)

                EventBus.getDefault().post(QueneEvent(quene, p1))
            }
        }
    }

        //更新播放位置
        fun refreshPlayId(newPlayId: Int){
            val lastId = playingId
            playingId = newPlayId
            notifyItemChanged(playingId)
            notifyItemChanged(lastId)
        }

//        //点击右边的弹出更多操作，删除，或添加到歌单
//        p0.itembinding.ivPopMore.setOnClickListener {
//            listener?.onPopMoreClick(list[p0.adapterPosition],p0.adapterPosition)
//        }




    class ViewHolder(val binding: RecycleItemLatesMusicBinding): RecyclerView.ViewHolder(binding.root) {

    }
}