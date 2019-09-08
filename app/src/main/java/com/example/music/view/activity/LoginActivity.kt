package com.example.music.view.activity

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.music.R
import com.example.music.model.db.table.User
import com.example.music.viewmodel.LoginVM
import com.example.music.databinding.ActivityLoginBinding
import org.jetbrains.anko.toast

/**
 * 登录activity
 */
class LoginActivity : AppCompatActivity() {


    val progressDialog by lazy { ProgressDialog(this) }
    val mViewModel = LoginVM()
    val user = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding: ActivityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        mBinding.user = user
        mBinding.viewmodel = mViewModel
        observe()
        mViewModel.checkHasLogin()

    }

    fun observe(){
        mViewModel.isLogining.observe(this, Observer {
            if (it!!){
                progressDialog.apply {
                    setMessage("正在登录")
                    setCancelable(false)
                    show()
                }
            }else{
                progressDialog.dismiss()
            }
        })

        mViewModel.isRegistering.observe(this, Observer {
            if (it!!){
                progressDialog.apply {
                    setMessage("正在注册")
                    show()
                }
            }else{
                progressDialog.dismiss()
            }
        })

        mViewModel.isLoginSuccess.observe(this, Observer {
            if (it!!){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else{
                toast("登录失败")
            }
        })
        mViewModel.isRegisterSuccess.observe(this, Observer {
            if (it!!){
                //注册成功后会直接使用注册信息登录
            }else{
                toast("注册失败")
            }
        })
        mViewModel.isInoutNull.observe(this, Observer {
            toast("用户名或密码不能为空")
        })
        mViewModel.hasLogin.observe(this, Observer {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        })

    }
}
