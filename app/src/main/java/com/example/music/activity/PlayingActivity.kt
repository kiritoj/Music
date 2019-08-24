package com.example.music.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.arch.lifecycle.Observer
import android.database.DatabaseUtils
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.example.music.MusicApp
import com.example.music.R
import com.example.music.databinding.ActivityPlayingBinding
import com.example.music.databindingadapter.getAlbumArt
import com.example.music.db.table.LocalMusic
import com.example.music.event.ProcessEvent
import com.example.music.viewmodel.PlayVM
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_playing.*
import org.greenrobot.eventbus.Subscribe

/**
 * 音乐详情界面
 */
class PlayingActivity : AppCompatActivity() {

    lateinit var mSong:LocalMusic
    lateinit var binding: ActivityPlayingBinding
    lateinit var valueAnimator: ValueAnimator//旋转动画
    var currentTime: Long = 0//动画已播放的时间
    val viewmodel by lazy { PlayVM()}
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_playing)
        mSong = intent.getSerializableExtra("song") as LocalMusic
        binding.song = mSong
        binding.viewmodel = viewmodel
        loadImage(mSong)
        initAnim()
        initToolbar()
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

    /**
     * 记载旋转的大图
     */
    fun loadImage(song: LocalMusic){
        if(!TextUtils.isEmpty(song.path)){
            //本地歌曲直接设置图片
            binding.ivSongCover.setImageBitmap(getAlbumArt(song.albumID!!,this))
        }else{
            Glide.with(this).load(song.coverUrl).placeholder(R.drawable.ic_loading).into(binding.ivSongCover)
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

    fun hideStateBar(){
        if (Build.VERSION.SDK_INT >= 21) {

            val decorView = getWindow().getDecorView()
            decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            )
            getWindow().setStatusBarColor(Color.TRANSPARENT)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun observe(){
        viewmodel.isPause.observe(this, Observer {
            if (it!!){
                //暂停时停止动画
                iv_play.setImageResource(R.drawable.ic_play_pause)
                iv_song_cover.animauseP()
                valueAnimator.pause()
                currentTime = valueAnimator.currentPlayTime
            }else{
                //继续动画
                iv_play.setImageResource(R.drawable.ic_play_running)
                iv_song_cover.animcontinue()
                valueAnimator.start()
                valueAnimator.currentPlayTime = currentTime

            }
        })
        //播放完成后更新UI
        viewmodel.song.observe(this, Observer {
            loadImage(it!!)
            binding.tvSongname.text = it?.songName
            binding.tvSingername.text = it?.singerName
            Glide.with(this).load(it?.coverUrl)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_loading_error)
                .bitmapTransform(BlurTransformation(MusicApp.context, 30, 5))
                .into(binding.ivBackground)
        })
        viewmodel.modeIndex.observe(this, Observer {
            when(it){
                0->binding.ivMode.setImageResource(R.drawable.vector_drawable_play_order)
                1->binding.ivMode.setImageResource(R.drawable.vector_drawable_play_random)
                2->binding.ivMode.setImageResource(R.drawable.vector_drawable_play_repeat)
            }
        })

    }

    /**
     * 更新进度条
     */
    @Subscribe
    fun updateProcess(event: ProcessEvent){
        binding.seekBar.max = event.end
        binding.seekBar.progress = event.current
        val currentTime = String.format("%02d:%02d", event.current / 60, event.current % 60)
        val endtime = String.format("%02d:%02d", event.end / 60, event.end % 60)
        binding.tvCurrent.text = currentTime
        binding.tvEnd.text = endtime

    }


    override fun onDestroy() {
        super.onDestroy()
        valueAnimator.cancel()
    }


}
