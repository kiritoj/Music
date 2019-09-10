package com.example.music.view.activity

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.View
import com.example.music.PlayManger
import com.example.music.R
import com.example.music.adapter.TopListAdapter
import com.example.music.databinding.ActivityTopListBinding
import com.example.music.viewmodel.BottomStateBarVM
import com.example.music.viewmodel.TopListVM
import kotlinx.android.synthetic.main.activity_top_list.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * 排行榜
 */
class TopListActivity : BaseActivity() {

    val mViewModel = BottomStateBarVM.get()
    val mTopViewModel by lazy { TopListVM() }
    //官方榜适配器
    val mAdapter1 by lazy { TopListAdapter(ArrayList(),this) }
    //其他榜单适配器
    val mAdapter2 by lazy { TopListAdapter(ArrayList(),this)}
    //点击事件

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityTopListBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_top_list)
        //绑定底部播放栏
        binding.apply {
            viewmodel = mViewModel
            topViewModel = mTopViewModel
        }
        val mHanlder = MyHanlder()

        //检查播放栏可见性
        mViewModel?.checkMusicPlaying()
        //初始化toolbar
        toolbar.init("排行榜")

        initRecycle()
        observe()



    }

    fun initRecycle(){
        rv_offical_top.apply {
            adapter = mAdapter1
            layoutManager = GridLayoutManager(this@TopListActivity,3)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.set(10, 5, 10, 5)
                }
            })
        }

        rv_more_top.apply {
            adapter = mAdapter2
            layoutManager = GridLayoutManager(this@TopListActivity,3)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.set(10, 5, 10, 5)
                }
            })
        }
    }

    fun observe() {
        //底部播放栏可见性
        mViewModel?.isDisplay?.observe(this, Observer {
            if (it!!) {
                top_list_bottom.visibility = View.VISIBLE
            }
        })

        mTopViewModel.apply {
            showList.observe(this@TopListActivity, Observer {
                nest_scroll_view.visibility = View.VISIBLE
            })
            showBar.observe(this@TopListActivity, Observer {
                if (it!!){
                    process_bar.visibility = View.VISIBLE
                }else{
                    process_bar.visibility = View.GONE
                }
            })
            showError.observe(this@TopListActivity, Observer {
                if (it!!){
                    tv_error.visibility = View.VISIBLE
                }else{
                    tv_error.visibility = View.GONE
                }
            })
            showNoNet.observe(this@TopListActivity, Observer {
                if (it!!){
                    tv_no_internet.visibility = View.VISIBLE
                }else{
                    tv_no_internet.visibility = View.GONE
                }
            })
            officeTopList.observe(this@TopListActivity, Observer {
                mAdapter1.setData(it!!)
            })
            otherTopList.observe(this@TopListActivity, Observer {
                mAdapter2.setData(it!!)
            })
        }


    }



    /**
     * 取消baseactivity的搜索功能
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    inner class MyHanlder{

        init {
            //点击底部播放栏跳转至playactivity
            top_list_bottom.setOnClickListener {
                startActivity<PlayingActivity>()
            }
            singer_top_list.setOnClickListener {
                toast("该功能暂未开放")
            }
        }



    }
}
