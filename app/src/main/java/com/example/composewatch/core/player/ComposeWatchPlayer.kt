package com.example.composewatch.core.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.foundation.content.MediaType
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.ExoPlayer
@OptIn(UnstableApi::class)

object ComposeWatchPlayerFactory {

    @Synchronized
    fun createPlayer(context: Context,url:String): ExoPlayer {
        val player = ExoPlayer.Builder(context).apply {
        }.build()
        return player
    }
}