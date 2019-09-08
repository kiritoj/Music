package com.example.music.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.music.model.db.table.SongList
import com.example.music.R
import com.example.music.view.activity.CreatSongListActivity
import com.example.music.view.activity.SongListDetailActivity
import com.example.music.databinding.RecycleItemCreatSonglistWithNavBinding
import org.jetbrains.anko.startActivity

/**
 * Created by tk on 2019/8/18
 * 用户歌单适配器
 */
class UserSongListAdapter(val list: ArrayList<SongList>, val context: Context) : RecyclerView.Adapter<UserSongListAdapter.ViewHolder>() {

    var listener: OnSongListItemMoreListener? = null

    /**
     * 歌单操作
     */
    interface OnSongListItemMoreListener{
        fun onMoreClick(mSongList: SongList, position: Int)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding = DataBindingUtil
            .inflate<RecycleItemCreatSonglistWithNavBinding>(LayoutInflater.from(p0.context)
                ,R.layout.recycle_item_creat_songlist_with_nav,p0,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(list[p1])

        p0.binding.llSonglistRootView.setOnClickListener {
            when(list[p1].isNetSongList){
                1-> context.startActivity<SongListDetailActivity>("songlist" to list[p1])
                0-> context.startActivity<CreatSongListActivity>("songlist" to list[p1])
            }

        }
        p0.binding.ivCreatSongListNav.setOnClickListener {
            listener?.onMoreClick(list[p1],p1)
        }

    }

    /**
     * 删除某项
     */
    fun delete(position: Int){
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,list.size-position)
    }

    class ViewHolder(val binding: RecycleItemCreatSonglistWithNavBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(sonList: SongList){
            binding.songlist = sonList
        }
    }
}