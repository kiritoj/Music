package com.example.music.view.customveiw

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.music.R
import kotlinx.android.synthetic.main.custom_imag_and_text_layout.view.*


/**
 * Created by tk on 2019/9/14
 * 组合自定义控件，图标加下面的数字
 */
class ImageWithTextView : FrameLayout {

    lateinit var imageView: ImageView
    lateinit var textView: TextView

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr){
        init(context,attrs)
    }

    fun init(context: Context?,attrs: AttributeSet?){
        val view = LayoutInflater.from(context).inflate(R.layout.custom_imag_and_text_layout,this,true)
        imageView = view.iv_image
        textView = view.tv_text
        val typeArray = context!!.obtainStyledAttributes(attrs,R.styleable.ImageWithTextView)
        imageView.setImageResource(typeArray.getResourceId(R.styleable.ImageWithTextView_icon,R.drawable.loading_01))
        textView.text = typeArray.getInt(R.styleable.ImageWithTextView_text,0).toString()
        typeArray.recycle()
    }

    fun setImag(resourceId: Int){
        imageView.setImageResource(resourceId)
    }

    fun setText(num: Int){
        textView.text = num.toString()
    }
}