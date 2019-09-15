package com.example.music.view.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.PopupWindow
import android.widget.SeekBar
import com.example.music.PlayManger
import com.example.music.R
import com.example.music.adapter.PlayListAdapter
import com.example.music.databinding.ActivityPlayingBinding
import com.example.music.event.ProcessEvent
import com.example.music.model.db.table.LocalMusic
import com.example.music.util.getScreeHeight
import com.example.music.util.reduceTransparency
import com.example.music.util.resetTransparency
import com.example.music.viewmodel.PlayVM
import kotlinx.android.synthetic.main.activity_playing.*
import kotlinx.android.synthetic.main.pop_playlist.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity

/**
 * 音乐详情界面
 */
class PlayingActivity : AppCompatActivity() {

    val TAG = "PlayingActivity"
    lateinit var mSong: LocalMusic
    lateinit var binding: ActivityPlayingBinding
    lateinit var valueAnimator: ValueAnimator//旋转动画
    var currentTime: Long = 0//动画已播放的时间
    val viewmodel by lazy { PlayVM() }
    //播放列表适配器
    lateinit var mAdapter: PlayListAdapter
    //播放列表
    lateinit var mPopupWindow: PopupWindow
    //pop布局
    lateinit var mRootView: View

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_playing)
        //注册eventbus
        EventBus.getDefault().register(this)

        binding.viewmodel = viewmodel
        binding.hanlder = MyHanlder()
        viewmodel.checkPlaying()
        //初始化进度为0
        updateProcess(ProcessEvent("duration", 0))
        updateProcess(ProcessEvent("current", 0))
        //初始化动画
        initPop()
        initAnim()
        initToolbar()
        //隐藏状态栏
        hideStateBar()

        observe()

    }


    //初始化popwindow
    fun initPop() {
        mRootView = LayoutInflater.from(this)
            .inflate(R.layout.pop_playlist, null)
        mAdapter = PlayListAdapter(PlayManger.quene, PlayManger.index)
        mRootView.rv_play_list.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@PlayingActivity)
        }
        mPopupWindow = PopupWindow(
            mRootView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            (getScreeHeight() * 0.6).toInt()
        )
        mPopupWindow.apply {
            isFocusable = true
            animationStyle = R.style.popwindow
            setOnDismissListener { resetTransparency() }
        }
    }

    //初始化动画
    fun initAnim() {
        //旋转动画的设置
        valueAnimator = ObjectAnimator.ofFloat(binding.ivSongCover, "rotation", 0f, 360f)
        valueAnimator.apply {
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            duration = 9000
            interpolator = LinearInterpolator()
            start()
        }
    }


    fun initToolbar() {
        setSupportActionBar(toolbar)
        toolbar.apply {

            setNavigationIcon(R.drawable.vector_drawable_back)
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    /**
     * 隐藏状态栏
     */
    fun hideStateBar() {
        if (Build.VERSION.SDK_INT >= 21) {

            val decorView = getWindow().getDecorView()
            decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            )
            getWindow().setStatusBarColor(Color.TRANSPARENT)
        }
    }


    fun observe() {
        //播放、暂停动画
        viewmodel.isPlaying.observe(this, Observer {
            if (it!!) {
                //继续动画
                valueAnimator.start()
                valueAnimator.currentPlayTime = currentTime
            } else {
                //暂停动画
                valueAnimator.pause()
                currentTime = valueAnimator.currentPlayTime

            }
        }
        )
        //播放模式显示
        viewmodel.modeIndex.observe(this, Observer {
            when (it) {
                0 -> binding.ivMode.setImageResource(R.drawable.vector_drawable_play_order)
                1 -> binding.ivMode.setImageResource(R.drawable.vector_drawable_play_random)
                2 -> binding.ivMode.setImageResource(R.drawable.vector_drawable_play_repeat)
            }
        })
        //在暂停状态下进入活动初始化歌曲总长度
        viewmodel.mDuration.observe(this, Observer {
           updateProcess(ProcessEvent("duration",it!!))
        })
        //暂停状态下进入活动初始化歌曲当前播放长度
        viewmodel.mCurrentPosition.observe(this, Observer {
            updateProcess(ProcessEvent("current",it!!))
        })
        //更新播放列表正在播放的位置
        viewmodel.songIndex.observe(this, Observer {
            it?.let { it1 -> mAdapter.setId(it1) }
        })
        //显示缓冲中text
        viewmodel.showBuffer.observe(this, Observer {
            if (it!!){
                tv_buffer.visibility = View.VISIBLE
            }else{
                tv_buffer.visibility = View.INVISIBLE
            }
        })


    }


    /**
     * 更新进度条
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun updateProcess(event: ProcessEvent) {
        //将数值转换为分秒的形式

        val mTime = String.format("%02d:%02d", event.num / 60000, (event.num / 1000) % 60)
        when (event.tag) {
            "duration" -> {
                if (event.num == 0) {
                    viewmodel.banSeekBar(binding.seekBar)
                }
                //更新歌曲长度
                binding.seekBar.max = event.num
                binding.tvEnd.text = mTime
            }

            "current" -> {
                //更新歌曲播放进度
                if (event.num > 0) {
                    viewmodel.enableSeekBar(binding.seekBar)
                }
                binding.seekBar.progress = event.num
                binding.tvCurrent.text = mTime
            }
        }


    }

    //点击事件
    inner class MyHanlder {
        //进入评论activity
        fun onCommentClick() {
            viewmodel.song.get()?.let {
                startActivity<CommentActivity>("id" to it.musicId, "tag" to "music")
            }
        }

        //底部弹出播放列表
        fun onNavClick() {
            reduceTransparency()
            mPopupWindow.showAtLocation(fr_play_root, Gravity.BOTTOM, 0, 0)
        }

        //点击歌曲图片，隐藏，显示歌词
        fun onPicClick() {
            binding.ivSongCover.visibility = View.GONE
            binding.lrcview.visibility = View.VISIBLE
        }

        //点击歌词隐藏自己，显示歌曲图片
        fun onLrcClick() {
            binding.ivSongCover.visibility = View.VISIBLE
            binding.lrcview.visibility = View.GONE
        }

        init {
            //seekbar拖拽进度
            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.progress?.let { viewmodel.changeProcess(it) }
                }
            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        //停止动画，防止内存泄漏
        iv_song_cover.animStop()
        valueAnimator.cancel()
        EventBus.getDefault().unregister(this)
    }


}
