package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import cn.leancloud.AVObject
import cn.leancloud.AVUser
import com.example.music.model.db.table.SongList
import com.example.music.util.checkStringIsNull
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus


/**
 * Created by tk on 2019/8/15
 */
class LoginVM : ViewModel() {

    val TAG = "LoginVM"

    val isLogining = MutableLiveData<Boolean>()//正在登录
    val isLoginSuccess = MutableLiveData<Boolean>()//是否登陆成功
    val isRegistering = MutableLiveData<Boolean>()//正在注册
    val isRegisterSuccess = MutableLiveData<Boolean>()//注册成功
    val isInoutNull = MutableLiveData<Boolean>()//输入是否为空
    val hasLogin = MutableLiveData<Boolean>()//已经登陆
    //val errorInfo = MutableLiveData<String>()//错误信息

    //登入
    fun login(name: String?, password: String?) {

        if (!checkStringIsNull(name, password)) {

            isLogining.value = true
            val disposable = AVUser.logIn(name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //登陆成功
                    isLoginSuccess.value = true
                    isLogining.value = false
                }, {
                    //登陆失败
                    isLoginSuccess.value = false
                    isLogining.value = false

                })
        } else {
            isInoutNull.value = true
        }
    }

    //注册
    fun register(name: String?, password: String?) {
        if (!checkStringIsNull(name, password)) {
            isRegistering.value = true
            val user = AVUser()// 新建 AVUser 对象实例
            user.username = name// 设置用户名
            user.password = password// 设置密码
            user.put("catSongList", listOf("全部","华语","流行","ACG","清新"))//添加默认展示的歌单类别

            val disposable = user.signUpInBackground()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isRegistering.value = false
                    isRegisterSuccess.value = true

                    //注册成功后自动登录
                    login(name, password)

                    //注册成功后自动创建默认歌单
                    AVObject("SongList").apply {
                        put("name", "我喜欢的音乐")
                        put("owner", it)
                        put("creatorName",it.username)
                        saveInBackground().subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Log.d(TAG,"默认歌单添加成功")
                            }, {
                                Log.d(TAG,it.message)
                            })
                    }

                }, {
                    isRegistering.value = false
                    isRegisterSuccess.value = false
                    Log.d(TAG,"注册失败${it.message}")
                })
        } else {
            isInoutNull.value = true
        }
    }

    /**
     * 检查用户已在登录状态
     */
    fun checkHasLogin() {
        if (AVUser.getCurrentUser() != null) {
            hasLogin.value = true
            EventBus.getDefault().post(AVUser.getCurrentUser())
        }
    }
}