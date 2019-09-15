package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
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
class TopMvFragmentVM  : ViewModel() {

    val showNoInternet = MutableLiveData<Boolean>()
    val showProcessBar = MutableLiveData<Boolean>()
    val showError = MutableLiveData<Boolean>()
    val mvList = MutableLiveData<ArrayList<MvData>>()
    init {
        EventBus.getDefault().register(this)
        if (checkNetIsCollected()) {
            getTopMv()
        }
    }


    //检查网络连接
    fun checkNetIsCollected(): Boolean {
        showNoInternet.value = !NetworkUtils.isConnectde()
        showProcessBar.value = NetworkUtils.isConnectde()
        return NetworkUtils.isConnectde()
    }


    //获取排行榜上的mv
    fun getTopMv(limit: Int = 5, offset: Int = 0) {
        MvRepository.getTopMv(limit, offset)
    }

    //重新加载
    fun reload(){
        if (checkNetIsCollected()) {
            getTopMv()
        }
    }

    //加载更多
    fun loadMore(limit: Int = 5,offset: Int){
        getTopMv(limit, offset)
    }


    /**
     * 加载结果
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveLoadState(event: LoadEvent) {
        if (event.receiver.equals("TopMvFragmentVM")) {
            showProcessBar.value = false
            showError.value = true
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveMv(event: MvEvent){
        if (event.cat.equals("topMv")){
            showProcessBar.value = false
            mvList.value = event.mvList
        }
    }


}