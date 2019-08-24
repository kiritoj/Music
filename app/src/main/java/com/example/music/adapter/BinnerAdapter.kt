package com.example.music.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.music.activity.BinnerActivity
import com.example.music.db.table.BannerTable
import org.jetbrains.anko.startActivity
import com.example.music.R
/**
 * Created by tk on 2019/8/19
 */
class BinnerAdapter(mList: ArrayList<BannerTable>,val context: Context?) : PagerAdapter() {
    val TAG = "BinnerAdapter"
    val list = ArrayList<BannerTable>()
    init {
        list.addAll(mList)
        //在开头和结尾各添加一个实现无限轮播
        list.add(0,mList[mList.size-1])
        list.add(mList[0])
    }
    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val imageView = ImageView(context)
        Glide.with(context).load(list[position].picUrl)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_loading_error)
            .into(imageView)
        container.addView(imageView)

        //点击轮播图片跳转详情页
        imageView.setOnClickListener {
            Log.d(TAG,list[position].url)
            context?.startActivity<BinnerActivity>("url" to list[position].url)
        }
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}