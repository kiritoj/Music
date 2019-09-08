package com.example.music.view.activity.customveiw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Scroller
import com.example.music.util.LrcUtil
import com.example.music.R
import com.example.music.model.db.bean.LrcBean

/**
 * Created by tk on 2019/8/28
 * 自定义歌词控件
 * constructor( context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
 */
class LrcView: View {
    val TAG = "LrcView"

    constructor(context: Context?):this(context,null)
    constructor(context: Context?,attrs: AttributeSet?):this(context, attrs,0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        init(context, attrs)
    }





    //绘制正在播放的歌词
    lateinit var sPaint: Paint
    //绘制普通歌词
    lateinit var nPaint: Paint
    //歌词列表
    val list = ArrayList<LrcBean>()
    //播放器
    lateinit var player: MediaPlayer
    //当前显示的歌词位置
    var currentIndex: Int = 0
    //滑动
    lateinit var scroller: Scroller

    fun init(context: Context?,attrs: AttributeSet?){
        val typeArray = context?.obtainStyledAttributes(attrs,R.styleable.LrcView)

        sPaint = Paint()
        nPaint = Paint()
        sPaint.apply {
            isAntiAlias = true
            color = typeArray?.getColor(R.styleable.LrcView_s_color,Color.WHITE)!!
            textSize = 36f
            //设置绘制文字的锚点为中心点
            textAlign = Paint.Align.CENTER
        }
        nPaint.apply {
            isAntiAlias = true
            color = typeArray?.getColor(R.styleable.LrcView_n_color,Color.GRAY)!!
            textSize = 36f
            textAlign = Paint.Align.CENTER
        }
        currentIndex = 0
        scroller = Scroller(context)
        typeArray?.recycle()
    }

    /**
     * 设置歌词文本
     */
    fun setLrc(text: String){
        list.clear()
        list.addAll(LrcUtil.analyzeLrc(text))
        Log.d(TAG,"setLrc")
    }

    /**
     * 绑定播放器
     */
    fun bindPlayer(mPlayer: MediaPlayer){
        player = mPlayer
       // 最后一句歌词的结束时间直到播放完毕

        invalidate()
        Log.d(TAG,"bindPlayer")
    }

    override fun onDraw(canvas: Canvas?) {
        if (list.isNullOrEmpty()){
            canvas?.drawText("暂无歌词",width/2f,height/2f,nPaint)
            return
        }

        // 绘制静态歌词
        if (currentIndex > -1 && player.currentPosition > list[currentIndex].endTime) {
            scroller.startScroll(0,currentIndex*110,0,110,1000)
        }
        getCurrentIndex(player.currentPosition)
        for (i in 0 until list.size) {
            if (currentIndex == i) {
                //绘制高亮歌词
                if (list[i].hasTranslation) {
                    //如果有翻译歌词需绘制两行
                    canvas?.drawText(list[i].text[1], width / 2f, height / 2f + 110 * i, sPaint)
                    canvas?.drawText(list[i].text[0], width / 2f, height / 2f + 110 * i + 35, sPaint)
                } else {
                    canvas?.drawText(list[i].text[0], width / 2f, height / 2f + 80 * i, sPaint)
                }
            } else {
                //绘制普通歌词
                if (list[i].hasTranslation) {
                    //如果有翻译歌词需绘制两行
                    canvas?.drawText(list[i].text[1], width / 2f, height / 2f + 110 * i, nPaint)
                    canvas?.drawText(list[i].text[0], width / 2f, height / 2f + 110 * i + 35, nPaint)
                } else {
                    canvas?.drawText(list[i].text[0], width / 2f, height / 2f + 80 * i, nPaint)
                }
            }
        }



        postInvalidateDelayed(1)

    }

    /**
     * 获取当前正在播放哪段歌词
     * @param currentPosition player当前播放进度
     */
    fun getCurrentIndex(currentPosition: Int){
        if (currentPosition < list[0].startTime){
            currentIndex = -1
        }else{
           for (i in 0  until list.size){
               if (currentPosition >= list[i].startTime && currentPosition < list[i].endTime){
                   currentIndex = i
                   break
               }
           }
        }
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.currX,scroller.currY)
            postInvalidate()
        }
    }
}