package com.example.music.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.music.R
import kotlinx.android.synthetic.main.recycle_item_categray.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by tk on 2019/8/22
 */
class CategrayAdapter(val list: ArrayList<String>): RecyclerView.Adapter<CategrayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.recycle_item_categray,p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemview.tv_cate_name.setText(list[p1])
        p0.itemview.setOnClickListener {
            //点击改变展示的歌单类别
            EventBus.getDefault().post(list[p1])
        }
    }

    class ViewHolder(val itemview: View): RecyclerView.ViewHolder(itemview) {

    }
}