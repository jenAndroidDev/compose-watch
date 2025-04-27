package com.example.composewatch.core.player.utils

import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.LoadControl
import androidx.media3.exoplayer.upstream.Allocator


@UnstableApi
class LoadControlImpl(): LoadControl {
    override fun getAllocator(): Allocator {
        return  DefaultLoadControl.Builder(
       ).build()
           .allocator
    }
}