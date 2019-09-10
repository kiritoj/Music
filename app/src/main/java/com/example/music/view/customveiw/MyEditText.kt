package com.example.music.view.customveiw

/**
 * Created by tk on 2019/8/15
 */

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

internal class MyEditView : android.support.v7.widget.AppCompatEditText, TextWatcher, View.OnFocusChangeListener {
    private var isfous: Boolean = false//是否获得焦点
    private var drawable: Drawable? = null//右侧图标

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        //获取右边的drawable
        drawable = compoundDrawables[2]
        if (drawable == null) {
            return
        }
        drawable!!.setBounds(0, 0, 40, 40)
        //初始化把清除按钮隐藏
        setCompoundDrawables(compoundDrawables[0], null, null, null)
        //焦点改变的监听
        onFocusChangeListener = this
        addTextChangedListener(this)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        //当手指抬起即认为点击了
        if (event.action == MotionEvent.ACTION_UP) {

            val x = event.x.toInt()//点击位置距离editview左上角x的距离
            val y = event.y.toInt()
            //判断有没有点击到右侧clean的图标
            if (istouchclean(x, y)) {
                //清空输入
                setText("")
            }

        }
        return super.onTouchEvent(event)
    }

    fun istouchclean(x: Int, y: Int): Boolean {
        val left = width - totalPaddingRight//控件的宽度-图标左边沿的距离到控件右边沿的距离
        val right = right - paddingRight
        //x,y是以控件左上角为原点，不是父容器，所以这里top不能用getTop
        val top = paddingTop
        val bottom = top + drawable!!.intrinsicHeight
        return x > left && x < right && y > top && y < bottom
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (isfous) {
            if (s.length > 0) {
                setCompoundDrawables(compoundDrawables[0], null, drawable, null)

            } else {
                setCompoundDrawables(compoundDrawables[0], null, null, null)
            }
        }

    }

    override fun afterTextChanged(s: Editable) {

    }

    override//当焦点改变时，相应地改变drawleft的显示与隐藏
    fun onFocusChange(v: View, hasFocus: Boolean) {

        isfous = hasFocus
        if (hasFocus) {


            //获得焦点且输入框内字符长度>0才显示出来
            if (text!!.length > 0) {
                setCompoundDrawables(compoundDrawables[0], null, drawable, null)
            }
        } else {
            setCompoundDrawables(compoundDrawables[0], null, null, null)

        }
    }


}
