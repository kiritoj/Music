package com.example.music.view.activity

import android.arch.lifecycle.Observer
import android.content.pm.ActivityInfo
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import cn.jzvd.JZMediaManager
import cn.jzvd.JZVideoPlayerStandard
import com.bumptech.glide.Glide
import com.example.music.R
import kotlinx.android.synthetic.main.activity_mv.*
import cn.jzvd.JZVideoPlayer
import com.bumptech.glide.request.RequestOptions
import com.example.music.adapter.FragmentAdapter
import com.example.music.databinding.ActivityMvBinding
import com.example.music.view.fragment.NewMvFragment
import com.example.music.view.fragment.TopMvFragment
import com.example.music.viewmodel.BottomStateBarVM
import kotlinx.android.synthetic.main.activity_song_list.*
import org.jetbrains.anko.startActivity


class MVActivity : BaseActivity() {

    val TAG = "MVActivity"
    val mList = ArrayList<Fragment>()
    //tablayout标题
    val mTitles = arrayOf("最新","排行榜")

    lateinit var adapter: FragmentAdapter
    //底部播放栏VM
    val viewModel = BottomStateBarVM.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMvBinding>(this,R.layout.activity_mv)
        binding.viewmodel = viewModel
        viewModel?.checkMusicPlaying()
        toolbar.init("MV")
        addFragment()
        //tablayout绑定viewpager
        tl_mv.setupWithViewPager(vp_mv)
        observe()
        //底部播栏点击跳转至音播放播放详情活动
        mv_song_bottom.setOnClickListener {
            startActivity<PlayingActivity>()
        }




    }

    fun addFragment(){
        mList.add(NewMvFragment())
        mList.add(TopMvFragment())
        adapter = FragmentAdapter(mList,mTitles,supportFragmentManager)
        vp_mv.adapter = adapter
    }

    fun observe(){
        //底部播放栏可见性控制
        viewModel?.isDisplay?.observe(this, Observer {
            if (it!!){
                mv_song_bottom.visibility = View.VISIBLE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

}
