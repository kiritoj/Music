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
import com.example.music.R
import com.example.music.adapter.CommentsAdapter
import com.example.music.adapter.RelatedMvAdapter
import com.example.music.databinding.ActivityMvDetailBinding
import com.example.music.event.ClickEvent
import com.example.music.viewmodel.MvDetailVM
import kotlinx.android.synthetic.main.activity_mv_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MvDetailActivity : AppCompatActivity() {

    val viewmodel by lazy { MvDetailVM() }
    lateinit var binding: ActivityMvDetailBinding
    //相关mv适配器
    val mvAdapter by lazy { RelatedMvAdapter(ArrayList(),this) }
    //评论适配器
    val comAdapter by lazy { CommentsAdapter(ArrayList()) }
    //
    var mvID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mv_detail)

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK)
        }

        EventBus.getDefault().register(this)
        binding.viewmodel = viewmodel
        //接受mvid
        mvID = intent.getLongExtra("mvid",0)
        viewmodel.getAllAboutMv(mvID)
        binding.id = mvID
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
        //展开详细介绍
        viewmodel.isOpenDesc.observe(this, Observer {
            if (it!!){
                binding.ivOpenDesc.setImageResource(R.drawable.vector_drawable_dengbian)
                binding.tvDesc.visibility = View.VISIBLE
            }else{
                binding.ivOpenDesc.setImageResource(R.drawable.vector_drawable_dengbian_down)
                binding.tvDesc.visibility = View.GONE
            }
        })
        //加载失败
        viewmodel.isError.observe(this, Observer {
            if (it!!){
                binding.tvError.visibility = View.VISIBLE
            }else{
                binding.tvError.visibility = View.GONE
            }
        })
        //
        viewmodel.showProcessBar.observe(this, Observer {
            if (it!!){
                binding.processBar.visibility = View.VISIBLE
            }else{
                binding.processBar.visibility = View.GONE
            }
        })
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
                        viewmodel.getMvComment(mvID,offset = comAdapter.list.size)
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
    fun onRelatedItemClick(event: ClickEvent){
        mvID = event.id
        binding.nestScrollView.scrollTo(0,0)
        //停止播放当前视频
        jz_video_player.release()
        //清空评论列表
        comAdapter.list.clear()
        comAdapter.notifyDataSetChanged()
        //隐藏详情信息
        binding.nestScrollView.visibility = View.INVISIBLE
        binding.processBar.visibility = View.VISIBLE
        viewmodel.getAllAboutMv(event.id)
    }
}





