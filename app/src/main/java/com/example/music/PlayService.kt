package com.example.music

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.NotificationTarget
import com.example.music.databindingadapter.getAlbumArt
import com.example.music.event.*
import com.example.music.view.activity.PlayingActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PlayService : Service() {

    val TAG = "PlayService"
    lateinit var remoteView: RemoteViews
    lateinit var notification: Notification
    lateinit var manger: NotificationManager
    val hasPlayList = false //是否已经接受到播放列表了
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }


    override fun onCreate() {
        EventBus.getDefault().register(this)
        manger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


    @Subscribe
    fun setQuene(event: QueneEvent) {
        Log.d(TAG, "接收到播放列表")
        if (!hasPlayList) {
            //第一次播放开启前台服务
            remoteView = RemoteViews(packageName, R.layout.notification_play_layout)
            //整个前台服务点击跳转至playingactivity
            val intent = Intent(this, PlayingActivity::class.java)
            val pi = PendingIntent.getActivity(this, 0, intent, 0)
            //播放上一首、下一首、播放/暂停,这里通过静态广播的方式让app退出后仍然可以收广播
            val playPreIntent = Intent(PlayControlReceiver.ACTION1)
            val playNextIntent = Intent(PlayControlReceiver.ACTION2)
            val playOrPauseIntent = Intent(PlayControlReceiver.ACTION3)
            playPreIntent.setComponent(ComponentName(this, PlayControlReceiver::class.java))
            playNextIntent.setComponent(ComponentName(this, PlayControlReceiver::class.java))
            playOrPauseIntent.setComponent(ComponentName(this, PlayControlReceiver::class.java))
            val playPrePi = PendingIntent.getBroadcast(this, 0, playPreIntent, 0)
            val playNextPi = PendingIntent.getBroadcast(this, 0, playNextIntent, 0)
            val playOrPausePi = PendingIntent.getBroadcast(this, 0, playOrPauseIntent, 0)
            remoteView.apply {
                setOnClickPendingIntent(R.id.notifi_play_pre, playPrePi)
                setOnClickPendingIntent(R.id.notifi_play_or_pause, playOrPausePi)
                setOnClickPendingIntent(R.id.notifi_play_next, playNextPi)
            }

            //8.0以上创建通知渠道
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("play", "音乐播放", NotificationManager.IMPORTANCE_DEFAULT)
                notification = NotificationCompat.Builder(this, "play")
                    .setContentTitle("标题")
                    .setContentText("内容")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_music)
                    .setContentIntent(pi)
                    .setCustomBigContentView(remoteView)
                    .build()
            } else {
                //android 8.0已下不用设置渠道
                notification = NotificationCompat.Builder(this)
                    .setContentTitle("标题")
                    .setContentText("内容")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_music)
                    .setContentIntent(pi)
                    .setCustomBigContentView(remoteView)
                    .build()

            }
            startForeground(1, notification)

        }
        PlayManger.setPlayQuene(event.list, event.index, event.tag)
    }

    @Subscribe
    fun setMode(event: ModeEvent) {
        PlayManger.setMode(event.mode)
    }

    @Subscribe
    fun setState(event: PlayManger.State) {
        PlayManger.setState(event)
    }

    @Subscribe
    fun setIndex(event: IndexEvent) {
        PlayManger.setPlayIndex(event.index)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun refresh(event: RefreshEvent) {
        Log.d(TAG, "刷新")
        //更新前台服务音乐信息
        remoteView.setTextViewText(R.id.notifi_music_name, event.mSong.songName)
        remoteView.setTextViewText(R.id.notifi_music_singer, event.mSong.singerName)
        val target = NotificationTarget(this, R.id.notifi_music_cover, remoteView, notification, 1)
        when (event.mSong.tag) {
            "LOCAL" -> remoteView.setImageViewBitmap(
                R.id.notifi_music_cover,
                getAlbumArt(event.mSong.albumID!!, MusicApp.context)
            )
            "NET_WITH_URL" ->
                remoteView.setImageViewResource(R.id.notifi_music_cover,R.drawable.back)
            "NET_NON_URL" ->
                Glide.with(this)
                    .asBitmap()
                    .load(event.mSong.coverUrl)
                    .into(target)
        }

        manger.notify(1, notification)


    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun receiveState(event: StateEvent) {
        //更新播放按钮icon
        Log.d(TAG, "音乐状态变化")
        when (event.state) {
            PlayManger.State.PLAY -> remoteView.setImageViewResource(
                R.id.notifi_play_or_pause,
                R.drawable.ic_pause_notifi
            )
            PlayManger.State.PAUSE -> remoteView.setImageViewResource(
                R.id.notifi_play_or_pause,
                R.drawable.ic_play_notifi
            )

        }
        manger.notify(1, notification)
    }

    //android 8.0以上创建通知渠道
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(channerId: String, channelName: String, importance: Int) {
        val channel = NotificationChannel(channerId, channelName, importance)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


    override fun onDestroy() {
        Log.d(TAG, "服务被关闭")
        PlayManger.recycle()
        EventBus.getDefault().unregister(this)
    }
}
