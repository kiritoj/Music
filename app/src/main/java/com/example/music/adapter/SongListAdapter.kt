package com.example.music.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.music.databinding.RecycleItemSonglistBinding
import com.example.music.R
import com.example.music.view.activity.SongListDetailActivity
import com.example.music.model.db.bean.Playlists
import com.example.music.model.db.table.SongList
import org.jetbrains.anko.startActivity

/**
 * Created by tk on 2019/8/20
 */
class SongListAdapter(val list:ArrayList<Playlists>, val context: Context) : RecyclerView.Adapter<SongListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding = DataBindingUtil
            .inflate<RecycleItemSonglistBinding>(LayoutInflater.from(p0.context),R.layout.recycle_item_songlist,p0,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.binding.data = list[p1]
        p0.binding.rlSonglistRoot.setOnClickListener {
            //包装成songList类
            val sonList = SongList()
            sonList.apply {
                coverUrl = list[p1].coverImgUrl
                name = list[p1].name
                creatorAvatar = list[p1].creator.avatarUrl
                creatorName = list[p1].creator.nickname
                creatorId = list[p1].creator.userId
                description = list[p1].description
                commentNum = list[p1].commentCount
                collectNum = list[p1].subscribedCount
                netId = list[p1].id
                num = list[p1].trackCount

            }
            context.startActivity<SongListDetailActivity>("songlist" to sonList)
        }
    }

    class ViewHolder(val binding: RecycleItemSonglistBinding) : RecyclerView.ViewHolder(binding.root){

    }
}