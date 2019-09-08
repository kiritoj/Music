package com.example.music.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.music.R
import com.example.music.model.db.table.SongList
import com.example.music.databinding.RecycleItemCreatedSonglistBinding

/**
 * Created by tk on 2019/8/18
 */
class CollectToSongListAdapter(val list: ArrayList<SongList>, val context: Context) : RecyclerView.Adapter<CollectToSongListAdapter.ViewHolder>() {

    var listener: OnSongListClickListener?=null

    //向外暴露一个接口
    interface OnSongListClickListener{
        fun onSongListClick(sonList: SongList)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            val binding = DataBindingUtil
                .inflate<RecycleItemCreatedSonglistBinding>(
                    LayoutInflater.from(p0.context)
                    , R.layout.recycle_item_created_songlist,p0,false)

            return ViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
            p0.bind(list[p1])
            p0.binding.llCreatSonglistRoot.setOnClickListener {listener?.onSongListClick(list[p1])}

        }

        class ViewHolder(val binding: RecycleItemCreatedSonglistBinding) : RecyclerView.ViewHolder(binding.root){
            fun bind(sonList: SongList){
                binding.songlist = sonList
            }
        }
}