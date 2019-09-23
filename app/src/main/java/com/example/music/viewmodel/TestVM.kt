package com.example.music.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField

/**
 * Created by tk on 2019/9/18
 */
class TestVM : ViewModel(){
    val isVisiable = ObservableField<Boolean>(false)
    fun changeV(){
        isVisiable.set(true)
    }
}