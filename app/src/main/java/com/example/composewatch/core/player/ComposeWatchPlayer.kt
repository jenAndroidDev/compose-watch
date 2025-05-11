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

    private const val minBufferedDuration = 2_000
    private const val maxBufferedDuration = 3_000
    private const val bufferForPlayBackMs = 2_000
    private const val bufferForRebufferingMs =2_000


    private  var player: Player?=null

    fun createPlayer(context: Context): Player {
        if (player==null){
           val loadControl =  DefaultLoadControl.Builder(
            ).setBufferDurationsMs(
                minBufferedDuration,
                maxBufferedDuration,
                bufferForPlayBackMs,
                bufferForRebufferingMs
            )
            player = ExoPlayer.Builder(context).apply {
                setLoadControl(loadControl.build())
                setPauseAtEndOfMediaItems(true)
            }.build()
        }
        return player as Player
    }
}