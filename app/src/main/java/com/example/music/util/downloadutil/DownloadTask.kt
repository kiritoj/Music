package com.example.music.util.downloadutil

import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile

/**
 * Created by tk on 2019/9/18
 */
class DownloadTask(val listener: DownloadLintener) : AsyncTask<String,Int,Int>(){
    private val ON_SUCCESS = 0
    private val ON_FAILED = 1
    private val ON_PAUSED = 2
    private val ON_CANCLE = 3
    private var isPause = false//是否暂停？
    private var isCancle = false//是否取消？
    private var lastlenghth: Int = 0
    val TAG = "DownloadTask"



    override fun doInBackground(vararg strings: String): Int? {
        val file: File//保存下载的文件
        var accessFile: RandomAccessFile? = null//向file中写入
        var input: InputStream? = null
        var filelengh: Long = 0//已下载的文件字节长度
        var contentLength: Long = 0//待下载文件总长度
        val downloadurl = strings[0]//获取下载链接
        val fileName = downloadurl.substring(downloadurl.lastIndexOf("/"))//获取文件名
        val downloadPath = Environment
            .getExternalStorageDirectory()
            .path+"/"//文件的存储目录为系统的Download目录下
        file = File(downloadPath + strings[1] + ".mp3")
        contentLength = getContentLengh(downloadurl)

        if (file.exists()) {
            //获取已下载的文件长度，便于后续断点下载
            filelengh = file.length()
        }
        if (contentLength == 0L) {
            //如果下载文件长度为零，下载失败
            Log.d(TAG,"文件长度")
            return ON_FAILED
        } else if (filelengh == contentLength) {
            return ON_SUCCESS
        }
        val client = OkHttpClient()
        val request = Request.Builder().addHeader("RANGE", "bytes=$filelengh-$contentLength")
            .url(downloadurl)
            .build()
        try {
            //写入文件，由于RandomAccessFile只能通过字节写入，用InputStream写入字节数组中，在将字节数组写入文件
            val response = client.newCall(request).execute()
            if (response != null) {
                accessFile = RandomAccessFile(file, "rw")
                accessFile.seek(filelengh)
                var totle = 0//模卡吗已下载的文件长度，用于计算下载进度
                var len: Int//返回每次从字节流读取的字节数
                input = response.body()!!.byteStream()
                val bytes = ByteArray(1024)

                while (true) {//读取数据，直到读到文件末尾
                    len = input!!.read(bytes)
                    if (len == -1){
                        break
                    }
                    if (isPause) {
                        return ON_PAUSED
                    } else if (isCancle) {
                        return ON_CANCLE
                    } else {
                        totle += len
                        accessFile.write(bytes, 0, len)
                        val progress = ((filelengh + totle) * 100 / contentLength).toInt()
                        publishProgress(progress)
                    }
                }
                response.body()!!.close()
                return ON_SUCCESS


            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                //关闭流
                input?.close()
                accessFile?.close()
                if (isCancle && file.exists()) {
                    //取消下载后删除源文件
                    file.delete()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }


        return ON_FAILED
    }


    override fun onProgressUpdate(vararg values: Int?) {
        val progress = values[0]
        if (progress!! > lastlenghth) {
            listener.onProgress(progress)
            lastlenghth = progress
        }

    }

    override fun onPostExecute(integer: Int) {
        when (integer) {
            ON_SUCCESS -> listener.onSuccess()
            ON_FAILED -> listener.onFailed()
            ON_PAUSED -> listener.onPause()
            ON_CANCLE -> listener.onCancle()
            else -> {
            }
        }
    }

    //获取下载文件的大小
    fun getContentLengh(downloadurl: String): Long {

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(downloadurl)
            .build()
        try {

            val response = client.newCall(request).execute()
            if (response != null && response.isSuccessful) {
                val contentLength = response.body()!!.contentLength()
                response.close()
                return contentLength
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return 0
    }

    fun pause() {
        isPause = true
    }

    fun cancle() {
        isCancle = true
    }

}