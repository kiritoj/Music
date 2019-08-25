package com.example.music.activity

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.example.music.*
import com.example.music.adapter.CollectToSongListAdapter
import com.example.music.adapter.SongsAdapter
import com.example.music.databinding.ActivitySongListDetailBinding
import com.example.music.databindingadapter.getAlbumArt
import com.example.music.db.table.LocalMusic
import com.example.music.db.table.SongList
import com.example.music.event.RefreshEvent
import com.example.music.event.SongEvent
import com.example.music.viewmodel.BottomStateBarVM
import com.example.music.viewmodel.SongsVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_song_list.*
import kotlinx.android.synthetic.main.activity_song_list_detail.*
import kotlinx.android.synthetic.main.pop_window_collect_to_songlist.view.*
import kotlinx.android.synthetic.main.pop_window_online_song.view.*
import kotlinx.android.synthetic.main.song_info_button.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import org.jetbrains.anko.startActivity

/**
 * 歌单详情页
 */
class SongListDetailActivity : AppCompatActivity() {
    val TAG = "SongListDetailActivity"
    //其他活动传来的歌单对象，通过该对象获得该歌单里的全部歌曲
    lateinit var mSonglist: SongList
    //歌曲recycleview适配器
    lateinit var mAdapter: SongsAdapter
    //正在添加歌单对话框
    val procesDialog by lazy { ProgressDialog(this) }

    lateinit var viewmodel: SongsVM
    //添加到已有歌单，已有歌单的recycleview适配器
    val collectAdapter by lazy { CollectToSongListAdapter(ArrayList(), this) }

    //底部播放栏的VM
    val mViewModel = BottomStateBarVM.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySongListDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_song_list_detail)
        //注册eventbus
        EventBus.getDefault().register(this)
        //绑定底部导航栏VM
        binding.mViewModel = mViewModel
        //初始化recycleview，防止skip layout
        mAdapter = SongsAdapter(ArrayList(), this)
        binding.rvSongs.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@SongListDetailActivity)
        }

        //绑定接收到的歌单对象
        mSonglist = intent.getSerializableExtra("songlist") as SongList
        viewmodel = SongsVM(mSonglist)
        observe()
        binding.data = mSonglist
        //初始化toolbar
        initToolBar()
        click()

    }

    /**
     * 初始化inittoolbar
     */
    fun initToolBar() {
        setSupportActionBar(tool_bar)
        tool_bar.setNavigationIcon(R.drawable.vector_drawable_back)
        tool_bar.setNavigationOnClickListener { finish() }

        //防止CollapsingToolbarLayout中其他控件的事件被toobar消费
        tool_bar.setOnTouchListener { v, event ->
            ll_root.dispatchTouchEvent(event)
        }
    }

    /**
     * 部分控件的点击事件
     */
    fun click() {
        iv_collect.setOnClickListener {
            viewmodel.addSongList()
        }
        //点击底部播放栏跳转至播放活动
        detail_song_bottom.setOnClickListener {
            startActivity<PlayingActivity>("song" to mViewModel?.song?.get())
        }

    }

    fun observe() {
        viewmodel.songs.observe(this, Observer {
            mAdapter.list.addAll(it!!)
            mAdapter.notifyDataSetChanged()
        })
        viewmodel.toast.observe(this, Observer {
            toast(it!!)
        })
        viewmodel.showAddDialog.observe(this, Observer {
            if (it!!) {
                procesDialog.apply {
                    setMessage("正在添加")
                    setCancelable(true)
                    show()
                }
            } else {
                procesDialog.dismiss()
            }
        })
        viewmodel.iscollected.observe(this, Observer {
            //根据是否收藏改变UI
            if (it!!) {
                iv_collect.setImageResource(R.drawable.vector_drawable_collect_red)
            } else {
                iv_collect.setImageResource(R.drawable.vector_drawable_collect_white)
            }
        })
        viewmodel.creatSongList.observe(this, Observer {
            collectAdapter.list.clear()
            collectAdapter.list.addAll(it!!)

        })

        //底部播放栏可见性控制
        mViewModel?.isDisplay?.observe(this, Observer {
            if (it!!){
                detail_song_bottom.visibility = View.VISIBLE
            }
        })
        //更改adapter的当前播放位置
        mViewModel?.index?.observe(this, Observer {
            if (it!! > -1){
                mAdapter.refreshPlayId(it)
            }
        })
    }


    /**
     * 音乐的一些删除，添加到歌单等操作
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun reveiveOnlineSong(event: SongEvent) {
        if (event.tag.equals("onlineSong")) {
            reduceTransparency()
            val view = LayoutInflater.from(this).inflate(R.layout.pop_window_online_song, null)
            val popupWindow =
                PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            popupWindow.apply {
                isFocusable = true
                animationStyle = R.style.popwindow
                setOnDismissListener { resetTransparency() }
                showAtLocation(ll_songlist_detail_root, Gravity.BOTTOM, 0, 0)
            }
            //添加到歌单
            view.ll_add_to_songlist.setOnClickListener {
                viewmodel.getUserCreatSongList()
                popupWindow.dismiss()
                reduceTransparency()

                //弹出歌单pop
                val view = LayoutInflater.from(this@SongListDetailActivity)
                    .inflate(R.layout.pop_window_collect_to_songlist, null)

                var mPopupWindow = PopupWindow()
                //点击歌单进行保存工作
                val mListener = object : CollectToSongListAdapter.OnSongListClickListener {
                    override fun onSongListClick(sonList: SongList) {
                        viewmodel.saveToSongList(event.onlineSong!!, sonList)
                        mPopupWindow.dismiss()

                    }
                }
                collectAdapter.listener = mListener
                view.rv_collect_to_songlist.apply {
                    adapter = collectAdapter
                    layoutManager = LinearLayoutManager(this@SongListDetailActivity)
                }

                mPopupWindow.apply {
                    contentView = view
                    width = (getScreenWidth() * 0.9).toInt()
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    isFocusable = true
                    animationStyle = R.style.popwindowCenter
                    setOnDismissListener { resetTransparency() }
                    showAtLocation(ll_songlist_detail_root, Gravity.CENTER, 0, 0)
                }
            }

        }
    }



    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
