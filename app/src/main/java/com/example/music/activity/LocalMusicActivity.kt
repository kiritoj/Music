package com.example.music.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.music.R
import com.example.music.adapter.LocalMusicAdapter
import com.example.music.bean.LocalMusic
import com.example.music.viewmodel.LocalMusicVM
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_local_music.*

import org.jetbrains.anko.toast

class LocalMusicActivity : BaseActivity() {


    val processDialog by lazy { ProgressDialog(this) }
    val viewModel = LocalMusicVM()
    lateinit var adapter: LocalMusicAdapter
    lateinit var listener: LocalMusicAdapter.OnPopMoreClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_music)
        toolbar.init("本地音乐")
        observe()
        requestPermission()
    }

    fun showProgressDialog() {
        processDialog.apply {
            setMessage("正在扫描")
            setCancelable(false)
            show()
        }
    }


    /**
     * 与viewmodel事件绑定
     */
    fun observe() {
        viewModel.localMusic.observe(this, Observer {
            adapter = LocalMusicAdapter(it!!,this)
            rv_local_music.adapter = adapter
            rv_local_music.layoutManager = LinearLayoutManager(this)
        })
        viewModel.dismissDialog.observe(this, Observer {
            processDialog.dismiss()
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
    fun initListener(){
        listener = object : LocalMusicAdapter.OnPopMoreClickListener{
            override fun onPopMoreClick(music: LocalMusic) {

            }
        }
    }

}
