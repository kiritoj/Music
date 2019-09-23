package com.example.music.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.music.PlayManger

import kotlinx.android.synthetic.main.toolbar_with_search.*
import com.example.music.R

/**
 * Created by tk on 2019/8/17
 */
open class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //透明状态栏
        if (Build.VERSION.SDK_INT >= 21) {

            val decorView = getWindow().getDecorView()
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.setSystemUiVisibility(option)
            getWindow().setStatusBarColor(Color.TRANSPARENT)
        }
    }









    val toolbar get() = toolbar_with_search

    protected fun Toolbar.init(title: String,
                               @DrawableRes icon: Int = R.drawable.vector_drawable_back,
                               listener: View.OnClickListener? = android.view.View.OnClickListener { finish() }) {
        setTitleTextColor(resources.getColor(R.color.white))
        this.title = title
        setSupportActionBar(this)
        if (listener == null) {
            navigationIcon = null
        } else {
            setNavigationIcon(icon)
            setNavigationOnClickListener(listener)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.iv_search -> onSearch()
            else-> return true
        }
        return true
    }

    open fun onSearch(){
    }


}