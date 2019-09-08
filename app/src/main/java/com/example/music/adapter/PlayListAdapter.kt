package com.example.music.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.music.MusicApp
import com.example.music.R
import com.example.music.databinding.RecycleItemPlaylistBinding
import com.example.music.model.db.table.LocalMusic
import com.example.music.event.IndexEvent
import org.greenrobot.eventbus.EventBus

/**
 * Created by tk on 2019/9/6
 * 播放队列适配器
 */
class PlayListAdapter(val list: ArrayList<LocalMusic>, var playId: Int) :
    RecyclerView.Adapter<PlayListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: RecycleItemPlaylistBinding = DataBindingUtil.inflate(
            LayoutInflater.from(p0.context)
            , R.layout.recycle_item_playlist
            , p0, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        if (playId == p1) {
            MusicApp.context.resources.getColor(R.color.red).let {
                p0.binding.tvSongName.setTextColor(it)
                p0.binding.tvDivide.setTextColor(it)
                p0.binding.tvSingerName.setTextColor(it)
            }
        }else{
            MusicApp.context.resources.let {
                p0.binding.tvSongName.setTextColor(it.getColor(R.color.black))
                p0.binding.tvDivide.setTextColor(it.getColor(R.color.lightGray))
                p0.binding.tvSingerName.setTextColor(it.getColor(R.color.lightGray))
            }
        }
        p0.binding.song = list[p1]
        //点击播放
        p0.binding.llPlaylistRoot.setOnClickListener {
            if (playId != p1){
                setId(p1)
                EventBus.getDefault().post(IndexEvent(p1))
            }
        }

    }

    class ViewHolder(val binding: RecycleItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setId(newId: Int) {
        val lastPlayId = playId
        playId = newId
        notifyItemChanged(lastPlayId)
        notifyItemChanged(playId)

    }
}