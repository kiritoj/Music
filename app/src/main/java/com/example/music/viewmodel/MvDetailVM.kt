package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import com.example.music.event.LoadEvent
import com.example.music.model.bean.*
import com.example.music.model.db.repository.CommentRepository
import com.example.music.model.db.repository.MvRepository
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by tk on 2019/9/14
 */
class MvDetailVM : ViewModel() {
    val TAG = "MvDetailVM"
    //是否展开详细介绍
    val isOpenDesc = MutableLiveData<Boolean>()
    //加载失败
    val isError = MutableLiveData<Boolean>()
    //mv的完整信息
    val mv = ObservableField<MvDetailData>()
    //显示peocessbar
    val showProcessBar = MutableLiveData<Boolean>()
    //相关mv
    val relatedMv = MutableLiveData<List<MvData>>()
    //评论列表
    val comments = MutableLiveData<ArrayList<Comment>>()
    init {
        EventBus.getDefault().register(this)
        isOpenDesc.value = false
    }

    fun getAllAboutMv(id: Long){
        getMvDetail(id)
        getRelatedMv(id)
        getMvComment(id)

    }

    //获取mv详情
    fun getMvDetail(id: Long){
        MvRepository.getMvDetail(id)
    }

    //获取相关mv
    fun getRelatedMv(id: Long){
        MvRepository.getRelatedMv(id)
    }

    //获取mv评论
    fun getMvComment(id:Long,limit: Int = 20,offset: Int = 0){
        CommentRepository.getMvComment(id, limit, offset)
    }

    //展开/收起详细介绍
    fun openDesc(){
        isOpenDesc.value = !isOpenDesc.value!!
    }

    //加载更过评论


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveMvDetail(mvDetail: MvDetailData){
        mv.set(mvDetail)
        showProcessBar.value = false

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveLoadState(event: LoadEvent){
        if (event.receiver.equals("MvDetailVM")){
            isError.value = true
            showProcessBar.value = false
        }
    }

    @Subscribe
    fun receiveRelatedMv(mvs: RelatedMvBean){
        relatedMv.value = mvs.mvs
        showProcessBar.value = false
    }

    @Subscribe
    fun receiveComments(commentBean: CommentBean){
        comments.value = commentBean.comments as ArrayList
    }

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
    }
}