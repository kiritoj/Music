package com.example.music.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.music.databinding.RecycleItemMvBinding
import com.example.music.model.bean.MvData
import com.example.music.R
import com.example.music.view.activity.MvDetailActivity
import org.jetbrains.anko.startActivity

/**
 * Created by tk on 2019/9/13
 */
class MvAdapter(val list: ArrayList<MvData>,val context: Context) : RecyclerView.Adapter<MvAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: RecycleItemMvBinding = DataBindingUtil.inflate(
            LayoutInflater.from(p0.context)
            ,R.layout.recycle_item_mv,p0,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.binding.mv = list[p1]
        p0.binding.ivMvCover.setOnClickListener {
            context.startActivity<MvDetailActivity>("mvid" to list[p1].id)
        }


    }

    class ViewHolder(val binding: RecycleItemMvBinding) : RecyclerView.ViewHolder(binding.root)
}