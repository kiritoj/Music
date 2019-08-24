package com.example.music.activity

import android.arch.lifecycle.Observer
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.example.music.*
import com.example.music.adapter.CategrayAdapter
import com.example.music.adapter.FragmentStateAdapter
import com.example.music.databindingadapter.getAlbumArt
import com.example.music.db.table.LocalMusic
import com.example.music.event.RefreshEvent
import com.example.music.fragment.SongListFragment
import com.example.music.viewmodel.SongListActivityVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_song_list.*
import kotlinx.android.synthetic.main.pop_swindow_select_catgory.view.*
import kotlinx.android.synthetic.main.song_info_button.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import org.jetbrains.anko.startActivity
/**
 * 展示歌单
 */

class SongListActivity : AppCompatActivity() {

    val TAG = "SongListActivity"
    lateinit var mAdapter: FragmentStateAdapter
    val adapterList = ArrayList<CategrayAdapter>()
    val list = ArrayList<Fragment>()
    val title = ArrayList<String>()
    val mViewModel by lazy { SongListActivityVM() }
    lateinit var popwindow : PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)
        EventBus.getDefault().register(this)
        initToolbar()
        addFragment()
        tl_songlist.setupWithViewPager(vp_songlist)
        observe()
    }

    fun initToolbar() {
        setSupportActionBar(tb_songlist)
        tb_songlist.setTitleTextColor(resources.getColor(R.color.white))
        tb_songlist.setNavigationIcon(R.drawable.vector_drawable_back)
        tb_songlist.setNavigationOnClickListener { finish() }

    }

    //初始化viewpager
    fun addFragment() {
        mAdapter = FragmentStateAdapter(list,title,supportFragmentManager)
        vp_songlist.adapter = mAdapter
    }


    fun observe() {
        mViewModel.mList.observe(this, Observer {
            //更新viewpager
            it?.let { it1 -> title.addAll(it1) }
            it?.forEach {
                list.add(SongListFragment.getInstance(it))
            }
            it?.let { it1 -> mAdapter.title.addAll(it1) }
            mAdapter.notifyDataSetChanged()
            Log.d(TAG,"test")


        })
        mViewModel.category.observe(this, Observer {

            adapterList.add(CategrayAdapter(it?.lunguageList!!))
            adapterList.add(CategrayAdapter(it.styleList!!))
            adapterList.add(CategrayAdapter(it.placeList!!))
            adapterList.add(CategrayAdapter(it.emotionList!!))
            adapterList.add(CategrayAdapter(it.themeList!!))
            iv_more_categray.setOnClickListener {
                changeCategray()
            }

        })

    }


    /**
     * pop展示所有歌单种类
     */
    fun changeCategray() {
        reduceTransparency()
        val view = LayoutInflater.from(this).inflate(R.layout.pop_swindow_select_catgory, null)
        val decoration = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.set(10, 15, 10, 15)
            }
        }
        view.rv_lunguage.apply {
            adapter = adapterList[0]
            layoutManager = GridLayoutManager(this@SongListActivity, 4)
            addItemDecoration(decoration)
        }
        view.rv_style.apply {
            adapter = adapterList[1]
            layoutManager = GridLayoutManager(this@SongListActivity, 4)
            addItemDecoration(decoration)
        }
        view.rv_place.apply {
            adapter = adapterList[2]
            layoutManager = GridLayoutManager(this@SongListActivity, 4)
            addItemDecoration(decoration)
        }
        view.rv_enotion.apply {
            adapter = adapterList[3]
            layoutManager = GridLayoutManager(this@SongListActivity, 4)
            addItemDecoration(decoration)
        }
        view.rv_theme.apply {
            adapter = adapterList[4]
            layoutManager = GridLayoutManager(this@SongListActivity, 4)
            addItemDecoration(decoration)
        }
        popwindow = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, (getScreeHeight()*0.8).toInt())
        popwindow.apply {
            isFocusable = true
            setOnDismissListener { resetTransparency() }
            animationStyle = R.style.popwindow
            showAtLocation(ll_songlist_root, Gravity.BOTTOM, 0, 0)
        }
    }


    /**
     *用选中的种类代替当前种类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveNewCate(newName: String) {
        if (mViewModel.mList.value?.contains(newName)!!) {
           toast("该类别已添加")
        }else{
            //移除最后一个类别
            list.removeAt(4)
            //添加新类别到第二个位置
            list.add(1, SongListFragment.getInstance(newName))
            //更新tab
            title.removeAt(4)
            title.add(1, newName)
            mAdapter.notifyDataSetChanged()
            //更新用户歌单信息
            mViewModel.update(newName)
            popwindow.dismiss()
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun update(event: RefreshEvent){
        list_song_bottom.visibility = View.VISIBLE
        list_song_bottom.setOnClickListener { startActivity<PlayingActivity>("song" to event.song) }
        if (event.song.albumID!=null){
            iv_song_cover.setImageBitmap(getAlbumArt(event.song.albumID!!,this))
        }else{
            Glide.with(this).load(event.song.coverUrl).into(iv_song_cover)
        }
        tv_song_name.text = event.song.songName
        tv_singer_name.text = event.song.singerName

    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}
