package com.example.music.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.music.PlayManger
import com.example.music.R
import com.example.music.view.activity.PlayingActivity
import com.example.music.model.db.bean.Data
import com.example.music.databinding.RecycleItemLatesMusicBinding
import com.example.music.model.db.table.LocalMusic
import com.example.music.event.IndexEvent
import com.example.music.event.QueneEvent
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity

/**
 * Created by tk on 2019/8/20
 */
class LatestSongAdapter(val list: ArrayList<Data>, val context: Context, val tag: String) :
    RecyclerView.Adapter<LatestSongAdapter.ViewHolder>() {
    var playingId = -1
    val quene = ArrayList<LocalMusic>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: RecycleItemLatesMusicBinding = DataBindingUtil
            .inflate(LayoutInflater.from(p0.context), R.layout.recycle_item_lates_music, p0, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        if (playingId == p1) {
            p0.binding.ivPlaying.visibility = View.VISIBLE

        } else {
            p0.binding.ivPlaying.visibility = View.GONE

        }
        p0.binding.song = list[p1]
        //点击整体播放
        p0.binding.llLatestSongRoot.setOnClickListener {

            if (tag.equals(PlayManger.queneTag)) {
                if (playingId == p1) {
                    //第二次点击跳转至详情页
                    context.startActivity<PlayingActivity>()
                } else {
                    refreshPlayId(p1)
                    EventBus.getDefault().post(IndexEvent(p1))
                }
            } else {

                if (playingId == p1) {
                    //第二次点击跳转至详情页
                    context.startActivity<PlayingActivity>()
                } else {
                    refreshPlayId(p1)
                    //没有发送将网络获取的歌曲包装成LocalMusic类
                    list.forEach {
                        val music = LocalMusic()
                        music.apply {
                            musicId = it.id
                            songName = it.name
                            singerName = it.album.artists[0].name
                            coverUrl = it.album.picUrl
                            length = it.duration
                            tag = "NET_NON_URL"
                        }
                        quene.add(music)
                    }
                    EventBus.getDefault().post(QueneEvent(quene, p1, tag))
                }

            }


        }
    }

    //更新播放位置
    fun refreshPlayId(newPlayId: Int) {

        val lastId = playingId
        playingId = newPlayId
        notifyItemChanged(playingId)
        notifyItemChanged(lastId)

    }

    fun refreshPlayidWithTag(newPlayId: Int) {
        if (tag.equals(PlayManger.queneTag)) {
            refreshPlayId(newPlayId)
        } else {
            //与tao不符说明该播放队列不是正在播放的队列
            val lastId = playingId
            playingId = -1
            notifyItemChanged(lastId)
        }
    }

//        //点击右边的弹出更多操作，删除，或添加到歌单
//        p0.itembinding.ivPopMore.setOnClickListener {
//            listener?.onPopMoreClick(list[p0.adapterPosition],p0.adapterPosition)
//        }


    class ViewHolder(val binding: RecycleItemLatesMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}