package com.example.music.view.fragment

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
import com.example.music.view.activity.TopListActivity
import com.example.music.view.activity.SongListActivity
import com.example.music.adapter.BinnerAdapter
import com.example.music.adapter.LatestSongAdapter
import com.example.music.adapter.SongListAdapter
import com.example.music.event.RefreshEvent
import com.example.music.viewmodel.FindFragmentVM
import kotlinx.android.synthetic.main.fragment_find.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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
    val latestSongAdapter by lazy { LatestSongAdapter(ArrayList(),context!!,"newSong") }

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
        refresh_layout.setOnRefreshListener {
            mViewModel.refresh()
            refresh_layout.isRefreshing = false
        }

    }

    /**
     * 部分控件的点击事件
     */
    fun click(){
        //跳转至歌单activity
        ll_songlist.setOnClickListener {
            context?.startActivity<SongListActivity>()
        }
        //跳转至排行榜activity
        ll_top.setOnClickListener {
            context?.startActivity<TopListActivity>()
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
            process_bar.visibility = View.GONE
        })
        mViewModel.latestSong.observe(this, Observer {
            latestSongAdapter.list.clear()
            latestSongAdapter.list.addAll(it!!)
            latestSongAdapter.notifyDataSetChanged()
            new_music_process_bar.visibility = View.GONE
        })

        //加载歌单失败显示
        mViewModel.showError1.observe(this, Observer {
            if (it!!) {
                tv_error_songlist.visibility = View.VISIBLE
                process_bar.visibility = View.GONE
            }else{
                tv_error_songlist.visibility = View.GONE
            }
        })

        //加载新歌失败提示
        mViewModel.showError2.observe(this, Observer {
            if (it!!){
                tv_error_song.visibility = View.VISIBLE
                new_music_process_bar.visibility = View.GONE
            }else{
                tv_error_song.visibility = View.GONE
            }
        })

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
            /**
             * 播放其他歌曲时计时刷新UI
             */
    fun refresh(event: RefreshEvent){
        latestSongAdapter.refreshPlayidWithTag(event.position)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}