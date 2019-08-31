package com.example.music.fragment


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.content.ContentValues
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import cn.leancloud.AVUser
import com.example.music.R
import com.example.music.activity.LocalMusicActivity
import com.example.music.activity.LoginActivity
import com.example.music.adapter.UserSongListAdapter
import com.example.music.databinding.FragmentMineBinding
import com.example.music.databinding.PopWindowSonglistMoreBinding
import com.example.music.db.table.LocalMusic
import com.example.music.db.table.SongList
import com.example.music.getScreenWidth
import com.example.music.reduceTransparency
import com.example.music.resetTransparency
import com.example.music.viewmodel.MineFragmentVM
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.dialog_input.view.*
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.pop_window_edit_name.view.*
import kotlinx.android.synthetic.main.pop_window_sure.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.litepal.LitePal

/**
 * Created by tk on 2019/8/16
 * ‘我的’fragment
 */
class MineFragment : Fragment() {

    lateinit var viewModel: MineFragmentVM
    lateinit var creatAdapter: UserSongListAdapter //创建歌单的适配器
    lateinit var collectAdapter: UserSongListAdapter//收藏歌单的适配器
    //lateinit var collectListener: UserSongListAdapter.OnSongListItemMoreListener
    lateinit var mListener: UserSongListAdapter.OnSongListItemMoreListener
    val progressDialog by lazy { ProgressDialog(context) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMineBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_mine, container, false)
        viewModel = MineFragmentVM()
        binding.viewmodel = viewModel

