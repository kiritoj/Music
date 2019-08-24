package com.example.music.customveiw

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.example.music.adapter.BinnerAdapter
import com.example.music.R


/**
 * Created by tk on 2019/8/19
 * 自定义viewpager组合linearlayout
 */
class AutoScrollLayout : RelativeLayout {

    lateinit var autoScrollViewPager: AutoScrollViewPager
    lateinit var layout: LinearLayout

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {

        init(context)
    }

    fun init(context: Context) {

        autoScrollViewPager = AutoScrollViewPager(context)
        layout = LinearLayout(context)
        addView(autoScrollViewPager)
    }

    fun setAdapter(mAdapter: BinnerAdapter) {
        autoScrollViewPager.adapter = mAdapter
        autoScrollViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                if (p0 == mAdapter.count-1){
                    autoScrollViewPager.setCurrentItem(1,false)
                    refreshIdot(0)
                }else if(p0 == 0){
                    autoScrollViewPager.setCurrentItem(mAdapter.count-2,false)
                    refreshIdot(mAdapter.count-3)
                }else{
                    refreshIdot(p0-1)
                }
            }

            override fun onPageScrollStateChanged(p0: Int) {

            }
        })
        autoScrollViewPager.setCurrentItem(1)
        autoScrollViewPager.start()
        initdots(mAdapter.count - 2)
    }

    /**
     * 初始化点
     */
    fun initdots(size: Int) {
        for (i in 0 until size) {
            val imageView = ImageView(context)
            val params = LinearLayout.LayoutParams(30, 30)
            params.leftMargin = 8
            params.gravity = CENTER
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.vector_drawable_point_red_)
            } else {
                imageView.setBackgroundResource(R.drawable.vector_drawable_point)
            }

            layout.addView(imageView)
        }

        val layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(ALIGN_PARENT_BOTTOM)
        layoutParams.addRule(CENTER_IN_PARENT)
        layoutParams.setMargins(0, 0, 0, 15)
        layout.setLayoutParams(layoutParams)
        addView(layout)

    }

    /**
     * 刷新下面的原点
     */
    fun refreshIdot(position: Int){
        val size = layout.childCount
        for (i in 0 until size){
            val imageview = layout.getChildAt(i)
            if (i == position){
                imageview.setBackgroundResource(R.drawable.vector_drawable_point_red_)
            }else{
                imageview.setBackgroundResource(R.drawable.vector_drawable_point)
            }
        }
    }


}