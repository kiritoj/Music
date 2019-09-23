package com.example.music.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import cn.leancloud.AVObject
import com.example.music.MusicApp
import com.example.music.event.LoadEvent
import com.example.music.event.RefreshMvEvent
import com.example.music.model.bean.*
import com.example.music.model.db.repository.CommentRepository
import com.example.music.model.db.repository.MvRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal

/**
 * Created by tk on 2019/9/14
 */
class MvDetailVM : ViewModel() {
    val TAG = "MvDetailVM"
    //是否展开详细介绍
    val isOpenDesc = ObservableField<Boolean>(false)
    //加载失败
    val isError = ObservableField<Boolean>(false)
    //mv的完整信息
    val mv = ObservableField<MvDetailData>()
    //显示peocessbar
    val showProcessBar = ObservableField<Boolean>(true)
    //相关mv
    val relatedMv = MutableLiveData<List<MvData>>()
    //评论列表
    val comments = MutableLiveData<ArrayList<Comment>>()

    //是否收藏
    val isCollect = ObservableField<Boolean>(false)
    //
    lateinit var mvData: MvData
    //提示
    val toast = MutableLiveData<String>()
    init {
        EventBus.getDefault().register(this)
    }

    fun getAllAboutMv(data: MvData){
        mvData = data
        getMvDetail(data.mvId)
        getRelatedMv(data.mvId)
        getMvComment(data.mvId)
        checkIsCollect(data.mvId)

    }

    //重新加载
    fun reload(data: MvData){
        isError.set(false)
        showProcessBar.set(true)
        getAllAboutMv(data)
    }

    //检查是否收藏
    fun checkIsCollect(id: Long){
        val mv = LitePal.where("mvId = ?",id.toString()).findFirst(MvData::class.java)
        isCollect.set(mv != null)
    }

    //收藏mv
    @SuppressLint("CheckResult")
    fun collectMv(){
        if (isCollect.get()!!){
            Log.d(TAG,"改mv已收藏，取消收藏")
            //从本地数据库删除
            val mMv = LitePal.where("mvId = ?",mvData.mvId.toString()).findFirst(MvData::class.java)
            mMv.delete()
            isCollect.set(false)
            //从云端删除
            AVObject.createWithoutData("Mv",mMv.objectId)
                .deleteInBackground()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    toast.value = "已取消收藏"
                }, {
                    Log.d(TAG, it.message)
                })
            //刷新列表
            EventBus.getDefault().post(RefreshMvEvent(true))
        }else {
            //保存到本地
            isCollect.set(true)
            //保存到云端
            AVObject("Mv").apply {
                put("artistId", mvData.artistId)
                put("artistName", mvData.artistName)
                put("briefDesc", mvData.briefDesc)
                put("cover", mvData.cover)
                put("mvId", mvData.mvId)
                put("name", mvData.name)
                put("playCount", mvData.playCount)
                put("owner",MusicApp.currentUser)
                saveInBackground().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        //保存到本地数据库
                        mvData.apply {
                            objectId = it.objectId
                            save()
                        }
                        //刷新歌单列表
                        //EventBus.getDefault().post(RefreshSongList("collect", true))
                        toast.value = "收藏成功"

                    }, {
                        //showAddDialog.value = false
                        toast.value = "添加失败"
                        Log.d(TAG, it.message)

                    })
            }
        }

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
        isOpenDesc.set(!isOpenDesc.get()!!)
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveMvDetail(mvDetail: MvDetailData){
        mv.set(mvDetail)
        Log.d(TAG,mvDetail.publishTime)
        showProcessBar.set(false)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveLoadState(event: LoadEvent){
        if (event.receiver.equals("MvDetailVM")){
            isError.set(true)
            showProcessBar.set(false)
        }
    }

    @Subscribe
    fun receiveRelatedMv(mvs: RelatedMvBean){
        relatedMv.value = mvs.mvs
        showProcessBar.set(false)
    }

    @Subscribe
    fun receiveComments(commentBean: CommentBean){
        comments.value = commentBean.comments as ArrayList
    }

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
    }
}