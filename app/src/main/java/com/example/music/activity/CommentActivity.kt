package com.example.music.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.View
import com.example.music.R
import com.example.music.adapter.CommentsAdapter
import com.example.music.viewmodel.CommentVM
import kotlinx.android.synthetic.main.activity_comment.*

class CommentActivity : BaseActivity() {
    val mViewmodel = CommentVM()
    //热门评论适配器
    val mAdapter1 = CommentsAdapter(ArrayList())
    //普通评论适配器
    val mAdapter2 = CommentsAdapter(ArrayList())
    //歌曲/歌单/专辑id及标识tag
    var mId: Long? = null
    lateinit var mTag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        toolbar.init("评论")

        mTag = intent.getStringExtra("tag")
        mId = intent.getLongExtra("id", -1)
        initRecycler()
        //获取评论
        mViewmodel.getComment(mTag, mId!!)
        observe()
    }

    fun initRecycler() {
        rv_hot_comment.apply {
            adapter = mAdapter1
            layoutManager = LinearLayoutManager(this@CommentActivity)
        }

        rv_more_comment.apply {
            adapter = mAdapter2
            layoutManager = LinearLayoutManager(this@CommentActivity)


        }
        //加载更多
        nest_scroll_view.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                p0: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (p0 != null) {
                    if (scrollY == p0.getChildAt(0).measuredHeight - p0.measuredHeight) {
                        mViewmodel.getComment(mTag, mId!!, mAdapter2.list.size)
                    }
                }

            }

        })
    }


    fun observe() {
        mViewmodel.isShow.observe(this, Observer {
            if (it!!) {
                nest_scroll_view.visibility = View.GONE
                tv_note.visibility = View.VISIBLE
            }
        })
        //加载热门评论
        mViewmodel.hotConments.observe(this, Observer {
            mAdapter1.list.clear()
            mAdapter1.list.addAll(it as ArrayList)
            mAdapter1.notifyDataSetChanged()
        })
        //加载普通评论
        mViewmodel.comments.observe(this, Observer {
            val size = mAdapter2.list.size
            mAdapter2.list.addAll(it as ArrayList)
            mAdapter2.notifyItemRangeInserted(size, it.size)
        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}
