package com.example.composewatch.core.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.LoadControl
import androidx.media3.exoplayer.analytics.PlayerId
import com.example.composewatch.core.player.utils.LoadControlImpl

@OptIn(UnstableApi::class)
object ComposeWatchPlayerFactory {

    private  var player: Player?=null
    @Synchronized
    fun createPlayer(context: Context): Player {
        if (player==null){
            val loadControl = LoadControlImpl().allocator
            player = ExoPlayer.Builder(context).apply {
                setPauseAtEndOfMediaItems(true)
            }.build()
        }
        return player as Player
    }
}