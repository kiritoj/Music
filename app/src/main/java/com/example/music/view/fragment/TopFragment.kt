package com.example.music.view.fragment

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.music.R
import com.example.music.adapter.MvAdapter
import com.example.music.databinding.FragmentTopmvBinding
import com.example.music.viewmodel.TopMvFragmentVM
import com.jcodecraeer.xrecyclerview.XRecyclerView
import org.jetbrains.anko.toast

/**
 * Created by tk on 2019/9/12
 * mv碎片
 */
class TopMvFragment : Fragment() {
    val TAG = "MvFragment"
    val mAdapter by lazy { MvAdapter(ArrayList(), context!!) }
    val viewmodel by lazy { TopMvFragmentVM() }

    lateinit var binding: FragmentTopmvBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_topmv, container, false)
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
                    viewmodel.loadMore(offset = mAdapter.list.size)
                }
            })
            addItemDecoration(
                DividerItemDecoration(
                    context!!,
                    DividerItemDecoration.VERTICAL
                )
            )
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
        viewmodel.showNoInternet.observe(this, Observer {
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

        //填充数据
        viewmodel.mvList.observe(this, Observer {
            val index = mAdapter.list.size
            mAdapter.list.addAll(it!!)
            binding.xrvMv.loadMoreComplete()
            mAdapter.notifyItemRangeInserted(index+1,it.size)

        })
    }


}