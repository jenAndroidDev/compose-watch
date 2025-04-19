package com.example.composewatch.core.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

@OptIn(UnstableApi::class)
object ComposeWatchPlayerFactory {

    private  var player: ExoPlayer?=null
    @Synchronized
    fun createPlayer(context: Context,url:String): ExoPlayer {
        if (player==null){
            player = ExoPlayer.Builder(context).apply {
                setPauseAtEndOfMediaItems(true)
                setWakeMode(C.WAKE_MODE_LOCAL)
            }.build()
            return player as ExoPlayer
        }else{
            return player as ExoPlayer
        }
        
    }
}