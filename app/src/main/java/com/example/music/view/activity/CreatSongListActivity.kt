package com.example.music.view.activity

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.example.music.R
import com.example.music.adapter.CreatListAdapter
import com.example.music.databinding.ActivityCreatSongListBinding
import com.example.music.model.db.table.SongList
import com.example.music.event.SongEvent
import com.example.music.util.reduceTransparency
import com.example.music.util.resetTransparency
import com.example.music.viewmodel.BottomStateBarVM
import com.example.music.viewmodel.CreatListVM
import kotlinx.android.synthetic.main.activity_creat_song_list.*
import kotlinx.android.synthetic.main.activity_song_list_detail.ll_songlist_detail_root
import kotlinx.android.synthetic.main.activity_song_list_detail.tool_bar
import kotlinx.android.synthetic.main.pop_window_creat_song.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.startActivity
import org.litepal.LitePal

class CreatSongListActivity : AppCompatActivity() {
    val TAG = "CreatSongListActivity"
    lateinit var mSonglist: SongList
    lateinit var mAdapter: CreatListAdapter
    lateinit var viwmodel: CreatListVM
    val mViewmodel = BottomStateBarVM.get() //底部播放栏的VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityCreatSongListBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_creat_song_list)

        EventBus.getDefault().register(this)
        //接收歌单
        mSonglist = intent.getSerializableExtra("songlist") as SongList
        viwmodel = CreatListVM(mSonglist)

        //绑定
        binding.data = mSonglist
        binding.viewmodel = mViewmodel

        //检查音乐播放情况，加载底部播放栏
        mViewmodel?.checkMusicPlaying()

        //初始化recycleview
        val songList = LitePal.where("name = ?",mSonglist.name)
            .findFirst(SongList::class.java,true)

        mAdapter = CreatListAdapter(songList.songs, this,mSonglist.name!!)
        binding.rvSongs.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@CreatSongListActivity)
        }

        //与viewmodel的状态绑定
        observe()
        initToolBar()

        //点击底部播放栏点击跳转到播放活动
        creat_song_bottom.setOnClickListener {
            startActivity<PlayingActivity>()
        }
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

        //底部播放栏的可见性控制
        mViewmodel?.isDisplay?.observe(this, Observer {
            if (it!!){
                creat_song_bottom.visibility = View.VISIBLE
            }
        })

        //更新播放歌曲位置
        mViewmodel?.index?.observe(this, Observer {
            if (it!! > -1){
                mAdapter.refreshPlayIdWithTag(it)
            }
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

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}
