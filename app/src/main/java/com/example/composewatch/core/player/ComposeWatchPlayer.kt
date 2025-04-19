package com.example.composewatch.core.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

@OptIn(UnstableApi::class)
object ComposeWatchPlayerFactory {

    private  var player: Player?=null
    @Synchronized
    fun createPlayer(context: Context): Player {
        if (player==null){
            player = ExoPlayer.Builder(context).apply {
                setPauseAtEndOfMediaItems(true)
            }.build()
        }
        return player as Player
    }
}