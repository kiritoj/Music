package com.example.music.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.toolbar_with_search.*
import com.example.music.R
import org.greenrobot.eventbus.EventBus

/**
 * Created by tk on 2019/8/17
 */
open class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        //注册eventbus
        EventBus.getDefault().register(this)
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

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}