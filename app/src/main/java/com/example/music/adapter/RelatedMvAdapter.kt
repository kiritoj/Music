package com.example.music.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.music.databinding.RecycleItmRelatedMvBinding
import com.example.music.model.bean.MvData
import com.example.music.R
import com.example.music.event.MvClickEvent
import com.example.music.view.activity.MvDetailActivity
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity

/**
 * Created by tk on 2019/9/15
 */
class RelatedMvAdapter(var list: List<MvData>,val context: Context): RecyclerView.Adapter<RelatedMvAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: RecycleItmRelatedMvBinding = DataBindingUtil.inflate(
            LayoutInflater.from(p0.context)
            ,R.layout.recycle_itm_related_mv
            ,p0,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.binding.mv = list[p1]
        p0.binding.root.setOnClickListener {
           EventBus.getDefault().post(MvClickEvent(list[p1]))
        }
    }

    class ViewHolder(val binding: RecycleItmRelatedMvBinding):RecyclerView.ViewHolder(binding.root)
}