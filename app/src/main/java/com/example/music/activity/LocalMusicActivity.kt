package com.example.music.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.example.music.*
import com.example.music.adapter.CollectToSongListAdapter
import com.example.music.adapter.LocalMusicAdapter

import com.example.music.db.table.SongList
import com.example.music.databinding.PopWindowLocalMusicBinding
import com.example.music.databindingadapter.getAlbumArt
import com.example.music.db.table.LocalMusic
import com.example.music.event.RefreshEvent
import com.example.music.viewmodel.LocalMusicVM
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_local_music.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pop_window_collect_to_songlist.view.*
import kotlinx.android.synthetic.main.pop_window_delete_with_checkbox.view.*
import kotlinx.android.synthetic.main.song_info_button.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import org.jetbrains.anko.startActivity

class LocalMusicActivity : BaseActivity() {


    val viewModel = LocalMusicVM()
    lateinit var adapter: LocalMusicAdapter
    val mAdapter by lazy { CollectToSongListAdapter(ArrayList(), this) }
    lateinit var mListener: LocalMusicAdapter.OnPopMoreClickListener
    val processDialog by lazy { ProgressDialog(this) }
    val popupWindow by lazy { PopupWindow() }
    val builder by lazy { AlertDialog.Builder(this) }
    lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_music)
        EventBus.getDefault().register(this)
        toolbar.init("本地音乐")
        observe()
        initRecycle()
        requestPermission()
    }

    fun showProgressDialog() {
        processDialog.apply {
            setMessage("正在扫描")
            setCancelable(false)
            show()
        }
    }


    fun initRecycle() {
        adapter = LocalMusicAdapter(ArrayList<LocalMusic>(), this)
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
        })
        viewModel.toast.observe(this, Observer {
            toast(it!!)
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
     * 歌曲右侧导航button弹出，选择删除or添加到歌单
     */
    fun initListener() {
        mListener = object : LocalMusicAdapter.OnPopMoreClickListener {
            override fun onPopMoreClick(music: LocalMusic, position: Int) {
                //数据绑定
                val binding: PopWindowLocalMusicBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(this@LocalMusicActivity),
                    R.layout.pop_window_local_music,
                    null,
                    false
                )
                binding.music = music
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

                //删除，弹出确认对话框
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
     * 重新扫描
     */
    override fun onSearch() {
        super.onSearch()
        showProgressDialog()
        viewModel.reScanning()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(event:RefreshEvent){
        local_song_bottom.visibility = View.VISIBLE
        adapter.refreshPlayId(event.position)
        local_song_bottom.setOnClickListener { startActivity<PlayingActivity>("song" to event.song) }
        if (event.song.albumID!=null){
            iv_song_cover.setImageBitmap(getAlbumArt(event.song.albumID!!,this))
        }else{
            Glide.with(this).load(event.song.coverUrl).into(iv_song_cover)
        }
        tv_song_name.text = event.song.songName
        tv_singer_name.text = event.song.singerName

        //暂停
        var pause: Boolean = false
        iv_pause.setOnClickListener {
            if (pause){
                EventBus.getDefault().post(PlayManger.State.PLAY)
                iv_pause.setImageResource(R.drawable.vector_drawable_play_black)
                pause = false
            }else{
                EventBus.getDefault().post(PlayManger.State.PAUSE)
                iv_pause.setImageResource(R.drawable.vector_drawable_pause_black)
                pause = true
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}
