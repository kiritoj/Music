package com.example.music.view.fragment

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.music.R
import com.example.music.adapter.MvAdapter
import com.example.music.databinding.FragmentMvBinding
import com.example.music.viewmodel.NewMvFragmentVM
import com.jcodecraeer.xrecyclerview.XRecyclerView
import org.jetbrains.anko.toast

import android.support.v7.widget.DividerItemDecoration


/**
 * Created by tk on 2019/9/13
 */
class NewMvFragment : Fragment() {

    val viewmodel by lazy { NewMvFragmentVM() }
    //mv适配器
    val mAdapter by lazy { MvAdapter(ArrayList(), context!!) }
    lateinit var binding: FragmentMvBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mv, container, false)
        binding.viewmodel = viewmodel
        observe()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //初始化recycleview
        binding.xrvMv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
            setPullRefreshEnabled(false)
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onRefresh() {}
                override fun onLoadMore() {
                    viewmodel.loadMore()
                }
            })

        }


    }

    fun observe() {
        //加载错误提示
        viewmodel.showError.observe(this, Observer {
            if (it!!){
                binding.tvError.visibility = View.VISIBLE
            }else{
                binding.tvError.visibility = View.GONE
            }
        })
        //无网络提示
        viewmodel.showNoNet.observe(this, Observer {
            if (it!!){
                binding.tvNoInternet.visibility = View.VISIBLE
            }else{
                binding.tvNoInternet.visibility = View.GONE
            }
        })
        //progressbar
        viewmodel.showProcessBar.observe(this, Observer {
            if (it!!){
                binding.processBar.visibility = View.VISIBLE
            }else{
                binding.processBar.visibility = View.INVISIBLE
            }
        })

        //没有更多了
        viewmodel.showNoMore.observe(this, Observer {
            binding.xrvMv.loadMoreComplete()
        })

        //填充数据
        viewmodel.mvListData.observe(this, Observer {
            val index = mAdapter.list.size
            mAdapter.list.addAll(it!!)
            binding.xrvMv.loadMoreComplete()
            mAdapter.notifyItemRangeInserted(index+1,it.size)

        })
    }


}