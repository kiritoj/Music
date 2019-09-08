package com.example.music.view.fragment

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.music.R
import com.example.music.adapter.SongListAdapter
import com.example.music.event.AssortedSongListEvent
import com.example.music.viewmodel.SongListFragmentVM
import com.jcodecraeer.xrecyclerview.XRecyclerView
import kotlinx.android.synthetic.main.fragment_songlist.*
import kotlinx.android.synthetic.main.fragment_songlist.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by tk on 2019/8/21
 */
class SongListFragment : Fragment() {
    val TAG = "SongListFragment"
    val mViewModel by lazy {SongListFragmentVM()}
    lateinit var mAdapter: SongListAdapter
    lateinit var  recycle: XRecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_songlist,container,false)
        mAdapter = SongListAdapter(ArrayList(),context!!)
        mViewModel.getAssortedSongList(cat = getCategory())
        EventBus.getDefault().register(this)
        recycle = view.rv_songlist
        initRecycler()
        return view
    }

   fun initRecycler() {

       recycle.apply {
           adapter = mAdapter
           layoutManager = GridLayoutManager(context, 3)
           addItemDecoration(object : RecyclerView.ItemDecoration() {
               override fun getItemOffsets(
                   outRect: Rect,
                   view: View,
                   parent: RecyclerView,
                   state: RecyclerView.State
               ) {
                   outRect.set(10, 5, 10, 5)
               }
           })

           setPullRefreshEnabled(false)
           //加载监听
           setLoadingListener(object : XRecyclerView.LoadingListener {
               override fun onRefresh() {}
               override fun onLoadMore() {
                   mViewModel.loadMore(getCategory())
               }
           })
       }
   }





    companion object {
        fun getInstance(cat: String): SongListFragment {
            val fragment = SongListFragment()
            Bundle().apply {
                putString("category", cat)
                fragment.setArguments(this)
            }
            return fragment
        }
    }

    fun getCategory() : String{
        val args = arguments
        return args!!.getString("category")

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveAssortedList(event: AssortedSongListEvent) {
        if (event.tag.equals(getCategory())) {
            val size = mAdapter.list.size
            mAdapter.list.addAll(event.list)
            mAdapter.notifyItemRangeInserted(size,18)
            recycle.loadMoreComplete()
            process_bar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}