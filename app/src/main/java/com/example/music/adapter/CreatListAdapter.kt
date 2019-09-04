package com.example.music.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.music.PlayManger
import com.example.music.R
import com.example.music.activity.PlayingActivity
import com.example.music.databinding.RecycleItemCreatSongsBinding
import com.example.music.db.table.LocalMusic
import com.example.music.event.IndexEvent
import com.example.music.event.QueneEvent
import com.example.music.event.SongEvent
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity


/**
 * Created by tk on 2019/8/24
 */
class CreatListAdapter(val list: ArrayList<LocalMusic>, val context: Context,val tag: String) :
    RecyclerView.Adapter<CreatListAdapter.ViewHolder>() {

    //正在播放的位置
    var playingId = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: RecycleItemCreatSongsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(p0.context)
            , R.layout.recycle_item_creat_songs
            , p0
            , false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(p0: CreatListAdapter.ViewHolder, p1: Int) {
        if (playingId == p1) {
            p0.itembinding.ivPlaying.visibility = View.VISIBLE

        } else {
            p0.itembinding.ivPlaying.visibility = View.GONE

        }
        p0.bind(list[p1])

        //点击整体播放
        p0.itembinding.llSongRoot.setOnClickListener {
            if (tag.equals(PlayManger.queneTag)) {
                //第二次点击跳转至详情页
                if (playingId == p1) {
                    context.startActivity<PlayingActivity>()
                }else{
                    refreshPlayId(p1)
                    EventBus.getDefault().post(IndexEvent(p1))
                }
            } else {
                if (playingId == p1) {
                    context.startActivity<PlayingActivity>()
                }else {
                    refreshPlayId(p1)
                    EventBus.getDefault().post(QueneEvent(list, p1,tag))
                }
            }
        }

        //点击右边的弹出更多操作，删除，或添加到歌单
        p0.itembinding.ivPopMore.setOnClickListener {
            EventBus.getDefault().post(SongEvent("creat", creatSong = list[p1], position = p1))
        }
    }

    fun refreshPlayId(newPlayId: Int){
        if (tag.equals(PlayManger.queneTag)) {
            val lastId = playingId
            playingId = newPlayId
            notifyItemChanged(playingId)
            notifyItemChanged(lastId)
        }else{
            //与tao不符说明该播放队列不是正在播放的队列
            val lastId = playingId
            playingId = -1
            notifyItemChanged(lastId)
        }
    }

    /**
     * 删除某一项
     */
    fun delete(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }


    class ViewHolder(val itembinding: RecycleItemCreatSongsBinding) : RecyclerView.ViewHolder(itembinding.root) {
        fun bind(mSong: LocalMusic) {
            itembinding.song = mSong
        }

    }
}
