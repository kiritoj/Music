package com.example.music.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.example.music.R
import com.example.music.adapter.MvAdapter
import com.example.music.event.RefreshMvEvent
import com.example.music.model.bean.MvData
import kotlinx.android.synthetic.main.activity_my_collect.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal
import org.litepal.extension.find

class MyCollectActivity : BaseActivity() {

    lateinit var mAdapter: MvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_collect)
        EventBus.getDefault().register(this)
        toolbar.init("我的收藏")
        val mvdatas = LitePal.findAll(MvData::class.java) as ArrayList
        mAdapter = MvAdapter(mvdatas,this)
        rv_collect_mv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@MyCollectActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refresh(event: RefreshMvEvent){
        mAdapter.list.clear()
        mAdapter.list.addAll(LitePal.findAll(MvData::class.java) as ArrayList)
        mAdapter.notifyDataSetChanged()
    }



}
