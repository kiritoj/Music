package com.example.music.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.music.*
import com.example.music.view.activity.BinnerActivity
import com.example.music.model.db.table.BannerTable
import org.jetbrains.anko.startActivity
import com.example.music.view.activity.PlayingActivity
import com.example.music.model.db.table.LocalMusic
import com.example.music.event.QueneEvent
import com.example.music.model.bean.MvData
import com.example.music.model.db.table.SongList
import com.example.music.network.ApiGenerator
import com.example.music.network.services.MusicService
import com.example.music.util.GlideUtil
import com.example.music.view.activity.MvDetailActivity
import com.example.music.view.activity.SongListDetailActivity
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus


/**
 * Created by tk on 2019/8/19
 */
class BinnerAdapter(mList: ArrayList<BannerTable>, val context: Context?) : PagerAdapter() {
    val TAG = "BinnerAdapter"
    val list = ArrayList<BannerTable>()
    init {
        list.addAll(mList)
        //在开头和结尾各添加一个实现无限轮播
        list.add(0,mList[mList.size-1])
        list.add(mList[0])
    }
    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val imageView = ImageView(context)
        GlideUtil.loadCornerPic(imageView
            ,list[position].imageUrl
            ,ContextCompat.getDrawable(context!!,R.drawable.ic_loading)!!
            ,ContextCompat.getDrawable(context!!,R.drawable.ic_loading_error)!!
            ,20)


        //imagview两边留出空隙，避免两页粘连在一起
        imageView.setPadding(30,0,30,0)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        container.addView(imageView)


        //点击轮播图片跳转详情页
        imageView.setOnClickListener {

            /**
             * banner点击有三种情况，根据targetType来区分
             * 1.直接跳转至网页
             * 2.播放对应id的歌曲
             * 3.打开对应id的专辑
             */

            when(list[position].targetType){
                //打开网页
               3000 -> context?.startActivity<BinnerActivity>("url" to list[position].url)
                //播放歌曲
                1 -> {
                    ApiGenerator.getApiService(MusicService::class.java)
                        .getSong(list[position].targetId)
                        .subscribeOn(Schedulers.io())
                        .subscribe ({
                            val mMusic = LocalMusic()
                            mMusic.apply {
                                musicId = it.songs[0].id
                                songName = it.songs[0].name
                                singerName = it.songs[0].ar[0].name
                                coverUrl = it.songs[0].al.picUrl
                                tag = "NET_NON_URL"
                            }
                            val quene = ArrayList<LocalMusic>()
                            quene.add(mMusic)
                            EventBus.getDefault().post(QueneEvent(quene, 0,"banner"))
                            context?.startActivity<PlayingActivity>()
                        },{
                            Log.d(TAG,"BinnerAdapter:获取banner歌曲信息失败${it.message}")
                        })
                }
                1004 -> {
                    val mv = MvData(0,"","",list[position].imageUrl,list[position].targetId,"视频",0)
                    context.startActivity<MvDetailActivity>("mv" to mv)
                }

//                mUrl.startsWith("/song") -> {
//                    val mId = mUrl.substring(9).toLong()
//                    ApiGenerator.getApiService(MusicService::class.java)
//                        .getSong(mId)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe({
//                            val music = LocalMusic()
//                            music.apply {
//                                id = mId
//                                songName = it.data!!.songs!![0].name
//                                singerName = it.data!!.songs!![0].ar!![0].name
//                                url = SONG_PLAY_BASE_URL+id
//                                coverUrl = IMAGE_BASE_URL+id
//                            }
//                            val list = ArrayList<LocalMusic>()
//                            list.add(music)
//                            EventBus.getDefault().post(QueneEvent(list,0))
//                            context?.startActivity<PlayingActivity>()
//                        },{
//                            Log.d(TAG,"banner获取歌曲失败${it.message}")
//                        })
//                }
//
//                //打开歌单
//                mUrl.startsWith("/playlist") -> {
//                    Log.d(TAG,"因接口原因，暂时无法通过歌单id获取到歌单信息")
//                    val sonList = SongList()
//                    sonList.apply {
//                        coverUrl = DEFAULT_COVER
//                        name = "暂时无法获取歌单名"
//                        creatorAvatar = DEFAULT_AVATAR
//                        creatorName = "暂时无法获取创建者"
//                        description = "暂无"
//                        commentNum = 0
//                        collectNum = 0
//                        netId = mUrl.substring(13).toLong()
//                        num = 0
//                        creatorId = 0
//                    }
//                    context?.startActivity<SongListDetailActivity>("songlist" to sonList)
//                }
//
            }

        }
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}