package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.view.View
import com.example.music.event.LoadEvent
import com.example.music.event.MvEvent
import com.example.music.model.bean.MvData
import com.example.music.model.db.repository.MvRepository
import com.example.music.util.NetworkUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by tk on 2019/9/13
 */
class NewMvFragmentVM : ViewModel(){
    val TAG = "NewMvFragmentVM"
    val showNoNet = MutableLiveData<Boolean>()
    val showProcessBar = MutableLiveData<Boolean>()
    val showError = MutableLiveData<Boolean>()
    val showNoMore = MutableLiveData<Boolean>()
    //接口只有57条数据且没有分页，通过手动赋值的方式实现分页加载
    val mvListData = MutableLiveData<ArrayList<MvData>>()
    //所有mv数据
    var mvList = ArrayList<MvData>()
    var index = 0 //已经取出mv条数

    init {
        EventBus.getDefault().register(this)
        if (checkNetCollect()) {
            getNewMv()
        }
    }

    //检查网络连接
    fun checkNetCollect(): Boolean {
        showNoNet.value = !NetworkUtils.isConnectde()
        showProcessBar.value = NetworkUtils.isConnectde()
        return NetworkUtils.isConnectde()
    }

    //获取最新mv
    fun getNewMv() {
        MvRepository.getNewMv()
    }

    //加载更多,每次加载5条数据
    fun loadMore() {

        if (index <= 52) {
            mvListData.value = ArrayList(mvList.subList(index, index + 5))
            index += 5
        }else if(index < 57){
            //不足5条数据
            mvListData.value = ArrayList(mvList.subList(index,57))
            index = 57
        }else{
            //已经取完
            showNoMore.value = true
        }
    }

    //重新加载
    fun reload(){
        if (checkNetCollect()){
            Log.d(TAG,"重新加载")
            showProcessBar.value = true
            showError.value = false
            showNoNet.value = false
            showNoMore.value = false
            getNewMv()
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveNewMv(event: MvEvent) {
        if (event.cat.equals("newMv")) {
            showProcessBar.value = false
            mvList = event.mvList
            mvListData.value = ArrayList(mvList.subList(0,5))
            index = 5
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveLoadState(event: LoadEvent) {
        if (event.receiver.equals("NewMvFragment")) {
            showError.value = true
            showProcessBar.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }


}