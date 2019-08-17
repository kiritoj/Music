package com.example.music.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.widget.TableLayout
import android.widget.TextView
import com.example.music.R
import com.example.music.adapter.FragmentAdapter
import com.example.music.fragment.FindFragment
import com.example.music.fragment.MineFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import org.jetbrains.anko.find

class MainActivity : BaseActivity() {

    val list = ArrayList<Fragment>()
    lateinit var adapter: FragmentAdapter
    lateinit var tabListener : TabLayout.OnTabSelectedListener
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolBar()
        initFragment()
        initTabListener()
        tl_main.setupWithViewPager(vp_main)
        tl_main.addOnTabSelectedListener(tabListener)
        tl_main.getTabAt(1)?.select()


    }

    fun initToolBar(){
        setSupportActionBar(tb_main)
        supportActionBar?.setTitle("")
        textView = LayoutInflater.from(this@MainActivity)
            .inflate(R.layout.tab_title, null) as TextView


    }

    fun initFragment(){
        list.add(MineFragment())
        list.add(FindFragment())
        adapter = FragmentAdapter(list, arrayOf("我的","发现"),supportFragmentManager)
        vp_main.adapter = adapter

    }

    fun initTabListener(){
        tabListener = object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                p0?.customView = null
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                textView.text = p0?.text
                p0?.customView = textView
            }
        }
    }

    //加载toolbar上action按钮
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }




}
