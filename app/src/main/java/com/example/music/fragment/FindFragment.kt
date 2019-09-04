package com.example.music.fragment

import android.arch.lifecycle.Observer
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.music.R
import com.example.music.activity.SongListActivity
import com.example.music.adapter.BinnerAdapter
import com.example.music.adapter.LatestSongAdapter
import com.example.music.adapter.SongListAdapter
import com.example.music.event.RefreshEvent
import com.example.music.viewmodel.FindFragmentVM
import kotlinx.android.synthetic.main.fragment_find.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.startActivity

/**
 * Created by tk on 2019/8/16
 */
class FindFragment : Fragment() {

    val mViewModel by lazy { FindFragmentVM() }

    //轮播图适配器
    lateinit var mAdapter: BinnerAdapter
    //精品歌单适配器
    val songListAdapter by lazy { SongListAdapter(ArrayList(),context!!) }
    //新歌速递适配器
    val latestSongAdapter by lazy { LatestSongAdapter(ArrayList(),context!!) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_find, container, false)
        EventBus.getDefault().register(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        click()
        rv_hot_songlist_limit.apply {
            adapter = songListAdapter
            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.set(10, 5, 10, 5)
                }
            })
        }

        rv_latest_song.apply {
            adapter = latestSongAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        }
        observe()

        //刷新
        refresh_layout.setColorSchemeResources(R.color.yellow)
        refresh_layout.setOnRefreshListener { mViewModel.getHotSongList()
            refresh_layout.isRefreshing = false
        }

    }

    /**
     * 部分控件的点击事件
     */
    fun click(){
        ll_songlist.setOnClickListener {
            context?.startActivity<SongListActivity>()
        }
    }


    fun observe() {
        mViewModel.listBanner.observe(this, Observer {
            mAdapter = BinnerAdapter(it!!, context)
            auto_scroll_layout.setAdapter(mAdapter)
        })
        mViewModel.songList.observe(this, Observer {
            songListAdapter.list.clear()
            songListAdapter.list.addAll(it!!)
            songListAdapter.notifyDataSetChanged()
            //process_bar.visibility = View.GONE
        })
        mViewModel.latestSong.observe(this, Observer {
            latestSongAdapter.list.clear()
            latestSongAdapter.list.addAll(it!!)
            latestSongAdapter.notifyDataSetChanged()
            new_music_process_bar.visibility = View.GONE
        })


    }

    @Subscribe
            /**
             * 播放其他歌曲时计时刷新UI
             */
    fun refresh(event: RefreshEvent){
        latestSongAdapter.refreshPlayId(event.position)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}