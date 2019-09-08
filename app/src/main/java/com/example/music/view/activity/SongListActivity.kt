package com.example.music.view.activity

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
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
import com.example.music.*
import com.example.music.adapter.CategrayAdapter
import com.example.music.adapter.FragmentStateAdapter
import com.example.music.databinding.ActivitySongListBinding
import com.example.music.view.fragment.SongListFragment
import com.example.music.util.getScreeHeight
import com.example.music.util.reduceTransparency
import com.example.music.util.resetTransparency
import com.example.music.viewmodel.BottomStateBarVM
import com.example.music.viewmodel.SongListActivityVM
import kotlinx.android.synthetic.main.activity_song_list.*
import kotlinx.android.synthetic.main.pop_swindow_select_catgory.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import org.jetbrains.anko.startActivity
/**
 * 分类展示歌单
 */

class SongListActivity : AppCompatActivity() {

    val TAG = "SongListActivity"
    //分类viewpager的适配器
    lateinit var mAdapter: FragmentStateAdapter
    //分类适配器，语种、风格、场景、心情、每个大分类用recycleview展示
    val adapterList = ArrayList<CategrayAdapter>()
    //不同类别的fragment列表
    val list = ArrayList<Fragment>()
    //tablayout标题
    val title = ArrayList<String>()

    val mViewModel by lazy { SongListActivityVM() }
    //弹出重新选择分类popwindow
    lateinit var popwindow : PopupWindow

    //底部播放栏VM
    val viewModel = BottomStateBarVM.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySongListBinding = DataBindingUtil
            .setContentView(this,R.layout.activity_song_list)
        //注册EventBUs
        EventBus.getDefault().register(this)
        binding.viewmodel = viewModel

        //检查底部播放栏的可见性
        viewModel?.checkMusicPlaying()

        initToolbar()
        addFragment()
        tl_songlist.setupWithViewPager(vp_songlist)
        observe()
        //底部播栏点击跳转至音播放播放详情活动
        list_song_bottom.setOnClickListener {
            startActivity<PlayingActivity>()
        }
    }

    fun initToolbar() {
        setSupportActionBar(tb_songlist)
        tb_songlist.setTitleTextColor(resources.getColor(R.color.white))
        tb_songlist.setNavigationIcon(R.drawable.vector_drawable_back)
        tb_songlist.setNavigationOnClickListener { finish() }

    }

    //初始化viewpager，添加用户保存的5个分类fragment
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
        //底部播放栏可见性控制
        viewModel?.isDisplay?.observe(this, Observer {
            if (it!!){
                list_song_bottom.visibility = View.VISIBLE
            }
        })

    }


    /**
     * pop展示所有歌单种类
     */
    fun changeCategray() {
        //屏幕变暗
        reduceTransparency()
        val view = LayoutInflater.from(this).inflate(R.layout.pop_swindow_select_catgory, null)
        //recycleview添加分割线
        val decoration = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.set(10, 15, 10, 15)
            }
        }
        //按语种分类
        view.rv_lunguage.apply {
            adapter = adapterList[0]
            layoutManager = GridLayoutManager(this@SongListActivity, 4)
            addItemDecoration(decoration)
        }
        //按风格分类
        view.rv_style.apply {
            adapter = adapterList[1]
            layoutManager = GridLayoutManager(this@SongListActivity, 4)
            addItemDecoration(decoration)
        }
        //按场景分类
        view.rv_place.apply {
            adapter = adapterList[2]
            layoutManager = GridLayoutManager(this@SongListActivity, 4)
            addItemDecoration(decoration)
        }
        //按心情分类
        view.rv_enotion.apply {
            adapter = adapterList[3]
            layoutManager = GridLayoutManager(this@SongListActivity, 4)
            addItemDecoration(decoration)
        }
        //按主题分类
        view.rv_theme.apply {
            adapter = adapterList[4]
            layoutManager = GridLayoutManager(this@SongListActivity, 4)
            addItemDecoration(decoration)
        }
        //弹出窗口
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

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}
