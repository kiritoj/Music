package com.example.music.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.util.Log
import com.example.music.util.NetworkUtils
import com.example.music.model.bean.X
import com.example.music.network.ApiGenerator
import com.example.music.network.services.TopListService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by tk on 2019/9/7
 * 排行榜viewmodel
 */
class TopListVM {
    val TAG = "TopListVM"
    //显示加载错误
    val showError = MutableLiveData<Boolean>()
    //显示无网络
    val showNoNet = MutableLiveData<Boolean>()
    //显示processBar
    val showBar = MutableLiveData<Boolean>()
    //显示所有榜单
    val showList = MutableLiveData<Boolean>()
    //官方榜单列表
    val officeTopList = MutableLiveData<List<X>>()
    //其他榜单列表
    val otherTopList = MutableLiveData<List<X>>()
    //歌手榜
    val singerTopList = ObservableField<X>()

    init {
        //检查网络
        if (NetworkUtils.isConnectde()){
            getAllTopList()
        }else{
            showBar.value = false
            showNoNet.value = true
        }
    }

    //获取所有榜单
    @SuppressLint("CheckResult")
    fun getAllTopList(){
        ApiGenerator.getApiService(TopListService::class.java)
            .getAllTopList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                showBar.value = false
                //将官方榜和其他榜单拆分出来
                val mOfficeList = ArrayList<X>()
                val mOtherList = ArrayList<X>()
                it.list.forEach{
                    if (it.ToplistType.isNullOrEmpty()){
                        mOtherList.add(it)
                    }else{
                        mOfficeList.add(it)
                    }
                    Log.d(TAG,it.coverImgUrl)
                }
                officeTopList.value = mOfficeList
                otherTopList.value = mOtherList
                //将歌手榜包装成普通榜单
                singerTopList.set(
                    X(
                        "",
                        it.artistToplist.coverUrl,
                        null,
                        "",
                        0.toLong(),
                        it.artistToplist.name,
                        it.artistToplist.upateFrequency
                    )
                )
                showList.value = true


            },{
                showBar.value = false
                showError.value = true
                Log.d(TAG,"获取榜单失败${it.message}")
            })
    }

    //重新加载
    fun reLoad(){
        if (NetworkUtils.isConnectde()){
            Log.d(TAG,"test")
            showError.value = false
            showNoNet.value = false
            showBar.value = true
            getAllTopList()
        }else{
            Log.d(TAG,"网络未连接")
        }
    }



}