package com.example.music.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import com.example.music.R
import com.example.music.databinding.ActivityPlayingBinding
import com.example.music.db.table.LocalMusic
import com.example.music.event.ProcessEvent
import com.example.music.viewmodel.PlayVM
import kotlinx.android.synthetic.main.activity_playing.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 音乐详情界面
 */
class PlayingActivity : AppCompatActivity() {

    val TAG = "PlayingActivity"
    lateinit var mSong:LocalMusic
    lateinit var binding: ActivityPlayingBinding
    lateinit var valueAnimator: ValueAnimator//旋转动画
    var currentTime: Long = 0//动画已播放的时间
    val viewmodel by lazy { PlayVM()}

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_playing)
        EventBus.getDefault().register(this)
        binding.viewmodel = viewmodel
        //初始化动画
        initAnim()
        viewmodel.checkPlaying()
        initToolbar()
        //隐藏状态栏
        hideStateBar()

        observe()

        //seekbar拖拽进度
       binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
           override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

           override fun onStartTrackingTouch(seekBar: SeekBar?) {}

           override fun onStopTrackingTouch(seekBar: SeekBar?) {
               seekBar?.progress?.let { viewmodel.changeProcess(it) }
           }
       })

        binding.ivSongCover.setOnClickListener {
            binding.ivSongCover.visibility = View.GONE
            binding.lrcview.visibility = View.VISIBLE

        }
        binding.lrcview.setOnClickListener {
            binding.ivSongCover.visibility = View.VISIBLE
            binding.lrcview.visibility = View.GONE
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



    fun initToolbar(){
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
    fun hideStateBar(){
        if (Build.VERSION.SDK_INT >= 21) {

            val decorView = getWindow().getDecorView()
            decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            )
            getWindow().setStatusBarColor(Color.TRANSPARENT)
        }
    }



    fun observe(){
        viewmodel.isPlaying.observe(this, Observer {
            if (it!!){
                //继续动画
                valueAnimator.start()
                valueAnimator.currentPlayTime = currentTime
            }else{
                //暂停动画
                valueAnimator.pause()
                currentTime = valueAnimator.currentPlayTime

            }
        }
        )
        //播放模式显示
        viewmodel.modeIndex.observe(this, Observer {
            when(it){
                0->binding.ivMode.setImageResource(R.drawable.vector_drawable_play_order)
                1->binding.ivMode.setImageResource(R.drawable.vector_drawable_play_random)
                2->binding.ivMode.setImageResource(R.drawable.vector_drawable_play_repeat)
            }
        })
        viewmodel.mDuration.observe(this, Observer {
            //设置seekbar最大值
            binding.seekBar.max = it!!
            //转换格式
            val mTime = String.format("%02d:%02d", it / 60000, (it/1000) % 60)
            binding.tvEnd.text = mTime
        })


    }



    /**
     * 更新进度条
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateProcess(event: ProcessEvent){
        //将数值转换为分秒的形式

        val mTime = String.format("%02d:%02d", event.num / 60000, (event.num/1000) % 60)
        when(event.tag){
            "duration" -> {
                //更新歌曲长度
                binding.seekBar.max = event.num
                binding.tvEnd.text = mTime
            }

            "current" -> {
                //更新歌曲播放进度
                binding.seekBar.progress = event.num
                binding.tvCurrent.text = mTime
            }
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
