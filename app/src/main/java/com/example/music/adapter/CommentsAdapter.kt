package com.example.music.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.music.MusicApp
import com.example.music.model.db.bean.Comment
import com.example.music.databinding.RecycleItemCommentBinding
import com.example.music.R

/**
 * Created by tk on 2019/9/5
 * 评论适配器
 */
class CommentsAdapter(val list: ArrayList<Comment>):RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    val TAG = "CommentsAdapter"

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: RecycleItemCommentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(p0.context)
            ,R.layout.recycle_item_comment
            ,p0,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.binding.comment = list[p1]

    }

    class ViewHolder(val binding: RecycleItemCommentBinding): RecyclerView.ViewHolder(binding.root)
}