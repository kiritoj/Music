package com.example.music.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import cn.leancloud.AVException
import cn.leancloud.AVQuery
import cn.leancloud.callback.CloudQueryCallback
import cn.leancloud.query.AVCloudQueryResult
import com.example.music.R
import com.example.music.bean.Banner
import com.example.music.db.table.BannerTable
import com.example.music.test.Author
import com.example.music.test.Book
import kotlinx.android.synthetic.main.activity_test.*
import org.litepal.LitePal


class TestActivity : AppCompatActivity() {
    val TAG = "TestActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        Log.d(TAG,"cnm")
        bt_save.setOnClickListener {

        }


        bt_get.setOnClickListener {

        }



    }
}


