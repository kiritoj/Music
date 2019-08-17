package com.example.music.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.example.music.R
import com.example.music.activity.LocalMusicActivity
import com.example.music.databinding.FragmentMineBinding
import com.example.music.getScreenWidth
import com.example.music.reduceTransparency
import com.example.music.resetTransparency
import com.example.music.viewmodel.MineFragmentVM
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.pop_window_edit_name.view.*
import org.jetbrains.anko.toast

/**
 * Created by tk on 2019/8/16
 * ‘我的’fragment
 */
class MineFragment : Fragment(){

    lateinit var viewModel: MineFragmentVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMineBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_mine, container, false)
        viewModel = MineFragmentVM()
        binding.viewmodel = viewModel
        observe()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initClick()
    }


    /**
     * 初始化部分控件的点击事件
     */
    fun initClick() {
        iv_user_avatar.setOnClickListener{requestPermission()}
        tv_user_name.setOnClickListener{showPopupWindow()}
        ll_local_music.setOnClickListener { startActivity(Intent(activity,LocalMusicActivity::class.java)) }

    }

    /**
     * viewmode与fragment绑定
     */
    fun observe(){
        viewModel.isUpLoadSuccess.observe(this, Observer {
            if (it!!){
                activity?.toast("上传成功")
            }else{
                activity?.toast("上传失败")
            }
        })
    }
    /**
     * 权限操作
     */
    @SuppressLint("CheckResult")
    fun requestPermission() {
        RxPermissions(this)
            .request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe {
                if (it) {
                    //选择图片
                    selectPic()
                } else {
                    activity?.toast("拒绝授权将无法打开相册")
                }
            }
    }

    //更改昵称，弹出popwindow
    fun showPopupWindow(){
        activity?.reduceTransparency()
        val contentView = LayoutInflater.from(activity).inflate(R.layout.pop_window_edit_name,null)
        val popWindow = PopupWindow(contentView,(activity?.getScreenWidth()!! *0.95).toInt(),ViewGroup.LayoutParams.WRAP_CONTENT)
        popWindow.apply {
            animationStyle = R.style.popwindow
            isFocusable = true
            setOnDismissListener { activity?.resetTransparency() }
            showAtLocation(sl_parent, Gravity.BOTTOM, 0, 50)
        }
        contentView.bt_save_cancle.setOnClickListener { popWindow.dismiss() }
        contentView.bt_sure_save.setOnClickListener {
            viewModel.changeName(contentView.et_change_name.text.toString())
            popWindow.dismiss()
        }
    }

    /**
     * 选择图片
     */
    fun selectPic() {
        Matisse.from(this)
            .choose(MimeType.allOf())
            .countable(false)
            .maxSelectable(1)
            .capture(true)
            .captureStrategy(CaptureStrategy(true, "com.example.music.fileprovider"))
            .imageEngine(GlideEngine())
            .forResult(2)
    }

    /**
     * 选取图片后的操作，设置本地头像和上传头像
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK) {
            val uris = Matisse.obtainResult(data!!)
            iv_user_avatar.setImageURI(uris[0])
            val bitmap = (iv_user_avatar.getDrawable() as BitmapDrawable).bitmap

            //上传头像
            viewModel.uploadAvatar(bitmap)
        }
    }

}