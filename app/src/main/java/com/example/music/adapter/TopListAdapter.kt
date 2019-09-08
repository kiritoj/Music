package com.example.music.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.music.R
import com.example.music.view.activity.SongListDetailActivity
import com.example.music.model.db.bean.X
import com.example.music.databinding.RecycleItemToplistBinding
import com.example.music.model.db.table.SongList
import org.jetbrains.anko.startActivity

/**
 * Created by tk on 2019/9/7
 * 榜单适配器
 */
class TopListAdapter(var list: List<X>, val context: Context) : RecyclerView.Adapter<TopListAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: RecycleItemToplistBinding = DataBindingUtil.inflate(
            LayoutInflater.from(p0.context)
            ,R.layout.recycle_item_toplist
            ,p0,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.binding.toplist = list[p1]
        p0.binding.root.setOnClickListener {
            val sonList = SongList()
            sonList.apply {
                coverUrl = list[p1].coverImgUrl
                name = list[p1].name
                description = list[p1].description
                netId = list[p1].id

            }
            context.startActivity<SongListDetailActivity>("songlist" to sonList)
        }

    }

    class ViewHolder(val binding: RecycleItemToplistBinding): RecyclerView.ViewHolder(binding.root)

    fun setData(mList: List<X>){
        list = mList
        notifyDataSetChanged()
    }
}