        //初始化recycleview
        initListener()
        creatAdapter = UserSongListAdapter(ArrayList<SongList>(), context!!)
        collectAdapter = UserSongListAdapter(ArrayList<SongList>(), context!!)
        creatAdapter.listener = mListener
        collectAdapter.listener = mListener
        binding.rvCreatedSonglist.apply {
            adapter = creatAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.rvCollectSonglist.apply {
            adapter = collectAdapter
            layoutManager = LinearLayoutManager(context)
        }

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
        iv_user_avatar.setOnClickListener { requestPermission() }
        tv_user_name.setOnClickListener { showPopupWindow() }
        ll_local_music.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    LocalMusicActivity::class.java
                )
            )
        }
        iv_add_songlist.setOnClickListener { showAddSongListWindow() }

        //退出登录
        bt_quit_login.setOnClickListener {
            //清空本地用户名和头像
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().apply {
                remove("username")
                remove("useravatar")
                apply()
            }
            //将当前用户的全部歌单清空
            LitePal.deleteAll(SongList::class.java)

            //用户退出
            AVUser.logOut()
            activity?.startActivity<LoginActivity>()
            activity?.finish()
        }

    }

    /**
     * viewmode与fragment绑定,刷新视图
     */
    fun observe() {
        viewModel.isUpLoadSuccess.observe(this, Observer {
            if (it!!) {
                activity?.toast("上传成功")
            } else {
                activity?.toast("上传失败")
            }
        })
        viewModel.isShowCreatedList.observe(this, Observer {
            if (it!!) {
                rv_created_songlist.visibility = View.VISIBLE
                ObjectAnimator.ofFloat(iv_creat_songList_more, "rotation", 0f, 90f).apply {
                    duration = 400
                    start()
                }
            } else {
                rv_created_songlist.visibility = View.GONE
                ObjectAnimator.ofFloat(iv_creat_songList_more, "rotation", 90f, 0f).apply {
                    duration = 400
                    start()
                }
            }
        })
        viewModel.isShowCollectList.observe(this, Observer {
            if (it!!) {
                rv_collect_songlist.visibility = View.VISIBLE
                ObjectAnimator.ofFloat(iv_collect_songList_more, "rotation", 0f, 90f).apply {
                    duration = 400
                    start()
                }

            } else {
                rv_collect_songlist.visibility = View.GONE
                ObjectAnimator.ofFloat(iv_collect_songList_more, "rotation", 90f, 0f).apply {
                    duration = 400
                    start()
                }
            }
        })
        viewModel.creatSongList.observe(this, Observer {
            if (it != null) {
                creatAdapter.list.clear()
                creatAdapter.list.addAll(it)
            }
            creatAdapter.notifyDataSetChanged()
        })
        viewModel.collectSongList.observe(this, Observer {
            if (it != null) {
                collectAdapter.list.clear()
                collectAdapter.list.addAll(it)
            }
            collectAdapter.notifyDataSetChanged()
        })
        viewModel.toast.observe(this, Observer {
            it?.let { it1 -> context?.toast(it1) }
        })
        viewModel.isShowProcessPar.observe(this, Observer {
            if (it!!) {
                progressDialog.apply {
                    setMessage("正在添加")
                    setCancelable(true)
                    show()
                }
            } else {
                progressDialog.dismiss()
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
    fun showPopupWindow() {
        activity?.reduceTransparency()
        val contentView = LayoutInflater.from(activity).inflate(R.layout.pop_window_edit_name, null)
        val popWindow =
            PopupWindow(
                contentView,
                (activity?.getScreenWidth()!! * 0.95).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        popWindow.apply {
            animationStyle = R.style.popwindowCenter
            isFocusable = true
            setOnDismissListener { activity?.resetTransparency() }
            showAtLocation(sl_parent, Gravity.CENTER, 0, -100)
        }
        contentView.bt_save_cancle.setOnClickListener { popWindow.dismiss() }
        contentView.bt_sure_save.setOnClickListener {
            viewModel.changeName(contentView.et_change_name.text.toString())
            popWindow.dismiss()
        }
    }

    /**
     * 添加歌单popwindow
     */
    fun showAddSongListWindow() {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_input, null, false)
        val popWindow =
            PopupWindow(
                view,
                (activity?.getScreenWidth()!! * (0.9)).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        activity?.reduceTransparency()
        popWindow.apply {
            isFocusable = true
            animationStyle = R.style.popwindowCenter
            setOnDismissListener { activity?.resetTransparency() }
            showAtLocation(sl_parent, Gravity.CENTER, 0, -100)
        }
        view.tv_cancle.setOnClickListener { popWindow.dismiss() }

        //添加歌单到云端
        view.tv_ok.setOnClickListener {
            view.et_songlist_name.text.toString().let { viewModel.addSongList(it) }
            popWindow.dismiss()
        }
    }

    /**
     * 选择头像
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

    /**
     * 点击歌单列表右侧功能键进行歌单操作
     */
    fun initListener() {
        mListener = object : UserSongListAdapter.OnSongListItemMoreListener {
            override fun onMoreClick(mSongList: SongList, position: Int) {
                activity?.reduceTransparency()
                val binding: PopWindowSonglistMoreBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.pop_window_songlist_more,
                    null,
                    false
                )
                binding.songlist = mSongList
                val popupWindow =
                    PopupWindow(
                        binding.root,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                popupWindow.apply {
                    isFocusable = true
                    animationStyle = R.style.popwindow
                    setOnDismissListener { activity?.resetTransparency() }
                    showAtLocation(sl_parent, Gravity.BOTTOM, 0, 0)
                }
                binding.llDeleteRoot.setOnClickListener {

                    popupWindow.dismiss()
                    activity?.reduceTransparency()
                    val view = LayoutInflater.from(context).inflate(R.layout.pop_window_sure, null)
                    val mPopupWindow = PopupWindow(view, 800, ViewGroup.LayoutParams.WRAP_CONTENT)
                    mPopupWindow.apply {
                        isFocusable = true
                        animationStyle = R.style.popwindowCenter
                        setOnDismissListener { activity?.resetTransparency() }
                        showAtLocation(sl_parent, Gravity.CENTER, 0, 0)
                    }
                    view.tv_sure_cancle.setOnClickListener { mPopupWindow.dismiss() }
                    view.tv_sure_ok.setOnClickListener {
                        //删除歌单
                        viewModel.deleteSongList(mSongList, position)
                        collectAdapter.delete(position)
                        mPopupWindow.dismiss()

                    }

                }
            }
        }


    }


}