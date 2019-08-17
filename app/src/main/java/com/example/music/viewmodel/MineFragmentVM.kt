package com.example.music.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.preference.PreferenceManager
import com.example.music.checkStringIsNull
import cn.leancloud.AVFile
import cn.leancloud.AVUser
import com.example.music.DEFAULT_AVATAR
import com.example.music.MusicApp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream

/**
 * Created by tk on 2019/8/16
 */
class MineFragmentVM : ViewModel() {

    val mUserAvatar = ObservableField<String>(DEFAULT_AVATAR)//头像
    val mUserName = ObservableField<String>()//用户名
    val currentUser: AVUser = AVUser.currentUser()
    val isUpLoadSuccess = MutableLiveData<Boolean>()

    init {

        //加载用户头像和昵称，首先从本地加载
        val preference = PreferenceManager.getDefaultSharedPreferences(MusicApp.context)
        val name = preference.getString("username","")
        val avatar = preference.getString("useravatar","")
        if (checkStringIsNull(name)){
            //从网络拉取用户名
            mUserName.set(currentUser.username)
            //保存用户名
            preference.edit().apply{
                putString("username",mUserName.get())
                apply()
            }
        }else{
            mUserName.set(name)
        }
        if (checkStringIsNull(avatar)){
            //从网络获取头像链接
            currentUser.getAVFile("avatar")?.url?.let {mUserAvatar.set(it)}
            preference.edit().apply{
                putString("useravatar",mUserAvatar.get())
                apply()
            }
        }else{
            mUserAvatar.set(avatar)
        }
    }

    /**
     * 上传头像
     *
     */
    @SuppressLint("CheckResult")
    fun uploadAvatar(bitmap: Bitmap){
        val outputStream = ByteArrayOutputStream(bitmap.byteCount)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        //上传图片，返回url
        val file = AVFile("avatar.png",outputStream.toByteArray())
        file.saveInBackground().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                mUserAvatar.set(it.url)
                PreferenceManager.getDefaultSharedPreferences(MusicApp.context).edit().apply {
                    putString("useravatar", it.url)
                    apply()
                }
                isUpLoadSuccess.value = true
            },{
                isUpLoadSuccess.value = false
            })


        //与当前用户关联
        currentUser.apply {
            put("avatar",file)
            saveInBackground()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    bitmap.recycle()
                })
        }
    }

    /**
     * 上传昵称
     */
    @SuppressLint("CheckResult")
    fun changeName(newName: String){
        mUserName.set(newName)
        if (!checkStringIsNull(newName)){
            currentUser.put("username",newName)
            currentUser.saveInBackground().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    PreferenceManager.getDefaultSharedPreferences(MusicApp.context).edit().apply {
                        putString("username",newName)
                        apply()
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

}