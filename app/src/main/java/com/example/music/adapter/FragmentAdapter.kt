package com.example.music.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

/**
 * Created by tk on 2019/8/16
 */
class FragmentAdapter(val list: List<Fragment>,val title: Array<String>,val fm: FragmentManager)
    :  FragmentPagerAdapter(fm){

    override fun getItem(p0: Int): Fragment {
        return list[p0]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment =  super.instantiateItem(container, position) as Fragment
        fm.beginTransaction().show(fragment).commit()
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val fagment = list[position]
        fm.beginTransaction().hide(fagment).commit()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}