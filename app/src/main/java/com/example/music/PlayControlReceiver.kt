package com.example.music

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by tk on 2019/9/16
 */
class PlayControlReceiver: BroadcastReceiver() {
        companion object {
            val ACTION1 = "PLAY_PREV"//播放上一曲
            val ACTION2 = "PLAY_NEXT"//播放下一曲
            val ACTION3 = "PLAY_OR_PAUSE"//继续播放或暂停
        }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("testbro", "收到广播")

        when (intent?.action) {
                ACTION1 -> PlayManger.playPreVious()
                ACTION2 -> PlayManger.playNext()
                ACTION3 -> {
                    if (PlayManger.player.isPlaying) {
                        PlayManger.pause()
                    } else {
                        PlayManger.resume()
                    }
                }
            }

    }
}