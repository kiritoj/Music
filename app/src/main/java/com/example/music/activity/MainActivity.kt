package com.example.music.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.TableLayout
import android.widget.TextView
import cn.leancloud.AVUser
import com.bumptech.glide.Glide
import com.example.music.PlayManger
import com.example.music.PlayService

import com.example.music.R
import com.example.music.adapter.FragmentAdapter
import com.example.music.databindingadapter.getAlbumArt
import com.example.music.db.table.LocalMusic
import com.example.music.fragment.FindFragment
import com.example.music.fragment.MineFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.song_info_button.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    val TAG = "MainActivity"
    val list = ArrayList<Fragment>()
    lateinit var adapter: FragmentAdapter
    lateinit var tabListener : TabLayout.OnTabSelectedListener
    lateinit var textView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        initToolBar()
        initFragment()
        initTabListener()
        tl_main.setupWithViewPager(vp_main)
        tl_main.addOnTabSelectedListener(tabListener)
        tl_main.getTabAt(1)?.select()
        EventBus.getDefault().post(AVUser.getCurrentUser())


        //开启播放服务

        startService(Intent(this, PlayService::class.java))


    }

    fun initToolBar(){
        setSupportActionBar(tb_main)
        supportActionBar?.setTitle("")
        textView = LayoutInflater.from(this@MainActivity)
            .inflate(R.layout.tab_title, null) as TextView


    }

    fun initFragment(){
        list.add(MineFragment())
        list.add(FindFragment())
        adapter = FragmentAdapter(list, arrayOf("我的","发现"),supportFragmentManager)
        vp_main.adapter = adapter

    }

    fun initTabListener(){
        tabListener = object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                p0?.customView = null
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                textView.text = p0?.text
                p0?.customView = textView
            }
        }
    }

    //加载toolbar上action按钮
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(song:LocalMusic){
        Log.d(TAG,"显示底部")
        main_song_bottom.visibility = View.VISIBLE
        if (song.albumID!=null){
            iv_song_cover.setImageBitmap(getAlbumArt(song.albumID!!,this))
        }else{
           Glide.with(this).load(song.coverUrl).into(iv_song_cover)
        }
        tv_song_name.text = song.songName
        tv_singer_name.text = song.singerName
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
        stopService(Intent(this, PlayService::class.java))
    }



}
