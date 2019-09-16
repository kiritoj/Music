package com.example.music.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.music.PlayControlReceiver
import com.example.music.PlayManger
import com.example.music.R
import kotlinx.android.synthetic.main.activity_test.*


class TestActivity : AppCompatActivity() {
    val TAG = "TestActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        Log.d(TAG, "cnm")
        bt_save.setOnClickListener {
            //val intent = Intent(PlayControlReceiver.ACTION1)
            sendBroadcast(intent)
        }


    }

}


