package com.example.music.view.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.TextView
import cn.leancloud.AVUser
import com.example.music.PlayControlReceiver
import com.example.music.PlayService
import org.jetbrains.anko.startActivity
import com.example.music.R
import com.example.music.adapter.FragmentAdapter
import com.example.music.databinding.ActivityMainBinding
import com.example.music.view.fragment.FindFragment
import com.example.music.view.fragment.MineFragment
import com.example.music.viewmodel.BottomStateBarVM
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
class MainActivity : BaseActivity() {

    val TAG = "MainActivity"
    //我的，首页，碎片list
    val list = ArrayList<Fragment>()
    //viewpager 碎片适配器
    lateinit var adapter: FragmentAdapter

    lateinit var tabListener : TabLayout.OnTabSelectedListener
    //tab选中时替代的view
    lateinit var textView: TextView

    val mViewmodel = BottomStateBarVM.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.viewmodel = mViewmodel
        initToolBar()
        initFragment()
        initTabListener()
        initClick()
        tl_main.setupWithViewPager(vp_main)
        tl_main.addOnTabSelectedListener(tabListener)
        tl_main.getTabAt(1)?.select()
        EventBus.getDefault().post(AVUser.getCurrentUser())

        //开启播放服务
        startService(Intent(this, PlayService::class.java))
        observe()
        mViewmodel?.checkMusicPlaying()

//        //注册播放控制广播，用于前台服务进行播放控制
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(PlayControlReceiver.ACTION1)
//        intentFilter.addAction(PlayControlReceiver.ACTION2)
//        intentFilter.addAction(PlayControlReceiver.ACTION3)
//        registerReceiver(PlayControlReceiver.get(), intentFilter)


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

    fun observe(){
        mViewmodel?.isDisplay?.observe(this, Observer {
            if (it!!){
                main_song_bottom.visibility = View.VISIBLE
            }
        })

    }

    /**
     * 控件的点击事件
     */
    fun initClick(){
        //点击底部播放栏进入歌曲详情界面
        main_song_bottom.setOnClickListener {
            startActivity<PlayingActivity>()
        }
    }



}
