package com.example.music.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.music.bean.LocalMusic
import com.example.music.databinding.RecycleItemLocalMusicBinding
import com.example.music.R
import com.example.music.activity.PlayingActivity
import org.jetbrains.anko.startActivity

/**
 * Created by tk on 2019/8/17
 * 本地音乐列表适配器
 */
class LocalMusicAdapter(val list: List<LocalMusic>,val context: Context) : RecyclerView.Adapter<LocalMusicAdapter.ViewHolder>(){
    //正在播放的位置
    var playingId = -1
    var listener: OnPopMoreClickListener? = null

    interface OnPopMoreClickListener {
        fun onPopMoreClick(music: LocalMusic)
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
            if (playingId == p1) {
                //第二次点击跳转至详情页
                context.startActivity<PlayingActivity>("music" to list[p1])
            }else{
                val lastPlayingId = playingId
                playingId = p1
                notifyItemChanged(playingId)
                notifyItemChanged(lastPlayingId)
            }
        }

        //点击右边的弹出更多操作，删除，或添加到歌单
        p0.itembinding.ivPopMore.setOnClickListener {
            listener?.onPopMoreClick(list[p1])
        }
    }


    class ViewHolder(val itembinding: RecycleItemLocalMusicBinding) : RecyclerView.ViewHolder(itembinding.root) {
        fun bind(localMusic: LocalMusic){
            itembinding.localmusic = localMusic
        }

    }
}