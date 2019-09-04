package com.example.music.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.music.PlayManger
import com.example.music.db.table.LocalMusic
import com.example.music.databinding.RecycleItemLocalMusicBinding
import com.example.music.R
import com.example.music.activity.PlayingActivity
import com.example.music.event.IndexEvent
import com.example.music.event.QueneEvent
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity


/**
 * Created by tk on 2019/8/17
 * 本地音乐列表适配器
 * @param tag 该标志位adapter所处的activity或fragment
 * 用于后台播放服务切换播放队列时不同的tag的adapter根据tag更新自己正在播放的位置
 */
class LocalMusicAdapter(val list: ArrayList<LocalMusic>,val context: Context,val tag: String) : RecyclerView.Adapter<LocalMusicAdapter.ViewHolder>(){
    //正在播放的位置
    var playingId = -1
    var listener: OnPopMoreClickListener? = null

    interface OnPopMoreClickListener {
        fun onPopMoreClick(music: LocalMusic,position: Int)

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding : RecycleItemLocalMusicBinding= DataBindingUtil.inflate(LayoutInflater.from(p0.context)
            ,R.layout.recycle_item_local_music
            ,p0
            ,false)

        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        if (playingId == p1){
            p0.itembinding.ivPlaying.visibility = View.VISIBLE

        }else{
            p0.itembinding.ivPlaying.visibility = View.GONE

        }
        p0.bind(list[p1])

        //点击整体播放
        p0.itembinding.llLocalMusicParent.setOnClickListener {
            //检查该播放队列是不是player正在播放的队列
            if (tag.equals(PlayManger.queneTag)){
                if (playingId == p1) {
                    //第二次点击跳转至详情页
                    context.startActivity<PlayingActivity>()
                } else {
                    refreshPlayId(p1)
                    EventBus.getDefault().post(IndexEvent(p1))
                }
            }else{
                //与player的播放队列不符，重新发送播放队列
                if (playingId == p1) {
                    //第二次点击跳转至详情页
                    context.startActivity<PlayingActivity>()
                }else{
                    refreshPlayId(p1)
                    EventBus.getDefault().post(QueneEvent(list,p1,tag))
                }
            }

        }

        //点击右边的弹出更多操作，删除，或添加到歌单
        p0.itembinding.ivPopMore.setOnClickListener {
            listener?.onPopMoreClick(list[p0.adapterPosition],p0.adapterPosition)
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
     * 删除某一条
     */
    fun delete(position: Int){
        list.removeAt(position)
        if (position == playingId){
            playingId = -1
        }
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,list.size-position)
    }


    class ViewHolder(val itembinding: RecycleItemLocalMusicBinding) : RecyclerView.ViewHolder(itembinding.root) {
        fun bind(localMusic: LocalMusic){
            itembinding.localmusic = localMusic
        }

    }
}