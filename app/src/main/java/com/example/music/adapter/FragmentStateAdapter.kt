package com.example.music.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter


/**
 * Created by tk on 2019/8/21
 */
class FragmentStateAdapter(val list: List<Fragment>,var title: ArrayList<String>,val fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(i: Int): Fragment {
        return list[i]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        //给viewpager的每个界面设置title
        return title[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }


}