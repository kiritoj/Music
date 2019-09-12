package com.example.music.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.music.R

/**
 * Created by tk on 2019/9/12
 * mv碎片
 */
class MvFragment : Fragment() {
    val TAG = "MvFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mv,container,false)
        return view
    }


    companion object {
        /**
         * @param mTag mv的种类，推荐mv的数据是不变的，排行榜的数据可分页获取，最新mv的数据只能一次全部取完
         */
        fun getInstance(mTag: String): SongListFragment {
            val fragment = SongListFragment()
            Bundle().apply {
                putString("tag", mTag)
                fragment.setArguments(this)
            }
            return fragment
        }
    }
}