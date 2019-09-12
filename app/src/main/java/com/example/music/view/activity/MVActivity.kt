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
import com.example.music.databinding.ActivityMvBinding
import com.example.music.viewmodel.BottomStateBarVM
import kotlinx.android.synthetic.main.activity_song_list.*
import org.jetbrains.anko.startActivity


class MVActivity : BaseActivity() {

    val TAG = "MVActivity"
    val mList = ArrayList<Fragment>()
    //tablayout标题
    val mTitles = arrayListOf("推荐","最新","排行榜")
    //底部播放栏VM
    val viewModel = BottomStateBarVM.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMvBinding>(this,R.layout.activity_mv)
        binding.viewmodel = viewModel
        viewModel?.checkMusicPlaying()

        toolbar.init("MV")
        //tablayout绑定viewpager
        tl_mv.setupWithViewPager(vp_mv)
        observe()
        //底部播栏点击跳转至音播放播放详情活动
        mv_song_bottom.setOnClickListener {
            startActivity<PlayingActivity>()
        }
//        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE //横向
//        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
//        JZVideoPlayer.setVideoImageDisplayType(JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP)
//        //清晰度
//        val map = LinkedHashMap<String,String>()
//
//        val dataSourceObjects = arrayOfNulls<Any>(1)
//        dataSourceObjects[0] = map
//        jz_vedioplayer.setUp("http://vodkgeyttp8.vod.126.net/cloudmusic/7ec3/core/c30f/99a3de75dda9feaa8050053f900645c0.mp4?wsSecret=f376ca582beac21249bc99da11d64b00&wsTime=1568209857",
//            JZVideoPlayerStandard.SCROLL_AXIS_HORIZONTAL, "嫂子闭眼睛")
//
//        jz_vedioplayer.thumbImageView.scaleType = ImageView.ScaleType.CENTER_CROP
//        Glide.with(this).load("http://p1.music.126.net/gPGygaKPIZimcB6PYNWswg==/109951164353209473.jpg")
//            .apply(RequestOptions().placeholder(R.drawable.back)
//                .error(R.drawable.ic_loading_error).dontAnimate())
//            .into(jz_vedioplayer.thumbImageView)
//        Log.d(TAG,jz_vedioplayer.currentPositionWhenPlaying.toString())

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
}
