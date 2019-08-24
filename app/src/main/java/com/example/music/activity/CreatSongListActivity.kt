package com.example.music.activity

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
import com.example.music.PlayManger
import com.example.music.R
import com.example.music.adapter.CreatListAdapter
import com.example.music.adapter.SongsAdapter
import com.example.music.databinding.ActivityCreatSongListBinding
import com.example.music.databindingadapter.getAlbumArt
import com.example.music.db.table.LocalMusic
import com.example.music.db.table.SongList
import com.example.music.event.RefreshEvent
import com.example.music.event.SongEvent
import com.example.music.reduceTransparency
import com.example.music.resetTransparency
import com.example.music.viewmodel.CreatListVM
import kotlinx.android.synthetic.main.activity_creat_song_list.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_song_list_detail.*
import kotlinx.android.synthetic.main.activity_song_list_detail.ll_songlist_detail_root
import kotlinx.android.synthetic.main.activity_song_list_detail.tool_bar
import kotlinx.android.synthetic.main.pop_window_creat_song.view.*
import kotlinx.android.synthetic.main.song_info_button.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity

class CreatSongListActivity : AppCompatActivity() {
    lateinit var mSonglist: SongList
    lateinit var mAdapter: CreatListAdapter
    lateinit var viwmodel: CreatListVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityCreatSongListBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_creat_song_list)
        EventBus.getDefault().register(this)
        mSonglist = intent.getSerializableExtra("songlist") as SongList
        binding.data = mSonglist
        viwmodel = CreatListVM(mSonglist)

        //初始化recycleview
        mAdapter = CreatListAdapter(ArrayList(), this)
        binding.rvSongs.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@CreatSongListActivity)
        }
        observe()
        initToolBar()
    }

    /**
     * 初始化inittoolbar
     */
    fun initToolBar() {
        setSupportActionBar(tool_bar)
        tool_bar.setNavigationIcon(R.drawable.vector_drawable_back)
        tool_bar.setNavigationOnClickListener { finish() }
    }

    fun observe(){
        viwmodel.songs.observe(this, Observer {
            mAdapter.list.clear()
            mAdapter.list.addAll(it!!)
            mAdapter.notifyDataSetChanged()
        })
    }

    @Subscribe
    fun receiveCreatSong(event: SongEvent){
        if (event.tag.equals("creat")){
            reduceTransparency()
            val view = LayoutInflater.from(this).inflate(R.layout.pop_window_creat_song,null)
            val popupWindow = PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            popupWindow.apply {
                isFocusable = true
                animationStyle = R.style.popwindow
                setOnDismissListener { resetTransparency() }
                showAtLocation(ll_songlist_detail_root,Gravity.BOTTOM,0,0)
            }

            //从歌单删除
            view.ll_delete_from_songlist.setOnClickListener {
                viwmodel.delete(event.creatSong!!)
                mAdapter.delete(event.position)
                popupWindow.dismiss()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun update(event: RefreshEvent){
        creat_song_bottom.visibility = View.VISIBLE
        //更新adapter
        mAdapter.refreshPlayId(event.position)
        creat_song_bottom.setOnClickListener { startActivity<PlayingActivity>("song" to event.song) }

        if (event.song.albumID!=null){
            iv_song_cover.setImageBitmap(getAlbumArt(event.song.albumID!!,this))
        }else{
            Glide.with(this).load(event.song.coverUrl).into(iv_song_cover)
        }
        tv_song_name.text = event.song.songName
        tv_singer_name.text = event.song.singerName
        //暂停
        var pause: Boolean = false
        iv_pause.setOnClickListener {
            if (pause){
                EventBus.getDefault().post(PlayManger.State.PLAY)
                iv_pause.setImageResource(R.drawable.vector_drawable_play_black)
                pause = false
            }else{
                EventBus.getDefault().post(PlayManger.State.PAUSE)
                iv_pause.setImageResource(R.drawable.vector_drawable_pause_black)
                pause = true
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
