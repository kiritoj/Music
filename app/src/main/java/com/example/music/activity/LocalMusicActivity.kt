package com.example.music.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.example.music.*
import com.example.music.adapter.CollectToSongListAdapter
import com.example.music.adapter.LocalMusicAdapter
import com.example.music.databinding.ActivityLocalMusicBinding
import com.example.music.db.table.SongList
import com.example.music.databinding.PopWindowLocalMusicBinding
import com.example.music.db.table.LocalMusic
import com.example.music.viewmodel.BottomStateBarVM
import com.example.music.viewmodel.LocalMusicVM
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_local_music.*
import kotlinx.android.synthetic.main.pop_window_collect_to_songlist.view.*
import kotlinx.android.synthetic.main.pop_window_delete_with_checkbox.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * 展示本地音乐
 */
class LocalMusicActivity : BaseActivity() {

    val TAG = "LocalMusicActivity"
    //处理本地歌曲avtivity的VM
    val viewModel = LocalMusicVM()
    //本地歌曲recycleview adapter
    lateinit var adapter: LocalMusicAdapter
    //本地歌曲item右侧多功能键点击listener
    lateinit var mListener: LocalMusicAdapter.OnPopMoreClickListener
    //弹出收藏到歌单，歌单recycleview适配器
    val mAdapter by lazy { CollectToSongListAdapter(ArrayList(), this) }
    //添加中dialog
    val processDialog by lazy { ProgressDialog(this) }
    //弹出已有歌单
    val popupWindow by lazy { PopupWindow() }
    //底部播放栏的VM
    val mViewModel = BottomStateBarVM.get()
    lateinit var alertDialog: AlertDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLocalMusicBinding =
            DataBindingUtil.setContentView(this,R.layout.activity_local_music)

        //绑定底部播放栏VM
        binding.viewmodel = mViewModel
        //检查播放栏可见性
        mViewModel?.checkMusicPlaying()

        toolbar.init("本地音乐")

        observe()
        //oncreat初始化recycleview，防止skip layout异常
        initRecycle()
        //请求读写权限
        requestPermission()
        //底部播放栏点击跳转至播放活动
        local_song_bottom.setOnClickListener {
            startActivity<PlayingActivity>()
        }
    }

    fun showProgressDialog() {
        processDialog.apply {
            setMessage("正在扫描")
            setCancelable(false)
            show()
        }
    }


    fun initRecycle() {
        adapter = LocalMusicAdapter(ArrayList<LocalMusic>(), this,TAG)
        initListener()
        adapter.listener = mListener
        rv_local_music.adapter = adapter
        rv_local_music.layoutManager = LinearLayoutManager(this)
    }


    /**
     * 与viewmodel事件绑定
     */
    fun observe() {
        viewModel.localMusic.observe(this, Observer {
            it?.let { it1 -> adapter.list.addAll(it1) }
            adapter.notifyDataSetChanged()
        })
        viewModel.dismissDialog.observe(this, Observer {
            processDialog.dismiss()
        })
        viewModel.creatSongList.observe(this, Observer {
            mAdapter.list.clear()
            it?.let { it1 -> mAdapter.list.addAll(it1) }
            mAdapter.notifyDataSetChanged()
        })
        viewModel.toast.observe(this, Observer {
            toast(it!!)
        })

        //底部播放栏
        mViewModel?.isDisplay?.observe(this, Observer {
            if (it!!){
                local_song_bottom.visibility = View.VISIBLE
            }
        })
        //更新adapter中正在播放的位置
        mViewModel?.index?.observe(this, Observer {
            if (it!! > -1){
                adapter.refreshPlayId(it)
            }
        })

    }


    /**
     * 权限操作
     */
    @SuppressLint("CheckResult")
    fun requestPermission() {
        RxPermissions(this)
            .request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe {
                if (it) {
                    showProgressDialog()
                    viewModel.getLocalMusic()
                } else {
                    toast("拒绝授权将无法扫描本地歌曲")
                }
            }
    }

    /**
     * 歌曲右侧多功能popwindow弹出，选择删除or添加到歌单
     */
    fun initListener() {
        mListener = object : LocalMusicAdapter.OnPopMoreClickListener {
            override fun onPopMoreClick(music: LocalMusic, position: Int) {
                //与popwindow布局绑定
                val binding: PopWindowLocalMusicBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(this@LocalMusicActivity),
                    R.layout.pop_window_local_music,
                    null,
                    false
                )
                binding.music = music
                //popwindow弹出减低屏幕透明度(变暗)
                reduceTransparency()
                popupWindow.apply {
                    contentView = binding.root
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    isFocusable = true
                    animationStyle = R.style.popwindow
                    setOnDismissListener { resetTransparency() }
                    showAtLocation(ll_local_music_root_view, Gravity.BOTTOM, 0, 0)
                }

                //删除该歌曲，弹出确认对话框
                binding.llLocalMusicDelete.setOnClickListener {
                    popupWindow.dismiss()
                    reduceTransparency()
                    val view = LayoutInflater.from(this@LocalMusicActivity)
                        .inflate(R.layout.pop_window_delete_with_checkbox, null)
                    val mPopupWindow =
                        PopupWindow(view, (getScreenWidth() * 0.9).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
                    mPopupWindow.apply {
                        isFocusable = true
                        animationStyle = R.style.popwindowCenter
                        setOnDismissListener { resetTransparency() }
                        showAtLocation(ll_local_music_root_view, Gravity.CENTER, 0, 0)
                    }
                    view.tv_check_cancle.setOnClickListener { mPopupWindow.dismiss() }
                    view.tv_check_ok.setOnClickListener {

                        adapter.delete(position)
                        viewModel.deleteMusic(music, position,view.cb_source.isChecked)
                        mPopupWindow.dismiss()
                    }

                }

                //添加到歌单
                binding.llLocalMusicAdd.setOnClickListener {
                    viewModel.getSonList()
                    popupWindow.dismiss()
                    reduceTransparency()
                    //弹出歌单pop
                    val view = LayoutInflater.from(this@LocalMusicActivity)
                        .inflate(R.layout.pop_window_collect_to_songlist, null)

                    var mPopupWindow = PopupWindow()
                    //点击歌单进行保存工作
                    val mListener = object : CollectToSongListAdapter.OnSongListClickListener {
                        override fun onSongListClick(sonList: SongList) {
                            viewModel.saveToSongList(music, sonList)
                            mPopupWindow.dismiss()

                        }
                    }
                    mAdapter.listener = mListener
                    view.rv_collect_to_songlist.apply {
                        adapter = mAdapter
                        layoutManager = LinearLayoutManager(this@LocalMusicActivity)
                    }

                    mPopupWindow.apply {
                        contentView = view
                        width = (getScreenWidth() * 0.9).toInt()
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                        isFocusable = true
                        animationStyle = R.style.popwindowCenter
                        setOnDismissListener { resetTransparency() }
                        showAtLocation(ll_local_music_root_view, Gravity.CENTER, 0, 0)
                    }
                }


            }
        }
    }

    /**
     * 重新扫描本地音乐
     */
    override fun onSearch() {
        super.onSearch()
        showProgressDialog()
        viewModel.reScanning()
    }





}
