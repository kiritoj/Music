package com.example.music
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.music.model.db.table.LocalMusic
import com.example.music.util.downloadutil.DownloadLintener
import com.example.music.util.downloadutil.DownloadTask
import org.jetbrains.anko.toast
import java.io.File

class DownloadService : Service() {


    lateinit var manger: NotificationManager
    lateinit var builder: NotificationCompat.Builder
    var downloadTask: DownloadTask? = null
    //下载监听器
    val listener = object : DownloadLintener{
        override fun onProgress(progress: Int) {
            manger.notify(2,getNotification("正在下载",progress))
        }

        override fun onCancle() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onFailed() {
            downloadTask = null
            stopForeground(true)
            toast("凉凉，下载失败了")
        }

        override fun onPause() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSuccess() {
            downloadTask = null
            stopForeground(true)
            //重置标志位，下次进入本地歌曲活动重新扫描本地歌曲
            PreferenceManager.getDefaultSharedPreferences(MusicApp.context).edit().apply {
                putBoolean("hasGetFromLocal", false)
                apply()
            }

            toast("下载完成,文件存放在根目录下,请自行重命名后重新扫描")
        }
    }

    override fun onBind(intent: Intent): IBinder {
       return DownloadBinder()
    }

    override fun onCreate() {
        super.onCreate()
        manger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("download","下载通知",NotificationManager.IMPORTANCE_DEFAULT)
            builder = NotificationCompat.Builder(this,"download")
        }else{
            builder = NotificationCompat.Builder(this)
        }

    }

    internal inner class DownloadBinder : Binder() {
        fun startDownload(url: String,name: String) {
            Log.d("binder","开始下载")
            if (downloadTask == null) {
                downloadTask = DownloadTask(listener)
                downloadTask?.execute(url,name)
                startForeground(2, getNotification("正在下载", 0))
                Toast.makeText(this@DownloadService, "正在下载", Toast.LENGTH_SHORT).show()

            }
        }

    }

    fun getNotification(title: String,process: Int): Notification{
        builder.apply {
            setSmallIcon(R.mipmap.ic_music)
            setContentTitle("正在下载")
            setWhen(System.currentTimeMillis())
            setContentText("${process}%")
            setProgress(100,process,false)
        }
        return builder.build()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun Service.createNotificationChannel(channerId: String, channelName: String, importance: Int) {
        val channel = NotificationChannel(channerId, channelName, importance)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}
