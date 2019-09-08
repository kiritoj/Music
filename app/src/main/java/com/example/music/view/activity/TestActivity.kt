package com.example.music.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.music.R
import kotlinx.android.synthetic.main.activity_test.*


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


