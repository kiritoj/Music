package com.example.music.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.music.PlayControlReceiver
import com.example.music.PlayManger
import com.example.music.R
import com.example.music.databinding.ActivityTestBinding
import com.example.music.viewmodel.TestVM
import kotlinx.android.synthetic.main.activity_test.*


class TestActivity : AppCompatActivity() {
    val TAG = "TestActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityTestBinding= DataBindingUtil.setContentView(this,R.layout.activity_test)
        val viewmodel = TestVM()
        binding.viewmodel = viewmodel





    }

}


