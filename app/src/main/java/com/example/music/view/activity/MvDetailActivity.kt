package com.example.music.view.activity

import android.animation.ValueAnimator
import android.arch.lifecycle.Observer
import android.content.pm.ActivityInfo
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.jzvd.JZVideoPlayer
import com.example.music.PlayManger
import com.example.music.R
import com.example.music.adapter.CommentsAdapter
import com.example.music.adapter.RelatedMvAdapter
import com.example.music.databinding.ActivityMvDetailBinding
import com.example.music.event.MvClickEvent
import com.example.music.model.bean.MvData
import com.example.music.viewmodel.MvDetailVM
import kotlinx.android.synthetic.main.activity_mv_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.toast

class MvDetailActivity : AppCompatActivity() {

    val viewmodel by lazy { MvDetailVM() }
    lateinit var binding: ActivityMvDetailBinding
    //相关mv适配器
    val mvAdapter by lazy { RelatedMvAdapter(ArrayList(),this) }
    //评论适配器
    val comAdapter by lazy { CommentsAdapter(ArrayList()) }
    //
    lateinit var mv : MvData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mv_detail)
        PlayManger.pause()
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK)
        }

        EventBus.getDefault().register(this)
        binding.viewmodel = viewmodel
        //接受mvid
        mv = intent.getSerializableExtra("mv") as MvData
        viewmodel.getAllAboutMv(mv)
        binding.mv = mv
        binding.hangdler = MyHandler()
        initPlayer()
        initRecycle()

        observe()

    }


    //播放器设置
    fun initPlayer() {
        //点击横向全屏播放
        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE //横向
        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        //视频填充模式
        JZVideoPlayer.setVideoImageDisplayType(JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP)
    }

    //初始化recycleview
    fun initRecycle(){
        binding.rvRelatedMv.apply {
            adapter = mvAdapter
            layoutManager = LinearLayoutManager(this@MvDetailActivity)
        }
        binding.rvComment.apply {
            adapter = comAdapter
            layoutManager = LinearLayoutManager(this@MvDetailActivity)
        }
    }

    fun observe(){

        //相关mv
        viewmodel.relatedMv.observe(this, Observer {
            mvAdapter.list = it!!
            binding.nestScrollView.visibility = View.VISIBLE
            mvAdapter.notifyDataSetChanged()
        })
        //评论
        viewmodel.comments.observe(this, Observer {
            val size = comAdapter.list.size
            comAdapter.list.addAll(it!!)
            comAdapter.notifyItemRangeInserted(size,it.size)
        })
        //toast
        viewmodel.toast.observe(this, Observer {
            toast(it!!)
        })
    }

    override fun onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        JZVideoPlayer.releaseAllVideos()
    }

    inner class MyHandler(){
        init {
            binding.nestScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { p0, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (p0 != null) {
                    if (scrollY == p0.getChildAt(0).measuredHeight - p0.measuredHeight) {
                        viewmodel.getMvComment(mv.mvId,offset = comAdapter.list.size)
                    }
                }
            })
        }

        //点击评论下滑至列表处
        fun scrollToComment(){
            val dy = binding.nestScrollView.getChildAt(0).measuredHeight-binding.rvComment.measuredHeight
            val animator =  ValueAnimator.ofFloat(0f,dy.toFloat())
            animator.duration = 500
            animator.start()
            animator.addUpdateListener {
                binding.nestScrollView.scrollTo(0,(it.animatedValue as Float).toInt())
            }

        }
    }

    @Subscribe
    fun onRelatedItemClick(event: MvClickEvent){
        mv = event.data
        binding.nestScrollView.scrollTo(0,0)
        //停止播放当前视频
        jz_video_player.release()
        //清空评论列表
        comAdapter.list.clear()
        comAdapter.notifyDataSetChanged()
        //隐藏详情信息
        binding.nestScrollView.visibility = View.INVISIBLE
        viewmodel.reload(mv)
    }
}





